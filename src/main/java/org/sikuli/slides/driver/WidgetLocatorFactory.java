package org.sikuli.slides.driver;

import java.lang.reflect.Field;

interface WidgetLocatorFactory {
	WidgetLocator createLocator(Field field);
}