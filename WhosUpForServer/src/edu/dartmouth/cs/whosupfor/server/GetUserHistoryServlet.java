package edu.dartmouth.cs.whosupfor.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.server.data.UserDatastore;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class GetUserHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ArrayList<UserEntry> userList = UserDatastore.query();

		PrintWriter out = resp.getWriter();
		JSONArray jsonArray = new JSONArray();
		
		for (UserEntry user : userList) {
			jsonArray.put(userEntryToJSONObject(user));
		}
		
		out.append(jsonArray.toString());
		
		
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
	public static JSONObject userEntryToJSONObject(UserEntry user) {
		JSONObject obj = new JSONObject();

		try {
			obj.put(Globals.KEY_USER_EMAIL, user.getEmail());
			obj.put(Globals.KEY_USER_FIRST_NAME, user.getFirstName());
			obj.put(Globals.KEY_USER_LAST_NAME, user.getLastName());
			obj.put(Globals.KEY_USER_BIO, user.getBio());
			obj.put(Globals.KEY_USER_GENDER, user.getGender());
			obj.put(Globals.KEY_USER_CLASS_YEAR, user.getClassYear());
			obj.put(Globals.KEY_USER_MAJOR, user.getMajor());
			obj.put(Globals.KEY_USER_PASSWORD, user.getPassword());
			obj.put(Globals.KEY_USER_PROFILE_PHOTO, user.getProfilePhoto());
			
		} catch (JSONException e) {
			return null;
		}

		return obj;
	}
	
	
}
