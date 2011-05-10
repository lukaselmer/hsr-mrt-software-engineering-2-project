package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface Transmittable {
	public JSONObject toJSONObject();

	public boolean isTransmitted();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean processTransmission(JSONObject jsonObject) throws JSONException;
}
