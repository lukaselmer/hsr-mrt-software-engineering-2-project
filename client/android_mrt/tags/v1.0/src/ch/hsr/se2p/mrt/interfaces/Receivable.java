package ch.hsr.se2p.mrt.interfaces;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides a method to transfer objects from the server to the client.
 */
public interface Receivable {
	/**
	 * @return Id set on the server
	 */
	public int getIdOnServer();

	/**
	 * @return Time of last modification as Timestamp
	 */
	public Timestamp getUpdatedAt();

	/**
	 * The obtained object contains all eligible attributes.
	 * 
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean fromJSON(JSONObject jsonObject) throws JSONException;
}