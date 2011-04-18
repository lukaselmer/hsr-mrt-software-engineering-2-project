package ch.hsr.se2p.mrt.network;

import ch.hsr.se2p.mrt.models.User;

public class UserHelperTest extends HttpTestCase {

	public void testLogin() {
		User u = new User();
		String firstName = "Peter", lastName = "Muster", email = "peter@muster.ch";
		Integer id = 77;
		expectedResultFromTransmitter("{\"user\":{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"email\":\"" + email + "\",\"id\":" + id + "}}");
		UserHelper userHelper = new UserHelper(httpTransmitter);
		assertTrue(userHelper.login("validlogin", "validpassword", u));
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(email, u.getEmail());
		assertEquals(id, u.getRailsId());
	}

	public void testLoginFails() {
		User u = new User();
		expectedResultFromTransmitter("{}");
		UserHelper userHelper = new UserHelper(httpTransmitter);
		assertFalse(userHelper.login("invalidlogin", "invalidpassword", u));
	}

}
