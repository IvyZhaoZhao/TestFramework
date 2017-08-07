package com.test.selenium.pagefactory;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.test.selenium.common.WebElementHelper;



public class DefaultElementLocator implements ElementLocator {
	
	/**
	 * The logger.
	 */
	private static Logger logger = Logger
			.getLogger(DefaultElementLocator.class);

	/**
	 * The search context of a web driver.
	 */
	@SuppressWarnings("unused")
	private final SearchContext searchContext;

	/**
	 * Describe the element should be cached or not. This value is getted from
	 * annotation.
	 */
	private final boolean shouldCache;

	/**
	 * The identify of a web element.
	 */
	private final By by;

	/**
	 * This variable is used to cache a web element.
	 */
	private WebElement cachedElement;

	/**
	 * This variable is used to cache a list of web elements.
	 */
	private List<WebElement> cachedElementList;

	/**
	 * The web element helper to wait for web elements.
	 */
	private WebElementHelper webElementHelper;

	/**
	 * The field name for the field in page object.
	 */
	private String fieldName = "";

	/**
	 * Creates a new element locator.
	 * 
	 * @param searchContext
	 *            The context to use when finding the element
	 * @param field
	 *            The field on the Page Object that will hold the located value
	 * @param driver
	 *            The web driver
	 * @param by
	 *            The identify of a web element
	 * 
	 */
	public DefaultElementLocator(SearchContext searchContext, Field field,
			WebDriver driver, By by) {
		this.fieldName = field.getName();
		this.searchContext = searchContext;
		Annotations annotations = new Annotations(field);
		this.shouldCache = annotations.isLookupCached();
		this.webElementHelper = new WebElementHelper(driver);
		this.by = by;
	}
	
	
	/**
	 * Find the element.
	 * 
	 * @return a web element
	 */

	@Override
	public WebElement findElement() {
		if ((this.cachedElement != null) && (this.shouldCache)) {
			return this.cachedElement;
		}
		if (this.by == null) {
			return null;
		}
		try {
			WebElement element = webElementHelper.waitElementVisible(this.by);
			if (this.shouldCache) {
				this.cachedElement = element;
			}
			return element;
		} catch (Exception e) {
			logger.error("The " + this.fieldName
					+ " can't find the related webElement.");
			logger.error(e);
			return null;
		}
	}
	

	/**
	 * Find the element list.
	 * 
	 * @return a list of web elements
	 */
	@Override
	public List<WebElement> findElements() {
		if ((this.cachedElementList != null) && (this.shouldCache)) {
			return this.cachedElementList;
		}
		
		try {
			List<WebElement> elements = webElementHelper
			.waitElementsVisible(this.by);
			if (this.shouldCache) {
				this.cachedElementList = elements;
			}
			return elements;
		} catch (Exception e) {
			logger.error("The " + this.fieldName
					+ " can't find a list of related webElements.");
			e.printStackTrace();
			return null;
		}
	}
	
	

}
