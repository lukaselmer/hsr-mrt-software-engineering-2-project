package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.models.GpsPosition;

public class PositionSynchronizer implements Synchronizer {
	private static final String TAG = PositionSynchronizer.class.getSimpleName();

	private final DatabaseHelper databaseHelper;
	private final MRTApplication mrtApplication;

	public PositionSynchronizer(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void synchronize() {
		// TODO: save GPS position
		try {
			Location location = mrtApplication.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.i(TAG, "New location is " + location);
			if (location != null) {
				databaseHelper.getDao(GpsPosition.class).create(new GpsPosition(location));
			}
		} catch (SQLException e) {
			Log.e(TAG, "SQL Exception", e);
		}
	}

}
