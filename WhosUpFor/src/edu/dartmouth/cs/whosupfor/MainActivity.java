package edu.dartmouth.cs.whosupfor;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.dartmouth.cs.whosupfor.gcm.ServerUtilities;
import edu.dartmouth.cs.whosupfor.menu.ContactsActivity;
import edu.dartmouth.cs.whosupfor.menu.EditProfileActivity;
import edu.dartmouth.cs.whosupfor.menu.SettingsActivity;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class MainActivity extends FragmentActivity {

	private MyPagerAdapter mPagerAdapter;
	private ViewPager mPager;
	private GoogleCloudMessaging mGcm;
	private String mRegid;

	public Fragment mNewsFeedFragment, mMyPostFragment, mGoingFragment;

	public Context mContext;

	/**
	 * Setup fragments
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = getApplicationContext();

		// ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mPager);

		mPagerAdapter.addTab(
				actionBar.newTab().setText(
						getString(R.string.ui_tabname_news_feed)),
				NewsFeedFragment.class, null);
		mPagerAdapter.addTab(
				actionBar.newTab().setText(
						getString(R.string.ui_tabname_my_post)),
				MyPostFragment.class, null);
		mPagerAdapter.addTab(
				actionBar.newTab()
						.setText(getString(R.string.ui_tabname_going)),
				GoingFragment.class, null);

		// Restore to the previous tab before rotation
		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					Globals.TAB_KEY_INDEX, 0));
		}

		// -----------------------------------------------------
		// GCM
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {
			mGcm = GoogleCloudMessaging.getInstance(this);
			mRegid = getRegistrationId(mContext);

			if (mRegid.isEmpty()) {
				registerInBackground();
			}
		}

		// // Create new tabs and set up the titles of the tabs
		// ActionBar.Tab mStartTab = actionBar.newTab().setText(
		// getString(R.string.ui_tabname_news_feed));
		// ActionBar.Tab mHistoryTab = actionBar.newTab().setText(
		// getString(R.string.ui_tabname_my_post));
		// ActionBar.Tab mSettingsTab = actionBar.newTab().setText(
		// getString(R.string.ui_tabname_going));

		// Create the fragments
		// mNewsFeedFragment = new NewsFeedFragment();
		// mMyPostFragment = new MyPostFragment();
		// mGoingFragment = new GoingFragment();

		// // Bind the fragments to the tabs - set up tabListeners for each tab
		// mStartTab.setTabListener(new MyTabsListener(mNewsFeedFragment,
		// getApplicationContext()));
		// mHistoryTab.setTabListener(new MyTabsListener(mMyPostFragment,
		// getApplicationContext()));
		// mSettingsTab.setTabListener(new MyTabsListener(mGoingFragment,
		// getApplicationContext()));
		//
		// // Add the tabs to the actionBar
		// actionBar.addTab(mStartTab);
		// actionBar.addTab(mHistoryTab);
		// actionBar.addTab(mSettingsTab);

	}

	// ------------------------------------------------------------------------
	// GCM
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						Globals.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(Globals.PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(Globals.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (mGcm == null) {
						mGcm = GoogleCloudMessaging.getInstance(mContext);
					}
					mRegid = mGcm.register(Globals.SENDER_ID);
					msg = "Device registered, registration ID=" + mRegid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					ServerUtilities.sendRegistrationIdToBackend(mContext,
							mRegid);

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(mContext, mRegid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.i(Globals.TAG_MAIN_ACTIVITY, "gcm register msg: " + msg);
			}
		}.execute(null, null, null);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Globals.PROPERTY_REG_ID, regId);
		editor.putInt(Globals.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	// ----------------------------------------------------------------------------------------------------
	// UI

	/**
	 * To store the tab navigation
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(Globals.TAB_KEY_INDEX, getActionBar()
				.getSelectedNavigationIndex());
		Log.d(Globals.TAG_MAIN_ACTIVITY, "onSaveInstanceState()");

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
			finish();
			return true;
		}

		return false;
	}

	/**
	 * A simple pager adapter that represents 3 objects, in sequence.
	 */
	private class MyPagerAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mHelperContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public MyPagerAdapter(FragmentManager fm, ViewPager pager) {
			super(fm);
			mHelperContext = getApplicationContext();
			mActionBar = getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Fragment getItem(int index) {

			switch (index) {
			case 0:
				// Top Rated fragment activity
				return new NewsFeedFragment();
			case 1:
				// Games fragment activity
				return new MyPostFragment();
			case 2:
				// Movies fragment activity
				return new GoingFragment();
			}
			return null;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mHelperContext, "Unselected!", Toast.LENGTH_SHORT)
					.show();
			// ft.remove(getFragmentManager().findFragmentByTag(tab.toString()));
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}

}

// /**
// * TabListenr class for managing user interaction with the ActionBar tabs. The
// * application mHelperContext is passed in pass it in constructor, needed for
// * the toast.
// */
// class MyTabsListener implements ActionBar.TabListener {
// public Fragment mFragment;
// public Context mHelperContext;
//
// public MyTabsListener(Fragment fragment, Context context) {
// this.mFragment = fragment;
// this.mHelperContext = context;
//
// }
//
// @Override
// public void onTabReselected(Tab tab, FragmentTransaction ft) {
// Toast.makeText(mHelperContext, "Reselected!", Toast.LENGTH_SHORT)
// .show();
//
// }
//
// @Override
// public void onTabSelected(Tab tab, FragmentTransaction ft) {
// Toast.makeText(mHelperContext, "Selected!", Toast.LENGTH_SHORT).show();
//
// // ft.replace(R.id.fragment_container, mFragment);
// }
//
// @Override
// public void onTabUnselected(Tab tab, FragmentTransaction ft) {
// Toast.makeText(mHelperContext, "Unselected!", Toast.LENGTH_SHORT)
// .show();
//
// // ft.remove(mFragment);
// }
//
// }