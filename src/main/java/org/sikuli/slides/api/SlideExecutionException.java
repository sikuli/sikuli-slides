package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

/**
 * Describes an exception during the execution of
 * a slide.
 * 
 * @author Sikuli Lab
 */
@SuppressWarnings("serial")
public class SlideExecutionException extends Exception {
	
	private Action action;
	private Slide slide;
	public SlideExecutionException(String message) {
		super(message);
	}

	public SlideExecutionException() {
		super();
	}

	public SlideExecutionException(Throwable cause) {
		super(cause);
	}

	/**Get the {@link Action} associated with this exception.
	 * @return
	 */
	public Action getAction() {
		return action;
	}
	/**Set the {@link Action} associated with this exception.
	 * @param action
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	/**Get the {@link Slide} associated with this exception.
	 * @return
	 */
	public Slide getSlide() {
		return slide;
	}
	/**Set the {@link Slide} associated with this exception.
	 * @return
	 */	
	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	
}
