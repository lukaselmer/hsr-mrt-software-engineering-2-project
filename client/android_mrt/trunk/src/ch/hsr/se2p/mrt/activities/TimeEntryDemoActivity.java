package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import ch.hsr.se2p.mrt.network.CustomerHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.dao.Dao;

public class TimeEntryDemoActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}
	
	private boolean isStarted = false;
	private TimeEntry timeEntry;
	
	private OnClickListener lstnStartStopTime = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!isStarted){
				timeEntry = createTimeEntry();
				isStarted = true;
				updateView();
			} else {
				timeEntry = finishTimeEntry();
				isStarted = false;
				updateView();
			}
			
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);

		AutoCompleteTextView txtView = (AutoCompleteTextView) findViewById(R.id.autocompleteCustomer);
		ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(
				this, R.layout.list_item, CustomerHelper.hackForTest());
		txtView.setAdapter(customerAdapter);

		Spinner spinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(
				this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(timeEntryTypeAdapater);
		
		Button b1 = (Button) findViewById(R.id.btnStartStop);
		b1.setOnClickListener(lstnStartStopTime);
		updateView();
	}

	protected TimeEntry finishTimeEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void updateView() {
		TextView tv = (TextView) findViewById(R.id.txtTime);
		Button b = (Button) findViewById(R.id.btnStartStop);
	    Drawable d = findViewById(R.id.btnStartStop).getBackground();
	    PorterDuffColorFilter filter;
		if(isStarted){
			tv.setText("Zeit gestartet um " + timeEntry.getTimeStart().toLocaleString());
			b.setText("Stop");
			filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);  
		} else {
			tv.setText("Zeit gestoppt");
			b.setText("Start");
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
		}
		d.setColorFilter(filter);
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
	
	private TimeEntry createTimeEntry(){
		TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis()));
		return t;
	}
}
