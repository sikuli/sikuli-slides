package org.sikuli.slides.api.actions;

import org.sikuli.slides.api.Context;

public class WaitActionNode extends ActionNode {
		
	private int waitDuration;
	
	public WaitActionNode(int waitDuration) {
		super();
		this.waitDuration = waitDuration;
	}

	/**
	 * Execute and wait for execution to finish
	 */
	public void execute(Context context) throws ActionExecutionException{
		synchronized (this){
			try {
				this.wait(waitDuration);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void stop(){
		synchronized(this){
			this.notify();
		}
	}

}