package org.sikuli.slides.api.concurrent;

import org.jnativehook.mouse.NativeMouseEvent;
import org.sikuli.api.ScreenRegion;

public class ScreenRegionHoverLatch extends ScreenRegionLatch {
	

	public ScreenRegionHoverLatch(ScreenRegion screenRegion){
		super(screenRegion);
	}
	
	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		if (inRange(e))
			release();
	}	
}