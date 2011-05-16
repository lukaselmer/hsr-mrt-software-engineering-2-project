package ch.hsr.se2p.mrt.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * A Null-Object. An adapter to simplify location listeners that do not need all events defined on the LocationListener interface.
 */
class LocationListenerAdapter implements LocationListener {
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
}
