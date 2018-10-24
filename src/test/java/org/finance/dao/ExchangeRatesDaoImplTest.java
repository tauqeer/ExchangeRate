package org.finance.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

public class ExchangeRatesDaoImplTest {
	
	ExchangeRatesDaoImpl exchangeRatesDaoImpl = new ExchangeRatesDaoImpl();
	List<Document> positiveCaseList = new ArrayList<Document>();
	
	@Before
	public void init() {

		Calendar cal = Calendar.getInstance();
		
		//Data for 2-Sept-2017
		cal.set(2017, 8, 2, 9, 0, 0);
		Document doc1 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.1));
		Document doc2 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.2));
		
		//Data for 3-Sept-2017
		cal.set(Calendar.DAY_OF_MONTH, 3);
		Document doc3 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.3));
		cal.set(Calendar.HOUR, 5);
		Document doc4 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.4));
		cal.set(Calendar.HOUR, 6);
		Document doc5 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.5));

		//Data for 5-Sept-2017
		cal.set(Calendar.DAY_OF_MONTH, 5);
		Document doc6 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.6));

		positiveCaseList = Arrays.asList(doc1,doc2,doc3,doc4,doc5,doc6);
	
	}

	
	@Test
	public void testDecorateOutput(){

		Document testDoc = exchangeRatesDaoImpl.decorateOutput(positiveCaseList);
		
		String Sept2Key = "2017-9-2";
		assertTrue( testDoc.get(Sept2Key, ArrayList.class).size() == 2 );
		
		String Sept3Key = "2017-9-3";
		assertTrue( testDoc.get(Sept3Key, ArrayList.class).size() == 3 );
		
		String Sept5Key = "2017-9-5";
		assertTrue( testDoc.get(Sept5Key, ArrayList.class).size() == 1 );
		
	}
	
	@Test
	public void testDecorateOutputSingle(){
		Calendar cal = Calendar.getInstance();

		//Data for 2-Sept-2017
		cal.set(2017, 8, 2, 9, 0, 0);
		Document doc1 = new Document("_id",new ObjectId()).append("base", "EUR").append("date", cal.getTime()).append("rates", new Document("USD",1.1));
		Document testDoc = exchangeRatesDaoImpl.decorateOutput(Arrays.asList(doc1));
		
		String Sept2Key = "2017-9-2";
		assertTrue( testDoc.get(Sept2Key, ArrayList.class).size() == 1 );
	}
	
}
