package org.sikuli.slides.api.slideshow;

import java.awt.event.KeyEvent;

import org.jnativehook.keyboard.NativeKeyEvent;

class SlideShowHotkeyManager extends GlobalHotkeyManager{

	private SlideShowController slideShow;

	public SlideShowHotkeyManager(SlideShowController target) {
		slideShow = target;
	}
	
	public SlideShowHotkeyManager() {
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (slideShow == null)
			return;
		
		if (isAltPressed(e) && isCtrlPressed(e)){
			if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				slideShow.next();
			}else if (e.getKeyCode() == KeyEvent.VK_LEFT){			
				slideShow.previous();
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
				slideShow.quit();
			}else if (e.getKeyCode() == KeyEvent.VK_P){
			 
				if (slideShow.isPaused()){
					slideShow.play();
				}else{
					slideShow.pause();
				}
			}
		}
	}

	public void setSlideShow(SlideShowController slideShow) {
		this.slideShow = slideShow;
	}
}