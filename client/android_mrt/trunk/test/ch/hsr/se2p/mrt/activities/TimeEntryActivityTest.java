package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;

import junit.framework.TestCase;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;

import com.j256.ormlite.dao.Dao;

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity> {
	private TextView mView;
	private String resourceString;
	private TimeEntryActivity mActivity;

	public TimeEntryActivityTest() {
		super("ch.hsr.se2p.mrt", TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mView = (TextView) mActivity.findViewById(R.id.textview);
		resourceString = mActivity.getString(R.string.txtWelcome);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// getActivity().deleteDatabase(TEST_DB_NAME);
	}

	public void testPreconditions() {
		assertNotNull(mView);
	}

	@UiThreadTest
	public void testWelcomeText() {
		try {
			Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
			assertEquals(String.format(resourceString, dao.queryForAll().size()), (String) mView.getText());
			getActivity().getLstnCreateTimeEntryWithDescription().onClick(mView);
			assertEquals(String.format(resourceString, dao.queryForAll().size()), (String) mView.getText());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@UiThreadTest
	public void testCreateTimeEntries() {
		getInstrumentation();
		getActivity();
		try {
			Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
			int count = dao.queryForAll().size();
			getActivity().getLstnCreateTimeEntryWithDescription().onClick(mView);
			assertEquals(count + 1, dao.queryForAll().size());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	// @UiThreadTest
	// public void testSendTimeEntries() {
	// try {
	// Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
	// int count = dao.queryForAll().size();
	// mActivity.getLstnSendTimeEntries().onClick(mView);
	//
	// // new MockContentProvider().
	// assertEquals(0, dao.queryForAll().size());
	// } catch (SQLException e) {
	// e.printStackTrace();
	// assert (false);
	// }
	// }
}
