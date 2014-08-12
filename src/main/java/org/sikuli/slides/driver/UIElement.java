package org.sikuli.slides.driver;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

public interface UIElement {
	
	/** 
	 * Execute a click operation on this element
	 */
	void click();
	
	/** 
	 * Execute a click operation on this element
	 */
	void doubeClick();
	

	Target getTarget();
	String getLabel();

	/** 
	 * Set the @ScreenRegion where element is found
	 * 
	 */
	void setScreenRegion(ScreenRegion found);
	
	/** 
	 * Get the @ScreenRegion where this element can be found
	 * 
	 */	
	ScreenRegion getScreenRegion();
}