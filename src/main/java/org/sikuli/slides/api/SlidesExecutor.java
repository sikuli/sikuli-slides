package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.slides.models.Slide;

public interface SlidesExecutor {
			
	// Execute a list of slides
	public void execute(List<Slide> slides) throws SlideExecutionException;	
	
}