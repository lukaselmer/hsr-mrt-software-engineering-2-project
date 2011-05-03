package ch.hsr.se2p.mrt.services;

import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;

public class PositionSynchronizer implements Synchronizer {

	private final DatabaseHelper databaseHelper;
	private final MRTApplication mrtApplication;

	public PositionSynchronizer(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void synchronize() {
		// TODO: save GPS position
	}

}
