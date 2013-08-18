/**
Khalid
*/
package org.sikuli.slides.v1.listeners;

import java.util.concurrent.atomic.AtomicBoolean;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.Constants.DesktopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalMouseListeners implements NativeMouseInputListener, Runnable{
	private static final Logger logger = (Logger) LoggerFactory.getLogger(GlobalMouseListeners.class);
	private AtomicBoolean isPerformed;
	private ScreenRegion region;
	private DesktopEvent desktopEvent;
	private static AtomicBoolean isDragged=new AtomicBoolean();
	
	public GlobalMouseListeners(ScreenRegion region, DesktopEvent desktopEvent){
		this.region=region;
		this.desktopEvent=desktopEvent;
		this.isPerformed=new AtomicBoolean();
	}
	
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		// Double click
    	if(desktopEvent==DesktopEvent.DOUBLE_CLICK && e.getClickCount()==2){
    		if(inRange(e)){
    			logger.info("Double click action was successfully performed.");
    			logger.info("========================================");
    			isPerformed.set(true);
    		}
    		else{
    			handleClickError();
    		}
    	}
    	// Left click
    	else if(desktopEvent==DesktopEvent.LEFT_CLICK){
    		if(inRange(e)){
    			logger.info("Click action was successfully performed.");
    			logger.info("========================================");
        		isPerformed.set(true);
    		}
    		else{
    			handleClickError();
    		}
    	}
    	// Right click
    	else if(desktopEvent==DesktopEvent.RIGHT_CLICK && e.getButton()==NativeMouseEvent.BUTTON2){
    		if(inRange(e)){
    			logger.info("Right click action was successfully performed.");
    			logger.info("========================================");
    			isPerformed.set(true);
    		}
    		else{
    			handleClickError();
    		}
    	}
	}
	/**
	 * Check the mouse action is within the screen region range.
	 * @param e native mouse event
	 * @return true if the mouse event was within the screen region range. 
	 * Otherwise, it returns false.
	 */
	private boolean inRange(NativeMouseEvent e) {
		int x_val=(int) this.region.getBounds().getX() + Constants.Total_Screen_Width;
		int y_val=(int) this.region.getBounds().getY();
		int max_x=(int) (this.region.getBounds().width +x_val);
		int max_y=(int) (this.region.getBounds().height + y_val);
		int clicked_x=e.getX();
		int clicked_y=e.getY();
		
		if(clicked_x>=x_val&&clicked_x<=max_x &&
				clicked_y>=y_val && clicked_y<=max_y){
			return true;
		}
		return false;
	}
	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		// Drag and drop : Dragged
		if(desktopEvent==DesktopEvent.DRAG_N_DROP){
			if(inRange(e)){
				GlobalMouseListeners.isDragged.set(true);
				logger.info("Interacting with drag target...");
				isPerformed.set(true);
			}
    		else{
    			handleClickError();
    		}
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		// Drag and drop : Dropped
		if(desktopEvent==DesktopEvent.DRAG_N_DROP && GlobalMouseListeners.isDragged.get()){
			if(inRange(e)){
				logger.info("Drag and drop action was successfully performed.");
				logger.info("========================================");
				isPerformed.set(true);
			}
    		else{
    			handleClickError();
    		}
		}
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
	}
	
	private void handleClickError(){
		logger.info("Error: Please click inside the heighlighted rectangle.");
	}
	
	@Override
	public void run() {		
		while(!isPerformed.get()){
			try{
				Thread.sleep(100);
				if(Constants.IsPreviousStep){
					Constants.IsPreviousStep=false;
					break;
				}
				else if(Constants.IsNextStep){
					Constants.IsNextStep=false;
					break;
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}
