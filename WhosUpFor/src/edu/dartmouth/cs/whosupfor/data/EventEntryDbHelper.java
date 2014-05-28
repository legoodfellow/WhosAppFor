package edu.dartmouth.cs.whosupfor.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings.Global;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class EventEntryDbHelper extends SQLiteOpenHelper {

	private SQLiteDatabase mDatabase;

	private String[] allColumns = { Globals.KEY_EVENT_ROWID,
			Globals.KEY_EVENT_ID, Globals.KEY_EVENT_EMAIL,
			Globals.KEY_EVENT_TYPE, Globals.KEY_EVENT_TITLE,
			Globals.KEY_EVENT_LOCATION, Globals.KEY_EVENT_TIME_STAMP,
			Globals.KEY_EVENT_START_DATE_TIME, Globals.KEY_EVENT_END_DATE_TIME,
			Globals.KEY_EVENT_DETAIL, Globals.KEY_EVENT_ATTENDEES,
			Globals.KEY_EVENT_CIRCLE };

	// SQL query to create the table for the first time
	// Data types are defined below
	public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
			+ Globals.TABLE_NAME_EVENT_ENTRIES
			+ " ("
			+ Globals.KEY_EVENT_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ Globals.KEY_EVENT_ID
			+ " TEXT, "
			+ Globals.KEY_EVENT_EMAIL
			+ " TEXT, "
			+ Globals.KEY_EVENT_TYPE
			+ " INTEGER NOT NULL, "
			+ Globals.KEY_EVENT_TITLE
			+ " TEXT, "
			+ Globals.KEY_EVENT_LOCATION
			+ " TEXT, "
			+ Globals.KEY_EVENT_TIME_STAMP
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_START_DATE_TIME
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_END_DATE_TIME
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_DETAIL
			+ " TEXT, "
			+ Globals.KEY_EVENT_ATTENDEES
			+ " BLOB, "
			+ Globals.KEY_EVENT_CIRCLE + " INTEGER NOT NULL " + ");";

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public EventEntryDbHelper(Context context) {
		// DATABASE_NAME is defined as a string constant
		// DATABASE_VERSION is the version of mDatabase, which is defined as an
		// integer constant
		super(context, Globals.DATABASE_NAME_EVENT, null,
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
	public long insertEntry(EventEntry entry) {
		// Get a mDatabase object
		mDatabase = getWritableDatabase();
		ContentValues values = new ContentValues();

//		entry.setEventId();
		
		values.put(Globals.KEY_EVENT_ID, entry.getEventId());
		values.put(Globals.KEY_EVENT_EMAIL, entry.getEmail());
		values.put(Globals.KEY_EVENT_TYPE, entry.getEventType());
		values.put(Globals.KEY_EVENT_TITLE, entry.getEventTitle());
		values.put(Globals.KEY_EVENT_LOCATION, entry.getLocation());
		values.put(Globals.KEY_EVENT_TIME_STAMP, entry.getTimeStamp());
		values.put(Globals.KEY_EVENT_START_DATE_TIME,
				entry.getStartDateTimeInMillis());
		values.put(Globals.KEY_EVENT_END_DATE_TIME,
				entry.getEndDateTimeInMillis());
		values.put(Globals.KEY_EVENT_DETAIL, entry.getDetail());
		values.put(Globals.KEY_EVENT_CIRCLE, entry.getCircle());
		values.put(Globals.KEY_EVENT_ATTENDEES, entry.getAttendeesByteArray());

		long insertId = mDatabase.insert(Globals.TABLE_NAME_EVENT_ENTRIES,
				null, values);

		mDatabase.close();

		return insertId;
	}

	/**
	 * Remove an entry by giving its index
	 * 
	 * @param rowIndex
	 */
	public void removeEntry(long rowIndex) {
		// Log.d(Globals.TAG_DATABASE, "removeEntry() = " + rowIndex);
		mDatabase = getWritableDatabase();
		mDatabase.delete(Globals.TABLE_NAME_EVENT_ENTRIES,
				Globals.KEY_EVENT_ROWID + " = " + rowIndex, null);
		mDatabase.close();
	}

	/**
	 * Remove all entries
	 */
	public void removeAllEntries() {
		// Log.d(Globals.TAG_DATABASE, "removeAllEntries()");
		mDatabase = getWritableDatabase();
		mDatabase.delete(Globals.TABLE_NAME_EVENT_ENTRIES, null, null);
		mDatabase.close();
	}

	/**
	 * Query a specific entry by its index.
	 * 
	 * @param rowId
	 * @return
	 */
	public EventEntry fetchEntryByIndex(long rowId) {
		EventEntry eventEntry = new EventEntry();
		mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_EVENT_ENTRIES,
				allColumns, Globals.KEY_EVENT_ROWID + "=" + rowId, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			// convert the cursor to an ExerciseEntry object
			eventEntry = cursorToEventEntry(cursor);
		}

		cursor.close();
		mDatabase.close();

		return eventEntry;
	}

	/**
	 * Query entries by filter
	 * 
	 * @param attendees
	 * @return
	 */
	public ArrayList<EventEntry> fetchEntriesByEventType(int idx) {
		ArrayList<EventEntry> mEventEntries = new ArrayList<EventEntry>();
		EventEntry mEventEntry = new EventEntry();
		mDatabase = getReadableDatabase();

		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_EVENT_ENTRIES,
				allColumns, Globals.KEY_EVENT_TYPE + " like ?",
				new String[] { idx + "%" }, null, null, null, null);
		if (cursor.moveToFirst()) {
			// convert the cursor to an UserEntry object
			mEventEntry = cursorToEventEntry(cursor);
			mEventEntries.add(mEventEntry);
		}

		mDatabase.close();
		return mEventEntries;
	}

	/**
	 * Query my post entries by email
	 * 
	 * @param attendees
	 * @return
	 */
	public ArrayList<EventEntry> fetchEntriesByEmail(String email) {
		ArrayList<EventEntry> mEventEntries = new ArrayList<EventEntry>();
		ArrayList<EventEntry> result = new ArrayList<EventEntry>();

		mEventEntries = fetchEntries();
		for (EventEntry eventEntry : mEventEntries) {
			if (eventEntry.getEmail().equals(email)) {
				result.add(eventEntry);
			}
		}

		return result;
	}

	/**
	 * Query going entries by email
	 * 
	 * @param attendees
	 * @return
	 */
	public ArrayList<EventEntry> fetchEntriesGoingByEmail(String email) {
		ArrayList<EventEntry> mEventEntries = new ArrayList<EventEntry>();
		ArrayList<EventEntry> result = new ArrayList<EventEntry>();

		mEventEntries = fetchEntries();
		for (EventEntry eventEntry : mEventEntries) {
			if (eventEntry.getAttendees().contains(email)) {
				result.add(eventEntry);
			}
		}

		return result;
	}

	/**
	 * Query the entire table, return all rows
	 * 
	 * @return
	 */
	public ArrayList<EventEntry> fetchEntries() {
		ArrayList<EventEntry> eventEntries = new ArrayList<EventEntry>();

		mDatabase = getReadableDatabase();
		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_EVENT_ENTRIES,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			// Helper method to get entry information from the mDatabase
			EventEntry mEventEntry = cursorToEventEntry(cursor);
			// Log.d(Globals.TAG_DATABASE, "get exercise entry = "
			// + cursorToExerciseEntry(cursor).toString());
			eventEntries.add(mEventEntry);
			cursor.moveToNext();
		}
		cursor.close();
		mDatabase.close();
		return eventEntries;

	}

	/**
	 * Helper method to get eventEntry data
	 * 
	 * @param cursor
	 * @return
	 */
	private EventEntry cursorToEventEntry(Cursor cursor) {

		EventEntry mEventEntry = new EventEntry();

		// Set up content
		mEventEntry.setID(cursor.getLong(cursor
				.getColumnIndex(Globals.KEY_EVENT_ROWID)));
		mEventEntry.setEmail(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_EVENT_EMAIL)));
		mEventEntry.setEventType(cursor.getInt(cursor
				.getColumnIndex(Globals.KEY_EVENT_TYPE)));
		mEventEntry.setEventTitle(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_EVENT_TITLE)));
		mEventEntry.setLocation(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_EVENT_LOCATION)));
		mEventEntry.setTimeStamp(cursor.getLong(cursor
				.getColumnIndex(Globals.KEY_EVENT_TIME_STAMP)));
		mEventEntry.setStartDateTime(cursor.getLong(cursor
				.getColumnIndex(Globals.KEY_EVENT_START_DATE_TIME)));
		mEventEntry.setEndDateTime(cursor.getLong(cursor
				.getColumnIndex(Globals.KEY_EVENT_END_DATE_TIME)));
		mEventEntry.setDetail(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_EVENT_DETAIL)));
		mEventEntry.setCircle(cursor.getInt(cursor
				.getColumnIndex(Globals.KEY_EVENT_CIRCLE)));

		mEventEntry.setAttendees(cursor.getBlob(cursor
				.getColumnIndex(Globals.KEY_EVENT_ATTENDEES)));
		mEventEntry.setEventId(cursor.getString(cursor
				.getColumnIndex(Globals.KEY_EVENT_ID)));

		return mEventEntry;
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
