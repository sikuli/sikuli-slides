package org.sikuli.slides.actions;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;

public class LeftClickAction implements TargetScreenRegionAction {
	
	public LeftClickAction(){
	}

	@Override
	public void execute(Context context, ScreenRegion screenRegion) {
		Mouse mouse = new DesktopMouse();
		ScreenLocation loc = screenRegion.getCenter();
		mouse.click(loc);
	}
}
