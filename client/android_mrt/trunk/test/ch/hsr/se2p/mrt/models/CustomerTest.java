package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

public class CustomerTest extends AndroidTestCase {

	private static final String PHONE = "+41 444 44 44", FIRST_NAME = "Test", LAST_NAME = "customer";
	private Customer customer;
	private JSONObject customerObj;
	private Timestamp timeStamp;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		customer = new Customer();
		timeStamp = new Timestamp(System.currentTimeMillis());
		customerObj = new JSONObject().put("customer", getCustomerJSON());
	}

	private JSONObject getCustomerJSON() throws JSONException {

		return new JSONObject().put("id", 1).put("first_name", FIRST_NAME).put("last_name", LAST_NAME).put("phone", PHONE)
				.put("updated_at", DateHelper.format(timeStamp));
	}

	public void testInitialCustomer() {
		// assertEquals(new Integer(0), customer.getId());
		// assertEquals(0, customer.getIdOnServer());
		assertNull(customer.getFirstName());
		assertNull(customer.getLastName());
		assertNull(customer.getPhone());
		assertNull(customer.getPosition());
		assertEquals(new Timestamp(0), customer.getUpdatedAt());
		assertFalse(customer.isDeleted());
	}

	public void testFromJSON() {
		try {
			customer.fromJSON(customerObj);
			// assertEquals(new Integer(0), customer.getId());
			assertEquals(1, customer.getIdOnServer());
			assertEquals(FIRST_NAME, customer.getFirstName());
			assertEquals(LAST_NAME, customer.getLastName());
			assertEquals(PHONE, customer.getPhone());
			assertEquals(timeStamp, customer.getUpdatedAt());
			assertEquals(FIRST_NAME + " " + LAST_NAME, customer.toString());
		} catch (JSONException e) {
			assert (false);
			e.printStackTrace();
		}
	}
}
