package ch.hsr.se2p.mrt.activities;

import com.jayway.android.robotium.solo.Solo;

import ch.hsr.se2p.mrt.R;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

	public LoginActivityTest() {
		super("ch.hsr.se2p.mrt", LoginActivity.class);
	}

	private LoginActivity activity;
	private Solo solo;

	private EditText editUsername;
	private EditText editPasswort;
	private CheckBox checkbox;
	private Button loginBtn;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// getActivity().getHelper().reset();
		activity = getActivity();
		editUsername = (EditText) activity.findViewById(R.id.editUsername);
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

	public void testPreconditions() {
		assertNull(editUsername);
		assertNull(editPasswort);
		assertEquals(true, checkbox.isChecked());
	}

	@UiThreadTest
	public void testLoginSuccessfulWithPreferenceSave() {
		editUsername.setText("");
		editPasswort.setText("");
		loginBtn.performClick();

		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is not showing
		// TODO: Save preferences successful
	}

	@UiThreadTest
	public void testLoginSuccessfulWithoutPreferenceSave() {
		editUsername.setText("");
		editPasswort.setText("");
		loginBtn.performClick();

		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is not showing
		// TODO: Preferences not saved
	}

	@UiThreadTest
	public void testNoLoginDataSupplied() {
		editUsername.setText("");
		editPasswort.setText("");
		loginBtn.performClick();

		// TODO: Test if dialog is showing
		// TODO: Click on dialog
		// TODO: Check if main window is showing

	}

	@UiThreadTest
	public void testNoUsernameSupplied() {
		editUsername.setText("");
		editPasswort.setText("");
		loginBtn.performClick();
		assertTrue(true);
	}

	@UiThreadTest
	public void testNoPasswordSupplied() {
		editUsername.setText("");
		editPasswort.setText("");
		loginBtn.performClick();
		assertTrue(true);
	}

	@UiThreadTest
	public void testBla() {
		assertTrue(true);
	}

	// @Override
	// protected void tearDown() throws Exception {
	// // TODO Auto-generated method stub
	// super.tearDown();
	// }

	// AlertDialog.Builder b = new AlertDialog.Builder(ch.hsr.se2p.mrt.activities.LoginActivity);
	// b.setTitle("Fehler");
	// b.setMessage("Bitte Benutzernamen und Passwort angeben!");

	// activity.runOnUiThread(new Runnable() {
	// public void run() {
	// loginBtn.performClick();
	// }
	// });

	// assertNull(editUsername);
	// assertNull(editPasswort);
	// assertTrue(b.isShowing());

}
