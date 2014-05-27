package edu.dartmouth.cs.whosupfor.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import edu.dartmouth.cs.whosupfor.data.EventEntry;
import edu.dartmouth.cs.whosupfor.util.Globals;

public class ServerUtilities {
	private static final String TAG = "DemoAndroid";

	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	public static void sendRegistrationIdToBackend(Context context, String regId) {
		String serverUrl = Globals.SERVER_ADDR + "/register";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);

		// Once GCM returns a registration id, we need to register it in the
		// server. As the server might be down, we will retry it a couple
		// times.
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		for (int i = 1; i <= ServerUtilities.MAX_ATTEMPTS; i++) {
			try {
				ServerUtilities.post(serverUrl, params);
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Thread.currentThread().interrupt();
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		Log.d(TAG,
				"ServerUtilities.java, sendRegistrationIdToBackend(),  called ");

	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	// public static String post(String endpoint, Map<String, String> params)
	// throws IOException {
	// Log.d(TAG,
	// "ServerUtilities.java, post(),  called ");
	// URL url;
	//
	// try {
	// url = new URL(endpoint);
	// } catch (MalformedURLException e) {
	// throw new IllegalArgumentException("invalid url: " + endpoint);
	// }
	//
	// StringBuilder bodyBuilder = new StringBuilder();
	// Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	//
	// // constructs the POST body using the parameters
	// while (iterator.hasNext()) {
	// Entry<String, String> param = iterator.next();
	// bodyBuilder.append(param.getKey()).append('=')
	// .append(param.getValue());
	// if (iterator.hasNext()) {
	// bodyBuilder.append('&');
	// }
	// }
	//
	// String body = bodyBuilder.toString();
	// byte[] bytes = body.getBytes();
	// HttpURLConnection conn = null;
	//
	// try {
	// conn = (HttpURLConnection) url.openConnection();
	// conn.setDoOutput(true);
	// conn.setUseCaches(false);
	// conn.setFixedLengthStreamingMode(bytes.length);
	// conn.setRequestMethod("POST");
	// conn.setRequestProperty("Content-Type",
	// "application/x-www-form-urlencoded;charset=UTF-8");
	// // post the request
	// OutputStream out = conn.getOutputStream();
	// out.write(bytes);
	// out.close();
	// // handle the response
	// int status = conn.getResponseCode();
	// if (status != 200) {
	// throw new IOException("Post failed with error code " + status);
	// }
	//
	// // Get Response
	// InputStream is = conn.getInputStream();
	// BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	// String line;
	// StringBuffer response = new StringBuffer();
	//
	// while ((line = rd.readLine()) != null) {
	// response.append(line);
	// response.append('\n');
	// }
	// rd.close();
	// return response.toString();
	//
	// } finally {
	// if (conn != null) {
	// conn.disconnect();
	// }
	// }
	// }

	public static ArrayList<EventEntry> post(String endpoint,
			Map<String, String> params) throws IOException {
		Log.d(TAG, "ServerUtilities.java, post(),  called ");
		URL url;

		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}

		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}

			// Get Response
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			// StringBuffer response = new StringBuffer();
			String finalString = "";

			while ((line = rd.readLine()) != null) {
				// response.append(line);
				// response.append('\n');
				finalString += line;
			}
			rd.close();
			
			ArrayList<EventEntry> eventEntries = new ArrayList<EventEntry>();

			try {
				eventEntries = parseStringToEventEntry(finalString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return eventEntries;

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * Helper method to parse string value to json object to eventEntry object
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<EventEntry> parseStringToEventEntry(String result)
			throws JSONException {
		ArrayList<EventEntry> eventEntries = new ArrayList<EventEntry>();
		JSONObject jsonObj;

		// Convert string to json array
		JSONArray jsonArray = new JSONArray(result);

		// Convert json object to eventEntry object
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObj = jsonArray.getJSONObject(i);
			try {
				EventEntry eventEntry = new EventEntry();
				eventEntry.fromJSONObject(jsonObj);
				eventEntries.add(eventEntry);
			} catch (Exception e) {

			}
		}

		return eventEntries;
	}
}
