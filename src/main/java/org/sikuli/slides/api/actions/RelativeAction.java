package org.sikuli.slides.api.actions;

import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

// Execute a target action on a new screen region relative to
// the screen region given in the context. It can be used in conjunction
// with a TargetAction to execute an action not directly on a target
// but in another area relative to the target.
public class RelativeAction implements Action {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;
	private Action targetAction;
	
	public RelativeAction(int x, int y, int width, int height, Action targetAction){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.targetAction = targetAction;
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		logger.info("executing " + this);
		ScreenRegion screenRegion = context.getScreenRegion();
		ScreenRegion targetRegion = screenRegion.getRelativeScreenRegion(x, y, width, height);
		Context subConext = context.createCopy();			
		subConext.setScreenRegion(targetRegion);
		targetAction.execute(subConext);
	}
	public String toString(){
		return Objects.toStringHelper(this)
				.add("x", x)
				.add("y", y)
				.add("width", width)
				.add("height", height)
				.add("action", targetAction).toString();
	}

}