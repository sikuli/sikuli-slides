/**
Khalid
*/
package org.sikuli.slides.media;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.sikuli.api.audio.DesktopSpeaker;
import org.sikuli.api.audio.Speaker;
import org.sikuli.slides.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sound {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(Sound.class);
	private String name;
	private String fileName;
	private String relationshipId;
	
	public Sound(){
		
	}
	public Sound (String name, String fileName, String relationshipId){
		this.name=name;
		this.fileName=fileName;
		this.relationshipId=relationshipId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	public void playSound(){
		String slideMediaLocation=Constants.projectDirectory+Constants.MEDIA_DIRECTORY+File.separator+getFileName();
			Speaker speaker = new DesktopSpeaker();
			try {
				speaker.play(new URL("file://"+slideMediaLocation));
			} catch (MalformedURLException e) {
				logger.error("Unknown audio location..");
			}
	}
	public String toString(){
		return "Sound info:\n" +"name:"+name+"\n file name:"+fileName+
				"\n relationshipID="+relationshipId;
	}
	
}
