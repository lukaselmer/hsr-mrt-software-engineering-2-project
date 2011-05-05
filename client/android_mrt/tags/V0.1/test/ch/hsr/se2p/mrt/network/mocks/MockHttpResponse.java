package ch.hsr.se2p.mrt.network.mocks;

import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.params.HttpParams;

public class MockHttpResponse implements HttpResponse {

	@Override
	public ProtocolVersion getProtocolVersion() {
		return null;
	}

	@Override
	public boolean containsHeader(String name) {
		return false;
	}

	@Override
	public Header[] getHeaders(String name) {
		return null;
	}

	@Override
	public Header getFirstHeader(String name) {
		return null;
	}

	@Override
	public Header getLastHeader(String name) {
		return null;
	}

	@Override
	public Header[] getAllHeaders() {
		return null;
	}

	@Override
	public void addHeader(Header header) {

	}

	@Override
	public void addHeader(String name, String value) {

	}

	@Override
	public void setHeader(Header header) {

	}

	@Override
	public void setHeader(String name, String value) {

	}

	@Override
	public void setHeaders(Header[] headers) {

	}

	@Override
	public void removeHeader(Header header) {

	}

	@Override
	public void removeHeaders(String name) {

	}

	@Override
	public HeaderIterator headerIterator() {
		return null;
	}

	@Override
	public HeaderIterator headerIterator(String name) {
		return null;
	}

	@Override
	public HttpParams getParams() {
		return null;
	}

	@Override
	public void setParams(HttpParams params) {

	}

	@Override
	public StatusLine getStatusLine() {
		return null;
	}

	@Override
	public void setStatusLine(StatusLine statusline) {

	}

	@Override
	public void setStatusLine(ProtocolVersion ver, int code) {

	}

	@Override
	public void setStatusLine(ProtocolVersion ver, int code, String reason) {

	}

	@Override
	public void setStatusCode(int code) throws IllegalStateException {

	}

	@Override
	public void setReasonPhrase(String reason) throws IllegalStateException {

	}

	@Override
	public HttpEntity getEntity() {
		return null;
	}

	@Override
	public void setEntity(HttpEntity entity) {

	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public void setLocale(Locale loc) {

	}

}
