package org.sikuli.slides.api.slideshow;

import java.util.EventListener;

import org.sikuli.slides.api.models.Slide;

public interface SlideShowListener extends EventListener {	
	public void slideExecuted(Slide slide);
	public void slideFinished(Slide slide);
	public void slideSelected(Slide slide);
	public void slideFailed(Slide slide);
}