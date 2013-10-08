package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

public class TypeAction extends RobotAction {
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void doExecute(Context context) {
		String textToType = context.render(getText());
		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		mouse.click(screenRegion.getCenter());
		keyboard.type(textToType);		
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("text", text).toString();
	}

}

