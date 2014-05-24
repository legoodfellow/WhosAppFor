package edu.dartmouth.cs.whosupfor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class SelectNewEventTypeActivity extends Activity {

	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_new_event_type);
		
		mContext = getApplicationContext();
		// Show the Up button in the action bar.
//		setupActionBar();
	}

//	/**
//	 * Set up the {@link android.app.ActionBar}, if the API is available.
//	 */
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setupActionBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			getActionBar().setDisplayHomeAsUpEnabled(true);
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.select_new_event_type, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * Open different event post UI depending on the event type
	 * 
	 * @param view
	 */
	public void onClicked(View view) {

		Intent intent;
		Bundle extra = new Bundle();

		switch (view.getId()) {
		// Food
		case R.id.activitySelectNewEventTypeBtnFood:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnFood),
					Toast.LENGTH_SHORT).show();
			// Remember the event type
			extra.putInt(Globals.KEY_EVENT_TYPE,
					Globals.EVENT_TYPE_FOOD);
			break;
		// Sports
		case R.id.activitySelectNewEventTypeBtnSports:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnSports),
					Toast.LENGTH_SHORT).show();
			extra.putInt(Globals.KEY_EVENT_TYPE,
					Globals.EVENT_TYPE_SPORTS);
			break;
		// Study
		case R.id.activitySelectNewEventTypeBtnStudy:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnStudy),
					Toast.LENGTH_SHORT).show();
			extra.putInt(Globals.KEY_EVENT_TYPE,
					Globals.EVENT_TYPE_STUDY);
			break;
		// Movie
		case R.id.activitySelectNewEventTypeBtnMovie:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnMovie),
					Toast.LENGTH_SHORT).show();
			extra.putInt(Globals.KEY_EVENT_TYPE,
					Globals.EVENT_TYPE_MOVIE);
			break;
		// Party
		case R.id.activitySelectNewEventTypeBtnParty:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnParty),
					Toast.LENGTH_SHORT).show();
			extra.putInt(Globals.KEY_EVENT_TYPE,
					Globals.EVENT_TYPE_PARTY);
			break;
		// New Event Type
		case R.id.activitySelectNewEventTypeBtnAddNew:
			Toast.makeText(this,
					getString(R.id.activitySelectNewEventTypeBtnAddNew),
					Toast.LENGTH_SHORT).show();
//			extra.putInt(Globals.KEY_EVENT_TYPE,
//					R.id.activitySelectNewEventTypeBtnAddNew);
			break;

		default:
			break;
		}

		intent = new Intent(mContext, CreateNewEventActivity.class);
		intent.putExtras(extra);
		// Call CreateNewEventActivity to enter detail event info.
		startActivity(intent);
	}

}
