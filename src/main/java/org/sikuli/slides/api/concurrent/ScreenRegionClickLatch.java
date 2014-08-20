package org.sikuli.slides.api.concurrent;

import org.jnativehook.mouse.NativeMouseEvent;
import org.sikuli.api.ScreenRegion;

public class ScreenRegionClickLatch extends ScreenRegionLatch {
	

	public ScreenRegionClickLatch(ScreenRegion screenRegion){
		super(screenRegion);
	}
	
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		if (inRange(e))
			release();
	}	
}