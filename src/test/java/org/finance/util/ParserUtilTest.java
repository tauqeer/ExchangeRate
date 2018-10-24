package org.finance.util;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.finance.exception.InvalidInputException;
import org.junit.Before;
import org.junit.Test;

public class ParserUtilTest {
	
	ParserUtil parserUtil = new ParserUtil();
	String errorDate;
	String correctDate;
	
	
	@Before
	public void init(){
		 errorDate = "2017-0x9-05";
		 correctDate = "2017-09-05";
	}
	
	@Test
	public void  testValidateDate(){
		
		Date parsedDate = ParserUtil.parseDate(correctDate);
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 8, 5, 0, 0, 0);
		cal.set(Calendar.MILLISECOND,0);
		
		assertTrue(parsedDate.equals(cal.getTime()));
	}

	@Test
	public void  testValidateDatesMessage(){
		
		String exceptioMsg = "Unparseable date: \"2017-0x9-05\" Please provide valid date in yyyy-MM-dd format";
		try{
		ParserUtil.parseDate(errorDate);
		
		} catch(InvalidInputException invalidInputException){
			
			String msg = invalidInputException.getMessage();
			System.out.println("Msg: " + msg);
			System.out.println("Exception Msg: " + exceptioMsg);
			
			assertTrue(invalidInputException.getMessage().equals(exceptioMsg));
		}
	}
	
	@Test
	public void  testValidateDatesExceptionCode(){
		
		try{
		ParserUtil.parseDate(errorDate);
		
		} catch(InvalidInputException invalidInputException){
			
			int status = invalidInputException.getStatusCode();
			assertTrue(status == 901);
		}
	}
}
