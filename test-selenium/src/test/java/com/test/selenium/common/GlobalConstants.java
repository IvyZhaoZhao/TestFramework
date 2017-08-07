package com.test.selenium.common;

public class GlobalConstants {
	

	/**
	 * global variable timeout, this value can be changed when you configure
	 * timeout in file
	 * com\tsystems\demail\at\fw\configuration\config.properties.
	 */
	public static int TIMEOUT = 10;

	/**
	 * When load configuration file, there is a parent path for the
	 * configuration file path.
	 */
	public static String parentPath = System.getProperty("user.dir") + "/";

	/**
	 * when do the check operation, the application will do less than this
	 * parameter. com\tsystems\demail\at\fw\configuration\config.properties.
	 */
	public static int CHECKTIMES = 20;

	/**
	 * global variable dispatchTimeOut, this value can be changed when you
	 * configure timeout in file. It is the limit of dispatch page.
	 * com\tsystems\demail\at\fw\configuration\config.properties.
	 */
	public static int dispatchTimeOut = 20;

}
