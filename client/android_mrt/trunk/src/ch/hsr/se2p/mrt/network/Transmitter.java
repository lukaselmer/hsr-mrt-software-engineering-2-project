package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.util.Config;

public class Transmitter {
	private static final int HTTP_TIMEOUT_IN_MILLISECONDS = 3000;
	private static final String TAG = Transmitter.class.getSimpleName();
	private String cookie;

	public boolean transmit(TimeEntry timeEntry) {
		if (timeEntry.isTransmitted())
			return true;
		try {
			String ret = transmit(timeEntry.toJSONObject(), Config.TIME_ENTRY_CREATE_URL);
			JSONObject readObject = new JSONObject(ret);
			int id = readObject.optJSONObject("time_entry").getInt("id");
			String hashcode = readObject.optJSONObject("time_entry").getString("hashcode");
			if (timeEntry.getHashcode().equals(hashcode) && id != 0) {
				// Everything is fine, it worked! Now lets get rid of the hashcode!
				timeEntry.setRailsId(id);
				return true;
			}
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		}
		return false;
	}

	public boolean confirm(TimeEntry timeEntry) {
		int id = 0;
		try {
			String ret = transmit(timeEntry.toJSONObject(), String.format(Config.TIME_ENTRY_REMOVE_HASH_CODE_URL, timeEntry.getRailsId()));
			JSONObject readObject;

			readObject = new JSONObject(ret);
			id = readObject.optJSONObject("time_entry").getInt("id");
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		}
		return id == timeEntry.getRailsId();
	}

	private String transmit(JSONObject j, String url) throws IOException {
		return doPost(url, j);
	}

	private String doPost(String url, JSONObject c) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient(getHttpParams());

		HttpPost post = new HttpPost(url);
		JSONObject holder = new JSONObject();

		Log.i(TAG, "Starting HTTP Reququest: " + url);
		try {
			holder.put("time_entry", c);
			Log.i(TAG, "Parameter time_entry = " + holder.toString());
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			post.setHeader("Content-Type", "application/json");
		} catch (UnsupportedEncodingException e) {
			Log.e("Error", "" + e, e);
			e.printStackTrace();
		} catch (JSONException js) {
			js.printStackTrace();
		}

		if (cookie != null)
			post.addHeader("cookie", cookie);

		Log.i(TAG, "Executing HTTP request");

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			Log.d("ClientProtocol", "ClientProtocolException when executing request", e);
			return null;
		} catch (IOException e) {
			Log.d(TAG, "IOException when executing request", e);
			throw e;
		}

		Log.i(TAG, "HTTP request finished");
		HttpEntity entity = response.getEntity();
		Log.i(TAG, "Server answer length is: " + entity.getContentLength());
		String responseString = EntityUtils.toString(entity);
		Log.i(TAG, "Server answer is: " + responseString);

		Header h = response.getFirstHeader("set-cookie");
		if (h != null)
			cookie = h.getValue();

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

	protected HttpParams getHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT_IN_MILLISECONDS);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT_IN_MILLISECONDS);
		return httpParams;
	}
}
