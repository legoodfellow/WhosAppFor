package edu.dartmouth.cs.whosupfor.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class EventEntryDbHelper  {

	private SQLiteDatabase mDatabase;

	private String[] allColumns = { Globals.KEY_EVENT_ROWID,
			Globals.KEY_EVENT_EMAIL, Globals.KEY_EVENT_TYPE,
			Globals.KEY_EVENT_TITLE, Globals.KEY_EVENT_LOCATION,
			Globals.KEY_EVENT_TIME_STAMP, Globals.KEY_EVENT_START_DATE,
			Globals.KEY_EVENT_START_TIME, Globals.KEY_EVENT_END_DATE,
			Globals.KEY_EVENT_END_TIME, Globals.KEY_EVENT_DETAIL,
			Globals.KEY_EVENT_ATTENDEES, Globals.KEY_EVENT_CIRCLE };

	// SQL query to create the table for the first time
	// Data types are defined below
	public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
			+ Globals.TABLE_NAME_EVENT_ENTRIES
			+ " ("
			+ Globals.KEY_EVENT_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
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
			+ Globals.KEY_EVENT_START_DATE
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_START_TIME
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_END_DATE
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_END_TIME
			+ " DATETIME NOT NULL, "
			+ Globals.KEY_EVENT_DETAIL
			+ " TEXT, "
			+ Globals.KEY_EVENT_ATTENDEES
			+ " BLOB "
			+ Globals.KEY_EVENT_CIRCLE
			+ " INTEGER NOT NULL, " + ");";

	/**
//	 * Constructor
//	 * 
//	 * @param context
//	 */
//	public EventEntryDbHelper(Context context) {
//		// DATABASE_NAME is defined as a string constant
//		// DATABASE_VERSION is the version of mDatabase, which is defined as an
//		// integer constant
//		super(context, Globals.DATABASE_NAME, null, Globals.DATABASE_VERSION);
//	}

	/**
	 * Create table schema if not exists
	 */
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_ENTRIES);
	}

	/**
//	 * Insert a item given each column value
//	 * 
//	 * @param entry
//	 * @return
//	 */
//	public long insertEntry(EventEntry entry) {
//		// Get a mDatabase object
//		mDatabase = getWritableDatabase();
//		ContentValues values = new ContentValues();
//
//		// values.put(KEY_ROWID, entry.getID());
//		values.put(Globals.KEY_EVENT_EMAIL, entry.getEmail());
//		values.put(Globals.KEY_EVENT_TYPE, entry.getEventType());
//		values.put(Globals.KEY_EVENT_TITLE, entry.getEventTitle());
//		values.put(Globals.KEY_EVENT_LOCATION, entry.getLocation());
//		values.put(Globals.KEY_EVENT_TIME_STAMP, entry.getTimeStamp());
//		values.put(Globals.KEY_EVENT_START_DATE, entry.getStartDate());
//		values.put(Globals.KEY_EVENT_START_TIME, entry.getStartTime());
//		values.put(Globals.KEY_EVENT_END_DATE, entry.getEndDate());
//		values.put(Globals.KEY_EVENT_END_TIME, entry.getEndTime());
//		values.put(Globals.KEY_EVENT_DETAIL, entry.getDetail());
//
//		values.put(Globals.KEY_COMMENT, entry.getComment());
//		values.put(Globals.KEY_GPS_DATA, entry.getGPSData());
//
//		long insertId = mDatabase.insert(Globals.TABLE_NAME_ENTRIES, null,
//				values);
//
//		mDatabase.close();
//		Log.d(Globals.TAG_DATABASE,
//				"insertEntry(ExerciseEntry entry) saved new entry to mDatabase: "
//						+ "KEY_INPUT_TYPE: " + entry.getInputType()
//						+ ", KEY_ACTIVITY_TYPE: " + entry.getActivityType()
//						+ ", KEY_DATE: " + entry.getDate() + ", KEY_TIME: "
//						+ entry.getTime() + ", KEY_DURATION: "
//						+ entry.getDuration() + ", KEY_DISTANCE: "
//						+ entry.getDistance() + ", KEY_CALORIES: "
//						+ entry.getCalorie() + ", KEY_HEARTRATE: "
//						+ entry.getHeartRate() + ", KEY_COMMENT: "
//						+ entry.getComment());
//		return insertId;
//	}
//
//	/**
//	 * Remove an entry by giving its index
//	 * 
//	 * @param rowIndex
//	 */
//	public void removeEntry(long rowIndex) {
//		Log.d(Globals.TAG_DATABASE, "removeEntry() = " + rowIndex);
//		mDatabase = getWritableDatabase();
//		mDatabase.delete(Globals.TABLE_NAME_ENTRIES, Globals.KEY_ROWID + " = "
//				+ rowIndex, null);
//		mDatabase.close();
//	}
//
//	/**
//	 * Remove all entries
//	 */
//	public void removeAllEntries() {
//		Log.d(Globals.TAG_DATABASE, "removeAllEntries()");
//		mDatabase = getWritableDatabase();
//		mDatabase.delete(Globals.TABLE_NAME_ENTRIES, null, null);
//		mDatabase.close();
//	}
//
//	/**
//	 * Query a specific entry by its index.
//	 * 
//	 * @param rowId
//	 * @return
//	 */
//	public ExerciseEntry fetchEntryByIndex(long rowId) {
//		ExerciseEntry exerciseEntry = new ExerciseEntry();
//		mDatabase = getReadableDatabase();
//		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_ENTRIES, allColumns,
//				Globals.KEY_ROWID + "=" + rowId, null, null, null, null);
//		if (cursor.moveToFirst()) {
//			// convert the cursor to an ExerciseEntry object
//			exerciseEntry = cursorToExerciseEntry(cursor);
//		}
//
//		cursor.close();
//		mDatabase.close();
//
//		return exerciseEntry;
//	}
//
//	/**
//	 * Query the entire table, return all rows
//	 * 
//	 * @return
//	 */
//	public ArrayList<ExerciseEntry> fetchEntries() {
//		ArrayList<ExerciseEntry> exerciseEntries = new ArrayList<ExerciseEntry>();
//
//		mDatabase = getReadableDatabase();
//		Cursor cursor = mDatabase.query(Globals.TABLE_NAME_ENTRIES, allColumns,
//				null, null, null, null, null);
//		cursor.moveToFirst();
//
//		while (!cursor.isAfterLast()) {
//			// Helper method to get entry information from the mDatabase
//			ExerciseEntry exerciseEntry = cursorToExerciseEntry(cursor);
//			Log.d(Globals.TAG_DATABASE, "get exercise entry = "
//					+ cursorToExerciseEntry(cursor).toString());
//			exerciseEntries.add(exerciseEntry);
//			cursor.moveToNext();
//		}
//		cursor.close();
//		mDatabase.close();
//		return exerciseEntries;
//
//	}
//
//	private ExerciseEntry cursorToExerciseEntry(Cursor cursor) {
//
//		ExerciseEntry exerciseEntry = new ExerciseEntry();
//
//		// Set up content
//		exerciseEntry.setID(cursor.getLong(cursor
//				.getColumnIndex(Globals.KEY_ROWID)));
//		exerciseEntry.setInputType(cursor.getInt(cursor
//				.getColumnIndex(Globals.KEY_INPUT_TYPE)));
//		exerciseEntry.setActivityType(cursor.getInt(cursor
//				.getColumnIndex(Globals.KEY_ACTIVITY_TYPE)));
//		exerciseEntry.setDate(cursor.getString(cursor
//				.getColumnIndex(Globals.KEY_DATE)));
//		exerciseEntry.setTime(cursor.getString(cursor
//				.getColumnIndex(Globals.KEY_TIME)));
//		exerciseEntry.setDuration(cursor.getDouble(cursor
//				.getColumnIndex(Globals.KEY_DURATION)));
//		exerciseEntry.setDistance(cursor.getDouble(cursor
//				.getColumnIndex(Globals.KEY_DISTANCE)));
//		exerciseEntry.setAvgPace(cursor.getDouble(cursor
//				.getColumnIndex(Globals.KEY_AVG_PACE)));
//		exerciseEntry.setAvgSpeed(cursor.getDouble(cursor
//				.getColumnIndex(Globals.KEY_AVG_SPEED)));
//		exerciseEntry.setCalorie(cursor.getInt(cursor
//				.getColumnIndex(Globals.KEY_CALORIES)));
//		exerciseEntry.setClimb(cursor.getDouble(cursor
//				.getColumnIndex(Globals.KEY_CLIMB)));
//		exerciseEntry.setHeartRate(cursor.getInt(cursor
//				.getColumnIndex(Globals.KEY_HEARTRATE)));
//		exerciseEntry.setComment(cursor.getString(cursor
//				.getColumnIndex(Globals.KEY_COMMENT)));
//		exerciseEntry.setGPSData(cursor.getBlob(cursor
//				.getColumnIndex(Globals.KEY_GPS_DATA)));
//
//		return exerciseEntry;
//	}
//
//	/**
//	 * Upgrade
//	 */
//	public void onUpgrade(SQLiteDatabase database, int oldVersion,
//			int newVersion) {
//		Log.w(ExerciseEntryDbHelper.class.getName(),
//				"Upgrading mDatabase from version " + oldVersion + " to "
//						+ newVersion + ", which will destroy all old data");
//		database.execSQL("DROP TABLE IF EXISTS ");
//		onCreate(database);
//	}
}
