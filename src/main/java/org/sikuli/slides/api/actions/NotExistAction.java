package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class NotExistAction extends TargetAction {
	
	public NotExistAction(Target target){
		super(target); 
	}
	
	@Override
	protected void doExecute(Context context) throws ActionExecutionException{
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