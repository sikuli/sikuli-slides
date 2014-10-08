package org.sikuli.recorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class KeyboardEventWriter {

	static Logger logger = LoggerFactory.getLogger(KeyboardEventWriter.class);
	static BufferedWriter writer = null;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");	
	String name = "";	
	static public String getTimeStamp(){
		return sdf.format(new Date());	
	}

	private File outputDir;
	public KeyboardEventWriter(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		name = sdf.format(new Date());	
		name = name + ".txt";
		outputDir = new File(name);
	}

	public void write(List<NativeKeyEvent> keyEvents) {
		//System.out.println(getEventDir() + "/" + event.getClass());
		logger.trace("Wrote to file --> " + outputDir.getAbsolutePath());
		String keys = "";
		for(int i=0; i<keyEvents.size(); i++){
			if(i==0){
				keys = "";
			}
			else if(keyEvents.get(i).getKeyCode() == NativeKeyEvent.VK_ENTER){
				keys = keys + "\n ";
			}
			else
				keys = keys + keyEvents.get(i).getKeyChar();
		}
		try {
			Files.write(keys, outputDir, Charsets.US_ASCII);
		} catch (IOException e) {
		} 
	}

	public File getEventDir() {
		return outputDir;
	}

	public void setEventDir(File outputDir) {
		this.outputDir = outputDir;
	}
}