package org.sikuli.slides.api.actions;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class ParallelAction implements Action {
	
	private CountDownLatch doneSignal;
	private List<Action> actions = Lists.newArrayList();
	
	public void addAction(Action action){
		actions.add(action);
	}
	
	public List<Action> getActions(){
		return Lists.newArrayList(actions);
	}
		
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
	public void execute(Context context) throws ActionExecutionException {
		
		int n = actions.size();
		doneSignal = new CountDownLatch(n); 
		List<Worker> workers = Lists.newArrayList();
		for (Action action : actions){
			Worker worker = new Worker(action, context);
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
				.add("actions", actions)
				.toString();
	}

}