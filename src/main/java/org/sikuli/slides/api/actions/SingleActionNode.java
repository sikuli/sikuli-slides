package org.sikuli.slides.api.actions;

import static com.google.common.base.Preconditions.checkNotNull;

import org.sikuli.slides.api.Context;

public class SingleActionNode extends ActionNode {	
	
	public SingleActionNode(Action action){
		setAction(checkNotNull(action));
	}
	
	public void execute(Context context) throws ActionExecutionException {
		checkNotNull(context);
		getAction().execute(context);
	}
	
	public void stop(){
		
	}
}