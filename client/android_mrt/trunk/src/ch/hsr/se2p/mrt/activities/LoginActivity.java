package ch.hsr.se2p.mrt.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.network.UserHelper;

public class LoginActivity extends Activity {

	private EditText editUsername;
	private EditText editPassword;
	private CheckBox saveLogin;

	SharedPreferences preferences;
	private MRTApplication mrtApplication;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mrtApplication = (MRTApplication) getApplication();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
		saveLogin = (CheckBox) findViewById(R.id.cBsaveLogin);

		checkIfAvailablePreferences();
		
		setContentView(R.layout.settings);
		
		Button loginBtn = (Button) findViewById(R.id.loginButton);
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editUsername.getText().length() > 0 && editPassword.getText().length() > 0) {
					processLogin(editUsername.getText().toString(), editPassword.getText().toString());
				} else {
					ActivityHelper.displayAlertDialog("Fehler", "Bitte Benutzernamen und Passwort angeben!", LoginActivity.this);
				}
			}
		});
	}

	private void checkIfAvailablePreferences() {
		String username = preferences.getString("username", "n/a");
		String password = preferences.getString("password", "n/a");
		processLogin(username, password);
	}

	private void startNewActivity() {
		Intent intent = new Intent(LoginActivity.this, TimeEntryActivity.class);
		this.startActivity(intent);
		finish();
	}

	private void processLogin(String username, String password) {
		// ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);
		if (new UserHelper(mrtApplication.getHttpHelper()) {
			public boolean login(String login, String password, ch.hsr.se2p.mrt.interfaces.Receivable receivable) {
				return true;
			};
		}.login(username, password, mrtApplication.getCurrentUser())) {
			saveLoginData(username, password);
			//ActivityHelper.displayAlertDialog("Willkommen " + user.getFirstName() + " " + user.getLastName(), "Anmeldung war erfolgreich.", this);
			ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);		
			startNewActivity();
		}
		ActivityHelper.displayAlertDialog(null, "Anmeldung schlug fehl!", this);
		editPassword.setText("");
	}

	private void saveLoginData(String username, String password) {
		if (saveLogin.isChecked()) {
			saveLoginData();
		}
	}

	private void showLoginData() {
		String username = preferences.getString("username", "n/a");
		String password = preferences.getString("password", "n/a");
		ActivityHelper.displayAlertDialog(null, "Username: " + username + " Passwort: " 
				+ password, this);
	}
	
	private void saveLoginData() {
		Editor edit = preferences.edit();
		String username = editUsername.getText().toString();
		String password = editPassword.getText().toString();
		edit.putString("username", username);
		edit.putString("password", password);
		edit.commit();
	}
}