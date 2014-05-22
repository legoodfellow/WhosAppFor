package edu.dartmouth.cs.whosupfor.util;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;

public class Utils {
	// From 1970 epoch time in seconds to something like "10/24/2012"
		public static String parseTime(long timeInMs, Context context) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(timeInMs);
			SimpleDateFormat dateFormat;
			dateFormat = new SimpleDateFormat(Globals.DATE_FORMAT, Locale.getDefault());

			return dateFormat.format(calendar.getTime());
		}
}
