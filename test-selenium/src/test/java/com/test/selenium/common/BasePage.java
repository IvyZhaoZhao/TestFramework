package com.test.selenium.common;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.test.selenium.annotation.ConfigFileProvider;
import com.test.selenium.pagefactory.PageFactory;





public class BasePage {
	
	
	protected static Logger logger;
	
	protected ActionBot actionBot;
	
	protected static int dispatchTimeOut = GlobalConstants.dispatchTimeOut;
	
	protected WebDriver driver;
	
	public BasePage(WebDriver driver) {
		logger = Logger.getLogger(this.getClass());
		try {
			this.actionBot = new ActionBot(driver);
			String configFile = "";
			if (this.getClass().getAnnotation(ConfigFileProvider.class) != null) {
				configFile = this.getClass().getAnnotation(
						ConfigFileProvider.class).value();
			}
			this.driver = driver;
			PageFactory pageFactory = new PageFactory();
			pageFactory.initPageObject(driver, configFile, this);
			logger.info("Initialize PageObject: "
					+ this.getClass().getSimpleName() + " successfully.");
		} catch (Exception e) {
			logger.error("Initialize PageObject: "
					+ this.getClass().getSimpleName() + " failed.");
			logger.error(e);
		}
	}

}
