package edu.dartmouth.cs.whosupfor;

import java.io.File;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;

public class EditProfileActivity extends Activity {

	private ImageView mImageView;
	private Uri mImageCaptureUri;
	private byte[] mByteArray;

	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.edit_profile, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * Display a dialog when clicking on the user profile image
	 * 
	 * @param v
	 */
	public void onImageClicked(View v) {
		DialogFragment fragment = MyDialogFragment
				.newInstance(R.string.ui_main_activity_dialog_profile_photo);
		fragment.show(getFragmentManager(),
				getString(R.string.ui_main_activity_dialog_profile_photo));
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

}
