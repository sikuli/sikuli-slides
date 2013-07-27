/**
Khalid
*/
package org.sikuli.slides.listeners;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.utils.Constants;
import org.sikuli.slides.utils.Constants.DesktopEvent;

public class GlobalKeyboardListeners implements NativeKeyListener, Runnable{
	private ScreenRegion region;
	private String textToBeTyped;
	private DesktopEvent desktopEvent;
	private AtomicBoolean isPerformed;
	private StringBuilder typedText;
	public GlobalKeyboardListeners(ScreenRegion region, String textToBeTyped, DesktopEvent desktopEvent){
		this.region=region;
		this.textToBeTyped=textToBeTyped;
		this.desktopEvent=desktopEvent;
		this.isPerformed=new AtomicBoolean();
		typedText=new StringBuilder();
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO: Fix user's typing errors. Use OCR to make sure the user types the required text.
		char ch=e.getKeyChar();
		typedText.append(ch);
		if(textToBeTyped.equalsIgnoreCase(typedText.toString())){
			this.isPerformed.set(true);
		}
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
