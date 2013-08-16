package org.sikuli.slides.api.actions;

import org.sikuli.api.Relative;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.Context;

import com.google.common.base.Objects;

// Execute a target action on a new screen region relative to
// the screen region given in the context. It can be used in conjunction
// with a TargetAction to execute an action not directly on a target
// but in another area relative to the target.
public class RelativeAction extends DefaultAction {
	
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;	
	
	private double xmin = 0;
	private double xmax = 1;
	private double ymin = 0;
	private double ymax = 1;
	
	private boolean isPixelUnit;
	
	public RelativeAction(int x, int y, int width, int height, Action targetAction){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isPixelUnit = true;
		addChild(targetAction);
	}
	
	public RelativeAction(double xmin, double ymin, double xmax, double ymax, Action targetAction){
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.isPixelUnit = false;
		addChild(targetAction);
	}
	
	@Override
	public void execute(Context context) throws ActionExecutionException {
		logger.debug("executing " + this);
		ScreenRegion screenRegion = context.getScreenRegion();
		
		ScreenRegion targetRegion;
		if (isPixelUnit){
			targetRegion = Relative.to(screenRegion).region(x,y,width,height).getScreenRegion();
		}else{
			targetRegion = Relative.to(screenRegion).region(xmin, ymin, xmax, ymax).getScreenRegion();
		}
		
		Context childConext = context.createCopy();			
		childConext.setScreenRegion(targetRegion);
		for (Action child : getChildren()){
			child.execute(childConext);
		}
	}
	
	public String toString(){
		if (isPixelUnit){
		return Objects.toStringHelper(this)
				.add("x", x)
				.add("y", y)
				.add("width", width)
				.add("height", height)
				.add("children", getChildren())
				.toString();
		}else{
			return Objects.toStringHelper(this)
					.add("x", String.format("(%.2f,%.2f)", xmin, xmax))
					.add("y", String.format("(%.2f,%.2f)", ymin, ymax))
					.add("children", getChildren())
					.toString();			
		}
	}

}