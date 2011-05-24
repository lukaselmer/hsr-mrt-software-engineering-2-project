package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Saves needed information about the timeentrytype, which was received from the server.
 */
public class TimeEntryType implements Receivable, Comparable<TimeEntryType> {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int railsId = 0;
	@DatabaseField
	private String name;
	@DatabaseField
	private long createdAt = 0;
	@DatabaseField
	private long validUntil = 0;
	@DatabaseField
	private long updatedAt = 0;

	private boolean deleted = false;

	public TimeEntryType() {
		// Needed for ormlite
	}

	public TimeEntryType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public boolean fromJSON(JSONObject timeEntryTypeObj) throws JSONException {
		int railsId = timeEntryTypeObj.getInt("id");
		if (railsId <= 0)
			return false;
		this.railsId = railsId;
		parseMembersFromJson(timeEntryTypeObj);
		if (deleted)
			validUntil = ISO8601DateParser.parse(timeEntryTypeObj.getString("valid_until")).getTime();
		return true;
	}

	private void parseMembersFromJson(JSONObject timeEntryTypeObj) throws JSONException {
		name = timeEntryTypeObj.getString("description");
		createdAt = ISO8601DateParser.parse(timeEntryTypeObj.getString("created_at")).getTime();
		updatedAt = ISO8601DateParser.parse(timeEntryTypeObj.getString("updated_at")).getTime();
		deleted = !timeEntryTypeObj.isNull("valid_until");
	}

	public Timestamp getCreatedAt() {
		return new Timestamp(createdAt);
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int getIdOnServer() {
		return railsId;
	}

	public String getName() {
		return name;
	}

	@Override
	public Timestamp getUpdatedAt() {
		return new Timestamp(updatedAt);
	}

	public Timestamp getValidUntil() {
		return new Timestamp(validUntil);
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(TimeEntryType anotherTimeEntryType) {
		return getName().compareTo(anotherTimeEntryType.getName());	
	}
}
