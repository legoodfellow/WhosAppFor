package edu.dartmouth.cs.whosupfor.menu;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.R;
import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.data.UserEntryDbHelper;

/**
 * Add or delete contacts, edit circles
 * 
 * @author Aaron Jun Yang
 * 
 */
public class ContactsActivity extends ListActivity {
	//
	private UserEntryDbHelper mUserEntryDbHelper;
	private ContactsEntriesAdapter mContactsEntriesAdapter;
	private ArrayList<UserEntry> mUserEntries;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		mUserEntryDbHelper = new UserEntryDbHelper(this);
		mUserEntryDbHelper.getWritableDatabase();

		mUserEntries = mUserEntryDbHelper.fetchEntries();

		mContactsEntriesAdapter = new ContactsEntriesAdapter(this, mUserEntries);

		mContext = getApplicationContext();

		mUserEntryDbHelper.close();
		setListAdapter(mContactsEntriesAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menuitem_add_contacts:
			intent = new Intent(mContext, EditProfileActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	private class ContactsEntriesAdapter extends ArrayAdapter<UserEntry> {

		private Context mHelperContext;
		private byte[] mByteArray;
		private ArrayList<UserEntry> mEntries;
		private LayoutInflater mLayoutInflater = LayoutInflater.from(this
				.getContext());

		public ContactsEntriesAdapter(Context context,
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
				mHolder.mButton = (Button) convertView.findViewById(R.id.contactListItemDelete);
				// set tag of convertView to the holder
				convertView.setTag(mHolder);
			}// if it's exist convertView then consume it
			else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			UserEntry entry = mEntries.get(position);

			// Set image
			mByteArray = entry.getProfilePhoto();
			ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
			Bitmap bp = BitmapFactory.decodeStream(bis);
			mHolder.mImage.setImageBitmap(bp);

			// Get first and last name
			String firstName = entry.getFirstName();
			String lastName = entry.getLastName();
			mHolder.mName.setText(firstName + " " + lastName);
			
			// Set delete button
//			if(entry.getFirstName().equals("Aaron")){
//				mHolder.mButton.setVisibility(View.GONE);
//			}
			
			
			

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
