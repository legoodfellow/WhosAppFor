package edu.dartmouth.cs.whosupfor.server.data;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class UserDatastore {
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getParentKey() {
		return KeyFactory.createKey(UserEntity.ENTITY_KIND_PARENT,
				UserEntity.ENTITY_PARENT_KEY);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getParentKey());

		mDatastore.put(entity);
	}

	public static boolean add(UserEntity user) {
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(UserEntity.ENTITY_KIND_USER,
				user.getDbId(), parentKey);

		entity.setProperty(UserEntity.FIELD_NAME_ID, user.getDbId());
		entity.setProperty(UserEntity.FIELD_NAME_EMAIL, user.getEmail());
		entity.setProperty(UserEntity.FIELD_NAME_FIRST_NAME, user.getFirstName());
		entity.setProperty(UserEntity.FIELD_NAME_LAST_NAME, user.getLastName());
		entity.setProperty(UserEntity.FIELD_NAME_BIO, user.getBio());
		entity.setProperty(UserEntity.FIELD_NAME_GENDER, user.getGender());
		entity.setProperty(UserEntity.FIELD_NAME_CLASS_YEAR, user.getClassYear());
		entity.setProperty(UserEntity.FIELD_NAME_MAJOR, user.getMajor());
		entity.setProperty(UserEntity.FIELD_NAME_PROFILE_PHOTO, user.getProfilePhoto());
		entity.setProperty(UserEntity.FIELD_NAME_PASSWORD, user.getPassword());
	

		
		mDatastore.put(entity);

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<UserEntity> query() {
		ArrayList<UserEntity> resultList = new ArrayList<UserEntity>();

		Query query = new Query(UserEntity.ENTITY_KIND_USER);
		query.setFilter(null);
		query.setAncestor(getParentKey());
		PreparedQuery pq = mDatastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			UserEntity user = new UserEntity();
			user.setDbId((Long)entity.getProperty(UserEntity.FIELD_NAME_ID));
			user.setEmail((String) entity.getProperty(UserEntity.FIELD_NAME_EMAIL));
			user.setFirstName((String) entity.getProperty(UserEntity.FIELD_NAME_FIRST_NAME));
			user.setLastName((String) entity.getProperty(UserEntity.FIELD_NAME_LAST_NAME));
			user.setBio((String) entity.getProperty(UserEntity.FIELD_NAME_BIO));
			user.setGender((int) entity.getProperty(UserEntity.FIELD_NAME_GENDER));
			user.setClassYear((int) entity.getProperty(UserEntity.FIELD_NAME_CLASS_YEAR));
			user.setMajor((String) entity.getProperty(UserEntity.FIELD_NAME_MAJOR));
			user.setProfilePhoto((byte[]) entity.getProperty(UserEntity.FIELD_NAME_PROFILE_PHOTO));
			user.setPassword((String) entity.getProperty(UserEntity.FIELD_NAME_PASSWORD));
			
			
			resultList.add(user);
		}
		return resultList;
	}

}
