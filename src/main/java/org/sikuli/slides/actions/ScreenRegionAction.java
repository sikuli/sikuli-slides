package org.sikuli.slides.actions;

import java.awt.Rectangle;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.ActionRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class ScreenRegionAction implements Action {

	private ScreenRegion targetScreenRegion;
	protected Logger logger = LoggerFactory.getLogger(ScreenRegionAction.class);

	public void perform(){
		if (targetScreenRegion != null) {
			Rectangle r = targetScreenRegion.getBounds();
			if (r != null){
				Canvas canvas = new ScreenRegionCanvas(targetScreenRegion);
				canvas.addBox(targetScreenRegion);
				canvas.show();
				performOnScreenRegion(targetScreenRegion);
				canvas.hide();
			}else{
				throw new ActionRuntimeException(this);
			}
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
