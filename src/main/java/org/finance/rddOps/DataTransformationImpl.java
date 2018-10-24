package org.finance.rddOps;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.bson.Document;
import org.finance.controller.ExchangeRateProducer;
import org.finance.util.Constants;
import org.finance.util.ParserUtil;

import com.mongodb.spark.MongoSpark;

import scala.Tuple2;

public class DataTransformationImpl implements DataTransformation,Serializable{
	
	final static Logger logger = Logger.getLogger(ExchangeRateProducer.class);
	private static final long serialVersionUID = 5553782848835885099L;

	/* (non-Javadoc)
	 * @see org.finance.rddOps.DataTransformation#parseStrToDoc(scala.Tuple2)
	 */
	public Document parseStrToDoc(Tuple2<String, String> tuple) {
		Document inputDoc = Document.parse(tuple._2);
		String dateInStr = inputDoc.getString(Constants.DATE);
		Date startDate = ParserUtil.parseDate(dateInStr);
		inputDoc.put(Constants.DATE, startDate);
		
		return inputDoc;
	}

	/* (non-Javadoc)
	 * @see org.finance.rddOps.DataTransformation#saveRddToDb(org.apache.spark.api.java.JavaRDD)
	 */
	public void saveRddToDb(JavaRDD<Document> docRdd) {
		MongoSpark.save(docRdd);		
	}

}
