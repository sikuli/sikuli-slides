/**
Khalid
*/
package org.sikuli.slides.sikuli;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sikuli.slides.core.SikuliAction;
import org.sikuli.slides.utils.Constants;

/**
 * A Singleton TutorialController class that processes users' interaction events when running the slides in tutorial mode.
 * This includes using the controller UI to switch to the previous and next step in the tutorial mode. 
 * 
 * @author Khalid
 *
 */
// uses the Singleton design pattern to ensure that only one instance of a class is created.
public class TutorialController {
	private static TutorialController instance = null;
	private static AtomicInteger currentStep=null;
	private List<SikuliAction> tasks=null;
	
	private TutorialController(){
		// Exists to prevent instantiation. 
	}
	public void executeSikuliActions(List<SikuliAction> tasks){
		this.tasks=tasks;
		// run the first step
		tasks.get(0).doSikuliAction();
		// run the remaining steps
		doNextStep();
	}
	public static TutorialController getInstance(){
		if(instance==null){
			instance=new TutorialController();
			currentStep=new AtomicInteger(0);
		}
		return instance;
	}
	
	public SikuliAction getStepAction(int step){
		SikuliAction sikuliAction=null;
		try{
			if(tasks!=null){
				sikuliAction = tasks.get(step);
			}
		}
		catch(IndexOutOfBoundsException e){
			System.err.println("Error in interactive tutorial mode.");
		}
		return sikuliAction;
	}
	
	private void doPreviousStep(){
		if(hasPreviousStep()){
			SikuliAction shapeAction=tasks.get(currentStep.decrementAndGet());
			shapeAction.doSikuliAction();
			// keep performing the next step
			doNextStep();
		}

	}
	public void doNextStep(){
		if(Constants.IsReturnToPreviousStep){
			Constants.IsReturnToPreviousStep=false;
			doPreviousStep();
		}
		else if(hasNextStep()){
			SikuliAction shapeAction=tasks.get(currentStep.incrementAndGet());
			shapeAction.doSikuliAction();
			// keep performing the next step
			doNextStep();
		}
	}
	/**
	 * Returns true if the presentation slides have more forward steps
	 * @return true if the presentation slides have more forward steps
	 */
	public boolean hasNextStep(){
		if(currentStep.get()<tasks.size()-1){
			return true;
		}
		else{
			System.out.println("There are no next steps to move forward to.");
			return false;
		}
	}
	
	/**
	 * Returns true if the presentation slides have more backward steps
	 * @return true if the presentation slides have more backward steps
	 */
	public boolean hasPreviousStep(){
		if(currentStep.get()>0){
			return true;
		}
		else{
			System.out.println("There are no previous steps to go back to.");
			return false;
		}
	}
}
