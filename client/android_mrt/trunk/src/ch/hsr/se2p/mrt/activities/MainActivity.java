package ch.hsr.se2p.mrt.activities;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
	private static final int DIALOG_TRANSMIT_TIME_ENTRIES_SUCCESS = 0, DIALOG_TRANSMIT_TIME_ENTRIES_FAIL = 1, DIALOG_EXCEPTION = 2;

	// private TransmitTimeEntriesTask transmitTimeEntriesTask = null;
	public static final String TAG = MainActivity.class.getSimpleName();

	private OnClickListener lstnCreateTimeEntryWithDescription = new OnClickListener() {
		@Override
		public void onClick(View v) {
			createTimeEntryDialog(true);
			updateView();
		}
	};
	private OnClickListener lstnCreateTimeEntryWithoutDescription = new OnClickListener() {
		@Override
		public void onClick(View v) {
			createTimeEntryDialog(false);
			updateView();
		}
	};
	private OnClickListener lstnSendTimeEntries = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendTimeEntiesDialog();
			updateView();
		}
	};

	private Exception currentException;

	private void createTimeEntryDialog(boolean withDescrition) {
		ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Creating TimeEntry. Please wait...", true);
		dialog.show();
		try {
			int id = createTimeEntry(withDescrition);
			dialog.dismiss();
			displayAlertDialog("", "TimeEntry with id " + id + " created.");
		} catch (SQLException e) {
			dialog.dismiss();
			Log.e(TAG, "Database Exception", e);
			displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log.");
		}
	}

	private void displayAlertDialog(String title, String message) {
		getAlertDialog(title, message).show();
	}

	protected Dialog getAlertDialog(String title, String message) {
		AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
		b.setTitle(title);
		b.setMessage(message);
		b.setPositiveButton("Ok", null);
		Dialog d = b.create();
		return d;
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
		Button b2 = (Button) findViewById(R.id.btnCreateTimeEntryWithoutDescription);
		b2.setOnClickListener(lstnCreateTimeEntryWithoutDescription);
		Button b3 = (Button) findViewById(R.id.btnSendTimeEntries);
		b3.setOnClickListener(lstnSendTimeEntries);

		updateView();

		// createSomeTimeEntries();
		// transmitTheTimeEnties();

	}

	private void updateView() {
		TextView tv;
		try {
			tv = (TextView) findViewById(R.id.textview);
		} catch (NullPointerException e) {
			// Element not yet initialized
			return;
		}
		tv.setText(String.format(getString(R.string.txtWelcome), getTimeEntriesToTransmitCount()));
	}

	// private void createSomeTimeEntries() {
	// try {
	// createTimeEntryWithDescription();
	// createTimeEntryWithoutDescription();
	// } catch (SQLException e) {
	// Log.e(TAG, "Database excaeption", e);
	// }
	// }

	private int getTimeEntriesToTransmitCount() {
		try {
			return getTimeEntriesToTransmit().size();
		} catch (SQLException e) {
			return 0;
		}
	}

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

	private List<TimeEntry> getTimeEntriesToTransmit() throws SQLException {
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		return timeEntryDao.queryForAll();
	}

	private int transmitTimeEnties(List<TimeEntry> timeEntries, ProgressDialog dialog) throws SQLException {
		int timeEntriesTransmitted = 0;
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		Log.d(TAG, "Transmitting " + timeEntries.size() + " timeEntries");
		Transmitter transmitter = new Transmitter();
		for (TimeEntry timeEntry : timeEntries) {
			if (transmitter.transmit(timeEntry)) {
				if (!timeEntry.isTransmitted()) {
					timeEntry.setTransmitted();
					timeEntryDao.update(timeEntry);
				} else {
					break;
				}

				if (transmitter.confirm(timeEntry)) {
					timeEntryDao.delete(timeEntry);
					timeEntriesTransmitted++;
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return timeEntriesTransmitted;
	}

	private void sendTimeEntiesDialog() {
		final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Searching TimeEntries to transmit...", true);
		dialog.show();
		final List<TimeEntry> timeEntries;
		try {
			timeEntries = getTimeEntriesToTransmit();
		} catch (SQLException e) {
			dialog.dismiss();
			Log.e(TAG, "Database excaeption", e);
			displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log.");
			return;
		}
		if (timeEntries.size() == 0) {
			dialog.dismiss();
			displayAlertDialog("Transmission finished", "No TimeEntries found.");
			return;
		}

		dialog.setMessage("Transmitting " + timeEntries.size() + " TimeEntries...");

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				//dialog.dismiss();
				Bundle b = msg.getData();
				displayAlertDialog(b.getString("title"), b.getString("message"));
			}
		};

		new Thread(new Runnable() {
			public void run() {
				int count = 0;
				try {
					count = transmitTimeEnties(timeEntries, dialog);
				} catch (SQLException e) {
					MainActivity.this.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log");
				}
				dialog.dismiss();
				//handler.sendEmptyMessage(0);

				if (count == 0)
					handler.sendMessage(getMessageForAlertDialog("Transmission finished", "No TimeEntries transmitted. For further details, see log."));
				else
					handler.sendMessage(getMessageForAlertDialog("Transmission finished", count + " of " + timeEntries.size() + " TimeEntries transmitted."));
			}
		}).start();
	}

	private Message getMessageForAlertDialog(String title, String message) {
		Message m = Message.obtain();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("message", message);
		m.setData(b);
		return m;
	}

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// Dialog dialog;
	// switch (id) {
	// case DIALOG_TRANSMIT_TIME_ENTRIES_SUCCESS:
	// dialog = getAlertDialog("Transmission finished", "No TimeEntries transmitted. For further details, see log.");
	// break;
	// // case DIALOG_TRANSMIT_TIME_ENTRIES_FAIL:
	// // dialog = getAlertDialog("Transmission finished", transmitTimeEntriesTask.getSuccessful() + " of " + transmitTimeEntriesTask.getTotal() + " TimeEntries transmitted.");
	// // break;
	// // case DIALOG_EXCEPTION:
	// // dialog = getAlertDialog("SQL Exception", currentException.getMessage() + "\n" + "For further details, see log");
	// // break;
	// default:
	// dialog = null;
	// }
	// return dialog;
	// }

	// private class TransmitTimeEntriesTask extends AsyncTask<TimeEntry, Integer, Integer> {
	// private Exception exception;
	// private TimeEntry[] timeEntries;
	// private int timeEntriesSucessfullyTransmitted = 0;
	//
	// public Exception getException() {
	// return exception;
	// }
	//
	// public int getTotal() {
	// return timeEntries.length;
	// }
	//
	// public int getSuccessful() {
	// return timeEntriesSucessfullyTransmitted;
	// }
	//
	// protected Integer doInBackground(TimeEntry... timeEntries) {
	// this.timeEntries = timeEntries;
	// timeEntriesSucessfullyTransmitted = 0;
	// try {
	// Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
	// Log.d(TAG, "Transmitting " + timeEntries.length + " timeEntries");
	// Transmitter transmitter = new Transmitter();
	// for (TimeEntry timeEntry : timeEntries) {
	// if (transmitter.transmit(timeEntry)) {
	// if (!timeEntry.isTransmitted()) {
	// timeEntry.setTransmitted();
	// timeEntryDao.update(timeEntry);
	// } else {
	// break;
	// }
	// if (transmitter.confirm(timeEntry)) {
	// timeEntryDao.delete(timeEntry);
	// timeEntriesSucessfullyTransmitted++;
	// } else {
	// break;
	// }
	// } else {
	// break;
	// }
	// }
	// } catch (SQLException e) {
	// this.exception = e;
	// }
	// return timeEntriesSucessfullyTransmitted;
	//
	// // int count = urls.length;
	// // long totalSize = 0;
	// // for (int i = 0; i < count; i++) {
	// // totalSize += Downloader.downloadFile(urls[i]);
	// // publishProgress((int) ((i / (float) count) * 100));
	// // }
	// // return totalSize;
	// }
	//
	// // protected void onProgressUpdate(Integer... progress) {
	// // setProgressPercent(progress[0]);
	// // }
	//
	// protected void onPostExecute(Integer result) {
	// if (exception != null){
	// currentException = exception;
	// showDialog(DIALOG_EXCEPTION);
	// }
	// showDialog(result == 0 ? DIALOG_TRANSMIT_TIME_ENTRIES_FAIL : DIALOG_TRANSMIT_TIME_ENTRIES_SUCCESS);
	// }
	// }

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