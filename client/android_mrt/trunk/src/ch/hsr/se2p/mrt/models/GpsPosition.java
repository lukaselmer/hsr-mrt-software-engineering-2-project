package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import com.j256.ormlite.field.DatabaseField;

public class GpsPosition {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private long createdAt = 0;
	@DatabaseField
	private double latitude = 0.0;
	@DatabaseField
	private double longitude = 0.0;

	GpsPosition() {
		// Needed for ormlite
	}

	public GpsPosition(double latitude, double longitude) {
		this.createdAt = System.currentTimeMillis();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GpsPosition(long createdAt, double latitude, double longitude) {
		this.createdAt = createdAt;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public Timestamp getCreatedAt() {
		return new Timestamp(createdAt);
	}
}
