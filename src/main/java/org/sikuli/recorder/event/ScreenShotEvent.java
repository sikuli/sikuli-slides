package org.sikuli.recorder.event;

import java.awt.image.BufferedImage;
import java.io.File;

import com.google.common.base.Objects;

public class ScreenShotEvent extends Event {

	private BufferedImage image;
	private File imageFile;
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public void setFile(File file){
		this.imageFile = file;
	}
	
	public File getFile(){
		return imageFile;
	}
	
	public static ScreenShotEvent createFromFile(File f) {
		ScreenShotEvent s = new ScreenShotEvent();
		s.setFile(f);
		return s;
	}
	
	@Override
	public String toString() {
	    return Objects.toStringHelper(this.getClass())
	    		.add("file", imageFile)
	    		.add("image", image)
	    		.toString();
	}

}