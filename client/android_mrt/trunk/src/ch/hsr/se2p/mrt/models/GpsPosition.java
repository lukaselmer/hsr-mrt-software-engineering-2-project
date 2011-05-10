package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.FloatMath;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

/**
 * A GpsPosition contains the geographical coordinates (latitude and longitude). Additionally, time is the time when the position was fixed and the
 * createdAt is the time when the position was saved the first time.
 */
public class GpsPosition {
	private static final String TAG = TimeEntry.class.getSimpleName();
	private static final int EAST_COORDINATE = 0;
	private static final int NORTH_COORDINATE = 1;
	
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
	
	private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
	    double pk = (180/3.14169);

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 6366000*tt;
	}
	
	/**
	 * Calculates the distance in meters from one to another GPS Position.
	 */
	public double distanceTo(GpsPosition otherPos) {
		return gps2m(latitude, longitude, otherPos.getLatitude(), otherPos.getLongitude());
		
//		List<Double> oneList = GpsPositionConversion.calculateWGSToLV03(latitude, longitude, 0);
//		List<Double> otherList = GpsPositionConversion.calculateWGSToLV03(otherPos.getLatitude(), otherPos.getLongitude(), 0);
//		
//		double eastDistance = Math.abs(oneList.get(EAST_COORDINATE) - otherList.get(EAST_COORDINATE));
//		double northDistance = Math.abs(oneList.get(NORTH_COORDINATE) - otherList.get(NORTH_COORDINATE));
//		
//		return Math.hypot(eastDistance, northDistance);	
	}
}