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
public class Customer implements Receivable {
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
	private boolean changed;

	public Customer() {
		// Needed for ormlite
	}

	protected Customer(String firstname, String lastname, String phone, Double distance) {
		firstName = firstname;
		lastName = lastname;
		this.phone = phone;
		this.distance = distance;
	}

	public boolean hasGpsPosition() {
		return gpsPositionId != null && gpsPositionId != 0;
	}

	public Integer getId() {
		return id;
	}

	@Override
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

	public Integer getGpsPositionId() {
		return gpsPositionId;
	}

	@Override
	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
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

	private void setNormalAttributes(JSONObject customerObj) throws JSONException {
		firstName = customerObj.getString("first_name");
		lastName = customerObj.getString("last_name");
		phone = customerObj.getString("phone");
		updatedAt = ISO8601DateParser.parse(customerObj.getString("updated_at")).getTime();
		deleted = !customerObj.isNull("deleted_at");
	}

	private void setAddressAndGpsPosition(JSONObject customerObj) throws JSONException {
		if (customerObj.has("address")) {
			JSONObject address_postition = customerObj.getJSONObject("address").getJSONObject("gps_position");
			setGpsPosition(new GpsPosition(address_postition.getDouble("latitude"), address_postition.getDouble("longitude")));
		}
	}

	public void setGpsPositionId(Integer id) {
		gpsPositionId = id;
		if (!hasGpsPosition())
			setDistance(null);
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
				s += f.format(getDistance()/1000);
			}
		}
		return s;
	}

	public void setChanged() {
		this.changed = true;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void setGpsPosition(GpsPosition gpsPosition) {
		this.gpsPosition = gpsPosition;
	}

	public GpsPosition getGpsPosition() {
		return gpsPosition;
	}
}
