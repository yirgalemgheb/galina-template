package com.sqa.gy.helpers;

import java.io.*;

import org.apache.commons.io.*;
import org.openqa.selenium.*;

/**
 * Helper methods for automation testing tasks such as taking screenshots of
 * verifying if element is present
 */
public class AutoBasics {

	/**
	 * Verifies that needed element is present and, if not, catches
	 * NoSuchElement exception
	 *
	 * @param driver
	 * @param by
	 *            element to be located
	 * @return boolean - if element was found
	 */
	public static boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return false;
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

}
