package org.sikuli.slides;

public interface Presenter {
	
	// play a slide deck from the start until the end or until it crashes
	public void play(SlideDeck slideDeck) throws PresenterException;
	
	// play a single slide
	public void play(Slide slide) throws PresenterException;	
	
}