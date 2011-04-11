package ch.hsr.se2p.mrt.models;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	public static final String TAG = DatabaseHelper.class.getSimpleName();

	private static final String DATABASE_NAME = "mrt.db";
	private static final int DATABASE_VERSION = 2;

	// the DAO object we use to access the TimeEntry table
	private Dao<TimeEntry, Integer> timeEntryDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Creating database");
			TableUtils.createTable(connectionSource, TimeEntry.class);
		} catch (SQLException e) {
			Log.e(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "Upgrading database -> drop + create");
			TableUtils.dropTable(connectionSource, TimeEntry.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<TimeEntry, Integer> getTimeEntryDao() throws SQLException {
		if (timeEntryDao == null)
			timeEntryDao = getDao(TimeEntry.class);
		return timeEntryDao;
	}

	@Override
	public void close() {
		super.close();
		timeEntryDao = null;
	}
}
