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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import edu.dartmouth.cs.whosupfor.CreateNewEventActivity;
import edu.dartmouth.cs.whosupfor.R;
import edu.dartmouth.cs.whosupfor.menu.EditProfileActivity;

public class MyDialogFragment extends DialogFragment {

	private CharSequence[] items = { "Friends", "CS65", "Public" };
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

		TimePickerDialog.OnTimeSetListener mTimeListener;
		DatePickerDialog.OnDateSetListener mDateListener;

		switch (title) {

		// Profile change image
		case R.string.ui_dialog_profile_photo:

			// Build picture picker dialog for choosing from camera or gallery
			return new AlertDialog.Builder(getActivity())
					.setTitle(title)
					.setItems(
							R.array.ui_edit_profile_activity_dialog_profile_image_array,
							new OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									((EditProfileActivity) getActivity())
											.onDialogItemSeleced(arg1);
								}
							}).create();

			// Filter
		case R.string.ui_news_feed_fragment_btn_filter:
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.fragment_filter_dialog, null);

			// Build picture picker dialog for choosing from camera or gallery
			return new AlertDialog.Builder(getActivity())
					.setView(view)
					.setTitle(title)
					// .setMultiChoiceItems(items,
					// new boolean[] { false, false, false },
					// new DialogInterface.OnMultiChoiceClickListener() {
					// public void onClick(DialogInterface arg0,
					// int arg1, boolean arg2) {
					// // TODO Auto-generated method stub
					//
					// }
					// })
					// .setMultiChoiceItems(items,
					// new boolean[] { false, false, false },
					// new DialogInterface.OnMultiChoiceClickListener() {
					// public void onClick(DialogInterface arg0,
					// int arg1, boolean arg2) {
					// // TODO Auto-generated method stub
					//
					// }
					// })
					.setPositiveButton(R.string.ui_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.ui_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();

			// Pick start date
		case R.string.ui_dialog_pick_start_date:
			mDateListener = new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mDateAndTime.set(Calendar.YEAR, year);
					mDateAndTime.set(Calendar.MONTH, monthOfYear);
					mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					((CreateNewEventActivity) getActivity()).onStartDateSet(
							mDateAndTime.get(Calendar.YEAR),
							mDateAndTime.get(Calendar.MONTH),
							mDateAndTime.get(Calendar.DAY_OF_MONTH));
				}
			};
			return new DatePickerDialog(getActivity(), mDateListener,
					mDateAndTime.get(Calendar.YEAR),
					mDateAndTime.get(Calendar.MONTH),
					mDateAndTime.get(Calendar.DAY_OF_MONTH));

			// Pick start time
		case R.string.ui_dialog_pick_start_time:
			mTimeListener = new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mDateAndTime.set(Calendar.HOUR, hourOfDay);
					mDateAndTime.set(Calendar.MINUTE, minute);
					((CreateNewEventActivity) getActivity()).onStartTimeSet(
							hourOfDay, minute);
				}
			};
			return new TimePickerDialog(getActivity(), mTimeListener,
					mDateAndTime.get(Calendar.HOUR_OF_DAY),
					mDateAndTime.get(Calendar.MINUTE), false);

			// Pick start date
		case R.string.ui_dialog_pick_end_date:
			mDateListener = new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mDateAndTime.set(Calendar.YEAR, year);
					mDateAndTime.set(Calendar.MONTH, monthOfYear);
					mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					((CreateNewEventActivity) getActivity()).onEndDateSet(
							mDateAndTime.get(Calendar.YEAR),
							mDateAndTime.get(Calendar.MONTH),
							mDateAndTime.get(Calendar.DAY_OF_MONTH));
				}
			};
			return new DatePickerDialog(getActivity(), mDateListener,
					mDateAndTime.get(Calendar.YEAR),
					mDateAndTime.get(Calendar.MONTH),
					mDateAndTime.get(Calendar.DAY_OF_MONTH));

			// Pick start time
		case R.string.ui_dialog_pick_end_time:
			mTimeListener = new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mDateAndTime.set(Calendar.HOUR, hourOfDay);
					mDateAndTime.set(Calendar.MINUTE, minute);
					((CreateNewEventActivity) getActivity()).onEndTimeSet(
							hourOfDay, minute);
				}
			};
			return new TimePickerDialog(getActivity(), mTimeListener,
					mDateAndTime.get(Calendar.HOUR_OF_DAY),
					mDateAndTime.get(Calendar.MINUTE), false);
		default:
			return null;
		}
	}
}
