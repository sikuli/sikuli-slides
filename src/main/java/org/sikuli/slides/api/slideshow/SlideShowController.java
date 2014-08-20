package org.sikuli.slides.api.slideshow;

import java.util.List;

import org.sikuli.slides.api.models.Slide;

public interface SlideShowController {	
	public void start();
	public void next();
	public void previous();
	public void quit();
	
	// Pause. Don't execute the current slide (but can still navigate around) 
	public void pause();
	public void play();

	
	boolean hasNext();
	boolean hasPrevious();
	
	public boolean isPaused();
	
	public void jumpTo(int index);
	public List<Slide> getContent();
	public void addListener(SlideShowListener listener);
	public void removeListener(SlideShowListener listener);

}