package org.sikuli.slides.api;

import java.util.Map;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.models.Slide;
import org.stringtemplate.v4.ST;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Context {
	private static final long DEFAULT_WAIT_TIME = 5000;
	private static final float DEFAULT_MIN_SCORE = 0.7f;

	private ScreenRegion screenRegion;
	
	// the default slide selector is to accept all
	private SlideSelector slideSelector = new SlideSelector(){
		@Override
		public boolean accept(Slide slide) {			
			return true;
		}		
	};
	private Map<String, Object> parameters = Maps.newHashMap();	
	private float minScore = DEFAULT_MIN_SCORE;	
	// how long to wait for a target in ms
	private long waitTime = DEFAULT_WAIT_TIME;
	
	public Context(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}	
	
	public Context(){
		screenRegion = new DesktopScreenRegion();
	}
	
	public Context createCopy(){
		Context copy = new Context();
		copy.setMinScore(getMinScore());
		copy.setScreenRegion(getScreenRegion());
		copy.setSlideSelector(getSlideSelector());
		copy.setWaitTime(getWaitTime());
		return copy;
	}

	public ScreenRegion getScreenRegion() {
		return screenRegion;
	}

	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}

	public void addParameter(String key, Object value){
		parameters.put(key, value);
	}

	public Map<String, Object> getParameters() {
		return Maps.newHashMap(parameters);
	}
	
	public String render(String tempalteText) {
		ST st = new ST(tempalteText);
		for (Map.Entry<String, Object> e : parameters.entrySet()){
			st.add(e.getKey(), e.getValue());	
		}
		return st.render();
	}

	public SlideSelector getSlideSelector() {
		return slideSelector;
	}

	public void setSlideSelector(SlideSelector slideSelector) {
		this.slideSelector = slideSelector;
	}
	
	public String toString(){
		return Objects.toStringHelper(this)
				.add("minScore", minScore)
				.add("parameter", parameters)				
				.add("screenRegion", screenRegion).toString();
	}

	public float getMinScore() {
		return minScore;
	}

	public void setMinScore(float minScore) {
		this.minScore = minScore;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
}
