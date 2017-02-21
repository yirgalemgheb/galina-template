package com.sqa.gy.helpers;

import java.io.*;

import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * Helper methods for automation testing tasks such as taking screenshots of
 * verifying if element is present
 */
public class AutoBasics {

	/**
	 * Clear text field and sends keys string into the field
	 *
	 * @param value
	 *            String to be entered into field
	 * @param element
	 *            for the text field
	 */
	public static void clearAndSendKeysToField(String value, WebElement element) {
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Selects item from drop down list
	 * 
	 * @param value
	 *            to be selected from the list
	 * @param element
	 *            list on page
	 */
	public static void selectFromDropDown(String value, WebElement element) {
		if (!value.equalsIgnoreCase("null")) {
			Select select = new Select(element);
			select.selectByVisibleText(value);
		}
	}

	/**
	 * Takes a screenshot of the current web page Catches IO exception and
	 * prints stack trace
	 *
	 * @param driver
	 * @param fileName
	 *            to save it as
	 * @return boolean - if screenshot was successful
	 */
	public static boolean takesScreenshot(WebDriver driver, String fileName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File("screenshots/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Verifies that needed element is present and, if not, catches
	 * NoSuchElement exception
	 *
	 * @param driver
	 * @param by
	 *            element to be located
	 * @return boolean - if element was found
	 */
	public boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return false;
	}

}
