package org.sikuli.slides.driver;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import org.sikuli.slides.driver.annotations.Label;

/**
 * Default decorator for use with PageFactory. Will decorate 1) all of the
 * WebElement fields and 2) List<WebElement> fields that have @FindBy or
 * @FindBys annotation with a proxy that locates the elements using the passed
 * in ElementLocatorFactory.
 */
public class DefaultFieldDecorator implements FieldDecorator {

  protected UIElementLocatorFactory factory;

  public DefaultFieldDecorator(UIElementLocatorFactory factory) {
    this.factory = factory;
  }

  public Object decorate(ClassLoader loader, Field field) {
    if (!(UIElement.class.isAssignableFrom(field.getType())
          || isDecoratableList(field))) {
      return null;
    }

    UIElementLocator locator = factory.createLocator(field);
    
    if (locator == null) {
      return null;
    }

    if (UIElement.class.isAssignableFrom(field.getType())) {
      return proxyForLocator(loader, locator);
    }else{ 
      return null;
    }
  }

  private boolean isDecoratableList(Field field) {
    if (!List.class.isAssignableFrom(field.getType())) {
      return false;
    }

    // Type erasure in Java isn't complete. Attempt to discover the generic
    // type of the list.
    Type genericType = field.getGenericType();
    if (!(genericType instanceof ParameterizedType)) {
      return false;
    }

    Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

    if (!UIElement.class.equals(listType)) {
      return false;
    }

    if (field.getAnnotation(Label.class) == null){
      return false;
    }

    return true;
  }

  protected UIElement proxyForLocator(ClassLoader loader, UIElementLocator locator) {
    InvocationHandler handler = new LocatingUIElementHandler(locator);
    UIElement proxy;
	proxy = (UIElement) Proxy.newProxyInstance(
			loader, new Class[] {UIElement.class}, handler);
    return proxy;
  }

 
}