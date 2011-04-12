package ch.hsr.se2p.mrt.test.persistence;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.j256.ormlite.dao.Dao;

import ch.hsr.se2p.mrt.persistence.database.DatabaseHelper;
import ch.hsr.se2p.mrt.persistence.models.TimeEntry;
import android.test.AndroidTestCase;

public class TimeEntryTest extends AndroidTestCase {
	private Dao<TimeEntry, Integer> dao;
	private DatabaseHelper dh;

	@Override
	protected void setUp() throws Exception {
		dh = new DatabaseHelper(getContext());
		dh.reset();
		dao = dh.getDao(TimeEntry.class);
	}

	@Override
	protected void tearDown() throws Exception {
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
			int id = dao.create(t);
			t.setDescription("bla");
			t.setTimeStop(new Timestamp(System.currentTimeMillis()));
			assertEquals(1, dao.queryForAll().size());
			t = dao.queryForId(id);
			assertNotNull(t);
			assertEquals(id, (int) t.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			assert (false);
		}
	}
}
