package ch.hsr.se2p.mrt.services;

import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;

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
		// Log.d(TAG, "Synchronizing time entry types");
		// try {
		// Dao<TimeEntryType, Integer> dao = databaseHelper.getDao(TimeEntryType.class);
		// List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
		// // TODO: sync time entry types: xsynchronizeReceivables(dao, new CustomerHelper(mrtApplication.getHttpHelper()), receivables);
		// } catch (SQLException e) {
		// Log.e(TAG, "Database error", e);
		// }
	}

}
