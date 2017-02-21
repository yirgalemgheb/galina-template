package com.sqa.gy.helpers;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.testng.annotations.*;

public class BasicTest extends Core {

	/**
	 * Constructs instance of BasicTest Initiates logger
	 *
	 * @param baseUrl
	 */
	public BasicTest(String baseUrl) {
		super();
		setBaseUrl(baseUrl);
		setLogger(Logger.getLogger(BasicTest.class));
		getLogger().info("Created a BasicTest object through constructor");
	}

	/**
	 * sets up Chrome Driver before each test - when enabled
	 */
	@BeforeMethod(groups = "chrome")
	// (enabled = false)
	public void setUpChrome() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		getDriver().get(getBaseUrl());
	}

	/**
	 * sets up Firefox Driver before each test - when enabled
	 */
	@BeforeMethod(groups = "firefox")
	// (enabled = false)
	public void setUpFirefox() {
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		getDriver().get(getBaseUrl());
	}

	/**
	 * tears down and quits driver after each test
	 */
	@AfterMethod
	public void tearDown() {
		getDriver().quit();

	}
}