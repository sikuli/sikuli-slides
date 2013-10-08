package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.ExecutionEvent;
import org.sikuli.slides.api.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract public class RobotAction implements Action {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		ExecutionListener actionListener = context.getExecutionListener();
		if (actionListener != null){
			actionListener.beforeExecution(new ExecutionEvent(this, context));
		}
		
		logger.debug("executing {}", this);
		try {
			doExecute(context);
		} catch (ActionExecutionException e) {
			throw e;
		}finally{
			if (actionListener != null){
				actionListener.afterExecution(new ExecutionEvent(this, context));
			}			
		}
		
	}
	
	abstract protected void doExecute(Context context) throws ActionExecutionException;
	
	public void stop(){		
	}
	
}
