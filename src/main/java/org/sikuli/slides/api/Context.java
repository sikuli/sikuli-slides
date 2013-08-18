package org.sikuli.slides.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ScreenRegion;
import org.sikuli.slides.api.listeners.ExecutionVisualizer;
import org.sikuli.slides.api.models.Slide;
import org.stringtemplate.v4.ST;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

/**
 * Describes the context where an action is executed  
 * <p>
 * Examples of uses:
 * <p>
 * Specify which {@link ScreenRegion} on to execute an action, for instance, to operate only
 * within the left half of the screen or to operate on the entire secondary screen. 
 * <p>
 * Specify image recognition parameters such as minimum score of similarity and the amount
 * of time to wait and retry
 * 
 * @author Sikuli Lab
 *
 */
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
	
	/**
	 * Construct a new context with a particular screen region.
	 * 
	 * @param screenRegion
	 */
	public Context(ScreenRegion screenRegion) {
		this.screenRegion = checkNotNull(screenRegion);
	}
	
	/**
	 * Construct a new context, which is a copy of the
	 * given context but with a different slide.
	 * 
	 * @param context	the context to copy data from
	 * @param slide	the new slide
	 */
	public Context(Context context, Slide slide){
		checkNotNull(context);		
		copyFrom(context);
		this.slide = checkNotNull(slide);		
	}

	/**
	 * Construct a new context, which is a copy of the 
	 * given context but with a different screen region.
	 * 
	 * @param context	the context to copy data from
	 * @param screenRegion	the new screen region
	 */
	public Context(Context context, ScreenRegion screenRegion){
		copyFrom(context);
		this.screenRegion = screenRegion;
	}
	
	/**
	 * Construct a default context. The screen region is 
	 * set to the entire primary screen.
	 */
	public Context(){
		screenRegion = new DesktopScreenRegion();
	}
	
	/**
	 * Construct a context by copying all data from another context
	 * @param context another context to copy data from
	 */
	public Context(Context context) {
		checkNotNull(context);
		copyFrom(context);
	}

	private void copyFrom(Context copy){
		setMinScore(copy.getMinScore());
		setScreenRegion(copy.getScreenRegion());
		setExecutionFilter(copy.getExecutionFilter());
		setExecutionListener(copy.getExecutionListener());
		setWaitTime(copy.getWaitTime());
		setSlide(copy.getSlide());
		parameters = Maps.newHashMap(copy.getParameters());
	}
	
	/**
	 * Get the screen region associated with this context
	 * @return	the screen region
	 */
	public ScreenRegion getScreenRegion() {
		return screenRegion;
	}
	
	/**
	 * Set the screen region of this context
	 * @param screenRegion the screen region
	 */
	public void setScreenRegion(ScreenRegion screenRegion) {
		this.screenRegion = checkNotNull(screenRegion);
	}

	/**
	 * Add a parameter
	 * @param key	key of the parameter
	 * @param value	value of the parameter
	 */
	public void addParameter(String key, Object value){
		parameters.put(key, value);
	}

	/**
	 * Get the parameters map
	 * @return	the parameters map
	 */
	public Map<String, Object> getParameters() {
		return Maps.newHashMap(parameters);
	}
	
	/**
	 * Render the given template string as a string based on the parameter values
	 * associated with this context
	 *  
	 * @param templateText	the template string
	 * @return	the rendered string
	 */
	public String render(String templateText) {
		checkNotNull(templateText);
		ST st = new ST(templateText);
		for (Map.Entry<String, Object> e : parameters.entrySet()){
			st.add(e.getKey(), e.getValue());	
		}
		return st.render();
	}
 
	/**
	 * Get the execution filter associated with this context.
	 * @return	the execution filter
	 */
	public ExecutionFilter getExecutionFilter() {
		return filter;
	}

	/**
	 * Set the execution filter for this context. The filter can be used to accept or reject an execution event. 
	 * For instance, accept only those with a slide number > 3
	 * or accept only those whose Action is not a TYPE action
	 * 
	 * @param filter	the filter
	 */
	public void setExecutionFilter(ExecutionFilter filter) {
		this.filter = filter;
	}
	
	public String toString(){
		return Objects.toStringHelper(this)
				.add("minScore", minScore)
				.add("parameter", parameters)				
				.add("screenRegion", screenRegion).toString();
	}

	/**
	 * Get the minimum score
	 * @return	minimum score
	 */
	public float getMinScore() {
		return minScore;
	}

	/**
	 * Set the minimum score of similarity for something to be considered as a match
	 * @param minScore	the minimum score
	 */
	public void setMinScore(float minScore) {
		this.minScore = minScore;
	}

	/**
	 * Get the wait time
	 * @return	wait time
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * Set the wait time
	 * @param waitTime	time in milliseconds
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * Get the execution listener associated with this context
	 * @return	an {@link ExecutionListener} object
	 */
	public ExecutionListener getExecutionListener() {
		return executionListener;
	}

	/**
	 * Set the execution listener associated with this context
	 * @param executionListener	the {@link ExecutionListener} object
	 */
	public void setExecutionListener(ExecutionListener executionListener) {
		this.executionListener = executionListener;
	}

	/**
	 * Get the slide associated with this context
	 * @return	a {@link Slide} object
	 */
	public Slide getSlide() {
		return slide;
	}

	/**
	 * Set the slide associated with this context
	 * @param slide	a {@link Slide} object
	 */
	public void setSlide(Slide slide) {
		this.slide = slide;
	}

}
