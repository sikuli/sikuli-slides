
package org.sikuli.slides.uis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.core.SikuliAction;
import org.sikuli.slides.listeners.tutorials.Observable;
import org.sikuli.slides.listeners.tutorials.Observer;
import org.sikuli.slides.uis.TutorialConrollerUI;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyScreen;
import org.sikuli.slides.utils.UserPreferencesEditor;
import org.sikuli.slides.utils.Constants.NavigationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Tutorial worker that processes users' interaction events when running the slides in tutorial mode.
 * This includes observing the controller UI to switch to the previous and next step in the tutorial mode. 
 * 
 * @author Khalid Alharbi
 *
 */

public class TutorialWorker extends SwingWorker<Boolean, Integer> implements Observer, Observable{
	private static final Logger logger = (Logger) LoggerFactory.getLogger(TutorialWorker.class);
	private UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	private static AtomicInteger currentStep=null;
	private List<SikuliAction> tasks=null;
	private final JLabel currentSlideLabel;
	private TutorialConrollerUI observable;
	private ArrayList<Observer> observers;
	private NavigationStatus navigationStatus;
	
	public TutorialWorker(List<SikuliAction> tasks,JLabel currentSlideLabel,TutorialConrollerUI observable){
		this.tasks=tasks;
		this.currentSlideLabel=currentSlideLabel;
		currentStep=new AtomicInteger(-1);
		this.observable=observable;
		this.observable.register(this);
		this.observers = new ArrayList<Observer>();
	}
	
	@Override
	protected Boolean doInBackground(){
		// Update the status. It sends data chunks to the process() method.
		publish(currentStep.get()+2);
		// run the first step
		doNextStep();
		
		return true;
	}

	@Override
	protected void done(){
	}
	@Override
	protected void process(List<Integer> stepsChunks){
		for(Integer step:stepsChunks){
			currentSlideLabel.setText((step).toString());
		}
	}
	
	private void doNextStep(){
		if(hasNextStep()){
			publish(currentStep.incrementAndGet()+1);
			tasks.get(currentStep.get()).doSikuliAction();
			doNextStep();
		}
	}
	
	/**
	 * Returns true if the presentation slides have more forward steps
	 * @return true if the presentation slides have more forward steps
	 */
	private boolean hasNextStep(){
		if(currentStep.get()<tasks.size()-1){
			return true;
		}
		else{
			logger.info("There are no next steps to move forward to.");
			showCompleteNotification();
			return false;
		}
	}

	/**
	 * Returns true if the presentation slides have more backward steps
	 * @return true if the presentation slides have more backward steps
	 */
	private boolean hasPreviousStep(){
		if(currentStep.get()>0){
			return true;
		}
		else{
			logger.info("There are no previous steps to go back to.");
			return false;
		}
	}

	@Override
	public void update(NavigationStatus navigationStatus) {
		// Don't skip steps when wait action is being running
		//TODO: disable UI next, previous buttons instead of using this flag
		if(Constants.IsWaitAction){
			return;
		}
		
		if(navigationStatus.equals(NavigationStatus.NEXT)){
			if(hasNextStep()){
				Constants.IsNextStep=true;
			}
		}
		else if(navigationStatus.equals(NavigationStatus.PREVIOUS)){
			if(hasPreviousStep()){
				Constants.IsPreviousStep=true;
				currentStep.decrementAndGet();
				currentStep.decrementAndGet();
			}
		}
	}
	
	private void showCompleteNotification() {
		Dimension screenDimension = MyScreen.getScreenDimensions();
		int x = (int) (screenDimension.getWidth() / 2.0);
		int y = (int) (screenDimension.getHeight() / 2.0);
		ScreenRegion fullScreenRegion = new DesktopScreenRegion(Constants.ScreenId,
				200,200,x, y);
		Canvas canvas = new ScreenRegionCanvas(new DesktopScreenRegion(Constants.ScreenId));
		canvas.addLabel(fullScreenRegion, 
				"Great job! You have successfully completed this tutorial.")
				.withFontSize(prefsEditor.getInstructionHintFontSize());
		canvas.display(prefsEditor.getLabelDisplayTime());
		navigationStatus = NavigationStatus.DONE;
		notifyObserver();
	}
	
	@Override
	public void register(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void unregister(Observer observer) {
		int index = observers.indexOf(observer);
		observers.remove(index);
	}

	@Override
	public void notifyObserver() {
		for(final Observer observer : observers){
			observer.update(navigationStatus);
		}
	}
}
