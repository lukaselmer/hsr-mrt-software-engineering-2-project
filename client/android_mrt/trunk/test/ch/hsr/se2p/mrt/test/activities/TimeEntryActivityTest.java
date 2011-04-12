package ch.hsr.se2p.mrt.test.activities;

import java.sql.SQLException;

import android.app.Instrumentation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.mock.MockContentProvider;
import android.test.mock.MockResources;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.activities.TimeEntryActivity;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;

import com.j256.ormlite.dao.Dao;

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity> {
	private TimeEntryActivity mActivity;
	private TextView mView;
	private String resourceString;

	public TimeEntryActivityTest() {
		super(TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
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
			mActivity.getLstnCreateTimeEntryWithDescription().onClick(mView);
			assertEquals(String.format(resourceString, dao.queryForAll().size()), (String) mView.getText());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@UiThreadTest
	public void testCreateTimeEntries() {
		try {
			Dao<TimeEntry, ?> dao = getActivity().getHelper().getDao(TimeEntry.class);
			int count = dao.queryForAll().size();
			mActivity.getLstnCreateTimeEntryWithDescription().onClick(mView);
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
