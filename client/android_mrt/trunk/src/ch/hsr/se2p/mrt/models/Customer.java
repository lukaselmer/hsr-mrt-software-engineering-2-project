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
	private boolean changed = false;

	public Customer() {
		// Needed for ormlite
	}

	protected Customer(String firstname, String lastname, String phone, Double distance) {
		firstName = firstname;
		lastName = lastname;
		this.phone = phone;
		this.distance = distance;
	}

	/**
	 * Findbugs: this class uses the Object's equals method. This is considered bad style because (x.compareTo(y)==0) == (x.equals(y)) is not
	 * guaranteed. If this class was in a library this problem would have to be fixed.
	 */
	@Override
	public int compareTo(Customer another) {
		if (equals(another))
			return 0;
		return compareToNotEquals(another);
	}

	private int compareToNotEquals(Customer another) {
		if (bothDistancesNull(another))
			return compareLastName(another);
		if (getDistance() == null || another.getDistance() == null)
			return compareToNullDistance();
		return compareToNonNullDistance(another);
	}

	private boolean bothDistancesNull(Customer another) {
		return getDistance() == null && another.getDistance() == null;
	}

	private int compareToNullDistance() {
		return getDistance() == null ? 1 : -1;
	}

	private int compareToNonNullDistance(Customer another) {
		int cmp = getRoundedDistance().compareTo(another.getRoundedDistance());
		return cmp == 0 ? compareLastName(another) : cmp;
	}

	private int compareLastName(Customer another) {
		return getLastName().compareTo(another.getLastName());
	}

	@Override
	public boolean fromJSON(JSONObject customerObj) throws JSONException {
		setChanged();
		int railsId = customerObj.getInt("id");
		if (railsId <= 0)
			return false;
		this.railsId = railsId;
		setNormalAttributes(customerObj);
		setAddressAndGpsPosition(customerObj);
		return true;
	}

	public Double getDistance() {
		return distance;
	}

	private Long getRoundedDistance() {
		return Math.round(getDistance());
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
		setChanged(true);
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
		return getFirstName() + " " + getLastName() + (getDistance() == null ? "" : getDistanceString());
	}

	private String getDistanceString() {
		if (getDistance() <= 500) {
			DecimalFormat f = new DecimalFormat(" (0 m)");
			return f.format(getDistance());
		} else {
			DecimalFormat f = new DecimalFormat(" (0.0 km)");
			return f.format(getDistance() / 1000);
		}
	}

}
