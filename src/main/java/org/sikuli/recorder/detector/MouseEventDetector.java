package org.sikuli.recorder.detector;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.sikuli.api.ScreenRegion;
import org.sikuli.recorder.event.ClickEvent;

public class MouseEventDetector extends EventDetector 
implements NativeMouseInputListener {


	public void nativeMouseClicked(NativeMouseEvent e) {
		//            System.out.println("Mosue Clicked: " + e.getClickCount());

		
		ScreenRegion region = getRegionOfInterest();
		boolean isInsideROI = region.getBounds().contains(e.getX(), e.getY());			
		if (isInsideROI){
			
			ClickEvent event = new ClickEvent();
			event.setX(e.getX() - region.getBounds().x);
			event.setY(e.getY() - region.getBounds().y);		
			event.setButton(e.getButton());
			event.setClickCount(e.getClickCount());
			eventDetected(event);
		}
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		//System.out.println("Mosue Pressed: " + e.getButton());
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		//System.out.println("Mosue Released: " + e.getButton());
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		//System.out.println("Mosue Moved: " + e.getX() + ", " + e.getY());
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		//System.out.println("Mosue Dragged: " + e.getX() + ", " + e.getY());
	}

	public void start(){
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			//    		System.exit(1);
		}

		//Construct the example object.
		//GlobalMouseListenerExample example = new GlobalMouseListenerExample();

		//Add the appropriate listeners for the example object.
		GlobalScreen.getInstance().addNativeMouseListener(this);
		GlobalScreen.getInstance().addNativeMouseMotionListener(this);
	}
	
	public void stop(){
		GlobalScreen.getInstance().removeNativeMouseListener(this);
		GlobalScreen.getInstance().removeNativeMouseMotionListener(this);
	}
	

}