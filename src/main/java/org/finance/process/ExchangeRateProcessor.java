package org.finance.process;

import org.apache.log4j.Logger;
import org.finance.dao.ExchangeRatesDao;
import org.finance.dao.ExchangeRatesDaoImpl;

public class ExchangeRateProcessor {
	
	final static Logger logger = Logger.getLogger(ExchangeRateProcessor.class);
	
	/**
	 * @param startDate - starting date inclusive for search
	 * @param endDate   - end date inclusive for search
	 * @return custom output as string in json format 
	 */
	public String[] getHistoricalData(String startDate, String endDate){

		String[] result = new String[2];
		DataValidator dataValidator = new DataValidatorImpl(); 
		
		if(!dataValidator.validateDates(startDate, endDate)){
			String errorMessage = startDate + " cannot be after " + endDate;
			result[0] = "902";
			result[1]=errorMessage;
			
			logger.error("ErrorCode: " + result[0]);
			logger.error("Message: " + result[1]);
			
			return result;
		}

		ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDaoImpl();
		String responseMessage = exchangeRatesDao.getHistoricalRates(startDate, endDate);
		result[0] = "200";
		result[1] = responseMessage;
		
		return result;
	}
	
	/**
	 * @return latest rate as per external service
	 */
	public String getLatestRates(){
		
		ExchangeRatesDao exchangeRatesDao = new ExchangeRatesDaoImpl();
		String result = exchangeRatesDao.getLatestRate();
		
		return result;
	}

}
