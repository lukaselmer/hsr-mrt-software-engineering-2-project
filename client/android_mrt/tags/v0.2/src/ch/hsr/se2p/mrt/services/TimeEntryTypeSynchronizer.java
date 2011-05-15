package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import ch.hsr.se2p.mrt.application.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import ch.hsr.se2p.mrt.network.SynchronizationException;
import ch.hsr.se2p.mrt.network.TimeEntryTypeHelper;

import com.j256.ormlite.dao.Dao;

class TimeEntryTypeSynchronizer implements Synchronizer {
	private static final String TAG = TimeEntryTypeSynchronizer.class.getSimpleName();

	private final DatabaseHelper databaseHelper;
	private final MRTApplication mrtApplication;

	public TimeEntryTypeSynchronizer(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void synchronize() {
		Log.d(TAG, "Synchronizing time_entry_types");
		try {
			Dao<TimeEntryType, Integer> dao = databaseHelper.getTimeEntryTypeDao();
			List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
			synchronizeTimeEntryTypes(dao, new TimeEntryTypeHelper(mrtApplication.getHttpHelper()), receivables);
		} catch (SQLException e) {
			Log.e(TAG, "Database error", e);
		} catch (Exception e) {
			Log.e(TAG, "TimeEntryType sync error", e);
		}
	}

	protected void synchronizeTimeEntryTypes(Dao<TimeEntryType, Integer> dao, TimeEntryTypeHelper ch, List<Receivable> receivables)
			throws SQLException {
		try {
			if (ch.synchronize(receivables, TimeEntryType.class)) {
				for (Receivable receivable : receivables) {
					processTimeEntryType(dao, (TimeEntryType) receivable);
				}
			}
		} catch (SynchronizationException e) {
			Log.e(TAG, "Error synchronizing TimeEntryTypes", e);
		}
	}

	protected void processTimeEntryType(Dao<TimeEntryType, Integer> dao, TimeEntryType t) throws SQLException {
		if (handleDeletion(dao, t))
			return;

		if (needsUpdating(t)) {
			handleUpdate(dao, t);
		} else {
			handleCreation(dao, t);
		}
	}

	private boolean needsUpdating(TimeEntryType t) {
		return t.getId() != null && t.getId() > 0;
	}

	private void handleUpdate(Dao<TimeEntryType, Integer> dao, TimeEntryType t) throws SQLException {
		Log.d(TAG, "Updating " + t);
		dao.update(t);
	}

	private void handleCreation(Dao<TimeEntryType, Integer> dao, TimeEntryType t) throws SQLException {
		Log.d(TAG, "Creating " + t);
		dao.create(t);
	}

	private boolean handleDeletion(Dao<TimeEntryType, Integer> dao, TimeEntryType t) throws SQLException {
		if (t.isDeleted()) {
			Log.d(TAG, "Deleting " + t);
			if (needsUpdating(t)) {
				dao.delete(t);
				return true;
			}
		}
		return false;
	}
}