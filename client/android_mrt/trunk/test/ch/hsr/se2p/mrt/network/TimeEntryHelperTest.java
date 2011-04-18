package ch.hsr.se2p.mrt.network;

import java.sql.Timestamp;

import ch.hsr.se2p.mrt.models.TimeEntry;

public class TimeEntryHelperTest extends HttpTestCase {

	// TODO: implement more tests
	
	public void testTimeEntryTransmission() {
		TimeEntry timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		timeEntry.setDescription("bla");
		expectedResultFromTransmitter("{\"time_entry\":{\"created_at\":\"2011-04-12T21:57:32+02:00\",\"time_stop\":\""
				+ "2011-04-12T23:57:28+02:00\",\"time_entry_type_id\":null,\"updated_at\":\"2011-04-12T21:57:32+02:00\"," + "\"audio_record_name\":null,\"hashcode\":\"" + timeEntry.getHashcode()
				+ "\",\"id\":165,\"customer_id\":null," + "\"description\":null,\"position_id\":null,\"time_start\":\"2011-04-12T19:57:28+02:00\"}}");
		TimeEntryHelper timeEntryHelper = new TimeEntryHelper(httpHelper);
		assertTrue(timeEntryHelper.transmit(timeEntry));
	}
}
