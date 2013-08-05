package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

public class ExistAction implements Action {

	Target target;
	public ExistAction(Target target){
		this.target = target;
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(target, 5000);
		if (ret == null){
			throw new ActionExecutionException(this);
		}
	}
}