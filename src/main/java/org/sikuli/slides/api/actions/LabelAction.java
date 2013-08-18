package org.sikuli.slides.api.actions;

import java.awt.Color;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class LabelAction extends AbstractAction {
	
	private String text = "";
	private int fontSize = 12;
	private int duration = 3000;
	private Color backgroundColor = Color.yellow;

	@Override
	protected void doExecute(Context context){
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
