package edu.dartmouth.cs.whosupfor.menu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

	private UserEntry mUserEntry;
	private UserEntryDbHelper mUserEntryDbHelper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);

		// Get imageView and context
		mImageView = (ImageView) findViewById(R.id.editProfileImage);
		mContext = getApplicationContext();

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
		saveProfileData();
		Toast.makeText(mContext, Globals.TOAST_SAVE_MESSAGE, Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(mContext, MainActivity.class);
		startActivity(intent);

	}

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
		
		// Only for demo purpose, we save the profile to UserEntry and show it in the Contacts
		mUserEntry = new UserEntry();
		
		mValue = (String) ((EditText) findViewById(R.id.editProfileFirstName))
				.getText().toString();
		mUserEntry.setFirstName(mValue);
		
		mValue = (String) ((EditText) findViewById(R.id.editProfileLastName))
				.getText().toString();
		mUserEntry.setFirstName(mValue);
		
		mValue = (String) ((EditText) findViewById(R.id.editProfileEmail))
				.getText().toString();
		mUserEntry.setEmail(mValue);
		
		mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
				.getCheckedRadioButtonId()));
		mUserEntry.setGender(mIntValue);
		
		mIntValue = Integer.parseInt((String)((EditText) findViewById(R.id.editProfileClass))
				.getText().toString());
		mUserEntry.setClassYear(mIntValue);
		
		mValue = (String) ((EditText) findViewById(R.id.editProfileMajor))
				.getText().toString();
		mUserEntry.setMajor(mValue);
		
		mValue = (String) ((EditText) findViewById(R.id.editProfileBio))
				.getText().toString();
		mUserEntry.setBio(mValue);
		
		mUserEntry.setProfilePhoto(mByteArray);
		
		mUserEntryDbHelper = new UserEntryDbHelper(this);
		mUserEntryDbHelper.insertEntry(mUserEntry);
		mUserEntryDbHelper.close();
		
	}

}
