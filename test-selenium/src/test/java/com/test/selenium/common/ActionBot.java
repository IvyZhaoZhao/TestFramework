package com.test.selenium.common;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.Select;




/**
 * The ActionBot component is an action-oriented abstraction over the raw selenium APIs.
 * 
 * @author zhaozhao
 *
 */

public class ActionBot {
	
	/**
	 * logger ;
	 */
	private static Logger logger = Logger.getLogger(ActionBot.class);
	
	
	/**
	 * web driver
	 */
	private WebDriver driver;
	
	
	/**
	 * actionBot constructor, in order to 
	 * @param driver
	 */
	public ActionBot(WebDriver driver){
		driver=this.driver;
	}
	
	/**
	 * Clear the current text and input new text 
	 * @param webElement
	 * @param text
	 */
	public void clearAndType(WebElement webElement, String text){
		webElement.clear();
		webElement.sendKeys(text);
		
	}
	
	/**
	 * click on web webElement
	 * @param webElement
	 */
	public void click(WebElement webElement){
		webElement.click();
	}
	
	/**
	 * click on submit web webElement
	 * @param webElement
	 */
	public void submit(WebElement webElement){
		webElement.submit();
	}
	
	/**
	 * get webElement's text 
	 * if text is null, return null
	 * else return the text of the web webElement
	 * @param webElement
	 * @return
	 */
	public String getText(WebElement webElement){
		if(webElement.getText()==null){
			return null;
	
		}
		return webElement.getText();
	}
	
	
	/**
	 * get webElement's tag name
	 * if tag name is null ,return null
	 * else return tag name of the webElement
	 * @param webElement
	 * @return
	 */
	public String getTagName(WebElement webElement){
		if(webElement.getTagName()==null){
			return null;
	
		}
		return webElement.getTagName();
	}
	
	/**
	 * send keys for input items
	 * @param webElement
	 * @param str
	 */
	public void sendKeys(WebElement webElement, String str) {
		webElement.sendKeys(str);
	}
	
	
	/**
	 * Get attribute with attribute name.
	 * @param attr
	 * @param webElement
	 * @return
	 */
	public String getAttribute(String attr, WebElement webElement) {
		if (webElement == null) {
			return null;
		} else {
			return webElement.getAttribute(attr);
		}
	}
	
	/**
	 * Click Cascade Element.
	 * @param upperElement
	 * @param cascadeElement
	 */
	public void clickCascadeElement(WebElement upperElement,
			WebElement cascadeElement) {
		new Actions(driver).moveToElement(upperElement).perform();
		new Actions(driver).moveToElement(cascadeElement).click().perform();
	}
	
	/**
	 * Get select with attribute name.
	 * @param webElement
	 * @param optionValue
	 */
	public void select(WebElement webElement, String optionValue) {
		new Select(webElement).selectByValue(optionValue);
	}
	
	
	/**
	 * Get select with attribute name.
	 * @param webElement
	 * @param index
	 */
	public void select(WebElement webElement, int index) {
		new Select(webElement).selectByIndex(index);
	}
	
	/**
	 * Get value of a selector
	 * @param webElement
	 * @param index
	 * @return
	 */
	public String getValueOfSelector(WebElement webElement, int index) {
		return new Select(webElement).getOptions().get(index).getText();
	}
	
	/**
	 * Perform double click action with a web element.
	 * @param webElement
	 */
	public void doubleClick(WebElement webElement) {
		new Actions(driver).doubleClick(webElement).perform();
	}
	
	/**
	 * Show the context menu with a web element.
	 * @param webElement
	 */
	public void contextMenu(WebElement webElement) {
		new Actions(driver).contextClick(webElement).perform();
	}
	
	/**
	 * Move to a specific area and click it.
	 * @param webElement
	 * @param coordString
	 */
	public void clickAt(WebElement webElement, String coordString) {
		int index = coordString.trim().indexOf(',');
		int xOffset = Integer.parseInt(coordString.trim().substring(0, index));
		int yOffset = Integer.parseInt(coordString.trim().substring(index + 1));
		new Actions(driver).moveToElement(webElement, xOffset, yOffset).click()
				.perform();
	}
	
	
   /**
    * move to specific area and double click it.
    * @param webElement
    * @param coordString
    */
	public void doubleClickAt(WebElement webElement, String coordString) {
		int index = coordString.trim().indexOf(',');
		int xOffset = Integer.parseInt(coordString.trim().substring(0, index));
		int yOffset = Integer.parseInt(coordString.trim().substring(index + 1));
		new Actions(driver).moveToElement(webElement, xOffset, yOffset)
				.doubleClick(webElement).perform();
	}
	
	/**
	 * Move to a specific area and show the context menu.
	 * @param webElement
	 * @param coordString
	 */
	public void contextMenuAt(WebElement webElement, String coordString) {
		int index = coordString.trim().indexOf(',');
		int xOffset = Integer.parseInt(coordString.trim().substring(0, index));
		int yOffset = Integer.parseInt(coordString.trim().substring(index + 1));
		new Actions(driver).moveToElement(webElement, xOffset, yOffset)
				.contextClick(webElement).perform();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Wait for some milliseconds.
	 * @param timeout
	 */
	public void sleep(int timeout) {
		try {
			Thread.sleep(timeout * 1000);
		} catch (InterruptedException e) {
			logger.error("Sleep operation is failed.");
			e.printStackTrace();
		}
	}


}
