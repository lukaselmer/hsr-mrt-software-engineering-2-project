package ch.hsr.se2p.mrt.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.application.MRTApplication;

public class MRTApplicationTest extends AndroidTestCase {

	private MRTApplication mrtApp;
	private static final String EMAIL = "field_worker@mrt.ch", PASSWORD = "mrt";

	@Override
	protected void setUp() throws Exception {
		mrtApp = new MRTApplication();
		super.setUp();
	}

	public void testInitialMRTApplication() {
		assertNotNull(mrtApp.getHttpHelper());
		assertNotNull(mrtApp.getCurrentUser());
		assertNull(mrtApp.getEmail());
		assertNull(mrtApp.getPassword());
		assertNull(mrtApp.getPreferences());
	}

	public void testLoginWithoutCredentials() {
		mrtApp.login(EMAIL, PASSWORD, false);
		assertEquals(EMAIL, mrtApp.getEmail());
		assertEquals(PASSWORD, mrtApp.getPassword());
	}

	public void testLoginWithCredentials() {
		setPreferencesAndLogin(true);
		SharedPreferences sp = mrtApp.getPreferences();
		assertEquals(EMAIL, sp.getString("email", null));
		assertEquals(PASSWORD, sp.getString("password", null));
	}

	public void testLogout() {
		setPreferencesAndLogin(false);
		mrtApp.logout();
		assertNull(mrtApp.getEmail());
		assertNull(mrtApp.getPassword());
	}

	private void setPreferencesAndLogin(boolean saveCredentials) {
		mrtApp.setPreferences(PreferenceManager.getDefaultSharedPreferences(this.getContext()));
		mrtApp.login(EMAIL, PASSWORD, saveCredentials);
	}

}
