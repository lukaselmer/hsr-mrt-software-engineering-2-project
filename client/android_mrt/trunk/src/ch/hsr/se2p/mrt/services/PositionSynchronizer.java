package ch.hsr.se2p.mrt.services;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
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
		Location l = mrtApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

}
