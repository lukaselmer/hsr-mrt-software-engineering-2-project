package ch.hsr.se2p.mrt.models;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;

import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;
import android.test.AndroidTestCase;

public class TimeEntryTest extends AndroidTestCase {
	private static final String DESCRIPTION = "text";
	private Dao<TimeEntry, Integer> dao;
	private DatabaseHelper dh;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dh = new DatabaseHelper(getContext());
		dh.reset();
		dao = dh.getDao(TimeEntry.class);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		dh.reset();
	}

	public void testCreate() {
		try {
			assertEquals(0, dao.queryForAll().size());
			TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis()));
			dao.create(t);
			assertEquals(1, dao.queryForAll().size());
			assertNotNull(t.getId());
			assertNotNull(t.getTimeStart());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testFind() {
		try {
			assertEquals(0, dao.queryForAll().size());
			TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60)); // 1h ago
			t.setDescription(DESCRIPTION);
			Timestamp timeStop = new Timestamp(System.currentTimeMillis());
			t.setTimeStop(timeStop);
			int id = dao.create(t);
			assertEquals(1, dao.queryForAll().size());
			t = dao.queryForId(id);
			assertNotNull(t);
			assertEquals(id, (int) t.getId());
			assertEquals(DESCRIPTION, t.getDescription());
			assertEquals(timeStop, t.getTimeStop());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testSetTransmitted() {
		try {
			assertEquals(0, dao.queryForAll().size());
			TimeEntry t = new TimeEntry(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60)); // 1h ago
			int id = dao.create(t);
			t.setTransmitted();
			t.setTimeStop(new Timestamp(System.currentTimeMillis()));
			assertEquals(1, dao.queryForAll().size());
			dao.update(t);
			t = dao.queryForId(id);
			assertNotNull(t);
			assertEquals(id, (int) t.getId());
			assertTrue(t.isTransmitted());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}

	public void testToJSON() {
		try {
			Timestamp timeStop = new Timestamp(System.currentTimeMillis()), timeStart = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60);
			TimeEntry t = new TimeEntry(timeStart);
			t.setTimeStop(timeStop);
			t.setDescription(DESCRIPTION);
			int id = dao.create(t);
			t = dao.queryForId(id);
			JSONObject j = t.toJSONObject();
			assertEquals(DESCRIPTION, j.getString("description"));
			assertEquals(timeStart, j.get("time_start"));
			assertEquals(timeStop, j.get("time_stop"));
			assertEquals(t.getHashcode(), j.getString("hashcode"));
		} catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (JSONException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	//TODO: Test processTransmission & processConfirmation
}
