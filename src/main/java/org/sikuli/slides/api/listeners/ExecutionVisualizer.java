package org.sikuli.slides.api.listeners;

import java.awt.Color;

import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.ExecutionListener;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.DoubleClickAction;
import org.sikuli.slides.api.actions.AssertExistAction;
import org.sikuli.slides.api.actions.LeftClickAction;
import org.sikuli.slides.api.actions.RightClickAction;
import org.sikuli.slides.api.actions.SlideAction;
import org.sikuli.slides.api.actions.TypeAction;

public class ExecutionVisualizer implements ExecutionListener {

	boolean accept(Action action){
		return action instanceof LeftClickAction ||
				action instanceof RightClickAction ||
				action instanceof DoubleClickAction ||
				action instanceof AssertExistAction ||
				action instanceof TypeAction;
	}
	
	private Canvas canvas;
	private Canvas stepCanvas;
	@Override
	public void beforeExecution(ExecutionEvent event) {
		if (event.getAction() instanceof SlideAction){
			String msg = "Executing slide " + event.getSlide().getNumber();
			ScreenRegion r = event.getContext().getScreenRegion();
			stepCanvas = new ScreenRegionCanvas(r);			
			stepCanvas.addLabel(Relative.to(r).bottomCenter().getScreenLocation(), msg)
			.withFontSize(12)
			.withColor(Color.white).withBackgroundColor(Color.BLACK)
			.withVerticalAlignmentBottom().withHorizontalAlignmentCenter();
			stepCanvas.show();
		}
		
		if (accept(event.getAction())){
			ScreenRegion screenRegion = event.getContext().getScreenRegion();
			canvas = new ScreenRegionCanvas(screenRegion);
			canvas.addBox(screenRegion);
			canvas.show();
		}
	}

	@Override
	public void afterExecution(ExecutionEvent event) {
		if (event.getAction() instanceof SlideAction){
			stepCanvas.hide();
		}
		
		if (accept(event.getAction())){
			canvas.hide();				
		}
	}
}
