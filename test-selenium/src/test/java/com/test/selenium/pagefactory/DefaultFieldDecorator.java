package com.test.selenium.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import com.test.selenium.annotation.UIMapKeyProvider;
import com.test.selenium.common.BasePage;


public class DefaultFieldDecorator implements FieldDecorator {

	/**
	 * The element locator factory for a page object.
	 */
	private ElementLocatorFactory factory;

	/**
	 * The constructor of this class.
	 * 
	 * @param factory
	 *            The element locator factory
	 */
	public DefaultFieldDecorator(ElementLocatorFactory factory) {
		this.factory = factory;
	}

	/**
	 * This method is called by PageFactory on all fields to decide how to
	 * decorate the field.
	 * 
	 * @param loader
	 *            The class loader that was used for the page object
	 * @param field
	 *            The field that may be decorated.
	 * @param by
	 *            The by identify of a web element.
	 * @return Value to decorate the field with or null if it shouldn't be
	 *         decorated. If non-null, must be assignable to the field.
	 */
	public Object decorate(ClassLoader loader, Field field, By by) {
		if ((!WebElement.class.isAssignableFrom(field.getType()))
				&& (!isDecoratableList(field))) {
			if ((field.getType().getSuperclass() != null)
					&& field.getType().getSuperclass().equals(BasePage.class)) {
				return decorateNestedObject(field);
			} else {
				return null;
			}
		}

		ElementLocator locator = this.factory.createLocator(field, by);
		if (locator == null) {
			return null;
		}

		if (WebElement.class.isAssignableFrom(field.getType())) {
			return proxyForLocator(loader, locator);
		}
		if (List.class.isAssignableFrom(field.getType())) {
			return proxyForListLocator(loader, locator);
		}
		return null;
	}

	/**
	 * When the field in the page object is a nested page object, it will
	 * initial the field as a page object.
	 * 
	 * @param field
	 *            The field of page object
	 * @return The nested object.
	 */
	private Object decorateNestedObject(Field field) {
		DefaultElementLocatorFactory newFactory = (DefaultElementLocatorFactory) this.factory;
		try {
			Object obj = newFactory.initNestedPageObject(field);			
			return obj;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * To judge the field is a list of web element or not.
	 * 
	 * @param field
	 *            a field of page object
	 * @return true or false
	 */
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

		Type listType = ((ParameterizedType) genericType)
				.getActualTypeArguments()[0];

		if (!WebElement.class.equals(listType)) {
			return false;
		}

		return (field.getAnnotation(UIMapKeyProvider.class) != null);
	}

	/**
	 * Create a proxy instance for a web element.
	 * 
	 * @param loader
	 *            The class loader
	 * @param locator
	 *            The locator of a web element
	 * @return web element
	 */
	protected WebElement proxyForLocator(ClassLoader loader,
			ElementLocator locator) {
		InvocationHandler handler = new LocatingElementHandler(locator);

		WebElement proxy = (WebElement) Proxy.newProxyInstance(loader,
				new Class[] {WebElement.class, WrapsElement.class,
						Locatable.class }, handler);
		return proxy;
	}

	/**
	 * Create a list of proxy instances for web elements.
	 * 
	 * @param loader
	 *            The class loader
	 * @param locator
	 *            The locator of a web element
	 * @return list of web elements
	 */
	@SuppressWarnings("unchecked")
	protected List<WebElement> proxyForListLocator(ClassLoader loader,
			ElementLocator locator) {
		InvocationHandler handler = new LocatingElementListHandler(locator);

		List proxy = (List) Proxy.newProxyInstance(loader,
				new Class[] {List.class }, handler);
		return proxy;
	}

}