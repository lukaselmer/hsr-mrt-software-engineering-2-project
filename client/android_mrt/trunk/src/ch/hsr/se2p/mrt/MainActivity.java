package ch.hsr.se2p.mrt;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.R.layout;
import ch.hsr.se2p.mrt.models.DbHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();
	private DbHelper dbh;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbh = new DbHelper(getApplicationContext());

		createSomeTimeEntries();
		transmitTheTimeEnties();

		dbh.close();

		// TimeEntry.Values values = new TimeEntry.Values();
		// values.description = "bla";
		//
		// long id = TimeEntry.create(db, values);
		// Log.d(TAG, "TimeEntry with ID " + id + " created");
		//
		// c = TimeEntry.getById(db, id);
		// Log.d(TAG, "c.getCount(): " + c.getCount());
		// Log.d(TAG, "c.c.getString(0): " + c.getString(0));
		// Log.d(TAG, "c.c.getString(1): " + c.getString(1));
		// Log.d(TAG, "c.c.getString(2): " + c.getString(2));
		// Log.d(TAG, "c.c.getString(3): " + c.getString(3));
		// Log.d(TAG, "c.c.getString(4): " + c.getString(4));

		// final Button button = (Button) findViewById(R.id.login_button);
		// final EditText usernameEditText = (EditText)
		// findViewById(R.id.login), passwordEditText = (EditText)
		// findViewById(R.id.password);
		// String[] usernameAndPassword = loadLoginFromCache();
		// if (usernameAndPassword != null) {
		// doRequests(usernameAndPassword[0], usernameAndPassword[1]);
		// }
		// button.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// String username = usernameEditText.getText().toString(), password =
		// passwordEditText.getText().toString();
		// doRequests(username, password);
		// }
		// });
	}

	private void createSomeTimeEntries() {
		for (int i = 0; i < 10; i++) {
			TimeEntry.Values v = new TimeEntry.Values();
			v.description = "test bla bla " + i + ", time is " + System.currentTimeMillis();
			long id = TimeEntry.create(dbh, v);
			Log.i(TAG, "Inserted ID: " + id);
		}
	}

	private void transmitTheTimeEnties() {
		Cursor c = TimeEntry.all(dbh);
		Log.d(TAG, "c.getCount(): " + c.getCount());
	}

}