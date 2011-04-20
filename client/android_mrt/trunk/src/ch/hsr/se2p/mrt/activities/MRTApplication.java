package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.models.User;
import ch.hsr.se2p.mrt.network.HttpHelper;
import android.app.Application;

public class MRTApplication extends Application {
	private final HttpHelper httpHelper;
	private String email, password;
	private final User currentUser;

	public MRTApplication() {
		httpHelper = new HttpHelper();
		currentUser = new User();
	}

	public HttpHelper getHttpHelper() {
		return httpHelper;
	}

	public String getEmail() {
		return email;
	}

	public void setCredentials(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
