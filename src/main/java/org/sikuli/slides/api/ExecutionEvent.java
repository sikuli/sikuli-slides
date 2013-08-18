package org.sikuli.slides.api;

import static com.google.common.base.Preconditions.checkNotNull;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

/**
 * Describes an event when an action is executed in a given context.
 * 
 * @author Sikuli Slide
 *
 */
public class ExecutionEvent {
	private Action action;
	private Slide slide;
	private Context context;
	
	/**Construct an instance of this event.
	 * @param action	the action associated with this event
	 * @param context	the context associated with this event
	 */
	public ExecutionEvent(Action action, Context context) {
		this.action = checkNotNull(action);
		this.context = checkNotNull(context);
		this.slide = context.getSlide();
	}
	/**Get the action associated with this event
	 * @return	action
	 */
	public Action getAction() {
		return action;
	}
	/**Set the action associated with this event
	 * @param action
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	/**Get the slide associated with this event
	 * @return	slide, null if the action is executed independent of any slide
	 */
	public Slide getSlide() {
		return slide;
	}
	/**Get the slide associated with this event
	 * @param slide
	 */
	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	/**Get the context associated with this event
	 * @return context
	 */
	public Context getContext() {
		return context;
	}
	/**Set the context associated with this event
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = checkNotNull(context);
	}
}
