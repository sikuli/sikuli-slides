package org.sikuli.slides.api;

import org.sikuli.slides.actions.Action;
import org.sikuli.slides.models.Slide;


// describes an exception encountered while presenting slides
public class ActionRuntimeException extends RuntimeException {
	
	private Action action;
	
	public ActionRuntimeException(Action action){
		this.action = action;
	}
	
	// the slide where an exception occurred 
	private Slide slide;

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
