package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class ExistAction extends TargetAction {
	
	public ExistAction(Target target){
		super(target);
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {
		logger.debug("executing {}", this);
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(getTarget(), 5000);
		if (ret == null){
			throw new ActionExecutionException("Unable to find the target expected to exist", this);
		}
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("target",getTarget()).toString();
	}
}