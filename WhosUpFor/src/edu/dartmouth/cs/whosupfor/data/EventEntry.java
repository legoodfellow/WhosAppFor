package edu.dartmouth.cs.whosupfor.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.dartmouth.cs.whosupfor.util.Globals;

/**
 * ADT for storing event data
 * 
 * @author Aaron Jun Yang
 * 
 */
public class EventEntry {
	private Long mDbId; // Database mDbId of the entry
	private String mOrganizer; // User id of the person who created this event
	private int mEventType; // eg: Food, Study, Movie....
	private String mStartDate; // Event start date
	private String mStartTime; // Event start time
	private String mEndDate; // Event end date
	private String mEndTime; // Event end time

	private double mDistance; // Distance traveled. Either in meters or feet.
	private double mAvgPace; // Average pace
	private double mAvgSpeed; // Average speed
	private int mCalorie; // Calories burnt
	private double mClimb; // Climb. Either in meters or feet.
	private int mHeartRate; // Heart rate
	private String mComment; // Comments
	private byte[] mGPSData;
	// private ArrayList<LatLng> mLocationList; // Location list
	//
	// private static final String DATE_FORMAT_NOW = "MMM dd, yyyy";
	// private static final String TIME_FORMAT_NOW = "HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			Globals.DATE_FORMAT_NOW);
	private static final SimpleDateFormat stf = new SimpleDateFormat(
			Globals.TIME_FORMAT_NOW);

	public EventEntry() {
		this.mEventType = -1;

		this.mStartDate = sdf.format(Calendar.getInstance().getTime());
		this.mStartTime = stf.format(Calendar.getInstance().getTime());

		this.mDistance = 0.0;
		this.mAvgPace = 0.0;
		this.mAvgSpeed = 0.0;
		this.mCalorie = 0;
		this.mClimb = 0.0;
		this.mHeartRate = 0;
		this.mComment = " ";
		this.mGPSData = new byte[0];
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

			mStartDate = obj.getString("date");
			mStartTime = obj.getString("time");

			mDistance = obj.getDouble("distance");
			mAvgSpeed = obj.getDouble("avgSpeed");
			mCalorie = obj.getInt("calorie");
			mClimb = obj.getDouble("climb");
			mHeartRate = obj.getInt("heartrate");
			mComment = obj.getString("comment");
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

			obj.put("dateTime", mStartDate);
			obj.put("time", mStartTime);

			obj.put("distance", mDistance);
			obj.put("avgSpeed", mAvgSpeed);
			obj.put("calorie", mCalorie);
			obj.put("climb", mClimb);
			obj.put("heartrate", mHeartRate);
			obj.put("comment", mComment);
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
	 * Set input type
	 * 
	 * @param inputType
	 */
	public void setInputType(int inputType) {
		mEventType = inputType;
	}

	public int getInputType() {
		return mEventType;
	}

	/**
	 * Set date and time
	 * 
	 * @param dateTime
	 */
	public void setDate(String date) {
		mStartDate = date;
	}

	public String getDate() {
		return mStartDate;
	}

	public void setTime(String Time) {
		mStartTime = Time;
	}

	public String getTime() {
		return mStartTime;
	}

	/**
	 * Set distance
	 * 
	 * @param distance
	 */
	public void setDistance(double distance) {
		mDistance = distance;
	}

	/**
	 * Get distance in km/mile
	 * 
	 * @return
	 */
	public double getDistance() {
		return mDistance;
	}

	/**
	 * Set average pace
	 * 
	 * @param avgPace
	 */
	public void setAvgPace(double avgPace) {
		mAvgPace = avgPace;
	}

	public double getAvgPace() {
		return mAvgPace;
	}

	/**
	 * Set average speed
	 * 
	 * @param avgSpeed
	 */
	public void setAvgSpeed(double avgSpeed) {
		mAvgSpeed = avgSpeed;
	}

	public double getAvgSpeed() {
		return mAvgSpeed;
	}

	/**
	 * Set Calorie
	 * 
	 * @param calorie
	 */
	public void setCalorie(int calorie) {
		mCalorie = calorie;
	}

	public int getCalorie() {
		return mCalorie;
	}

	/**
	 * Set climb
	 * 
	 * @param climb
	 */
	public void setClimb(double climb) {
		mClimb = climb;
	}

	public double getClimb() {
		return mClimb;
	}

	/**
	 * Set heart rate
	 * 
	 * @param heartRate
	 */
	public void setHeartRate(int heartRate) {
		mHeartRate = heartRate;
	}

	public int getHeartRate() {
		return mHeartRate;
	}

	/**
	 * Set comment
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		mComment = comment;
	}

	public String getComment() {
		return mComment;
	}

	public void setGPSData(byte[] locationArray) {
		mGPSData = locationArray;
	}

	public byte[] getGPSData() {
		return mGPSData;
	}

}
