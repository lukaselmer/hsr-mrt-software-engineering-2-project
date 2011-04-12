package ch.hsr.se2p.mrt.test.network;

import ch.hsr.se2p.mrt.network.HttpTransmitter;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class HttpTransmitterTest extends AndroidTestCase {
	private HttpTransmitter transmitter;

	@Override
	protected void setUp() throws Exception {
		MockContext c = new MockContext();
		setContext(c);
		transmitter = new HttpTransmitter();
	}

	public void testBla() {
		assertTrue(true);
	}
}
