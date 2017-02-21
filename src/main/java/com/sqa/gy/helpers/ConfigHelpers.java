package com.sqa.gy.helpers;

import java.io.*;
import java.util.*;

public class ConfigHelpers {

	public static final String DEFAULT_CONFIG_FILE_NAME = "config.properties";

	/**
	 * Overloads addProperty method to work with default config file name
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addProperty(String key, String value) {
		return addProperty(DEFAULT_CONFIG_FILE_NAME, key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean addProperty(String fileName, String key, String value) {
		Properties prop = readProps(fileName);
		prop.setProperty(key, value);
		return writeProps(prop, fileName);
	}

	public static Properties readProps(String fileName) {
		// Create the Properties object
		Properties prop = new Properties();
		try {
			// Create an InputStream and set it to a new FileInputStream based
			// on properties file we are reading
			InputStream input = new FileInputStream("src/main/resources/" + fileName);
			// Load the properties object with data from the properties file
			prop.load(input);
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " was not found in src/main/resources.");
			return null;
		} catch (IOException e) {
			System.err.println("File " + fileName + " encounters errors while reading.");
			return null;
		}
		return prop;
	}

	/**
	 * Overloads retrieveIntValueFromConfig method to work with default config
	 * file name
	 *
	 * @param key
	 * @return value as int
	 */
	public static int retrieveIntValueFromConfig(String key) {
		return retrieveIntValueFromConfig(DEFAULT_CONFIG_FILE_NAME, key);
	}

	/**
	 * @param key
	 * @return value as int
	 */
	public static int retrieveIntValueFromConfig(String fileName, String key) {
		String input = retrieveProp(fileName, key);
		int result = 0;
		try {
			result = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.err.println("Issue converting to int value: " + input);
		}
		return result;
	}

	/**
	 * Overloads retrieveProp method to work with default config file name
	 *
	 * @param key
	 * @return property value as String
	 */
	public static String retrieveProp(String key) {
		return retrieveProp(DEFAULT_CONFIG_FILE_NAME, key);
	}

	/**
	 * @param key
	 * @return property value as String
	 */
	public static String retrieveProp(String fileName, String key) {
		Properties prop = readProps(fileName);
		String keyValue = prop.getProperty(key);
		return keyValue;
	}

	/**
	 * Writes new config file in default folder
	 * 
	 * @param prop
	 * @param fileName
	 * @return
	 */
	public static boolean writeProps(Properties prop, String fileName) {
		try {
			FileOutputStream output = new FileOutputStream("src/main/resources/" + fileName);
			prop.store(output, "Config Properties");
		} catch (FileNotFoundException e) {
			System.err.println("File " + fileName + " can not be created in in src/main/resources.");
			return false;
		} catch (IOException e) {
			System.err.println("File " + fileName + " encounters errors while writing.");
			return false;
		}
		return true;
	}
}
