package ch.hsr.se2p.mrt.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

public class UserHelper {
	protected HttpHelper httpHelper;

	public UserHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public boolean login(String login, String password, Receivable receivable) {
		try {
			String ret = httpHelper.doHttpPost(generateJSONObject(login, password), NetworkConfig.LOGIN_URL);
			return receivable.fromJSON(new JSONObject(ret));
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		} catch (JSONException e) {
			// Request failed, pass
		}
		return false;
	}

	protected JSONObject generateJSONObject(String login, String password) throws JSONException {
		JSONObject j = new JSONObject();
		j.put("login", login);
		j.put("password", password);
		return j;
	}
}
