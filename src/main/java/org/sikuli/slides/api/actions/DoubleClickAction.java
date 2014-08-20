package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class DoubleClickAction extends RobotAction {
	
	@Override
	protected void doExecute(Context context) {
		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		ScreenLocation loc = screenRegion.getCenter();
		mouse.doubleClick(loc);
	}

	public String toString(){
		return Objects.toStringHelper(this).toString();
	}
}
