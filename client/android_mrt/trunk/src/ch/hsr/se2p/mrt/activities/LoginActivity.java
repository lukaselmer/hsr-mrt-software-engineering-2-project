package ch.hsr.se2p.mrt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		Button loginBtn = (Button) findViewById(R.id.loginButton);
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int usernameSize = editUsername.getText().length();
				int passwordSize = editPassword.getText().length();
				if (usernameSize > 0 && passwordSize > 0) {
					ProgressDialog.show(LoginActivity.this, "", "Ladevorgang. Bitte warten...", true);
					// doLogin(editUsername.getText().toString(), editPassword.getText().toString());
				} else {
					displayAlertDialog();
				}
			}

		});
		
		Button cancelBtn= (Button) findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
        	public void onClick(View v) { 
            	
            }
        });
	}

	protected void displayAlertDialog() {
		AlertDialog.Builder dialogAlerting = new AlertDialog.Builder(LoginActivity.this);
		dialogAlerting.setMessage("Bitte Benutzername und Passwort angeben!");
		dialogAlerting.setTitle("Fehler");
		dialogAlerting.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		dialogAlerting.create();
	}

}
