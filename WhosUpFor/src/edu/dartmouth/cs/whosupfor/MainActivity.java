package edu.dartmouth.cs.whosupfor;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import edu.dartmouth.cs.whosupfor.menu.ContactsActivity;
import edu.dartmouth.cs.whosupfor.menu.EditProfileActivity;
import edu.dartmouth.cs.whosupfor.menu.SettingsActivity;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class MainActivity extends FragmentActivity {

	private MyPagerAdapter mPagerAdapter;
	private ViewPager mPager;

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
//			ft.remove(getFragmentManager().findFragmentByTag(tab.toString()));
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