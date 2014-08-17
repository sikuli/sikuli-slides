package org.sikuli.slides.driver;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

public interface Widget {
	
	/** 
	 * Execute a click operation on this widget
	 */
	public void click();	
	
	/** 
	 * Execute a click operation on this widget
	 */
	public void doubeClick();
	
	/** 
	 * Type the given text in this widget
	 */	
	public void type(String text);	
	
	/** 
	 * Highlight the widget briefly
	 */	
	public void highlight();	
	
	
	/**
	 * Get the {@link Target} based on which this widget is found 
	 * 
	 * @return
	 */
	public Target getTarget();
	
	
	/**
	 * Get the text label of this widget (as specified in a slide)
	 * 
	 */
	public String getLabel();

	/** 
	 * Set the @ScreenRegion where widget is found
	 * 
	 */
	public void setScreenRegion(ScreenRegion found);
	
	/** 
	 * Get the @ScreenRegion where this widget can be found
	 * 
	 */	
	public ScreenRegion getScreenRegion();
}