package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class TargetAction implements Action {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

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
		logger.info("executing " + this);
		target.setMinScore(context.getMinScore());
		long waitTime = context.getWaitTime();
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(target, (int) waitTime);
		if (ret != null){
			Canvas canvas = new ScreenRegionCanvas(ret);
			canvas.addBox(ret);
			canvas.show();
			
			Context subConext = context.createCopy();			
			subConext.setScreenRegion(ret);		
			targetAction.execute(subConext);

			canvas.hide();
		}else if (noTargetAction != null){
			noTargetAction.execute(context);
		}else{
			throw new ActionExecutionException("Unable to locate the target on the screen", this);
		}
	}

	public Action getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(Action targetAction) {
		this.targetAction = targetAction;
	}
	
	public String toString(){
		return Objects.toStringHelper(this)
				.add("action", targetAction)
				.add("target", target).toString();
	}

}