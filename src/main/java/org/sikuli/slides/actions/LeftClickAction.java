package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

public class LeftClickAction extends ScreenRegionAction {
	
	public LeftClickAction(ScreenRegion screenRegion){
		setTargetScreenRegion(screenRegion);
	}
	
	/**
	 * perform left click
	 * @param targetRegion the region to perform left click input event on.
	 */	
	protected void performOnScreenRegion(ScreenRegion targetRegion){
		logger.info("performing left click event on target...");				
		Mouse mouse = new DesktopMouse();
		mouse.click(targetRegion.getCenter());
	}

}