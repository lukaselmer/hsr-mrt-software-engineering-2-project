package ch.hsr.se2p.mrt.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

public class TimeEntry implements JSONObjectable {
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
	private Blob audoRecord;

	private static SecureRandom random = new SecureRandom();

	TimeEntry() {
		// Needed for ormlite
	}

	public TimeEntry(Timestamp timeStart) {
		hashcode = new BigInteger(130, random).toString(32);
		this.timeStart = timeStart.getTime();
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
			j.put("audo_record", audoRecord);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
	}

	public Integer getId() {
		return id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public Integer getTimeEntryTypeId() {
		return timeEntryTypeId;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getTimeStart() {
		return new Timestamp(timeStart);
	}

	public Timestamp getTimeStop() {
		return new Timestamp(timeStop);
	}

	public Location getPosition() {
		return position;
	}

	public String getHashcode() {
		return hashcode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPosition(Location position) {
		this.position = position;
	}

	public void setTimeStop(Timestamp timeStop) {
		this.timeStop = timeStop.getTime();
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setTimeEntryTypeId(Integer timeEntryTypeId) {
		this.timeEntryTypeId = timeEntryTypeId;
	}

	public void setAudoRecord(Blob audoRecord) {
		this.audoRecord = audoRecord;
	}

	public void setRailsId(Integer railsId) {
		this.railsId = railsId;
	}

	public Integer getRailsId() {
		return railsId;
	}

	public boolean isTransmitted() {
		return transmitted;
	}

	// public TimeEntry(Cursor c) {
	// id = c.getInt(c.getColumnIndex(DbHelper.C_ID[0]));
	// customerId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0]));
	// timeEntryTypeId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0]));
	// hashcode = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_HASHCODE[0]));
	// description = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0]));
	// timeStart = Timestamp.valueOf(c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_START[0])));
	// timeStop = Timestamp.valueOf(c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_STOP[0])));
	// transmitted = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TRANSMITTED[0]));
	// initPosition(c);
	// initAudioRecord(c);
	// }
	//
	// private void initAudioRecord(Cursor c) {
	// // TODO: Implement this, DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0]
	// }

	// protected void initPosition(Cursor c) {
	// String s = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_POSITION[0]));
	// if (s.length() > 0) {
	// Parcel p = Parcel.obtain();
	// p.writeString(s);
	// Location.CREATOR.createFromParcel(p);
	// }
	// }
	// protected ContentValues generateContentValues() {
	// ContentValues contentValues = new ContentValues();
	// contentValues.put(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0], customerId == null ? "" : customerId.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0], timeEntryTypeId == null ? "" : timeEntryTypeId.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_HASHCODE[0], hashcode);
	// contentValues.put(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0], description);
	// contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_START[0], timeStart.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_STOP[0], timeStop.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_TRANSMITTED[0], transmitted.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0], audoRecord == null ? "" : audoRecord.toString());
	// contentValues.put(DbHelper.TIME_ENTRIES_C_POSITION[0], getPositionString());
	// return contentValues;
	// }
	// protected String getPositionString() {
	// String postitionString = "";
	// if (position != null) {
	// Parcel p = Parcel.obtain();
	// position.writeToParcel(p, 0);
	// postitionString = p.readString();
	// }
	// return postitionString;
	// }

}
