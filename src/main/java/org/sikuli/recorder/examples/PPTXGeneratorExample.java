package org.sikuli.recorder.examples;

import java.io.File;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.recorder.Recorder;
import org.sikuli.recorder.pptx.PPTXGenerator;

public class PPTXGeneratorExample {
	
	public static void main(String[] args) {
		
		File eventDir = new File("tmp/example");
		eventDir.mkdirs();
		PPTXGenerator.generate(eventDir, new File("t.pptx"));
		
	}
}
