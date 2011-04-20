package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import ch.hsr.se2p.mrt.interfaces.Receivable;

import com.j256.ormlite.field.DatabaseField;

public class Customer implements Receivable {
	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Integer railsId;
	@DatabaseField
	private String firstName, lastName, phone;
	//TODO: @DatabaseField
	private Location position;
	@DatabaseField
	private Long updatedAt;
	@DatabaseField
	private boolean deleted;

	public Customer() {
		// Needed for ormlite
	}

	// TODO: remove this!!
	public Customer(Integer id, String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public int getIdOnServer() {
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

	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String toString() {
		return lastName + " " + firstName;
	}

	@Override
	public boolean fromJSON(JSONObject customerObj) throws JSONException {
		int railsId = customerObj.getInt("id");
		if (railsId <= 0)
			return false;
		this.railsId = railsId;
		firstName = customerObj.getString("first_name");
		lastName = customerObj.getString("last_name");
		phone = customerObj.getString("phone");
		position = parsePosition(customerObj);
		updatedAt = Timestamp.valueOf(customerObj.getString("updated_at")).getTime();
		deleted = customerObj.has("deleted_at");
		return true;
	}

	private Location parsePosition(JSONObject customerObj) throws JSONException {
		// customerObj.getLong("position");
		// TODO: Parse position
		return null;
	}
}
