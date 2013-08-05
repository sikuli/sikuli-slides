package org.sikuli.slides.actions;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleClickAction implements Action {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void execute(Context context) {
		logger .info("performing double click event on target...");
		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		ScreenLocation loc = screenRegion.getCenter();
		mouse.doubleClick(loc);
	}


}
