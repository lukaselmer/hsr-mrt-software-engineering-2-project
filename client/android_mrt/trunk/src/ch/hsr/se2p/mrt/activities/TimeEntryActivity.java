package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
	private static final double CIRCLE_RADIUS_FOR_CUSTOMER_DROPDOWN = 100000;// Circle 100km;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private String locationProvider;
	private GpsPosition currentPosition;

	private boolean isMeasurementStarted = false;
	private TimeEntry currentTimeEntry;
	private Spinner timeEntryTypeSpinner;
	private MRTAutocompleteSpinner comboBox;
	private List<Customer> customers;
	private List<TimeEntryType> timeEntryTypes;
	private MRTApplication mrtApplication;
	private ArrayAdapter<Customer> customerAdapter;

	private OnClickListener lstnStartStopTime = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isMeasurementStarted)
				startTimeMeasurement();
			else
				stopTimeMeasurement();
			updateView();
		}
	};

	private void startTimeMeasurement() {
		currentTimeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		isMeasurementStarted = true;
	}

	private void stopTimeMeasurement() {
		try {
			setGPSImage(false);
			saveTimeEntry();
			Toast.makeText(getApplicationContext(), "Neuer Stundeneintrag wurde erstellt.", Toast.LENGTH_LONG).show();
		} catch (SQLException e) {
			Log.e(TAG, "Database Exception", e);
			ActivityHelper.displayAlertDialog("SQL Exception", e.getMessage() + "\n" + "Für weitere Informationen Log anzeigen.",
					TimeEntryActivity.this);
		}
		isMeasurementStarted = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);
		ActivityHelper.startSyncService(this);
		mrtApplication = (MRTApplication) getApplication();
		initLocationService();
		((Button) findViewById(R.id.btnStartStop)).setOnClickListener(lstnStartStopTime);
		updateView();
	}

	private void initLocationService() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationProvider = locationManager.getBestProvider(getInitializedCriteria(), true);
		locationListener = new LocationListenerAdapter() {
			@Override
			public void onLocationChanged(Location location) {
				sortCustomersByLocation(location);
				locationManager.removeUpdates(locationListener);
				setGPSImage(true);
			}
		};
		locationManager.requestLocationUpdates(locationProvider, 60 * 1000, 0, locationListener); // update location maximal every 60 seconds
	}

	/**
	 * Calculates the distance to the currentPosition and sets it on each customer
	 */
	private static void calculateAndSetDistances(Dao<GpsPosition, Integer> dao, List<Customer> customers, GpsPosition currentPosition)
			throws SQLException {
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

	private Criteria getInitializedCriteria() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	private void populateCustomers() {
		comboBox = (MRTAutocompleteSpinner) findViewById(R.id.my_combo);
		comboBox.setAdapter(getCustomerAdapter());
	}

	private void populateTimeEntryTypes() {
		loadTimeEntryTypes();
		timeEntryTypeSpinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(this, android.R.layout.simple_spinner_item,
				timeEntryTypes);
		timeEntryTypeAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeEntryTypeSpinner.setAdapter(timeEntryTypeAdapater);
	}

	private ArrayAdapter<Customer> getCustomerAdapter() {
		customerAdapter = new ArrayAdapter<Customer>(this, R.layout.list_item, getCustomers());
		customerAdapter.setNotifyOnChange(true);
		return customerAdapter;
	}

	private void loadCustomers() {
		try {
			customers = getHelper().getCustomerDao().queryForAll();
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
		populateCustomers();
		populateTimeEntryTypes();

		if (isMeasurementStarted) {
			setLayout("Zeit gestartet um " + new Time(currentTimeEntry.getTimeStart().getTime()) + " Uhr", "Stop", Color.RED);
		} else {
			setLayout("Zeit gestoppt", "Start", Color.GREEN);
			((TextView) findViewById(R.id.txtDescription)).setText("");
			((MRTAutocompleteSpinner) findViewById(R.id.my_combo)).resetText();
			((Spinner) findViewById(R.id.spinnerTimeEntryType)).setSelection(0);
			Collections.sort(getCustomers());
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

		if (!(timeEntryTypeSpinner.getSelectedItem().equals(timeEntryTypeSpinner.getItemAtPosition(0))))
			currentTimeEntry.setTimeEntryTypeId(((TimeEntryType) timeEntryTypeSpinner.getSelectedItem()).getId());

		currentTimeEntry.setDescription(((TextView) findViewById(R.id.txtDescription)).getText().toString());

		if (currentPosition != null)
			currentTimeEntry.setGpsPositionId(saveGpsPosition());

		setCustomer();
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(currentTimeEntry);
		Log.i(TAG, "Inserted ID: " + currentTimeEntry.getId());
	}

	private int saveGpsPosition() throws SQLException {
		if (currentPosition == null)
			return -1;
		Dao<GpsPosition, Integer> gpsPositionDao = getHelper().getGpsPositionDao();
		gpsPositionDao.create(currentPosition);
		return currentPosition.getId();
	}

	private void setCustomer() throws SQLException {
		if (comboBox.getText().length() != 0 && getCustomer() != null) {
			currentTimeEntry.setCustomerId(getCustomer().getId());
		}
	}

	private Customer getCustomer() throws SQLException {
		String text = comboBox.getText();
		if (text.length() == 0)
			return null;
		return findCustomer(text);
	}

	private Customer findCustomer(String customerStr) {
		for (Customer customer : customers) {
			if (customer.toString().equals(customerStr.toString())) {
				return customer;
			}
		}
		return null;
	}

	private synchronized List<Customer> getCustomers() {
		if (customers == null)
			loadCustomers();
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
		mrtApplication.logout();
		this.startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	private void sortCustomersByLocation(Location location) {
		currentPosition = new GpsPosition(location);
		try {
			calculateAndSetDistances(getHelper().getGpsPositionDao(), getCustomers(), currentPosition);
			Collections.sort(getCustomers());
			populateCustomers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
