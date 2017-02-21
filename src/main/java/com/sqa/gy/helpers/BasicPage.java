package com.sqa.gy.helpers;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

/**
 * Parent class for all page objects. Creates instance of page and accesses
 * AutoBasics to take screenshots
 */
public class BasicPage {

	@SuppressWarnings("javadoc")
	public Logger logger;
	private WebDriver driver;

	/**
	 * Creates instance of page
	 *
	 * @param driver
	 */
	public BasicPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.logger = Logger.getLogger(BasicPage.class); // you pass in the
															// class type. you
															// could put in a
															// string, but
															// unique to the
															// THIS logger.
	}

	/**
	 * Clear text field and sends keys string into the field
	 *
	 * @param value
	 *            String to be entered into field
	 * @param element
	 *            for the text field
	 */
	protected void clearAndSendKeysToField(String value, WebElement element) {
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Calls AutoBasics method to Verify that needed element is present
	 *
	 * @param by
	 *            element to be located
	 * @return boolean - if element was found
	 */
	protected boolean isElementPresent(By by) {
		return AutoBasics.isElementPresent(this.driver, by);
	}

	/**
	 * Overloads method - if user does not supply filename for screenshot Calls
	 * AutoBasics method to take screenshot
	 *
	 * @return boolean if ss was successful
	 */
	protected boolean takeScreenshot() {
		return AutoBasics.takesScreenshot(this.driver, "screenshot");
	}

	/**
	 * Calls AutoBasics method to take screenshot
	 *
	 * @param filename
	 *            to be given to screenshot file
	 * @return boolean if ss was successful
	 */
	protected boolean takeScreenshot(String filename) {
		return AutoBasics.takesScreenshot(this.driver, filename);
	}
}
