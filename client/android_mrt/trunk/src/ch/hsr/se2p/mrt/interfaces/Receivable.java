package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface Receivable {
	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean fromJSON(JSONObject jsonObject) throws JSONException;
}
