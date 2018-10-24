package org.finance.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyReader {
	
	final static Logger logger = Logger.getLogger(PropertyReader.class);
	
	private static Properties props = new Properties();

	public static void loadProperties(){

		Map<String,String> env = System.getenv();

		FileReader reader;
		try {
			reader = new FileReader(env.get("STORAGE_PROPERTIES").toString());
			props.load(reader);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public static String getProperty(String property){
		return props.getProperty(property).trim();
	}
	
	public static String setProperty(String key,String value){
		
		Map<String,String> env = System.getenv();
		
		OutputStream output = null;
		
		try {
			output = new FileOutputStream(env.get("STORAGE_PROPERTIES").toString());
			
			props.setProperty(key, value);
			props.store(output, null);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return props.getProperty(key).trim();
	}
	
}
