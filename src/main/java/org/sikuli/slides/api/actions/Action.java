package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;
/**
 * An Action represents a simple operation that can be carried out quickly.
 * Once an action is executed, it can not be stopped. The execution is expected to be 
 * quick and is not expected to cause the calling thread to block. Any wait or retry logic 
 * that might result in blocking must NOT be implemented as an Action. Instead, it should
 * be implemented as an Activity
 * 
 * @author tomyeh
 *
 */
public interface Action {
	public void execute(Context context) throws ActionExecutionException;
	public void stop();	
}
