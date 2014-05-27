package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.server.data.EventDatastore;
import edu.dartmouth.cs.whosupfor.server.data.EventEntry;
import edu.dartmouth.cs.whosupfor.server.data.UserDatastore;
import edu.dartmouth.cs.whosupfor.server.data.UserEntry;
import edu.dartmouth.cs.whosupfor.util.Globals;


public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String ENTITY_KIND_KEY = "EntityKind";

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String postText = req.getParameter("post_text");

		try {
			JSONArray jsonArray = new JSONArray(postText);
			System.out.println("jsonArray: " + jsonArray.toString());
			for (int i=0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.has(Globals.KEY_EVENT_ROWID)) {
					EventEntry event = jsonToEventEntry(jsonObject);
					EventDatastore.add(event);
				}

				if (jsonObject.has(Globals.KEY_USER_ROWID)) {
					UserEntry user = jsonToUserEntry(jsonObject);
					UserDatastore.add(user);
				}
			}
			
			
			resp.sendRedirect("/sendmsg.do");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}


	@SuppressWarnings("unchecked")
	public static EventEntry jsonToEventEntry(JSONObject eventJSON) {
		EventEntry event = new EventEntry();
		System.out.println(eventJSON);
		
		if (eventJSON != null) {
			event.setID(eventJSON.optLong(Globals.KEY_EVENT_ROWID));
			event.setEmail(eventJSON.optString(Globals.KEY_EVENT_EMAIL));
			event.setEventType(eventJSON.optInt(Globals.KEY_EVENT_TYPE));
			event.setEventTitle(eventJSON.optString(Globals.KEY_EVENT_TITLE));
			event.setLocation(eventJSON.optString(Globals.KEY_EVENT_LOCATION));
			event.setDetail(eventJSON.optString(Globals.KEY_EVENT_DETAIL));
			event.setCircle(eventJSON.optInt(Globals.KEY_EVENT_CIRCLE));
			event.setTimeStamp(eventJSON.optLong(Globals.KEY_EVENT_TIME_STAMP));
			event.setStartDateTime(eventJSON.optLong(Globals.KEY_EVENT_START_DATE_TIME));
			event.setEndDateTime(eventJSON.optLong(Globals.KEY_EVENT_END_DATE_TIME));
			JSONArray attendees = eventJSON.optJSONArray(Globals.KEY_EVENT_ATTENDEES);
			for (int i=0; i < attendees.length(); i++){
				String attendee = attendees.optString(i, null);
				if (attendee != null) {
					event.addAttendee(attendee);
				}
			}
		}
		return event;
	}

	public static UserEntry jsonToUserEntry(JSONObject userJSON) {
		UserEntry user = new UserEntry();

		if (userJSON != null){
			user.setID(userJSON.optLong(Globals.KEY_USER_ROWID));
			user.setEmail(userJSON.optString(Globals.KEY_USER_EMAIL));
			user.setFirstName(userJSON.optString(Globals.KEY_USER_FIRST_NAME));
			user.setLastName(userJSON.optString(Globals.KEY_USER_LAST_NAME));
			user.setBio(userJSON.optString(Globals.KEY_USER_BIO));
			user.setGender(userJSON.optInt(Globals.KEY_USER_GENDER));
			user.setClassYear(userJSON.optInt(Globals.KEY_USER_CLASS_YEAR));
			user.setMajor(userJSON.optString(Globals.KEY_USER_MAJOR));
			user.setProfilePhoto((byte[]) userJSON.opt(Globals.KEY_USER_PROFILE_PHOTO));
			user.setPassword(userJSON.optString(Globals.KEY_USER_PASSWORD));
		}
		return user;
	}


	//
	//	private void addEventToDatastore(JSONObject eventJSON){
	//		EventEntity event = new EventEntity();
	//		
	//		event.setDbId(eventJSON.optLong(EventEntity.FIELD_NAME_ID));
	//		event.setEmail(eventJSON.optString(EventEntity.FIELD_NAME_EMAIL));
	//		event.setEventType(eventJSON.optInt(EventEntity.FIELD_NAME_EVENT_TYPE));
	//		event.setEventTitle(eventJSON.optString(EventEntity.FIELD_NAME_EVENT_TITLE));
	//		event.setLocation(eventJSON.optString(EventEntity.FIELD_NAME_LOCATION));
	//		event.setDetail(eventJSON.optString(EventEntity.FIELD_NAME_DETAILS));
	//		event.setCircle(eventJSON.optInt(EventEntity.FIELD_NAME_CIRCLE));
	//		
	//		ArrayList<String> attendees = new ArrayList<String>();
	//		JSONArray attendeesJSON = eventJSON.optJSONArray(EventEntity.FIELD_NAME_ATTENDEES);
	//		if (attendeesJSON != null) {
	//			for (int i=0; i<attendeesJSON.length(); i++){
	//				attendees.add(attendees.get(i));
	//			}
	//		}
	//		event.setAttendees(attendees);
	//		
	//		Calendar timestamp = Calendar.getInstance();
	//		timestamp.setTimeInMillis(eventJSON.optLong(EventEntity.FIELD_NAME_TIMESTAMP));
	//		event.setTimeStamp(timestamp);
	//		
	//		Calendar startDateTime = Calendar.getInstance();
	//		startDateTime.setTimeInMillis(eventJSON.optLong(EventEntity.FIELD_NAME_START_DATE_TIME));
	//		event.setStartDateTime(startDateTime);
	//		
	//		Calendar endDateTime = Calendar.getInstance();
	//		endDateTime.setTimeInMillis(eventJSON.optLong(EventEntity.FIELD_NAME_END_DATE_TIME));
	//		event.setEndDateTime(endDateTime);
	//		
	//	}
	//
	//	private void addUserToDatastore(JSONObject userJSON){
	//		UserEntity user = new UserEntity();
	//		
	//		user.setDbId(userJSON.optLong(UserEntity.FIELD_NAME_ID));
	//		user.setEmail(userJSON.optString(UserEntity.FIELD_NAME_EMAIL));
	//		user.setFirstName(userJSON.optString(UserEntity.FIELD_NAME_FIRST_NAME));
	//		user.setLastName(userJSON.optString(UserEntity.FIELD_NAME_LAST_NAME));
	//		user.setBio(userJSON.optString(UserEntity.FIELD_NAME_BIO));
	//		user.setGender(userJSON.optInt(UserEntity.FIELD_NAME_GENDER));
	//		user.setClassYear(userJSON.optInt(UserEntity.FIELD_NAME_CLASS_YEAR));
	//		user.setMajor(userJSON.optString(UserEntity.FIELD_NAME_MAJOR));
	//		user.setProfilePhoto(Base64.decode(userJSON.optString(UserEntity.FIELD_NAME_PROFILE_PHOTO)));
	//		user.setPassword(userJSON.optString(UserEntity.FIELD_NAME_PASSWORD));
	//		
	//		UserDatastore.add(user);
	//	}

}
