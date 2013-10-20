package org.sikuli.slides.api.slideshow;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

abstract class GlobalHotkeyManager implements NativeKeyListener {
	
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
	}

	final public void stop(){
		GlobalScreen.getInstance().removeNativeKeyListener(this);
	}
	
	final public void nativeKeyReleased(NativeKeyEvent e) {
	}
	final public void nativeKeyTyped(NativeKeyEvent e) {
	}
}