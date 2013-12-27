package org.sikuli.slides.api.slideshow;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.event.EventListenerList;

import org.sikuli.slides.api.Context;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.models.Slide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

class DefaultSlideShowController implements SlideShowController{

	static private Logger logger = LoggerFactory.getLogger(DefaultSlideShowController.class);

	private Context context;
	public DefaultSlideShowController(Context context, List<Slide> slides){
		this.context = context;
		this.playSignal = new CountDownLatch(0);
		this.slides = slides;
	}	

	private List<Slide> slides = Lists.newArrayList();
	volatile int index = 0;
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
				
				index = 0;
				if (slides.isEmpty()){
					return;
				}else{
					fireSlideSelected(slides.get(0));
				}

				while (true){					
					try {
						playSignal.await();
					} catch (InterruptedException e1) {
					}

					if (!quit){
						currentSlide = slides.get(index);		
						Result result = execute(currentSlide);						
						if (result == Result.SUCCESS && index == slides.size() - 1){						
							pause();
						}else if (result == Result.SUCCESS && !isPaused()){
							next();
						}else if (result == Result.EXCEPTION){
							pause();
						}
					} else{
						break;
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
		if (index == slides.size() - 1)
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
		if (index >= 0 && index < slides.size()){			
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
	
	void fireSlideFailed(Slide slide){
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==SlideShowListener.class) {
				((SlideShowListener)listeners[i+1]).slideFailed(slide);
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

			if (stopped){
				stopped = false;
				fireSlideFinished(slide);
				return Result.STOPPED;
			}else{
				fireSlideFinished(slide);
				return Result.SUCCESS;
			}

		} catch (ActionExecutionException e) {
			executing = false;
			fireSlideFailed(slide);
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
	public List<Slide> getContent() {
		return slides;
	}

	@Override
	public boolean hasNext() {
		return index < slides.size() - 1;
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}





}