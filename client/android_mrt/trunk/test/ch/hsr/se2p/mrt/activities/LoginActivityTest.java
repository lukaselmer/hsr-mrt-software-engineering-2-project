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
	private EditText editPasswort;
	private CheckBox checkbox;
	private Button loginBtn;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// getActivity().getHelper().reset();
		activity = getActivity();
		editEmail = (EditText) activity.findViewById(R.id.editEmail);
		editPasswort = (EditText) activity.findViewById(R.id.editPassword);
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
		assertEquals("", editPasswort.getText().toString());
		assertEquals(true, checkbox.isChecked());
	}

	public void testNoLoginDataSuppliedWithoutPreferencesSaved() {
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("");
				editPasswort.setText("");
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
				editPasswort.setText("");
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
				editPasswort.setText("1234");
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

	@UiThreadTest
	public void testLoginSuccessfulWithPreferencesSaved() {
		editEmail.setText("aarglos@mrt.ch");
		editPasswort.setText("1234");
		loginBtn.performClick();


		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is not showing
		// TODO: Save preferences successful
	}

	@UiThreadTest
	public void testLoginSuccessfulWithoutPreferencesSaved() {
		editEmail.setText("ttuechtig@mrt.ch");
		editPasswort.setText("");
		loginBtn.performClick();


		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is not showing
		// TODO: Preferences not saved
	}
	
	public void testLoginUnsuccessfulWithoutPreferenceSave() {
		assertFalse(solo.searchText("Bitte Emailadresse und Passwort angeben", true));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editEmail.setText("ttuechtig@mrt.ch");
				editPasswort.setText("");
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
		
		
		

		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is not showing
		// TODO: Preferences not saved
	}

	// AlertDialog.Builder b = new AlertDialog.Builder(ch.hsr.se2p.mrt.activities.LoginActivity);
	// b.setTitle("Fehler");
	// b.setMessage("Bitte Emailadresse und Passwort angeben!");

	// activity.runOnUiThread(new Runnable() {
	// public void run() {
	// loginBtn.performClick();
	// }
	// });

	// assertTrue(b.isShowing());

}
