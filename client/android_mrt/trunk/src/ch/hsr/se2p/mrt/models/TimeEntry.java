package ch.hsr.se2p.mrt.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

public class TimeEntry {
	private static String TABLE_NAME = DbHelper.TIME_ENTRIES_TABLE_NAME;

	private Integer id, customerId, timeEntryTypeId;
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
		String s;
		id = c.getInt(c.getColumnIndex(DbHelper.C_ID[0]));
		customerId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0]));
		timeEntryTypeId = c.getInt(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0]));
		hashcode = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_HASHCODE[0]));
		description = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0]));

		s = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_START[0]));
		if (s.length() > 0)
			timeStart = Timestamp.valueOf(s);

		s = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_TIME_STOP[0]));
		if (s.length() > 0)
			timeStop = Timestamp.valueOf(s);

		s = c.getString(c.getColumnIndex(DbHelper.TIME_ENTRIES_C_POSITION[0]));
		if (s.length() > 0) {
			Parcel p = Parcel.obtain();
			p.writeString(s);
			Location.CREATOR.createFromParcel(p);
		}

		// TODO: Audio Record
	}

	public static long create(DbHelper dbh, TimeEntry timeEntry) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0], timeEntry.customerId == null ? "" : timeEntry.customerId.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0], timeEntry.timeEntryTypeId == null ? "" : timeEntry.timeEntryTypeId.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_HASHCODE[0], timeEntry.hashcode);
		initialValues.put(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0], timeEntry.description);
		initialValues.put(DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0], timeEntry.audoRecord == null ? "" : timeEntry.audoRecord.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_START[0], timeEntry.timeStart == null ? "" : timeEntry.timeStart.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_STOP[0], timeEntry.timeStop == null ? "" : timeEntry.timeStop.toString());

		if (timeEntry.position != null) {
			Parcel p = Parcel.obtain();
			timeEntry.position.writeToParcel(p, 0);
			initialValues.put(DbHelper.TIME_ENTRIES_C_POSITION[0], p.readString());
		} else {
			initialValues.put(DbHelper.TIME_ENTRIES_C_POSITION[0], "");
		}

		return dbh.insert(TABLE_NAME, initialValues);
	}

	public static void delete(DbHelper dbh, long id) {
		dbh.delete(TABLE_NAME, id);
	}

	public static List<TimeEntry> all(DbHelper dbh) {
		ArrayList<TimeEntry> l = new ArrayList<TimeEntry>();
		Cursor c = dbh.all(TABLE_NAME);
		while (c.moveToNext()) {
			l.add(new TimeEntry(c));
		}
		c.getCount();
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

}
