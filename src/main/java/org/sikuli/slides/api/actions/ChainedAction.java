package org.sikuli.slides.api.actions;


import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainedAction extends CompoundAction {

	Logger logger = LoggerFactory.getLogger(getClass());

	public void setChild(Action action){
		removeAllChildren();
		addChild(action);
	}
	
	public Action getChild(){
		if (hasChild(0))
			return getChild(0);
		else
			return null;
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException{
	}

	@Override
	public void stop(){
		if (getChild() != null)
			getChild().stop();
	}

}

