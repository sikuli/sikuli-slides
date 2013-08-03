package org.sikuli.slides.api;

import org.sikuli.slides.models.Slide;


// describes an exception encountered while presenting slides
public class SlideExecutionException extends Exception {
	
	// the slide where an exception occurred 
	private Slide slide;

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}
}
