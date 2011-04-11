package ch.hsr.se2p.mrt.test.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.activities.TimeEntryActivity;

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

	public void testPreconditions() {
		assertNotNull(mView);
	}

	public void testText() {
		assertEquals(resourceString, (String) mView.getText());
		// assertEquals(resourceString, (String) mView.getText());
	}
}
