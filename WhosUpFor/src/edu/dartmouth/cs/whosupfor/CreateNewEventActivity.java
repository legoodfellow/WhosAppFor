package edu.dartmouth.cs.whosupfor;

import java.util.Calendar;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;

public class CreateNewEventActivity extends Activity {
//
//	private EventEntryDbHelper mEventEntryDbHelper;
//	private EventEntry mEventEntry;
	
	private Context mContext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_event);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Get mHelperContext
		mContext =  getApplicationContext();
		
		// Get event type
		Bundle extra = getIntent().getExtras();
//		mEventEntry.setEventType(extra.getInt(Globals.KEY_EVENT_TYPE));
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClicked(View view) {
		int dialogId = 0;
		Intent intent;
		Bundle extra = new Bundle();

		switch (view.getId()) {
		case R.id.createNewEventActivityTextStarts:
			dialogId = R.string.ui_dialog_pick_date;
			disDialog(dialogId);
			break;

		case R.id.createNewEventActivityTextEnds:
			dialogId = R.string.ui_dialog_pick_date;;
			disDialog(dialogId);
			break;
			
			// After creating new event, return to the main screen
		case R.id.createNewEventActivitybtnSave:
			
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

		// if (id != R.string.ui_date_title && id != R.string.ui_time_title) {
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		//
		// }
		newFragment.show(getFragmentManager(), "dialog");
	}

	/**
	 * Handle the Save call back from MyDialogFrag
	 * 
	 * @param dateTime
	 */
	public void onDateSave(Calendar dateTime) {
//
//		// Date date = null;
//		// dateTime.setTime(date);
//		String DATE_FORMAT_NOW = "MMM dd, yyyy";
//		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
//		String dt = sdf.format(dateTime.getTime());
//
//		// long millis = dateTime.getTimeInMillis();
//		exerEntry.setDate(dt);
//		Log.d(Globals.TAG_MANUAL_INPUT_ACTIVITY,
//				"onDateSave(Calendar dateTime): " + dt);
		
		int dialogId = R.string.ui_dialog_pick_time;
		disDialog(dialogId);
	}
	
	public void onTimeSave(Calendar dateTime) {

//		String DATE_FORMAT_NOW = "HH:mm:ss";
//		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
//		String dt = sdf.format(dateTime.getTime());
//
//		// long millis = dateTime.getTimeInMillis();
//		exerEntry.setTime(dt);
//		Log.d(Globals.TAG_MANUAL_INPUT_ACTIVITY, "onDateSave(Calendar dateTime): " + dt);
	}
}
