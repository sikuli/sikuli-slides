package org.sikuli.slides.api.slideshow;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.event.EventListenerList;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultSlideShowController implements SlideShowController{

	static private Logger logger = LoggerFactory.getLogger(DefaultSlideShowController.class);

	private Context context;
	public DefaultSlideShowController(Context context){
		this.context = context;
		this.playSignal = new CountDownLatch(0);
	}	

	private List<Slide> slides;
	volatile int index = 0;
	int n = 0;
	volatile private boolean quit;
	volatile private boolean paused;

	Thread executingThread;
	private Slide currentSlide;

	private CountDownLatch  playSignal;
	
	class WorkerThread extends Thread {
		
		Slide slide;			
		Context context;
		public WorkerThread(Slide slide, Context context) {		
			this.slide = slide;
			this.context = context;
		}

		public void run(){
			try {
				slide.execute(context);
			} catch (ActionExecutionException e) {
			}
		}
		
		public void terminate(){
			slide.stop();
		}		
	}
	
	
	@Override
	public void start() {

		executingThread = new Thread(){

			public void run(){
				n = slides.size();
				
				fireSlideSelected(slides.get(index));
				
				while (!quit){					
					try {
						playSignal.await();
						currentSlide = slides.get(index);		
						Result result = execute(currentSlide);						
						if (result == Result.SUCCESS && index == n - 1){						
							pause();
						}else if (result == Result.SUCCESS && !isPaused()){
							next();
						}else if (result == Result.EXCEPTION){
							pause();
						}
						
					} catch (InterruptedException e1) {

					} 
				}
			};

		};
		executingThread.start();
	}

	@Override
	synchronized public void next() {
		logger.debug("[next]");
		//int i = slides.indexOf(currentSlide);
		if (index == n - 1)
			return;
		
//		List<Slide> sub = slides.subList(i+1, slides.size());
//		queue.clear();
//		queue.addAll(sub);
		index = index + 1;
		fireSlideSelected(slides.get(index));
		if (executing)
			stop();	
	}

	@Override
	synchronized public void previous() {
		logger.debug("[previous]");
		if (index == 0)
			return;
		index = index - 1;
		fireSlideSelected(slides.get(index));
		if (executing)
			stop();
	}

	@Override
	synchronized public void jumpTo(int index) {
		logger.debug("[jumpTo] " + this.index + " -> " + index);
		if (index >= 0 && index < n && this.index != index){			
			this.index = index;
			fireSlideSelected(slides.get(index));
			if (executing)
				stop();
		}
	}

	@Override
	public void quit() {
		stop();
		quit = true;

	}

	@Override
	synchronized public void pause(){	
		logger.debug("[pause] ");
		
		// do nothing if it is already paused or there is no more slide to execute 
		if (paused)
			return;		
		paused = true;		
		playSignal = new CountDownLatch(1);				
		if (executing)
			stop();
	}
	
	@Override
	synchronized public void play() {	
		logger.debug("[play] ");
		if (!paused)
			return;
		paused = false;
		playSignal.countDown();		
	}
	
	@Override
	synchronized public boolean isPaused() {
		return paused;
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
				((SlideShowListener)listeners[i+1]).slideExecuted(slide);
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
	
	void fireSlideSelected(Slide slide){
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==SlideShowListener.class) {
				((SlideShowListener)listeners[i+1]).slideSelected(slide);
			}
		}
	}
	
	enum Result {
		SUCCESS,
		STOPPED,
		EXCEPTION
	};
	
	volatile boolean stopped = false;
	volatile boolean executing = true;
	private Result execute(Slide slide) {
		logger.debug("[execute] " + slide);
		fireSlideStarted(slide);
		executing = true;
		stopped = false;
		try {
			slide.execute(context);		
			executing = false;
			fireSlideFinished(slide);
			if (stopped){
				stopped = false;
				return Result.STOPPED;
			}else{			
				return Result.SUCCESS;
			}
		
		} catch (ActionExecutionException e) {
			executing = false;
			return Result.EXCEPTION;
		}		
	}

	synchronized private void stop(){
		if (currentSlide != null && executing){
			logger.debug("[stop] " + currentSlide);
			currentSlide.stop();
			stopped = true;
		}
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