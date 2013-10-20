package org.sikuli.slides.api.slideshow;

import java.awt.event.KeyEvent;

import org.jnativehook.keyboard.NativeKeyEvent;

class SlideShowHotkeyManager extends GlobalHotkeyManager<SlideShowController>{

	private SlideShowController slideshow;

	public SlideShowHotkeyManager(SlideShowController target) {
		super(target);
		slideshow = target;
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (isAltPressed(e) && isCtrlPressed(e)){
			if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				slideshow.next();
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT){			
				slideshow.previous();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
				slideshow.quit();
			}else if (e.getKeyCode() == KeyEvent.VK_P){
			 
				if (slideshow.isPaused()){
					slideshow.play();
				}else{
					slideshow.pause();
				}
			}
		}
	}
}