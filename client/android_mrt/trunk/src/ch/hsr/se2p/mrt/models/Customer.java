package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import android.location.Location;

import com.j256.ormlite.field.DatabaseField;

public class Customer {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Integer railsId;
	@DatabaseField
	private String firstName, lastName, phone;
	@DatabaseField
	private Location position;
	@DatabaseField
	private long updatedAt;

	public Customer(Integer railsId, String firstName, String lastName, String phone, Location position, Timestamp updatedAt) {
		super();
		this.railsId = railsId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.position = position;
		this.updatedAt = updatedAt.getTime();
	}

	public Integer getId() {
		return id;
	}

	public Integer getRailsId() {
		return railsId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public Location getPosition() {
		return position;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public String toString() {
		return lastName + " " + firstName;
	}
}
