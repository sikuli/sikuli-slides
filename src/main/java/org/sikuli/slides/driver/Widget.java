package org.sikuli.slides.driver;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

public interface Widget {
	
	/** 
	 * Execute a click operation on this widget
	 */
	public void click();	
	
	/** 
	 * Execute a double click operation on this widget
	 */
	public void doubeClick();
	
	/** 
	 * Execute a right click operation on this widget
	 */
	public void rightClick();
		
	/** 
	 * Type the given text at this widget
	 * 
	 * @param text text to type
	 */	
	public void type(String text);	
	
	/**
	 * Paste the given text at this widget
	 * 
	 * @param text text to paste
	 */
	void paste(String text);
	
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