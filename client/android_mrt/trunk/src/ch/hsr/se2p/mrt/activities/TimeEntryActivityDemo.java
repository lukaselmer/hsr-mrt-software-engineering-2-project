package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.network.TimeEntryHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class TimeEntryActivityDemo extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	public static final String TAG = TimeEntryActivityDemo.class.getSimpleName();
	private MRTApplication mrtApplication;

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

	public OnClickListener getLstnCreateTimeEntryWithDescription() {
		return lstnCreateTimeEntryWithDescription;
	}

	public OnClickListener getLstnCreateTimeEntryWithoutDescription() {
		return lstnCreateTimeEntryWithoutDescription;
	}

	public OnClickListener getLstnSendTimeEntries() {
		return lstnSendTimeEntries;
	}

	private void createTimeEntryDialog(boolean withDescrition) {
		ProgressDialog dialog = ProgressDialog.show(TimeEntryActivityDemo.this, "", "Creating TimeEntry. Please wait...", true);
		dialog.show();
		try {
			int id = createTimeEntry(withDescrition);
			dialog.dismiss();
			ActivityHelper.displayAlertDialog("", "TimeEntry with id " + id + " created.", this);
		} catch (SQLException e) {
			dialog.dismiss();
			Log.e(TAG, "Database Exception", e);
			ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log.", this);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mrtApplication = (MRTApplication) getApplication();

		Button b1 = (Button) findViewById(R.id.btnCreateTimeEntryWithDescription);
		b1.setOnClickListener(lstnCreateTimeEntryWithDescription);
		Button b2 = (Button) findViewById(R.id.btnCreateTimeEntryWithoutDescription);
		b2.setOnClickListener(lstnCreateTimeEntryWithoutDescription);
		Button b3 = (Button) findViewById(R.id.btnSendTimeEntries);
		b3.setOnClickListener(lstnSendTimeEntries);

		updateView();
	}

	protected void updateView() {
		TextView tv;
		tv = (TextView) findViewById(R.id.textview);
		tv.setText(String.format(getString(R.string.txtWelcome), mrtApplication.getCurrentUser().getFirstName(), getTimeEntriesToTransmitCount()));
	}

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

	private int transmitTimeEnties(List<TimeEntry> timeEntries, ProgressDialog dialog, Handler progressHandler) throws SQLException {
		int timeEntriesTransmitted = 0;
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		Log.d(TAG, "Transmitting " + timeEntries.size() + " timeEntries");
		TimeEntryHelper timeEntryHelper = new TimeEntryHelper(mrtApplication.getHttpHelper());
		for (TimeEntry timeEntry : timeEntries) {
			if (timeEntryHelper.transmit(timeEntry)) {
				if (!timeEntry.isTransmitted()) {
					timeEntry.setTransmitted();
					timeEntryDao.update(timeEntry);
				}

				if (timeEntryHelper.confirm(timeEntry)) {
					timeEntryDao.delete(timeEntry);
					timeEntriesTransmitted++;
				} else {
					break;
				}
			} else {
				break;
			}
			dialog.setProgress(timeEntriesTransmitted);
		}
		return timeEntriesTransmitted;
	}

	private void sendTimeEntiesDialog() {
		final ProgressDialog dialog = new ProgressDialog(TimeEntryActivityDemo.this); // ProgressDialog.show(MainActivity.this,
																						// "",
																						// "Searching TimeEntries to transmit...",
																						// true, false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle("Transmitting TimeEntries...");
		dialog.setCancelable(false);
		dialog.show();
		final List<TimeEntry> timeEntries;
		try {
			timeEntries = getTimeEntriesToTransmit();
		} catch (SQLException e) {
			dialog.dismiss();
			Log.e(TAG, "Database exception", e);
			ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log.", this);
			return;
		}
		if (timeEntries.size() == 0) {
			dialog.dismiss();
			ActivityHelper.displayAlertDialog("Transmission finished", "No TimeEntries found.", this);
			return;
		}

		dialog.setProgress(0);
		dialog.setMax(timeEntries.size());

		final Handler notificationHandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
				ActivityHelper.displayAlertDialog(b.getString("title"), b.getString("message"), TimeEntryActivityDemo.this);
				updateView();
			}
		};
		final Handler progressHandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
				ActivityHelper.displayAlertDialog(b.getString("title"), b.getString("message"), TimeEntryActivityDemo.this);
				updateView();
			}
		};

		new Thread(new Runnable() {
			public void run() {
				int count = 0;
				try {
					count = transmitTimeEnties(timeEntries, dialog, progressHandler);
				} catch (SQLException e) {
					ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "For further details, see log",
							TimeEntryActivityDemo.this);
				}
				dialog.dismiss();

				String messageText = (count == 0) ? "No TimeEntries transmitted. For further details, see log." : count + " of " + timeEntries.size()
						+ " TimeEntries transmitted.";
				notificationHandler.sendMessage(getMessageForAlertDialog("Transmission finished", messageText));
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
}
