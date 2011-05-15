package ch.hsr.se2p.mrt.database;

import java.sql.SQLException;

import android.test.AndroidTestCase;
import ch.hsr.se2p.mrt.database.mocks.DatabaseHelperMock;

public class DatabaseHelperTest extends AndroidTestCase {

	public void testCreateDatabase() throws SQLException {
		{
			DatabaseHelperMock d = new DatabaseHelperMock(getContext());
			d.deleteDatabase();
			d.close();
		}
		int dbCount = getContext().databaseList().length;
		DatabaseHelper d = new DatabaseHelper(getContext());
		d.getWritableDatabase().close();
		assertEquals(dbCount + 1, getContext().databaseList().length);
	}
}
