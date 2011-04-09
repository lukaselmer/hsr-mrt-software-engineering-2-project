package ch.hsr.se2p.mrt.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.activities.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private TextView mView;
	private String resourceString;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		mView = (TextView) mActivity.findViewById(R.id.textview);
		resourceString = mActivity.getString(R.string.hello);
	}

	public void testPreconditions() {
		assertNotNull(mView);
	}

	public void testText() {
		assertEquals(resourceString, (String) mView.getText());
		// assertEquals(resourceString, (String) mView.getText());
	}
}
