package org.sikuli.slides;

import java.util.Arrays;

import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.slides.uis.MainCommandLine;
import org.sikuli.slides.uis.MainUI;
import org.sikuli.slides.v1.core.RunOptions;
import org.sikuli.slides.v1.core.SikuliPowerPoint;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.MyScreen;
import org.sikuli.slides.v1.utils.UserPreferencesEditor;
import org.sikuli.slides.v1.utils.Utils;
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
	public void doSikuliPowerPoint(RunOptions runOptions){	
		Constants.Execution_Start_Time=System.currentTimeMillis();
		if(runOptions != null){
			// run sikuli-slides work
			SikuliPowerPoint sikuliPowerPoint=new SikuliPowerPoint(runOptions.getSourceName());
			sikuliPowerPoint.runSikuliPowerPoint(runOptions.getStart(), runOptions.getEnd());
		}
	}
	
	public static void showHelp(){
		String helpMessage =
		"Sikuli Slides commands:\n\n"+ 
		"     execute         execute slides\n" +
		"     record          record actionsas slides\n" +
		"     gui             launch GUI";
		System.out.println(helpMessage);

	}

	public static void main(String[]args){
		// Set the application name in Mac OS X title bar
		if (System.getProperty("os.name").contains("Mac")){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			// set the name of the application menu item
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sikuli-Slides");
			// set the look and feel
//			try {
//				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			} 
//			catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			catch (InstantiationException e) {
//				e.printStackTrace();
//			}
//			catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} 
//			catch (UnsupportedLookAndFeelException e) {
//				e.printStackTrace();
//			}
		}								

		Main main=new Main();
		main.initProject();
		
		// if arguments are passed
		if (args.length >= 1){
			String command = args[0];	
			String[] otherArgs;
			if (args.length >= 2){
				otherArgs = Arrays.copyOfRange(args,1,args.length);
			}else{
				otherArgs = new String[]{};
			}
			if (command.compareToIgnoreCase("execute") == 0){
				RunOptions runOptions = MainCommandLine.runCommandLineTool(otherArgs);
				if(runOptions != null){
					main.doSikuliPowerPoint(runOptions);					
				}				
			}else if (command.compareToIgnoreCase("gui") == 0){
				MainUI.runGuiTool();			
			}else if (command.compareToIgnoreCase("record") == 0){
				//RecorderMain.main(otherArgs);				
			}else{
				System.err.println("[" + command + "] is not a valid command");
				showHelp();
			}
		}else{
			showHelp();
		}
	}

}