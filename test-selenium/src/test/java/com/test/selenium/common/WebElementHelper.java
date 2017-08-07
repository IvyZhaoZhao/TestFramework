package com.test.selenium.common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WebElementHelper {
	
	/**
	 * variable driver.
	 */
	private WebDriver driver;

	/**
	 * constructor of WebElementHelper.
	 * 
	 * @param driver
	 *            the web driver.
	 */
	public WebElementHelper(WebDriver driver) {
		super();
		this.driver = driver;
	}

	/**
	 * try to click twice.
	 * 
	 * @param locator
	 *            locator
	 */
	public void tryToClickTwice(By locator) {
		try {
			waitElementClickable(locator).click();
		} catch (StaleElementReferenceException e) {
			waitElementClickable(locator).click();
		}
	}

	/**
	 * wait element until it's visible.
	 * 
	 * @param locator
	 *            locator
	 * @return WebElement
	 */
	public WebElement waitElementVisible(By locator) {
		return waitFor(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * wait element util it's visible.
	 * 
	 * @param webElement
	 *            webElement
	 * @param timeout
	 *            timeout limit
	 * @return boolean
	 */
	public boolean waitElementVisible(WebElement webElement, int timeout) {
		try {
			waitFor(ExpectedConditions.visibilityOf(webElement), timeout);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * wait for webelement until expected condition is true.
	 * 
	 * @param expectedCondition
	 *            expected condition.
	 * @param timeout
	 *            timeout limit.
	 * @return webElement
	 */
	private WebElement waitFor(ExpectedCondition<WebElement> expectedCondition,
			int timeout) {
		return new WebDriverWait(driver, timeout).until(expectedCondition);

	}

	/**
	 * wait element until it's invisible.
	 * 
	 * @param locator
	 *            locator
	 * @return boolean
	 */
	public boolean waitElementInvisible(By locator) {
		return waitFor(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * wait element until it is clickable.
	 * 
	 * @param locator
	 *            locator
	 * @return WebElement
	 */
	public WebElement waitElementClickable(By locator) {
		return waitFor(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * wait for common function.
	 * 
	 * @param <Type>
	 *            type
	 * @param expectedCondition
	 *            expected condition
	 * @return Type
	 */
	private <Type> Type waitFor(ExpectedCondition<Type> expectedCondition) {
		return new WebDriverWait(driver, GlobalConstants.TIMEOUT)
				.until(expectedCondition);
	}

	/**
	 * get element if it is visible.
	 * 
	 * @param locator
	 *            locator
	 * @return WebElement
	 */
	public WebElement elementIfVisible(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			return element != null && element.isDisplayed() ? element : null;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * check if the given element is present.
	 * 
	 * @param by
	 *            The locator of the element.
	 * @return True if the element is present.
	 */
	public boolean isElementPresent(By by) {
		try {
			waitElementVisible(by);
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * wait elements until it's visible.
	 * 
	 * @param locator
	 *            locator
	 * @return List<WebElement>
	 */
	public List<WebElement> waitElementsVisible(By locator) {
		if (isElementPresent(locator)) {
			return this.driver.findElements(locator);
		} else {
			return null;
		}
	}

}
