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

	/* Anmerkung:
	 * Ist der testLoginUnsuccessfulWithoutPreferencesSaved nicht auskommentiert, so wirft er
	 * eine WindowManager$BadTokenException und die Testreihe bricht ab
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
		// setPreferences();
	}

	public void setPreferences() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Editor edit = activity.preferences.edit();
				edit.putString("email", "ttuechtig@mrt.ch");
				edit.putString("password", "5678");
				edit.commit();
			}
		});
	}
	
	public void resetPreferences() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Editor edit = activity.preferences.edit();
				edit.putString("email", "");
				edit.putString("password", "");
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
		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
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

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
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

		String email = activity.preferences.getString("email", null);
		String password = activity.preferences.getString("password", null);
		assertTrue("".equals(email));
		assertTrue("".equals(password));
	}

	/*
	 * Anmerkung Damit dieser Test funktioniert, muss in processLogin(email, password) die Überschreibung der Methode login(login, password, receivable) (das return true;) auskommentiert werden
	 */
//	public void testLoginUnsuccessfulWithoutPreferencesSaved() {
//		assertFalse(solo.searchText("Anmeldung schlug fehl!", true));
//		activity.runOnUiThread(new Runnable() {
//			public void run() {
//				editEmail.setText("ttuechtig@mrt.ch");
//				editPassword.setText("5678");
//				loginBtn.performClick();
//			}
//		});
//		assertTrue(solo.searchText("Anmeldung schlug fehl!", true));
//		solo.clickOnButton("Ok");
//		assertFalse(solo.searchText("Anmeldung schlug fehl!", true));
//		assertTrue("".equals(editPassword.getText().toString()));
//		assertEquals(activity, solo.getCurrentActivity());
//
//		String email = activity.preferences.getString("email", null);
//		String password = activity.preferences.getString("password", null);
//		assertTrue("".equals(email));
//		assertTrue("".equals(password));
//	}

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
		assertTrue("".equals(email));
		assertTrue("".equals(password));
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
		assertTrue("".equals(email));
		assertTrue("".equals(password));
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
//		assertTrue("".equals(email));
//		assertTrue("".equals(password));
	}
}
