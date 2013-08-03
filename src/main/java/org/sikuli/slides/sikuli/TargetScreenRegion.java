package org.sikuli.slides.sikuli;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import org.sikuli.api.ImageTarget;
import org.sikuli.api.Screen;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.event.StateChangeListener;
import org.sikuli.api.event.TargetEventListener;

public class TargetScreenRegion implements ScreenRegion {
	
	private static final int MAX_WAITTIME = 5000;
	private ScreenRegion targetScreenRegion = null;
	private Target target;
	
	// the screen region to find this target
	private ScreenRegion screenRegion;
	
	public TargetScreenRegion(Target imageTarget, ScreenRegion screenRegion){
		this.target = imageTarget;
		this.screenRegion = screenRegion;
	}	
	
	private ScreenRegion getTargetScreenRegion() {
		if (targetScreenRegion == null){			
			targetScreenRegion = screenRegion.wait(target, MAX_WAITTIME);		
		}
		return targetScreenRegion;
	}
	
	public Rectangle getBounds() {
		return getTargetScreenRegion().getBounds();
	}

	public void setBounds(Rectangle newBounds) {
		getTargetScreenRegion().setBounds(newBounds);
	}

	public Screen getScreen() {
		return getTargetScreenRegion().getScreen();
	}

	public void setScreen(Screen screen) {
		getTargetScreenRegion().setScreen(screen);
	}

	public ScreenRegion getRelativeScreenRegion(int xoffset, int yoffset,
			int width, int height) {
		return getTargetScreenRegion().getRelativeScreenRegion(xoffset, yoffset, width,
				height);
	}

	public ScreenLocation getRelativeScreenLocation(int xoffset, int yoffset) {
		return getTargetScreenRegion().getRelativeScreenLocation(xoffset, yoffset);
	}

	public ScreenLocation getUpperLeftCorner() {
		return getTargetScreenRegion().getUpperLeftCorner();
	}

	public ScreenLocation getLowerLeftCorner() {
		return getTargetScreenRegion().getLowerLeftCorner();
	}

	public ScreenLocation getUpperRightCorner() {
		return getTargetScreenRegion().getUpperRightCorner();
	}

	public ScreenLocation getLowerRightCorner() {
		return getTargetScreenRegion().getLowerRightCorner();
	}

	public ScreenLocation getCenter() {
		return getTargetScreenRegion().getCenter();
	}

	public double getScore() {
		return getTargetScreenRegion().getScore();
	}

	public void setScore(double score) {
		getTargetScreenRegion().setScore(score);
	}

	public void addState(Target target, Object state) {
		getTargetScreenRegion().addState(target, state);
	}

	public void removeState(Target target) {
		getTargetScreenRegion().removeState(target);
	}

	public Object getState() {
		return getTargetScreenRegion().getState();
	}

	public List<ScreenRegion> findAll(Target target) {
		return getTargetScreenRegion().findAll(target);
	}

	public ScreenRegion find(Target target) {
		return getTargetScreenRegion().find(target);
	}

	public ScreenRegion wait(Target target, int mills) {
		return getTargetScreenRegion().wait(target, mills);
	}

	public BufferedImage capture() {
		return getTargetScreenRegion().capture();
	}

	public void addTargetEventListener(Target target,
			TargetEventListener listener) {
		getTargetScreenRegion().addTargetEventListener(target, listener);
	}

	public void removeTargetEventListener(Target target,
			TargetEventListener listener) {
		getTargetScreenRegion().removeTargetEventListener(target, listener);
	}

	public ScreenRegion snapshot() {
		return getTargetScreenRegion().snapshot();
	}

	public void addStateChangeEventListener(StateChangeListener listener) {
		getTargetScreenRegion().addStateChangeEventListener(listener);
	}

	public Map<Target, Object> getStates() {
		return getTargetScreenRegion().getStates();
	}

	public void addROI(int x, int y, int width, int height) {
		getTargetScreenRegion().addROI(x, y, width, height);
	}

	public List<Rectangle> getROIs() {
		return getTargetScreenRegion().getROIs();
	}

	public BufferedImage getLastCapturedImage() {
		return getTargetScreenRegion().getLastCapturedImage();
	}

}
