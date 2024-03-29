package ch.hsr.se2p.mrt.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;
import ch.hsr.se2p.mrt.interfaces.Confirmable;
import ch.hsr.se2p.mrt.interfaces.Transmittable;

import com.j256.ormlite.field.DatabaseField;

public class TimeEntry implements Transmittable, Confirmable {
	private static final String TAG = TimeEntry.class.getSimpleName();

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Integer railsId, customerId, timeEntryTypeId;
	@DatabaseField
	private boolean transmitted;
	@DatabaseField
	private String hashcode, description;
	@DatabaseField
	private long timeStart, timeStop;
	// TODO: Add location
	// @DatabaseField
	private Location position;
	// TODO: Add audio record
	// @DatabaseField
	private Blob audioRecord;

	private static SecureRandom random = new SecureRandom();

	TimeEntry() {
		// Needed for ormlite
	}

	public TimeEntry(Timestamp timeStart) {
		hashcode = new BigInteger(130, random).toString(32);
		this.timeStart = timeStart.getTime();
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getDescription() {
		return description;
	}

	public String getHashcode() {
		return hashcode;
	}

	public Integer getId() {
		return id;
	}

	public Location getPosition() {
		return position;
	}

	public int getIdOnServer() {
		return railsId;
	}

	public Integer getTimeEntryTypeId() {
		return timeEntryTypeId;
	}

	public Timestamp getTimeStart() {
		return new Timestamp(timeStart);
	}

	public Timestamp getTimeStop() {
		return new Timestamp(timeStop);
	}

	public boolean isTransmitted() {
		return transmitted;
	}

	public void setAudioRecord(Blob audioRecord) {
		this.audioRecord = audioRecord;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPosition(Location position) {
		this.position = position;
	}

	public void setRailsId(Integer railsId) {
		this.railsId = railsId;
	}

	public void setTimeEntryTypeId(Integer timeEntryTypeId) {
		this.timeEntryTypeId = timeEntryTypeId;
	}

	public void setTimeStop(Timestamp timeStop) {
		this.timeStop = timeStop.getTime();
	}

	public void setTransmitted() {
		transmitted = true;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject j = new JSONObject();
		try {
			j.put("customer_id", customerId);
			j.put("time_entry_type_id", timeEntryTypeId);
			j.put("hashcode", hashcode);
			j.put("description", description);
			j.put("time_start", getTimeStart());
			j.put("time_stop", getTimeStop());
			j.put("position", position);
			j.put("audio_record", audioRecord);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
	}

	@Override
	public boolean processTransmission(JSONObject jsonObject) throws JSONException {
		int id = jsonObject.optJSONObject("time_entry").getInt("id");
		String hashcode = jsonObject.optJSONObject("time_entry").getString("hashcode");

		if (getHashcode().equals(hashcode) && id != 0) {
			// Everything is fine, it worked! Now lets get rid of the hashcode!
			setRailsId(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean processConfirmation(JSONObject jsonObject) throws JSONException {
		return getIdOnServer() == jsonObject.optJSONObject("time_entry").getInt("id");
	}
}
