package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

import com.j256.ormlite.dao.Dao;
import com.jayway.android.robotium.solo.Solo;

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity> {

	private TimeEntryActivity activity;
	private MRTAutocompleteSpinner editCustomer;
	private Spinner editTimeEntryType;
	private TextView editDescription;
	private Solo solo;
	private Button button;
	private TextView txtTime;
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
		this.solo = new Solo(getInstrumentation(), activity);
	}

	private void retrieveWidgetsById() {
		editCustomer = (MRTAutocompleteSpinner) activity.findViewById(R.id.my_combo);
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
		assertEquals("", editCustomer.getText());
		assertEquals(Integer.valueOf(0), ((TimeEntryType) editTimeEntryType.getSelectedItem()).getId());
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

	// public void testCreateTimeEntryWithoutAnyInformation() {
	// try {
	// int count = getTimeEntryDaoCount();
	// startAndStopTimeMeasurement();
	// assertEquals(count + 1, getTimeEntryDaoCount());
	// } catch (SQLException e) {
	// e.printStackTrace();
	// assert (false);
	// }
	// }

	public void testSpinnerSelection() {
		setSpinner();
		TimeEntryType timeEntryType = (TimeEntryType) editTimeEntryType.getSelectedItem();
		try {
			assertEquals(getTimeEntryType(TEST_POSITION - 1).getName(), timeEntryType.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
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
			startAndStopTimeMeasurement();
			timeEntry = getTimeEntry(getTimeEntryDaoCount() - 1);
			
			assertEquals(getTimeEntryType(TEST_POSITION - 1).getId(), timeEntry.getTimeEntryTypeId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithCustomer() {
		setCustomerName();
		try {
			startAndStopTimeMeasurement();
			timeEntry = getTimeEntry(getTimeEntryDaoCount() - 1);
			assertEquals(customer.getId(), timeEntry.getCustomerId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithInvalidCustomer() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editCustomer.setText("Ungültiger Benutzer");
			}
		});
		try {
			startAndStopTimeMeasurement();
			timeEntry = getTimeEntry(getTimeEntryDaoCount() - 1);
			assertEquals(null, timeEntry.getCustomerId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithDescription() {
		setDescription();
		try {
			startAndStopTimeMeasurement();
			timeEntry = getTimeEntry(getTimeEntryDaoCount() - 1);
			assertEquals(DESCRIPTION, timeEntry.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	private void startAndStopTimeMeasurement() throws SQLException {
		performClickOnButton(START);
		performClickOnButton(STOP);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	private TimeEntryType getTimeEntryType(int index) throws SQLException {
		return sortTimeEntryTypes().get(index);
	}

	private List<TimeEntryType> sortTimeEntryTypes() throws SQLException {
		final List<TimeEntryType> timeEntryTypes = getTimeEntryTypeDao().queryForAll();
		Collections.sort(timeEntryTypes);
		return timeEntryTypes;
	}

	private Dao<TimeEntryType, Integer> getTimeEntryTypeDao() throws SQLException {
		return activity.getHelper().getDao(TimeEntryType.class);
	}

	private int getTimeEntryDaoCount() throws SQLException {
		return activity.getHelper().getTimeEntryDao().queryForAll().size();
	}

	private TimeEntry getTimeEntry(int index) throws SQLException {
		return activity.getHelper().getTimeEntryDao().queryForAll().get(index);
	}

	private void performClickOnButton(String button) {
		solo.clickOnButton(button);
	}

	private void setDescription() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editDescription.setText(DESCRIPTION);
			}
		});
	}

	private void setSpinner() {
		activity.runOnUiThread(new Runnable() {
			@Override
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
			@Override
			public void run() {
				editCustomer.setText(customer.toString());
			}
		});
		solo.sendKey(KeyEvent.KEYCODE_DPAD_DOWN);
		solo.sendKey(KeyEvent.KEYCODE_DPAD_CENTER);
	}
}
