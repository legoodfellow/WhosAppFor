package edu.dartmouth.cs.whosupfor.server.data;

import java.util.ArrayList;
import java.util.Calendar;

public class EventEntity {
	public static String ENTITY_KIND_PARENT = "EventParent";
	public static String ENTITY_PARENT_KEY = ENTITY_KIND_PARENT;
	public static String ENTITY_KIND_EVENT = "Event";

	public static String FIELD_NAME_ID = "DbId";
	public static String FIELD_NAME_EMAIL = "Email";
	public static String FIELD_NAME_EVENT_TITLE = "EventTitle";
	public static String FIELD_NAME_EVENT_TYPE = "EventType";
	public static String FIELD_NAME_LOCATION = "Location";
	public static String FIELD_NAME_TIMESTAMP = "TimeStamp";
	public static String FIELD_NAME_START_DATE_TIME = "StartDateTime";
	public static String FIELD_NAME_END_DATE_TIME = "EndDateTime";
	public static String FIELD_NAME_DETAILS = "Details";
	public static String FIELD_NAME_ATTENDEES = "Attendees";
	public static String FIELD_NAME_CIRCLE = "Circle";

	public long mDbId;
	public String mEmail;
	public int mEventType;
	public String mEventTitle;
	public String mLocation;
	public Calendar mTimeStamp;
	public Calendar mStartDateTime;
	public Calendar mEndDateTime;
	public String mDetail;
	public ArrayList<String> mAttendees;
	public int mCircle;

	public EventEntity() {
		mEmail = null;
		mEventTitle = null;
		mLocation = null;
		mTimeStamp = null;
		mStartDateTime = null;
		mEndDateTime = null;
		mDetail = null;
		mAttendees = null;
	}

	public long getDbId() {
		return mDbId;
	}

	public void setDbId(Long dbId) {
		this.mDbId = dbId;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}

	public int getEventType() {
		return mEventType;
	}

	public void setEventType(int eventType) {
		this.mEventType = eventType;
	}

	public String getEventTitle() {
		return mEventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.mEventTitle = eventTitle;
	}

	public String getLocation() {
		return mLocation;
	}

	public void setLocation(String location) {
		this.mLocation = location;
	}

	public Calendar getTimeStamp() {
		return mTimeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.mTimeStamp = timeStamp;
	}

	public Calendar getStartDateTime() {
		return mStartDateTime;
	}

	public void setStartDateTime(Calendar startDateTime) {
		this.mStartDateTime = startDateTime;
	}

	public Calendar getEndDateTime() {
		return mEndDateTime;
	}

	public void setEndDateTime(Calendar endDateTime) {
		this.mEndDateTime = endDateTime;
	}

	public String getDetail() {
		return mDetail;
	}

	public void setDetail(String detail) {
		this.mDetail = detail;
	}

	public ArrayList<String> getAttendees() {
		return mAttendees;
	}

	public void setAttendees(ArrayList<String> attendees) {
		this.mAttendees = attendees;
	}

	public int getCircle() {
		return mCircle;
	}

	public void setCircle(int circle) {
		this.mCircle = circle;
	}

	
}
