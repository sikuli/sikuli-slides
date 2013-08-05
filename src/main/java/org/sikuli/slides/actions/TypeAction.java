package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeAction implements Action {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void execute(Context context) {
		logger.info("performing keyboard typing event on target...");
		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		mouse.click(screenRegion.getCenter());
		keyboard.type(getText());		
	}
}
