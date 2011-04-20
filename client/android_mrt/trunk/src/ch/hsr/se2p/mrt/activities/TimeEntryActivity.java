package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import ch.hsr.se2p.mrt.network.CustomerHelper;

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
	private TimeEntry timeEntry;
	private AutoCompleteTextView textView;
	private Spinner spinner;
	private ArrayAdapter<Customer> customerAdapter;

	private OnClickListener lstnStartStopTime = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isStarted) {
				createTimeEntry();
				isStarted = true;
				updateView();
			} else {
				try {
					finishTimeEntry();
				} catch (SQLException e) {
					Log.e(TAG, "Database Exception", e);
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

		textView = (AutoCompleteTextView) findViewById(R.id.autocompleteCustomer);
		customerAdapter = new ArrayAdapter<Customer>(
				this, R.layout.list_item, CustomerHelper.hackForTest());
		textView.setAdapter(customerAdapter);
		textView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				arg0.getItemAtPosition(arg2);
			}
			
		});

		spinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(
				this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(timeEntryTypeAdapater);

		Button button = (Button) findViewById(R.id.btnStartStop);
		button.setOnClickListener(lstnStartStopTime);
		updateView();
	}

	protected void updateView() {
		TextView tv = (TextView) findViewById(R.id.txtTime);
		Button button = (Button) findViewById(R.id.btnStartStop);
		Drawable d = findViewById(R.id.btnStartStop).getBackground();
		PorterDuffColorFilter filter;
		if (isStarted) {
			tv.setText("Zeit gestartet um "
					+ timeEntry.getTimeStart().toLocaleString());
			button.setText("Stop");
			filter = new PorterDuffColorFilter(Color.RED,
					PorterDuff.Mode.SRC_ATOP);
		} else {
			tv.setText("Zeit gestoppt");
			button.setText("Start");
			filter = new PorterDuffColorFilter(Color.GREEN,
					PorterDuff.Mode.SRC_ATOP);
		}
		d.setColorFilter(filter);
	}

	protected void createTimeEntry() {
		timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
	}

	protected int finishTimeEntry() throws SQLException {
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		timeEntry.setTimeEntryTypeId(((TimeEntryType) spinner.getSelectedItem()).getId());
		timeEntry.setDescription(((TextView) findViewById(R.id.txtDescription)).getText().toString());
		int i = textView.getListSelection();
		System.out.println(i);
		Toast.makeText(getApplicationContext(),
				"Neuer Stundeneintrag wurde erstellt: " + timeEntry.getDescription(), Toast.LENGTH_LONG).show();
		
		Dao<TimeEntry, Integer> timeEntryDao = getHelper().getTimeEntryDao();
		timeEntryDao.create(timeEntry);
		return 1;
	}

	final static List<TimeEntryType> hackForTimeEntryTypes() {
		ArrayList<TimeEntryType> timeEntryTypes = new ArrayList<TimeEntryType>();
		timeEntryTypes.add(new TimeEntryType(1, "Stundeneintragstyp 1"));
		timeEntryTypes.add(new TimeEntryType(2, "Stundeneintragstyp 2"));
		timeEntryTypes.add(new TimeEntryType(3, "Stundeneintragstyp 3"));
		timeEntryTypes.add(new TimeEntryType(4, "Stundeneintragstyp 4"));
		timeEntryTypes.add(new TimeEntryType(5, "Stundeneintragstyp 5"));
		return timeEntryTypes;
	}
}
