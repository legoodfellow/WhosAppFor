package edu.dartmouth.cs.whosupfor;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.data.EventEntryDbHelper;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class CreateNewEventActivity extends Activity {

	private EventEntryDbHelper mEventEntryDbHelper;
	private EventEntry mEventEntry;
	private String mValue;
	private int mIntValue;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_event);

		// Get mHelperContext
		mContext = getApplicationContext();

		// mEventEntryDbHelper = new EventEntryDbHelper(mContext);
		mEventEntry = new EventEntry();

		// Get the shared preference
		String mKey = getString(R.string.preference_name_edit_profile_activity);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);
		// Load the email and add it to event entry
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_email);
		mValue = mPrefs.getString(mKey, "");
		mEventEntry.setEmail(mValue);
		
		// Get event type
		Bundle extra = getIntent().getExtras();
		mEventEntry.setEventType(extra.getInt(Globals.KEY_EVENT_TYPE));

	}

	/**
	 * onClicked method Handle Post, Cancel, Date and Time click
	 * 
	 * @param view
	 */
	public void onClicked(View view) {
		int dialogId = 0;
		Intent intent;
		Bundle extra = new Bundle();

		switch (view.getId()) {
		case R.id.createNewEventActivityTextStarts:
			// Display time dialog
			dialogId = R.string.ui_dialog_pick_start_time;
			disDialog(dialogId);

			// Display date dialog
			dialogId = R.string.ui_dialog_pick_start_date;
			disDialog(dialogId);
			break;

		case R.id.createNewEventActivityTextEnds:
			// Display time dialog
			dialogId = R.string.ui_dialog_pick_end_time;
			disDialog(dialogId);
			// Display date dialog
			dialogId = R.string.ui_dialog_pick_end_date;
			disDialog(dialogId);
			break;

		// After creating new event, return to the main screen
		case R.id.createNewEventActivitybtnSave:

			postNewEvent(view);

			intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			break;

		// Cancel creating new event, return to the main screen
		case R.id.createNewEventActivitybtnCancel:
			intent = new Intent(mContext, MainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	/**
	 * Helper method to display the dialog
	 * 
	 * @param int id
	 */
	public void disDialog(int id) {

		DialogFragment newFragment = MyDialogFragment.newInstance(id);
		newFragment.show(getFragmentManager(), "dialog");

	}

	/**
	 * Handle the Save call back from MyDialogFrag Set start date and time
	 * 
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public void onStartDateSet(int year, int monthOfYear, int dayOfMonth) {
		mEventEntry.setStartDate(year, monthOfYear, dayOfMonth);

		// DialogFragment newFragment = MyDialogFragment
		// .newInstance(R.string.ui_dialog_pick_start_time);
		// newFragment.show(getFragmentManager(), "dialog");
	}

	public void onStartTimeSet(int hourOfDay, int minute) {
		mEventEntry.setStartTime(hourOfDay, minute);

		// Render the picked date and time in the text view
		TextView dateTimeView = (TextView) findViewById(R.id.createNewEventActivityTextStarts);
		String dateString = Utils.parseTime(
				mEventEntry.getStartDateTimeInMillis(), mContext);
		dateTimeView.setText(dateString);
	}

	/**
	 * Handle the Save call back from MyDialogFrag Set end date and time
	 * 
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public void onEndDateSet(int year, int monthOfYear, int dayOfMonth) {
		mEventEntry.setEndDate(year, monthOfYear, dayOfMonth);

		// DialogFragment newFragment = MyDialogFragment
		// .newInstance(R.string.ui_dialog_pick_end_time);
		// newFragment.show(getFragmentManager(), "dialog");
	}

	public void onEndTimeSet(int hourOfDay, int minute) {
		mEventEntry.setEndTime(hourOfDay, minute);

		// Render the picked date and time in the text view
		TextView dateTimeView = (TextView) findViewById(R.id.createNewEventActivityTextEnds);
		String dateString = Utils.parseTime(
				mEventEntry.getEndDateTimeInMillis(), mContext);
		dateTimeView.setText(dateString);
	}

	/**
	 * Save eventEntry data and send it to GCM
	 */
	private void postNewEvent(View view) {

		// EventCircle
		try {
			mIntValue = ((Spinner) findViewById(R.id.createNewEventActivitySpinnerWhos))
					.getSelectedItemPosition();
		} catch (Exception e) {
			mIntValue = 0;
		}
		mEventEntry.setCircle(mIntValue);

		// EventTitle
		try {
			mValue = (String) ((EditText) findViewById(R.id.createNewEventActivityTextUp4))
					.getText().toString();
		} catch (Exception e) {
			mValue = "";
		}
		mEventEntry.setEventTitle(mValue);

		// EventLocation
		try {
			mValue = (String) ((EditText)findViewById(R.id.createNewEventActivityTextLocation))
					.getText().toString();
		} catch (Exception e) {
			mValue = "";
		}
		mEventEntry.setLocation(mValue);

		// EventComments
		try {
			mValue = (String) ((EditText)findViewById(R.id.createNewEventActivityTextComments))
					.getText().toString();
		} catch (Exception e) {
			mValue = "";
		}
		mEventEntry.setDetail(mValue);
	}
}
