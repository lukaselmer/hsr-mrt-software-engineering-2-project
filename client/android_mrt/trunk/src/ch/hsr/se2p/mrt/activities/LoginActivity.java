package ch.hsr.se2p.mrt.activities;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;

import android.app.ProgressDialog;
import android.content.Context;
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
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.network.UserHelper;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	private EditText editEmail;
	private EditText editPassword;
	private CheckBox saveLogin;

	SharedPreferences preferences;
	private MRTApplication mrtApplication;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mrtApplication = (MRTApplication) getApplication();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		saveLogin = (CheckBox) findViewById(R.id.chbxSaveLogin);

//		checkIfAvailablePreferencesForAutoLogin();

		Button loginBtn = (Button) findViewById(R.id.loginButton);
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkLoginData();
			}
		});
	}

//	private void checkIfAvailablePreferencesForAutoLogin() {
//		String email = preferences.getString("email", null);
//		String password = preferences.getString("password", null);
//		processLogin(email, password);
//	}
	
	protected void checkLoginData() {
		if (editEmail.getText().length() > 0 && editPassword.getText().length() > 0) {
			processLogin(editEmail.getText().toString(), editPassword.getText().toString());
		} else {
			ActivityHelper.displayAlertDialog("Fehler", "Bitte Emailadresse und Passwort angeben!", LoginActivity.this);
		}
	}

	private void processLogin(String email, String password) {
		if (new UserHelper(mrtApplication.getHttpHelper()) 
		{
			public boolean login(String login, String password, ch.hsr.se2p.mrt.interfaces.Receivable receivable) {
				return true;
			};	
		}
		.login(email, password, mrtApplication.getCurrentUser())) {
			saveLoginData(email, password);
			ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);
			startNewActivity();
		}
		ActivityHelper.displayAlertDialog(null, "Anmeldung schlug fehl!", this);
		editPassword.setText("");
	}

	private void saveLoginData(String email, String password) {
		if (saveLogin.isChecked()) {
			saveLoginData();
		}
	}

//	private void showLoginData() {
//		String email = preferences.getString("email", null);
//		String password = preferences.getString("password", null);
//		ActivityHelper.displayAlertDialog(null, "Email: " + email + " Passwort: " + password, this);
//	}

	private void saveLoginData() {
		Editor edit = preferences.edit();
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		edit.putString("email", email);
		edit.putString("password", password);
		edit.commit();
	}
	
	private void startNewActivity() {
		Intent intent = new Intent(LoginActivity.this, TimeEntryActivity.class);
		this.startActivity(intent);
		finish();
	}
}