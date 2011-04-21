package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
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

	private OnClickListener lstnStartStopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isStarted) {
				currentTimeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
				isStarted = true;
				updateView();
			} else {
				ProgressDialog dialog = ProgressDialog.show(TimeEntryActivity.this, "", "Creating TimeEntry. Please wait...", true);
				dialog.show();
				try {
					saveTimeEntry();
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "Neuer Stundeneintrag wurde erstellt.", Toast.LENGTH_LONG).show();
				} catch (SQLException e) {
					dialog.dismiss();
					Log.e(TAG, "Database Exception", e);
					ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "Für weitere Informationen Log anzeigen.",
							TimeEntryActivity.this);
				}
				isStarted = false;
				updateView();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);

		autoCompleteCustomers = (AutoCompleteTextView) findViewById(R.id.autocompleteCustomer);
		updateAutocompleteCustomers();

		spinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(timeEntryTypeAdapater);

		Button button = (Button) findViewById(R.id.btnStartStop);
		button.setOnClickListener(lstnStartStopTime);
		updateView();
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
		TextView tv = (TextView) findViewById(R.id.txtTime);
		Button button = (Button) findViewById(R.id.btnStartStop);
		Drawable d = findViewById(R.id.btnStartStop).getBackground();
		PorterDuffColorFilter filter;
		if (isStarted) {
			tv.setText("Zeit gestartet um " + currentTimeEntry.getTimeStart().toLocaleString());
			button.setText("Stop");
			filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		} else {
			tv.setText("Zeit gestoppt");
			button.setText("Start");
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
		}
		d.setColorFilter(filter);
		updateAutocompleteCustomers();
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
		} catch (NullPointerException e){}
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(currentTimeEntry);
		Log.i(TAG, "Inserted ID: " + currentTimeEntry.getId());
	}

	private Customer getCustomer() throws SQLException {
		if (autoCompleteCustomers.getText().length() != 0) {
			for (int i = 0; i < customers.size(); i++){
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
}
