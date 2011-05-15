package ch.hsr.se2p.mrt.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

/**
 * A GpsPosition contains the geographical coordinates (latitude and longitude). Additionally, time is the time when the gpsPosition was fixed and the
 * createdAt is the time when the gpsPosition was saved the first time.
 */
public class GpsPosition {
	private static final String TAG = TimeEntry.class.getSimpleName();

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private double latitude = 0.0;

	@DatabaseField
	private double longitude = 0.0;

	public GpsPosition() {
		// Needed for ormlite
	}

	public GpsPosition(double latitude, double longitude) {
		this(0L, latitude, longitude);
	}

	public GpsPosition(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	private GpsPosition(long time, double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Calculates the distance in meters from one to another GPS Position.
	 */
	public double distanceTo(GpsPosition otherPos) {
		return distance(latitude, longitude, otherPos.getLatitude(), otherPos.getLongitude());
	}

	private float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
		float[] results = { Float.MAX_VALUE };
		Location.distanceBetween(lat_a, lng_a, lat_b, lng_b, results);
		return results[0];
	}

	public Integer getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public JSONObject toJSONObject() {
		JSONObject j = new JSONObject();
		try {
			// j.put("time", time);
			j.put("latitude", latitude);
			j.put("longitude", longitude);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
	}
}