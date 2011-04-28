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

public class TimeEntryActivityTest extends
		ActivityInstrumentationTestCase2<TimeEntryActivity> {

	private TimeEntryActivity activity;
	private AutoCompleteTextView editCustomer;
	private Spinner editTimeEntryType;
	private TextView editDescription;
	private Solo solo;
	private Button button;
	private TextView txtTime;
	private Dao<TimeEntry, ?> dao;
	private int count;
	private TimeEntry timeEntry;
	private Customer customer;

	public static final String START = "Start";
	public static final String STOP = "Stop";
	public static final int INITIAL_POSITION = 0;
	public static final int TEST_POSITION = 2;
	public static final String DESCRIPTION = "Wasserhahn Reparatur";	

	public TimeEntryActivityTest() {
		super("ch.hsr.se2p.mrt", TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		editCustomer = (AutoCompleteTextView) activity.findViewById(R.id.autocompleteCustomer);
		editTimeEntryType = (Spinner) activity.findViewById(R.id.spinnerTimeEntryType);
		editDescription = (TextView) activity.findViewById(R.id.txtDescription);
		button = (Button) activity.findViewById(R.id.btnStartStop);
		txtTime = (TextView) activity.findViewById(R.id.txtTime);
		customer = getActivity().getHelper().getCustomerDao().queryForAll().get(0);
		this.solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			this.solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	@UiThreadTest
	public void testPreconditions() {
		assertEquals("", editCustomer.getText().toString());
		assertEquals(new Integer(1),
				((TimeEntryType) editTimeEntryType.getSelectedItem()).getId());
		assertEquals("", editDescription.getText().toString());
		assertEquals(START, button.getText().toString());
	}

	public void testStandardDialog() {
		assertEquals("Zeit gestoppt", txtTime.getText().toString());
		solo.clickOnButton(START);
		assertTrue(solo.searchText("Zeit gestartet um ", true));
		assertEquals(STOP, button.getText().toString());
		solo.clickOnButton(STOP);
		assertTrue(solo.searchText("Neuer Stundeneintrag wurde erstellt.", true));
		assertEquals(START, button.getText().toString());
	}

	public void testCreateTimeEntryWithoutAnyInformation() {
		try {
			setDao();
			assertEquals(count + 1, dao.queryForAll().size());
		} catch (SQLException e) {
			e.printStackTrace();
			assert(false);
		}
	}

	public void testSpinnerSelection() {
		setSpinner();
		TimeEntryType timeEntryType = (TimeEntryType) editTimeEntryType.getSelectedItem();
		assertEquals(
				TimeEntryActivity.hackForTimeEntryTypes().get(TEST_POSITION).getName(), timeEntryType.getName());
	}

	public void testSelectionCustomer() {
		setCustomerName();
		assertEquals(customer.toString(), editCustomer.getText().toString());
	}

	@UiThreadTest
	public void testDescription() {
		editDescription.setText(DESCRIPTION);
		assertEquals(DESCRIPTION, editDescription.getText().toString());
	}

	public void testCreateTimeEntryWithTimeEntryType() {
		setSpinner();
		try {
			setDao();
			timeEntry = dao.queryForAll().get(count);
			assertEquals(TimeEntryActivity.hackForTimeEntryTypes().get(TEST_POSITION).getId(), timeEntry.getTimeEntryTypeId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithCustomer() {
		setCustomerName();
		try {
			setDao();
			timeEntry = dao.queryForAll().get(count);
			assertEquals(customer.getId(), timeEntry.getCustomerId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}
	
	public void testCreateTimeEntryWithInvalidCustomer(){
		activity.runOnUiThread(new Runnable() {
			public void run() {
				editCustomer.setText("Ungültiger Benutzer");
			}
		});
		try {
			setDao();
			timeEntry = dao.queryForAll().get(count);
			assertEquals(null, timeEntry.getCustomerId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testCreateTimeEntryWithDescription() {
		setDescription();		
		try {
			setDao();
			timeEntry = dao.queryForAll().get(count);
			assertEquals(DESCRIPTION, timeEntry.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}
	
	private void setDao() throws SQLException {
		solo.clickOnButton(START);
			dao = getActivity().getHelper().getDao(TimeEntry.class);
			count = dao.queryForAll().size();
			solo.clickOnButton(STOP);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				assert (false);
			}
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
