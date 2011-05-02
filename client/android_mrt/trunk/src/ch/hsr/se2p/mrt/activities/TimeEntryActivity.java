package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

public class TimeEntryActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}
	public static final String TAG = TimeEntryActivity.class.getSimpleName();

	private boolean isStarted = false;
	private TimeEntry currentTimeEntry;
	private AutoCompleteTextView autoCompleteCustomers;
	private Spinner spinner;
	private List<Customer> customers;
	private MRTApplication mrtApplication;

	private OnClickListener lstnStartStopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isMeasurementStarted())
				startTimeMeasurement();
			else
				stopTimeMeasurement();
			updateView();
		}

		private void stopTimeMeasurement() {
			try {
				saveTimeEntry();
				Toast.makeText(getApplicationContext(), "Neuer Stundeneintrag wurde erstellt.", Toast.LENGTH_LONG).show();
			} catch (SQLException e) {
				Log.e(TAG, "Database Exception", e);
				ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "Für weitere Informationen Log anzeigen.",
						TimeEntryActivity.this);
			}
			setMeausurementStarted(false);
		}

		private void setMeausurementStarted(boolean bool) {
			isStarted = bool;
		}

		private void startTimeMeasurement() {
			currentTimeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
			setMeausurementStarted(true);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);

		ActivityHelper.startSyncService(this);
		mrtApplication = (MRTApplication) getApplication();

		autoCompleteCustomers = (AutoCompleteTextView) findViewById(R.id.autocompleteCustomer);
		initSpinner();

		((Button) findViewById(R.id.btnStartStop)).setOnClickListener(lstnStartStopTime);
		updateView();
	}

	private boolean isMeasurementStarted() {
		return isStarted;
	}

	private void initSpinner() {
		spinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(timeEntryTypeAdapater);
	}

	protected ArrayAdapter<Customer> getCustomerAdapter() {
		return new ArrayAdapter<Customer>(this, R.layout.list_item, getCustomers());
	}

	private void loadCustomers() {
		try {
			customers = getHelper().getCustomerDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "Init customers", e);
		}
	}

	protected void updateView() {
		if (isMeasurementStarted())
			setLayout("Zeit gestartet um " + currentTimeEntry.getTimeStart().toLocaleString(), "Stop", Color.RED);
		else{
			setLayout("Zeit gestoppt", "Start", Color.GREEN);
			removeText((TextView) findViewById(R.id.txtDescription));
			removeText((TextView) findViewById(R.id.autocompleteCustomer));
			((Spinner) findViewById(R.id.spinnerTimeEntryType)).setSelection(0);
		}
		updateAutocompleteCustomers();
	}

	private void removeText(TextView textView) {
		textView.setText("");
	}

	private void setLayout(String textViewText, String buttonText, int color) {
		((TextView) findViewById(R.id.txtTime)).setText(textViewText);
		((Button) findViewById(R.id.btnStartStop)).setText(buttonText);
		findViewById(R.id.btnStartStop).getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
	}

	private void updateAutocompleteCustomers() {
		loadCustomers();
		autoCompleteCustomers.setAdapter(getCustomerAdapter());
	}

	protected void saveTimeEntry() throws SQLException {
		currentTimeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		currentTimeEntry.setTimeEntryTypeId(((TimeEntryType) spinner.getSelectedItem()).getId());
		currentTimeEntry.setDescription(((TextView) findViewById(R.id.txtDescription)).getText().toString());
		try {
			currentTimeEntry.setCustomerId(getCustomer().getId());
		} catch (NullPointerException e) {
		}
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(currentTimeEntry);
		Log.i(TAG, "Inserted ID: " + currentTimeEntry.getId());
	}

	private Customer getCustomer() throws SQLException {
		if (autoCompleteCustomers.getText().length() != 0) {
			for (int i = 0; i < customers.size(); i++) {
				if (customers.get(i).toString().equals(autoCompleteCustomers.getText().toString()))
					return customers.get(i);
			}
		}
		return null;
	}

	private synchronized List<Customer> getCustomers() {
		if (customers == null)
			loadCustomers();
		return customers;
	}

	final static List<TimeEntryType> hackForTimeEntryTypes() {
		ArrayList<TimeEntryType> timeEntryTypes = new ArrayList<TimeEntryType>();
		timeEntryTypes.add(new TimeEntryType(1, "Kein Stundeneintragstyp"));
		timeEntryTypes.add(new TimeEntryType(2, "Stundeneintragstyp 1"));
		timeEntryTypes.add(new TimeEntryType(3, "Stundeneintragstyp 2"));
		timeEntryTypes.add(new TimeEntryType(4, "Stundeneintragstyp 3"));
		timeEntryTypes.add(new TimeEntryType(5, "Stundeneintragstyp 4"));
		return timeEntryTypes;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void logout() {
		mrtApplication.logout();
		switchToLoginActivity();
	}

	private void switchToLoginActivity() {
		this.startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
}
