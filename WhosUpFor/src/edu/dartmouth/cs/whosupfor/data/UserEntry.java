package edu.dartmouth.cs.whosupfor.data;


/**
 * ADT for storing user entry data
 * using email address as the unique user id
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
