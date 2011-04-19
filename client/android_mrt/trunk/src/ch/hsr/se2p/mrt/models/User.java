package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;

import ch.hsr.se2p.mrt.interfaces.Receivable;

public class User implements Receivable {

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Integer railsId;
	@DatabaseField
	private String firstName, lastName, email;
	@DatabaseField
	private long updatedAt;

	public User() {
		// Needed for ormlite
	}

	@Override
	public boolean fromJSON(JSONObject jsonObject) throws JSONException {
		JSONObject userObj = jsonObject.optJSONObject("user");
		int id = userObj.getInt("id");
		if (id <= 0)
			return false;
		railsId = id;
		firstName = userObj.getString("first_name");
		lastName = userObj.getString("last_name");
		email = userObj.getString("email");
		updatedAt = userObj.getLong("updated_at");
		return true;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Integer getRailsId() {
		return railsId;
	}

	@Override
	public int getIdOnServer() {
		return getRailsId();
	}

	@Override
	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}
}
