package org.sikuli.slides.api.actions;

import java.awt.Color;

import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.interpreters.SpatialRelationship;
import org.sikuli.slides.api.models.SlideElement;

import com.google.common.base.Objects;

public class LabelAction implements Action {
	
	private String text = "";
	private int fontSize = 12;
	private Color backgroundColor = Color.yellow;
	private Canvas canvas;
	
	private SlideElement slideElement;
		
	private double xmin = 0;
	private double xmax = 1;
	private double ymin = 0;
	private double ymax = 1;
	private SpatialRelationship spatial = null;

	@Override
	public void execute(Context context) throws ActionExecutionException{
		ScreenRegion targetRegion = context.getScreenRegion();		
		if (spatial != null){
			targetRegion = spatial.apply(context);
			if (targetRegion == null){
				throw new ActionExecutionException("",this);
			}
		}

		String textToDisplay = context.render(text);
		
		canvas = new ScreenRegionCanvas(targetRegion);
		canvas.addLabel(targetRegion, textToDisplay)
		.withColor(Color.black).withFontSize((int)fontSize).withLineWidth(2)
		.withBackgroundColor(backgroundColor)
		.withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();		
		canvas.show();		
	}
	
	@Override
	public void stop(){
		if (canvas != null)
			canvas.hide();
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
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	
	public String toString(){
		return Objects.toStringHelper(this).add("text", text).toString();
	}

	public SlideElement getSlideElement() {
		return slideElement;
	}

	public void setSlideElement(SlideElement slideElement) {
		this.slideElement = slideElement;
	}

	public void setBounds(double xmin, double xmax, double ymin, double ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;		
	}
	
	public double getMinX() {
		return xmin;
	}
	
	public double getMaxX() {
		return xmax;
	}
	
	public double getMinY() {
		return ymin;
	}
	
	public double getMaxY() {
		return ymax;
	}

	public void setSpatialRelationship(SpatialRelationship spatial) {
		this.spatial = spatial;	
	}
	
	public SpatialRelationship getSpatialRelationship(){
		return spatial;
	}
}
