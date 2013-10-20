package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class AssertNotExistAction extends TargetAction {
	
	public AssertNotExistAction(Target target){
		super(target); 
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException{
		logger.debug("executing {}", this);
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.find(getTarget());
		if (ret != null){
			throw new ActionExecutionException("Target not expected to exist is found", this);
		}
	}	
	
	public String toString(){
		return Objects.toStringHelper(this).add("target",getTarget()).toString();
	}


}