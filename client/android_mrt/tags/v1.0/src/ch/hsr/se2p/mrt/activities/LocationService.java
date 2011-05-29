package ch.hsr.se2p.mrt.activities;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import ch.hsr.se2p.mrt.models.GpsPosition;

class LocationService {
	private final LocationManager locationManager;
	private final LocationListener locationListener = new LocationListenerAdapter() {
		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
			currentGPSPosition = location == null ? null : new GpsPosition(location);
			locationChangedAction.run();
		}
	};
	private final Runnable locationChangedAction;
	protected Location currentLocation;
	private GpsPosition currentGPSPosition;

	protected LocationService(LocationManager locationManager, Runnable locationChangedAction) {
		this.locationManager = locationManager;
		this.locationChangedAction = locationChangedAction;
		currentLocation = locationManager.getLastKnownLocation(getLocationProvider());
		// Update location maximal every 10 seconds
		locationManager.requestLocationUpdates(getLocationProvider(), 10 * 1000, 0, locationListener);
	}

	protected Location getCurrentLocation() {
		return currentLocation;
	}

	protected GpsPosition getCurrentGPSPosition() {
		return currentGPSPosition;
	}

	private String getLocationProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return locationManager.getBestProvider(criteria, true);
	}

	protected void stop() {
		locationManager.removeUpdates(locationListener);
	}

}
