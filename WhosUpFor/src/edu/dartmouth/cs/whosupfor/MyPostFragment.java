package edu.dartmouth.cs.whosupfor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.data.EventEntryDbHelper;
import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.data.UserEntryDbHelper;
import edu.dartmouth.cs.whosupfor.gcm.ServerUtilities;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class MyPostFragment extends ListFragment {

	private Context mContext;

	public static boolean mHasProfileImage = false;

	private EventEntryDbHelper mEventEntryDbHelper;
	private UserEntryDbHelper mUserEntryDbHelper;
	private ArrayList<EventEntry> mEventEntries;
	private MyPostEntriesAdapter mMyPostEntriesAdapter;
	private byte[] mByteArray;

	private IntentFilter mMessageIntentFilter;
	private MyBroadcastReceiver mMessageUpdateReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for start fragment
		View view = inflater.inflate(R.layout.my_post_fragment, container,
				false);

		mContext = getActivity();

		return view;

	}

	// --------------------------------------------------------------------------------------
	// GCM
	@Override
	public void onResume() {
		// Create broadcast receiver filter
		mMessageIntentFilter = new IntentFilter();
		mMessageIntentFilter.addAction("GCM_NOTIFY");

		// Create broadcast receiver and register it
		mMessageUpdateReceiver = new MyBroadcastReceiver();
		mContext.registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);

		// Get eventEntrie information from local database and populate the
		// event list
		mEventEntryDbHelper = new EventEntryDbHelper(mContext);
		mUserEntryDbHelper = new UserEntryDbHelper(mContext);
		mUserEntryDbHelper.getReadableDatabase();
		mEventEntryDbHelper.getReadableDatabase();

		// Get user email
		String mKey = getString(R.string.preference_name_edit_profile_activity);
		SharedPreferences mPrefs = mContext.getSharedPreferences(mKey,
				mContext.MODE_PRIVATE);
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_email);
		String mValue = mPrefs.getString(mKey, "");

		mEventEntries = new ArrayList<EventEntry>();

		// test
		mEventEntries = mEventEntryDbHelper.fetchEntriesByEmail(mValue);
		mEventEntryDbHelper.close();

		mMyPostEntriesAdapter = new MyPostEntriesAdapter(mContext,
				mEventEntries);

		setListAdapter(mMyPostEntriesAdapter);

		mUserEntryDbHelper.close();

		super.onResume();
	}

	@Override
	public void onPause() {

		mContext.unregisterReceiver(mMessageUpdateReceiver);
		super.onPause();
	}

	/**
	 * Refresh event list
	 */
	private void refreshPostHistory() {
		new AsyncTask<Void, Void, ArrayList<EventEntry>>() {

			@Override
			protected ArrayList<EventEntry> doInBackground(Void... arg0) {
				// Call GetHistoryServlet on server side
				String url = Globals.SERVER_ADDR + "/get_history.do";
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
					mEventEntryDbHelper.close();

					// Update event list
					mMyPostEntriesAdapter = new MyPostEntriesAdapter(mContext,
							res);
					setListAdapter(mMyPostEntriesAdapter);

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
			if (msg != null && msg.equals("update_going")) {
				refreshPostHistory();
			}
		}
	}

	// -----------------------------------------------------------------------------------------
	// ArrayAdapter

	/**
	 * Helper class for setting up arrayAdapter It gets called every time
	 * NewFeedFragment is created, resumed or the broadcast receiver receives a
	 * new update message from the server.
	 * 
	 * When NewFeedFragment is created or resumed, it retrieves event entries
	 * from local database and display them in the listview
	 * 
	 * When the broadcast receiver receives a new update message, it retrieves
	 * event entries from cloud datastore and update listView
	 */
	private class MyPostEntriesAdapter extends ArrayAdapter<EventEntry> {

		private Context mHelperContext;
		private ArrayList<EventEntry> mEntries;
		private LayoutInflater mLayoutInflater = LayoutInflater.from(this
				.getContext());

		/**
		 * Constructor
		 * 
		 * @param context
		 * @param entries
		 */
		public MyPostEntriesAdapter(Context context,
				ArrayList<EventEntry> entries) {
			super(context, R.layout.event_list_item, entries);
			this.mHelperContext = context;
			this.mEntries = entries;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder;

			// if it's not created convertView yet create new one and consume it
			if (convertView == null) {

				convertView = mLayoutInflater.inflate(R.layout.event_list_item,
						null);
				// get new ViewHolder
				mHolder = new ViewHolder();

				// get all item in ListView item to corresponding fields in our
				// ViewHolder class
				mHolder.mImage = (ImageView) convertView
						.findViewById(R.id.eventListItemUserProfileImage);
				mHolder.mName = (TextView) convertView
						.findViewById(R.id.eventListItemUserName);
				mHolder.mEventType = (TextView) convertView
						.findViewById(R.id.eventListItemEventType);
				mHolder.mEventDataTime = (TextView) convertView
						.findViewById(R.id.eventListItemStartDateTime);
				mHolder.mEventTitle = (TextView) convertView
						.findViewById(R.id.eventListItemEventTitle);

				// set tag of convertView to the holder
				convertView.setTag(mHolder);
			}// if it's exist convertView then consume it
			else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			// Get eventEntry data
			EventEntry entry = mEntries.get(position);

			UserEntry userEntry = mUserEntryDbHelper
					.fetchEntriesByEventEntry(entry);

			// Set image
			try {
				mByteArray = userEntry.getProfilePhoto();
				ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
				Bitmap bp = BitmapFactory.decodeStream(bis);
				mHolder.mImage.setImageBitmap(bp);
			} catch (Exception e) {
				mHolder.mImage = (ImageView) convertView
						.findViewById(R.id.contactListItemUserProfileImage);
			}

			// Get first and last name
			try {
				String firstName = userEntry.getFirstName();
				String lastName = userEntry.getLastName();
				mHolder.mName.setText(firstName + " " + lastName);
			} catch (Exception e) {
				mHolder.mName.setText("unknown");
			}

			// Set Event type
			try {
				int eventType = entry.getEventType();
				String result = Globals.EVENT_TYPE_ARRAY[eventType];
				mHolder.mEventType.setText(result);
			} catch (Exception e) {

			}

			// Set event start date and time
			try {
				long dateTime = entry.getStartDateTimeInMillis();
				String result = Utils.parseTime(dateTime, mContext);
				mHolder.mEventDataTime.setText(result);
			} catch (Exception e) {

			}

			// Set event title
			try {
				String eventTitle = entry.getEventTitle();
				mHolder.mEventTitle.setText(eventTitle);
			} catch (Exception e) {

			}

			// Set onClick listener
			convertView.setOnClickListener(new OnItemClickListener(position));

			// Log.d(TAG, "getView() finished");
			return convertView;
		}

		/**
		 * Helper class OnItemClickListener, hanlde the click on list item In
		 * order to keep strong consistent, instead of just passing the position
		 * idx and letting EventDetailActivity use that idx to retrieve
		 * eventEntry data from local database, we send bundle object to the
		 * EventDetailActivity.
		 * 
		 * The reason is that before EventDetailActivity is called and the view
		 * is displayed, the broadcast receiver may receive new update request
		 * and update the database thus the position idx will be directed to
		 * wrong entry data
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

				// My post or other's post
				extras.putInt("MY_POST", 1);

				// Get UserEntry
				mUserEntryDbHelper.getReadableDatabase();
				UserEntry userEntry = mUserEntryDbHelper
						.fetchEntriesByEventEntry(mEntries.get(mPosition));

				// Event Organizer name
				mValue = userEntry.getFirstName() + " "
						+ userEntry.getLastName();
				extras.putString(Globals.KEY_USER_FIRST_NAME, mValue);

				// Event Organizer profile image
				try {
					mByteArray = userEntry.getProfilePhoto();
					extras.putByteArray(Globals.KEY_USER_PROFILE_PHOTO,
							mByteArray);
				} catch (Exception e) {

				}

				// Event type
				mIntValue = mEntries.get(mPosition).getEventType();
				extras.putInt(Globals.KEY_EVENT_TYPE, mIntValue);

				// Event title
				mValue = mEntries.get(mPosition).getEventTitle();
				extras.putString(Globals.KEY_EVENT_TITLE, mValue);

				// Event location
				mValue = mEntries.get(mPosition).getLocation();
				extras.putString(Globals.KEY_EVENT_LOCATION, mValue);

				// Event start date and time, end date and time
				long dateTime = mEntries.get(mPosition)
						.getStartDateTimeInMillis();
				mValue = Utils.parseTime(dateTime, mContext);
				extras.putString(Globals.KEY_EVENT_START_DATE_TIME, mValue);
				dateTime = mEntries.get(mPosition).getEndDateTimeInMillis();
				mValue = Utils.parseTime(dateTime, mContext);
				extras.putString(Globals.KEY_EVENT_END_DATE_TIME, mValue);

				// Event detail
				mValue = mEntries.get(mPosition).getDetail();
				extras.putString(Globals.KEY_EVENT_DETAIL, mValue);

				// Event Attendees
				mAttendees = mEntries.get(mPosition).getAttendees();
				extras.putStringArrayList(Globals.KEY_EVENT_ATTENDEES,
						mAttendees);

				// Fire intent to EventDetailActivity
				intent.setClass(mContext, EventDetailsActivity.class);
				intent.putExtras(extras);
				startActivity(intent);

			}
		}

		// ViewHolder class that hold over ListView Item
		private class ViewHolder {
			ImageView mImage;
			TextView mName;
			TextView mEventType;
			TextView mEventDataTime;
			TextView mEventTitle;
		}

	}

}
