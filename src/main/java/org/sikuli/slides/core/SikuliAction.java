/**
Khalid
*/
package org.sikuli.slides.core;

import java.io.File;
import org.sikuli.slides.media.Sound;
import org.sikuli.slides.screenshots.Screenshot;
import org.sikuli.slides.screenshots.SlideTargetRegion;
import org.sikuli.slides.shapes.SlideShape;
import org.sikuli.slides.utils.Constants.DesktopEvent;

public class SikuliAction{
	private SlideComponent slideComponent;
	private SlideShape slideShape;
	private DesktopEvent desktopEvent;
	public SikuliAction(File imageTargetFile, SlideShape slideShape, Screenshot screenshot, 
			SlideTargetRegion slideTargetRegion,DesktopEvent desktopEvent, Sound sound, SlideShape label,
			File imageLabelFile, SlideTargetRegion slideLabelRegion){
		this.slideShape=slideShape;
		this.desktopEvent=desktopEvent;
		slideComponent=new SlideComponent(imageTargetFile, 
				slideShape, screenshot, slideTargetRegion, sound, label,
				imageLabelFile, slideLabelRegion);
	}
	public void doSikuliAction(){
		slideShape.doSikuliAction(slideComponent,desktopEvent);
	}
}