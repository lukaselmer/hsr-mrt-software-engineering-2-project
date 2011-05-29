package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

public class TimeEntryTypeTest extends AndroidTestCase {
	private static final String NAME = "Wasserhahn reparieren";
	private TimeEntryType timeEntryType;
	private JSONObject timeEntryTypeObj;
	private Timestamp timeStamp;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		timeEntryType = new TimeEntryType();
		timeStamp = new Timestamp(System.currentTimeMillis());
		timeEntryTypeObj = new JSONObject().put("customer", getTimeEntryTypeJSON());
	}
	
	private JSONObject getTimeEntryTypeJSON() throws JSONException {
		return new JSONObject().put("id", 1).put("name", NAME).put("updated_at", ISO8601DateParser.format(timeStamp));
	}
	
	public void testFromJSON() {
		try {
			timeEntryType.fromJSON(timeEntryTypeObj);
			assertEquals(1, timeEntryType.getIdOnServer());
			assertEquals(NAME, timeEntryType.getName());
			assertEquals(timeStamp, timeEntryType.getUpdatedAt());
		} catch (JSONException e) {
			assert (false);
			e.printStackTrace();
		}
	}
	
	public void testTimeEntryTypeSort() {
		Set<TimeEntryType> set = new TreeSet<TimeEntryType>();
		set.add(new TimeEntryType(1, "Wasserzähler ersetzen"));
		set.add(new TimeEntryType(2, "Heizung ansehen"));
		set.add(new TimeEntryType(3, "Lavabo wechseln"));
		set.add(new TimeEntryType(4, "Keramikschüssel reparieren"));
		Object[] timeEntryTypeArray = set.toArray();
		assertEquals("Heizung ansehen", ((TimeEntryType) timeEntryTypeArray[0]).getName());
		assertEquals("Keramikschüssel reparieren", ((TimeEntryType) timeEntryTypeArray[1]).getName());
		assertEquals("Lavabo wechseln", ((TimeEntryType) timeEntryTypeArray[2]).getName());
		assertEquals("Wasserzähler ersetzen", ((TimeEntryType) timeEntryTypeArray[3]).getName());
	}
}
