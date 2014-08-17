package org.sikuli.slides.driver;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.api.robot.desktop.DesktopMouse;

import com.google.common.base.Objects;

class SlideUIElement implements UIElement {

	private Target target;
	private String label;
	private ScreenRegion screenRegion = null;
	
	private Mouse mouse = new DesktopMouse();
	private Keyboard keyboard = new DesktopKeyboard();
	
	public SlideUIElement(){
		
	}

	public SlideUIElement(Target t, String label){
		this.target = t;
		this.label = label;
	}

	public Target getTarget() {
		return target;
	}

	public String getLabel() {
		return label;
	}	
	
	public void click(){
		ScreenRegion r = getScreenRegion();
		if (r != null){
			mouse.click(r.getCenter());
		}
	}
	
	@Override
	public void doubeClick() {
		ScreenRegion r = getScreenRegion();
		if (r != null){
			mouse.doubleClick(r.getCenter());
		}		
	}
	
	@Override
	public void type(String text) {
		ScreenRegion r = getScreenRegion();
		if (r != null){
			mouse.click(r.getCenter());
			keyboard.type(text);
		}	
	}	
	
	public String toString(){
		return Objects.toStringHelper(getClass().getSimpleName())
				.add("label", label)
				.add("target", target)				
				.toString();
	}
	
	@Override
	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}
	
	@Override
	public ScreenRegion getScreenRegion(){
		return screenRegion;
	}




	
	
}