package org.finance.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.finance.controller.ExchangeRateListener;
import org.finance.controller.ExchangeRateProducer;


@Path("/controllers")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class ControllerServices {
	
	final static Logger logger = Logger.getLogger(ControllerServices.class);
	
	@GET
	@Path("/startProducer")
	public void invokeProducer(){
		
		ExchangeRateProducer exchangeRateProducer = new ExchangeRateProducer();
		
		logger.info("Kafka producer started......");
		logger.info("In order to stop producer change property \"kafka.producer.status\" to stop");
		exchangeRateProducer.run();
	}
	

	@GET
	@Path("/startStreaming")
	public void invokeListener(){
		
		ExchangeRateListener exchangeRateListener = new ExchangeRateListener();
		
		logger.info("Listener started......");
		exchangeRateListener.startListener();
	}

}
