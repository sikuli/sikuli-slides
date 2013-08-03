package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class ScreenRegionAction implements Action {

	private ScreenRegion targetScreenRegion;
	protected Logger logger = LoggerFactory.getLogger(ScreenRegionAction.class);

	public void perform(){
		if (getTargetScreenRegion() != null){
			displayBoxOnRegion(targetScreenRegion);
			performOnScreenRegion(targetScreenRegion);
		}
	}	

	// Display the canvas around the target
	static public void displayBoxOnRegion(ScreenRegion screenRegion){
		if(screenRegion != null){
			Canvas canvas = new ScreenRegionCanvas(screenRegion);
			canvas.addBox(screenRegion);
			canvas.display(2);
		}
	}

	// subclasses must implement this
	abstract protected void performOnScreenRegion(ScreenRegion targetRegion);

	public ScreenRegion getTargetScreenRegion() {
		return targetScreenRegion;
	}

	public void setTargetScreenRegion(ScreenRegion targetRegion) {
		this.targetScreenRegion = targetRegion;
	}

}
