package edu.dartmouth.cs.whosupfor.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.util.Globals;

/**
 * ADT for storing event entry data using email as the unique user id
 * 
 * @author Aaron Jun Yang
 * 
 */
public class EventEntry {
	private Long mDbId; // Database mDbId of the entry
	// private String mOrganizerId; // User id of the person who created this
	// event
	private String mEmail;
	private int mEventType; // eg: Food, Study, Movie....
	private String mEventTitle; // Brief description of the event ("Dinner",
	// "Studying for CS65", "Playing frisbee", etc.)
	private String mLocation; // The organizer's text description of the
	// location ("Boloco", "Sudikoff", etc) (GPS
	// data might be added for v2)
	private Calendar mTimeStamp; // Automatically generated timestamp in
									// milliseconds
									// of when the event was created
	private Calendar mStartDateTime;
	private Calendar mEndDateTime;
	// private String mStartDate; // Event start date
	// private String mStartTime; // Event start time
	// private String mEndDate; // Event end date
	// private String mEndTime; // Event end time
	private String mDetail; // The organizer's comment with event details
	private ArrayList<String> mAttendees; // List of attendee IDs
	private int mCircle; // Indicates which friend circle can view the event (
							// e.g. 0 = public, 1 = friends)
	private String mEventId;

	public EventEntry() {
		this.mEventType = -1;
		this.mEventTitle = " ";
		this.mEmail = " ";
		this.mLocation = " ";
		this.mStartDateTime = Calendar.getInstance();
		this.mEndDateTime = Calendar.getInstance();
		this.mTimeStamp = Calendar.getInstance();
		// this.mStartDate = sdf.format(Calendar.getInstance().getTime());
		// this.mStartTime = stf.format(Calendar.getInstance().getTime());
		// this.mEndDate = sdf.format(Calendar.getInstance().getTime());
		// this.mEndTime = stf.format(Calendar.getInstance().getTime());
		this.mDetail = " ";
		this.mAttendees = new ArrayList<String>();
		this.mCircle = -1;
		this.mEventId = " ";
	}

	/**
	 * Get the json file from web and convert it back to ExerciseEntry
	 * 
	 * @param obj
	 * @return
	 */
	public void fromJSONObject(JSONObject obj) {
		mDbId = obj.optLong(Globals.KEY_EVENT_ROWID);
		mEmail = obj.optString(Globals.KEY_EVENT_EMAIL);
		mEventType = obj.optInt(Globals.KEY_EVENT_TYPE);
		mEventTitle = obj.optString(Globals.KEY_EVENT_TITLE);
		mLocation = obj.optString(Globals.KEY_EVENT_LOCATION);
		mTimeStamp.setTimeInMillis(obj.optLong(Globals.KEY_EVENT_TIME_STAMP));
		mStartDateTime.setTimeInMillis(obj
				.optLong(Globals.KEY_EVENT_START_DATE_TIME));
		mEndDateTime.setTimeInMillis(obj
				.optLong(Globals.KEY_EVENT_END_DATE_TIME));
		mDetail = obj.optString(Globals.KEY_EVENT_DETAIL);
		mCircle = obj.optInt(Globals.KEY_EVENT_CIRCLE);
		mEventId = obj.optString(Globals.KEY_EVENT_ID);

		JSONArray attendees = obj.optJSONArray(Globals.KEY_EVENT_ATTENDEES);
		for (int i = 0; i < attendees.length(); i++) {
			String attendee = attendees.optString(i, null);
			if (attendee != null) {
				mAttendees.add(attendee);
			}
		}
	}

	/**
	 * Convert ExerciseEntry to JSON file upload it to web
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();

		try {
			obj.put(Globals.KEY_EVENT_ROWID, mDbId);
			obj.put(Globals.KEY_EVENT_EMAIL, mEmail);
			obj.put(Globals.KEY_EVENT_TYPE, mEventType);
			obj.put(Globals.KEY_EVENT_TITLE, mEventTitle);
			obj.put(Globals.KEY_EVENT_LOCATION, mLocation);
			obj.put(Globals.KEY_EVENT_TIME_STAMP, mTimeStamp.getTimeInMillis());
			obj.put(Globals.KEY_EVENT_START_DATE_TIME,
					mStartDateTime.getTimeInMillis());
			obj.put(Globals.KEY_EVENT_END_DATE_TIME,
					mEndDateTime.getTimeInMillis());
			obj.put(Globals.KEY_EVENT_DETAIL, mDetail);
			obj.put(Globals.KEY_EVENT_CIRCLE, mCircle);
			obj.put(Globals.KEY_EVENT_ID, mEventId);

			JSONArray attendees = new JSONArray();
			for (int i = 0; i < mAttendees.size(); i++) {
				attendees.put(mAttendees.get(i));
			}
			obj.put(Globals.KEY_EVENT_ATTENDEES, attendees);
		} catch (JSONException e) {
			return null;
		}

		return obj;
	}

	/**
	 * Set rowID
	 * 
	 * @param mDbId
	 */
	public void setID(Long id) {
		this.mDbId = id;
	}

	public Long getID() {
		return mDbId;
	}

	/**
	 * Set unique event id
	 */
	public void setEventId() {
		mEventId = mEmail + String.valueOf(mTimeStamp.getTimeInMillis());
	}

	public void setEventId(String eventId) {
		mEventId = eventId;
	}

	public String getEventId() {
		return mEventId;
	}

	/**
	 * Set email
	 * 
	 * @param organizerId
	 */
	public void setEmail(String email) {
		mEmail = email;
	}

	public String getEmail() {
		return mEmail;
	}

	/**
	 * Set event type
	 * 
	 * @param inputType
	 */
	public void setEventType(int eventType) {
		mEventType = eventType;
	}

	public int getEventType() {
		return mEventType;
	}

	/**
	 * Set event title
	 * 
	 * @param eventTitle
	 */
	public void setEventTitle(String eventTitle) {
		mEventTitle = eventTitle;
	}

	public String getEventTitle() {
		return mEventTitle;
	}

	/**
	 * Set event location
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		mLocation = location;
	}

	public String getLocation() {
		return mLocation;
	}

	public Calendar getDateTime() {
		return mStartDateTime;
	}

	public void setDateTime(long timestamp) {
		this.mStartDateTime.setTimeInMillis(timestamp);

	}

	// -------------------------------------------------------------------
	// Start date and time
	/**
	 * Set start date and time
	 * 
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public void setStartDate(int year, int monthOfYear, int dayOfMonth) {
		mStartDateTime.set(year, monthOfYear, dayOfMonth);
	}

	public void setStartTime(int hourOfDay, int minute) {
		mStartDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mStartDateTime.set(Calendar.MINUTE, minute);
		mStartDateTime.set(Calendar.SECOND, 0);
	}

	public void setStartDateTime(Calendar dateTime) {
		this.mStartDateTime = dateTime;

	}

	public void setStartDateTime(long timestamp) {
		this.mStartDateTime.setTimeInMillis(timestamp);

	}

	/**
	 * Get start data and time and render them in the text view
	 * 
	 * @return
	 */
	public long getStartDateTimeInMillis() {
		return mStartDateTime.getTimeInMillis();
	}

	// -----------------------------------------------------------
	// End date and time

	/**
	 * Set end date and time
	 * 
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public void setEndDate(int year, int monthOfYear, int dayOfMonth) {
		mEndDateTime.set(year, monthOfYear, dayOfMonth);
	}

	public void setEndTime(int hourOfDay, int minute) {
		mEndDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mEndDateTime.set(Calendar.MINUTE, minute);
		mEndDateTime.set(Calendar.SECOND, 0);
	}

	public void setEndDateTime(Calendar dateTime) {
		this.mEndDateTime = dateTime;

	}

	public void setEndDateTime(long timestamp) {
		this.mEndDateTime.setTimeInMillis(timestamp);

	}

	/**
	 * Get end data and time and render them in the text view
	 * 
	 * @return
	 */
	public long getEndDateTimeInMillis() {
		return mEndDateTime.getTimeInMillis();
	}

	// ---------------------------------------------------------
	// Timestamp
	/**
	 * Set time stamp
	 * 
	 * @param timeStamp
	 */
	public void setTimeStamp(long timeStamp) {
		this.mTimeStamp.setTimeInMillis(timeStamp);
	}

	public void setTimeStamp(Calendar timeStamp) {
		mTimeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return mTimeStamp.getTimeInMillis();
	}

	// /**
	// * Set Start date and time
	// *
	// * @param startDate
	// */
	// public void setStartDate(String startDate) {
	// mStartDate = startDate;
	// }
	//
	// public String getStartDate() {
	// return mStartDate;
	// }
	//
	// public void setStartTime(String startTime) {
	// mStartTime = startTime;
	// }
	//
	// public String getStartTime() {
	// return mStartTime;
	// }
	//
	// /**
	// * Set end date and time
	// *
	// * @param endDatena
	// */
	// public void setEndDate(String endDate) {
	// mStartDate = endDate;
	// }
	//
	// public String getEndDate() {
	// return mStartDate;
	// }
	//
	// public void setEndTime(String endTime) {
	// mStartTime = endTime;
	// }
	//
	// public String getEndTime() {
	// return mStartTime;
	// }

	/**
	 * Set comment
	 * 
	 * @param comment
	 */
	public void setDetail(String detail) {
		mDetail = detail;
	}

	public String getDetail() {
		return mDetail;
	}

	/**
	 * Set circle
	 * 
	 * @param circle
	 */
	public void setCircle(int circle) {
		mCircle = circle;
	}

	public int getCircle() {
		return mCircle;
	}

	/**
	 * Set attendees
	 * 
	 * @param attendees
	 */
	public void setAttendees(ArrayList<String> attendees) {
		mAttendees = attendees;
	}

	/**
	 * Set attendees
	 * 
	 * @param bytes
	 */
	public void setAttendees(byte[] bytes) {
		ArrayList<String> attendees = new ArrayList<String>();

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(bais);
		try {
			while (in.available() > 0) {
				String element = in.readUTF();
				attendees.add(element);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mAttendees = attendees;
	}

	public ArrayList<String> getAttendees() {
		return mAttendees;
	}

	/**
	 * Convert arraylist<String> to byte[], and store it in database
	 * 
	 * @return
	 */
	public byte[] getAttendeesByteArray() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (String attendee : mAttendees) {
			try {
				out.writeUTF(attendee);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] bytes = baos.toByteArray();

		return bytes;
	}

	public void addAttendee(String attendee) {
		if (mAttendees != null) {
			mAttendees.add(attendee);
		}

	}

}
