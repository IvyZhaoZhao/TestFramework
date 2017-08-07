package com.test.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

/**
 * 
 * @author zhaozhao
 * annotation is used to inject a mapping configuration file
 */

public @interface ConfigFileProvider {
	
	/**
	 * @return The value of the annotation
	 */
	String value();

}
