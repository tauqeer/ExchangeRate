package org.finance.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.finance.config.KafkaProducerConfig;
import org.finance.util.Constants;
import org.finance.util.DBUtil;
import org.finance.util.ParserUtil;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;


public class ExchangeRatesDaoImpl implements ExchangeRatesDao {
	
	final static Logger logger = Logger.getLogger(ExchangeRatesDaoImpl.class);
	
	private MongoClient mongoClient;
	private MongoCollection<Document> collection;
	
	public ExchangeRatesDaoImpl(){
		mongoClient = DBUtil.getMongoConnection();
		logger.debug("Database: " + DBUtil.getDbName());
		logger.debug("Collection: " + DBUtil.getCurrencyCollection());
		collection = mongoClient.getDatabase(DBUtil.getDbName()).getCollection(DBUtil.getCurrencyCollection());
	}

	/* (non-Javadoc)
	 * @see org.finance.dao.ExchangeRatesDao#getLatestRate()
	 */
	public String getLatestRate() {
		ExchangeRatesDaoImpl exchangeRatesDaoImpl = new ExchangeRatesDaoImpl();
		Client client = ClientBuilder.newClient();
		
		String latestRate = client.target(KafkaProducerConfig.getProducerUrl())
				.queryParam("symbols", KafkaProducerConfig.getTargetCurrency())
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);
		
		logger.debug(latestRate);
		
		logger.info("Getting latest rate for euro to usd from: " +  KafkaProducerConfig.getProducerUrl() + "?symbols=USD");
		
		Document latestRateDoc = Document.parse(latestRate);
		String dateInStr = latestRateDoc.getString(Constants.DATE);
		Date date = ParserUtil.parseDate(dateInStr);
		latestRateDoc.put(Constants.DATE, date);
		
		Document docPerDay = exchangeRatesDaoImpl.decorateOutput(Arrays.asList(latestRateDoc));
		Document outputDoc = new Document("FROM","EUR").append("TO", "USD");
		
		outputDoc.append("rates", docPerDay);
		
		logger.debug("Output Doc: " + outputDoc.toJson());
		String response = ParserUtil.toPrettyFormat(outputDoc.toJson());

		logger.info(response);
		return response;
	}

	/* (non-Javadoc)
	 * @see org.finance.dao.ExchangeRatesDao#getHistoricalRates(java.lang.String, java.lang.String)
	 */
	public String getHistoricalRates(String startDateInput, String endDateInput) {

		ExchangeRatesDaoImpl exchangeRatesDaoImpl = new ExchangeRatesDaoImpl();
		List<Document> docList = new ArrayList<Document>();
		
		Date startDate = ParserUtil.parseDate(startDateInput);
		Date endDate = ParserUtil.parseDate(endDateInput);

		Document queryDoc = new Document("date", new Document("$gte", startDate).append("$lte", endDate));
		Document outputDoc = new Document("FROM","EUR").append("TO", "USD");
		
		logger.debug("Query: " + queryDoc.toJson());

		collection.find(queryDoc).into(docList);
		
		Document docPerDay = exchangeRatesDaoImpl.decorateOutput(docList);
		logger.debug("docPerDay document: " + docPerDay.toJson());
		outputDoc.append("rates", docPerDay);

		String response = ParserUtil.toPrettyFormat(outputDoc.toJson());

		logger.info(response);

		return response;
	}
	
	/**
	 * This method arranges data in a particular format for reporting purpose
	 * @param docList 
	 * @return Document containing exchange rates on per day basis
	 */
	public Document decorateOutput(List<Document> docList){
		
		Document docPerDay = new Document();
		
		docList.forEach(x->{
			
			logger.debug("Document under inspection: " + x.toJson());
			
			Date date = x.getDate("date");
			
			Document rateDoc = (Document) x.get("rates");
			double rate = rateDoc.getDouble("USD");

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) +1;
			int day = cal.get(Calendar.DAY_OF_MONTH);

			String dateAsKey = year + "-" + month + "-" + day;
			
			logger.debug("Key_in_map: " + dateAsKey);

			Document rateDocForDay = new Document("time", date.toString())
										.append("conversionRate", rate);
			
			
			if(docPerDay.containsKey(dateAsKey)){
				List<Document> list =  docPerDay.get(dateAsKey,ArrayList.class);
				list.add(rateDocForDay);

			} else{
				List<Document> list = new ArrayList<Document>();
				list.add(rateDocForDay);
				docPerDay.append(dateAsKey, list);
			}
		
		});
			
	return docPerDay;
	}

	@Override
	public void getHighestRate(String startDateInput, String endDateInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLowestRate(String startDateInput, String endDateInput) {
		// TODO Auto-generated method stub
		
	}
	
}
