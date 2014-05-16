package edu.dartmouth.cs.whosupfor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageView mImageView;
	private Uri mImageCaptureUri;
	private byte[] mByteArray;

	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageView = (ImageView) findViewById(R.id.imageMainActivityProfile);
		mContext = getApplicationContext();
	}

	@Override
	/**
	 * Create menu options: Profile, Contacts, Settings, Logout
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		
		switch (item.getItemId()) {
		case R.id.menuitem_profile:
			// Launch EditProfileActivity
			Toast.makeText(this, getString(R.string.ui_menu_profile),
					Toast.LENGTH_SHORT).show();
			
			intent = new Intent(mContext, EditProfileActivity.class);
			startActivity(intent);
			return true;
		case R.id.menuitem_contacts:
			// Launch ContactsActivity
			Toast.makeText(this, getString(R.string.ui_menu_contacts),
					Toast.LENGTH_SHORT).show();
			intent = new Intent(mContext, ContactsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menuitem_settings:
			// Jump to SettingsActivity
			Toast.makeText(this, getString(R.string.ui_menu_settings),
					Toast.LENGTH_SHORT).show();
			intent = new Intent(mContext, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menuitem_logout:
			// Launch LoginActivity
			Toast.makeText(this, getString(R.string.ui_menu_logout),
					Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return false;
	}

	public void onPostEventClicked(View v) {

		Intent intent = new Intent(mContext, SelectNewEventTypeActivity.class);
		startActivity(intent);
	}



}
