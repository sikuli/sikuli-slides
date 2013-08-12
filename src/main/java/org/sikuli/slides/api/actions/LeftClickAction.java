package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

public class LeftClickAction implements Action {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void execute(Context context) {
		logger .info("performing left click event on target...");
		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		ScreenLocation loc = screenRegion.getCenter();
		mouse.click(loc);
	}
	
	public String toString(){
		return Objects.toStringHelper(this).toString();
	}
}