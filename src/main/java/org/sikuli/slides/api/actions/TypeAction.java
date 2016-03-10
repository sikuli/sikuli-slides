package org.sikuli.slides.api.actions;

import java.util.List;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.slides.api.Context;
import org.apache.commons.lang.StringEscapeUtils;
import org.sikuli.slides.api.interpreters.TypeStringParser;
import org.sikuli.slides.api.interpreters.TypeStringParser.TypeStringPart;

import com.google.common.base.Objects;

public class TypeAction extends RobotAction {
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	String interpretAsKeyString(String name){
		if (name.equals("ENTER")){
			return Key.ENTER;
		}else if (name.equals("ESC")){
			return Key.ESC;
		}else if (name.equals("LEFT")){
			return Key.LEFT;
		}else if (name.equals("UP")){
			return Key.UP;
		}else if (name.equals("RIGHT")){
			return Key.RIGHT;
		}else if (name.equals("DOWN")){
			return Key.DOWN;
		}else if (name.equals("TAB")){
			return Key.TAB;
		}else if (name.equals("BACKSPACE")){
			return Key.BACKSPACE;
		}else if (name.equals("PAGEUP")){
			return Key.PAGE_UP;
		}else if (name.equals("PAGEDOWN")){
			return Key.PAGE_DOWN;
                }else if (name.equals("ALT")){
			return Key.ALT;
                }else if (name.equals("CTRL")){
			return Key.CTRL;
		}else if (name.equals("F1")){
			return Key.F1;
		}else if (name.equals("F2")){
			return Key.F2;
		}else if (name.equals("F3")){
			return Key.F3;
		}else if (name.equals("F4")){
			return Key.F4;
		}else if (name.equals("F5")){
			return Key.F5;
		}else if (name.equals("F6")){
			return Key.F6;
		}else if (name.equals("F7")){
			return Key.F7;
		}else if (name.equals("F8")){
			return Key.F8;
		}else if (name.equals("F9")){
			return Key.F9;
		}else if (name.equals("F10")){
			return Key.F10;
		}else if (name.equals("F11")){
			return Key.F11;
		}else if (name.equals("F12")){
			return Key.F12;
		}
		return null;
	}

	@Override
	protected void doExecute(Context context) {
		String textToType = context.render(getText());
		textToType = StringEscapeUtils.unescapeJava(textToType);

		ScreenRegion screenRegion = context.getScreenRegion();
		Mouse mouse = new DesktopMouse();
		Keyboard keyboard=new DesktopKeyboard();
		mouse.click(screenRegion.getCenter());
		
		TypeStringParser p = new TypeStringParser();
		List<TypeStringPart> parts = p.parse(textToType);		
		for (TypeStringPart part : parts){
		
			if (part.getType() == TypeStringPart.Type.Key){
				String keyText = part.getText();							
				String key = interpretAsKeyString(keyText);
				if (key != null){
					keyboard.keyDown(key);				
					keyboard.keyUp();			
				}
			}else if (part.getType() == TypeStringPart.Type.Text){			
				keyboard.type(part.getText());
			}			
		}
			
				
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("text", text).toString();
	}

}

