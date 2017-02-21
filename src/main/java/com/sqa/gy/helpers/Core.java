package com.sqa.gy.helpers;

import org.apache.log4j.*;
import org.openqa.selenium.*;

public class Core {

	private String baseUrl;

	private WebDriver driver;

	private Logger logger;

	public boolean addProp(String key, String value) {
		return ConfigHelpers.addProperty(key, value);
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public int getInt(String name) {
		return ConfigHelpers.retrieveIntValueFromConfig(name);
	}

	public Logger getLog() {
		return this.logger;
	}

	public Logger getLogger() {
		return this.logger;
	}

	public String getProp(String name) {
		return ConfigHelpers.retrieveProp(name);
	}

	protected void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}
}
