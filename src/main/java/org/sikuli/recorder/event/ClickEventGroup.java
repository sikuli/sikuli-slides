package org.sikuli.recorder.event;
public class ClickEventGroup {
	private ClickEvent clickEvent;
	private ScreenShotEvent screenShotEventBefore;
	public ClickEvent getClickEvent() {
		return clickEvent;
	}
	public void setClickEvent(ClickEvent clickEvent) {
		this.clickEvent = clickEvent;
	}
	public ScreenShotEvent getScreenShotEventBefore() {
		return screenShotEventBefore;
	}
	public void setScreenShotEventBefore(ScreenShotEvent screenShotEventBefore) {
		this.screenShotEventBefore = screenShotEventBefore;
	}
}