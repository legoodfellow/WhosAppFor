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

public class EventDatastore {
	private static final DatastoreService mDatastore = DatastoreServiceFactory
			.getDatastoreService();

	private static Key getParentKey() {
		return KeyFactory.createKey(EventEntity.ENTITY_KIND_PARENT,
				EventEntity.ENTITY_PARENT_KEY);
	}

	private static void createParentEntity() {
		Entity entity = new Entity(getParentKey());

		mDatastore.put(entity);
	}

	public static boolean add(EventEntity event) {
		Key parentKey = getParentKey();
		try {
			mDatastore.get(parentKey);
		} catch (Exception ex) {
			createParentEntity();
		}

		Entity entity = new Entity(EventEntity.ENTITY_KIND_EVENT,
				event.getDbId(), parentKey);

		entity.setProperty(EventEntity.FIELD_NAME_ID, event.getDbId());
		entity.setProperty(EventEntity.FIELD_NAME_EMAIL, event.getEmail());
		entity.setProperty(EventEntity.FIELD_NAME_EVENT_TITLE, event.getEventTitle());
		entity.setProperty(EventEntity.FIELD_NAME_EVENT_TYPE, event.getEventType());
		entity.setProperty(EventEntity.FIELD_NAME_LOCATION, event.getLocation());
		entity.setProperty(EventEntity.FIELD_NAME_TIMESTAMP, event.getTimeStamp());
		entity.setProperty(EventEntity.FIELD_NAME_START_DATE_TIME, event.getStartDateTime());
		entity.setProperty(EventEntity.FIELD_NAME_END_DATE_TIME, event.getEndDateTime());
		entity.setProperty(EventEntity.FIELD_NAME_DETAILS, event.getDetail());
		entity.setProperty(EventEntity.FIELD_NAME_ATTENDEES, event.getAttendees());
		entity.setProperty(EventEntity.FIELD_NAME_CIRCLE, event.getCircle());
		
		mDatastore.put(entity);

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EventEntity> query() {
		ArrayList<EventEntity> resultList = new ArrayList<EventEntity>();

		Query query = new Query(EventEntity.ENTITY_KIND_EVENT);
		query.setFilter(null);
		query.setAncestor(getParentKey());
		query.addSort(EventEntity.FIELD_NAME_TIMESTAMP, SortDirection.ASCENDING);
		PreparedQuery pq = mDatastore.prepare(query);

		for (Entity entity : pq.asIterable()) {
			EventEntity event = new EventEntity();
			event.setDbId((Long)entity.getProperty(EventEntity.FIELD_NAME_ID));
			event.setEmail((String) entity.getProperty(EventEntity.FIELD_NAME_EMAIL));
			event.setEventTitle((String) entity.getProperty(EventEntity.FIELD_NAME_EVENT_TITLE));
			event.setEventType((int) entity.getProperty(EventEntity.FIELD_NAME_EVENT_TYPE));
			event.setLocation((String) entity.getProperty(EventEntity.FIELD_NAME_LOCATION));
			event.setTimeStamp((Calendar) entity.getProperty(EventEntity.FIELD_NAME_TIMESTAMP));
			event.setStartDateTime((Calendar) entity.getProperty(EventEntity.FIELD_NAME_START_DATE_TIME));
			event.setEndDateTime((Calendar) entity.getProperty(EventEntity.FIELD_NAME_END_DATE_TIME));
			event.setDetail((String) entity.getProperty(EventEntity.FIELD_NAME_DETAILS));
			Object attendeesProperty = entity.getProperty(EventEntity.FIELD_NAME_ATTENDEES);
			if (attendeesProperty instanceof ArrayList<?>) {
				event.setAttendees((ArrayList<String>) attendeesProperty);
			}
			event.setCircle((int) entity.getProperty(EventEntity.FIELD_NAME_CIRCLE));
				
			resultList.add(event);
		}
		return resultList;
	}

}
