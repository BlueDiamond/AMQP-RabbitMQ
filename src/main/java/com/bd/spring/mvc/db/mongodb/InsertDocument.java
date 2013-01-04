package com.bd.spring.mvc.db.mongodb;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class InsertDocument {

	private static Logger logger = LoggerFactory.getLogger(InsertDocument.class);

	public static void insertAndFetch() {

		try {

			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("paldb");

			// get a single collection
			DBCollection collection = db.getCollection("users");

			// BasicDBObject -example 1
			logger.debug("BasicDBObject example...");
			BasicDBObject document = new BasicDBObject();
			document.put("database", "paldb");
			document.put("table", "hosting");

			BasicDBObject documentDetail = new BasicDBObject();
			documentDetail.put("records", "99");
			documentDetail.put("index", "vps_index1");
			documentDetail.put("active", "true");
			document.put("detail", documentDetail);

			collection.insert(document);

			DBCursor cursorDoc = collection.find();
			while (cursorDoc.hasNext()) {
				logger.debug(cursorDoc.next().toString());
			}

			collection.remove(new BasicDBObject());

			// BasicDBObjectBuilder -example 2
			logger.debug("BasicDBObjectBuilder example...");
			BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start().add("database", "paldb2").add("table", "hosting");

			BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start().add("records", "99").add("index", "vps_index1").add("active", "true");

			documentBuilder.add("detail", documentBuilderDetail.get());

			collection.insert(documentBuilder.get());

			DBCursor cursorDocBuilder = collection.find();
			while (cursorDocBuilder.hasNext()) {
				logger.debug(cursorDocBuilder.next().toString());
			}

			collection.remove(new BasicDBObject());

			// Map -example 3
			logger.debug("Map example...");
			Map<String, Object> documentMap = new HashMap<String, Object>();
			documentMap.put("database", "paldb3");
			documentMap.put("table", "hosting");

			Map<String, Object> documentMapDetail = new HashMap<String, Object>();
			documentMapDetail.put("records", "99");
			documentMapDetail.put("index", "vps_index1");
			documentMapDetail.put("active", "true");

			documentMap.put("detail", documentMapDetail);

			collection.insert(new BasicDBObject(documentMap));

			DBCursor cursorDocMap = collection.find();
			while (cursorDocMap.hasNext()) {
				logger.debug(cursorDocMap.next().toString());
			}

			collection.remove(new BasicDBObject());

			// JSON parse example
			logger.debug("JSON parse example...");

			String json = "{'database' : 'paldb','table' : 'hosting'," + "'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";

			DBObject dbObject = (DBObject) JSON.parse(json);

			collection.insert(dbObject);

			DBCursor cursorDocJSON = collection.find();
			while (cursorDocJSON.hasNext()) {
				logger.debug(cursorDocJSON.next().toString());
			}

			collection.remove(new BasicDBObject());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		insertAndFetch();

	}
}
