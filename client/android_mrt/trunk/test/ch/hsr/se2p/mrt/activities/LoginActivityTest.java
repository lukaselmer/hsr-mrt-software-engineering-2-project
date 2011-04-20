package ch.hsr.se2p.mrt.activities;

import com.jayway.android.robotium.solo.Solo;

import ch.hsr.se2p.mrt.R;
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
	}

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
		
		// TODO: Save preferences successful
		//Problem: das Passwort wird aus dem EditText-Feld "gelöscht", darum kann es nicht aus editPassword gelesen werden
		editPassword.setText("1234");
		
		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertEquals(editEmail.getText().toString(), email);
		assertEquals(editPassword.getText().toString(), password);
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
		
		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertFalse("".equals(email));
		assertFalse("".equals(password));
	}

	public void testLoginUnsuccessfulWithoutPreferencesSaved() {
		assertFalse(solo.searchText("Anmeldung schlug fehl!", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("ttuechtig@mrt.ch");
				editPassword.setText("5678");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText("Anmeldung schlug fehl!", true));
		solo.clickOnButton("Ok");
		assertFalse(solo.searchText("Anmeldung schlug fehl!", true));
		assertTrue("".equals(editPassword.getText().toString()));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertFalse("".equals(email));
		assertFalse("".equals(password));
	}

	public void testNoLoginDataSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPassword.setText("");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		solo.clickOnButton("Ok");
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertFalse("".equals(email));
		assertFalse("".equals(password));
	}

	public void testNoPasswordSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("aarglos@mrt.ch");
				editPassword.setText("");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		solo.clickOnButton("Ok");
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertFalse("".equals(email));
		assertFalse("".equals(password));
	}

	public void testNoEmailSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPassword.setText("1234");
				loginBtn.performClick();
			}
		});
		assertTrue(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		solo.clickOnButton("Ok");
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		assertEquals(activity, solo.getCurrentActivity());

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertFalse("".equals(email));
		assertFalse("".equals(password));
	}
}
