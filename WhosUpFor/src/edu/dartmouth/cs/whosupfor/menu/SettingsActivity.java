package edu.dartmouth.cs.whosupfor.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import edu.dartmouth.cs.whosupfor.R;
import edu.dartmouth.cs.whosupfor.util.Globals;


public class SettingsActivity extends ListActivity {

	private SettingsAdapter mSettingsAdapter;
	private List<String> mSettingsList;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mContext = getApplicationContext();

		mSettingsList = new ArrayList<String>();
		mSettingsList = Arrays.asList(Globals.SETTINGS_ARRAY);

		mSettingsAdapter = new SettingsAdapter(this, mSettingsList);
		setListAdapter(mSettingsAdapter);

	}

	private class SettingsAdapter extends ArrayAdapter<String> {

		private Context mHelperContext;
		private List<String> mEntries;
		private LayoutInflater mLayoutInflater = LayoutInflater.from(this
				.getContext());

		public SettingsAdapter(Context context, List<String> entries) {
			super(context, R.layout.settings_list_item, entries);
			this.mHelperContext = context;
			this.mEntries = entries;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder;

			// if it's not create convertView yet create new one and consume it
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.settings_list_item, null);
				// get new ViewHolder
				mHolder = new ViewHolder();
				// get all item in ListView item to corresponding fields in our
				// ViewHolder class
				mHolder.mName = (TextView) convertView
						.findViewById(R.id.settingsListItemName);
				mHolder.mButton = (Switch) convertView
						.findViewById(R.id.settingsSwitch);
				// set tag of convertView to the holder
				convertView.setTag(mHolder);
			}// if it's exist convertView then consume it
			else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			String entry = mEntries.get(position);

			// Set settings list name
			mHolder.mName.setText(entry);

			// Add sliding view for Notification
			if (!entry.equals("Notification")) {
				mHolder.mButton.setVisibility(View.GONE);
			}

			// Log.d(TAG, "getView() finished");
			return convertView;

		}

		// ViewHolder class that hold over ListView Item
		private class ViewHolder {
			TextView mName;
			Switch mButton;
		}

	}

}
