package org.finance.rddOps;

import org.apache.spark.api.java.JavaRDD;
import org.bson.Document;

import scala.Tuple2;

public interface DataTransformation {

	/**
	 * This method is responsible for spark transformations, it transforms data recieved from kafka
	 * into bson document which can finally be inserted into mongodb as javardd
	 * 
	 * @param tuple  - Tuple2<String,String> - Message recieved from kafka
	 * @return
	 */
	public Document parseStrToDoc(Tuple2<String,String> tuple);
	
	
	/**
	 * This method is responsible for inserting transformed rdd into database
	 * 
	 * @param docRdd - JavaRDD<Document>
	 */
	public void saveRddToDb(JavaRDD<Document> docRdd);
}
