package org.sikuli.slides.api.actions;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

// execute all children actions in parallel
// execution returns when all the children have finished execution
// 
public class ParallelActionNode extends ActionNode {
		
	private CountDownLatch doneSignal;
	
	class Worker implements Runnable {
		ActionNode action;
		Context context;
		boolean success = true;
		ActionExecutionException exception = null;
		Worker(ActionNode action, Context context) { 
			this.action = action; 
			this.context = context;
		}
		public void run() {
			try {
				action.execute(context);
				success = true;
			} catch (ActionExecutionException e) {
				success = false;
				// Saves the exception so it can be re-thrown
				exception = e;
			} catch (InterruptedException e) {
				

			}
			doneSignal.countDown();
		}
	}
	
	
	/**
	 * Execute and wait for execution to finish
	 */
	public void execute(Context context) throws ActionExecutionException{
		int n = getChildren().size();
		doneSignal = new CountDownLatch(n); 
		List<Worker> workers = Lists.newArrayList();
		for (ActionNode action : getChildren()){
			Context workerContxt = new Context(context);
			Worker worker = new Worker(action, workerContxt);
			workers.add(worker);
			new Thread(worker).start();
		}
		try {
			doneSignal.await();
		} catch (InterruptedException e) {

		}

		// if any of the worker did not succeed, 
		// rethrow the associated ActionExecutionException
		for (Worker worker : workers){
			if (!worker.success){
				throw worker.exception;
			}
		}
	}
	
	public void stop(){
		for (ActionNode action : getChildren()){
			action.stop();
		}
	}

	public String toString(){
		return Objects.toStringHelper(this)
				.add("actions", getChildren())
				.toString();
	}

}