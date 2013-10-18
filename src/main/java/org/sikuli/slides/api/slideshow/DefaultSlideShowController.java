package org.sikuli.slides.api.slideshow;

import java.util.List;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;

class DefaultSlideShowController implements SlideShowController{

	private Context context;
	public DefaultSlideShowController(Context context){
		this.context = context;
	}

	private List<Action> actions;
	int index = 0;
	int n = 0;
	private boolean quit;

	Thread executingThread;
	private boolean skip;
	private boolean pending;
	private Action currentAction;

	@Override
	public void start() {
		
		executingThread = new Thread(){

			public void run(){
				n = actions.size();
				index = 0;
				pending = true;

				while (!quit){

					Action action;
					synchronized (this){
						if (pending){
							action = actions.get(index);
							skip = false;
							pending = false;
						}else{
							action = null;							
						}
					}

					try {
						if (action != null){
							System.out.println(index);
							currentAction = action;
							action.execute(context);
							currentAction = null;
						}
						if (!skip){
							next();
						}

					} catch (ActionExecutionException e) {

					}

					if (index == n){
						quit = true;
					}				
				}
			};

		};
		executingThread.start();
		//.start();

		//			Action action;
		//			Slide slide;

		//			synchronized (this){
		//				action = actions.get(index);
		//				slide = slides.get(index);
		//				last = index == n - 1;
		//			}			
		//			if (pending){
		//				skipped = false;
		//				executeSlideAction(slide, action, last);				
		//			}
		//
		//			if (!skipped && autoAdvance){
		//				nextSlide();
		//			}
		//			
		//			while(!pending && !quit){
		//				try{
		//					Thread.sleep(1000);
		//				} catch (InterruptedException e) {
		//				}
		//			}		
		//		}

	}

	synchronized void stopCurrentAction(){
		if (currentAction != null){			
			currentAction.stop();
			currentAction = null;
			skip = true;
		}
	}

	@Override
	synchronized public void next() {
		if (index == n - 1)
			return;		
		stopCurrentAction();
		index = index + 1;		
		pending = true;
	}

	@Override
	synchronized public void previous() {
		if (index == 0)
			return;			
		stopCurrentAction();		
		index = index - 1;
		pending = true;
	}

	@Override
	public void quit() {
		stopCurrentAction();
		quit = true;

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutoAdvance(boolean autoAdvance) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAutoAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Action getCurrent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContent(List<Action> actions) {
		this.actions = actions;
	}



}