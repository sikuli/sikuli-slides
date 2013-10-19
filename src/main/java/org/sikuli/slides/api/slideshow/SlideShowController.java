package org.sikuli.slides.api.slideshow;

import java.util.EventListener;
import java.util.List;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

interface SlideShowController {	
	public void start();
	public void next();
	public void previous();
	public void quit();
	public void pause();
	public void setAutoAdvance(boolean autoAdvance);
	public boolean isAutoAdvance(); 
	public Action getCurrent();
	public void setContent(List<Slide> slides);
	public List<Slide> getContent();
	public void addListener(SlideShowListener listener);
	public void removeListener(SlideShowListener listener);
}

interface SlideShowListener extends EventListener {	
	public void slideStarted(Slide slide);
	public void slideFinished(Slide slide);
}