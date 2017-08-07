package com.test.selenium.common;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.test.selenium.annotation.ConfigFileProvider;





public abstract class TestBase {
	/**
	 * The logger.
	 */
	protected static Logger logger;
	
	/**
	 * variable driver.
	 */
	protected WebDriver driver;
	
	/**
	 * variable baseUrl.
	 */
	protected static String baseUrl;
	
	/**
	 * variable webElementHelper.
	 */
	protected WebElementHelper webElementHelper;
	
	/**
	 * variable webAppManager.
	 */
	protected WebAppManager webAppManager;
	
	/**
	 * To manage the test data for test case.
	 */
	protected Properties properties;
	
	private static DesiredCapabilities capabilities;
	
	/**
	 * The path for screen shot files.
	 */
	private static String screenShotPath = "";
	
	/**
	 * This variable is to judge the test case is successfully or failed.
	 */
	protected boolean successfulFlag = false;
	
	/**
	 * The remote URL of hub.
	 */
	private static URL remoteURL;

	
	/**
	 * setUp function, use to initial variables from a configuration file.
	 * 
	 * @param configFile
	 *            The environment configuration file.
	 * @throws Exception
	 *             exception.
	 */
	
	@BeforeSuite(alwaysRun = true)
	@Parameters({ "configFile" })
	public void setUp(@Optional String configFile) throws Exception {
		logger = Logger.getLogger(TestBase.class);
		logger.info("Begin to run test suite.");
		try {
			if (configFile == null) {
				configFile = "src/com/test/selenium/configuration/config.properties";
			}
			Properties envProperties = BaseUtil.loadProperties(configFile);

			// setup log4j configurator file.
			String logConfigFile = envProperties
					.getProperty("logconfigfile",
							"src/com/test/selenium/log4j.properties");
			PropertyConfigurator.configure(GlobalConstants.parentPath + logConfigFile);
			capabilities = getCapabilities(envProperties);

			// startup the test grid.
			String hubHost = envProperties.getProperty("hubHost", "localhost");
			int hubPort = Integer.valueOf(envProperties.getProperty("hubPort",
					"4444"));
			remoteURL = new URL("http://" + hubHost + ":" + hubPort + "/wd/hub");

			// get base URL from property file.
			baseUrl = envProperties.getProperty("baseUrl");
			//baseUrlGrk = envProperties.getProperty("baseUrlGrk");
			// get timeout limit from property file.
			String timeoutStr = envProperties.getProperty("timeout", "10");
			GlobalConstants.TIMEOUT = Integer.valueOf(timeoutStr);

			// get checktimes from property file. This parater is for checking operation.
			String checktimes = envProperties.getProperty("checktimes", "20");
			GlobalConstants.CHECKTIMES = Integer.valueOf(checktimes);

			// get checktimes from property file. This parater is for dispatching page.
			String dispatchTimeOut = envProperties.getProperty("dispatchTimeOut", "20");
			GlobalConstants.dispatchTimeOut = Integer.valueOf(dispatchTimeOut);

			// get screen-shot path from property file.
			if (envProperties != null) {
				screenShotPath = envProperties.getProperty("screenShotPath");
			}

			// log environment parameters.
			BaseUtil.logProperties(envProperties, "Environment Parameters");
			logger = Logger.getLogger(this.getClass());
		} catch (Exception e) {
			logger.error("Initialize runtime parameters failed.");
			logger.error(e);
		}
	}
	
	/**
	 * 1. Create web driver, webElementHelper, webAppManager for a test case. 2.
	 * Load the test data for test case, this function will run before a test
	 * case launch.
	 */
	@BeforeTest(alwaysRun = true)
	public void setUpTest() {
		logger.info("Begin to run test case: "
				+ this.getClass().getSimpleName() + ".");
		try {
			driver = new RemoteWebDriver(remoteURL, capabilities);
			//driver = new RemoteWebDriver(remoteURL, capabilities);
			logger.info("Create a new webdriver for "
					+ capabilities.getBrowserName() + ".");
		} catch (Exception e) {
			logger.error("Can't create a new webdriver for "
					+ capabilities.getBrowserName() + ".");
			logger.error(e);
		}

		webElementHelper = new WebElementHelper(driver);
		webAppManager = new WebAppManager(driver);

		String configFile = "";
		if (this.getClass().getAnnotation(ConfigFileProvider.class) != null) {
			configFile = this.getClass()
					.getAnnotation(ConfigFileProvider.class).value();
		}
		if (!configFile.equals("")) {
			this.properties = BaseUtil.loadProperties(configFile);
		}
		// log test data for of the test case.
		BaseUtil.logProperties(this.properties, "Test Data Of "
				+ this.getClass().getSimpleName());
		boolean isAnyPropertyNull = BaseUtil.isAnyPropertyNull(properties,
				getTestPropertyKeys());
		logger.assertLog(!isAnyPropertyNull,
				"Can not load expected test data from property file. ");
		Assert.assertFalse(isAnyPropertyNull,
				"Can not load expected test data from property file. ");

		// open main page.
		webAppManager.openMainPage(baseUrl);
		logger.info("Open the base page: " + baseUrl + ".");
	}

	

	/**
	 * get the browser's capabilities from properties.
	 * 
	 * @param envProperties
	 *            the test environment configuration properties.
	 * @return the capabilities for browser.
	 */
	private DesiredCapabilities getCapabilities(Properties envProperties) {
		String browser = envProperties.getProperty("browser",
				BrowserType.FIREFOX);
		String profilePath = envProperties.getProperty("profile", null);

		if ("ie".equals(browser)) {
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities
					.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
			capabilities.setPlatform(Platform.ANY);

			// proxy setting for IE.
			if (!StringUtils.isEmpty(profilePath)) {
				Proxy proxy = new Proxy();
				proxy.setProxyAutoconfigUrl(profilePath);
				capabilities.setCapability(CapabilityType.PROXY, proxy);
			}
		} else {
			capabilities = DesiredCapabilities.chrome();

			// proxy setting for Firefox.
			if (!StringUtils.isEmpty(profilePath)) {
				File profileDir = new File(profilePath);
				FirefoxProfile profile = new FirefoxProfile(profileDir);
				capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			}
		}

		if (envProperties.getProperty("version") != null) {
			capabilities.setVersion(envProperties.getProperty("version"));
		}
		return capabilities;
	}
	
	/**
	 * take a screen shot if test case failure.
	 * 
	 * @param result
	 *            The after method result.
	 */
	@AfterMethod(alwaysRun = true)
	public void takeScreenshotOnFailure(ITestResult result) {
		if (!result.isSuccess()) {
			BaseUtil.takeScreenshot(screenShotPath, result, driver);
			logger.info("Take a screenshot for the failed test case.");
		}
	}
	
	/**
	 * Get property keys of the test data.
	 * 
	 * @return Property keys of the test data.
	 */
	protected abstract String[] getTestPropertyKeys();
	
	
	
	

}
