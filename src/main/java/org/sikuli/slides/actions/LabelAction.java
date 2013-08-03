package org.sikuli.slides.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.ActionRuntimeException;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.MyScreen;
import org.sikuli.slides.utils.UnitConverter;

public class LabelAction extends ScreenRegionAction {

	private String text = "";
	private int fontSize = 12;
	private int duration;

	public LabelAction(ScreenRegion targetRegion){
		setTargetScreenRegion(targetRegion);
	}

	@Override
	public void perform(){
		performOnScreenRegion(getTargetScreenRegion());
	}	


	/**
	 * perform the action to display this label at the targetRegion
	 * @param targetRegion the region to display this label
	 */	
	protected void performOnScreenRegion(ScreenRegion targetRegion){
		logger.info("performing label action on target...");
		Rectangle r = targetRegion.getBounds();		
		if(r == null){			
			logger.error("Failed to find the target to display a label on.");
			logger.info("Displaying the label on the center of the screen.");
			// make the target region the entire screen
			targetRegion = new DefaultScreenRegion(targetRegion.getScreen());
		}
		
		Canvas canvas = new ScreenRegionCanvas(targetRegion);
		canvas.addLabel(targetRegion, text)
		.withColor(Color.black).withFontSize((int)fontSize).withLineWidth(2)
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.display(1.0*duration/1000);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


}
