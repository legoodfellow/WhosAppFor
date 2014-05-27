package edu.dartmouth.cs.whosupfor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.data.UserEntryDbHelper;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class EventDetailsActivity extends ListActivity {

	private Context mContext;
	private AttendeesEntriesAdapter mAttendeesEntriesAdapter;
	private ArrayList<String> mAttendees;
	private ArrayList<UserEntry> mUserEntries;
	private UserEntryDbHelper mUserEntryDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		mContext = this;

		Bundle extras = getIntent().getExtras();

		mUserEntryDbHelper = new UserEntryDbHelper(mContext);
		mUserEntryDbHelper.getReadableDatabase();

		if (extras != null) {

			// Organizer profile photo and name
			

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
			
			mAttendees.add("a@dali.dartmouth.edu");
			mUserEntries = mUserEntryDbHelper.fetchEntriesByAttendees(mAttendees);

			// test
//			mUserEntries = mUserEntryDbHelper.fetchEntries();

			mAttendeesEntriesAdapter = new AttendeesEntriesAdapter(this,
					mUserEntries);

			setListAdapter(mAttendeesEntriesAdapter);

		}

		mUserEntryDbHelper.close();
	}

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
//			convertView.setOnClickListener(new OnItemClickListener(position));

			// Log.d(TAG, "getView() finished");
			return convertView;

		}

		// ViewHolder class that hold over ListView Item
		private class ViewHolder {
			ImageView mImage;
			TextView mName;
			Button mButton;
		}

	}

}
