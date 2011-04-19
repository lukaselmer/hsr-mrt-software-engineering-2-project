package ch.hsr.se2p.mrt.network;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.User;

public class CustomerHelperTest extends HttpTestCase {

	public void testLogin() throws JSONException {
		Customer c = getCustomer("Peter", "Muster", 1, "", 1);

//		User u = new User();
//		String firstName = "Peter", lastName = "Muster", email = "peter@muster.ch";
//		Integer id = 77;
//		expectedResultFromTransmitter("{\"user\":{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"email\":\"" + email + "\",\"id\":" + id + "}}");
//		UserHelper userHelper = new UserHelper(httpHelper);
//		assertTrue(userHelper.login("validlogin", "validpassword", u));
//		assertEquals(firstName, u.getFirstName());
//		assertEquals(lastName, u.getLastName());
//		assertEquals(email, u.getEmail());
//		assertEquals(id, u.getRailsId());
	}

	private Customer getCustomer(String fistName, String lastName, int railsId, String phone, long updatedAt) throws JSONException {
		JSONObject customerObj = new JSONObject();
		customerObj.put("id", railsId);
		customerObj.put("first_name", fistName);
		customerObj.put("last_name", lastName);
		customerObj.put("phone", phone);
		customerObj.put("updated_at", updatedAt);
		customerObj.put("position", "");
		Customer c = new Customer();
		c.fromJSON(customerObj);
		return c;
	}

	// public void testLoginFails() {
	// User u = new User();
	// expectedResultFromTransmitter("{}");
	// UserHelper userHelper = new UserHelper(httpHelper);
	// assertFalse(userHelper.login("invalidlogin", "invalidpassword", u));
	// }

}
