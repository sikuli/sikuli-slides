package org.sikuli.slides.api;

import java.awt.event.KeyEvent;
import java.util.List;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.ActionExecutionException;
import org.sikuli.slides.api.interpreters.DefaultInterpreter;
import org.sikuli.slides.api.interpreters.Interpreter;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.api.views.SimpleSlideViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

class SlideShowExecutor 
	implements SlidesExecutor {

	static Logger logger = LoggerFactory.getLogger(SlideShowExecutor.class);

	private Context context;
	private List<Action> actions;
	private List<Slide> slides;

	private SimpleSlideViewer viewer;
	private NativeInputDetector detector;

	volatile int index;
	volatile boolean pending = true;
	volatile boolean skipped = false;
	volatile boolean last = false;
	volatile boolean quit = false;
	volatile boolean autoAdvance = true;


	private int n;

	public SlideShowExecutor(Context context){
		this.context = context;
	}

	public SlideShowExecutor(){
		context = new Context();
	}

	@Override
	public void execute(List<Slide> slides) throws SlideExecutionException {
		logger.info("Executing {} slide(s)", slides.size());

		this.slides = slides;
		Interpreter interpreter = new DefaultInterpreter(context);		
		actions = Lists.newArrayList();
		for (Slide slide : slides){
			Action action = interpreter.interpret(slide);
			actions.add(action);
			logger.debug("Action interpreted: {}", action);
		}

		detector = new NativeInputDetector();
		detector.start();
		viewer = new SimpleSlideViewer();

		context.setWaitTime(50000);

		n = slides.size();

		setCurrentSlideIndex(0);

		while (!quit){
			Action action;
			Slide slide;

			synchronized (this){
				action = actions.get(index);
				slide = slides.get(index);
				last = index == n - 1;
			}			
			if (pending){
				skipped = false;
				executeSlideAction(slide, action, last);				
			}

			if (!skipped && autoAdvance){
				nextSlide();
			}
			
			while(!pending && !quit){
				try{
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}		
		}
	}

	//		detector.stop();



	synchronized void setCurrentSlideIndex(int i){
		this.index = i;
		Slide slide = slides.get(i);
		viewer.setSlide(slide);
		viewer.setVisible(true);
		pending = true;
	}



	Action currentAction;
	void executeSlideAction(Slide slide, Action action, boolean last) throws SlideExecutionException{
		Context slideContext = new Context(context, slide);
		ExecutionEvent event = new ExecutionEvent(action,slideContext);

		logger.info("Slide {} of {}", slide.getNumber(), slides.size());

		try {				
			currentAction = action;
			action.execute(slideContext);
		} catch (ActionExecutionException e) {
			SlideExecutionException ex = new SlideExecutionException(e);
			ex.setAction(e.getAction());
			ex.setSlide(slide);
			throw ex;
		}

		if (last)
			pending = false;		
	}

	synchronized boolean hasNextSlide(){
		return index < n - 1;
	}

	synchronized void nextSlide(){
		if (index < n - 1){
			index = index + 1;
			setCurrentSlideIndex(index);
		}
	}
	
	synchronized void toggleAutoAdvance(){
		autoAdvance = !autoAdvance;
	}
	
	synchronized void skipCurrentAction(){
		if (currentAction != null){
			currentAction.stop();
			skipped = true;			
		}
	}

	synchronized void prevSlide(){
		if (index > 0){
			index = index - 1;
			setCurrentSlideIndex(index);
		}
	}
	
	synchronized void quit(){
		skipCurrentAction();
		quit = true;
	}

	class NativeInputDetector implements NativeKeyListener, NativeMouseInputListener {

		boolean isMetaPressed(NativeKeyEvent e){
			return (e.getModifiers() & NativeKeyEvent.META_MASK) > 0;
		}	
		boolean isAltPressed(NativeKeyEvent e){
			return (e.getModifiers() & NativeKeyEvent.ALT_MASK) > 0;
		}
		boolean isShiftPressed(NativeKeyEvent e){
			return (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) > 0;
		}
		boolean isCtrlPressed(NativeKeyEvent e){
			return (e.getModifiers() & NativeKeyEvent.CTRL_MASK) > 0;
		}


		final public void start() {
			try {
				GlobalScreen.registerNativeHook();
			} catch (NativeHookException e1) {
				return;
			} 			

			GlobalScreen.getInstance().addNativeKeyListener(this);
			GlobalScreen.getInstance().addNativeMouseMotionListener(this);
			GlobalScreen.getInstance().addNativeMouseListener(this);
		}

		final public void stop(){
			GlobalScreen.getInstance().removeNativeMouseListener(this);
			GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
			GlobalScreen.getInstance().removeNativeKeyListener(this);
		}

		public void nativeKeyPressed(NativeKeyEvent e) {
			if (isAltPressed(e) && isCtrlPressed(e)){
							
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					skipCurrentAction();
					nextSlide();
				}else if (e.getKeyCode() == KeyEvent.VK_LEFT){			
					skipCurrentAction();
					prevSlide();
				}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					quit();
				}else if (e.getKeyCode() == KeyEvent.VK_A){
					toggleAutoAdvance();
				}
			}
			
		}
		public void nativeKeyReleased(NativeKeyEvent e) {
		}
		public void nativeKeyTyped(NativeKeyEvent e) {
		}

		public void nativeMouseClicked(NativeMouseEvent e) {	
		}
		public void nativeMousePressed(NativeMouseEvent e) {
		}
		public void nativeMouseReleased(NativeMouseEvent e) {
		}
		public void nativeMouseMoved(NativeMouseEvent e) {
		}
		public void nativeMouseDragged(NativeMouseEvent e) {
		}

	}

}


