package org.sikuli.recorder.examples;

import java.io.File;

import org.sikuli.recorder.html.HTMLGenerator;

public class HTMLGeneratorExample {
	
	public static void main(String[] args) {
		
		File eventDir = new File("tmp/example");
		eventDir.mkdirs();
		HTMLGenerator.generate(eventDir, new File("tmp/html"));
		
	}
}
