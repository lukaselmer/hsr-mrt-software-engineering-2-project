package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

/**
 * A GpsPosition contains the geographical coordinates (latitude and longitude). Additionally, time is the time when the position was fixed and the
 * createdAt is the time when the position was saved the first time.
 */
public class GpsPosition {
	private static final String TAG = TimeEntry.class.getSimpleName();
	
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private long time = 0;
	@DatabaseField
	private long createdAt = 0;
	@DatabaseField
	private double latitude = 0.0;
	@DatabaseField
	private double longitude = 0.0;

	public GpsPosition() {
		// Needed for ormlite
	}
	
	private GpsPosition(long time, double latitude, double longitude) {
		createdAt = System.currentTimeMillis();
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GpsPosition(Location location) {
		this.from(location);
	}
	
	public GpsPosition(double latitude, double longitude) {
		this(0L, latitude, longitude);
	}
	
	public void from(Location location) {
	    time = location.getTime();
	    latitude = location.getLatitude();
	    longitude = location.getLongitude();
	}
	
	public JSONObject toJSONObject() {
		JSONObject j = new JSONObject();
		
		try {
			//j.put("time", time);
			j.put("latitude", latitude);
			j.put("longitude", longitude);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
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

	public long getTime() {
		return time;
	}

	public Timestamp getCreatedAt() {
		return new Timestamp(createdAt);
	}
}
