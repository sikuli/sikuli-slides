package org.sikuli.slides.driver;

import java.lang.reflect.Field;

interface UIElementLocatorFactory {
	UIElementLocator createLocator(Field field);
}