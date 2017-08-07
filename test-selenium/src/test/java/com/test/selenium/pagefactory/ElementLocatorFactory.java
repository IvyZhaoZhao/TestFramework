package com.test.selenium.pagefactory;

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ElementLocator;


public interface ElementLocatorFactory {
	
	/**
	 * When a field on a class needs to be decorated with an
	 * {@link ElementLocator} this method will be called.
	 * 
	 * @param field
	 *            The field of a page object
	 * @param by
	 *            The By of the field
	 * @return ElementLocator
	 */
	ElementLocator createLocator(Field field, By by);

}
