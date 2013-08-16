/**
Khalid
*/
package org.sikuli.slides.v1.sikuli;

import org.sikuli.slides.v1.utils.Constants.DesktopEvent;

public class TutorialAction{
	private SlideAction slideAction;
	private DesktopEvent desktopEvent;
	private int step;
	
	public TutorialAction(SlideAction slideAction,DesktopEvent desktopEvent, int step){
		this.setSlideAction(slideAction);
		this.setDesktopEvent(desktopEvent);
		this.setStep(step);
	}

	public SlideAction getSlideAction() {
		return slideAction;
	}

	public void setSlideAction(SlideAction slideAction) {
		this.slideAction = slideAction;
	}

	public DesktopEvent getDesktopEvent() {
		return desktopEvent;
	}

	public void setDesktopEvent(DesktopEvent desktopEvent) {
		this.desktopEvent = desktopEvent;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
}
