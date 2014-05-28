package edu.dartmouth.cs.whosupfor.menu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import edu.dartmouth.cs.whosupfor.MainActivity;
import edu.dartmouth.cs.whosupfor.NewsFeedFragment;
import edu.dartmouth.cs.whosupfor.R;
import edu.dartmouth.cs.whosupfor.data.UserEntry;
import edu.dartmouth.cs.whosupfor.data.UserEntryDbHelper;
import edu.dartmouth.cs.whosupfor.gcm.ServerUtilities;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;

public class EditProfileActivity extends Activity {

	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_TAKE_FROM_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;

	private ImageView mImageView;
	private Uri mImageCaptureUri;
	private byte[] mByteArray;

	public Context mContext;

	private String mEmail;
	private UserEntry mUserEntry;
	private UserEntryDbHelper mUserEntryDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		// Get imageView and context
		mImageView = (ImageView) findViewById(R.id.editProfileImage);
		mContext = getApplicationContext();
		mUserEntryDbHelper = new UserEntryDbHelper(mContext);

		Bundle extras = getIntent().getExtras();

		try {
			mEmail = extras.getString(Globals.KEY_USER_EMAIL);
		} catch (Exception e) {

		}

		if (mEmail != null) {
			loadOtherUserProfile();
		}

		else {
			if (savedInstanceState != null) {
				// Get saved byteArray of Bitmap image
				mByteArray = savedInstanceState
						.getByteArray(Globals.IMG_INSTANCE_STATE_KEY);
				mImageCaptureUri = savedInstanceState
						.getParcelable(Globals.URI_INSTANCE_STATE_KEY);
			}
			// Helper method to load saved data or handle orientation change
			loadProfileData();
		}
	}

	/**
	 * Save temporary data when the orientation changed and destroyed the
	 * activity
	 */
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the image capture uri before the activity goes into background
		outState.putParcelable(Globals.URI_INSTANCE_STATE_KEY, mImageCaptureUri);
		outState.putByteArray(Globals.IMG_INSTANCE_STATE_KEY, mByteArray);

	}

	/**
	 * Display a dialog when clicking on the user profile image
	 * 
	 * @param v
	 */
	public void onImageClicked(View v) {
		DialogFragment fragment = MyDialogFragment
				.newInstance(R.string.ui_dialog_profile_photo);
		fragment.show(getFragmentManager(),
				getString(R.string.ui_dialog_profile_photo));
		Log.d(Globals.TAG_MAIN_ACTIVITY, "MainActivity, onProfileChangeClick()");
	}

	/**
	 * Take the returned value of action and fire corresponding activity, calls
	 * helper method: takePhotoFromCamera() or takePhotoFromGallery()
	 * 
	 * @param item
	 *            : int value of action
	 */
	public void onDialogItemSeleced(int item) {

		switch (item) {
		case 0:
			takePhotoFromCamera();
			break;
		case 1:
			takePhotoFromGallery();
			break;
		}
	}

	/**
	 * Take a profile photo from camera
	 * 
	 * @param v
	 */
	private void takePhotoFromCamera() {
		// File a camera intent
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Construct temporary image path and name to save the taken
		// photo
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
		takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);

		try {

			startActivityForResult(takePictureIntent,
					Globals.REQUEST_CODE_TAKE_FROM_CAMERA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Take photo from gallery
	 */
	private void takePhotoFromGallery() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK, null);
		galleryIntent.setType("image/*");
		startActivityForResult(galleryIntent,
				Globals.REQUEST_CODE_TAKE_FROM_GALLERY);
	}

	/**
	 * Display the profile photo in activity Handle data after activity returns
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_TAKE_FROM_CAMERA:
			// mImageCaptureUri = data.getData();
			cropImage();
			break;
		case REQUEST_CODE_TAKE_FROM_GALLERY:
			// Get image from gallery for cropping
			mImageCaptureUri = data.getData();
			cropImage();
			break;
		case REQUEST_CODE_CROP_PHOTO:
			// Update image view after image crop
			Bundle extras = data.getExtras();
			// Set the picture image in UI
			if (extras != null) {
				// Set imageView
				mImageView
						.setImageBitmap((Bitmap) extras.getParcelable("data"));

				// Compress the bitmap image and save it to a byteArray
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Bitmap bmp = (Bitmap) extras.getParcelable("data");
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				mByteArray = stream.toByteArray();
			}

			break;
		}
	}

	/**
	 * Crop image
	 */
	private void cropImage() {
		// Use existing crop activity.
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, Globals.IMAGE_UNSPECIFIED);

		// Specify image size
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);

		// Specify aspect ratio, 1:1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);

		// REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
		// identify the activity in onActivityResult() when it returns
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

	/**
	 * Save image Call helper function saveProfileData
	 * 
	 * @param v
	 */
	public void onSaveClicked(View v) {
		// Call helper method
		saveProfileData();
		Toast.makeText(mContext, Globals.TOAST_SAVE_MESSAGE, Toast.LENGTH_SHORT)
				.show();

		// -------------------------------------------------------------------------------
		// GCM
		JSONArray jsonArray = new JSONArray();
		// Convert eventEntry to JSON object
		jsonArray.put(mUserEntry.toJSONObject());
		String msg = jsonArray.toString();
		// Call helper method to send msg to Google Cloud
		postMsg(msg);

		Intent intent = new Intent(mContext, MainActivity.class);
		startActivity(intent);

	}

	// ------------------------------------------------------------------------
	// GCM
	/**
	 * Post new user profile to Google Cloud
	 * 
	 * @param msg
	 *            : JSON string
	 */
	private void postMsg(String msg) {
		new AsyncTask<String, Void, ArrayList<UserEntry>>() {

			@Override
			protected ArrayList<UserEntry> doInBackground(String... arg0) {
				Log.d(Globals.TAG_CREATE_NEW_EVENT_ACTIVITY,
						"postMsg().doInBackground() got called");
				String url = Globals.SERVER_ADDR + "/post.do";
				ArrayList<UserEntry> res = new ArrayList<UserEntry>();
				Map<String, String> params = new HashMap<String, String>();
				params.put("post_text", arg0[0]);
				params.put("task_type", "create_new_user");
				try {
					res = ServerUtilities.postUser(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			protected void onPostExecute(ArrayList<UserEntry> res) {
				// mPostText.setText("");
				refreshPostHistory();
			}

		}.execute(msg);
		Log.d(Globals.TAG_CREATE_NEW_EVENT_ACTIVITY,
				"postMsg().doInBackground() got called");
	}

	/**
	 * Refresh event list
	 */
	private void refreshPostHistory() {
		new AsyncTask<Void, Void, ArrayList<UserEntry>>() {

			@Override
			protected ArrayList<UserEntry> doInBackground(Void... arg0) {
				// Call GetHistoryServlet on server side
				String url = Globals.SERVER_ADDR + "/get_user_history.do";
				ArrayList<UserEntry> res = new ArrayList<UserEntry>();
				Map<String, String> params = new HashMap<String, String>();
				params.put("task_type", "get_users");
				// Get ArrayList<EventEntry> from datastore
				try {
					res = ServerUtilities.postUser(url, params);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return res;
			}

			@Override
			/**
			 * Update the userEntry in the local database
			 */
			protected void onPostExecute(ArrayList<UserEntry> res) {
				if (!res.isEmpty() || res.size() != 0) {

					// Update database
					mUserEntryDbHelper.getWritableDatabase();
					mUserEntryDbHelper.removeAllEntries();
					for (UserEntry userEntry : res) {
						mUserEntryDbHelper.insertEntry(userEntry);
					}
					mUserEntryDbHelper.close();
				}
			}
		}.execute();
	}

	// -------------------------------------------------------------------------------------------------
	// UI
	/**
	 * Cancel the activity and return to parent activity
	 * 
	 * @param v
	 */
	public void onCancelClicked(View v) {
		Toast.makeText(mContext, Globals.TOAST_CANCEL_MESSAGE,
				Toast.LENGTH_SHORT).show();
		finish();
	}

	private void loadOtherUserProfile() {
		// Load user entry
		mUserEntryDbHelper.getReadableDatabase();
		UserEntry userEntry = new UserEntry();
		userEntry = mUserEntryDbHelper.fetchEntriesByEmail(mEmail);
		mUserEntryDbHelper.close();

		// Setup the photo
		mByteArray = userEntry.getProfilePhoto();
		if (mByteArray != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
			Bitmap bp = BitmapFactory.decodeStream(bis);
			mImageView.setImageBitmap(bp);
			mImageView.setClickable(false);
		}

		// Load the profile name
		String mValue = userEntry.getFirstName();
		((EditText) findViewById(R.id.editProfileFirstName)).setText(mValue);
		((EditText) findViewById(R.id.editProfileFirstName))
				.setFocusable(false);
		mValue = userEntry.getLastName();
		((EditText) findViewById(R.id.editProfileLastName)).setText(mValue);
		((EditText) findViewById(R.id.editProfileLastName)).setFocusable(false);

		// Load the profile email

		mValue = userEntry.getEmail();
		((EditText) findViewById(R.id.editProfileEmail)).setText(mValue);
		((EditText) findViewById(R.id.editProfileEmail)).setFocusable(false);

		// Load the profile gender
		int mIntValue = userEntry.getGender();
		// In case there isn't one saved before:
		if (mIntValue >= 0) {
			// Find the radio button that should be checked.
			RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.editProfileRadioGender))
					.getChildAt(mIntValue);
			// Check the button
			radioBtn.setChecked(true);
			radioBtn.setClickable(false);
			((RadioButton) findViewById(R.id.editProfileRadioGenderF))
					.setClickable(false);
		}

		// Load the profile class
		mValue = String.valueOf(userEntry.getClassYear());
		((EditText) findViewById(R.id.editProfileClass)).setText(mValue);
		((EditText) findViewById(R.id.editProfileClass)).setFocusable(false);

		// Load the profile major
		mValue = userEntry.getMajor();
		((EditText) findViewById(R.id.editProfileMajor)).setText(mValue);
		((EditText) findViewById(R.id.editProfileMajor)).setFocusable(false);

		// Load the profile bio
		mValue = userEntry.getBio();
		((EditText) findViewById(R.id.editProfileBio)).setText(mValue);
		((EditText) findViewById(R.id.editProfileBio)).setFocusable(false);

		((Button) findViewById(R.id.editProfileBtnSave))
				.setVisibility(View.GONE);
		((Button) findViewById(R.id.editProfileBtnCancel))
				.setVisibility(View.GONE);
	}

	/**
	 * Helper function to load previous saved image
	 */
	private void loadProfileData() {
		// When the phone rotates
		if (mByteArray != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(mByteArray);
			Bitmap bp = BitmapFactory.decodeStream(bis); // decode stream to
															// abitmap image
			mImageView.setImageBitmap(bp);

		}

		// Load and update all profile views

		// Get the shared preferences - create or retrieve the activity
		// preference object
		else {
			String mKey = getString(R.string.preference_name_edit_profile_activity);
			SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

			// Load the profile name
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_first_name);
			String mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileFirstName))
					.setText(mValue);
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_last_name);
			mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileLastName)).setText(mValue);

			// Load the profile email
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_email);
			mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileEmail)).setText(mValue);
			// if (!mValue.isEmpty()){
			// ((EditText)
			// findViewById(R.id.editProfileEmail)).setFocusable(false);
			// }

			// Load the profile gender
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_gender);
			int mIntValue = mPrefs.getInt(mKey, -1);
			// In case there isn't one saved before:
			if (mIntValue >= 0) {
				// Find the radio button that should be checked.
				RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.editProfileRadioGender))
						.getChildAt(mIntValue);
				// Check the button
				radioBtn.setChecked(true);
			}

			// Load the profile class
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_class);
			mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileClass)).setText(mValue);

			// Load the profile major
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_major);
			mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileMajor)).setText(mValue);

			// Load the profile bio
			mKey = getString(R.string.preference_key_edit_profile_activity_profile_bio);
			mValue = mPrefs.getString(mKey, "");
			((EditText) findViewById(R.id.editProfileBio)).setText(mValue);

			// Load the profile image from internal storage
			try {
				FileInputStream fis = openFileInput(getString(R.string.preference_key_edit_profile_photo_file_name));
				Bitmap bmap = BitmapFactory.decodeStream(fis);
				mImageView.setImageBitmap(bmap);
				// Convert bitmap to byte[]
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				mByteArray = stream.toByteArray();
				fis.close();
			} catch (IOException e) {
				// Default profile photo if no photo saved before.
				mImageView.setImageResource(R.drawable.icon_selfies);
			}
		}
	}

	/**
	 * Helper function to save the image
	 */
	private void saveProfileData() {
		// Getting the shared preferences editor
		String mKey = getString(R.string.preference_name_edit_profile_activity);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.clear();

		// Save profile first and last name information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_first_name);
		String mValue = (String) ((EditText) findViewById(R.id.editProfileFirstName))
				.getText().toString();
		mEditor.putString(mKey, mValue);
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_last_name);
		mValue = (String) ((EditText) findViewById(R.id.editProfileLastName))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		// Save profile Email information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_email);
		mValue = (String) ((EditText) findViewById(R.id.editProfileEmail))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		// Save profile gender information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_gender);
		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.editProfileRadioGender);
		int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
				.getCheckedRadioButtonId()));
		mEditor.putInt(mKey, mIntValue);

		// Save profile class information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_class);
		mValue = (String) ((EditText) findViewById(R.id.editProfileClass))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		// Save profile major information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_major);
		mValue = (String) ((EditText) findViewById(R.id.editProfileMajor))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		// Save profile bio information
		mKey = getString(R.string.preference_key_edit_profile_activity_profile_bio);
		mValue = (String) ((EditText) findViewById(R.id.editProfileBio))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		// Commit all the changes into preference file
		mEditor.commit();

		// Save profile image into internal storage.
		mImageView.buildDrawingCache();
		Bitmap bmap = mImageView.getDrawingCache();
		try {
			NewsFeedFragment.mHasProfileImage = true;
			FileOutputStream fos = openFileOutput(
					getString(R.string.preference_key_edit_profile_photo_file_name),
					MODE_PRIVATE);
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// Only for demo purpose, we save the profile to UserEntry and show it
		// in the Contacts
		mUserEntry = new UserEntry();

		try {
			mValue = (String) ((EditText) findViewById(R.id.editProfileFirstName))
					.getText().toString();
			mUserEntry.setFirstName(mValue);
		} catch (Exception e) {
			mValue = "";
			mUserEntry.setFirstName(mValue);
		}

		try {
			mValue = (String) ((EditText) findViewById(R.id.editProfileLastName))
					.getText().toString();

			mUserEntry.setLastName(mValue);
		} catch (Exception e) {
			mValue = "";
			mUserEntry.setLastName(mValue);
		}

		try {
			mValue = (String) ((EditText) findViewById(R.id.editProfileEmail))
					.getText().toString();
			mUserEntry.setEmail(mValue);
		} catch (Exception e) {
			mValue = "";
			mUserEntry.setEmail(mValue);
		}

		try {
			mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
					.getCheckedRadioButtonId()));
			mUserEntry.setGender(mIntValue);
		} catch (Exception e) {
			mIntValue = 0;
			mUserEntry.setGender(mIntValue);
		}

		try {
			mIntValue = Integer
					.parseInt((String) ((EditText) findViewById(R.id.editProfileClass))
							.getText().toString());
			mUserEntry.setClassYear(mIntValue);
		} catch (Exception e) {
			mIntValue = 1769;
			mUserEntry.setClassYear(mIntValue);
		}

		try {
			mValue = (String) ((EditText) findViewById(R.id.editProfileMajor))
					.getText().toString();
			mUserEntry.setMajor(mValue);
		} catch (Exception e) {
			mValue = "";
			mUserEntry.setMajor(mValue);
		}

		try {
			mValue = (String) ((EditText) findViewById(R.id.editProfileBio))
					.getText().toString();
			mUserEntry.setBio(mValue);
		} catch (Exception e) {
			mValue = "";
			mUserEntry.setBio(mValue);
		}

		mUserEntry.setProfilePhoto(mByteArray);

//		mUserEntryDbHelper.getWritableDatabase();
//		mUserEntryDbHelper.insertEntry(mUserEntry);
//		mUserEntryDbHelper.close();

	}

}
