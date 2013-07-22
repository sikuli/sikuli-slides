package org.sikuli.slides;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.uis.MainCommandLine;
import org.sikuli.slides.uis.MainUI;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyScreen;
import org.sikuli.slides.utils.UserPreferencesEditor;
import org.sikuli.slides.utils.Utils;
import org.sikuli.slides.core.SikuliPowerPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 
 * @author Khalid Alharbi
 * Khalid.Alharbi@Colorado.edu
 *
 */

public class Main{
	private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);
	private UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	
	private void initProject(){
		// create the project working directory directory
		if(!Utils.createWorkingDirectory()){
			logger.error("Failed to create sikuli-slides working directory in +"+System.getProperty("java.io.tmpdir"));
			System.exit(1);
		}
		// check default screen
		checkScreen();
		// set the total screen width
		Constants.Total_Screen_Width = MyScreen.getTotalScreenWidth(Constants.ScreenId);
	}
	// check if the value of the screen id is connected.
	private void checkScreen() {
		int numberOfScreens = DesktopScreen.getNumberScreens();
		int storedScreenId = prefsEditor.getDisplayId();
		// if the display is disconnected, update the screen id to the default screen
		if(storedScreenId >= numberOfScreens){
			storedScreenId = 0;
			prefsEditor.putDisplayId(storedScreenId);
		}
		Constants.ScreenId = storedScreenId;
	}

	/**
	 * starts processing the work
	 */
	public void doSikuliPowerPoint(String pptxSourceName){	
		Constants.Execution_Start_Time=System.currentTimeMillis();
		if(pptxSourceName != null){
			// run sikuli-slides work
			SikuliPowerPoint sikuliPowerPoint=new SikuliPowerPoint(pptxSourceName);
			sikuliPowerPoint.runSikuliPowerPoint();
		}
	}
	
	public static void main(String[]args){
		// Set the application name in Mac OS X title bar
		if (System.getProperty("os.name").contains("Mac")){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			// set the name of the application menu item
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sikuli-Slides");
	        // set the look and feel
	        try {
	        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        } 
	        catch (ClassNotFoundException e) {
	                e.printStackTrace();
	        }
	        catch (InstantiationException e) {
	                e.printStackTrace();
	        }
	        catch (IllegalAccessException e) {
	                e.printStackTrace();
	        } 
	        catch (UnsupportedLookAndFeelException e) {
	        	e.printStackTrace();
	        }
		}
		
		Main main=new Main();
		main.initProject();
		
		// if no arguments are passed, run the GUI mode
		if(args.length==0){
			MainUI.runGuiTool();
		}
		// Otherwise, run the command line tool and get the file name
		else{			
			String pptxSourceName=MainCommandLine.runCommandLineTool(args);
			if(pptxSourceName != null){
				main.doSikuliPowerPoint(pptxSourceName);
			}
		}
	}
	
}