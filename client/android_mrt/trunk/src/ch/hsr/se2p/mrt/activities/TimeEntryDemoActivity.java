package ch.hsr.se2p.mrt.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import ch.hsr.se2p.mrt.network.CustomerHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;

public class TimeEntryDemoActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {
			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_entry);

		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocompleteCustomer);
		ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(
				this, R.layout.list_item, CustomerHelper.hackForTest());
		textView.setAdapter(customerAdapter);

		Spinner spinner = (Spinner) findViewById(R.id.spinnerTimeEntryType);
		ArrayAdapter<TimeEntryType> timeEntryTypeAdapater = new ArrayAdapter<TimeEntryType>(
				this, android.R.layout.simple_spinner_item,
				hackForTimeEntryTypes());
		timeEntryTypeAdapater
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(timeEntryTypeAdapater);

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
