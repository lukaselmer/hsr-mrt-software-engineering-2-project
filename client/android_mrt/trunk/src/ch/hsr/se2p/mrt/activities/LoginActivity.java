package ch.hsr.se2p.mrt.activities;

import android.app.Activity;
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
import ch.hsr.se2p.mrt.models.User;
import ch.hsr.se2p.mrt.network.HttpHelper;
import ch.hsr.se2p.mrt.network.UserHelper;

public class LoginActivity extends Activity {

	private EditText editUsername;
	private EditText editPassword;
	private CheckBox saveLogin;
	private User user;

	SharedPreferences preferences;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		user = new User();

		editUsername = (EditText) findViewById(R.id.editUsername);
		editPassword = (EditText) findViewById(R.id.editPassword);
		saveLogin = (CheckBox) findViewById(R.id.cBsaveLogin);

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

	private void processLogin(String username, String password) {
		// ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);
		if (new UserHelper(HttpHelper.inst()) {
			public boolean login(String login, String password, ch.hsr.se2p.mrt.interfaces.Receivable receivable) {
				return true;
			};
		}.login(username, password, user)) {
			saveLoginData(username, password);
			ActivityHelper.displayAlertDialog("Willkommen " + user.getFirstName() + " " + user.getLastName(), 
					"Anmeldung war erfolgreich.", this);
			// wechsle Ansicht
			return;
		}
		ActivityHelper.displayAlertDialog(null, "Anmeldung schlug fehl!", this);
		editPassword.setText("");
	}

	private void saveLoginData(String username, String password) {
		if (saveLogin.isChecked()) {
			saveLoginData();
		}
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