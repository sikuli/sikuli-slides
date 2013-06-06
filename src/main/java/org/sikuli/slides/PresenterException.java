package org.sikuli.slides;


// describes an exception encountered while presenting slides
public class PresenterException extends Exception {
	
	// the slide where an exception occurred 
	private Slide slide;

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}
}
