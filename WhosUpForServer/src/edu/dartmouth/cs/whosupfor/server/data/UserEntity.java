package edu.dartmouth.cs.whosupfor.server.data;

import java.util.ArrayList;
import java.util.Calendar;

public class UserEntity {
	public static String ENTITY_KIND_PARENT = "UserParent";
	public static String ENTITY_PARENT_KEY = ENTITY_KIND_PARENT;
	public static String ENTITY_KIND_USER = "User";

	public static String FIELD_NAME_ID = "DbId";
	public static String FIELD_NAME_EMAIL = "Email";
	public static String FIELD_NAME_FIRST_NAME = "FirstName";
	public static String FIELD_NAME_LAST_NAME = "LastName";
	public static String FIELD_NAME_BIO = "Bio";
	public static String FIELD_NAME_GENDER = "Gender";
	public static String FIELD_NAME_CLASS_YEAR = "ClassYear";
	public static String FIELD_NAME_MAJOR = "Major";
	public static String FIELD_NAME_PROFILE_PHOTO = "ProfilePhoto";
	public static String FIELD_NAME_PASSWORD = "Password";
	
	private long mDbId; // Database mDbId of the entry
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mBio;
	private int mGender;
	private int mClassYear;
	private String mMajor;
	private byte[] mProfilePhoto;
	private String mPassword;
	
	public UserEntity() {
		mFirstName = null;
		mLastName = null;
		mEmail = null;
		mBio = null;
		mMajor = null;
		mPassword = null;
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

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		this.mFirstName = firstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		this.mLastName = lastName;
	}

	public String getBio() {
		return mBio;
	}

	public void setBio(String bio) {
		this.mBio = bio;
	}

	public int getGender() {
		return mGender;
	}

	public void setGender(int gender) {
		this.mGender = gender;
	}

	public int getClassYear() {
		return mClassYear;
	}

	public void setClassYear(int classYear) {
		this.mClassYear = classYear;
	}

	public String getMajor() {
		return mMajor;
	}

	public void setMajor(String major) {
		this.mMajor = major;
	}

	public byte[] getProfilePhoto() {
		return mProfilePhoto;
	}

	public void setProfilePhoto(byte[] profilePhoto) {
		this.mProfilePhoto = profilePhoto;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	
}
