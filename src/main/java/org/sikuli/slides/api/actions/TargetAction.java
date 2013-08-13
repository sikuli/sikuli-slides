package org.sikuli.slides.api.actions;

import org.sikuli.api.Relative;
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
	
	public TargetAction(Target target, Action targetAction){
		this.target = target;
		this.targetAction = targetAction;
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		logger.info("executing " + this);
		target.setMinScore(context.getMinScore());
		long waitTime = context.getWaitTime();
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion targetRegion = screenRegion.wait(target, (int) waitTime);
		if (targetRegion != null){			
			Canvas canvas = new ScreenRegionCanvas(targetRegion);
			canvas.addBox(targetRegion);
			//canvas.show();
						
			Context subConext = context.createCopy();			
			subConext.setScreenRegion(targetRegion);
			targetAction.execute(subConext);

			//canvas.hide();
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