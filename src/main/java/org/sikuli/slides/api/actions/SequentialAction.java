package org.sikuli.slides.api.actions;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

// It can execute all the children actions sequentially.
// When stopped, the currently running action is stopped.
// All subsequent actions are skipped.
// 
public class SequentialAction extends CompoundAction {

	volatile boolean stopFlag;

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
	


	public void execute(Context context) throws ActionExecutionException{

		stopFlag = false;
		Iterator<Action> iter = getChildren().iterator();
		while (iter.hasNext() && !stopFlag){
			Action action = iter.next();
			Context workerContxt = new Context(context);
			Worker worker = new Worker(action, workerContxt);
			Thread t = new Thread(worker);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
			}
			
			if (!worker.success){
				throw worker.exception;
			}
		}

	}

	public void stop(){
		stopFlag = true;
		for (Action action : getChildren()){
			action.stop();
		}
	}

}