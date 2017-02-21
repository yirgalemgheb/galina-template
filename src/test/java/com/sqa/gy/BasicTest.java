package com.sqa.gy;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.safari.*;
import org.testng.annotations.*;

public class BasicTest {

	private String baseUrl;
	private WebDriver driver;
	private Logger logger;

	/**
	 * Constructs instance of BasicTest Initiates logger
	 * 
	 * @param baseUrl
	 */
	public BasicTest(String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.logger = Logger.getLogger(BasicTest.class); // you pass in the
															// class type. you
															// could put in a
															// string, but
															// unique to the
															// THIS logger.
		this.logger.info("Created a BasicTest object through constructor");
	}

	/**
	 * getter for baseUrl
	 * 
	 * @return baseUrl
	 */
	public String getBaseUrl() {
		return this.baseUrl;
	}

	/**
	 * getter for driver
	 * 
	 * @return driver
	 */
	public WebDriver getDriver() {
		return this.driver;
	}

	/**
	 * getter for logger
	 * 
	 * @return logger
	 */
	public Logger getLogger() {
		return this.logger;
	}

	/**
	 * setter for baseURL
	 * 
	 * @param baseUrl
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * setter for driver
	 * 
	 * @param driver
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * setter for logger
	 * 
	 * @param logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * sets up Chrome Driver before each test - when enabled
	 */
	@BeforeMethod // (enabled = false)
	public void setUpChrome() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		this.driver = new ChromeDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	/**
	 * sets up Firefox Driver before each test - when enabled
	 */
	@BeforeMethod(enabled = false)
	public void setUpFirefox() {
		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	/**
	 * sets up Safari Driver before each test - when enabled Does not work right
	 * now because of compatibility issues
	 */
	@BeforeMethod(enabled = false)
	public void setUpSafari() {
		this.driver = new SafariDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.driver.get(this.baseUrl);
	}

	/**
	 * tears down and quits driver after each test
	 */
	@AfterMethod
	public void tearDown() {
		this.driver.quit();

	}
}