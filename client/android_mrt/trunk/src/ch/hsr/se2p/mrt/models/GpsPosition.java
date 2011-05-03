package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import android.location.Location;

import com.j256.ormlite.field.DatabaseField;

public class GpsPosition {
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

	GpsPosition() {
		// Needed for ormlite
	}

	private GpsPosition(long time, double latitude, double longitude) {
		createdAt = System.currentTimeMillis();
		this.time = time;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GpsPosition(Location location) {
		this(location.getTime(), location.getLatitude(), location.getLongitude());
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
