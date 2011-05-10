package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

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
	
	//Caches gps_position for creation or update as it has to be stored in a separate table
	public GpsPosition position;

	public Customer() {
		// Needed for ormlite
	}

	public Customer(int i, String string, String string2) {
		id = i;
		firstName = string;
		lastName = string2;
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
		updatedAt = DateHelper.parse(customerObj.getString("updated_at")).getTime();
		deleted = !customerObj.isNull("deleted_at");
		
		JSONObject address_postition = customerObj.getJSONObject("address").getJSONObject("gps_position");
		position = new GpsPosition(address_postition.getDouble("latitude"), address_postition.getDouble("longitude"));
		
		return true;
	}
	
	
	
	public void setGpsPositionId(Integer id) {
		gpsPositionId = id;
	}
}
