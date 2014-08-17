package org.sikuli.slides.driver;

import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;

public interface UIElement {
	
	/** 
	 * Execute a click operation on this element
	 */
	public void click();
	
	
	public void type(String text);
	
	/** 
	 * Execute a click operation on this element
	 */
	public void doubeClick();
	

	public Target getTarget();
	public String getLabel();

	/** 
	 * Set the @ScreenRegion where element is found
	 * 
	 */
	public void setScreenRegion(ScreenRegion found);
	
	/** 
	 * Get the @ScreenRegion where this element can be found
	 * 
	 */	
	public ScreenRegion getScreenRegion();
}