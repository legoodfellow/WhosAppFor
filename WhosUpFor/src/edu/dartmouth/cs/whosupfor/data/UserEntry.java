package edu.dartmouth.cs.whosupfor.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import edu.dartmouth.cs.whosupfor.util.Globals;

/**
 * ADT for storing user entry data using email address as the unique user id
 * 
 * 
 */
public class UserEntry {

	private Long mDbId; // Database mDbId of the entry
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mBio;
	private int mGender;
	private int mClassYear;
	private String mMajor;
	private byte[] mProfilePhoto;
	private String mPassword;

	public UserEntry() {

		this.mFirstName = " ";
		this.mLastName = " ";
		this.mEmail = " ";
		this.mBio = " ";
		this.mGender = -1;
		this.mClassYear = 1769;
		this.mMajor = " ";
		this.mPassword = " ";
		this.mProfilePhoto = new byte[0];
	}

	/**
	 * Get the json file from web and convert it back to UserEntry
	 * 
	 * @param obj
	 * @return
	 */
	public void fromJSONObject(JSONObject obj) {
		try {
			mDbId = obj.getLong(Globals.KEY_USER_ROWID);
			mFirstName = obj.getString(Globals.KEY_USER_FIRST_NAME);
			mLastName = obj.getString(Globals.KEY_USER_LAST_NAME);
			mEmail = obj.getString(Globals.KEY_USER_EMAIL);
			mBio = obj.getString(Globals.KEY_USER_BIO);
			mGender = obj.getInt(Globals.KEY_USER_GENDER);
			mClassYear = obj.getInt(Globals.KEY_USER_CLASS_YEAR);
			mMajor = obj.getString(Globals.KEY_USER_MAJOR);
			mPassword = obj.getString(Globals.KEY_USER_PASSWORD);
			String decodedImage = obj.getString(Globals.KEY_USER_PROFILE_PHOTO);
			mProfilePhoto = Base64.decode(decodedImage, Base64.DEFAULT);

		} catch (JSONException e) {
			
		}
		
	}

	/**
	 * Convert UserEntry to JSON file upload it to web
	 * 
	 * @return
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();

		try {
			obj.put(Globals.KEY_USER_ROWID, mDbId);
			obj.put(Globals.KEY_USER_FIRST_NAME, mFirstName);
			obj.put(Globals.KEY_USER_LAST_NAME, mLastName);
			obj.put(Globals.KEY_USER_EMAIL, mEmail);
			obj.put(Globals.KEY_USER_BIO, mBio);
			obj.put(Globals.KEY_USER_GENDER, mGender);
			obj.put(Globals.KEY_USER_CLASS_YEAR, mClassYear);
			obj.put(Globals.KEY_USER_MAJOR, mMajor);
			obj.put(Globals.KEY_USER_PASSWORD, mPassword);
			
			String encodedImage = Base64.encodeToString(mProfilePhoto, Base64.DEFAULT);
			obj.put(Globals.KEY_USER_PROFILE_PHOTO, encodedImage);
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
	 * Set first and last name
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getLastName() {
		return mLastName;
	}

	/**
	 * Set email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		mEmail = email;
	}

	public String getEmail() {
		return mEmail;
	}

	/**
	 * Set bio
	 * 
	 * @param bio
	 */
	public void setBio(String bio) {
		mBio = bio;
	}

	public String getBio() {
		return mBio;
	}

	/**
	 * Set gender
	 * 
	 * @param gender
	 */
	public void setGender(int gender) {
		mGender = gender;
	}

	public int getGender() {
		return mGender;
	}

	/**
	 * Set class year
	 * 
	 * @param year
	 */
	public void setClassYear(int year) {
		mClassYear = year;
	}

	public int getClassYear() {
		return mClassYear;
	}

	/**
	 * Set major
	 * 
	 * @param major
	 */
	public void setMajor(String major) {
		mMajor = major;
	}

	public String getMajor() {
		return mMajor;
	}

	/**
	 * Set password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		mPassword = password;
	}

	public String getPassword() {
		return mPassword;
	}

	/**
	 * Set profile photo
	 * 
	 * @param profilePhoto
	 */
	public void setProfilePhoto(byte[] profilePhoto) {
		mProfilePhoto = profilePhoto;
	}

	public byte[] getProfilePhoto() {
		return mProfilePhoto;
	}

}
