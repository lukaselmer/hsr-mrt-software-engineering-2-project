package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Formatter;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.location.Location;
import android.util.Log;
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
	
	//Caches gps_position for creation or update as it has to be stored in a separate table
	public GpsPosition position;

	public Customer() {
		// Needed for ormlite
	}
	
	public Customer(String firstname, String lastname, String phone, Double distance) {
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
		
		firstName = customerObj.getString("first_name");
		lastName = customerObj.getString("last_name");
		phone = customerObj.getString("phone");
		updatedAt = DateHelper.parse(customerObj.getString("updated_at")).getTime();
		deleted = !customerObj.isNull("deleted_at");
		
		if (customerObj.has("address")) {
			JSONObject address_postition = customerObj.getJSONObject("address").getJSONObject("gps_position");
			position = new GpsPosition(address_postition.getDouble("latitude"), address_postition.getDouble("longitude"));
		}
		
		return true;
	}
	
	public void setGpsPositionId(Integer id) {
		gpsPositionId = id;
	}

	@Override
	public int compareTo(Customer another) {
		if (this.distance == null) {
			if (another.distance == null) 
				return this.lastName.compareTo(another.lastName);
			return 1;
		}
		if (another.distance == null) {
			return -1;
		}
		if (this.distance > another.distance)
			return 1;
		if (this.distance.equals(another.distance)) 
			return this.lastName.compareTo(another.lastName);
		return -1;
	}
	
	public String toString() {
		String s = firstName + " " + lastName;
		
		if (distance != null) {
			DecimalFormat f = new DecimalFormat(" (0m)");
			s += f.format(distance); 
		}
		
		return s;
	}
}
