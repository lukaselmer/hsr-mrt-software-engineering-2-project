package ch.hsr.se2p.mrt.test.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.network.HttpTransmitter;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;
import ch.hsr.se2p.mrt.test.mocks.MockHttpClient;
import ch.hsr.se2p.mrt.test.mocks.MockHttpEntity;
import ch.hsr.se2p.mrt.test.mocks.MockHttpResponse;

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
