/**
@author Khalid Alharbi
*/
package org.sikuli.slides.v1.sikuli;

import java.awt.Color;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.v1.listeners.GlobalKeyboardListeners;
import org.sikuli.slides.v1.listeners.GlobalMouseListeners;
import org.sikuli.slides.v1.shapes.SlideShape;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.UserPreferencesEditor;
import org.sikuli.slides.v1.utils.Constants.DesktopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlideTutorial {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SlideTutorial.class);
	private UserPreferencesEditor prefsEditor = new UserPreferencesEditor();
	private ScreenRegion targetRegion;
	private Constants.DesktopEvent desktopEvent;
	private SlideShape slideShape;
	
	/**
	 * A slide tutorial to be used in tutorial mode and help mode
	 * @param targetRegion
	 * @param slideShape
	 * @param desktopEvent
	 * @param observable UI observable used to observe tutorial mode navigation status.
	 */
	public SlideTutorial(ScreenRegion targetRegion, SlideShape slideShape, Constants.DesktopEvent desktopEvent){
		this.targetRegion=targetRegion;
		this.slideShape=slideShape;
		this.desktopEvent=desktopEvent;
	}
	
	private String getActionDisplayName(){
		if(desktopEvent==DesktopEvent.LEFT_CLICK){
			return "Click here";
		}
		else if(desktopEvent==DesktopEvent.DOUBLE_CLICK){
			return "Double click here";
		}
		else if(desktopEvent==DesktopEvent.RIGHT_CLICK){
			return "Right click here";
		}
		else if(desktopEvent==DesktopEvent.DRAG_N_DROP){
			if(slideShape.getOrder()==0)
				return "Drag this";
			else if(slideShape.getOrder()==1)
				return "and drop it here";
		}
		else if(desktopEvent==DesktopEvent.KEYBOARD_TYPING){
			return "Click and type: "+ slideShape.getText()+" here";
		}
		return "";
	}
	
	public void performTutorialSlideAction() {
		if(targetRegion==null){
			return;
		}
		Canvas canvas=new ScreenRegionCanvas(new DesktopScreenRegion(Constants.ScreenId));
		canvas.addBox(targetRegion).withLineWidth(prefsEditor.getCanvasWidthSize());
		int x=targetRegion.getBounds().x;
		int y=targetRegion.getBounds().y;
		int w=targetRegion.getBounds().width;
		int h=targetRegion.getBounds().height;
		ScreenRegion labelRegion=new DesktopScreenRegion(Constants.ScreenId,x,y-h,w,h);
		canvas.addLabel(labelRegion, getActionDisplayName())
			.withColor(Color.black).withFontSize(prefsEditor.getInstructionHintFontSize());;
        logger.info("Waiting for the user to pefrom "+this.desktopEvent.toString()+" on the highlighted target.");
		try {
        	if(!GlobalScreen.isNativeHookRegistered()){
        		GlobalScreen.registerNativeHook();
        	}
            if(this.desktopEvent==DesktopEvent.KEYBOARD_TYPING){
            	// add native keyboard listener
            	GlobalKeyboardListeners globalKeyboardListener=new GlobalKeyboardListeners(targetRegion,slideShape.getText(),desktopEvent);
            	GlobalScreen.getInstance().addNativeKeyListener(globalKeyboardListener);
            	// display canvas around the target until the user performs the appropriate action
            	canvas.displayWhile(globalKeyboardListener);
            	// Remove native keyboard listener
            	GlobalScreen.getInstance().removeNativeKeyListener(globalKeyboardListener);
            	
            }
            else if(this.desktopEvent==DesktopEvent.LEFT_CLICK ||
            		this.desktopEvent==DesktopEvent.RIGHT_CLICK ||
            		this.desktopEvent==DesktopEvent.DOUBLE_CLICK ||
            		this.desktopEvent==DesktopEvent.DRAG_N_DROP){
            	// add native mouse listener
            	GlobalMouseListeners globalMouseListener=new GlobalMouseListeners(targetRegion,desktopEvent);
            	GlobalScreen.getInstance().addNativeMouseListener(globalMouseListener);
            	// display canvas around the target until the user performs the appropriate action
            	canvas.displayWhile(globalMouseListener);
            	// Remove native mouse listener
            	GlobalScreen.getInstance().removeNativeMouseListener(globalMouseListener);
            }
            else if(this.desktopEvent==DesktopEvent.LAUNCH_BROWSER){
            	//TODO:
            }
            else if(this.desktopEvent==DesktopEvent.EXIST||this.desktopEvent==DesktopEvent.NOT_EXIST){
            	//TODO:
            }
        }
        catch (NativeHookException ex) {
            logger.error("There was a problem in running the tutorial mode.");
            System.exit(1);
        }
	}
}
