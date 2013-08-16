/**
Khalid
*/
package org.sikuli.slides.v1.core;

import java.io.File;

import org.sikuli.slides.v1.media.Sound;
import org.sikuli.slides.v1.screenshots.Screenshot;
import org.sikuli.slides.v1.screenshots.SlideTargetRegion;
import org.sikuli.slides.v1.shapes.SlideShape;
import org.sikuli.slides.v1.utils.Constants.DesktopEvent;

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