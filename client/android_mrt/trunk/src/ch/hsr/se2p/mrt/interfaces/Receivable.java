package ch.hsr.se2p.mrt.interfaces;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public interface Receivable {
	public Integer getIdOnServer();

	public Timestamp getUpdatedAt();

	/**
	 * @param jsonObject
	 *            the object obtained by the request
	 * @return whether response was valid or not
	 * @throws JSONException
	 */
	public boolean fromJSON(JSONObject jsonObject) throws JSONException;
}
