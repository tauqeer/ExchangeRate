package org.finance.config;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.finance.util.Constants;
import org.finance.util.DBUtil;
import org.finance.util.PropertyReader;

public class SparkConfig {
	
	final static Logger logger = Logger.getLogger(SparkConfig.class);
	
	static{
		PropertyReader.loadProperties();
	}
	
	public static JavaSparkContext getSparkConfiguration() {

		String hostName = DBUtil.getHostName();
		String dbName =  DBUtil.getDbName();
		int portNumber = DBUtil.getPortNumber();
		String collection = DBUtil.getCurrencyCollection();
		
		//mongodb://localhost:27017/finance.currency
		// mongodb://host:port/db.collection
		String mongoInputUri = "mongodb://" + hostName + Constants.COLON + portNumber +  Constants.FORWARD_SLASH + dbName + Constants.DOT +  collection;
		String mongoOutputUri = "mongodb://" + hostName + Constants.COLON + portNumber +  Constants.FORWARD_SLASH + dbName + Constants.DOT +  collection;
		
		logger.debug("Datasource url: " + mongoInputUri);
		
		SparkConf sparkConf = new SparkConf().setMaster(SparkConfig.getSparkMasterUrl())
											.set("spark.mongodb.input.uri", mongoInputUri)
											.set("spark.mongodb.output.uri", mongoOutputUri)
											.setAppName("ExchangeRateStreaming");

		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		return sc;
	}
	
	/**
	 * @return - Freq at which spark streaming application will run.
	 */
	public static int getStreamingFreq(){
		String freqStr = PropertyReader.getProperty("spark.streaming.freq");
		int freq = Integer.parseInt(freqStr);
		
		return freq;
	}
	
	public static String getSparkMasterUrl(){
		String masterUrl = PropertyReader.getProperty("spark.master.url");
		
		return masterUrl;
	}

}
