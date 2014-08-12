package org.sikuli.slides.driver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import org.sikuli.slides.api.io.PPTXSlidesReader;
import org.sikuli.slides.api.io.SlidesReader;
import org.sikuli.slides.api.models.Slide;
import org.sikuli.slides.driver.annotations.Source;

public class SlideUIFactory {

	/**
	 * Create an object where each field represents an {@link UIElement} that can be found and interacted with
	 * automatically
	 * 
	 * @param pageClassToProxy
	 * @return
	 */
	public static <T> T create(Class<T> pageClassToProxy) {

		if (!pageClassToProxy.isAnnotationPresent(Source.class))
			// path to pptx file is not specified
			throw new RuntimeException("@Source annotation is missing in " + pageClassToProxy + ". Need this to know where" +
					"to read slides");

		Source source = pageClassToProxy.getAnnotation(Source.class);
		source.value();
		
//		if (source.url().isEmpty() > 0){
		SlidesReader reader = new PPTXSlidesReader();

		List<Slide> slides;
//		File file = new File(source.value());
		try {
			
			if (!source.value().isEmpty()){
				slides = reader.read(new File(source.value()));	
			}else if (!source.url().isEmpty()){
				slides = reader.read(new URL(source.url()));
			}else{
				throw new RuntimeException("@Source is not specified correctly");
			}
			
		} catch (IOException e) {
			// error in reading the slides from the given source 
			throw new RuntimeException(e);
		}
		
		SlideUIDriver driver;
		if (slides.size() == 0){
			// there is no slide found
			driver = new SlideUIDriver();
		}else{

			driver = new SlideUIDriver(slides.get(0));
		}

		T page = instantiatePage(driver, pageClassToProxy);		
		initElements(driver, page);
		return page;
	}

	static void initElements(UIDriver driverRef, Object page) {
		initElements(new DefaultUIElementLocatorFactory(driverRef), page);
	}
	//
	static void initElements(UIElementLocatorFactory factoryRef, Object page) {
		initElements(new DefaultFieldDecorator(factoryRef), page);
	}

	static void initElements(FieldDecorator decorator, Object page) {
		Class<?> proxyIn = page.getClass();
		while (proxyIn != Object.class) {
			proxyFields(decorator, page, proxyIn);
			proxyIn = proxyIn.getSuperclass();
		}
	}

	private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
		Field[] fields = proxyIn.getDeclaredFields();
		for (Field field : fields) {
			Object value = decorator.decorate(page.getClass().getClassLoader(), field);
			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(page, value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}


	private static <T> T instantiatePage(UIDriver driver, Class<T> pageClassToProxy) {
		try {
			try {
				// try to see if there's a constructor that takes a Driver object as an argument
				Constructor<T> constructor = pageClassToProxy.getConstructor(UIDriver.class);
				return constructor.newInstance(driver);
			} catch (NoSuchMethodException e) {
				// if not, use the default constructor
				return pageClassToProxy.newInstance();
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
