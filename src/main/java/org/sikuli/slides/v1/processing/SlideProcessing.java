/**
Khalid
*/
package org.sikuli.slides.v1.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SlideProcessing {
	BufferedImage originalScreenshot;
	public SlideProcessing(String screenshotLocation){

		try {
			originalScreenshot = ImageIO.read(new File(screenshotLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getScreenshotHeight(){
		return originalScreenshot.getHeight();
	}
	
	public int getScreenshotWidth(){
		return originalScreenshot.getWidth();
	}
	
}
