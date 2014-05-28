package edu.dartmouth.cs.whosupfor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.data.EventEntryDbHelper;
import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.data.UserEntryDbHelper;
import edu.dartmouth.cs.whosupfor.gcm.ServerUtilities;
import edu.dartmouth.cs.whosupfor.menu.EditProfileActivity;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class EventDetailsActivity extends ListActivity {

	private Context mContext;
	private AttendeesEntriesAdapter mAttendeesEntriesAdapter;
	private ArrayList<String> mAttendees;
	private ArrayList<UserEntry> mUserEntries;
	private UserEntryDbHelper mUserEntryDbHelper;
	private EventEntryDbHelper mEventEntryDbHelper;
	private byte[] mByteArray;
	private IntentFilter mMessageIntentFilter;
	private String mEventId;
	private String mEmail;

	private MyBroadcastReceiver mMessageUpdateReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		mContext = this;

		Bundle extras = getIntent().getExtras();

		// Get database helpers
		mUserEntryDbHelper = new UserEntryDbHelper(mContext);
		mUserEntryDbHelper.getReadableDatabase();
		mEventEntryDbHelper = new EventEntryDbHelper(mContext);

		// Create broadcast receiver filter
		mMessageIntentFilter = new IntentFilter();
		mMessageIntentFilter.addAction("GCM_NOTIFY");

		// Create broadcast receiver and register it
		mMessageUpdateReceiver = new MyBroadcastReceiver();
		registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);

		// Set event organizer email address
		// Get the shared preference
		String mKey = getString(R.string.preference_name_edit_profile_activity);
		Log.d(Globals.TAG_MAIN_ACTIVITY, " ");
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);
		Log.d(Globals.TAG_MAIN_ACTIVITY, " ");
		// Load the email and add it to event entry
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_email);
		Log.d(Globals.TAG_MAIN_ACTIVITY, " ");
		mEmail = mPrefs.getString(mKey, "");

		if (extras != null) {

			// Organizer profile photo and name
			try {
				mByteArray = extras
						.getByteArray(Globals.KEY_USER_PROFILE_PHOTO);
				ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
				Bitmap bp = BitmapFactory.decodeStream(bis);
				((ImageView) findViewById(R.id.eventDetailProfileImage))
						.setImageBitmap(bp);
			} catch (Exception e) {

			}

			// If it's my post, hide the radio group
			if (extras.getInt("MY_POST") == 1) {
				((RadioGroup) findViewById(R.id.eventDetailActivityRadioGroup))
						.setVisibility(View.GONE);
			}

			// Event Organizer name
			try {
				((TextView) findViewById(R.id.eventDetailActivityTextName))
						.setText(extras.getString(Globals.KEY_USER_FIRST_NAME));
			} catch (Exception e) {
				((TextView) findViewById(R.id.eventDetailActivityTextName))
						.setText("unknown");
			}

			// Event type
			((TextView) findViewById(R.id.eventDetailActivityTextType))
					.setText(Utils.getEventType(extras
							.getInt(Globals.KEY_EVENT_TYPE)));

			// Event title
			((EditText) findViewById(R.id.eventDetailActivityTextTypeTitle))
					.setText(extras.getString(Globals.KEY_EVENT_TITLE));

			// Event location
			((EditText) findViewById(R.id.eventDetailActivityTextTypeLocationDetail))
					.setText(extras.getString(Globals.KEY_EVENT_LOCATION));

			// Event start date and time, end date and time
			((EditText) findViewById(R.id.eventDetailActivityTextStartDateTimeDetail))
					.setText(extras
							.getString(Globals.KEY_EVENT_START_DATE_TIME));
			((EditText) findViewById(R.id.eventDetailActivityTextEndDateTimeDetail))
					.setText(extras.getString(Globals.KEY_EVENT_END_DATE_TIME));

			// Event detail
			((EditText) findViewById(R.id.eventDetailActivityTextDetailHint))
					.setText(extras.getString(Globals.KEY_EVENT_DETAIL));

			// Event Attendees
			mAttendees = new ArrayList<String>();
			mAttendees = extras.getStringArrayList(Globals.KEY_EVENT_ATTENDEES);

			mUserEntries = new ArrayList<UserEntry>();

			// mAttendees.add("a@dali.dartmouth.edu");
			mUserEntries = mUserEntryDbHelper
					.fetchEntriesByAttendees(mAttendees);

			// Get EventId
			mEventId = extras.getString(Globals.KEY_EVENT_ID);

			// test
			// mUserEntries = mUserEntryDbHelper.fetchEntries();

			mAttendeesEntriesAdapter = new AttendeesEntriesAdapter(this,
					mUserEntries);

			setListAdapter(mAttendeesEntriesAdapter);

		}

		// Set up radio group listener and communicate with GCM
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.eventDetailActivityRadioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.eventDetailActivityRadioGroupGoing) {
					postMsg(Globals.GOING);
				} else if (checkedId == R.id.eventDetailActivityRadioGroupNotGoing) {
					postMsg(Globals.DECLINE);
				}
			}
		});

		mUserEntryDbHelper.close();
	}

	// ------------------------------------------------------------------------
	// GCM
	/**
	 * Post GOING or DECLINE to Google Cloud
	 * 
	 * @param msg
	 *            : JSON string
	 */
	private void postMsg(int msg) {
		new AsyncTask<Integer, Void, ArrayList<EventEntry>>() {

			@Override
			protected ArrayList<EventEntry> doInBackground(Integer... arg0) {
				// Log.d(Globals.TAG_CREATE_NEW_EVENT_ACTIVITY,
				// "postMsg().doInBackground() got called");
				String url = Globals.SERVER_ADDR + "/post.do";
				ArrayList<EventEntry> res = new ArrayList<EventEntry>();
				Map<String, String> params = new HashMap<String, String>();

				// // Load the profile email
				// String mKey =
				// getString(R.string.preference_name_edit_profile_activity);
				// SharedPreferences mPrefs = getSharedPreferences(mKey,
				// MODE_PRIVATE);
				// mKey =
				// getString(R.string.preference_key_edit_profile_activity_profile_email);
				// mEmail = mPrefs.getString(mKey, "");

				// Add params to be sent
				params.put("post_text", arg0[0].toString());
				params.put("task_type", "reply_going");
				params.put("user_email", mEmail);
				params.put("event_id", mEventId);

				try {
					res = ServerUtilities.post(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			protected void onPostExecute(ArrayList<EventEntry> res) {
				// mPostText.setText("");
				// refreshPostHistory();
			}

		}.execute(msg);
		// Log.d(Globals.TAG_CREATE_NEW_EVENT_ACTIVITY,
		// "postMsg().doInBackground() got called");
	}

	/**
	 * Refresh event list
	 */
	private void refreshPostHistory() {
		new AsyncTask<Void, Void, ArrayList<EventEntry>>() {

			@Override
			protected ArrayList<EventEntry> doInBackground(Void... arg0) {
				// Call GetHistoryServlet on server side
				String url = Globals.SERVER_ADDR + "/get_event_history.do";
				ArrayList<EventEntry> res = new ArrayList<EventEntry>();

				Map<String, String> params = new HashMap<String, String>();
				params.put("task_type", "get_events");

				// Get ArrayList<EventEntry> from datastore
				try {
					res = ServerUtilities.post(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			/**
			 * Update the event list and local database
			 */
			protected void onPostExecute(ArrayList<EventEntry> res) {
				if (!res.isEmpty() || res.size() != 0) {

					// Update database
					mEventEntryDbHelper.getWritableDatabase();
					mEventEntryDbHelper.removeAllEntries();
					for (EventEntry eventEntry : res) {
						mEventEntryDbHelper.insertEntry(eventEntry);
					}

					mUserEntryDbHelper.getReadableDatabase();

					// Use the eventId to find the eventEntry and get its
					// updated attendees
					for (EventEntry eventEntry : res) {
						if (eventEntry.getEventId().equals(mEventId)) {
							mAttendees = eventEntry.getAttendees();
						}
					}

					// Get user entries and rend them in the listView
					mUserEntries = mUserEntryDbHelper
							.fetchEntriesByAttendees(mAttendees);
					mAttendeesEntriesAdapter = new AttendeesEntriesAdapter(
							mContext, mUserEntries);
					setListAdapter(mAttendeesEntriesAdapter);

					mUserEntryDbHelper.close();
					mEventEntryDbHelper.close();
				}
			}

		}.execute();
	}

	/**
	 * Broadcast Receiver
	 * 
	 * @author Aaron Jun Yang
	 * 
	 */
	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra("message");
			if (msg != null && msg.equals("update")) {
				refreshPostHistory();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// UI

	/**
	 * Helper method to set up the eventEntries arrayAdapter
	 * 
	 * @author Aaron Jun Yang
	 * 
	 */
	private class AttendeesEntriesAdapter extends ArrayAdapter<UserEntry> {

		private Context mHelperContext;
		private byte[] mByteArray;
		private ArrayList<UserEntry> mEntries;
		private LayoutInflater mLayoutInflater = LayoutInflater.from(this
				.getContext());

		public AttendeesEntriesAdapter(Context context,
				ArrayList<UserEntry> entries) {
			super(context, R.layout.contact_list_item, entries);
			this.mHelperContext = context;
			this.mEntries = entries;
			mByteArray = new byte[0];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder;

			// if it's not create convertView yet create new one and consume it
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.contact_list_item, null);
				// get new ViewHolder
				mHolder = new ViewHolder();
				// get all item in ListView item to corresponding fields in our
				// ViewHolder class
				mHolder.mImage = (ImageView) convertView
						.findViewById(R.id.contactListItemUserProfileImage);
				mHolder.mName = (TextView) convertView
						.findViewById(R.id.contactListItemUserName);
				mHolder.mButton = (Button) convertView
						.findViewById(R.id.contactListItemDelete);
				// set tag of convertView to the holder
				convertView.setTag(mHolder);
			}// if it's exist convertView then consume it
			else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			UserEntry entry = mEntries.get(position);

			// Set image
			try {
				mByteArray = entry.getProfilePhoto();
				ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
				Bitmap bp = BitmapFactory.decodeStream(bis);
				mHolder.mImage.setImageBitmap(bp);
			} catch (Exception e) {
				mHolder.mImage = (ImageView) convertView
						.findViewById(R.id.contactListItemUserProfileImage);
			}

			// Get first and last name
			try {
				String firstName = entry.getFirstName();
				String lastName = entry.getLastName();
				mHolder.mName.setText(firstName + " " + lastName);
			} catch (Exception e) {
				mHolder.mName.setText("unknown");
			}

			mHolder.mButton.setVisibility(View.GONE);

			// Set onClick listener
			// convertView.setOnClickListener(new
			// OnItemClickListener(position));

			// Set onClick listener
			convertView.setOnClickListener(new OnItemClickListener(position));
			// Log.d(TAG, "getView() finished");
			return convertView;

		}

		/**
		 * Helper class OnItemClickListener, hanlde the click on list item
		 * 
		 * @author Aaron Jun Yang
		 * 
		 */
		private class OnItemClickListener implements OnClickListener {

			private int mPosition;

			public OnItemClickListener(int position) {
				mPosition = position;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle extras = new Bundle();
				int mIntValue = -1;
				String mValue = " ";
				ArrayList<String> mAttendees = new ArrayList<String>();
				Intent intent = new Intent();

				// Write row id into extras.
				extras.putLong(Globals.KEY_EVENT_ROWID, mEntries.get(mPosition)
						.getID());

				// Add user email
				extras.putString(Globals.KEY_USER_EMAIL, mEntries
						.get(mPosition).getEmail());

				// Fire intent to EventDetailActivity
				intent.setClass(mContext, EditProfileActivity.class);
				intent.putExtras(extras);
				startActivity(intent);

			}
		}

		// ViewHolder class that hold over ListView Item
		private class ViewHolder {
			ImageView mImage;
			TextView mName;
			Button mButton;
		}

	}

	// -----------------------------------------------------------------------
	// GCM

}
