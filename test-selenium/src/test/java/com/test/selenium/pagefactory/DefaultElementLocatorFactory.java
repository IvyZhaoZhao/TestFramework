package com.test.selenium.pagefactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;



public final class DefaultElementLocatorFactory implements
ElementLocatorFactory {

/**
* The search context of a web driver.
*/
private final SearchContext searchContext;

/**
* The Remote web driver.
*/
private WebDriver driver;

/**
* The constructor of DefaultElementLocatorFactory class.
* 
* @param searchContext
*            The search context of a web driver.
* @param driver
*            The Remote web driver.
*/
public DefaultElementLocatorFactory(SearchContext searchContext,
	WebDriver driver) {
this.searchContext = searchContext;
this.driver = driver;
}

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
public ElementLocator createLocator(Field field, By by) {
return new DefaultElementLocator(this.searchContext, field,
		this.driver, by);
}

/**
* Initail the nested page object.
* 
* @param field
*            The nested field of the page object
* @return The nested page object
* @throws ClassNotFoundException
*             The exception for class not found
* @throws InstantiationException
*             The exception for instantiation
* @throws IllegalAccessException
*             The exception for illegal access
* @throws InvocationTargetException
*             The exception for invocation target
*/
public Object initNestedPageObject(Field field)
	throws ClassNotFoundException, InstantiationException,
	IllegalAccessException, InvocationTargetException {
Class<?> classType = field.getType();
Constructor<?>[] con = classType.getConstructors();
Object obj = con[0].newInstance(driver);
return obj;
}
}