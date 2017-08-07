package com.test.selenium.common;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;



public class WebAppManager {
	
	private WebDriver driver;
	
	public WebAppManager (WebDriver driver){
		this.driver=driver;
		
	}
	
	public void openMainPage(String baseUrl){
		driver.get(baseUrl);
	}
	
	public void maximizeWindow() {
		driver.manage().window().setPosition(new Point(0, 0));
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		Dimension dim = new Dimension((int) screenSize.getWidth(),
				(int) screenSize.getHeight());
		driver.manage().window().setSize(dim);
	}

}
