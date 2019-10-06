package com.javeriana.sobs.socializacion2backend.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DatabaseProperties {
	
	private final static Logger LOGGER = Logger.getLogger(DatabaseProperties.class);

	private Properties prop = new Properties();
	
	public DatabaseProperties() {
		try {
			String propFileName = "config.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
			inputStream.close();
		} catch (Exception e) {
			LOGGER.error("Exception: " + e);
		}
	}
	

	public String getPropValues(String value)  {
		return prop.getProperty(value);
	}
}
