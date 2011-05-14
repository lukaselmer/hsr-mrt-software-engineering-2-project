package ch.hsr.se2p.mrt.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ch.hsr.se2p.mrt.interfaces.Confirmable;
import ch.hsr.se2p.mrt.interfaces.Transmittable;

public class TimeEntryHelper {
	private static final String TAG = TimeEntryHelper.class.getSimpleName();

	protected HttpHelper httpHelper;

	public TimeEntryHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public boolean transmit(Transmittable transmittable) {
		if (transmittable.isTransmitted())
			return true;
		try {
			JSONObject j = transmittable.toJSONObject();
			String ret = httpHelper.doHttpPost(getJSONParameters(j), NetworkConfig.TIME_ENTRY_CREATE_URL);
			return transmittable.processTransmission(new JSONObject(ret));
		} catch (Exception e) {
			// Request failed, pass
			Log.w(TAG, "Request failed", e);
		}
		return false;
	}

	private Object getJSONParameters(JSONObject jsonObject) throws JSONException {
		return new JSONObject().put("time_entry", jsonObject);
	}

	public boolean confirm(Confirmable confirmable) {
		try {
			String ret = httpHelper.doHttpPost(confirmable.toJSONObject(),
					String.format(NetworkConfig.TIME_ENTRY_CONFIRM_URL, confirmable.getIdOnServer()));
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
}
