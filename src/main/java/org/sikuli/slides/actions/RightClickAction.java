package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

public class RightClickAction extends ScreenRegionAction {
	
	public RightClickAction(ScreenRegion targetRegion){
		setTargetScreenRegion(targetRegion);		
	}
	

	/**
	 * perform right click
	 * @param targetRegion the region to perform right click input event on.
	 */	
	protected void performOnScreenRegion(ScreenRegion targetRegion) {
		logger.info("performing right click event on target...");		
		Mouse mouse = new DesktopMouse();
		mouse.rightClick(targetRegion.getCenter());
	}


}
