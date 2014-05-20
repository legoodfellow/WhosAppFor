package edu.dartmouth.cs.whosupfor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import edu.dartmouth.cs.whosupfor.util.MyDialogFragment;

public class NewsFeedFragment extends Fragment {

	private ImageView mImageView;
	private Context mContext;
	private Button mBtnFilter, mBtnPost;
	public static boolean mHasProfileImage = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for start fragment
		View view = inflater.inflate(R.layout.news_feed_fragment, container,
				false);

		mContext = getActivity();

		// Update imageView;
		mImageView = (ImageView) view
				.findViewById(R.id.imageNewsFeedFragmentProfile);

		try {
			FileInputStream fis = mContext
					.openFileInput(getString(R.string.preference_key_edit_profile_photo_file_name));
			Bitmap bmap = BitmapFactory.decodeStream(fis);
			mImageView.setImageBitmap(bmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Assign button info of Start and Sync
		mBtnFilter = (Button) view.findViewById(R.id.btnNewsFeedFragmentFilter);
		mBtnPost = (Button) view
				.findViewById(R.id.btnNewsFeedFragmentPostEvent);
		setUpFilterBtn();
		setUpPostBtn();

		return view;

	}

	/**
	 * Helper method to setup post button listener
	 */
	private void setUpPostBtn() {
		// Set up Start button and Sync button listeners
		mBtnPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						SelectNewEventTypeActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Helper method to setup filter button listener
	 */
	private void setUpFilterBtn() {
		// Set up Start button and Sync button listeners
		mBtnFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment fragment = MyDialogFragment
						.newInstance(R.string.ui_news_feed_fragment_btn_filter);
				fragment.show(getActivity().getFragmentManager(),
						getString(R.string.ui_news_feed_fragment_btn_filter));
			}
		});
	}

}
