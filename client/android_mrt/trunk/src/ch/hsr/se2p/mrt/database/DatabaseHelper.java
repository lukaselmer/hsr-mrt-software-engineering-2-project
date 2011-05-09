package ch.hsr.se2p.mrt.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.GpsPosition;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	public static final String TAG = DatabaseHelper.class.getSimpleName();

	protected static final String DATABASE_NAME = "mrt.db";
	protected static final int DATABASE_VERSION = 5;
	private static final Class<?> MODEL_CLASSES[] = { TimeEntry.class, Customer.class, TimeEntryType.class, GpsPosition.class };

	private Dao<TimeEntry, Integer> timeEntryDao;
	private Dao<Customer, Integer> customerDao;
	private Dao<TimeEntryType, Integer> timeEntryTypeDao;
	private Dao<GpsPosition, Integer> gpsPositionDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Creating database");
			for (Class<?> modelClass : MODEL_CLASSES) {
				TableUtils.createTable(connectionSource, modelClass);
			}
		} catch (SQLException e) {
			Log.e(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		reset(db, connectionSource);
	}

	public void reset() {
		SQLiteDatabase w = getWritableDatabase();
		reset(w, getConnectionSource());
	}

	public void reset(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Upgrading database -> drop + create");
			TableUtils.dropTable(connectionSource, TimeEntry.class, true);
			TableUtils.dropTable(connectionSource, Customer.class, true);
			TableUtils.dropTable(connectionSource, TimeEntryType.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public synchronized Dao<TimeEntry, Integer> getTimeEntryDao() throws SQLException {
		if (timeEntryDao == null)
			timeEntryDao = getDao(TimeEntry.class);
		return timeEntryDao;
	}

	public synchronized Dao<Customer, Integer> getCustomerDao() throws SQLException {
		if (customerDao == null)
			customerDao = getDao(Customer.class);
		return customerDao;
	}
	
	public synchronized Dao<GpsPosition, Integer> getGpsPositionDao() throws SQLException {
        if (gpsPositionDao == null)
            gpsPositionDao = getDao(GpsPosition.class);
        return gpsPositionDao;
    }

	public synchronized Dao<TimeEntryType, Integer> getTimeEntryTypeDao() throws SQLException {
		if (timeEntryTypeDao == null)
			timeEntryTypeDao = getDao(TimeEntryType.class);
		return timeEntryTypeDao;
	}

	@Override
	public void close() {
		timeEntryDao = null;
		customerDao = null;
		timeEntryTypeDao = null;
		super.close();
	}

}
