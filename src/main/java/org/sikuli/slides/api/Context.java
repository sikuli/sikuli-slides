package org.sikuli.slides.api;

import java.util.Map;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.listeners.ExecutionVisualizer;
import org.sikuli.slides.api.models.Slide;
import org.stringtemplate.v4.ST;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Context {
	private static final long DEFAULT_WAIT_TIME = 5000;
	private static final float DEFAULT_MIN_SCORE = 0.7f;

	private ScreenRegion screenRegion;
	private Slide slide;	
	private ExecutionListener executionListener = new ExecutionVisualizer();
	
	// the default slide selector is to accept all
	private ExecutionFilter filter = ExecutionFilter.Factory.createAllFilter();
	private Map<String, Object> parameters = Maps.newHashMap();	
	private float minScore = DEFAULT_MIN_SCORE;	
	// how long to wait for a target in ms
	private long waitTime = DEFAULT_WAIT_TIME;
	
	public Context(ScreenRegion screenRegion) {
		this.screenRegion = screenRegion;
	}
	
	// Construct a new context based on the given context
	// and set it to a new slide.
	public Context(Context context, Slide slide){
		copyFrom(context);
		this.slide = slide;
	}

	// Construct a new context based on the given context
	// and set it to a new ScreenRegion.
	public Context(Context context, ScreenRegion screenRegion){
		copyFrom(context);
		this.screenRegion = screenRegion;
	}
	
	public Context(){
		screenRegion = new DesktopScreenRegion();
	}
	
	public Context(Context context) {
		copyFrom(context);
	}

	private void copyFrom(Context copy){
		setMinScore(copy.getMinScore());
		setScreenRegion(copy.getScreenRegion());
		setExecutionFilter(copy.getExecutionFilter());
		setExecutionListener(copy.getExecutionListener());
		setWaitTime(copy.getWaitTime());
		setSlide(copy.getSlide());
	}
	
//	public Context createCopy(){
//		Context copy = new Context();
//		copy.setMinScore(getMinScore());
//		copy.setScreenRegion(getScreenRegion());
//		copy.setExecutionFilter(getExecutionFilter());
//		copy.setWaitTime(getWaitTime());
//		copy.setSlide(getSlide());
//		return copy;
//	}

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
 
	public ExecutionFilter getExecutionFilter() {
		return filter;
	}

	// The filter can be used to accept or reject an execution event. 
	// For instance, accept only those with a slide number > 3
	// or accept only those whose Action is not a TYPE action
	public void setExecutionFilter(ExecutionFilter filter) {
		this.filter = filter;
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

	public ExecutionListener getExecutionListener() {
		return executionListener;
	}

	public void setExecutionListener(ExecutionListener executionListener) {
		this.executionListener = executionListener;
	}

	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}
}
