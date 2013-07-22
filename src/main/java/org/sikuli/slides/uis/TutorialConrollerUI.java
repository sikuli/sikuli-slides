/**
Khalid
*/
package org.sikuli.slides.uis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.sikuli.slides.core.SikuliAction;
import org.sikuli.slides.listeners.tutorials.Observable;
import org.sikuli.slides.listeners.tutorials.Observer;
import org.sikuli.slides.sikuli.TutorialWorker;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyScreen;
import org.sikuli.slides.utils.Constants.NavigationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialConrollerUI extends JFrame implements ActionListener, Observable, Observer{
	
	private static final long serialVersionUID = -7849896402606865992L;
	private static final Logger logger = (Logger) LoggerFactory.getLogger(TutorialConrollerUI.class);
	
	private JButton previousButton;
	private JButton nextButton;
	private JLabel totalSlidesLabel;
	private JLabel currentSlideLabel;
	
	private TutorialWorker tutorialWorker;
	private ArrayList<Observer> observers;
	private NavigationStatus navigationStatus; // indicates next or previous navigation direction
	
	public TutorialConrollerUI(){
		super("sikuli-slides");
		observers = new ArrayList<Observer>();
	}
	
	private void createAndShowGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.black);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.black);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.white);
		
		ImageIcon previousIcon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"previous-icon.png"));
		previousButton=new JButton("previous",previousIcon);
		previousButton.setMargin(new Insets(2, 5, 5, 5));
		previousButton.addActionListener(this);
		
		ImageIcon nextIcon=new ImageIcon(MainUI.class.getResource(Constants.RESOURCES_ICON_DIR+"next-icon.png"));
		nextButton=new JButton("next    ",nextIcon);
		nextButton.setMargin(new Insets(2, 5, 5, 5));
		nextButton.addActionListener(this);
		
		
		leftPanel.add(previousButton,BorderLayout.EAST);
		leftPanel.add(nextButton,BorderLayout.WEST);
		
		Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		
		currentSlideLabel=new JLabel("1",JLabel.CENTER);
		currentSlideLabel.setBorder(paddingBorder);
		
		totalSlidesLabel= new JLabel("/     "+Constants.Steps_Total,JLabel.LEFT);
		totalSlidesLabel.setBorder(paddingBorder);
		
		rightPanel.add(currentSlideLabel,BorderLayout.EAST);
		rightPanel.add(totalSlidesLabel,BorderLayout.WEST);	
		
		panel.add(leftPanel,BorderLayout.WEST);
		panel.add(rightPanel,BorderLayout.EAST);
		add(panel);
		
        setTitle("Sikuli-Slides - Tutorial Controller");
        Dimension fullScreenDimension=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension preferedDimension=getPreferredSize();
        setSize(fullScreenDimension.width, preferedDimension.height+20);
        
        Rectangle screenBounds = MyScreen.getScreenBounds();
        int x = (int) screenBounds.getMaxX() - getWidth();
        int y = (int) screenBounds.getMaxY() - getHeight();
        
        setLocationByPlatform(false);
        setLocation(x, y);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
	}
	private void runTasks(List<SikuliAction> tasks){
		// run the tutorial tasks in the background using a worker thread and pass the observable object to the observers :)
		tutorialWorker= new TutorialWorker(tasks, currentSlideLabel, this);
		this.tutorialWorker.register(this);
		tutorialWorker.execute();
		try {
			tutorialWorker.get();
		} catch (InterruptedException e) {
			logger.error("Error: Unexpected error in running tutorial mode.");
		} catch (ExecutionException e) {
			logger.error("Error: Unexpected error in running tutorial mode.");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==previousButton){
			changeNavigationStatus(NavigationStatus.PREVIOUS);
		}
		else if(e.getSource()==nextButton){
			changeNavigationStatus(NavigationStatus.NEXT);
		}
	}

	public static void runTutorialUIAndTasks( final List<SikuliAction> tasks){
		Constants.Steps_Total=tasks.size();
		TutorialConrollerUI tutorialConrollerUI=new TutorialConrollerUI();
		tutorialConrollerUI.createAndShowGUI();
		tutorialConrollerUI.runTasks(tasks);
	}
	
	private void changeNavigationStatus(NavigationStatus navigationStatus){
		this.navigationStatus=navigationStatus;
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
	
	@Override
	public void update(NavigationStatus navigationStatus) {
		if(navigationStatus.equals(NavigationStatus.DONE)){
			System.exit(0);
		}
	}

}

