package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

public class NotExistAction implements Action {
	
	private Target target;
	public NotExistAction(Target target){
		this.target = target; 
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException{
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.find(target);
		if (ret != null){
			throw new ActionExecutionException(this);
		}
	}	

}