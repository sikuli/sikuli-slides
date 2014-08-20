package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class TargetAction extends ChainedAction {
	
	private Target target;
	
	public TargetAction(Target target){
		this.setTarget(target);
	}
	
	public TargetAction(Target target, Action targetAction){
		this.setTarget(target);
		setChild(targetAction);
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		getTarget().setMinScore(context.getMinScore());
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion targetRegion = screenRegion.find(getTarget());
		if (targetRegion != null){			
			Context childConext = new Context(context, targetRegion);
			Action child = getChild();
			if (child != null){
				child.execute(childConext);			
			}
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