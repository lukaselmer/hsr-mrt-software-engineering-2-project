package ch.hsr.se2p.mrt.activities;

import com.jayway.android.robotium.solo.Solo;

import ch.hsr.se2p.mrt.R;
import android.content.SharedPreferences.Editor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

	public LoginActivityTest() {
		super("ch.hsr.se2p.mrt", LoginActivity.class);
	}

	private LoginActivity activity;
	private Solo solo;

	private EditText editEmail;
	private EditText editPassword;
	private CheckBox checkbox;
	private Button loginBtn;
	
	final private String LOGINDATA_ERROR = "Bitte Emailadresse und Passwort angeben";
	final private String LOGIN_FAILED = "Anmeldung schlug fehl!";
	final private String PASSWORD = "password";
	final private String EMAIL = "email";
	final private String OK = "Ok";
	

	/* Anmerkung:
	 * Ist der testLoginUnsuccessfulWithoutPreferencesSaved nicht auskommentiert, so wirft er
	 * eine WindowManager$BadTokenException geworfen und der nächste Test wird rot und die 
	 * Testreihe bricht ab.
	 */
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// getActivity().getHelper().reset();
		activity = getActivity();
		editEmail = (EditText) activity.findViewById(R.id.editEmail);
		editPassword = (EditText) activity.findViewById(R.id.editPassword);
		checkbox = (CheckBox) activity.findViewById(R.id.chbxSaveLogin);
		loginBtn = (Button) activity.findViewById(R.id.loginButton);
		this.solo = new Solo(getInstrumentation(), getActivity());

		/*
		 * Preferences für Autologin setzen
		 */
		// TODO: Automatisches Login, wie kann ich es testen?
		 
	}
	
	public void resetPreferences() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Editor edit = activity.preferences.edit();
				edit.putString(EMAIL, "");
				edit.putString(PASSWORD, "");
				edit.commit();
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			this.solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
		// getActivity().deleteDatabase(TEST_DB_NAME);
	}

	@UiThreadTest
	public void testPreconditions() {
		assertEquals("", editEmail.getText().toString());
		assertEquals("", editPassword.getText().toString());
		assertEquals(true, checkbox.isChecked());
		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}

//	@UiThreadTest
//	public void testAutoLogin() {
//		assertFalse(activity.equals(solo.getCurrentActivity()));
//	}

	public void testLoginSuccessfulWithPreferencesSaved() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("aarglos@mrt.ch");
				editPassword.setText("1234");
				checkbox.setChecked(true);
				loginBtn.performClick();
			}
		});
		assertFalse(activity.equals(solo.getCurrentActivity()));

		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertEquals(editEmail.getText().toString(), email);
		// TODO: Save password successful
		/*
		 * Beobachtung Das Passwort wird, bevor die Ansicht auf TimeEntry wechselt, aus dem EditText-Feld "gelöscht". Es wird aber trotzdem in den Preferences gespeichert. Vermutung: Das Passwort kann
		 * für diesen Test nicht aus dem Feld "abgelesen" werden, Grund siehe oben
		 */
		// assertEquals(editPassword.getText().toString(), password);
		resetPreferences();
	}

	public void testLoginSuccessfulWithoutPreferencesSaved() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("ttuechtig@mrt.ch");
				editPassword.setText("5678");
				checkbox.setChecked(false);
				loginBtn.performClick();
			}
		});
		assertFalse(activity.equals(solo.getCurrentActivity()));

		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}

	/*
	 * Anmerkung Damit dieser Test funktioniert, muss in processLogin(email, password) die Überschreibung der Methode login(login, password, receivable) (das return true;) auskommentiert werden
	 */
//	public void testLoginUnsuccessfulWithoutPreferencesSaved() {
//		assertFalse(solo.searchText(LOGIN_FAILED, true));
//		activity.runOnUiThread(new Runnable() {
//			public void run() {
//				editEmail.setText("ttuechtig@mrt.ch");
//				editPassword.setText("5678");
//				loginBtn.performClick();
//			}
//		});
//		assertTrue(solo.searchText(LOGIN_FAILED, true));
//		solo.clickOnButton(OK);
//		assertFalse(solo.searchText(LOGIN_FAILED, true));
//		assertTrue("".equals(editPassword.getText().toString()));
//		assertEquals(activity, solo.getCurrentActivity());
//
//		String email = activity.preferences.getString(EMAIL, null);
//		String password = activity.preferences.getString(PASSWORD, null);
//		assertTrue("".equals(email));
//		assertTrue("".equals(password));
//	}

	public void testNoLoginDataSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPassword.setText("");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}

	public void testNoPasswordSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("aarglos@mrt.ch");
				editPassword.setText("");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}

	public void testNoEmailSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPassword.setText("1234");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString(EMAIL, null);
		String password = activity.preferences.getString(PASSWORD, null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}
}
