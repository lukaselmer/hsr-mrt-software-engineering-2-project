package ch.hsr.se2p.mrt.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.util.Config;

public class Transmitter {
	private static final String TAG = Transmitter.class.getSimpleName();
	private String cookie;

	public boolean transmit(TimeEntry timeEntry) {
		// Log.d(TAG, "timeEntry: " + timeEntry.getId());
		// Log.d(TAG, "Hashcode: " + timeEntry.getHashcode());
		// Log.d(TAG, "Description: " + timeEntry.getDescription());
		// Log.d(TAG, "Start: " + timeEntry.getTimeStart());
		// Log.d(TAG, "Stop: " + timeEntry.getTimeStop());
		String ret = transmit(timeEntry.toJSONObject(), Config.TIME_ENTRY_CREATE_URL);
		try {
			JSONObject readObject = new JSONObject(ret);
			int id = readObject.optJSONObject("time_entry").getInt("id");
			String hashcode = readObject.optJSONObject("time_entry").getString("hashcode");
			if (timeEntry.getHashcode().equals(hashcode) && id != 0) {
				// Everything is fine, it worked! Now lets get rid of the hashcode!
				timeEntry.setRailsId(id);
				return true;
			}
			return true;
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		}
		return false;
	}

	public boolean confirm(TimeEntry timeEntry) {
		String ret = transmit(timeEntry.toJSONObject(), String.format(Config.TIME_ENTRY_REMOVE_HASH_CODE_URL, timeEntry.getRailsId()));
		JSONObject readObject;
		int id = 0;
		try {
			readObject = new JSONObject(ret);
			id = readObject.optJSONObject("time_entry").getInt("id");
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		}
		return id == timeEntry.getRailsId();
	}

	private String transmit(JSONObject j, String url) {
		try {
			return doPost(url, j);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String doPost(String url, JSONObject c) throws ClientProtocolException, IOException {

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 3000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		HttpPost post = new HttpPost(url);
		JSONObject holder = new JSONObject();

		try {
			holder.put("time_entry", c);
			Log.e("TimeEntry JSON", "TimeEntry JSON = " + holder.toString());
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			post.setHeader("Content-Type", "application/json");
		} catch (UnsupportedEncodingException e) {
			Log.e("Error", "" + e, e);
			e.printStackTrace();
		} catch (JSONException js) {
			js.printStackTrace();
		}

		Log.e(TAG, "Executing HTTP Reququest");

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("ClientProtocol", "" + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IO", "" + e);
		}

		// Log.e(TAG, "Headers: " + response.getAllHeaders());
		Log.e(TAG, "Readming answer");

		HttpEntity entity = response.getEntity();
		// if (entity != null) {
		// try {
		// entity.consumeContent();
		// } catch (IOException e) {
		// Log.e("IO E", "" + e);
		// e.printStackTrace();
		// }
		// }

		Log.e(TAG, "Answer length is: " + entity.getContentLength());
		String responseString = EntityUtils.toString(entity);
		Log.e(TAG, "Answer is: " + responseString);

		return responseString;

		// int TIMEOUT_MILLISEC = 10000; // = 10 seconds
		// HttpParams httpParams = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		// HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		// HttpClient client = new DefaultHttpClient(httpParams);
		//
		// HttpPost request = new HttpPost(url);
		// request.setEntity(new ByteArrayEntity(
		// c.toString().getBytes("UTF8")));
		// HttpResponse response = client.execute(request);
		//
		// return response;

		// DefaultHttpClient httpclient = new DefaultHttpClient();
		// HttpPost httpost = new HttpPost(url);
		//
		// JSONObject holder = new JSONObject();
		// try {
		// holder.put("time_entry", c.toString());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		//
		// StringEntity se = new StringEntity(holder.toString());
		// httpost.setEntity(se);
		// httpost.setHeader("Accept", "application/json");
		// httpost.setHeader("Content-type", "application/json");
		//
		// ResponseHandler responseHandler = new BasicResponseHandler();
		// return httpclient.execute(httpost, responseHandler);

		// HttpClient httpclient = new DefaultHttpClient();
		// HttpPost request = new HttpPost(url);
		// StringEntity e = new StringEntity(c.toString());
		// e.setContentEncoding("UTF-8");
		// e.setContentType("application/json");
		// request.setEntity(e);
		// return httpclient.execute(request);
	}
	// private String httpRequest(String url_string, boolean post) {
	// String myString = null;
	// try {
	// URL url = new URL(url_string);
	// HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	// if (post) {
	// urlConn.setRequestMethod("POST");
	// }
	// if (cookie != null) {
	// urlConn.addRequestProperty("cookie", cookie);
	// }
	// InputStream is = urlConn.getInputStream();
	// BufferedInputStream bis = new BufferedInputStream(is);
	// ByteArrayBuffer baf = new ByteArrayBuffer(50);
	// int current = 0;
	// while ((current = bis.read()) != -1) {
	// baf.append((byte) current);
	// }
	// myString = new String(baf.toByteArray());
	// String c = urlConn.getHeaderField("set-cookie");
	// if (c != null) {
	// cookie = c;
	// }
	// } catch (Exception e) {
	// myString = e.getMessage();
	// }
	// return myString;
	// }
}
