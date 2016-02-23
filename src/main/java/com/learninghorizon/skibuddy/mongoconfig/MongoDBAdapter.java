package com.learninghorizon.skibuddy.mongoconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.learninghorizon.skibuddy.configbean.SpringMongoConfig;
import com.learninghorizon.skibuddy.model.SkiEvent;
import com.learninghorizon.skibuddy.model.SkiSession;
import com.learninghorizon.skibuddy.model.User;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBAdapter {
	Random rand = new Random();
	private ApplicationContext ctx;
	private MongoOperations mongoOperation;
	private static final Logger logger = LoggerFactory.getLogger(MongoDBAdapter.class);

	public MongoDBAdapter() {
		this.ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	public User getUser(String facebookId) {
		MongoClient client = null;
		User user = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + facebookId + "'}").as(User.class);
			if (null != userInserted && userInserted.count() != 0) {
				user = userInserted.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public String addUser(String facebookId, User user) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + facebookId + "'}").as(User.class);

			if (null != userInserted && userInserted.count() <= 0) {
				friends.insert(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			userInserted.close();
			client.close();

		}
		String opStatus = "";
		return opStatus;
	}

	public String updateTagLine(String facebookId, String tagLine) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + facebookId + "'}").as(User.class);

			if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + facebookId + "'}").with("{$set: {tagLine: #}}", tagLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			userInserted.close();
			client.close();

		}
		String opStatus = "";
		return opStatus;
	}

	public String saveSession(String facebookId, SkiSession skiSession) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + facebookId + "'}").as(User.class);

			if (null != userInserted && userInserted.count() > 0) {
				User user = userInserted.next();
				user.setSkiSession(skiSession);
				//friends.update("{facebookId: '" + facebookId + "'}").with(user);
				friends.update("{facebookId: '" + facebookId + "'}").with("{$addToSet:{skiSessions:#}}",skiSession);
				
//				DBObject updateDbo = queryFactory.createQuery("{$set:#}", pojo).toDBObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			userInserted.close();
			client.close();

		}
		String opStatus = "";
		return opStatus;
	}

	public List<User> getAllUsers(String userId) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		List<User> usersList = new ArrayList<User>();
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find().as(User.class);

			if (null != userInserted && userInserted.count() > 0) {
				for (User singleUser : userInserted) {
					if (!singleUser.getFacebookId().equals(userId)) {
						usersList.add(singleUser);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return usersList;
		} finally {
			userInserted.close();
			client.close();

		}
		return usersList;
	}

	public void updateUser(User user) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + user.getFacebookId() + "'}").as(User.class);

			if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + user.getFacebookId() + "'}").with(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			userInserted.close();
			client.close();

		}
		String opStatus = "";
	}

	public void createEvent( User user) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			userInserted = friends.find("{facebookId: '" + user.getFacebookId() + "'}").as(User.class);

			if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + user.getFacebookId() + "'}").with(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			userInserted.close();
			client.close();

		}
		String opStatus = "";
	}
	
	public void createEvent(SkiEvent skiEvent) throws IOException {
		MongoClient client = null;
		MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("events");
				friends.insert(skiEvent);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			//userInserted.close();
			client.close();

		}
		String opStatus = "";
	}

	public void createdEvent(SkiEvent skiEvent, String userId) {
		MongoClient client = null;
		//MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			//userInserted = friends.find("{facebookId: '" + userId + "'}").as(User.class);

			//if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + userId + "'}").with("{$addToSet:{createdEvents:#}}",skiEvent);
			//}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			//userInserted.close();
			client.close();

		}
	}

	public void createEventInvite(String userId, SkiEvent skiEvent) {
		MongoClient client = null;
		//MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			//userInserted = friends.find("{facebookId: '" + userId + "'}").as(User.class);

			//if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + userId + "'}").with("{$addToSet:{eventInvites:#}}",skiEvent);
			//}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			//userInserted.close();
			client.close();

		}
	}

	public void updateShareLocation(String userId, boolean shareLocation) {
		MongoClient client = null;
		//MongoCursor<User> userInserted = null;
		try {
			String opStatus = "";
			client = new MongoClient(
					new MongoClientURI("mongodb://skibuddy:skibuddy@ds059654.mongolab.com:59654/skibuddy"));
			DB db = client.getDB("skibuddy");
			Jongo jongo = new Jongo(db);
			MongoCollection friends = jongo.getCollection("skibuddy");

			//userInserted = friends.find("{facebookId: '" + userId + "'}").as(User.class);

			//if (null != userInserted && userInserted.count() > 0) {
				friends.update("{facebookId: '" + userId + "'}").with("{$set:{shareLocation:#}}",shareLocation);
			//}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			//userInserted.close();
			client.close();

		}
	}
}
