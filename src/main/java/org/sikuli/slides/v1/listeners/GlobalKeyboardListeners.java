/**
Khalid
*/
package org.sikuli.slides.v1.listeners;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.v1.utils.Constants;
import org.sikuli.slides.v1.utils.Constants.DesktopEvent;

public class GlobalKeyboardListeners implements NativeKeyListener, Runnable{
	private ScreenRegion region;
	private String textToBeTyped;
	private DesktopEvent desktopEvent;
	private AtomicBoolean isPerformed;
	private StringBuilder typedText;
	public GlobalKeyboardListeners(ScreenRegion region, String textToBeTyped, DesktopEvent desktopEvent){
		this.region=region;
		this.textToBeTyped = textToBeTyped.replaceAll("\\s","");
		this.desktopEvent = desktopEvent;
		this.isPerformed = new AtomicBoolean();
		typedText = new StringBuilder();
	}
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if(e.getKeyCode() == NativeKeyEvent.VK_ESCAPE){
			isPerformed.set(true);
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		char ch=e.getKeyChar();
		if(Character.isLetter(ch)){
			typedText.append(ch);
			if(textToBeTyped.equalsIgnoreCase(typedText.toString())){
				isPerformed.set(true);
			}
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
