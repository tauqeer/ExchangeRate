package org.finance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.finance.exception.InvalidInputException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParserUtil {
	
	final static Logger logger = Logger.getLogger(ParserUtil.class);
	
	/**
	 * @param dateInStr -> date in string format to parse into Date, supported format type is yyyy-MM-dd
	 * @return a Date type
	 */
	public static Date parseDate(String dateInStr){

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = null;
		try {
			date = fmt.parse(dateInStr);
			logger.debug("Parsed Date: " + date);
			
		} catch (ParseException e) {
			
			String errorMessage = e.getMessage() + " Please provide valid date in yyyy-MM-dd format";
			logger.error(errorMessage);
			throw new InvalidInputException(901, errorMessage);
			
		}

		return date; 
	}
	
	/**
	   * Convert a JSON string to pretty print version
	   * @param jsonString
	   * @return
	   */
	  public static String toPrettyFormat(String jsonString) {
	      JsonParser parser = new JsonParser();
	      JsonObject json = parser.parse(jsonString).getAsJsonObject();

	      Gson gson = new GsonBuilder().setPrettyPrinting().create();
	      String prettyJson = gson.toJson(json);

	      return prettyJson;
	  }
	
}
