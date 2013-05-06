/**
Khalid
*/
package org.sikuli.slides.sikuli;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.DesktopCanvas;
import org.sikuli.slides.listeners.GlobalKeyboardListeners;
import org.sikuli.slides.listeners.GlobalMouseListeners;
import org.sikuli.slides.shapes.SlideShape;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Constants.DesktopEvent;

public class SlideTutorial {
	private ScreenRegion targetRegion;
	private Constants.DesktopEvent desktopEvent;
	private SlideShape slideShape;
	
	public SlideTutorial(ScreenRegion targetRegion, SlideShape slideShape, Constants.DesktopEvent desktopEvent){
		this.targetRegion=targetRegion;
		this.slideShape=slideShape;
		this.desktopEvent=desktopEvent;
	}
	
	public void performTutorialSlideAction() {
		Canvas canvas=new DesktopCanvas();
		canvas.addBox(targetRegion);
        System.out.println("Waiting for the user to pefrom "+this.desktopEvent.toString()+" on the highlighted target.");
		try {
        	if(!GlobalScreen.isNativeHookRegistered()){
        		GlobalScreen.registerNativeHook();
        	}
            if(this.desktopEvent==DesktopEvent.KEYBOARD_TYPING){
            	GlobalKeyboardListeners globalKeyboardListener=new GlobalKeyboardListeners(targetRegion,slideShape.getText(),desktopEvent);
            	GlobalScreen.getInstance().addNativeKeyListener(globalKeyboardListener);
            	canvas.displayWhile(globalKeyboardListener);
            }
            else{
            	GlobalMouseListeners globalMouseListener=new GlobalMouseListeners(targetRegion,desktopEvent);
            	GlobalScreen.getInstance().addNativeMouseListener(globalMouseListener);
            	canvas.displayWhile(globalMouseListener);
            }
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem running the tutorial mode.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

		
	}
}
