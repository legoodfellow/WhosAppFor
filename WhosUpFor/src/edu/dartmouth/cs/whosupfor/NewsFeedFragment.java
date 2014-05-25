package edu.dartmouth.cs.whosupfor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.DialogFragment;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.data.EventEntryDbHelper;
import edu.dartmouth.cs.whosupfor.gcm.ServerUtilities;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class NewsFeedFragment extends ListFragment {

	private ImageView mImageView;
	private Context mContext;
	private Button mBtnFilter, mBtnPost;
	public static boolean mHasProfileImage = false;
	private IntentFilter mMessageIntentFilter;

	private MyBroadcastReceiver mMessageUpdateReceiver;

	private EventEntryDbHelper mEventEntryDbHelper;
	private ArrayList<EventEntry> mEventEntries;
	private EventEntriesAdapter mEventEntriesAdapter;

	// private BroadcastReceiver mMessageUpdateReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String msg = intent.getStringExtra("message");
	// if (msg != null && msg.equals("update")) {
	// refreshPostHistory();
	// }
	// }
	// };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for start fragment
		View view = inflater.inflate(R.layout.news_feed_fragment, container,
				false);

		mContext = getActivity();

		// Update imageView;
		mImageView = (ImageView) view
				.findViewById(R.id.imageNewsFeedFragmentProfile);

		try {
			FileInputStream fis = mContext
					.openFileInput(getString(R.string.preference_key_edit_profile_photo_file_name));
			Bitmap bmap = BitmapFactory.decodeStream(fis);
			mImageView.setImageBitmap(bmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Assign button info of Start and Sync
		mBtnFilter = (Button) view.findViewById(R.id.btnNewsFeedFragmentFilter);
		mBtnPost = (Button) view
				.findViewById(R.id.btnNewsFeedFragmentPostEvent);
		setUpFilterBtn();
		setUpPostBtn();

		return view;

	}

	// --------------------------------------------------------------------------------------
	// GCM
	@Override
	public void onResume() {
		// Create broadcast receiver filter
		mMessageIntentFilter = new IntentFilter();
		mMessageIntentFilter.addAction("GCM_NOTIFY");

		mMessageUpdateReceiver = new MyBroadcastReceiver();

		mContext.registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);
		refreshPostHistory();
		
		// For test purpose
		mEventEntryDbHelper = new EventEntryDbHelper(mContext);
		mEventEntryDbHelper.getWritableDatabase();

		mEventEntries = mEventEntryDbHelper.fetchEntries();

		mEventEntriesAdapter = new EventEntriesAdapter(mContext, mEventEntries);

		mEventEntryDbHelper.close();
		setListAdapter(mEventEntriesAdapter);

		super.onResume();
	}

	@Override
	public void onPause() {

		mContext.unregisterReceiver(mMessageUpdateReceiver);
		super.onPause();
	}

	private void refreshPostHistory() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... arg0) {
				String url = Globals.SERVER_ADDR + "/get_history.do";
				String res = "";
				Map<String, String> params = new HashMap<String, String>();
				try {
					res = ServerUtilities.post(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			protected void onPostExecute(String res) {
				if (!res.equals("")) {
					// mHistoryText.setText(res);
				}
			}

		}.execute();
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msg = intent.getStringExtra("message");
			if (msg != null && msg.equals("update")) {
				refreshPostHistory();
			}
		}
	}

	// -----------------------------------------------------------------------------------------
	// UI

	/**
	 * Helper class for setting up arrayAdapter
	 * 
	 * 
	 */
	private class EventEntriesAdapter extends ArrayAdapter<EventEntry> {

		private Context mHelperContext;
		private ArrayList<EventEntry> mEntries;
		private LayoutInflater mLayoutInflater = LayoutInflater.from(this
				.getContext());

		public EventEntriesAdapter(Context context,
				ArrayList<EventEntry> entries) {
			super(context, R.layout.event_list_item, entries);
			this.mHelperContext = context;
			this.mEntries = entries;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder;

			// if it's not create convertView yet create new one and consume it
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.event_list_item, null);
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

				// set tag of convertView to the holder
				convertView.setTag(mHolder);
			}// if it's exist convertView then consume it
			else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			EventEntry entry = mEntries.get(position);

			// // Set image
			// try {
			// mByteArray = entry.getProfilePhoto();
			// ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
			// Bitmap bp = BitmapFactory.decodeStream(bis);
			// mHolder.mImage.setImageBitmap(bp);
			// } catch (Exception e) {
			// mHolder.mImage = (ImageView) convertView
			// .findViewById(R.id.contactListItemUserProfileImage);
			// }
			
			
			// Load the profile image from internal storage
			try {
				FileInputStream fis = mHelperContext
						.openFileInput(getString(R.string.preference_key_edit_profile_photo_file_name));
				Bitmap bmap = BitmapFactory.decodeStream(fis);
				mHolder.mImage.setImageBitmap(bmap);
				fis.close();
			} catch (IOException e) {
				// Default profile photo if no photo saved before.
				mHolder.mImage.setImageResource(R.drawable.icon_selfies);
			}

			// // Get first and last name
			// try {
			// String firstName = entry.get();
			// String lastName = entry.getLastName();
			// mHolder.mName.setText(firstName + " " + lastName);
			// } catch (Exception e) {
			// mHolder.mName.setText("unknown");
			// }

			String mKey = getString(R.string.preference_name_edit_profile_activity);
			SharedPreferences mPrefs = mContext.getSharedPreferences(mKey,
					mContext.MODE_PRIVATE);

			// Load the profile name
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_first_name);
			String mFirstName = mPrefs.getString(mKey, "");
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_last_name);
			String mLastName = mPrefs.getString(mKey, "");
			mHolder.mName.setText(mFirstName + " " + mLastName);
			
			// Set Event type
			String[] EventType = {"Food", "Sports", "Study", "Movie", "Party"};
			try{
				int eventType = entry.getEventType();
				String result = EventType[eventType];
				mHolder.mEventType.setText(result);
			}catch(Exception e){
				
			}
			
			try{
				long dateTime = entry.getStartDateTimeInMillis();
				String result = Utils.parseTime(dateTime, mContext);
				mHolder.mEventDataTime.setText(result);
			}catch(Exception e){
				
			}
			// Set date and time

			// Set delete button
			// if(entry.getFirstName().equals("Aaron")){
			// mHolder.mButton.setVisibility(View.GONE);
			// }

			// Log.d(TAG, "getView() finished");
			return convertView;

		}

		// ViewHolder class that hold over ListView Item
		private class ViewHolder {
			ImageView mImage;
			TextView mName;
			TextView mEventType;
			TextView mEventDataTime;
		}

	}

	/**
	 * Helper method to setup post button listener
	 */
	private void setUpPostBtn() {
		// Set up Start button and Sync button listeners
		mBtnPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						SelectNewEventTypeActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Helper method to setup filter button listener
	 */
	private void setUpFilterBtn() {
		// Set up Start button and Sync button listeners
		mBtnFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment fragment = MyDialogFragment
						.newInstance(R.string.ui_news_feed_fragment_btn_filter);
				fragment.show(getActivity().getFragmentManager(),
						getString(R.string.ui_news_feed_fragment_btn_filter));
			}
		});
	}

}
