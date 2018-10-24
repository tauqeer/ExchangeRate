package org.finance.process;

public interface DataValidator {

	/**
	 * @param startDate
	 * @param endDate
	 * @return true if endDate is after startDate, false otherwise
	 */
	public boolean validateDates(String startDate, String endDate);
	
}
