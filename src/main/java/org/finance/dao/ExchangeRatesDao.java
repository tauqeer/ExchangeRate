package org.finance.dao;

public interface ExchangeRatesDao {

	
	/**
	 * @return - Latest exchange rate in string format
	 */
	public String getLatestRate();
	
	/**
	 * @param startDateInput - starting date inclusive for search
	 * @param endDateInput - end date inclusive for search
	 * @return custom output as string in json format 
	 */
	public String getHistoricalRates(String startDateInput, String endDateInput);
	
	public void getHighestRate(String startDateInput, String endDateInput);
	
	public void getLowestRate(String startDateInput, String endDateInput);
}
