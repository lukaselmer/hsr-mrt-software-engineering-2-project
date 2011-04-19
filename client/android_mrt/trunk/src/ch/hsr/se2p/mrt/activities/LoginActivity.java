package ch.hsr.se2p.mrt.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.database.DatabaseHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}
	
	private EditText editUsername;
	private EditText editPassword;
	private CheckBox saveLogin;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

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
					displayDialog("Fehler","Bitte Benutzernamen und Passwort angeben!");
	            }
			}
		});
	}

	private void processLogin(String username, String password) {
		//ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);
		boolean bool = false;  //doLogin(username, password)
		if (bool) {
			saveLoginData(username, password);
			displayDialog("Willkommen", "Anmeldung war erfolgreich.");
			//wechsle Ansicht
			return;
		}
		displayDialog(null, "Anmeldung schlug fehl!");
		editPassword.setText("");
	}
	
	private void saveLoginData(String username, String password) {
		if (saveLogin.isChecked()) {
			
		} else {
			
		}
	}

	private void displayDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this)
			.setMessage(message)
			.setTitle(title)
			.setPositiveButton("OK", null)
			.create();
		alertDialog.show();
	}
}