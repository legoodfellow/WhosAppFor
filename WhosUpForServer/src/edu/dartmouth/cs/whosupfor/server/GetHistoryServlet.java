package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.server.data.EventDatastore;
import edu.dartmouth.cs.whosupfor.server.data.EventEntry;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class GetHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<EventEntry> eventList = EventDatastore.query();

		PrintWriter out = resp.getWriter();
		JSONArray jsonArray = new JSONArray();
		
		
		for (EventEntry event : eventList) {
			//TODO: output event
			jsonArray.put(eventEntryToJSONObject(event));
		}
		
		out.append(jsonArray.toString());
		
		//TODO: user history?
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

	/**
	 * Convert ExerciseEntry to JSON file upload it to web
	 * 
	 * @return
	 */
	public static JSONObject eventEntryToJSONObject(EventEntry event) {
		JSONObject obj = new JSONObject();

		try {
			obj.put(Globals.KEY_EVENT_ROWID, event.getID());
			obj.put(Globals.KEY_EVENT_EMAIL, event.getEmail());
			obj.put(Globals.KEY_EVENT_TYPE, event.getEventType());
			obj.put(Globals.KEY_EVENT_TITLE, event.getEventTitle());
			obj.put(Globals.KEY_EVENT_LOCATION, event.getLocation());
			obj.put(Globals.KEY_EVENT_TIME_STAMP, event.getTimeStamp());
			obj.put(Globals.KEY_EVENT_START_DATE_TIME, event.getStartDateTimeInMillis());
			obj.put(Globals.KEY_EVENT_END_DATE_TIME, event.getEndDateTimeInMillis());
			obj.put(Globals.KEY_EVENT_DETAIL, event.getDetail());
			obj.put(Globals.KEY_EVENT_CIRCLE, event.getCircle());
//			JSONArray attendees = new JSONArray();
//			attendees.put(event.getAttendees());
			//obj.put(Globals.KEY_EVENT_ATTENDEES, (Collection<?>) event.getAttendees());
			
			//obj.put(Globals.KEY_EVENT_ATTENDEES, event.getAttendees());
		} catch (JSONException e) {
			return null;
		}

		return obj;
	}
	
	
}
