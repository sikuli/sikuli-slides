package org.sikuli.slides.api.slideshow;

import java.awt.event.KeyEvent;

import org.jnativehook.keyboard.NativeKeyEvent;

class SlideShowHotkeyManager extends GlobalHotkeyManager<SlideShowController>{

	public SlideShowHotkeyManager(SlideShowController target) {
		super(target);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (isAltPressed(e) && isCtrlPressed(e)){
			if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				getTarget().next();
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT){			
				getTarget().previous();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
				getTarget().quit();
			}else if (e.getKeyCode() == KeyEvent.VK_A){
//				toggleAutoAdvance();
			}
		}

	}
}