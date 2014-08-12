package org.sikuli.slides.driver;

import java.lang.reflect.Field;

public class DefaultUIElementLocatorFactory implements UIElementLocatorFactory {

	final private UIDriver driver;
	public DefaultUIElementLocatorFactory(UIDriver driverRef) {
		driver = driverRef;
	}

	@Override
	public UIElementLocator createLocator(Field field) {
		return new DefaultUIElementLocator(driver, field);
	}

}