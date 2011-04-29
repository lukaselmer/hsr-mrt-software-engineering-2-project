package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

public class UserTest extends AndroidTestCase {

	private static final String EMAIL = "field_worker@mrt.ch";
	private static final String LAST_NAME = "worker";
	private static final String FIRST_NAME = "field";
	private User user;
	private Timestamp timeStamp;
	private JSONObject userObj;

	@Override
	public void setUp() {
		user = new User();
		timeStamp = new Timestamp(System.currentTimeMillis());
		try {
			userObj = new JSONObject().put("user", getUserJSON());
		} catch (JSONException e) {
			assert (false);
			e.printStackTrace();
		}
	}

	private JSONObject getUserJSON() throws JSONException {
		return new JSONObject().put("id", 1).put("first_name", FIRST_NAME).put("last_name", LAST_NAME).put("email", EMAIL)
				.put("updated_at", DateHelper.format(timeStamp));
	}
	
	public void testInitialUser(){
//		assertNull(user.getId());
		assertNull(user.getIdOnServer());
		assertNull(user.getFirstName());
		assertNull(user.getLastName());
		assertNull(user.getEmail());
		assertEquals(new Timestamp(0), user.getUpdatedAt());
	}
	
	public void testFromJSON(){
		try {
			user.fromJSON(userObj);
//			assertEquals(new Integer(0), user.getId());
			assertEquals(1, user.getIdOnServer());
			assertEquals(FIRST_NAME, user.getFirstName());
			assertEquals(LAST_NAME, user.getLastName());
			assertEquals(EMAIL, user.getEmail());
			assertEquals(DateHelper.format(timeStamp), user.getUpdatedAt());
		} catch (JSONException e) {
			e.printStackTrace();
			assert(false);
		}
	}
}
