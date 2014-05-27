package edu.dartmouth.cs.whosupfor.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CalendarContract.Attendees;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class UserEntryDbHelper extends SQLiteOpenHelper {

	private SQLiteDatabase mDatabase;

	private String[] allColumns = { Globals.KEY_USER_ROWID,
			Globals.KEY_USER_FIRST_NAME, Globals.KEY_USER_LAST_NAME,
			Globals.KEY_USER_EMAIL, Globals.KEY_USER_BIO,
			Globals.KEY_USER_GENDER, Globals.KEY_USER_CLASS_YEAR,
			Globals.KEY_USER_MAJOR, Globals.KEY_USER_PASSWORD,
			Globals.KEY_USER_PROFILE_PHOTO };

	// SQL query to create the table for the first time
	// Data types are defined below
	public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
			+ Globals.TABLE_NAME_USER_ENTRIES
			+ " ("
			+ Globals.KEY_USER_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ Globals.KEY_USER_FIRST_NAME
			+ " TEXT, "
			+ Globals.KEY_USER_LAST_NAME
			+ " TEXT, "
			+ Globals.KEY_USER_EMAIL
			+ " TEXT, "
			+ Globals.KEY_USER_BIO
			+ " TEXT, "
			+ Globals.KEY_USER_GENDER
			+ " INTEGER, "
			+ Globals.KEY_USER_CLASS_YEAR
			+ " INTEGER, "
			+ Globals.KEY_USER_MAJOR
			+ " TEXT, "
			+ Globals.KEY_USER_PASSWORD
			+ " TEXT, " + Globals.KEY_USER_PROFILE_PHOTO + " BLOB " + ");";

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public UserEntryDbHelper(Context context) {
		// DATABASE_NAME is defined as a string constant
		// DATABASE_VERSION is the version of mDatabase, which is defined as an
		// integer constant
		super(context, Globals.DATABASE_NAME_USER, null,
				Globals.DATABASE_VERSION);
	}

	/**
	 * Create table schema if not exists
	 */
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_ENTRIES);
	}

	/**
	 * Insert a item given each column value
	 * 
	 * @param entry
	 * @return
	 */
	public long insertEntry(UserEntry entry) {
		// Get a mDatabase object
		mDatabase = getWritableDatabase();
		ContentValues values = new ContentValues();

		// values.put(KEY_ROWID, entry.getID());
		values.put(Globals.KEY_USER_FIRST_NAME, entry.getFirstName());
		values.put(Globals.KEY_USER_LAST_NAME, entry.getLastName());
		values.put(Globals.KEY_USER_EMAIL, entry.getEmail());
		values.put(Globals.KEY_USER_BIO, entry.getBio());
		values.put(Globals.KEY_USER_GENDER, entry.getGender());
		values.put(Globals.KEY_USER_CLASS_YEAR, entry.getClassYear());
		values.put(Globals.KEY_USER_MAJOR, entry.getMajor());
		values.put(Globals.KEY_USER_PASSWORD, entry.getPassword());
		values.put(Globals.KEY_USER_PROFILE_PHOTO, entry.getProfilePhoto());

		long insertId = mDatabase.insert(Globals.TABLE_NAME_USER_ENTRIES, null,
				values);

		mDatabase.close();

		return insertId;
	}

	/**
	 * Remove an entry by giving its index
	 * 
	 * @param rowIndex
	 */
	public void removeEntry(long rowIndex) {

		mDatabase = getWritableDatabase();
		mDatabase.delete(Globals.TABLE_NAME_USER_ENTRIES,
				Globals.KEY_USER_ROWID + " = " + rowIndex, null);
		mDatabase.close();
	}

	/**
	 * Remove all entries
	 */
	public void removeAllEntries() {
		mDatabase = getWritableDatabase();
		mDatabase.delete(Globals.TABLE_NAME_USER_ENTRIES, null, null);
		mDatabase.close();
	}

	/**
	 * Query a specific entry by its index.
	 * 
	 * @param rowId
	 * @return
	 */
	public UserEntry fetchEntryByIndex(long rowId) {
		UserEntry mUserEntry = new UserEntry();
		mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_USER_ENTRIES,
				allColumns, Globals.KEY_USER_ROWID + "=" + rowId, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			// convert the cursor to an UserEntry object
			mUserEntry = cursorToUserEntry(cursor);
		}

		cursor.close();
		mDatabase.close();

		return mUserEntry;
	}

	/**
	 * Query entries by attendees(their email address)
	 * 
	 * @param attendees
	 * @return
	 */
	public ArrayList<UserEntry> fetchEntriesByAttendees(
			ArrayList<String> attendees) {
		ArrayList<UserEntry> mUserEntries = new ArrayList<UserEntry>();
		UserEntry mUserEntry = new UserEntry();
		mDatabase = getReadableDatabase();
		Cursor cursor = null;

		for (String attendee : attendees) {
			cursor = mDatabase.query(Globals.TABLE_NAME_USER_ENTRIES,
					allColumns, Globals.KEY_USER_EMAIL + " like ?",
					new String[] { attendee + "%" }, null, null, null, null);
			if (cursor.moveToFirst()) {
				// convert the cursor to an UserEntry object
				mUserEntry = cursorToUserEntry(cursor);
				mUserEntries.add(mUserEntry);
			}

		}

		mDatabase.close();
		return mUserEntries;
	}

	/**
	 * Query entry by eventEntry
	 * 
	 * @param attendees
	 * @return
	 */
	public UserEntry fetchEntriesByEventEntry(EventEntry eventEntry) {
		UserEntry mUserEntry = new UserEntry();
		String mEmail = eventEntry.getEmail(); 
		mDatabase = getReadableDatabase();
		Cursor cursor = null;

		cursor = mDatabase.query(Globals.TABLE_NAME_USER_ENTRIES, allColumns,
				Globals.KEY_USER_EMAIL + " like ?", new String[] { mEmail
						+ "%" }, null, null, null, null);
		if (cursor.moveToFirst()) {
			// convert the cursor to an UserEntry object
			mUserEntry = cursorToUserEntry(cursor);
		}

		mDatabase.close();
		return mUserEntry;
	}

	/**
	 * Query entry by email
	 * 
	 * @param attendees
	 * @return
	 */
	public UserEntry fetchEntriesByEmail(String email) {
		UserEntry mUserEntry = new UserEntry();
		mDatabase = getReadableDatabase();
		Cursor cursor = null;

		cursor = mDatabase.query(Globals.TABLE_NAME_USER_ENTRIES, allColumns,
				Globals.KEY_USER_EMAIL + " like ?", new String[] { email
						+ "%" }, null, null, null, null);
		if (cursor.moveToFirst()) {
			// convert the cursor to an UserEntry object
			mUserEntry = cursorToUserEntry(cursor);
		}

		mDatabase.close();
		return mUserEntry;
	}
	
	/**
	 * Query the entire table, return all rows
	 * 
	 * @return
	 */
	public ArrayList<UserEntry> fetchEntries() {
		ArrayList<UserEntry> exerciseEntries = new ArrayList<UserEntry>();

		mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_USER_ENTRIES,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			// Helper method to get entry information from the mDatabase
			UserEntry exerciseEntry = cursorToUserEntry(cursor);
			// Log.d(Globals.TAG_DATABASE, "get exercise entry = "
			// + cursorToUserEntry(cursor).toString());
			exerciseEntries.add(exerciseEntry);
			cursor.moveToNext();
		}
		cursor.close();
		mDatabase.close();
		return exerciseEntries;

	}

	private UserEntry cursorToUserEntry(Cursor cursor) {

		UserEntry mUserEntry = new UserEntry();

		// Set up content
		mUserEntry.setID(cursor.getLong(cursor
				.getColumnIndex(Globals.KEY_USER_ROWID)));
		mUserEntry.setFirstName(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_FIRST_NAME)));
		mUserEntry.setLastName(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_LAST_NAME)));
		mUserEntry.setEmail(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_EMAIL)));
		mUserEntry.setBio(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_BIO)));
		mUserEntry.setGender(cursor.getInt(cursor
				.getColumnIndex(Globals.KEY_USER_GENDER)));
		mUserEntry.setClassYear(cursor.getInt(cursor
				.getColumnIndex(Globals.KEY_USER_CLASS_YEAR)));
		mUserEntry.setMajor(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_MAJOR)));
		mUserEntry.setPassword(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_USER_PASSWORD)));
		mUserEntry.setProfilePhoto(cursor.getBlob(cursor
				.getColumnIndex(Globals.KEY_USER_PROFILE_PHOTO)));

		return mUserEntry;
	}

	/**
	 * Upgrade
	 */
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// Log.w(ExerciseEntryDbHelper.class.getName(),
		// "Upgrading mDatabase from version " + oldVersion + " to "
		// + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS ");
		onCreate(database);
	}
}
