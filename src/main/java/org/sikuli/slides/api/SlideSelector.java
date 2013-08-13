package org.sikuli.slides.api;

import org.sikuli.slides.api.models.Slide;

public interface SlideSelector {
	public boolean accept(Slide slide);
}