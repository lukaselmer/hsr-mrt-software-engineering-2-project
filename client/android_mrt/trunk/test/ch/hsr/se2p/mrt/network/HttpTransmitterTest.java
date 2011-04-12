package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.mocks.MockHttpClient;
import ch.hsr.se2p.mrt.mocks.MockHttpResponse;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;

public class HttpTransmitterTest extends AndroidTestCase {
	private HttpTransmitter transmitter;
	private TimeEntry timeEntry;

	@Override
	protected void setUp() throws Exception {
		timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		timeEntry.setDescription("bla");
		expectedResultFromTransmitter("{\"time_entry\":{\"created_at\":\"2011-04-12T21:57:32+02:00\",\"time_stop\":\""
				+ "2011-04-12T23:57:28+02:00\",\"time_entry_type_id\":null,\"updated_at\":\"2011-04-12T21:57:32+02:00\"," + "\"audio_record_name\":null,\"hashcode\":\"" + timeEntry.getHashcode()
				+ "\",\"id\":165,\"customer_id\":null," + "\"description\":null,\"position_id\":null,\"time_start\":\"2011-04-12T19:57:28+02:00\"}}");
	}

	private void expectedResultFromTransmitter(final String result) {
		transmitter = new HttpTransmitter() {
			protected HttpClient getHttpClient() {
				return new MockHttpClient() {
					public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
						return new MockHttpResponse() {
							public HttpEntity getEntity() {
								// return new MockHttpEntity();
								try {
									return new StringEntity(result);
								} catch (UnsupportedEncodingException e) {
								}
								return null;
							};
						};
					};
				};
			};
		};
	}

	public void testTransmission() {
		assertTrue(transmitter.transmit(timeEntry));
	}
}
