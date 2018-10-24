package org.finance.util;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class DBUtil {
	
	static{
		PropertyReader.loadProperties();
	}
	
	final static String hostName =  DBUtil.getHostName();
	final static String portNumber = PropertyReader.getProperty("datasource.port");
	
	private static MongoClient mongoClient = null;

	static{
		mongoClient = new MongoClient(new ServerAddress(hostName, Integer.parseInt(portNumber)));
	}
	
	public static MongoClient getMongoConnection(){	
		if(mongoClient == null )
			 mongoClient = new MongoClient(new ServerAddress(hostName, Integer.parseInt(portNumber)));
		
		return mongoClient;
	}
	
	public static String getHostName(){
		 return PropertyReader.getProperty("datasource.hostname");
	}
	
	public static String getDbName(){
		 return PropertyReader.getProperty("datasource.dbname");
	}
	
	public static int getPortNumber(){

		String portNumberStr = PropertyReader.getProperty("datasource.port");
		int portNumber = Integer.parseInt(portNumberStr);

		return portNumber;
	}
	
	public static String getCurrencyCollection(){
		 return PropertyReader.getProperty("datasource.collection");
	}
}
