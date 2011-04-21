package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.jayway.android.robotium.solo.Solo;

import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
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
	private static final String START = "Start";
	private static final String STOP = "Stop";

	public TimeEntryActivityTest() {
		super("ch.hsr.se2p.mrt", TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		editCustomer = (AutoCompleteTextView) activity
				.findViewById(R.id.autocompleteCustomer);
		editTimeEntryType = (Spinner) activity
				.findViewById(R.id.spinnerTimeEntryType);
		editDescription = (TextView) activity.findViewById(R.id.txtDescription);
		button = (Button) activity.findViewById(R.id.btnStartStop);
		txtTime = (TextView) activity.findViewById(R.id.txtTime);
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
		assertTrue(solo
				.searchText("Neuer Stundeneintrag wurde erstellt.", true));
		assertEquals(START, button.getText().toString());
	}

	public void testCreateTimeEntryWithoutAnyInformation() {
		try {
			Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
			int count = dao.queryForAll().size();
			solo.clickOnButton(START);
			solo.clickOnButton(STOP);
			assertEquals(count + 1, dao.queryForAll().size());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

//	public void testCreateTimeEntryWithCustomer() {
//
//	}
//
//	public void testCreateTimeEntryWithTimeEntryType() {
//		
//
//	}
//
//	public void testCreateTimeEntryWithDescription() {
//
//	}
}
