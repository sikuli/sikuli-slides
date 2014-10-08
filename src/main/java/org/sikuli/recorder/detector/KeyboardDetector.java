package org.sikuli.recorder.detector;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;



public class KeyboardDetector implements NativeKeyListener {

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}

	public void stop(){
		GlobalScreen.getInstance().removeNativeKeyListener(this);
	}

	public void start(){
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

}
