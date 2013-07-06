package org.sikuli.slides;

import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Utils;
import org.sikuli.slides.core.SikuliPowerPoint;
import org.sikuli.slides.guis.MainCommandLine;
import org.sikuli.slides.guis.MainUI;
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
	  
	private void initProject(){
		// create the project working directory directory
		if(!Utils.createWorkingDirectory()){
			logger.error("Failed to create sikuli-slides working directory in +"+System.getProperty("java.io.tmpdir"));
			System.exit(1);
		}
	}

	/**
	 * starts processing the work
	 */
	public void doSikuliPowerPoint(File file){	
		Constants.Execution_Start_Time=System.currentTimeMillis();
		if(file!=null){
			// run sikuli-slides work
			SikuliPowerPoint sikuliPowerPoint=new SikuliPowerPoint(file);
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
			String FileName=MainCommandLine.runCommandLineTool(args);
			if(FileName!=null){
				main.doSikuliPowerPoint(new File(FileName));
			}
		}
	}
	
}