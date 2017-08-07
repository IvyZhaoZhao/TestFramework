package com.test.selenium.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;




public class BaseUtil {
	
	/**
	 * The logger.
	 */
	private static Logger logger = Logger.getRootLogger();

	/**
	 * variable screentShotPath.
	 */
	private static String screenShotPath = GlobalConstants.parentPath
			+ "/test-output/screenshot";

	/**
	 * take a screen shot to default folder.
	 * 
	 * @param path
	 *            The screen shot path.
	 * @param result
	 *            The test result.
	 * @param driver
	 *            The web driver.
	 */
	public static void takeScreenshot(String path, ITestResult result,
			WebDriver driver) {
		if (driver == null) {
			return;
		}
		// RemoteWebDriver does not implement the TakesScreenshot class
		// if the driver does have the Capabilities to take a screenshot
		// then Augmenter will add the TakesScreenshot methods to the instance
		driver = new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		if (StringUtils.isNotEmpty(path))
			screenShotPath = path;
		File file = new File(screenShotPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		try {
			FileUtils.copyFile(screenshot, new File(screenShotPath
					+ File.separator + result.getName() + "_" + time + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Reporter.log("Test Name: " + result.getTestName() + " screenshot: <img src=\"" + screenshot.getAbsolutePath()
				+ "\" />");
	}

	/**
	 * load uimap properties from configuration file of the page object.
	 * 
	 * @param configFile
	 *            The UIMap configuration file
	 * @return properties The UIMap properties
	 */
	public static Properties loadProperties(String configFile) {
		if (StringUtils.isEmpty(configFile)) {
			return null;
		}

		Properties properties = new Properties();
		try {
			File file = new File(GlobalConstants.parentPath
					+ configFile);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			properties.load(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Remove all the blanks of a string.
	 * 
	 * @param before
	 *            The string before replacement
	 * @return The string after replacement
	 */
	public static String removeBlanks(String before) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(before);
		String after = m.replaceAll("");
		return after;
	}

	/**
	 * Transfer GBK string to UTF-8 string.
	 * 
	 * @param source
	 *            The GBK string.
	 * @return The UTF-8 string.
	 */
	//modified
	public static String gbkToUtf8(String source) {
		byte[] buf;
		try {
			buf = source.trim().getBytes("UTF-8");
			source = new String(buf, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return source;
	}

	/**
	 * Judge two string are the same or not when the enconde is GBK or UTF-8.
	 * 
	 * @param source Source string.
	 * @param target Target string.
	 * @return When the two string are the same, return true.
	 */
	public static boolean equalsStr(String source, String target) {
		if (source.equals(target) || source.equals(gbkToUtf8(target))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if any property of the given Properties is null.
	 * 
	 * @param properties
	 *            Properties to check.
	 * @param propertyKeys
	 *            Property keys.
	 * @return True if any property is null
	 */
	public static boolean isAnyPropertyNull(Properties properties,
			String[] propertyKeys) {
		if (properties == null) {
			if (propertyKeys == null) {
				return false;
			}
			return true;
		} else {
			if (propertyKeys != null) {
				for (String key : propertyKeys) {
					if (StringUtils.isEmpty(properties.getProperty(key))) {
						return true;
					}
				}
				return false;
			}
			return true;
		}

	}

	/**
	 * Print parameters in property file.
	 * 
	 * @param properties
	 *            The properties.
	 * @param info
	 *            The information of the parameters.
	 */
	@SuppressWarnings("unchecked")
	public static void logProperties(Properties properties, String info) {
		int expressionLen = 80;
		StringBuffer expression = new StringBuffer();
		int firstPart = (expressionLen-info.length())/2;
		for(int i=0; i<firstPart;i++){
			expression.append('-');
		}
		expression.append(info);
		for(int i=0;i<(expressionLen-firstPart-info.length());i++){
			expression.append('-');
		}
		logger.info(expression.toString());
		Iterator it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			logger.info(key + ":" + value);
		}
		logger.info("--------------------------------------------------------------------------------");

	}

}
