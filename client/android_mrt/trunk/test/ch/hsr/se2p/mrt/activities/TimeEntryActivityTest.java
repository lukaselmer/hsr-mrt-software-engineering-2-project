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

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity>  {

	private TimeEntryActivity activity;
	private AutoCompleteTextView editCustomer;
	private Spinner editTimeEntryType;
	private TextView editDescription;
	private Solo solo;
	private Button button;
	private static final String START = "Start";
	private static final String STOP = "Stop";

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
		assertEquals(new Integer(1), ((TimeEntryType) editTimeEntryType.getSelectedItem()).getId());
		assertEquals("", editDescription.getText().toString());
		assertEquals(START, button.getText().toString());
	}
	
	public void testCreateTimeEntryWithoutAnyInformation(){
//		try {
//			Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			assert (false);
		solo.clickOnButton(START);
		assertEquals(STOP, button.getText().toString());
		solo.clickOnButton(STOP);
		assertEquals(STOP, button.getText().toString());
}
}
