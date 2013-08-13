package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class TargetAction extends DefaultAction {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	Target target;
	
	public TargetAction(Target target, Action targetAction){
		this.target = target;
		addChild(targetAction);
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
				.add("target", target).toString();
	}

}