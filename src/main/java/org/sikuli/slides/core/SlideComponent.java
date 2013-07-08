/**
Khalid
*/
package org.sikuli.slides.core;

import java.io.File;
import org.sikuli.slides.media.Sound;
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.shapes.SlideShape;

/**
 * A slide component is an object having the necessary components of the
 *  slide such as media files and shapes.
 * @author Khalid
 *
 */
public class SlideComponent {
	private File imageTargetFile;
	private SlideShape slideShape;
	private Screenshot screenshot;
	private SlideTargetRegion slideTargetRegion;
	private Sound sound;
	private SlideShape slideLabel;
	private File imageLabelFile;
	private SlideTargetRegion slideLabelRegion;
	
	public SlideComponent(){
		
	}
	
	public SlideComponent(File imageTargetFile, SlideShape slideShape, Screenshot screenshot, 
			SlideTargetRegion slideTargetRegion, Sound sound,SlideShape slideLabel,
			File imageLabelFile, SlideTargetRegion slideLabelRegion){
		this.setTargetFile(imageTargetFile);
		this.setSlideShape(slideShape);
		this.setScreenshot(screenshot);
		this.setSlideTargetRegion(slideTargetRegion);
		this.setSound(sound);
		this.setSlideLabel(slideLabel);
		this.setLabelFile(imageLabelFile);
		this.setSlideLabelRegion(slideLabelRegion);
		
	}
	
	public File getTargetFile() {
		return imageTargetFile;
	}
	
	public void setTargetFile(File imageTargetFile) {
		this.imageTargetFile = imageTargetFile;
	}
	
	public SlideShape getSlideShape() {
		return slideShape;
	}

	public void setSlideShape(SlideShape slideShape) {
		this.slideShape = slideShape;
	}

	public Screenshot getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(Screenshot screenshot) {
		this.screenshot = screenshot;
	}

	public SlideTargetRegion getSlideTargetRegion() {
		return slideTargetRegion;
	}

	public void setSlideTargetRegion(SlideTargetRegion slideTargetRegion) {
		this.slideTargetRegion = slideTargetRegion;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}
	
	public SlideShape getSlideLabel() {
		return slideLabel;
	}

	public void setSlideLabel(SlideShape slideLabel) {
		this.slideLabel = slideLabel;
	}

	public File getLabelFile() {
		return imageLabelFile;
	}

	public void setLabelFile(File imageLabelFile) {
		this.imageLabelFile = imageLabelFile;
	}

	public SlideTargetRegion getSlideLabelRegion() {
		return slideLabelRegion;
	}

	public void setSlideLabelRegion(SlideTargetRegion slideLabelRegion) {
		this.slideLabelRegion = slideLabelRegion;
	}
	
	@Override
	public String toString(){
		return "Slide Component: "+slideShape+"\n"+screenshot+"\n"+
				slideTargetRegion+"\n"+sound+"\n"+"label: "+slideLabel;
	}
}
