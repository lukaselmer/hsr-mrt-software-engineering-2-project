package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.network.HttpHelper;
import android.app.Application;

public class MRTApplication extends Application {
	private HttpHelper httpHelper;
	private String login, password;

	public MRTApplication() {
		httpHelper = new HttpHelper();
	}

	public HttpHelper getHttpHelper() {
		return httpHelper;
	}

	public void setHttpHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
