package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.jayway.android.robotium.solo.Solo;

import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity> {

	private TimeEntryActivity activity;
	private AutoCompleteTextView editCustomer;
	private Spinner editTimeEntryType;
	private TextView editDescription;
	private Solo solo;
	private Button button;
	private TextView txtTime;
	private int count;
	private TimeEntry timeEntry;
	private Customer customer;

	public static final String START = "Start", STOP = "Stop", DESCRIPTION = "Wasserhahn Reparatur";
	public static final int INITIAL_POSITION = 0, TEST_POSITION = 2;

	public TimeEntryActivityTest() {
		super("ch.hsr.se2p.mrt", TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		retrieveWidgetsById();
		customer = activity.getHelper().getCustomerDao().queryForAll().get(0);
		solo = new Solo(getInstrumentation(), activity);
	}

	private void retrieveWidgetsById() {
		editCustomer = (AutoCompleteTextView) activity.findViewById(R.id.autocompleteCustomer);
		editTimeEntryType = (Spinner) activity.findViewById(R.id.spinnerTimeEntryType);
		editDescription = (TextView) activity.findViewById(R.id.txtDescription);
		button = (Button) activity.findViewById(R.id.btnStartStop);
		txtTime = (TextView) activity.findViewById(R.id.txtTime);
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			this.solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		activity.finish();
		super.tearDown();
	}

	@UiThreadTest
	public void testPreconditions() {
		assertEquals("", editCustomer.getText().toString());
		assertEquals(Integer.valueOf(1), ((TimeEntryType) editTimeEntryType.getSelectedItem()).getId());
		assertEquals("", editDescription.getText().toString());
		assertEquals(START, button.getText().toString());
	}

	public void testStandardDialog() {
		assertEquals("Zeit gestoppt", txtTime.getText().toString());
		performClickOnButton(START);
		assertTrue(solo.searchText("Zeit gestartet um ", true));
		assertEquals(STOP, button.getText().toString());
		performClickOnButton(STOP);
		assertTrue(solo.searchText("Neuer Stundeneintrag wurde erstellt.", true));
		assertEquals(START, button.getText().toString());
	}

	public void testCreateTimeEntryWithoutAnyInformation() {
//		try { //das gebaschtel gaht
//			timeEntry = getDao().queryForAll().get(count);
//			assertEquals("", timeEntry.getDescription());
//			assertEquals(null, timeEntry.getCustomerId());
//			assertEquals(new Integer(1), timeEntry.getTimeEntryTypeId());
//		} catch (SQLException e) {
//			e.printStackTrace();
//			assert (false);
			//das isch mis gebaschtel, gaht aber nöd
			try {
				assertEquals(count + 1, activity.getHelper().getDao(TimeEntry.class).queryForAll().size());
			} catch (SQLException e) {
				e.printStackTrace();
				assert(false);
			}
		
			//so isch es nach em Refactoring vom Lukas
			try {
				assertEquals(count + 1, getDao().queryForAll().size());
			} catch (SQLException e) {
				e.printStackTrace();
				assert(false);
			}
	}

	public void testSpinnerSelection() {
		setSpinner();
		TimeEntryType timeEntryType = (TimeEntryType) editTimeEntryType.getSelectedItem();
		assertEquals(TimeEntryActivity.hackForTimeEntryTypes().get(TEST_POSITION).getName(), timeEntryType.getName());
	}

	public void testSelectionCustomer() {
		setCustomerName();
		assertEquals(customer.toString(), editCustomer.getText().toString());
	}

	@UiThreadTest
	public void testDescription() {
		setDescription();
		assertEquals(DESCRIPTION, editDescription.getText().toString());
	}

	public void testCreateTimeEntryWithTimeEntryType() {
		setSpinner();
		try {
			timeEntry = getDao().queryForAll().get(count);
			assertEquals(TimeEntryActivity.hackForTimeEntryTypes().get(TEST_POSITION).getId(), timeEntry.getTimeEntryTypeId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithCustomer() {
		setCustomerName();
		try {
			timeEntry = getDao().queryForAll().get(count);
			assertEquals(customer.getId(), timeEntry.getCustomerId());
			
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithInvalidCustomer() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editCustomer.setText("Ungültiger Benutzer");
			}
		});
		try {
			timeEntry = getDao().queryForAll().get(count);
			assertEquals(null, timeEntry.getCustomerId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	//TODO: create Test for TimeEntryTyp
	
	public void testCreateTimeEntryWithDescription() {
		setDescription();
		try {
			timeEntry = getDao().queryForAll().get(count);
			assertEquals(DESCRIPTION, timeEntry.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	private Dao<TimeEntry, ?> getDao() throws SQLException {
		performClickOnButton(START);
		Dao<TimeEntry, ?> dao = activity.getHelper().getDao(TimeEntry.class);
		count = dao.queryForAll().size();
		performClickOnButton(STOP);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			assert (false);
		}
		return dao;
	}

	private void performClickOnButton(String button) {
		solo.clickOnButton(button);
	}

	private void setDescription() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editDescription.setText(DESCRIPTION);
			}
		});
	}

	private void setSpinner() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editTimeEntryType.requestFocus();
				editTimeEntryType.setSelection(INITIAL_POSITION);
			}
		});
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
		for (int i = 1; i <= TEST_POSITION; i++) {
			solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
	}

	private void setCustomerName() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editCustomer.setText(customer.toString());
			}
		});
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
	}
}
