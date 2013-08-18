package org.sikuli.recorder.examples;

import java.io.File;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.recorder.Recorder;
import org.sikuli.recorder.Utils;

public class RecordScreenRegionExample {
	
	public static void main(String[] args) {
		
		File eventDir = new File("tmp/example");
		if (eventDir.exists()){
			Utils.deleteFilesInFolder(eventDir);
		}else{
			eventDir.mkdirs();
		}
		
		Recorder recorder = new Recorder();
		//recorder.setRegionOfInterest(new DesktopScreenRegion(5,30,300,800));
		recorder.setEventDir(eventDir);
		recorder.start();
		
		
	}
}
