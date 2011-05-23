package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.application.MRTApplication;
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
	private static final String TAG = TimeEntryActivity.class.getSimpleName();
	private static final double CIRCLE_RADIUS_FOR_CUSTOMER_DROPDOWN = 30000;// Circle 30km;

	private LocationService locationService;

	private boolean isMeasurementStarted = false;
	private TimeEntry currentTimeEntry;
	private Spinner spinnerTimeEntryTypes;
	private MRTAutocompleteSpinner comboboxCustomers;
	private final List<Customer> customers = new ArrayList<Customer>();
	private List<TimeEntryType> timeEntryTypes;
	private MRTApplication mrtApplication;
	private ArrayAdapter<Customer> customerAdapter;

	private OnClickListener lstnStartStopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startOrStopTimeMeasurement();
			updateView();
		}
	};

	private void stopTimeMeasurement() {
		try {
			setGPSImage(false);
			saveTimeEntry();
			Toast.makeText(getApplicationContext(), "Neuer Stundeneintrag wurde erstellt.", Toast.LENGTH_LONG).show();
		} catch (SQLException e) {
			logAndDisplaySQLException(e);
		}
	}

	private void logAndDisplaySQLException(SQLException e) {
		Log.e(TAG, "Database Exception", e);
		ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "Für weitere Informationen Log anzeigen.", TimeEntryActivity.this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);
		initLocationService();
		loadCustomers();
		ActivityHelper.startSyncService(this);
		mrtApplication = (MRTApplication) getApplication();
		((Button) findViewById(R.id.btnStartStop)).setOnClickListener(lstnStartStopTime);
		populateSpinnerTimeEntryTypes();
		updateView();
	}

	private void initLocationService() {
		LocationListenerAdapter locationListener = new LocationListenerAdapter() {
			@Override
			public void onLocationChanged(Location location) {
				sortCustomersByCurrentLocation(location);
				setGPSImage(true);
			}
		};
		locationService = new LocationService(getLocationManager(), locationListener);
	}

	private LocationManager getLocationManager() {
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Calculates the distance to the currentPosition and sets it on each customer
	 */
	private static void calculateAndSetDistances(Dao<GpsPosition, Integer> dao, List<Customer> customers, GpsPosition currentPosition)
			throws SQLException {

		if (currentPosition == null)
			return;

		for (Customer c : customers) {
			calculateAndSetDistances(dao, currentPosition, c);
		}
	}

	private static void calculateAndSetDistances(Dao<GpsPosition, Integer> dao, GpsPosition currentPosition, Customer c) throws SQLException {
		if (c.hasGpsPosition()) {
			GpsPosition customerPosition = dao.queryForId(c.getGpsPositionId());
			if (customerPosition == null) {
				c.setDistance(null);
				return;
			}
			double distance = currentPosition.distanceTo(customerPosition);
			c.setDistance(distance <= CIRCLE_RADIUS_FOR_CUSTOMER_DROPDOWN ? distance : null);
		}
	}

	private static void resetDistances(List<Customer> customers) {
		for (Customer c : customers) {
			c.setDistance(null);
		}
	}

	private void populateComboboxCustomers() {
		comboboxCustomers = (MRTAutocompleteSpinner) findViewById(R.id.my_combo);
		comboboxCustomers.setAdapter(getCustomerAdapter());
	}

	private void populateSpinnerTimeEntryTypes() {
		loadTimeEntryTypes();
		spinnerTimeEntryTypes = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(this, android.R.layout.simple_spinner_item,
				timeEntryTypes);
		timeEntryTypeAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTimeEntryTypes.setAdapter(timeEntryTypeAdapater);
	}

	private ArrayAdapter<Customer> getCustomerAdapter() {
		customerAdapter = new ArrayAdapter<Customer>(this, R.layout.list_item, getCustomers());
		customerAdapter.setNotifyOnChange(true);
		return customerAdapter;
	}

	private void loadCustomers() {
		try {
			customers.clear();
			customers.addAll(getHelper().getCustomerDao().queryForAll());
		} catch (SQLException e) {
			Log.e(TAG, "Init customers", e);
		}
	}

	private void loadTimeEntryTypes() {
		try {
			timeEntryTypes = getHelper().getTimeEntryTypeDao().queryForAll();
			timeEntryTypes.add(0, new TimeEntryType(0, "Kein Stundeneintragstyp"));
		} catch (SQLException e) {
			Log.e(TAG, "Init timeentry types", e);
		}
	}

	private void updateView() {
		// sortCustomersByCurrentLocation();
		populateComboboxCustomers();

		if (isMeasurementStarted) {
			setLayout("Zeit gestartet um " + new Time(currentTimeEntry.getTimeStart().getTime()) + " Uhr", "Stop", Color.RED);
		} else {
			setLayout("Zeit gestoppt", "Start", Color.GREEN);
			((TextView) findViewById(R.id.txtDescription)).setText("");
			((MRTAutocompleteSpinner) findViewById(R.id.my_combo)).resetText();
			((Spinner) findViewById(R.id.spinnerTimeEntryType)).setSelection(0);
			populateSpinnerTimeEntryTypes();
		}
	}

	private void setLayout(String textViewText, String buttonText, int color) {
		((TextView) findViewById(R.id.txtTime)).setText(textViewText);
		((Button) findViewById(R.id.btnStartStop)).setText(buttonText);
		findViewById(R.id.btnStartStop).getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
	}

	private void setGPSImage(boolean gpsOn) {
		ImageView view = (ImageView) findViewById(R.id.image_gps);
		view.setImageResource(gpsOn ? R.drawable.gps_on : R.drawable.gps_off);
	}

	private void saveTimeEntry() throws SQLException {
		currentTimeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));

		if (spinnerTimeEntryTypes.getSelectedItemPosition() != 0)
			currentTimeEntry.setTimeEntryTypeId(((TimeEntryType) spinnerTimeEntryTypes.getSelectedItem()).getId());

		currentTimeEntry.setDescription(((TextView) findViewById(R.id.txtDescription)).getText().toString());

		if (locationService.getCurrentGPSPosition() != null)
			currentTimeEntry.setGpsPositionId(saveGpsPosition());

		if (comboboxCustomers.getText().length() != 0 && getCustomer() != null)
			currentTimeEntry.setCustomerId(getCustomer().getId());

		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(currentTimeEntry);
		Log.i(TAG, "Inserted ID: " + currentTimeEntry.getId());
	}

	private int saveGpsPosition() throws SQLException {
		if (locationService.getCurrentGPSPosition() == null)
			return -1;
		Dao<GpsPosition, Integer> gpsPositionDao = getHelper().getGpsPositionDao();
		gpsPositionDao.create(locationService.getCurrentGPSPosition());
		return locationService.getCurrentGPSPosition().getId();
	}

	private Customer getCustomer() throws SQLException {
		String textCustomer = comboboxCustomers.getText();
		if (textCustomer.length() == 0)
			return null;
		for (Customer customer : getCustomers()) {
			if (customer.toString().equals(textCustomer)) {
				return customer;
			}
		}
		return null;
	}

	private synchronized List<Customer> getCustomers() {
		return customers;
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
		case R.id.refreshMenu:
			updateView();
			Toast.makeText(getApplicationContext(), "Kunden und Stundeneintragstypen wurden aktualisiert.", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void logout() {
		locationService.stop();
		mrtApplication.logout();
		this.startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	private void sortCustomersByCurrentLocation(Location location) {
		try {
			calculateAndSetDistances(getHelper().getGpsPositionDao(), getCustomers(), locationService.getCurrentGPSPosition());
			Collections.sort(getCustomers());
			populateComboboxCustomers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void startOrStopTimeMeasurement() {
		if (isMeasurementStarted)
			stopTimeMeasurement();
		else
			currentTimeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		isMeasurementStarted = !isMeasurementStarted;
	}
}
