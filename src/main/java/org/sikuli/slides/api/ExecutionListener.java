package org.sikuli.slides.api;

/**
 * Interface to listen for execution events 
 * 
 * @author Sikuli Lab
 *
 */
public interface ExecutionListener {
	/**Invoked before the execution takes place
	 * @param event	the execution event
	 */
	public void beforeExecution(ExecutionEvent event);
	/**Invoked after the execution has taken place
	 * @param event	the execution event
	 */
	public void afterExecution(ExecutionEvent event);	
}
