package ch.hsr.se2p.mrt.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import ch.hsr.se2p.mrt.network.Transmitter;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.os.Parcel;
import android.util.Log;

public class TimeEntry implements JSONObjectable {
	private static final String TAG = TimeEntry.class.getSimpleName();

	private static String TABLE_NAME = DbHelper.TIME_ENTRIES_TABLE_NAME;

	private Integer id, railsId, customerId, timeEntryTypeId, transmitted;
	private String hashcode, description;
	private Timestamp timeStart, timeStop;
	private Location position;
	private Blob audoRecord;

	private static SecureRandom random = new SecureRandom();

	public TimeEntry(Timestamp timeStart) {
		hashcode = new BigInteger(130, random).toString(32);
		this.timeStart = timeStart;
	}

	public TimeEntry(Cursor c) {
		id = c.getInt(c.getColumnIndex(DbHelper.C_ID[0]));
		customerId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0]));
		timeEntryTypeId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0]));
		hashcode = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_HASHCODE[0]));
		description = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0]));
		timeStart = Timestamp.valueOf(c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_START[0])));
		timeStop = Timestamp.valueOf(c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_STOP[0])));
		transmitted = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TRANSMITTED[0]));
		initPosition(c);
		initAudioRecord(c);
	}

	private void initAudioRecord(Cursor c) {
		// TODO: Implement this, DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0]
	}

	protected void initPosition(Cursor c) {
		String s = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_POSITION[0]));
		if (s.length() > 0) {
			Parcel p = Parcel.obtain();
			p.writeString(s);
			Location.CREATOR.createFromParcel(p);
		}
	}

	public long create(DbHelper dbh) {
		if (timeStart == null || timeStop == null) {
			return -1;
		}
		return dbh.insert(TABLE_NAME, generateContentValues());
	}

	public void setTransmitted(DbHelper dbh) {
		transmitted = new Integer(1);
		dbh.update(TABLE_NAME, generateContentValues(), id);
	}

	protected ContentValues generateContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0], customerId == null ? "" : customerId.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0], timeEntryTypeId == null ? "" : timeEntryTypeId.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_HASHCODE[0], hashcode);
		contentValues.put(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0], description);
		contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_START[0], timeStart.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_TIME_STOP[0], timeStop.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_TRANSMITTED[0], transmitted.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0], audoRecord == null ? "" : audoRecord.toString());
		contentValues.put(DbHelper.TIME_ENTRIES_C_POSITION[0], getPositionString());
		return contentValues;
	}

	protected String getPositionString() {
		String postitionString = "";
		if (position != null) {
			Parcel p = Parcel.obtain();
			position.writeToParcel(p, 0);
			postitionString = p.readString();
		}
		return postitionString;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject j = new JSONObject();
		try {
			j.put("customer_id", customerId);
			j.put("time_entry_type_id", timeEntryTypeId);
			j.put("hashcode", hashcode);
			j.put("description", description);
			j.put("time_start", timeStart);
			j.put("time_stop", timeStop);
			j.put("position", position);
			j.put("audo_record", audoRecord);
		} catch (JSONException e) {
			Log.e(TAG, "Error creating JSON Object", e);
		}
		return j;
	}

	public void delete(DbHelper dbh) {
		dbh.delete(TABLE_NAME, id);
	}

	public static List<TimeEntry> all(DbHelper dbh) {
		ArrayList<TimeEntry> l = new ArrayList<TimeEntry>();
		Cursor c = dbh.all(TABLE_NAME);
		while (c.moveToNext()) {
			l.add(new TimeEntry(c));
		}
		c.close();
		return l;
	}

	public static Cursor getById(DbHelper dbh, long id) {
		return dbh.findById(TABLE_NAME, id);
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
		return timeStart;
	}

	public Timestamp getTimeStop() {
		return timeStop;
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
		this.timeStop = timeStop;
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
		return transmitted == 1;
	}
}
