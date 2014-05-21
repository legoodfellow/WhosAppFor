package edu.dartmouth.cs.whosupfor.menu;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.R;
import edu.dartmouth.cs.whosupfor.R.id;
import edu.dartmouth.cs.whosupfor.R.layout;
import edu.dartmouth.cs.whosupfor.R.menu;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
//		mUserEntryDbHelper = new UserEntryDbHelper(this);
//		mUserEntryDbHelper.getWritableDatabase();
//		
//		mUserEntries = mUserEntryDbHelper.fetchEntries();
		
		mContactsEntriesAdapter = new ContactsEntriesAdapter(this,
				android.R.layout.simple_list_item_activated_2, mUserEntries);

//		mUserEntryDbHelper.close();
//		setListAdapter(mContactsEntriesAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuitem_add_contacts:
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

	private class ContactsEntriesAdapter extends ArrayAdapter<UserEntry> {

		private Context mHelperContext;
		private ArrayList<UserEntry> mEntries;
		private LayoutInflater layout = LayoutInflater.from(this.getContext());

		public ContactsEntriesAdapter(Context context, int resource,
				ArrayList<UserEntry> entries) {
			super(context, resource, entries);
			this.mHelperContext = context;
			this.mEntries = entries;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = layout.inflate(
						android.R.layout.simple_list_item_2, parent, false);
			}

			// Setup textView
			TextView titleView = (TextView) convertView
					.findViewById(android.R.id.text1);
			// titleView.setTextAppearance(getActivity(),
			// android.R.attr.textAppearanceSmall);
			
			UserEntry entry = mEntries.get(position);

			// Get first and last name
//			String firstName = entry.getFirstName();
//			String lastName = entry.getLastName();

			// Setup textview
//			titleView.setText(firstName + " " + lastName);
			titleView.setTextSize(15);
			titleView.setTypeface(null, Typeface.BOLD_ITALIC);

			// Log.d(TAG, "getView() finished");
			return convertView;

		}

	}

}
