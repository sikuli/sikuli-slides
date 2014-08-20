package org.sikuli.slides.api.slideshow;

import java.util.EventListener;
import java.util.List;

import org.sikuli.slides.api.models.Slide;

interface SlideShowController {	
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

interface SlideShowListener extends EventListener {	
	public void slideExecuted(Slide slide);
	public void slideFinished(Slide slide);
	public void slideSelected(Slide slide);
	public void slideFailed(Slide slide);
}