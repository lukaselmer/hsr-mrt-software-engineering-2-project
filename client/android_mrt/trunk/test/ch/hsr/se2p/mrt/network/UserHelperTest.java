package ch.hsr.se2p.mrt.network;

import java.sql.Timestamp;

import ch.hsr.se2p.mrt.ISO8601DateParserHelperForTests;
import ch.hsr.se2p.mrt.models.User;

public class UserHelperTest extends HttpTestCase {

	public void testLogin() {
		User u = new User();
		String firstName = "Peter", lastName = "Muster", email = "peter@muster.ch";
		int id = 77;
		Timestamp updatedAt = ISO8601DateParserHelperForTests.formatAndParseToTimestamp(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
		expectedResultFromTransmitter("{\"field_worker\":{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"email\":\""
				+ email + "\",\"id\":" + id + ",\"updated_at\":\"" + ISO8601DateParserHelperForTests.toString(updatedAt) + "\"}}");
		UserHelper userHelper = new UserHelper(httpHelper);
		assertTrue(userHelper.login("validlogin", "validpassword", u));
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(email, u.getEmail());
		assertEquals(id, u.getRailsId());
		assertEquals(updatedAt, u.getUpdatedAt());
	}

	public void testLoginFails() {
		User u = new User();
		expectedResultFromTransmitter("{}");
		UserHelper userHelper = new UserHelper(httpHelper);
		assertFalse(userHelper.login("invalidlogin", "invalidpassword", u));
	}

	public void testLoginWithServer() {
		httpHelper = new HttpHelper();
		User u = new User();
		UserHelper userHelper = new UserHelper(httpHelper);
		assertTrue(userHelper.login("field_worker@mrt.ch", "mrt", u));
		assertTrue(u.getUpdatedAt().getTime() > 1);
		String firstName = "Fredi", lastName = "Worker", email = "field_worker@mrt.ch";
		Integer id = 2;
		// Timestamp updatedAt = new Timestamp(Timestamp.parse(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60).toGMTString()));
		// expectedResultFromTransmitter("{\"user\":{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"email\":\"" + email
		// + "\",\"id\":" + id + ",\"updated_at\":\"" + updatedAt.toGMTString() + "\"}}");
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(email, u.getEmail());
		assertSame(id, u.getRailsId());
		// assertEquals(updatedAt, u.getUpdatedAt());
	}

	public void testLoginFailsWithServer() {
		httpHelper = new HttpHelper();
		User u = new User();
		UserHelper userHelper = new UserHelper(httpHelper);
		assertFalse(userHelper.login("invalidlogin", "invalidpassword", u));
	}

}
