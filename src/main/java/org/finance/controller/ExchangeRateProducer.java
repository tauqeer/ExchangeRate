package org.finance.controller;

import java.util.Calendar;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.finance.config.KafkaProducerConfig;
import org.finance.util.Constants;
import org.finance.util.PropertyReader;


public class ExchangeRateProducer {

	final static Logger logger = Logger.getLogger(ExchangeRateProducer.class);
	
	private final static String TOPIC = KafkaProducerConfig.getTopicName();
	private final static String BOOTSTRAP_SERVERS = KafkaProducerConfig.getBrokerList();
	
	//In seconds
	private final static int producerFreq = 1000 * KafkaProducerConfig.getProducerFrequency();

	public static void main(String[] args) {
		
		logger.info("Starting producer application....");
		ExchangeRateProducer exchangeRateProducer = new ExchangeRateProducer();
		exchangeRateProducer.run();
	}
	
	public void run(){

		try {
			
			PropertyReader.loadProperties();
			String status = KafkaProducerConfig.getProducerStatus();
			
			logger.info("Status of producer is at: " + status);
			
			
			if(status.toLowerCase().equals(Constants.START)){
				
				logger.info("Starting execution of kafka producer.....");
				status = KafkaProducerConfig.setProducerStatus(Constants.RUNNING);
				logger.info("Frequency at which Listener will run is: " + KafkaProducerConfig.getProducerFrequency() + " seconds");
			} else {
				logger.info("Mark status for producer to start for running kafka producer....");
			}

			while(status.equals(Constants.RUNNING)){

				Producer<Long, String> runningProducer = ExchangeRateProducer.startProducer();
				Thread.sleep(producerFreq);

				logger.debug("Status of kafka producer is: " + status);

				PropertyReader.loadProperties();
				 status = KafkaProducerConfig.getProducerStatus();
				
				if(status.toLowerCase().equals("stop")){
					logger.info("Stopping execution of kafka producer......");
					ExchangeRateProducer.stopProducer(runningProducer);
					System.exit(1);
				}
			}

		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	
	}

	private static Producer<Long, String> createProducer(){
		
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,LongSerializer.class.getName());

		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}
	

	/**
	 * 
	 * This method is responsible for starting kafka producer, producer listen to external service
	 * and pushes date into kafka as per topic configured.
	 * 
	 * @throws InterruptedException
	 */
	static Producer<Long, String> startProducer() throws InterruptedException {
		final Producer<Long, String> producer = createProducer();
		
		Client client = ClientBuilder.newClient();
		String latestRate = client.target(KafkaProducerConfig.getProducerUrl())
				.queryParam("symbols", KafkaProducerConfig.getTargetCurrency())
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);

		final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, latestRate + Calendar.getInstance().getTime());
		
		logger.info("Message pushed in kafka system: " + record);
		producer.send(record);

		return producer;
	}
	
	static void stopProducer(Producer <Long, String> producer){
		producer.flush();
		producer.close();
	}
}
