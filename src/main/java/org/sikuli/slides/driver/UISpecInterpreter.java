package org.sikuli.slides.driver;

import org.sikuli.slides.api.models.Slide;

interface UISpecInterpreter {
	
	/** 
	 * Interpret the content of a given {@link Slide} to obtain a specification of an user interface.
	 * 
	 * @param slide the input slide.
	 * @return {@link UISpec} object.
	 */
	UISpec interpret(Slide slide);
}