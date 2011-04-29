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
	private MRTApplication testedApplication;
	private Solo solo;

	private EditText editEmail, editPassword;
	private CheckBox checkbox;
	private Button loginBtn;

	final static private String LOGINDATA_ERROR = "Bitte Emailadresse und Passwort angeben", 
	LOGIN_ERROR = "Anmeldung schlug fehl!", PASSWORD = "password", EMAIL = "email", OK = "Ok",
	EMAIL_FIELD_WORKER = "field_worker@mrt.ch", PASSWORD_MRT = "mrt";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		testedApplication = (MRTApplication) activity.getApplication();

		resetPreferences();
		retrieveWidgetsById();

		this.solo = new Solo(getInstrumentation(), activity);

		// TODO: Automatisches Login, wie kann ich es testen?
	}

	private void retrieveWidgetsById() {
		editEmail = (EditText) activity.findViewById(R.id.editEmail);
		editPassword = (EditText) activity.findViewById(R.id.editPassword);
		checkbox = (CheckBox) activity.findViewById(R.id.chbxSaveLogin);
		loginBtn = (Button) activity.findViewById(R.id.loginButton);
	}

	public void resetPreferences() {
		testedApplication.logout();
		Editor editor = testedApplication.getPreferences().edit();
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
		activity.finish();
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
		assertLoginDataIsNull();
	}

	public void testZLoginSuccessfulWithPreferencesSaved() {
		resetPreferences();

		fillInLoginForm(EMAIL_FIELD_WORKER, PASSWORD_MRT, true);
		performClickOnLoginButton();

		assertFalse(activity.equals(solo.getCurrentActivity()));

		assertEquals(EMAIL_FIELD_WORKER, testedApplication.getEmail());
		assertEquals(PASSWORD_MRT, testedApplication.getPassword());
		assertEquals(testedApplication.getEmail(), testedApplication.getPreferences().getString(EMAIL, null));
		assertEquals(testedApplication.getPassword(), testedApplication.getPreferences().getString(PASSWORD, null));

		resetPreferences();
	}

	public void testZLoginSuccessfulWithoutPreferencesSaved() {
		resetPreferences();

		fillInLoginForm(EMAIL_FIELD_WORKER, PASSWORD_MRT, false);
		performClickOnLoginButton();

		assertFalse(activity.equals(solo.getCurrentActivity()));

		assertNotNull(testedApplication.getEmail());
		assertNotNull(testedApplication.getPassword());
		assertNull(testedApplication.getPreferences().getString(EMAIL, null));
		assertNull(testedApplication.getPreferences().getString(PASSWORD, null));

		resetPreferences();
	}

	public void testLoginUnsuccessfulWithoutPreferencesSaved() {
		resetPreferences();
		assertFalse(solo.searchText(LOGIN_ERROR, true));

		fillInLoginForm("wrong@mrt.ch", "credentials", false);
		performClickOnLoginButton();

		assertTrue(solo.searchText(LOGIN_ERROR, true));
		solo.clickOnButton(OK);

		assertFalse(solo.searchText(LOGIN_ERROR, true));
		assertTrue("".equals(editPassword.getText().toString()));
		assertEquals(activity, solo.getCurrentActivity());
		assertLoginDataIsNull();
	}

	public void testNoLoginDataSuppliedWithoutPreferencesSaved() {
		resetPreferences();
		assertLoginDataErrorIsNotDisplayed();

		fillInLoginForm("", "", false);
		performClickOnLoginButton();

		assertLoginDataErrorIsDisplayed();
		solo.clickOnButton(OK);

		assertLoginDataErrorIsNotDisplayed();
		assertEquals(activity, solo.getCurrentActivity());

		assertLoginDataIsNull();
	}

	public void testNoPasswordSuppliedWithoutPreferencesSaved() {
		resetPreferences();
		assertLoginDataErrorIsNotDisplayed();

		fillInLoginForm(EMAIL_FIELD_WORKER, "", false);
		performClickOnLoginButton();

		assertLoginDataErrorIsDisplayed();
		solo.clickOnButton(OK);

		assertLoginDataErrorIsNotDisplayed();
		assertEquals(activity, solo.getCurrentActivity());

		assertLoginDataIsNull();
	}

	public void testNoEmailSuppliedWithoutPreferencesSaved() {
		resetPreferences();
		assertLoginDataErrorIsNotDisplayed();

		fillInLoginForm("", PASSWORD_MRT, false);
		performClickOnLoginButton();

		assertLoginDataErrorIsDisplayed();
		solo.clickOnButton(OK);

		assertLoginDataErrorIsNotDisplayed();
		assertEquals(activity, solo.getCurrentActivity());

		assertLoginDataIsNull();
	}

	private void assertLoginDataErrorIsNotDisplayed() {
		assertFalse(solo.searchText(LOGINDATA_ERROR, true));
	}

	private void assertLoginDataErrorIsDisplayed() {
		assertTrue(solo.searchText(LOGINDATA_ERROR, true));
	}

	private void assertLoginDataIsNull() {
		assertNull(testedApplication.getEmail());
		assertNull(testedApplication.getPassword());
	}

	private void fillInLoginForm(final String email, final String password, final boolean bool) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText(email);
				editPassword.setText(password);
				checkbox.setChecked(bool);
			}
		});
	}

	private void performClickOnLoginButton() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				loginBtn.performClick();
			}
		});
	}
}
