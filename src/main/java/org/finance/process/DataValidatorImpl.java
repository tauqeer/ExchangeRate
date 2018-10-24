package org.finance.process;

import java.util.Date;

import org.apache.log4j.Logger;
import org.finance.util.ParserUtil;

public class DataValidatorImpl implements DataValidator{
	
	final static Logger logger = Logger.getLogger(DataValidatorImpl.class);
	
	/* (non-Javadoc)
	 * @see org.finance.process.DataValidator#validateDates(java.lang.String, java.lang.String)
	 */
	public boolean validateDates(String startDateStr, String endDateStr){
		
		boolean isDateValid = false;
		
		logger.debug("Parsing startDate: " + startDateStr);
		Date startDate = ParserUtil.parseDate(startDateStr);
		
		logger.debug("Parsing endDate: " + endDateStr);
		Date endDate = ParserUtil.parseDate(endDateStr);
				
		if(endDate.after(startDate) || endDate.equals(startDate))
			isDateValid = true;
		
		return isDateValid;
	}
	

}
