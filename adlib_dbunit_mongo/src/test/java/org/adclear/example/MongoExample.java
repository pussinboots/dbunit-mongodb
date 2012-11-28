package org.adclear.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.adclear.dbunit.json.DbUnitRuleMongo;
import org.adclear.dbunit.json.annotations.JsonData;
import org.adclear.dbunit.json.operation.strategy.MongoDbCleanStrategy;
import org.adclear.dbunit.json.operation.strategy.MongoDbRefreshLoadStrategy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * The junit test case for the {@link MediaCodeReportBean} class.
 * 
 * @author fit
 * 
 */
public class MongoExample {
	
	private static final String MONGODB_URI = "mongodb://localhost:27017/junit_example.dbunit";

	private Mongo mongo;
	
	private DB db;
	
	private DBCollection dbCollection;

	@Rule
	public DbUnitRuleMongo dbUnit = new DbUnitRuleMongo(MongoExample.class,
			MONGODB_URI);

	/**
	 * Init test mongodb instance.
	 * 
	 * @throws Exception
	 *             shouldn't thrown
	 */
	@Before
	public void setUp() throws Exception {
		mongo = new Mongo(dbUnit.getMongoUri());
		db = mongo.getDB(dbUnit.getMongoUri().getDatabase());
		dbCollection = db.getCollection(dbUnit.getMongoUri().getCollection());
	}

	/**
	 * 
	 */
	@Test
	@JsonData
	public void testRead() {
		assertEquals(3, dbCollection.count());
		
		DBCursor find = dbCollection.find();
		Map<Integer, DBObject> map = getMap(find);
		
		assertEquals(1, map.get(1).get("_id"));
		
		assertEquals(2, map.get(2).get("_id"));
		assertEquals("dbunit", map.get(2).get("column1"));
		
		assertEquals(3, map.get(3).get("_id"));
		assertNull(map.get(3).get("column1"));
	}

	@Test
	@JsonData(loadStrategy = MongoDbRefreshLoadStrategy.class)
	public void testDelete() {
		assertEquals(3, dbCollection.count());
		
		dbCollection.remove(new BasicDBObject("_id", 1));
		
		DBCursor find = dbCollection.find();
		Map<Integer, DBObject> map = getMap(find);
		
		assertEquals(2, map.size());
		assertNull(map.get(1));
		assertEquals(2, map.get(2).get("_id"));
		assertEquals("dbunit", map.get(2).get("column1"));
		
		assertEquals(3, map.get(3).get("_id"));
		assertNull(map.get(3).get("column1"));
		
		assertEquals(2, dbCollection.count());
	}
	
	@Test
	@JsonData(loadStrategy=MongoDbRefreshLoadStrategy.class, loadData = false)
	public void testDelete2() {
		DBCollection collection = db.getCollection("during_test_created");
		assertEquals(0, collection.count());
		
		collection.save(new BasicDBObject("test", "test"));
		
		assertEquals(1, collection.count());
		dbCollection.remove(new BasicDBObject("_id", 1));
	}
	
	@Test
	@JsonData(loadStrategy=MongoDbCleanStrategy.class, loadData=false)
	public void testCleanStrategie() {
	}
	
	//##################### help methods ####################
	
	private static<T extends Number> Map<T, DBObject> getMap(DBCursor find) {
		Map<T, DBObject> map = new HashMap<T, DBObject>();
		while(find.hasNext()) {
			DBObject dbObject = find.next();
			map.put((T) dbObject.get("_id"), dbObject);
		}
		return map;
	}
}