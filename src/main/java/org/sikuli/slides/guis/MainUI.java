package org.sikuli.slides.guis;

import java.io.File;
import org.sikuli.slides.utils.Utils;
import org.sikuli.slides.core.SikuliPowerPoint;


/**
 * 
 * @author Khalid Alharbi
 * Khalid.Alharbi@Colorado.edu
 *
 */

public class MainUI{
	
	  
	private void intiProject(){
		// create the project working directory directory
		if(!Utils.createWorkingDirectory()){
			System.err.println("Failed to create sikuli-slides working directory.");
			System.exit(1);
		}
	}

	/**
	 * starts processing the work
	 */
	private void doSikuliPowerPoint(File file){	
		if(file!=null){
			// run sikuli-slides work
			SikuliPowerPoint sikuliPowerPoint=new SikuliPowerPoint(file);
			sikuliPowerPoint.runSikuliPowerPoint();
		}
	}
	
	public static void main(String[]args){
		MainUI mainUi=new MainUI();
		mainUi.intiProject();
		// run the command line tool and get the file name
		String FileName=MainCommandLine.runCommandLineTool(args);
		if(FileName!=null)
			mainUi.doSikuliPowerPoint(new File(FileName));
	}
	
}