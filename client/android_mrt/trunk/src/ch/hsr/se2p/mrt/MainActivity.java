package ch.hsr.se2p.mrt;

import java.sql.Timestamp;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import ch.hsr.se2p.mrt.models.DbHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.network.Transmitter;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();
	private DbHelper dbh;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			dbh = new DbHelper(getApplicationContext());

			createSomeTimeEntries();
			transmitTheTimeEnties();
		} finally {
			dbh.close();
		}

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
		TimeEntry t1 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		t1.setDescription("with description, time is " + System.currentTimeMillis());
		t1.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 4))); // 4h later
		long id1 = TimeEntry.create(dbh, t1);
		Log.i(TAG, "Inserted ID: " + id1);

		TimeEntry t2 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		t2.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 3))); // 3h later
		long id2 = TimeEntry.create(dbh, t2);
		Log.i(TAG, "Inserted ID: " + id2);

		TimeEntry t3 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		t3.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 30))); // 30min later
		long id3 = TimeEntry.create(dbh, t3);
		Log.i(TAG, "Inserted ID: " + id3);
	}

	private void transmitTheTimeEnties() {
		List<TimeEntry> l = TimeEntry.all(dbh);
		Log.d(TAG, "Size: " + l.size());
		Transmitter transmitter = new Transmitter();
		for (TimeEntry timeEntry : l) {
			if(transmitter.transmit(timeEntry)){
				TimeEntry.setTransmitted(dbh,timeEntry);
				if(transmitter.confirm(timeEntry)){
					TimeEntry.delete(dbh, timeEntry.getId());
				}
			}
		}
	}

}