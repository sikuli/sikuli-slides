package org.sikuli.slides.sikuli;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import org.sikuli.api.Screen;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.event.StateChangeListener;
import org.sikuli.api.event.TargetEventListener;

public class NullScreenRegion implements ScreenRegion {
	
	Screen screen;
	public NullScreenRegion(Screen screen){
		this.screen = screen;
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public void setBounds(Rectangle newBounds) {
	}

	@Override
	public Screen getScreen() {
		return screen;
	}

	@Override
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Override
	public ScreenRegion getRelativeScreenRegion(int xoffset, int yoffset,
			int width, int height) {
		return null;
	}

	@Override
	public ScreenLocation getRelativeScreenLocation(int xoffset, int yoffset) {
		return null;
	}

	@Override
	public ScreenLocation getUpperLeftCorner() {
		return null;
	}

	@Override
	public ScreenLocation getLowerLeftCorner() {
		return null;
	}

	@Override
	public ScreenLocation getUpperRightCorner() {
		return null;
	}

	@Override
	public ScreenLocation getLowerRightCorner() {
		return null;
	}

	@Override
	public ScreenLocation getCenter() {
		return null;
	}

	@Override
	public double getScore() {
		return 0;
	}

	@Override
	public void setScore(double score) {
	}

	@Override
	public void addState(Target target, Object state) {
	}

	@Override
	public void removeState(Target target) {
	}

	@Override
	public Object getState() {
		return null;
	}

	@Override
	public List<ScreenRegion> findAll(Target target) {
		return null;
	}

	@Override
	public ScreenRegion find(Target target) {
		return null;
	}

	@Override
	public ScreenRegion wait(Target target, int mills) {
		return null;
	}

	@Override
	public BufferedImage capture() {
		return null;
	}

	@Override
	public void addTargetEventListener(Target target,
			TargetEventListener listener) {
	}

	@Override
	public void removeTargetEventListener(Target target,
			TargetEventListener listener) {
	}

	@Override
	public ScreenRegion snapshot() {
		return null;
	}

	@Override
	public void addStateChangeEventListener(StateChangeListener listener) {		
	}

	@Override
	public Map<Target, Object> getStates() {
		return null;
	}

	@Override
	public void addROI(int x, int y, int width, int height) {		
	}

	@Override
	public List<Rectangle> getROIs() {
		return null;
	}

	@Override
	public BufferedImage getLastCapturedImage() {
		return null;
	}
	
}