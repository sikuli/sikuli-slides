package org.sikuli.slides.api.actions;

import java.awt.Color;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class LabelAction implements Action {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String text = "";
	private int fontSize = 12;
	private int duration = 3000;
	private Color backgroundColor = Color.yellow;

	@Override
	public void execute(Context context){
		logger.info("executing " + this);
		ScreenRegion targetRegion = context.getScreenRegion();
		
		String textToDisplay = context.render(text);
		
		Canvas canvas = new ScreenRegionCanvas(targetRegion);
		canvas.addLabel(targetRegion, textToDisplay)
		.withColor(Color.black).withFontSize((int)fontSize).withLineWidth(2)
		.withBackgroundColor(backgroundColor)
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	
	public String toString(){
		return Objects.toStringHelper(this).add("text", text).toString();
	}
}
