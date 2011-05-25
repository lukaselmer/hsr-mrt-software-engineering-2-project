package ch.hsr.se2p.mrt.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Time;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ch.hsr.se2p.mrt.interfaces.Confirmable;
import ch.hsr.se2p.mrt.interfaces.Transmittable;

import com.j256.ormlite.field.DatabaseField;

/**
 * TimeEntry stores information about a given customer, timeentry type and the gps gpsPosition. Furthermore it saves the time when the TimeEntry was
 * first created (started) and a stop time can be set too.
 * 
 */
public class TimeEntry implements Transmittable, Confirmable {
	private static final String TAG = TimeEntry.class.getSimpleName();

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField
	private Integer railsId, customerId, timeEntryTypeId, gpsPositionId;
	@DatabaseField
	private boolean transmitted;
	@DatabaseField
	private String hashcode, description;
	@DatabaseField
	private long timeStart, timeStop;

	private Customer customer;
	private GpsPosition gpsPosition;
	private TimeEntryType timeEntryType;

	private static SecureRandom random = new SecureRandom();

	TimeEntry() {
		// Needed for ormlite
	}

	/**
	 * Creates a new TimeEntry. A hashcode is created to make sure that an TimeEntry is not saved on the server more then once.
	 */
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

	public Integer getGpsPositionId() {
		return gpsPositionId;
	}

	public String getHashcode() {
		return hashcode;
	}

	public Integer getId() {
		return id;
	}

	@Override
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

	public boolean hasCustomer() {
		return customerId != null && customerId != 0;
	}

	public boolean hasGpsPosition() {
		return gpsPositionId != null && gpsPositionId != 0;
	}

	public boolean hasTimeEntryType() {
		return timeEntryTypeId != null && timeEntryTypeId != 0;
	}

	@Override
	public boolean isTransmitted() {
		return transmitted;
	}

	@Override
	public boolean processConfirmation(JSONObject jsonObject) throws JSONException {
		return getIdOnServer() == jsonObject.optJSONObject("time_entry").getInt("id");
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

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGpsPosition(GpsPosition gpsPosition) {
		this.gpsPosition = gpsPosition;
	}

	public void setGpsPositionId(Integer gpsPositionId) {
		this.gpsPositionId = gpsPositionId;
	}

	public void setRailsId(Integer railsId) {
		this.railsId = railsId;
	}

	public void setTimeEntryType(TimeEntryType timeEntryType) {
		this.timeEntryType = timeEntryType;
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
			putMembersToJson(j);
			putRelationsToJson(j);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
	}

	private void putMembersToJson(JSONObject j) throws JSONException {
		j.put("customer_id", customerId);
		j.put("time_entry_type_id", timeEntryTypeId);
		j.put("hashcode", hashcode);
		j.put("description", description);
		j.put("time_start", ISO8601DateParser.format(getTimeStart()));
		j.put("time_stop", ISO8601DateParser.format(getTimeStop()));
	}

	private void putRelationsToJson(JSONObject j) throws JSONException {
		if (customer != null)
			j.put("customer_id", customer.getIdOnServer());
		if (timeEntryType != null)
			j.put("time_entry_type_id", timeEntryType.getIdOnServer());
		if (gpsPosition != null)
			j.put("gps_position_data", gpsPosition.toJSONObject());
	}
}
