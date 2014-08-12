package org.sikuli.slides.driver.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Label {
  String value() default "";
}