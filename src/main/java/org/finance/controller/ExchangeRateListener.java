package org.finance.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.bson.Document;
import org.finance.config.KafkaProducerConfig;
import org.finance.config.SparkConfig;
import org.finance.rddOps.DataTransformation;
import org.finance.rddOps.DataTransformationImpl;

import kafka.serializer.StringDecoder;

public class ExchangeRateListener {
	
	final static Logger logger = Logger.getLogger(ExchangeRateListener.class);

	private final static String TOPIC = KafkaProducerConfig.getTopicName();

	
	/**
	 * Starts spark streaming process, this process recieves data from kafka topic,
	 * and process it via performing transformations and actions.
	 * Finally processed data is inserted into mongodb. 
	 */
	public void startListener(){

		JavaSparkContext sc = SparkConfig.getSparkConfiguration();
		logger.info("Freq at which Listener streams data: " + SparkConfig.getStreamingFreq() + " seconds");
		JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(SparkConfig.getStreamingFreq()));

		Map<String, String> kafkaParams = new HashMap<>();
		kafkaParams.put("metadata.broker.list", KafkaProducerConfig.getBrokerList());
		Set<String> topics = Collections.singleton(TOPIC);
		
		logger.info("Listener is streaming data from topic: "+ TOPIC);

		JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(ssc,
				String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

		DataTransformation dataTransformation = new DataTransformationImpl();

		JavaDStream<Document> currencyInDoc =  directKafkaStream.map(dataTransformation::parseStrToDoc);

		currencyInDoc.foreachRDD(dataTransformation::saveRddToDb);

		ssc.start();
		try {
			ssc.awaitTermination();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}

	}
}
