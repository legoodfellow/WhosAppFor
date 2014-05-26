package edu.dartmouth.cs.whosupfor.server.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import edu.dartmouth.cs.whosupfor.util.Globals;

public class UserDatastore {
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getParentKey() {
		return KeyFactory.createKey(DataGlobals.ENTITY_KIND_USER_PARENT,
				DataGlobals.ENTITY_USER_PARENT_KEY);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getParentKey());

		mDatastore.put(entity);
	}

	public static boolean add(UserEntry user) {
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(DataGlobals.ENTITY_KIND_USER,
				user.getID(), parentKey);

		setEntityFromUserEntry(entity, user);
		
		mDatastore.put(entity);

		return true;
	}
	
	public static boolean update(UserEntry user) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getParentKey(),
					DataGlobals.ENTITY_KIND_USER, user.getID()));
			
			setEntityFromUserEntry(result, user);

			mDatastore.put(result);
		} catch (Exception ex) {
		}
		return false;
	}
	
	public static boolean delete(long id) {
		// query
		Filter filter = new FilterPredicate(Globals.KEY_USER_ROWID,
				FilterOperator.EQUAL, id);

		Query query = new Query(Globals.KEY_USER_ROWID);
		query.setFilter(filter);

		PreparedQuery pq = mDatastore.prepare(query);

		Entity result = pq.asSingleEntity();
		boolean ret = false;
		if (result != null) {
			// delete
			mDatastore.delete(result.getKey());
			ret = true;
		}

		return ret;
	}
	
	public static UserEntry getUserById(long id, Transaction txn) {
		Entity result = null;
		try {
			result = mDatastore.get(KeyFactory.createKey(getParentKey(),
					DataGlobals.ENTITY_KIND_USER, id));
		} catch (Exception ex) {
		}

		return getUserEntryFromEntity(result);
	}
	
	public static ArrayList<UserEntry> query() {
		ArrayList<UserEntry> resultList = new ArrayList<UserEntry>();

		Query query = new Query(DataGlobals.ENTITY_KIND_USER);
		query.setFilter(null);
		query.setAncestor(getParentKey());
		PreparedQuery pq = mDatastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			UserEntry user = getUserEntryFromEntity(entity);
			
			resultList.add(user);
		}
		return resultList;
	}

	private static void setEntityFromUserEntry(Entity entity, UserEntry user){
		if (entity == null){
			return;
		}
		
		entity.setProperty(Globals.KEY_USER_ROWID, user.getID());
		entity.setProperty(Globals.KEY_USER_EMAIL, user.getEmail());
		entity.setProperty(Globals.KEY_USER_FIRST_NAME, user.getFirstName());
		entity.setProperty(Globals.KEY_USER_LAST_NAME, user.getLastName());
		entity.setProperty(Globals.KEY_USER_BIO, user.getBio());
		entity.setProperty(Globals.KEY_USER_GENDER, user.getGender());
		entity.setProperty(Globals.KEY_USER_CLASS_YEAR, user.getClassYear());
		entity.setProperty(Globals.KEY_USER_MAJOR, user.getMajor());
		entity.setProperty(Globals.KEY_USER_PROFILE_PHOTO, user.getProfilePhoto());
		entity.setProperty(Globals.KEY_USER_PASSWORD, user.getPassword());
	}
	
	private static UserEntry getUserEntryFromEntity(Entity entity){
		if (entity == null){
			return null;
		}
		
		UserEntry user = new UserEntry();
		user.setID((long) entity.getProperty(Globals.KEY_USER_ROWID));
		user.setEmail((String) entity.getProperty(Globals.KEY_USER_EMAIL));
		user.setFirstName((String) entity.getProperty(Globals.KEY_USER_FIRST_NAME));
		user.setLastName((String) entity.getProperty(Globals.KEY_USER_LAST_NAME));
		user.setBio((String) entity.getProperty(Globals.KEY_USER_BIO));
		user.setGender((int) entity.getProperty(Globals.KEY_USER_GENDER));
		user.setClassYear((int) entity.getProperty(Globals.KEY_USER_CLASS_YEAR));
		user.setMajor((String) entity.getProperty(Globals.KEY_USER_MAJOR));
		user.setProfilePhoto((byte[]) entity.getProperty(Globals.KEY_USER_PROFILE_PHOTO));
		user.setPassword((String) entity.getProperty(Globals.KEY_USER_PASSWORD));
		
		return user;
	}
	
}
