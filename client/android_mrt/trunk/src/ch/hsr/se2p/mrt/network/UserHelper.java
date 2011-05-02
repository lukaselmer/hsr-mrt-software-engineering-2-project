package ch.hsr.se2p.mrt.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.interfaces.Receivable;

public class UserHelper {
	protected HttpHelper httpHelper;

	public UserHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public boolean login(MRTApplication mrtApplication) {
		return login(mrtApplication.getEmail(), mrtApplication.getPassword(), mrtApplication.getCurrentUser());
	}

	public boolean login(String email, String password, Receivable receivable) {
		try {
			String ret = httpHelper.doHttpPost(generateJSONObject(email, password), NetworkConfig.LOGIN_URL);
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

	protected JSONObject generateJSONObject(String email, String password) throws JSONException {
		JSONObject j = new JSONObject(), u = new JSONObject();
		u.put("email", email);
		u.put("password", password);
		u.put("remember_me", "1");
		j.put("user", u);
		return j;
	}

}
