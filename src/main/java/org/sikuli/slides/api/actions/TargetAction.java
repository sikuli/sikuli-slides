package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class TargetAction extends DefaultAction {
	
	private Target target;
	
	public TargetAction(Target target){
		this.setTarget(target);
	}
	
	public TargetAction(Target target, Action targetAction){
		this.setTarget(target);
		addChild(targetAction);
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		logger.info("executing " + this);
		getTarget().setMinScore(context.getMinScore());
		long waitTime = context.getWaitTime();
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion targetRegion = screenRegion.wait(getTarget(), (int) waitTime);
		if (targetRegion != null){			
			Canvas canvas = new ScreenRegionCanvas(targetRegion);
			canvas.addBox(targetRegion);
			//canvas.show();
						
			Context childConext = context.createCopy();			
			childConext.setScreenRegion(targetRegion);
			for (Action child : getChildren()){
				child.execute(childConext);
			}

			//canvas.hide();
		}else{
			throw new ActionExecutionException("Unable to locate the target on the screen", this);
		}
	}

	public String toString(){
		return Objects.toStringHelper(this)				
				.add("target", getTarget()).toString();
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

}