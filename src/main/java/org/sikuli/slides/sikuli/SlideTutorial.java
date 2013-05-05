/**
Khalid
*/
package org.sikuli.slides.sikuli;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.listeners.GlobalMouseListeners;
import org.sikuli.slides.utils.Constants;

public class SlideTutorial {
	private ScreenRegion targetRegion;
	private Constants.DesktopEvent desktopEvent;
	
	public SlideTutorial(ScreenRegion targetRegion, Constants.DesktopEvent desktopEvent){
		this.targetRegion=targetRegion;
		this.desktopEvent=desktopEvent;
	}
	
	public void performTutorialSlideAction() {
        SikuliController.displayBox(targetRegion);
        System.out.print("Waiting for user to pefrom "+this.desktopEvent.toString()+" on target.");
		try {
        	if(!GlobalScreen.isNativeHookRegistered()){
        		GlobalScreen.registerNativeHook();
        	}
        	GlobalMouseListeners globalMouseListener=new GlobalMouseListeners(targetRegion,desktopEvent);
            GlobalScreen.getInstance().addNativeMouseListener(globalMouseListener);
            final ExecutorService service=Executors.newFixedThreadPool(1);
            final Future<Boolean>task=service.submit(globalMouseListener);
            
            performUserAction(task);
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem running the tutorial mode.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

		
	}
	
	private void performUserAction(final Future<Boolean>task){
		try{
			if(task.get()==true){
			// passed and add another task
			System.out.println(desktopEvent.toString()+ " performed!");
			return;
			}
		}
        catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
