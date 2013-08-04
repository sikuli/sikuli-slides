package org.sikuli.slides.actions;

import java.awt.Color;
import java.awt.Rectangle;

import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;

public class LabelAction extends ScreenRegionAction {

	private String text = "";
	private int fontSize = 12;
	private int duration = 3000;

	public LabelAction(ScreenRegion targetRegion){
		setTargetScreenRegion(targetRegion);
	}

	public LabelAction(){
		setTargetScreenRegion(null);
	}

	@Override
	public void execute(){
		exceuteOnScreenRegion(getTargetScreenRegion());
	}	


	/**
	 * perform the action to display this label at the targetRegion
	 * @param targetRegion the region to display this label
	 */	
	protected void exceuteOnScreenRegion(ScreenRegion targetRegion){
		logger.info("performing label action on target...");
		if (targetRegion.getBounds() == null){
			targetRegion = new DefaultScreenRegion(targetRegion.getScreen());
		}
				//			logger.error("Failed to find the target to display a label on.");
				//			logger.info("Displaying the label on the center of the screen.");
				//			// make the target region the entire screen
				targetRegion = new DefaultScreenRegion(targetRegion.getScreen());
		

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
