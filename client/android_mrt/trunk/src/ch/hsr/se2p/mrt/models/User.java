package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

import com.j256.ormlite.field.DatabaseField;

/**
 * The user or fieldWorker
 */
public class User implements Receivable {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int railsId;
	@DatabaseField
	private String firstName, lastName, email;
	@DatabaseField
	private long updatedAt;

	public User() {
		// Needed for ormlite
	}

	@Override
	public boolean fromJSON(JSONObject jsonObject) throws JSONException {
		JSONObject userObj = jsonObject.optJSONObject("field_worker");
		int id = userObj.getInt("id");
		if (id <= 0)
			return false;
		railsId = id;
		firstName = userObj.getString("first_name");
		lastName = userObj.getString("last_name");
		email = userObj.getString("email");
		updatedAt = ISO8601DateParser.parse(userObj.getString("updated_at")).getTime();
		return true;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	@Override
	public int getIdOnServer() {
		return getRailsId();
	}

	public String getLastName() {
		return lastName;
	}

	public int getRailsId() {
		return railsId;
	}

	@Override
	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}
}
