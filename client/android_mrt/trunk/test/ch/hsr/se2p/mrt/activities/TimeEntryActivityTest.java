package ch.hsr.se2p.mrt.activities;

import java.sql.SQLException;
import java.sql.Timestamp;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;

import com.j256.ormlite.dao.Dao;
import com.jayway.android.robotium.solo.Solo;

public class TimeEntryActivityTest extends ActivityInstrumentationTestCase2<TimeEntryActivity> {
	private TextView mView;
	private String resourceString;
	private TimeEntryActivity mActivity;
	private Solo solo;

	public TimeEntryActivityTest() {
		super("ch.hsr.se2p.mrt", TimeEntryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getActivity().getHelper().reset();
		mActivity = getActivity();
		mView = (TextView) mActivity.findViewById(R.id.textview);
		resourceString = mActivity.getString(R.string.txtWelcome);
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
		// getActivity().deleteDatabase(TEST_DB_NAME);
	}

	public void testPreconditions() {
		assertNotNull(mView);
	}

	@UiThreadTest
	public void testWelcomeText() {
		try {
			getActivity().updateView();
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
	// TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis()));
	// t.setTimeStop(new Timestamp(System.currentTimeMillis()));
	// dao.create(t);
	// int count = dao.queryForAll().size();
	// mActivity.getLstnSendTimeEntries().onClick(mView);
	// solo.sleep(500);
	// // new MockContentProvider().
	// assertEquals(0, dao.queryForAll().size());
	// } catch (SQLException e) {
	// e.printStackTrace();
	// assert (false);
	// }
	// }
}
