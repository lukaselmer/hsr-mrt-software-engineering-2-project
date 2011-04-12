package ch.hsr.se2p.mrt.test.network;

import java.sql.Timestamp;

import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.network.HttpTransmitter;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;

public class HttpTransmitterTest extends AndroidTestCase {
	private HttpTransmitter transmitter;

	@Override
	protected void setUp() throws Exception {
		transmitter = new HttpTransmitter();
	}

	public void testTransmission() {
		assertTrue(true);
		TimeEntry timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		timeEntry.setDescription("bla");
		assertTrue(transmitter.transmit(timeEntry));
	}
}
