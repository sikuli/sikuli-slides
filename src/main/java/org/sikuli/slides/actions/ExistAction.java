package org.sikuli.slides.actions;

import org.sikuli.api.ScreenRegion;

public class ExistAction extends ScreenRegionAction {
	
	public ExistAction(ScreenRegion screenRegion){
		setTargetScreenRegion(screenRegion);
	}
	
	/**
	 * perform exist
	 * @param targetRegion 
	 */	
	protected void exceuteOnScreenRegion(ScreenRegion targetRegion){
		// do nothing
		// an ActionRuntimeException will be thrown by its super-class if 
		// the target region is null or a NullScreenRegion
	}
}