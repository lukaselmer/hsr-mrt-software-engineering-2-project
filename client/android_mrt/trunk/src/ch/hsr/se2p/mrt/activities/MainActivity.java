package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.models.DatabaseHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.network.Transmitter;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	public static final String TAG = MainActivity.class.getSimpleName();

	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		createSomeTimeEntries();
		transmitTheTimeEnties();

	}

	private void createSomeTimeEntries() {
		try {
			Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();

			TimeEntry t1 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
			t1.setDescription("with description, time is " + System.currentTimeMillis());
			t1.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 4))); // 4h later
			int id1 = timeEntryDao.create(t1);
			Log.i(TAG, "Inserted ID: " + id1);

			TimeEntry t2 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
			t2.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 3))); // 3h later
			int id2 = timeEntryDao.create(t1);
			Log.i(TAG, "Inserted ID: " + id2);

			TimeEntry t3 = new TimeEntry(new Timestamp(System.currentTimeMillis()));
			t3.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 30))); // 30min later
			int id3 = timeEntryDao.create(t1);
			Log.i(TAG, "Inserted ID: " + id3);
		} catch (SQLException e) {
			Log.e(TAG, "Database excaeption", e);
		}
	}

	private void transmitTheTimeEnties() {
		try {
			Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();

			List<TimeEntry> l = timeEntryDao.queryForAll();
			Log.d(TAG, "Size: " + l.size());
			Transmitter transmitter = new Transmitter();
			for (TimeEntry timeEntry : l) {
				if (transmitter.transmit(timeEntry)) {
					if (!timeEntry.isTransmitted()) {
						timeEntry.setTransmitted();
						timeEntryDao.update(timeEntry);
					}
					if (transmitter.confirm(timeEntry)) {
						timeEntryDao.delete(timeEntry);
					}
				}
			}
		} catch (SQLException e) {
			Log.e(TAG, "Database excaeption", e);
		}
	}

}

//
// try {
// dbh = new DbHelper(getApplicationContext());
//
// } finally {
// dbh.close();
// }

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