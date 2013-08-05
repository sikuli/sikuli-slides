package org.sikuli.slides.api;

import org.sikuli.api.ScreenRegion;

public class Context {
	private ScreenRegion screenRegion;
	public ScreenRegion getScreenRegion() {
		return screenRegion;
	}

	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}
}
