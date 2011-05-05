package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
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
import ch.hsr.se2p.mrt.models.GpsPosition;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

/**
 * Creates new TimeEntry. A new TimeEntry with the current time is created after the user presses the button start. A customer, a TimeEntry type and a
 * description about the given task can be added to the TimeEntry. After the button stop is pressed the current time is added to the TimeEntry. If the
 * TimeEntry was successfully created a toast appears otherwise an alert dialog is shown.
 */
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
	private Spinner timeEntryType;
	private Spinner gpsSelection;
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

			// Acquire a reference to the system Location Manager
			LocationManager locman = (LocationManager) mrtApplication.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
			// locman.getLastKnownLocation(null);

			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network location provider.
					((TextView) findViewById(R.id.txtDescription)).setText(location.toString());
				}

				public void onStatusChanged(String provider, int status, Bundle extras) {
				}

				public void onProviderEnabled(String provider) {
				}

				public void onProviderDisabled(String provider) {
				}
			};

			// Register the listener with the Location Manager to receive location updates
			locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, locationListener);
			((TextView) findViewById(R.id.txtDescription)).setText("Suche GPS........");

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
		initSpinnerGPSSelection();
		initSpinnerTimeEntryType();

		((Button) findViewById(R.id.btnStartStop)).setOnClickListener(lstnStartStopTime);
		updateView();
	}

	private boolean isMeasurementStarted() {
		return isStarted;
	}

	private void initSpinnerTimeEntryType() {
		timeEntryType = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		// TODO: Remove hackForTimeEntryTypes() as soon as TimeEntryType is working
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeEntryType.setAdapter(timeEntryTypeAdapater);
	}

	private void initSpinnerGPSSelection() {
		gpsSelection = (Spinner) findViewById(R.id.spinnerGPSSelection);
		ArrayAdapter<Customer> gpsSelectionAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, hackForGPSSelection());
		gpsSelectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gpsSelection.setAdapter(gpsSelectionAdapter);
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
		if (isMeasurementStarted()) {
			setLayout("Zeit gestartet um " + new Time(currentTimeEntry.getTimeStart().getTime()) + " Uhr", "Stop", Color.RED);
		} else {
			setLayout("Zeit gestoppt", "Start", Color.GREEN);
			removeText((TextView) findViewById(R.id.txtDescription));
			removeText((TextView) findViewById(R.id.autocompleteCustomer));
			// ((Spinner) findViewById(R.id.spinnerGPSSelection)).setSelection(0);
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
		currentTimeEntry.setTimeEntryTypeId(((TimeEntryType) timeEntryType.getSelectedItem()).getId());
		currentTimeEntry.setDescription(((TextView) findViewById(R.id.txtDescription)).getText().toString());
		setCustomer();
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(currentTimeEntry);
		Log.i(TAG, "Inserted ID: " + currentTimeEntry.getId());
	}

	private void setCustomer() throws SQLException {
		if ((autoCompleteCustomers.getText().length() == 0) && (gpsSelection.getId() != 0)) { // kein Kunde in Autocomplete, aber Kunde in GPS-Auswahl
			// currentTimeEntry.setCustomerId(gpsSelection.getId());
		} else {
			try {
				currentTimeEntry.setCustomerId(getCustomer().getId()); // ursprünglicher Code
			} catch (NullPointerException e) {
			}
		}
	}

	private Customer getCustomer() throws SQLException {
		Editable text = autoCompleteCustomers.getText();
		if (text.length() == 0)
			return null;
		return findCustomer(text.toString());
	}

	private Customer findCustomer(String customerStr) {
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).toString().equals(customerStr.toString()))
				return customers.get(i);
		}
		return null;
	}

	private synchronized List<Customer> getCustomers() {
		if (customers == null)
			loadCustomers();
		return customers;
	}

	// TODO: Delete as soon as Class TimeEntryType is working
	final static List<TimeEntryType> hackForTimeEntryTypes() {
		ArrayList<TimeEntryType> timeEntryTypes = new ArrayList<TimeEntryType>();
		timeEntryTypes.add(new TimeEntryType(1, "Kein Stundeneintragstyp"));
		timeEntryTypes.add(new TimeEntryType(2, "Stundeneintragstyp 1"));
		timeEntryTypes.add(new TimeEntryType(3, "Stundeneintragstyp 2"));
		timeEntryTypes.add(new TimeEntryType(4, "Stundeneintragstyp 3"));
		timeEntryTypes.add(new TimeEntryType(5, "Stundeneintragstyp 4"));
		return timeEntryTypes;
	}

	final static List<Customer> hackForGPSSelection() {
		ArrayList<Customer> gpsSelection = new ArrayList<Customer>();
		// TODO: Customer Konstruktor mit Parameter löschen
		gpsSelection.add(new Customer(0, "Kunde", "GPS-Auswahl"));
		gpsSelection.add(new Customer(1, "Hadalbert", "Zwahlen"));
		gpsSelection.add(new Customer(2, "Kunigunde", "Heller"));
		return gpsSelection;
	}

	protected GpsPosition getCurrentPostition() {
		Location l = new Location("h");
		l.getLatitude();

		GpsPosition currentPosition = new GpsPosition(l);
		return currentPosition;
	}

	private void calculateCustomerPositionsFrom(GpsPosition position) {

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
