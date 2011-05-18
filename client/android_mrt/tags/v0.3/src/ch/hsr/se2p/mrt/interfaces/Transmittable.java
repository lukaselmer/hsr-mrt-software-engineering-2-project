package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Enables the implementing class to transfer objects to the server. Makes sure, that the object is saved only once on the server.
 */
public interface Transmittable {
	/**
	 * Creates a new JSONObject, initializes it.
	 * @return The newly created object
	 */
	public JSONObject toJSONObject();

	/**
	 * @return Successfully transmitted
	 */
	public boolean isTransmitted();

	/**
	 * Compares the hashcodes on the client to the server side. If they match, the hashcode on the server is removed. Additionally the Id on the server is set.
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean processTransmission(JSONObject jsonObject) throws JSONException;
}
