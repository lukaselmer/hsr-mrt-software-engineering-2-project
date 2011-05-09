package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.models.User;
import ch.hsr.se2p.mrt.network.HttpHelper;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
/**
 * MRTApplication operates as the main application and also saves the login preferences if wanted.
 */
public class MRTApplication extends Application {
	private final HttpHelper httpHelper;
	private String email, password;
	private final User currentUser;
	private SharedPreferences preferences;
	private LocationManager locationManager;

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

	public void login(String email, String password, boolean saveCredentials) {
		this.email = email;
		this.password = password;
		if (saveCredentials && preferences != null) {
			Editor edit = preferences.edit();
			edit.putString("email", this.email);
			edit.putString("password", this.password);
			edit.commit();
		}
	}

	public void logout() {
		email = null;
		password = null;
		if (preferences != null){
			Editor edit = preferences.edit();
			edit.remove("email");
			edit.remove("password");
			edit.commit();	
		}
	}

	public String getPassword() {
		return password;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
		email = preferences.getString("email", null);
		password = preferences.getString("password", null);
	}

	public boolean mayLogin() {
		return email != null && password != null;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	@Override
	public String toString() {
		return "Email: " + email + ", Password: " + password + ", Current user: " + currentUser + ", Cookie: " + httpHelper.getCookie();
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}
}
