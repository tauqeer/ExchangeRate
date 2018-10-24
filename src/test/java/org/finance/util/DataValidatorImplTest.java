package org.finance.util;

import static org.junit.Assert.assertTrue;

import org.finance.process.DataValidatorImpl;
import org.junit.Before;
import org.junit.Test;

public class DataValidatorImplTest {
	
	DataValidatorImpl dataValidatorImpl =new DataValidatorImpl();
	
	String startDateStr;
	String endDateStr;
	
	@Before
	public void init(){
		 startDateStr = "2017-09-05";
		 endDateStr = "2017-09-07";
	}
	
	@Test
	public void  testValidateDatesPos(){
		assertTrue(dataValidatorImpl.validateDates(startDateStr, endDateStr));
	}
	
	@Test
	public void testValidateDatesNeg(){
		assertTrue(!dataValidatorImpl.validateDates(endDateStr, startDateStr));
	}
	
	@Test
	public void testValidateDates(){
		assertTrue(dataValidatorImpl.validateDates(startDateStr, startDateStr));
	}

}
