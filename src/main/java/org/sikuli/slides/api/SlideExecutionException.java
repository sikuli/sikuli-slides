package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;

public class SlideExecutionException extends Exception {
	
	private Action action;
	private Slide slide;
	public SlideExecutionException(String message) {
		super(message);
	}

	public SlideExecutionException() {
		super();
	}

	public SlideExecutionException(Throwable cause) {
		super(cause);
	}

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
