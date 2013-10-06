package org.sikuli.slides.api.concurrent;

import org.jnativehook.keyboard.NativeKeyEvent;

public class EscapeKeyLatch extends NativeInputLatch{
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE){
			release();
		}
	}
}