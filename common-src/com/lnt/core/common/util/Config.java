package com.lnt.core.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lnt.core.common.exception.ServiceRuntimeException;

public class Config {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	private static final String fileName = "lnt.properties";

	private static Config soleInstance = new Config();

	private static boolean initialized = false;

	private Properties properties = new Properties();

	private Config() {
	}

	private void initialize() {
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getClassLoader()
					.getResourceAsStream(fileName);
			properties.load(inputStream);
			initialized = true;
		} catch (IOException e) {
			throw new ServiceRuntimeException(
					"Unable to read properties file : " + fileName);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new ServiceRuntimeException(
							"Unable to read properties file : " + fileName);
				}
			}
		}
	}

	public synchronized static Config getInstance() {
		if (!initialized)
			soleInstance.initialize();
		return soleInstance;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public int getIntProperty(String propertyName) {
		try {
			String value = properties.getProperty(propertyName);
			int intValue = Integer.parseInt(value);
			return intValue;
		} catch (NumberFormatException | NullPointerException e) {
			logger.error("Incorrect value in cfg file for : {} ", propertyName);
			return 0;
		}
	}

}
