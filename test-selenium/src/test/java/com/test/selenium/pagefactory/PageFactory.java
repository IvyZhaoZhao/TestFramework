package com.test.selenium.pagefactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.test.selenium.annotation.UIMapKeyProvider;
import com.test.selenium.common.BasePage;
import com.test.selenium.common.BaseUtil;



public class PageFactory {

	/**
	 * This variable saves configuration file.
	 */
	private String configFile;


	/**
	 * Initial a page object.
	 * 
	 * @param driver
	 *            The Remote web driver.
	 * @param cof
	 *            The configuration file path of the page object. This is used
	 *            to load UI locators from configuration file.
	 * @param obj
	 *            A page object
	 * @return The specific page object with right attributes
	 */
	public Object initPageObject(WebDriver driver, String cof, Object obj) {
		this.configFile = cof;
		initElements(driver, obj);
		return obj;
	}

	/**
	 * Instantiate an instance of the given class, and set a lazy proxy for each
	 * of the WebElement and List<WebElement> fields that have been declared,
	 * assuming that the field name is also the HTML element's "id" or "name".
	 * This means that for the class:
	 * 
	 * <code>
	 * public class Page {
	 *     private WebElement submit;
	 * }
	   * </code>
	 * 
	 * 
	 * This method will attempt to instantiate the class given to it, preferably
	 * using a constructor which takes a WebDriver instance as its only argument
	 * or falling back on a no-arg constructor. An exception will be thrown if
	 * the class cannot be instantiated.
	 * 
	 * @param driver
	 *            The driver that will be used to look up the elements
	 * @param pageClassToProxy
	 *            A class which will be initialised.
	 * @return An instantiated instance of the class with WebElement and
	 *         List<WebElement> fields proxied
	 * @throws InstantiationException
	 *             exception for instantiation
	 * @throws IllegalAccessException
	 *             exception for illegaAccess
	 */
	@SuppressWarnings("unchecked")
	public Class<BasePage> initElements(WebDriver driver,
			Class<BasePage> pageClassToProxy) throws InstantiationException,
			IllegalAccessException {
		Object page = instantiatePage(driver, pageClassToProxy);
		initElements(driver, page);
		return (Class<BasePage>) page;
	}

	/**
	 * As
	 * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver, Class)}
	 * but will only replace the fields of an already instantiated Page Object.
	 * 
	 * @param driver
	 *            The driver that will be used to look up the elements
	 * @param page
	 *            The object with WebElement and List<WebElement> fields that
	 *            should be proxied.
	 */
	public void initElements(WebDriver driver, Object page) {
		WebDriver driverRef = driver;
		//initElements(new DefaultElementLocatorFactory(driverRef, driverRef),
		//		page);
		
	}

	/**
	 * Similar to the other "initElements" methods, but takes an
	 * {@link ElementLocatorFactory} which is used for providing the mechanism
	 * for finding elements. If the ElementLocatorFactory returns null then the
	 * field won't be decorated.
	 * 
	 * @param factory
	 *            The factory to use
	 * @param page
	 *            The object to decorate the fields of
	 */
	public void initElements(ElementLocatorFactory factory, Object page) {
		ElementLocatorFactory factoryRef = factory;
		initElements(new DefaultFieldDecorator(factoryRef), page);
	}

	/**
	 * Similar to the other "initElements" methods, but takes an
	 * {@link FieldDecorator} which is used for decorating each of the fields.
	 * 
	 * @param decorator
	 *            the decorator to use
	 * @param page
	 *            The object to decorate the fields of
	 */
	@SuppressWarnings("unchecked")
	public void initElements(FieldDecorator decorator, Object page) {
		Class proxyIn = page.getClass();
		while (proxyIn != BasePage.class) {
			proxyFields(decorator, page, proxyIn);
			proxyIn = proxyIn.getSuperclass();
		}
	}

	/**
	 * Proxy fields of page object.
	 * 
	 * @param decorator
	 *            the decorator to use
	 * @param page
	 *            The object to decorate the fields of
	 * @param proxyIn
	 *            The proxy of page object Class
	 */
	private void proxyFields(FieldDecorator decorator, Object page,
			Class<?> proxyIn) {
		Field[] fields = proxyIn.getDeclaredFields();
		Properties properties = BaseUtil.loadProperties(configFile);
		for (Field field : fields) {
			By by = null;
			if (field.getAnnotation(UIMapKeyProvider.class) != null) {
				by = getLocator(field.getAnnotation(UIMapKeyProvider.class).value(),
						properties);
			}
			Object value = decorator.decorate(page.getClass().getClassLoader(),
					field, by);
			if (value == null) {
				continue;
			}
			try {
				field.setAccessible(true);
				field.set(page, value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Instantiage the page object.
	 * 
	 * @param driver
	 *            The remote web driver for the instance
	 * @param pageClassToProxy
	 *            The proxy for page object class
	 * @return the page object
	 * @throws InstantiationException
	 *             exception for instantiation
	 * @throws IllegalAccessException
	 *             exception for illegaAccess
	 */
	@SuppressWarnings("unchecked")
	private static Object instantiatePage(WebDriver driver,
			Class<BasePage> pageClassToProxy) throws InstantiationException,
			IllegalAccessException {
		try {
			Constructor constructor = pageClassToProxy
					.getConstructor(new Class[] { WebDriver.class });
			return constructor.newInstance(new Object[] { driver });
		} catch (NoSuchMethodException localNoSuchMethodException) {
			return pageClassToProxy.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get by identify.
	 * 
	 * @param key
	 *            The key of selector
	 * @param properties
	 *            The properties from configuration file
	 * @return by
	 */
	public static By getLocator(String key, Properties properties) {
		if (properties == null) {
			return null;
		}
		String locator = properties.getProperty(key);
		if (locator == null) {
			return null;
		}
		if (locator.startsWith("xpath:")) {
			return By.xpath(locator.substring("xpath:".length()));
		} else if (locator.startsWith("css:")) {
			return By.cssSelector(locator.substring("css:".length()));
		} else if (locator.startsWith("id")) {
			return By.id(locator.substring("id:".length()));
		} else if (locator.startsWith("tagName")) {
			return By.tagName(locator.substring("tagName:".length()));
		} else if (locator.startsWith("className")) {
			return By.className(locator.substring("className:".length()));
		} else if (locator.startsWith("name")) {
			return By.name(locator.substring("name:".length()));
		} else {
			throw new Error("Unrecognized locator " + locator);
		}
	}

}
