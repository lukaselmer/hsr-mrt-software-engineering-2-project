package ch.hsr.se2p.mrt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.application.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.network.UserHelper;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

/**
 * Performs the user login. Requires email and password. Preferences saving is available. Shows warnings when data is incomplete or incorrect.
 */
public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	private EditText editEmail;
	private EditText editPassword;
	private CheckBox chbxSaveLogin;

	private MRTApplication mrtApplication;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initApplication();
		initData();
		checkPreferencesForAutoLogin();

		createClickListener((Button) findViewById(R.id.loginButton));
	}

	private void initApplication() {
		mrtApplication = (MRTApplication) getApplication();
		mrtApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));
	}

	private void initData() {
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		chbxSaveLogin = (CheckBox) findViewById(R.id.chbxSaveLogin);
	}

	private void createClickListener(Button loginBtn) {
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				checkLoginData();
			}
		});
	}

	private void checkPreferencesForAutoLogin() {
		if (mrtApplication.mayLogin()) {
			processLogin(mrtApplication.getEmail(), mrtApplication.getPassword());
		}
	}

	private void checkLoginData() {
		if (editEmail.getText().length() > 0 && editPassword.getText().length() > 0) {
			processLogin(editEmail.getText().toString(), editPassword.getText().toString());
		} else {
			ActivityHelper.displayAlertDialog("Fehler", "Bitte Emailadresse und Passwort angeben!", LoginActivity.this);
		}
	}

	private void processLogin(String email, String password) {
		if (new UserHelper(mrtApplication.getHttpHelper()).login(email, password, mrtApplication.getCurrentUser())) {
			mrtApplication.login(email, password, chbxSaveLogin.isChecked());
			switchToTimeEntryActivity();
		} else {
			ActivityHelper.displayAlertDialog(null, "Anmeldung schlug fehl!", this);
			editPassword.setText("");
		}
	}

	private void switchToTimeEntryActivity() {
		this.startActivity(new Intent(this, TimeEntryActivity.class));
		finish();
	}
}