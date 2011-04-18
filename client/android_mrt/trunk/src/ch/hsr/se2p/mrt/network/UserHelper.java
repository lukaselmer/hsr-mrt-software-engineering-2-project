package ch.hsr.se2p.mrt.network;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

public class UserHelper {
	protected HttpTransmitter httpTransmitter;

	public UserHelper(HttpTransmitter httpTransmitter) {
		this.httpTransmitter = httpTransmitter;
	}

	public boolean login(String login, String password, Receivable receivable) {
		try {
			System.out.println("1XXXXXXXXXXXXXXXXXXXX");
			JSONObject j = new JSONObject();
			System.out.println("2XXXXXXXXXXXXXXXXXXXX");
			j.put("login", login);
			System.out.println("3XXXXXXXXXXXXXXXXXXXX");
			j.put("password", password);
			System.out.println("4XXXXXXXXXXXXXXXXXXXX");
			String ret = httpTransmitter.doHttpPost(j, NetworkConfig.LOGIN_URL);
			System.out.println("5XXXXXXXXXXXXXXXXXXXX");
			System.out.println("RET = " + ret);
			return receivable.fromJSON(new JSONObject(ret));
		} catch (NullPointerException e) {
			// Request failed, pass
			e.printStackTrace();
		} catch (IOException e) {
			// Request failed, pass
			e.printStackTrace();
		} catch (JSONException e) {
			// Request failed, pass
			e.printStackTrace();
		}
		return false;
	}
}
