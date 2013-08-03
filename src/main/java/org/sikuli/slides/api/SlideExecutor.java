package org.sikuli.slides.api;

import java.util.List;

import org.sikuli.slides.models.Slide;

public interface SlideExecutor {
			
	// Execute a list of slides
	public void execute(List<Slide> slide) throws ActionRuntimeException;	
	
}