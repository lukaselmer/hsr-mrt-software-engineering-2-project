package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.models.GpsPosition;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

class LocationService {
	private final LocationManager locationManager;
	private final LocationListener locationListener;
	private final LocationListener activityLocationListener;
	protected Location currentLocation;
	private GpsPosition currentGPSPosition;

	protected LocationService(LocationManager locationManager, LocationListener activityLocationListener) {
		this.locationManager = locationManager;
		this.activityLocationListener = activityLocationListener;
		locationListener = getLocationListener();
		// Update location maximal every 10 seconds
		locationManager.requestLocationUpdates(getLocationProvider(), 10 * 1000, 0, this.activityLocationListener);
	}

	protected Location getCurrentLocation() {
		return currentLocation;
	}

	protected GpsPosition getCurrentGPSPosition() {
		return currentGPSPosition;
	}

	private LocationListener getLocationListener() {
		return new LocationListenerAdapter() {
			@Override
			public void onLocationChanged(Location location) {
				currentLocation = location;
				currentGPSPosition = new GpsPosition(currentLocation);
				activityLocationListener.onLocationChanged(location);
			}
		};
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
