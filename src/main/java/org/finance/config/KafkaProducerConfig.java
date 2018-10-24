package org.finance.config;

import org.finance.util.PropertyReader;

public class KafkaProducerConfig {

	static{
		PropertyReader.loadProperties();
	}
	
	public static String getProducerStatus(){
		return PropertyReader.getProperty("kafka.producer.status");
	}
	
	public static String setProducerStatus( String value){
		return PropertyReader.setProperty("kafka.producer.status", value);
	}
	
	public static String getProducerUrl(){
		return PropertyReader.getProperty("kafka.topic.rest.url");
	}
	
	public static String getTargetCurrency(){
		return PropertyReader.getProperty("exchange.target.currency");
	}
	
	public static String getBrokerList(){
		return PropertyReader.getProperty("kafka.broker.list");
	}
	
	public static String getTopicName(){
		return PropertyReader.getProperty("kafka.topic.name");
	}
	
	public static int getProducerFrequency(){
		String freqStr = PropertyReader.getProperty("kafka.producer.freq");
		int freq = Integer.parseInt(freqStr);
		
		return freq;
	}
}
