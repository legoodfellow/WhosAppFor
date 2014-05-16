package edu.dartmouth.cs.whosupfor.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.EditText;
import edu.dartmouth.cs.whosupfor.EditProfileActivity;
import edu.dartmouth.cs.whosupfor.MainActivity;
import edu.dartmouth.cs.whosupfor.R;

public class MyDialogFragment extends DialogFragment {

	/**
	 * Method to create various dialogs, call helper method: onCreateDialog
	 * 
	 * @param title
	 *            : as the key of the action to be performed
	 * @return
	 */
	public static MyDialogFragment newInstance(int title) {

		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Value of action to be performed
		int title = getArguments().getInt("title");
//		final EditText input = new EditText(getActivity());

		switch (title) {
		// Profile change
		case R.string.ui_main_activity_dialog_profile_photo:

			// Build picture picker dialog for choosing from camera or gallery
			return new AlertDialog.Builder(getActivity())
					.setTitle(title)
					.setItems(
							R.array.ui_main_activity_dialog_profile_image_array,
							new OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									((EditProfileActivity) getActivity())
											.onDialogItemSeleced(arg1);
								}
							}).create();
		default:
			return null;
		}
	}
}
