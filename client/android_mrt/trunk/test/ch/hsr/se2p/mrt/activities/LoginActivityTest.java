package ch.hsr.se2p.mrt.activities;

import android.content.SharedPreferences.Editor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import ch.hsr.se2p.mrt.R;

import com.jayway.android.robotium.solo.Solo;

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

	/*
	 * Anmerkung: Ist der testLoginUnsuccessfulWithoutPreferencesSaved nicht auskommentiert, so wirft er eine WindowManager$BadTokenException und der
	 * nächste Test wird rot und die Testreihe bricht ab.
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// getActivity().getHelper().reset();
		activity = getActivity();
		resetPreferences();
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

	private MRTApplication getApplication(LoginActivity activity) {
		return (MRTApplication) activity.getApplication();
	}

	public void resetPreferences() {
		getApplication(activity).logout();
		Editor editor = getApplication(activity).getPreferences().edit();
		editor.clear();
		editor.commit();
	}

	@Override
	protected void tearDown() throws Exception {
		resetPreferences();
		try {
			this.solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	public void testAReset() {
		resetPreferences();
	}

	@UiThreadTest
	public void testPreconditions() {
		resetPreferences();
		assertEquals(0, editEmail.getText().length());
		assertEquals(0, editPassword.getText().length());
		assertEquals(true, checkbox.isChecked());
		String email = getApplication(activity).getEmail();
		String password = getApplication(activity).getPassword();
		assertNull(email);
		assertNull(password);
	}

	// @UiThreadTest
	// public void testAutoLogin() {
	// assertFalse(activity.equals(solo.getCurrentActivity()));
	// }

	public void testZLoginSuccessfulWithPreferencesSaved() {
		resetPreferences();
		final String email = "field_worker@mrt.ch", password = "mrt";
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText(email);
				editPassword.setText(password);
				checkbox.setChecked(true);
				loginBtn.performClick();
			}
		});
		assertFalse(activity.equals(solo.getCurrentActivity()));

		assertEquals(email, getApplication(activity).getEmail());
		assertEquals(password, getApplication(activity).getPassword());
		assertEquals(getApplication(activity).getEmail(), getApplication(activity).getPreferences().getString(EMAIL, null));
		assertEquals(getApplication(activity).getPassword(), getApplication(activity).getPreferences().getString(PASSWORD, null));

		resetPreferences();
	}

	public void testZLoginSuccessfulWithoutPreferencesSaved() {
		resetPreferences();
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("field_worker@mrt.ch");
				editPassword.setText("mrt");
				checkbox.setChecked(false);
				loginBtn.performClick();
			}
		});
		assertFalse(activity.equals(solo.getCurrentActivity()));

		assertNotNull(getApplication(activity).getEmail());
		assertNotNull(getApplication(activity).getPassword());
		assertNull(getApplication(activity).getPreferences().getString(EMAIL, null));
		assertNull(getApplication(activity).getPreferences().getString(PASSWORD, null));
		resetPreferences();
	}

	public void testLoginUnsuccessfulWithoutPreferencesSaved() {
		resetPreferences();
		assertFalse(solo.searchText(LOGIN_FAILED, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("wrong@mrt.ch");
				editPassword.setText("credentials");
				loginBtn.performClick();
			}
		});
		// solo.sleep(20000);
		assertTrue(solo.searchText(LOGIN_FAILED, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGIN_FAILED, true));
		assertTrue("".equals(editPassword.getText().toString()));
		assertEquals(activity, solo.getCurrentActivity());

		assertNull(getApplication(activity).getEmail());
		assertNull(getApplication(activity).getPassword());
	}

	public void testNoLoginDataSuppliedWithoutPreferencesSaved() {
		resetPreferences();
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

		assertNull(getApplication(activity).getEmail());
		assertNull(getApplication(activity).getPassword());
	}

	public void testNoPasswordSuppliedWithoutPreferencesSaved() {
		resetPreferences();
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("field_worker@mrt.ch");
				editPassword.setText("");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		assertEquals(activity, solo.getCurrentActivity());

		assertNull(getApplication(activity).getEmail());
		assertNull(getApplication(activity).getPassword());
	}

	public void testNoEmailSuppliedWithoutPreferencesSaved() {
		resetPreferences();
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPassword.setText("mrt");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
		solo.clickOnButton(OK);
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
		assertEquals(activity, solo.getCurrentActivity());

		assertNull(getApplication(activity).getEmail());
		assertNull(getApplication(activity).getPassword());
	}
}
