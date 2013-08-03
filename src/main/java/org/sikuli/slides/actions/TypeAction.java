package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

public class TypeAction extends ScreenRegionAction {

	private String text;

	public TypeAction(ScreenRegion screenRegion){
		setTargetScreenRegion(screenRegion);
	}

	/**
	 * perform type
	 * @param targetRegion the region to click into and type
	 */	
	protected void performOnScreenRegion(ScreenRegion targetRegion){
		logger.info("performing keyboard typing event on target...");
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		mouse.click(targetRegion.getCenter());
		keyboard.type(getText());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
