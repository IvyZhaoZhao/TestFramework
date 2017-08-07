package com.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author zhaozhao
 * annotation for UI mapping key in a mapping config file 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIMapKeyProvider {
	
	/**
	 * @return The value of the annotation
	 */
	String value();

}
