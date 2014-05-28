package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.server.data.DataGlobals;
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

		String postText = req.getParameter(DataGlobals.POST_KEY_POST_TEXT);
		String taskType = req.getParameter(DataGlobals.POST_KEY_TASK_TYPE);
		
		if (taskType.equals(DataGlobals.TASK_TYPE_CREATE_NEW_EVENT)) {
			createNewEvent(postText);
			resp.sendRedirect("/sendmsg.do");
		}
		
		if (taskType.equals(DataGlobals.TASK_TYPE_CREATE_NEW_USER)) {
			createNewUser(postText);
			// TODO: redirect to sendmsg for user data
		}
		
		if (taskType.equals(DataGlobals.TASK_TYPE_REPLY_GOING)) {
			long eventId; // TODO: get eventID, eventually this will be string not long
			String attendee; // TODO: get attendee
//			if (EventDatastore.addAttendee(eventId, attendee)) {
//				resp.sendRedirect("/sendmsg.do");
//			}
		}
		
		if (taskType.equals(DataGlobals.TASK_TYPE_REPLY_GOING)) {
			long eventId; // TODO: get eventID, eventually this will be string not long
			String attendee; // TODO: get attendee
//			if (EventDatastore.deleteAttendee(eventId, attendee)) {
//				resp.sendRedirect("/sendmsg.do");
//			}
		}
		
		if (taskType.equals(DataGlobals.TASK_TYPE_GET_EVENTS)) {
			resp.sendRedirect("/get_history.do");
		}
		
		if (taskType.equals(DataGlobals.TASK_TYPE_GET_USERS)) {
			// TODO: resp.sendRedirect("/get_history.do"); for user data!
		}

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

	
	public void createNewEvent(String postText) {
		try {
			JSONArray jsonArray = new JSONArray(postText);
			for (int i=0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.has(Globals.KEY_EVENT_ROWID)) {
					EventEntry event = jsonToEventEntry(jsonObject);
					EventDatastore.add(event);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void createNewUser(String postText) {
		try {
			JSONArray jsonArray = new JSONArray(postText);
			for (int i=0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.has(Globals.KEY_USER_ROWID)) {
					UserEntry user = jsonToUserEntry(jsonObject);
					UserDatastore.add(user);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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

}
