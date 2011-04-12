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

	@Override
	protected void setUp() throws Exception {
		transmitter = new HttpTransmitter() {
			protected HttpClient getHttpClient() {
				return new MockHttpClient() {
					public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
						return new MockHttpResponse(){
							public HttpEntity getEntity() {
								//return new MockHttpEntity();
								try {
									return new StringEntity("XXXXXX");
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
		assertTrue(true);
		TimeEntry timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
		timeEntry.setTimeStop(new Timestamp(System.currentTimeMillis()));
		timeEntry.setDescription("bla");
		
		assertTrue(transmitter.transmit(timeEntry));
	}
}
