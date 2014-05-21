package edu.dartmouth.cs.whosupfor.data;

import android.database.sqlite.SQLiteOpenHelper;

public class EventEntryDbHelper {

	// private SQLiteDatabase database;
	//
	// private String[] allColumns = { Globals.KEY_ROWID,
	// Globals.KEY_INPUT_TYPE,
	// Globals.KEY_ACTIVITY_TYPE, Globals.KEY_DATE, Globals.KEY_TIME,
	// Globals.KEY_DURATION, Globals.KEY_DISTANCE, Globals.KEY_AVG_PACE,
	// Globals.KEY_AVG_SPEED, Globals.KEY_CALORIES, Globals.KEY_CLIMB,
	// Globals.KEY_HEARTRATE, Globals.KEY_COMMENT, Globals.KEY_PRIVACY,
	// Globals.KEY_GPS_DATA };
	//
	// // private static final String DATABASE_NAME = "myrun.db";
	// // private static final int DATABASE_VERSION = 1;
	// // SQL query to create the table for the first time
	// // Data types are defined below
	// public static final String CREATE_TABLE_ENTRIES =
	// "CREATE TABLE IF NOT EXISTS "
	// + Globals.TABLE_NAME_ENTRIES
	// + " ("
	// + Globals.KEY_ROWID
	// + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	// + Globals.KEY_INPUT_TYPE
	// + " INTEGER NOT NULL, "
	// + Globals.KEY_ACTIVITY_TYPE
	// + " INTEGER NOT NULL, "
	// + Globals.KEY_DATE
	// + " DATETIME NOT NULL, "
	// + Globals.KEY_TIME
	// + " DATETIME NOT NULL, "
	// + Globals.KEY_DURATION
	// + " FLOAT, "
	// + Globals.KEY_DISTANCE
	// + " FLOAT, "
	// + Globals.KEY_AVG_PACE
	// + " FLOAT, "
	// + Globals.KEY_AVG_SPEED
	// + " FLOAT,"
	// + Globals.KEY_CALORIES
	// + " INTEGER, "
	// + Globals.KEY_CLIMB
	// + " FLOAT, "
	// + Globals.KEY_HEARTRATE
	// + " INTEGER, "
	// + Globals.KEY_COMMENT
	// + " TEXT, "
	// + Globals.KEY_PRIVACY
	// + " INTEGER, "
	// + Globals.KEY_GPS_DATA
	// + " BLOB " + ");";
	//
	// /**
	// * Constructor
	// *
	// * @param context
	// */
	// public EventEntryDbHelper(Context context) {
	// // DATABASE_NAME is, of course the name of the database, which is
	// // defined as a tring constant
	// // DATABASE_VERSION is the version of database, which is defined as an
	// // integer constant
	// super(context, Globals.DATABASE_NAME, null, Globals.DATABASE_VERSION);
	// }
	//
	// /**
	// * Create table schema if not exists
	// */
	// public void onCreate(SQLiteDatabase database) {
	// database.execSQL(CREATE_TABLE_ENTRIES);
	// }
	//
	// /**
	// * Insert a item given each column value
	// *
	// * @param entry
	// * @return
	// */
	// public long insertEntry(ExerciseEntry entry) {
	// // Get a database object
	// database = getWritableDatabase();
	// ContentValues values = new ContentValues();
	//
	// // values.put(KEY_ROWID, entry.getID());
	// values.put(Globals.KEY_INPUT_TYPE, entry.getInputType());
	// values.put(Globals.KEY_ACTIVITY_TYPE, entry.getActivityType());
	// values.put(Globals.KEY_DATE, entry.getDate());
	// values.put(Globals.KEY_TIME, entry.getTime());
	// values.put(Globals.KEY_DURATION, entry.getDuration());
	// values.put(Globals.KEY_DISTANCE, entry.getDistance());
	// values.put(Globals.KEY_AVG_PACE, entry.getAvgPace());
	// values.put(Globals.KEY_AVG_SPEED, entry.getAvgSpeed());
	// values.put(Globals.KEY_CALORIES, entry.getCalorie());
	// values.put(Globals.KEY_CLIMB, entry.getClimb());
	// values.put(Globals.KEY_HEARTRATE, entry.getHeartRate());
	// values.put(Globals.KEY_COMMENT, entry.getComment());
	// values.put(Globals.KEY_GPS_DATA, entry.getGPSData());
	//
	// long insertId = database.insert(Globals.TABLE_NAME_ENTRIES, null,
	// values);
	//
	// database.close();
	// Log.d(Globals.TAG_DATABASE,
	// "insertEntry(ExerciseEntry entry) saved new entry to database: "
	// + "KEY_INPUT_TYPE: " + entry.getInputType()
	// + ", KEY_ACTIVITY_TYPE: " + entry.getActivityType()
	// + ", KEY_DATE: " + entry.getDate() + ", KEY_TIME: "
	// + entry.getTime() + ", KEY_DURATION: "
	// + entry.getDuration() + ", KEY_DISTANCE: "
	// + entry.getDistance() + ", KEY_CALORIES: "
	// + entry.getCalorie() + ", KEY_HEARTRATE: "
	// + entry.getHeartRate() + ", KEY_COMMENT: "
	// + entry.getComment());
	// return insertId;
	// }
	//
	// /**
	// * Remove an entry by giving its index
	// *
	// * @param rowIndex
	// */
	// public void removeEntry(long rowIndex) {
	// Log.d(Globals.TAG_DATABASE, "removeEntry() = " + rowIndex);
	// database = getWritableDatabase();
	// database.delete(Globals.TABLE_NAME_ENTRIES, Globals.KEY_ROWID + " = "
	// + rowIndex, null);
	// database.close();
	// }
	//
	// /**
	// * Remove all entries
	// */
	// public void removeAllEntries() {
	// Log.d(Globals.TAG_DATABASE, "removeAllEntries()");
	// database = getWritableDatabase();
	// database.delete(Globals.TABLE_NAME_ENTRIES, null, null);
	// database.close();
	// }
	//
	// /**
	// * Query a specific entry by its index.
	// *
	// * @param rowId
	// * @return
	// */
	// public ExerciseEntry fetchEntryByIndex(long rowId) {
	// ExerciseEntry exerciseEntry = new ExerciseEntry();
	// database = getReadableDatabase();
	// Cursor cursor = database.query(Globals.TABLE_NAME_ENTRIES, allColumns,
	// Globals.KEY_ROWID + "=" + rowId, null, null, null, null);
	// if (cursor.moveToFirst()) {
	// // convert the cursor to an ExerciseEntry object
	// exerciseEntry = cursorToExerciseEntry(cursor);
	// }
	//
	// cursor.close();
	// database.close();
	//
	// return exerciseEntry;
	// }
	//
	// /**
	// * Query the entire table, return all rows
	// *
	// * @return
	// */
	// public ArrayList<ExerciseEntry> fetchEntries() {
	// ArrayList<ExerciseEntry> exerciseEntries = new
	// ArrayList<ExerciseEntry>();
	//
	// database = getReadableDatabase();
	// Cursor cursor = database.query(Globals.TABLE_NAME_ENTRIES, allColumns,
	// null, null, null, null, null);
	// cursor.moveToFirst();
	//
	// while (!cursor.isAfterLast()) {
	// // Helper method to get entry information from the database
	// ExerciseEntry exerciseEntry = cursorToExerciseEntry(cursor);
	// Log.d(Globals.TAG_DATABASE, "get exercise entry = "
	// + cursorToExerciseEntry(cursor).toString());
	// exerciseEntries.add(exerciseEntry);
	// cursor.moveToNext();
	// }
	// cursor.close();
	// database.close();
	// return exerciseEntries;
	//
	// }
	//
	// private ExerciseEntry cursorToExerciseEntry(Cursor cursor) {
	//
	// ExerciseEntry exerciseEntry = new ExerciseEntry();
	//
	// // Set up content
	// exerciseEntry.setID(cursor.getLong(cursor
	// .getColumnIndex(Globals.KEY_ROWID)));
	// exerciseEntry.setInputType(cursor.getInt(cursor
	// .getColumnIndex(Globals.KEY_INPUT_TYPE)));
	// exerciseEntry.setActivityType(cursor.getInt(cursor
	// .getColumnIndex(Globals.KEY_ACTIVITY_TYPE)));
	// exerciseEntry.setDate(cursor.getString(cursor
	// .getColumnIndex(Globals.KEY_DATE)));
	// exerciseEntry.setTime(cursor.getString(cursor
	// .getColumnIndex(Globals.KEY_TIME)));
	// exerciseEntry.setDuration(cursor.getDouble(cursor
	// .getColumnIndex(Globals.KEY_DURATION)));
	// exerciseEntry.setDistance(cursor.getDouble(cursor
	// .getColumnIndex(Globals.KEY_DISTANCE)));
	// exerciseEntry.setAvgPace(cursor.getDouble(cursor
	// .getColumnIndex(Globals.KEY_AVG_PACE)));
	// exerciseEntry.setAvgSpeed(cursor.getDouble(cursor
	// .getColumnIndex(Globals.KEY_AVG_SPEED)));
	// exerciseEntry.setCalorie(cursor.getInt(cursor
	// .getColumnIndex(Globals.KEY_CALORIES)));
	// exerciseEntry.setClimb(cursor.getDouble(cursor
	// .getColumnIndex(Globals.KEY_CLIMB)));
	// exerciseEntry.setHeartRate(cursor.getInt(cursor
	// .getColumnIndex(Globals.KEY_HEARTRATE)));
	// exerciseEntry.setComment(cursor.getString(cursor
	// .getColumnIndex(Globals.KEY_COMMENT)));
	// exerciseEntry.setGPSData(cursor.getBlob(cursor
	// .getColumnIndex(Globals.KEY_GPS_DATA)));
	//
	// return exerciseEntry;
	// }
	//
	// /**
	// * Upgrade
	// */
	// public void onUpgrade(SQLiteDatabase database, int oldVersion,
	// int newVersion) {
	// Log.w(ExerciseEntryDbHelper.class.getName(),
	// "Upgrading database from version " + oldVersion + " to "
	// + newVersion + ", which will destroy all old data");
	// database.execSQL("DROP TABLE IF EXISTS ");
	// onCreate(database);
	// }
}
