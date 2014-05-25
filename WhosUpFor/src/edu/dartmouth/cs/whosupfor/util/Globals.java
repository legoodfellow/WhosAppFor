package edu.dartmouth.cs.whosupfor.util;

import java.util.ArrayList;
import java.util.Calendar;

public class Globals {

	// LOG
	public static final String TAG_MAIN_ACTIVITY = "MainActivity";
	public static final String TAG_CREATE_NEW_EVENT_ACTIVITY = "CreateNewEventActivity";
	public static final String TAG_NEWS_FEED_FRAGMENT = "NewsFeedFragment";

	// MainActivity
	public static final String TAB_KEY_INDEX = "tab_key";
	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_TAKE_FROM_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;

	// Key
	// public static final String KEY_EVENT_TYPE = "eventType";

	// CreateNewEventActivity
	public static final int PICK_DATE = 1;
	public static final int PICK_TIME = 2;
	
	// SelectNewEventTypeActivity
	public static final int EVENT_TYPE_FOOD = 0;
	public static final int EVENT_TYPE_SPORTS = 1;
	public static final int EVENT_TYPE_STUDY = 2;
	public static final int EVENT_TYPE_MOVIE = 3;
	public static final int EVENT_TYPE_PARTY = 4;
	

	// EventEntry
	public static final String DATE_FORMAT = "HH:mm:ss MMM d yyyy";
	public static final String DATE_FORMAT_NOW = "MMM dd, yyyy";
	public static final String TIME_FORMAT_NOW = "HH:mm:ss";

	// EventEntryDbHelper
	public static final String DATABASE_NAME = "whosupfor.db";
	public static final String DATABASE_NAME_2 = "whosupfor2.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME_EVENT_ENTRIES = "eventEntryTable";
	public static final String KEY_EVENT_ROWID = "event_id";
	public static final String KEY_EVENT_EMAIL = "event_email";
	public static final String KEY_EVENT_TYPE = "event_type";
	public static final String KEY_EVENT_TITLE = "event_title";
	public static final String KEY_EVENT_LOCATION = "event_location";
	public static final String KEY_EVENT_TIME_STAMP = "event_time_stamp";
	public static final String KEY_EVENT_START_DATE_TIME = "event_start_date_time";
	public static final String KEY_EVENT_END_DATE_TIME = "event_end_date_time";
	public static final String KEY_EVENT_DETAIL = "event_detail";
	public static final String KEY_EVENT_ATTENDEES = "event_attendees";
	public static final String KEY_EVENT_CIRCLE = "event_circle";

	// UserEntryDbHelper
	public static final String TABLE_NAME_USER_ENTRIES = "userEntryTable";
	public static final String KEY_USER_ROWID = "user_id";
	public static final String KEY_USER_FIRST_NAME = "user_first_name";
	public static final String KEY_USER_LAST_NAME = "user_last_name";
	public static final String KEY_USER_EMAIL = "user_email";
	public static final String KEY_USER_BIO = "user_bio";
	public static final String KEY_USER_GENDER = "user_gender";
	public static final String KEY_USER_CLASS_YEAR = "user_class_year";
	public static final String KEY_USER_MAJOR = "user_major";
	public static final String KEY_USER_PASSWORD = "user_password";
	public static final String KEY_USER_PROFILE_PHOTO = "user_profile_photo";
	

	// EditProfileActivity
	public static final String IMG_INSTANCE_STATE_KEY = "saved_image";
	public static final String URI_INSTANCE_STATE_KEY = "saved_uri";
	public static final String IMAGE_UNSPECIFIED = "image/*";

	// Toast
	public static final String TOAST_SAVE_MESSAGE = "Saved";
	public static final String TOAST_CANCEL_MESSAGE = "Canceled";

	// Boolean
	public static boolean HAS_PROFILE_IMAGE = false;

	// SettingsActivity
	public static final String[] SETTINGS_ARRAY = { "Notification", "Privacy",
			"Like us on Facebook", "Like us on Twitter", "About", "Donations" };
	
	// GCM
	public static final String SERVER_ADDR = "http://10.31.239.242";
	public static final String SENDER_ID = "424192184423";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

}
