package ch.hsr.se2p.mrt.models;

import android.test.AndroidTestCase;

public class UserSettingsTest extends AndroidTestCase {

	private UserSettings settings;
	private static final String WORKER_EMAIL = "field_worker@mrt.ch";
	private static final String PASSWORD = "mrt";
	
	public void testIntialUserSettings(){
		settings = new UserSettings(1, WORKER_EMAIL, PASSWORD);
		assertEquals(new Integer(1), settings.getUserId());
		assertEquals(WORKER_EMAIL, settings.getEmail());
		assertEquals(PASSWORD, settings.getPassword());
	}
}
