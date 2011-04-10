package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	public static final String TAG = MainActivity.class.getSimpleName();

	private OnClickListener lstnCreateTimeEntryWithDescription = new OnClickListener() {
		@Override
		public void onClick(View v) {
			createTimeEntryDialog(true);
		}
	};
	private OnClickListener lstnCreateTimeEntryWithoutDescription = new OnClickListener() {
		@Override
		public void onClick(View v) {
			createTimeEntryDialog(false);
		}
	};
	private OnClickListener lstnSendTimeEntries = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendTimeEntiesDialog();
		}
	};

	private void createTimeEntryDialog(boolean withDescrition) {
		ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Creating TimeEntry. Please wait...", true);
		dialog.show();
		try {
			int id = createTimeEntry(withDescrition);
			dialog.dismiss();
			AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
			b.setMessage("TimeEntry with id " + id + " created.");
			b.setPositiveButton("Ok", null);
			b.create().show();
		} catch (SQLException e) {
			dialog.dismiss();
			Log.e(TAG, "Database excaeption", e);
			AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
			b.setTitle("SQL Exception");
			b.setMessage(e.getMessage() + "\n" + "For further details, see log");
			b.setPositiveButton("Ok", null);
			b.create().show();
		}
	}

	private void sendTimeEntiesDialog() {
		ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Creating TimeEntry. Please wait...", true);
		dialog.show();
		// try {
		transmitTheTimeEnties();
		dialog.dismiss();
		AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
		b.setMessage("TimeEntries transmitted.");
		b.setPositiveButton("Ok", null);
		b.create().show();
		// }
		// catch (SQLException e) {
		// dialog.dismiss();
		// Log.e(TAG, "Database excaeption", e);
		// AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
		// b.setTitle("SQL Exception");
		// b.setMessage(e.getMessage() + "\n" + "For further details, see log");
		// b.setPositiveButton("Ok", null);
		// b.create().show();
		// }
	}

	// private OnClickListener lstnCreateTimeEntryWithoutDescription = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// }
	// };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button b1 = (Button) findViewById(R.id.btnCreateTimeEntryWithDescription);
		b1.setOnClickListener(lstnCreateTimeEntryWithDescription);
		Button b2 = (Button) findViewById(R.id.btnCreateTimeEntryWithDescription);
		b2.setOnClickListener(lstnCreateTimeEntryWithoutDescription);
		Button b3 = (Button) findViewById(R.id.btnSendTimeEntries);
		b3.setOnClickListener(lstnSendTimeEntries);

		// createSomeTimeEntries();
		// transmitTheTimeEnties();

	}

	// private void createSomeTimeEntries() {
	// try {
	// createTimeEntryWithDescription();
	// createTimeEntryWithoutDescription();
	// } catch (SQLException e) {
	// Log.e(TAG, "Database excaeption", e);
	// }
	// }

	private int createTimeEntry(boolean withDescription) throws SQLException {
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		if (withDescription)
			t.setDescription("with description, time is " + System.currentTimeMillis());
		t.setTimeStop(new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 4))); // 4h later
		timeEntryDao.create(t);
		Log.i(TAG, "Inserted ID: " + t.getId());
		return t.getId();
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