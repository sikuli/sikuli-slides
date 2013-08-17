package org.sikuli.slides.api.actions;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.recorder.detector.EventDetector;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;
import static com.google.common.base.Preconditions.*;

interface NativeMouseEventFilter {
	boolean accept(NativeMouseEvent event);
}

class OnScreenRegionClick implements NativeMouseEventFilter{
	
	private ScreenRegion screenRegion;
	private int screenOffsetX;
	private int screenOffsetY;

	public OnScreenRegionClick(ScreenRegion screenRegion){
		this.screenRegion = checkNotNull(screenRegion);		
		this.screenOffsetX = 0; 
		this.screenOffsetY = 0; 
		// TODO: figure out how to calculate these		
		// Maybe:
//		int id =  ((DesktopScreen) screenRegion.getScreen()).getScreenId();
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice [] devices = ge.getScreenDevices();
//		Rectangle bounds = devices[id].getDefaultConfiguration().getBounds();
//		this.screenOffsetX = bounds.x; 
//		this.screenOffsetY = bounds.y; 
	}

	@Override
	public boolean accept(NativeMouseEvent event) {
		return inRange(event);
	}
	
	/**
	 * Check the mouse action is within the screen region range.
	 * @param e native mouse event
	 * @return true if the mouse event was within the screen region range. 
	 * Otherwise, it returns false.
	 */
	private boolean inRange(NativeMouseEvent e){
		Rectangle r = screenRegion.getBounds();
		r.x += screenOffsetX;
		r.y += screenOffsetY;
		
		int x = e.getX();
		int y = e.getY();
		return r.contains(x,y);
	}
}

class InputWaiter {
	
	private MouseEventDetector mouseDetector;	
	private GlobalKeyListenerExample keyboardDetector;
	private CountDownLatch detectedSignal;
	
	public InputWaiter(){
		mouseDetector = new MouseEventDetector();
		keyboardDetector = new GlobalKeyListenerExample();
		detectedSignal = new CountDownLatch(1);
	}	
	
	
	NativeMouseEventFilter filter;
	public void start(NativeMouseEventFilter filter) throws NativeHookException{
		this.filter = checkNotNull(filter);
		GlobalScreen.registerNativeHook();
		GlobalScreen.getInstance().addNativeKeyListener(keyboardDetector);
		GlobalScreen.getInstance().addNativeMouseMotionListener(mouseDetector);
		GlobalScreen.getInstance().addNativeMouseListener(mouseDetector);	
		try {
			detectedSignal.await();
		} catch (InterruptedException e) {
		}
		
		GlobalScreen.getInstance().removeNativeMouseListener(mouseDetector);
		GlobalScreen.getInstance().removeNativeMouseMotionListener(mouseDetector);
		GlobalScreen.getInstance().removeNativeKeyListener(keyboardDetector);
		GlobalScreen.unregisterNativeHook();
	}
	
	void detected(){
		detectedSignal.countDown();
	}
		
	class GlobalKeyListenerExample implements NativeKeyListener {
		List<NativeKeyEvent> events = new ArrayList<NativeKeyEvent>();
		public void nativeKeyPressed(NativeKeyEvent e) {
	    }
	    public void nativeKeyReleased(NativeKeyEvent e) {
	    }
	    public void nativeKeyTyped(NativeKeyEvent e) {
	    }
	}

	class MouseEventDetector extends EventDetector 
	implements NativeMouseInputListener {
		public void nativeMouseClicked(NativeMouseEvent e) {	
		}
		public void nativeMousePressed(NativeMouseEvent e) {
		}
		public void nativeMouseReleased(NativeMouseEvent e) {
		}
		public void nativeMouseMoved(NativeMouseEvent e) {
			if (filter.accept(e))
				detected();
		}
		public void nativeMouseDragged(NativeMouseEvent e) {
		}
	}
}
public class PauseAction extends AbstractAction {
	
	@Override
	public void doExecute(Context context) throws ActionExecutionException {
		
		// set up a canvas to display a button in the middle of the screen
		ScreenRegion r = context.getScreenRegion();
		ScreenRegion centerRegion = Relative.to(r).region(0.4,0.45,0.6,0.55).getScreenRegion();		

		Canvas canvas = new DesktopCanvas();		
		canvas.addBox(centerRegion).withColor(Color.black).withTransparency(0.7f);
		canvas.addLabel(context.getScreenRegion(), "  Continue ")
		.withBackgroundColor(Color.black)
		.withColor(Color.white)
		.withTransparency(0.7f)
		.withFontSize(20).withHorizontalAlignmentCenter().withVerticalAlignmentMiddle();
		canvas.show();
						
		// wait for a click event on the button
		InputWaiter inputWaiter = new InputWaiter();
		try {
			inputWaiter.start(new OnScreenRegionClick(centerRegion));
		} catch (NativeHookException e) {
			
			
		}		
		
		// hide the button
		canvas.hide();
			
		// execute the first child
		List<Action> children = getChildren();
		if (children.size() == 1){
			Action firstChild = children.get(0);
				firstChild.execute(context);
		}
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("children",getChildren()).toString();
	}
}
