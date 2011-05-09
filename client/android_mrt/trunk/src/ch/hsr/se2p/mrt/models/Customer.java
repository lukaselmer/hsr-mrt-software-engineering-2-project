package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import ch.hsr.se2p.mrt.interfaces.Receivable;

import com.j256.ormlite.field.DatabaseField;

public class Customer implements Receivable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int railsId = 0;
	@DatabaseField
	private String firstName, lastName, phone;
	// TODO: @DatabaseField
	private GpsPosition position;
	@DatabaseField
	private long updatedAt = 0;
	@DatabaseField
	private boolean deleted = false;

	public Customer() {
		// Needed for ormlite
	}

	public Customer(int i, String string, String string2) {
		id = i;
		firstName = string;
		lastName = string2;
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

	public GpsPosition getPosition() {
		return position;
	}

	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String toString() {
		return lastName + " " + firstName + " (" + railsId +")";
	}

	@Override
	public boolean fromJSON(JSONObject customerObj) throws JSONException {
		int railsId = customerObj.getInt("id");
		if (railsId <= 0)
			return false;
		this.railsId = railsId;
		Log.e("Helööööööööööw", customerObj.toString());
		Log.e("Railsid", railsId + "");
		firstName = customerObj.getString("first_name");
		lastName = customerObj.getString("last_name");
		phone = customerObj.getString("phone");
		position = parsePosition(customerObj);
		updatedAt = DateHelper.parse(customerObj.getString("updated_at")).getTime();
		deleted = !customerObj.isNull("deleted_at");
		Log.e("Railsid", railsId + "");
		return true;
	}

	private GpsPosition parsePosition(JSONObject customerObj) throws JSONException {
		// customerObj.getLong("position");
		// TODO: Parse position
		return null;
	}
}
