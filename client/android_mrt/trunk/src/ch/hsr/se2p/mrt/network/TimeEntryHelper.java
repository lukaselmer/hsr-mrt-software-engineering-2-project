package ch.hsr.se2p.mrt.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Confirmable;
import ch.hsr.se2p.mrt.interfaces.Transmittable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.GpsPosition;
import ch.hsr.se2p.mrt.models.TimeEntryType;

public class TimeEntryHelper {
	protected HttpHelper httpHelper;

	public TimeEntryHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public boolean transmit(Transmittable transmittable) {
		if (transmittable.isTransmitted())
			return true;
		try {
			String ret = httpHelper.doHttpPost(getJSONParameters(transmittable.toJSONObject()), NetworkConfig.TIME_ENTRY_CREATE_URL);
			return transmittable.processTransmission(new JSONObject(ret));
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		}
		return false;
	}
	
	public boolean transmit(Transmittable transmittable, GpsPosition position, Customer customer, TimeEntryType timeEntryType) {
		if (transmittable.isTransmitted())
			return true;
		try {
			JSONObject j = transmittable.toJSONObject();
			
			if (position != null) j.put("gps_position_data", position.toJSONObject());
			if (customer != null) j.put("customer_id", customer.getIdOnServer());
			if (timeEntryType != null) j.put("time_entry_type_id", timeEntryType.getIdOnServer());
			
			String ret = httpHelper.doHttpPost(getJSONParameters(j), NetworkConfig.TIME_ENTRY_CREATE_URL);
			return transmittable.processTransmission(new JSONObject(ret));
		} catch (JSONException e) {
			// Request failed, pass
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
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
