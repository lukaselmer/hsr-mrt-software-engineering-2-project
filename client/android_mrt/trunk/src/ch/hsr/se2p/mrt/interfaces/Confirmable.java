package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface Confirmable {
	public JSONObject toJSONObject();

	public int getIdOnServer();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean processConfirmation(JSONObject jsonObject) throws JSONException;
}
