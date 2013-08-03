package org.sikuli.slides.actions;

import java.awt.Rectangle;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.ActionRuntimeException;

public class NotExistAction implements Action {
	
	ScreenRegion targetScreenRegion;
	public NotExistAction(ScreenRegion targetScreenRegion){
		this.targetScreenRegion = targetScreenRegion; 
	}
	
	@Override
	public void perform(){
		if (targetScreenRegion != null){
			Rectangle bounds = targetScreenRegion.getBounds();
			if (bounds != null){
				// the bounds are valid, the target can be found, but
				// it's not supposed to exist.
				throw new ActionRuntimeException(this);
			}
		}
	}	

}