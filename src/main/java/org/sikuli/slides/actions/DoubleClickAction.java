package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

public class DoubleClickAction extends ScreenRegionAction {
	
	public DoubleClickAction(ScreenRegion targetRegion){
		setTargetScreenRegion(targetRegion);
	}
	
	/**
	 * perform double click
	 * @param targetRegion the region to perform double click input event on.
	 */	
	protected void exceuteOnScreenRegion(ScreenRegion targetRegion){
		logger.info("performing double click event on target...");
		Mouse mouse = new DesktopMouse();
		mouse.doubleClick(targetRegion.getCenter());
	}
	
	
}
