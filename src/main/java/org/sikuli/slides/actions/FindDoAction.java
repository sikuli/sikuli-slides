package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

public class FindDoAction implements Action {

	Target target;
	private TargetScreenRegionAction targetAction;
	public FindDoAction(Target target, TargetScreenRegionAction targetAction){
		this.target = target;
		this.setTargetAction(targetAction);
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(target, 5000);
		if (ret != null){
			getTargetAction().execute(context, ret);			
		}else{
			throw new ActionExecutionException(this);
		}
	}

	public TargetScreenRegionAction getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(TargetScreenRegionAction targetAction) {
		this.targetAction = targetAction;
	}
}