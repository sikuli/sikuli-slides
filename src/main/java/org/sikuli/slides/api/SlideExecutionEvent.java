package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.models.Slide;

public class SlideExecutionEvent {
	private Action action;
	private Slide slide;
	private Context context;
	public SlideExecutionEvent(Slide slide, Action action, Context context) {
		super();
		this.slide = slide;
		this.action = action;
		this.context = context;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public Slide getSlide() {
		return slide;
	}
	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
}
