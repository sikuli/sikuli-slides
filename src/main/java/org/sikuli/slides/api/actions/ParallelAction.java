package org.sikuli.slides.api.actions;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// execute all children actions in parallel
// execution returns when all the children have finished execution
// 
public class ParallelAction extends CompoundAction {

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

	class BackgroundWorker extends Worker {
		BackgroundWorker(Action action, Context context) {
			super(action, context);
		}

		public void run() {
			try {
				action.execute(context);
			} catch (ActionExecutionException e) {
			}
		}
	}

	private Set<Action> backgroundSet = Sets.newHashSet();
	public void addChildAsBackground(Action child){
		backgroundSet.add(child);
		addChild(child);
	}
	
	private boolean isBackground(Action action){
		return backgroundSet.contains(action);
	}
	
	
	/**
	 * Execute and wait for execution to finish
	 */
	public void execute(Context context) throws ActionExecutionException{
		int count = 0;
		for (Action action : getChildren()){
			if (!isBackground(action)){
				count += 1;			
			}
		}

		doneSignal = new CountDownLatch(count);

		List<Worker> workers = Lists.newArrayList();
		for (Action action : getChildren()){
			final Context workerContxt = new Context(context);

			if (!isBackground(action)){			
				Worker worker = new Worker(action, workerContxt);
				workers.add(worker);
				new Thread(worker).start();
			}else{
				Worker worker = new BackgroundWorker(action, workerContxt);
				new Thread(worker).start();
			}
		}
		try {
			doneSignal.await();
		} catch (InterruptedException e) {

		}
		stop();

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