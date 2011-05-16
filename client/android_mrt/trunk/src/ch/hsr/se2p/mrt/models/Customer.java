package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Saves needed information about the customer, which was received from the server.
 */
public class Customer implements Receivable, Comparable<Customer> {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int railsId = 0;
	@DatabaseField
	private Integer gpsPositionId;
	@DatabaseField
	private String firstName, lastName, phone;
	@DatabaseField
	private long updatedAt = 0;
	@DatabaseField
	private boolean deleted = false;
	private Double distance;
	// Caches gps_position for creation or update as it has to be stored in a separate table
	private GpsPosition gpsPosition;
	private boolean changed = true;

	public Customer() {
		// Needed for ormlite
	}

	protected Customer(String firstname, String lastname, String phone, Double distance) {
		firstName = firstname;
		lastName = lastname;
		this.phone = phone;
		this.distance = distance;
	}

	@Override
	public int compareTo(Customer another) {
		if (this.getDistance() == null || another.getDistance() == null) {
			if (this.getDistance() == null && another.getDistance() == null)
				return this.getLastName().compareTo(another.getLastName());
			else
				return this.getDistance() == null ? 1 : -1;
		} else {
			int cmp = this.getDistance().compareTo(another.getDistance());
			return cmp == 0 ? this.getLastName().compareTo(another.getLastName()) : cmp;
		}
	}

	@Override
	public boolean fromJSON(JSONObject customerObj) throws JSONException {
		int railsId = customerObj.getInt("id");
		if (railsId <= 0)
			return false;
		this.railsId = railsId;
		setNormalAttributes(customerObj);
		setAddressAndGpsPosition(customerObj);
		setChanged(true);
		return true;
	}

	public Double getDistance() {
		return distance;
	}

	public String getFirstName() {
		return firstName;
	}

	public GpsPosition getGpsPosition() {
		return gpsPosition;
	}

	public Integer getGpsPositionId() {
		return gpsPositionId;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int getIdOnServer() {
		return railsId;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}

	public boolean hasChanged() {
		return changed;
	}

	public boolean hasGpsPosition() {
		return gpsPositionId != null && gpsPositionId != 0;
	}

	public boolean isDeleted() {
		return deleted;
	}

	private void setAddressAndGpsPosition(JSONObject customerObj) throws JSONException {
		if (customerObj.has("address")) {
			JSONObject address_postition = customerObj.getJSONObject("address").getJSONObject("gps_position");
			setGpsPosition(new GpsPosition(address_postition.getDouble("latitude"), address_postition.getDouble("longitude")));
		}
	}

	public void setChanged() {
		this.changed = true;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public void setGpsPosition(GpsPosition gpsPosition) {
		this.gpsPosition = gpsPosition;
	}

	public void setGpsPositionId(Integer id) {
		gpsPositionId = id;
		if (!hasGpsPosition())
			setDistance(null);
	}

	private void setNormalAttributes(JSONObject customerObj) throws JSONException {
		firstName = customerObj.getString("first_name");
		lastName = customerObj.getString("last_name");
		phone = customerObj.getString("phone");
		updatedAt = ISO8601DateParser.parse(customerObj.getString("updated_at")).getTime();
		deleted = !customerObj.isNull("deleted_at");
	}

	@Override
	public String toString() {
		String s = getFirstName() + " " + getLastName();
		if (getDistance() != null) {
			if (getDistance() <= 500) {
				DecimalFormat f = new DecimalFormat(" (0 m)");
				s += f.format(getDistance());
			} else {
				DecimalFormat f = new DecimalFormat(" (0.0 km)");
				s += f.format(getDistance() / 1000);
			}
		}
		return s;
	}

}
