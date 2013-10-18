package org.sikuli.slides.api.slideshow;

import java.util.List;

import org.sikuli.slides.api.actions.Action;

interface SlideShowController {	
	public void start();
	public void next();
	public void previous();
	public void quit();
	public void pause();
	public void setAutoAdvance(boolean autoAdvance);
	public boolean isAutoAdvance(); 
	public Action getCurrent();
	public void setContent(List<Action> actions);
}