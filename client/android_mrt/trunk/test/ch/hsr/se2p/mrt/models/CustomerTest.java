package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

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
				.put("updated_at", ISO8601DateParser.format(timeStamp));
	}

	public void testInitialCustomer() {
		assertEquals(Integer.valueOf(0), customer.getId());
		assertEquals(0, customer.getIdOnServer());
		assertNull(customer.getFirstName());
		assertNull(customer.getLastName());
		assertNull(customer.getPhone());
		assertNull(customer.getGpsPositionId());
		assertEquals(new Timestamp(0), customer.getUpdatedAt());
		assertFalse(customer.isDeleted());
	}

	public void testFromJSON() {
		try {
			customer.fromJSON(customerObj);
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

	public void testCustomerSort() {
		Set<Customer> set = new TreeSet<Customer>();
		set.add(new Customer("Jasper", "Zazoo", "+41 444 44 11", null));
		set.add(new Customer("Duelue", "Anton", "+41 444 44 22", null));
		set.add(new Customer("The", "Zombie", "+41 444 44 33", 100.0));
		set.add(new Customer("Klara", "Wayne", "+41 444 44 44", 499.9));
		set.add(new Customer("Fritz", "Arnold", "+41 444 44 55", 800.0));
		set.add(new Customer("Hans", "Peter", "+41 444 44 66", 500.0));
		Object[] custArray = set.toArray();
		assertEquals("Zombie", ((Customer) custArray[0]).getLastName());
		assertEquals("Peter", ((Customer) custArray[1]).getLastName());
		assertEquals("Wayne", ((Customer) custArray[2]).getLastName());
		assertEquals("Arnold", ((Customer) custArray[3]).getLastName());
		assertEquals("Anton", ((Customer) custArray[4]).getLastName());
		assertEquals("Zazoo", ((Customer) custArray[5]).getLastName());
	}
}
