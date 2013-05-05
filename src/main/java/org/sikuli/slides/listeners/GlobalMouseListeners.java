/**
Khalid
*/
package org.sikuli.slides.listeners;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.utils.Constants.DesktopEvent;

public class GlobalMouseListeners implements NativeMouseInputListener, Callable<Boolean>{
	private AtomicBoolean result;
	private ScreenRegion region;
	private DesktopEvent desktopEvent;
	
	public GlobalMouseListeners(ScreenRegion region, DesktopEvent desktopEvent){
		this.region=region;
		this.desktopEvent=desktopEvent;
		result=new AtomicBoolean();
	}
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		
    	if(inRange(e)){
        	if(e.getClickCount()>1){
        		if(desktopEvent==DesktopEvent.DOUBLE_CLICK){
        			System.out.println("Double click");
        			result.set(true);
        		}
        	}
        	else{
        		if(desktopEvent==DesktopEvent.LEFT_CLICK){
        			System.out.println("Click");
        			result.set(true);
        		}
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
		int x_val=(int)region.getBounds().getX();
		int y_val=(int)region.getBounds().getY();
		int max_x=(int)(this.region.getBounds().width +x_val);
		int max_y=(int)(this.region.getBounds().height + y_val);
		int clicked_x=e.getX();
		int clicked_y=e.getY();
		
		//System.out.println("Clicked on ("+clicked_x+","+clicked_y+")");
		//System.out.println("x_val= "+x_val+", max_x="+max_x);
		//System.out.println("y_val= "+y_val+", max_y="+max_y);
		
		if(clicked_x>=x_val&&clicked_x<=max_x){
			if(clicked_y>=y_val && clicked_y<=max_y){
				System.out.println("Correct");
				return true;
			}
		}
		return false;
	}
	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
	}

	@Override
	public Boolean call(){
		while(result.get()==false){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result.get();
	}
	
}
