package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class WaitAction extends TargetAction {
	
	private long duration = 10000;
	private Target target;
	private Action retry;
	
	public WaitAction(Target target){
		super(target);
	}		

	@Override
	public void execute(Context context) throws ActionExecutionException {			
		Action action = new Action(){

			@Override
			public void execute(Context context) throws ActionExecutionException {
				ScreenRegion screenRegion = context.getScreenRegion();
				ScreenRegion ret = screenRegion.find(getTarget());
				if (ret == null){
					throw new ActionExecutionException("", this);
				}
			}

			@Override
			public void stop() {				
			}
			
		};
		
		retry = new RetryAction(action, duration, 200);
		retry.execute(context);
	}
	
	@Override
	public void stop(){
		if (retry != null)
			retry.stop();
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