package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;
import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.network.TimeEntryHelper;

import com.j256.ormlite.dao.Dao;

class TimeEntrySynchronizer implements Synchronizer {
	private static final String TAG = TimeEntrySynchronizer.class.getSimpleName();

	private final DatabaseHelper databaseHelper;
	private final MRTApplication mrtApplication;

	public TimeEntrySynchronizer(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void synchronize() {
		try {
			Log.d(TAG, "Transmitting TimeEntries...");
			List<TimeEntry> timeEntries = getTimeEntriesToTransmit();
			if (timeEntries.size() == 0) {
				Log.d(TAG, "Transmission finished, no TimeEntries found.");
				return;
			}
			transmitTimeEnties(timeEntries);
			Log.d(TAG, "Transmission finished");
		} catch (SQLException e) {
			Log.e(TAG, "Database excaeption", e);
		}
	}

	private void transmitTimeEnties(List<TimeEntry> timeEntries) throws SQLException {
		TimeEntryHelper timeEntryHelper = new TimeEntryHelper(mrtApplication.getHttpHelper());

		for (TimeEntry timeEntry : timeEntries) {
			setRelationObjectsOnTimeEntry(timeEntry);
			Log.d(TAG, "Transmitting " + timeEntry + "...");
			if (timeEntryHelper.transmit(timeEntry)) {
				handleTransmittedTimeEntry(databaseHelper.getTimeEntryDao(), timeEntryHelper, timeEntry);
			} else
				break;
		}
	}

	private void setRelationObjectsOnTimeEntry(TimeEntry timeEntry) throws SQLException {
		setCustomerRelationOnTimeEntry(timeEntry);
		setGpsPositionRelationOnTimeEntry(timeEntry);
		setTimeEntryTypeRelationOnTimeEntry(timeEntry);
	}

	private void setTimeEntryTypeRelationOnTimeEntry(TimeEntry timeEntry) throws SQLException {
		if (timeEntry.hasTimeEntryType())
			timeEntry.setTimeEntryType(databaseHelper.getTimeEntryTypeDao().queryForId(timeEntry.getTimeEntryTypeId()));
	}

	private void setGpsPositionRelationOnTimeEntry(TimeEntry timeEntry) throws SQLException {
		if (timeEntry.hasGpsPosition())
			timeEntry.setGpsPosition(databaseHelper.getGpsPositionDao().queryForId(timeEntry.getGpsPositionId()));
	}

	private void setCustomerRelationOnTimeEntry(TimeEntry timeEntry) throws SQLException {
		if (timeEntry.hasCustomer())
			timeEntry.setCustomer(databaseHelper.getCustomerDao().queryForId(timeEntry.getCustomerId()));
	}

	private void handleTransmittedTimeEntry(Dao<TimeEntry, Integer> timeEntryDao, TimeEntryHelper timeEntryHelper, TimeEntry timeEntry)
			throws SQLException {
		setTransmitted(timeEntryDao, timeEntry);
		if (timeEntryHelper.confirm(timeEntry)) {
			timeEntryDao.delete(timeEntry);
			Log.d(TAG, "OK: " + timeEntry + "");
		}
	}

	private void setTransmitted(Dao<TimeEntry, Integer> timeEntryDao, TimeEntry timeEntry) throws SQLException {
		if (!timeEntry.isTransmitted()) {
			timeEntry.setTransmitted();
			timeEntryDao.update(timeEntry);
		}
	}

	private List<TimeEntry> getTimeEntriesToTransmit() throws SQLException {
		Dao<TimeEntry, Integer> timeEntryDao = databaseHelper.getTimeEntryDao();
		return timeEntryDao.queryForAll();
	}

}
