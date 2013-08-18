package org.sikuli.slides.api.generators;

import java.io.File;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;

class GeneratedProgram {

	ScreenRegion screenRegion = new DesktopScreenRegion();
	Mouse mouse = new DesktopMouse();
	Keyboard keyboard = new DesktopKeyboard();

	
	// TODO: modify these settings if the default settings don't work for you
	public static int DEFAULT_WAIT_TIME = 5000;
	public static float DEFAULT_MINSCORE = 0.7f;
	
	// TODO: modify this path if you moved image files to another location
	public static String DEFAULT_IMAGE_DIRECTORY =  "images";
	
	public File findImageByName(String name){
		return new File(DEFAULT_IMAGE_DIRECTORY + File.pathSeparator + name);
	}

	// Source: Slide 1 
	// Action: click
	// Argument: 
	public boolean step1() {
	    	Target target = new ImageTarget(new File("image1.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);
	    	if (loc != null){
	    		mouse.click(loc.getCenter());
	    		return true;
	    	}else{
	    		return false;
	    	}

	}


	// Source: Slide 2 
	// Action: rightClick
	// Argument: 
	public boolean step2() {
	    	Target target = new ImageTarget(new File("image2.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);
	    	if (loc != null){
	    		mouse.rightClick(loc.getCenter());
	    		return true;
	    	}else{
	    		return false;
	    	}

	}


	// Source: Slide 3 
	// Action: type
	// Argument: something to type
	public boolean step3() {
	    	Target target = new ImageTarget(new File("image3.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);
	    	if (loc != null){
	    		mouse.click(loc.getCenter());
	    		keyboard.type("something to type");
	    		return true;
	    	}else{
	    		return false;
	    	}

	}


	// Source: Slide 4 
	// Action: BrowserAction
	// Argument: 
	public boolean step4() {
	    	// no action is generated for this step
	    	return true;
	}


	// Source: Slide 5 
	// Action: exist
	// Argument: 
	public boolean step5() {
	    	Target target = new ImageTarget(new File("image5.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);	
	    	return loc != null;

	}


	// Source: Slide 6 
	// Action: notExist
	// Argument: 
	public boolean step6() {
	    	Target target = new ImageTarget(new File("image6.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);	
	    	return loc == null;
	}


	// Source: Slide 7 
	// Action: type
	// Argument: something to type
	public boolean step7() {
	    	Target target = new ImageTarget(new File("image7.png"));
	    	target.setMinScore(DEFAULT_MINSCORE);
	    	ScreenRegion loc = screenRegion.find(target);
	    	if (loc != null){
	    		mouse.click(loc.getCenter());
	    		keyboard.type("something to type");
	    		return true;
	    	}else{
	    		return false;
	    	}

	}


	public void executeAll(){

		boolean success1 = step1();
		boolean success2 = step2();
		boolean success3 = step3();
		boolean success4 = step4();
		boolean success5 = step5();
		boolean success6 = step6();
		boolean success7 = step7();

		// TODO: Add code to handle the return value of each step
	}

	static public void main(String... args){
		GeneratedProgram prog = new GeneratedProgram();
		prog.executeAll();
	} 
}