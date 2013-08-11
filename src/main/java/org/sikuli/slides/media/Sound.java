/**
Khalid
*/
package org.sikuli.slides.media;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.jsoundsystem.JSound;
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
		try {
			JSound sound = new JSound(slideMediaLocation);
			sound.play();
		}
		catch (UnsupportedAudioFileException e) {
			logger.error("Error in playing the audio file: Unsupported audio file format");
		} catch (IOException e) {
			logger.error("Error in playing the audio file: Failed I/O operation.");
		}
	}
	public String toString(){
		return "Sound info:\n" +"name:"+name+"\n file name:"+fileName+
				"\n relationshipID="+relationshipId;
	}
	
}
