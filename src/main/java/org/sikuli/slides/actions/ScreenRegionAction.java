package org.sikuli.slides.actions;

import java.awt.Rectangle;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.visual.Canvas;
import org.sikuli.api.visual.ScreenRegionCanvas;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class ScreenRegionAction implements Action {

	private ScreenRegion targetScreenRegion;
	protected Logger logger = LoggerFactory.getLogger(ScreenRegionAction.class);

	@Override
	public void execute(Context context) throws ActionExecutionException{
		if (targetScreenRegion != null) {
			Rectangle r = targetScreenRegion.getBounds();
			if (r != null){
				Canvas canvas = new ScreenRegionCanvas(targetScreenRegion);
				canvas.addBox(targetScreenRegion);
				canvas.show();
				exceuteOnScreenRegion(targetScreenRegion);
				canvas.hide();
			}else{
				throw new ActionExecutionException(this);
			}
		}
	}	

	// subclasses must implement this
	abstract protected void exceuteOnScreenRegion(ScreenRegion targetRegion);

	public ScreenRegion getTargetScreenRegion() {
		return targetScreenRegion;
	}

	public void setTargetScreenRegion(ScreenRegion targetRegion) {
		this.targetScreenRegion = targetRegion;
	}

}
