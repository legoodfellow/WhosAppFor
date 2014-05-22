package edu.dartmouth.cs.whosupfor.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
	private String mEventTitle; // Brief description of the event (�Dinner�,
								// �Studying for CS65�, �Playing frisbee�, etc.)
	private String mLocation; // The organizer�s text description of the
								// location (�Boloco�, �Sudikoff�, etc) (GPS
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
	private String mDetail; // The organizer�s comment with event details
	private ArrayList<String> mAttendees; // List of attendee IDs
	private int mCircle; // Indicates which friend circle can view the event (
							// e.g. 0 = public, 1 = friends)

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			Globals.DATE_FORMAT_NOW);
	private static final SimpleDateFormat stf = new SimpleDateFormat(
			Globals.TIME_FORMAT_NOW);

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
	}

	/**
	 * Get the json file from web and convert it back to ExerciseEntry
	 * 
	 * @param obj
	 * @return
	 */
	public JSONObject fromJSONObject(JSONObject obj) {
		try {
			mDbId = obj.getLong("mDbId");
			mEventType = obj.getInt("inputType");

			// mStartDate = obj.getString("date");
			// mStartTime = obj.getString("time");

		} catch (JSONException e) {
			return null;
		}
		return obj;
	}

	/**
	 * Convert ExerciseEntry to JSON file upload it to web
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("mDbId", mDbId);
			obj.put("inputType", mEventType);

			// obj.put("dateTime", mStartDate);
			// obj.put("time", mStartTime);

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

	/**
	 * Get start data and time and render them in the text view
	 * 
	 * @return
	 */
	public long getStartDateTimeInMillis() {
		return mStartDateTime.getTimeInMillis();
	}

	/**
	 * Get end data and time and render them in the text view
	 * 
	 * @return
	 */
	public long getEndDateTimeInMillis() {
		return mEndDateTime.getTimeInMillis();
	}

	public void setDateTime(Calendar dateTime) {
		this.mStartDateTime = dateTime;

	}

	public void setDateTime(long timestamp) {
		this.mStartDateTime.setTimeInMillis(timestamp);

	}

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

	/**
	 * Set time stamp
	 * 
	 * @param timeStamp
	 */
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
}
