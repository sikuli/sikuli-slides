package org.sikuli.slides.actions;

import org.sikuli.slides.api.Context;

public interface Action {
	public void execute(Context context) throws ActionExecutionException; 
}
