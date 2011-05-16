package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Confirms a time entry on the server. Makes sure, that object is transmitted successfully and can be removed on client.
 */
public interface Confirmable {
	public JSONObject toJSONObject();

	/**
	 * @return Id set on the server.
	 */
	public int getIdOnServer();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean processConfirmation(JSONObject jsonObject) throws JSONException;
}
