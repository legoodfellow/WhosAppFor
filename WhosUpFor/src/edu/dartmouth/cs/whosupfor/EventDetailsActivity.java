package edu.dartmouth.cs.whosupfor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import edu.dartmouth.cs.whosupfor.util.Globals;
import edu.dartmouth.cs.whosupfor.util.Utils;

public class EventDetailsActivity extends Activity {

	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		
		mContext = this;
		
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			

			((EditText) findViewById(R.id.eventDetailActivityTextType))
					.setText(Utils.getEventType(
							extras.getInt(Globals.KEY_EVENT_TYPE)));

			
		}
	}

	

}
