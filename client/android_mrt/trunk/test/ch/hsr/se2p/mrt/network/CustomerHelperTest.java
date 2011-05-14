package ch.hsr.se2p.mrt.network;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.ISO8601DateParserHelperForTests;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.ISO8601DateParser;

public class CustomerHelperTest extends HttpTestCase {

	private ArrayList<Customer> customers;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		customers = new ArrayList<Customer>();
		customers.add(getCustomer("Peter", "Muster", 1, "", getTimestamp(27)));
		customers.add(getCustomer("Hans", "Meier", 2, "", getTimestamp(33)));
	}

	public void testInitialSynchronization() throws Exception {
		expectedResultFromTransmitter(responseFor(customers));
		CustomerHelper ch = new CustomerHelper(httpHelper);
		List<Receivable> l = new ArrayList<Receivable>();
		ch.synchronize(l, Customer.class);
		assertSameList(customers, l);
	}

	public void testAddCustomerSynchronization() throws Exception {
		testInitialSynchronization();
		customers.add(getCustomer("Maria", "Müller", 77, "777", getTimestamp(System.currentTimeMillis() - 1000 * 60 * 60)));
		expectedResultFromTransmitter(responseFor(customers));
		CustomerHelper ch = new CustomerHelper(httpHelper);
		List<Receivable> l = new ArrayList<Receivable>();
		ch.synchronize(l, Customer.class);
		assertSameList(customers, l);
	}

	public void testUpdateCustomerSynchronization() throws Exception {
		testInitialSynchronization();
		customers.remove(0);
		customers.add(getCustomer("Peter", "SUPERMUSTER", 1, "", getTimestamp(77)));
		expectedResultFromTransmitter(responseFor(customers));
		CustomerHelper ch = new CustomerHelper(httpHelper);
		List<Receivable> l = new ArrayList<Receivable>();
		ch.synchronize(l, Customer.class);
		assertSameList(customers, l);
	}

	public void testDeleteCustomerSynchronization() throws Exception {
		testInitialSynchronization();
		customers.remove(0);
		customers.add(getCustomer("Peter", "SUPERMUSTER", 77, "", getTimestamp(10), true));
		expectedResultFromTransmitter(responseFor(customers));
		CustomerHelper ch = new CustomerHelper(httpHelper);
		List<Receivable> l = new ArrayList<Receivable>();
		ch.synchronize(l, Customer.class);
		assertSameList(customers, l);
		assertTrue(customers.get(1).isDeleted());
	}

	protected void assertSameList(ArrayList<Customer> customers, List<Receivable> l) {
		assertEquals(customers.size(), l.size());
		for (Receivable receivable : l) {
			assertTrue(receivable instanceof Customer);
			Customer c1 = (Customer) receivable, c2 = null;
			for (Customer c : customers) {
				if (c.getIdOnServer() == c1.getIdOnServer()) {
					c2 = c;
					break;
				}
			}
			assertEqualCustomer(c1, c2);
		}
	}

	private void assertEqualCustomer(Customer c1, Customer c2) {
		if (c1 == null && c2 == null) {
			return;
		}
		assertNotNull(c1);
		assertNotNull(c2);
		assertEquals(c1.getFirstName(), c2.getFirstName());
		assertEquals(c1.getLastName(), c2.getLastName());
		assertEquals(c1.getIdOnServer(), c2.getIdOnServer());
		assertEquals(c1.getPhone(), c2.getPhone());
		assertEquals(c1.getUpdatedAt(), c2.getUpdatedAt());
		assertEquals(c1.getId(), c2.getId());
		assertEquals(c1.getGpsPositionId(), c2.getGpsPositionId());
	}

	private String responseFor(ArrayList<Customer> customers) throws JSONException {
		JSONArray a = new JSONArray();
		for (Customer customer : customers) {
			a.put(new JSONObject().put("customer", getCustomerJSON(customer)));
		}
		return a.toString();
	}

	private JSONObject getCustomerJSON(Customer customer) throws JSONException {
		return new JSONObject().put("first_name", customer.getFirstName()).put("last_name", customer.getLastName())
				.put("id", customer.getIdOnServer()).put("phone", customer.getPhone()).put("updated_at", ISO8601DateParser.toString(customer.getUpdatedAt()));
	}

	private Customer getCustomer(String fistName, String lastName, int railsId, String phone, Timestamp updatedAt) throws JSONException {
		return getCustomer(fistName, lastName, railsId, phone, updatedAt, false);

	}

	private Customer getCustomer(String fistName, String lastName, int railsId, String phone, Timestamp updatedAt, boolean deleted)
			throws JSONException {
		Customer c = new Customer();
		c.fromJSON(getCustomerInJson(fistName, lastName, railsId, phone, updatedAt, deleted));
		return c;
	}

	private JSONObject getCustomerInJson(String fistName, String lastName, int railsId, String phone, Timestamp updatedAt, boolean deleted)
			throws JSONException {
		JSONObject customerObj = new JSONObject();
		customerObj.put("id", railsId);
		customerObj.put("first_name", fistName);
		customerObj.put("last_name", lastName);
		customerObj.put("phone", phone);
		customerObj.put("updated_at", ISO8601DateParser.toString(updatedAt));
		customerObj.put("gpsPosition", "");
		if (deleted)
			customerObj.put("deleted_at", ISO8601DateParser.toString(getTimestamp(System.currentTimeMillis() - 1000 * 60 * 60)));
		return customerObj;
	}

	private Timestamp getTimestamp(long l) {
		return ISO8601DateParserHelperForTests.formatAndParseToTimestamp(new Timestamp(l));
	}

}
