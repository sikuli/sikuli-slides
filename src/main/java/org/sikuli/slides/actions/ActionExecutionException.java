package org.sikuli.slides.actions;

public class ActionExecutionException extends Exception {
	private Action action;
	
	public ActionExecutionException(Action action){
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
}
