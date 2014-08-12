package org.sikuli.slides.driver;
import java.lang.reflect.Field;

import org.sikuli.slides.driver.annotations.Label;

public class DefaultUIElementLocator implements UIElementLocator {

	final private UIDriver driver;
	final private String label;
	
	public DefaultUIElementLocator(UIDriver driverRef, Field field) {	    
	    if (field.isAnnotationPresent(Label.class)){
	    	// if a label annotation is explicity defined, use it
	    	label = field.getAnnotation(Label.class).value();
	    }else{
	    	// take the field name as the label
	    	label = field.getName();
	    }
	    driver = driverRef;
	}

	@Override
	public UIElement findElement() {
		return driver.findElement(label);
	}

}