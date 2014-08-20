package org.sikuli.slides.api.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.jnativehook.mouse.NativeMouseEvent;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.desktop.DesktopScreen;

class ScreenRegionLatch extends NativeInputLatch {
	
	private ScreenRegion screenRegion;
	private int screenOffsetX;
	private int screenOffsetY;

	ScreenRegionLatch(ScreenRegion screenRegion){
		this.screenRegion = checkNotNull(screenRegion);		

		// calculate the x,y offsets of the target screen, which can be 
		// the secondary screen. So we can map the x,y given by NativeHook
		// to the x,y of the ScreenRegion object
		int id =  ((DesktopScreen) screenRegion.getScreen()).getScreenId();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice [] devices = ge.getScreenDevices();
		Rectangle bounds = devices[id].getDefaultConfiguration().getBounds();
		this.screenOffsetX = bounds.x; 
		this.screenOffsetY = bounds.y; 
	}
	
	/**
	 * Check the mouse action is within the screen region range.
	 * @param e native mouse event
	 * @return true if the mouse event was within the screen region range. 
	 * Otherwise, it returns false.
	 */
	protected boolean inRange(NativeMouseEvent e){
		Rectangle r = screenRegion.getBounds();
		r.x += screenOffsetX;
		r.y += screenOffsetY;

		int x = e.getX();
		int y = e.getY();
		return r.contains(x,y);
	}
}