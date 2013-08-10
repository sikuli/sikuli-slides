package org.sikuli.slides.api.actions;

public class ActionExecutionException extends Exception {
	private Action action;
	
	public ActionExecutionException(String message, Action action){
		super(message);
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
