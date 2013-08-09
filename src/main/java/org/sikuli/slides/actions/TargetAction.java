package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;

public class TargetAction implements Action {

	Target target;
	private Action targetAction;
	private Action noTargetAction;
	
	public TargetAction(Target target, Action targetAction){
		this.target = target;
		this.targetAction = targetAction;
	}
	
	public TargetAction(Target target, Action targetAction, Action noTargetAction){
		this.target = target;
		this.targetAction = targetAction;
		this.noTargetAction = noTargetAction;
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(target, 5000);
		if (ret != null){
			Canvas canvas = new ScreenRegionCanvas(ret);
			canvas.addBox(ret);
			canvas.show();
			
			context.setScreenRegion(ret);			
			targetAction.execute(context);
			context.setScreenRegion(screenRegion);

			canvas.hide();
		}else if (noTargetAction != null){
			noTargetAction.execute(context);
		}else{
			throw new ActionExecutionException(this);
		}
	}

	public Action getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(Action targetAction) {
		this.targetAction = targetAction;
	}

}