package edu.dartmouth.cs.whosupfor.util;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import edu.dartmouth.cs.whosupfor.EditProfileActivity;
import edu.dartmouth.cs.whosupfor.CreateNewEventActivity;
import edu.dartmouth.cs.whosupfor.R;

public class MyDialogFragment extends DialogFragment {

	private Calendar mDateAndTime = Calendar.getInstance();
	
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
		// final EditText input = new EditText(getActivity());

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

		case Globals.PICK_DATE:
			DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mDateAndTime.set(Calendar.YEAR, year);
					mDateAndTime.set(Calendar.MONTH, monthOfYear);
					mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					((CreateNewEventActivity) getActivity())
							.onDateSave(mDateAndTime);
				}
			};
			return new DatePickerDialog(getActivity(), mDateListener,
					mDateAndTime.get(Calendar.YEAR),
					mDateAndTime.get(Calendar.MONTH),
					mDateAndTime.get(Calendar.DAY_OF_MONTH));

			// Time
		case Globals.PICK_TIME:
			TimePickerDialog.OnTimeSetListener mTimeListener = new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mDateAndTime.set(Calendar.HOUR, hourOfDay);
					mDateAndTime.set(Calendar.MINUTE, minute);
					((CreateNewEventActivity) getActivity())
							.onTimeSave(mDateAndTime);
				}
			};
			return new TimePickerDialog(getActivity(), mTimeListener,
					mDateAndTime.get(Calendar.HOUR),
					mDateAndTime.get(Calendar.MINUTE), false);
		default:
			return null;
		}
	}
}
