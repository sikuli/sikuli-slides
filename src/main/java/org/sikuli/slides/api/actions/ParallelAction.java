package org.sikuli.slides.api.actions;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

// execute all children actions in parallel
// execution returns when all the children have finished execution
// 
public class ParallelAction extends AbstractAction {
	
	private CountDownLatch doneSignal;
		
	class Worker implements Runnable {
	     Action action;
	     Context context;
	     boolean success = true;
	     ActionExecutionException exception = null;
	     Worker(Action action, Context context) { 
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
	    	 }
    		 doneSignal.countDown();
	     }
	   }
	
	@Override
	protected void doExecute(Context context) throws ActionExecutionException {
		int n = getChildren().size();
		doneSignal = new CountDownLatch(n); 
		List<Worker> workers = Lists.newArrayList();
		for (Action action : getChildren()){
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
	
	public String toString(){
		return Objects.toStringHelper(this)
				.add("actions", getChildren())
				.toString();
	}

}