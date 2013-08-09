package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

public class SlideExecutionException extends Exception {
	
	public SlideExecutionException(Slide slide) {
		super();
		this.slide = slide;
	}
	
	public SlideExecutionException(Slide slide, Action action) {
		super();
		this.slide = slide;
		this.action = action;
	}	
	
	private Action action;
	private Slide slide;
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Slide getSlide() {
		return slide;
	}
	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	
}
