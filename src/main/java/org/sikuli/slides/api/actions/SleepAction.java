package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class SleepAction implements Action {
		
	private long duration;
	
	public SleepAction(long waitDuration) {
		super();
		this.duration = waitDuration;
	}

	/**
	 * Execute and wait for execution to finish
	 */
	public void execute(Context context) throws ActionExecutionException{
		synchronized (this){
			try {
				this.wait(duration);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void stop(){
		synchronized(this){
			this.notify();
		}
	}

	public long getDuration() {		
		return duration;
	}
	
	public String toString(){
		return Objects.toStringHelper(getClass().getSimpleName()).add("duration",duration).toString();
	}

}