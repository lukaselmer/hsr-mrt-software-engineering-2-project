package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import ch.hsr.se2p.mrt.interfaces.Confirmable;
import ch.hsr.se2p.mrt.interfaces.Transmittable;

public class HttpTransmitter {
	private static final int HTTP_TIMEOUT_IN_MILLISECONDS = 30000;
	private static final String TAG = HttpTransmitter.class.getSimpleName();
	private String cookie;

	public boolean transmit(Transmittable transmittable) {
		if (transmittable.isTransmitted())
			return true;
		try {
			String ret = transmit(transmittable.toJSONObject(), NetworkConfig.TIME_ENTRY_CREATE_URL);
			if (transmittable.processTransmission(new JSONObject(ret))) {
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

	public boolean confirm(Confirmable confirmable) {
		try {
			String ret = transmit(confirmable.toJSONObject(), String.format(NetworkConfig.TIME_ENTRY_CONFIRM_URL, confirmable.getIdOnServer()));
			return confirmable.processConfirmation(new JSONObject(ret));
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		}
		return false;
	}

	protected String transmit(JSONObject jsonObject, String url) throws IOException {
		return doHttpRequest(url, jsonObject, getHttpPost(url));
	}

	protected HttpEntityEnclosingRequestBase getHttpPost(String url) {
		return new HttpPost(url);
	}

	protected String doHttpRequest(String url, JSONObject jsonObject, HttpEntityEnclosingRequestBase httpRequest) throws IOException {
		HttpClient client = getHttpClient();
		JSONObject holder = new JSONObject();
		prepareRequest(url, jsonObject, httpRequest, holder);
		HttpResponse response = executeRequest(client, httpRequest);
		String responseString = handleResponse(response);
		return responseString;
	}

	protected HttpClient getHttpClient() {
		return new DefaultHttpClient(getHttpParams());
	}

	protected HttpParams getHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT_IN_MILLISECONDS);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT_IN_MILLISECONDS);
		return httpParams;
	}

	protected void prepareRequest(String url, JSONObject jsonObject, HttpEntityEnclosingRequestBase httpRequest, JSONObject holder) {
		Log.i(TAG, "Starting HTTP Reququest: " + url);
		try {
			holder.put("time_entry", jsonObject);
			Log.i(TAG, "Parameter time_entry = " + holder.toString());
			StringEntity se = new StringEntity(holder.toString());
			httpRequest.setEntity(se);
			httpRequest.setHeader("Content-Type", "application/json");
		} catch (UnsupportedEncodingException e) {
			Log.e("Error", "" + e, e);
			e.printStackTrace();
		} catch (JSONException js) {
			js.printStackTrace();
		}

		if (cookie != null)
			httpRequest.addHeader("cookie", cookie);
	}

	protected HttpResponse executeRequest(HttpClient client, HttpEntityEnclosingRequestBase httpRequest) throws IOException {
		HttpResponse response = null;
		Log.i(TAG, "Executing HTTP request");

		try {
			response = client.execute(httpRequest);
		} catch (ClientProtocolException e) {
			Log.d("ClientProtocol", "ClientProtocolException when executing request", e);
			return null;
		} catch (IOException e) {
			Log.d(TAG, "IOException when executing request", e);
			throw e;
		}
		return response;
	}

	protected String handleResponse(HttpResponse response) throws IOException {
		Log.i(TAG, "HTTP request finished");
		HttpEntity entity = response.getEntity();
		Log.i(TAG, "Server answer length is: " + entity.getContentLength());
		String responseString = EntityUtils.toString(entity);
		Log.i(TAG, "Server answer is: " + responseString);

		Header h = response.getFirstHeader("set-cookie");
		if (h != null)
			cookie = h.getValue();
		return responseString;
	}

}
