package org.sikuli.slides.api.slideshow;

import java.util.List;

import javax.swing.event.EventListenerList;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;

class DefaultSlideShowController implements SlideShowController{

	private Context context;
	public DefaultSlideShowController(Context context){
		this.context = context;
	}

	private List<Slide> slides;
	int index = 0;
	int n = 0;
	private boolean quit;

	Thread executingThread;
	private boolean skip;
	private boolean pending;
	private Slide currentSlide;

	@Override
	public void start() {
		
		executingThread = new Thread(){

			public void run(){
				n = slides.size();
				index = 0;
				pending = true;

				while (!quit){

					Slide action;
					synchronized (this){
						if (pending){
							action = slides.get(index);
							skip = false;
							pending = false;
						}else{
							action = null;							
						}
					}

					try {
						if (action != null){
							executeSlide(action);
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
		if (currentSlide != null){			
			currentSlide.stop();
			currentSlide = null;
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

	EventListenerList listenerList = new EventListenerList();	
	@Override
	public void addListener(SlideShowListener listener) {
		listenerList.add(SlideShowListener.class, listener);		
	}

	@Override
	public void removeListener(SlideShowListener listener) {
		listenerList.remove(SlideShowListener.class, listener);
	}
	
	void fireSlideStarted(Slide slide){
	     Object[] listeners = listenerList.getListenerList();
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SlideShowListener.class) {
	             ((SlideShowListener)listeners[i+1]).slideStarted(slide);
	         }
	     }
	}
	
	void fireSlideFinished(Slide slide){
	     Object[] listeners = listenerList.getListenerList();
	     for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SlideShowListener.class) {
	             ((SlideShowListener)listeners[i+1]).slideFinished(slide);
	         }
	     }
	}

	private void executeSlide(Slide slide) throws ActionExecutionException {
		fireSlideStarted(slide);
		currentSlide = slide;							
		slide.execute(context);
		currentSlide = null;
		fireSlideFinished(slide);
	}

	@Override
	public void setContent(List<Slide> slides) {
		this.slides = slides;		
	}

	@Override
	public List<Slide> getContent() {
		return slides;
	}



}