package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class WaitAction implements Action {
	
	private long duration = 10000;
	private Target target;
	
	public WaitAction(Target target){
		this.setTarget(target);
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion ret = screenRegion.wait(getTarget(), (int) duration);
		if (ret == null){
			throw new ActionExecutionException("Unable to find the target after waiting for " + duration + " ms", this);
		}
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("target",getTarget()).toString();
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
}