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

	public boolean isPaused();
	
	public void jumpTo(int index);
	public void setContent(List<Slide> slides);
	public List<Slide> getContent();
	public void addListener(SlideShowListener listener);
	public void removeListener(SlideShowListener listener);

}

interface SlideShowListener extends EventListener {	
	public void slideExecuted(Slide slide);
	public void slideFinished(Slide slide);
	public void slideSelected(Slide slide);
}