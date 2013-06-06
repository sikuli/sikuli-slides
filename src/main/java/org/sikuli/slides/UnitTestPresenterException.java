package org.sikuli.slides;

import java.awt.image.BufferedImage;

// describes an exception encountered while presenting slides for
// the purpose of unit testing. 
public class UnitTestPresenterException extends PresenterException {
	
	// TODO
	// Think about what other data fields we can add here.
	// These data fields should provide developers / testers
	// extra information useful for unit-testing but not so useful
	// in general cases.
	// (release target: 1.4)
	
	// a screenshot showing the screen when an exception occurred
	private BufferedImage screenShot;

	public BufferedImage getScreenShot() {
		return screenShot;
	}

	public void setScreenShot(BufferedImage screenShot) {
		this.screenShot = screenShot;
	}

}
