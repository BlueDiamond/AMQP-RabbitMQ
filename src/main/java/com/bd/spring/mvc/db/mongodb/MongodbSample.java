package com.bd.spring.mvc.db.mongodb;

import java.net.UnknownHostException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongodbSample {

	private static Logger logger = LoggerFactory.getLogger(MongodbSample.class);

	private static void getCollection() {
		try {

			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("paldb");
			boolean auth = db.authenticate("pal", "admin123".toCharArray());
			logger.debug("authenticated: " + auth);

			Set<String> collections = db.getCollectionNames();

			for (String collectionName : collections) {
				logger.debug("collectionName: " + collectionName);
			}

			 DBCollection collection = db.getCollection("users");
			 logger.debug("collection: " + collection.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	private static void connectAndAuthenticate() {
		try {

			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("paldb");

			boolean auth = db.authenticate("pal", "admin123".toCharArray());

			DBCollection collection = db.getCollection("users");

			logger.debug("collection: " + collection);
			logger.debug("authenticated: " + auth);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}

	private static void saveAndGet() {
		try {
			// connect to mongoDB, ip and port number
			Mongo mongo = new Mongo("localhost", 27017);

			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = mongo.getDB("paldb");

			// Get collection from MongoDB, database named "paldb"
			// if collection doesn't exists, mongoDB will create it
			// automatically
			DBCollection collection = db.getCollection("users");

			// create a document to store key and value
			BasicDBObject document = new BasicDBObject();
			document.put("id", 1001);
			document.put("msg", "hello world mongoDB in Java");

			// save it into collection named "users"
			collection.insert(document);

			// search query
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("id", 1001);

			// query it
			DBCursor cursor = collection.find(searchQuery);

			// loop over the cursor and display the retrieved result
			while (cursor.hasNext()) {
				logger.debug("value from Mongodb:" + cursor.next());
			}

			logger.debug("Done");

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

		getCollection();

		// connectAndAuthenticate();

		// saveAndGet();

	}
}
