package org.finance.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.finance.exception.InvalidInputException;
import org.finance.process.ExchangeRateProcessor;

@Path("/insights")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class ExchangeRateInsightsService {
	
	final static Logger logger = Logger.getLogger(ExchangeRateInsightsService.class);
	
	@GET
	@Path("/getHistoryData")
	public Response getAggregatedDate(@QueryParam("startDate") String startDate,@QueryParam("endDate") String endDate){
		
		ExchangeRateProcessor exchangeRateProcessor = new ExchangeRateProcessor();
		
		logger.info("Generating report from: " + startDate + " to " + endDate);
		
		String[] responseMsg = exchangeRateProcessor.getHistoricalData(startDate, endDate);
		
		int statusCode = Integer.parseInt(responseMsg[0]);
		
		if(statusCode != 200){
			logger.error("ErrorCode: " + statusCode);
			logger.error("Message: " + responseMsg[1]);
			
			throw new InvalidInputException(statusCode, responseMsg[1]);
			
		}
		
		Response response =Response.status(statusCode)
				.entity(responseMsg[1])
				.build();
		
		return response;
	}
	
	@GET
	@Path("/getLatestRate")
	public Response getLatestRate(){
		
		ExchangeRateProcessor exchangeRateProcessor = new ExchangeRateProcessor();
		String responseMsg = exchangeRateProcessor.getLatestRates();
		
		Response response =Response.status(200)
				.entity(responseMsg)
				.build();
		
		return response;
	}

}
