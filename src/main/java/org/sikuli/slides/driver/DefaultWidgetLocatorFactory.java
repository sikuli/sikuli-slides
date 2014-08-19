package org.sikuli.slides.driver;

import java.lang.reflect.Field;

public class DefaultWidgetLocatorFactory implements WidgetLocatorFactory {

	final private SlideDriver driver;
	public DefaultWidgetLocatorFactory(SlideDriver driverRef) {
		driver = driverRef;
	}

	@Override
	public WidgetLocator createLocator(Field field) {
		return new DefaultWidgetLocator(driver, field);
	}

}