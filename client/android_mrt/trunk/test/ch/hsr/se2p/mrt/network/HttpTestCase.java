package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.network.mocks.MockHttpClient;
import ch.hsr.se2p.mrt.network.mocks.MockHttpResponse;

public class HttpTestCase extends AndroidTestCase {
	HttpHelper httpHelper;

	protected void expectedResultFromTransmitter(final String result) {
		httpHelper = new HttpHelper() {
			@Override
			public HttpClient getHttpClient() {
				return new MockHttpClient() {
					@Override
					public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
						return new MockHttpResponse() {
							@Override
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

}