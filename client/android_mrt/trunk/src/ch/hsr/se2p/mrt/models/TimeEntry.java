package ch.hsr.se2p.mrt.models;

import java.sql.Blob;
import java.sql.Timestamp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;

public class TimeEntry {
	private static String TABLE_NAME = DbHelper.TIME_ENTRIES_TABLE_NAME;

	public static class Values {
		public String hashcode, description;
		public Location position;
		public Timestamp timeStart, timeStop;
		public Integer customerId, timeEntryTypeId;
		public Blob audoRecord;
	}

	private Values values = new Values();

	public TimeEntry(Values values) {
		this.values = values;
	}
	public TimeEntry(Cursor c) {
		this.values = parseCursor(c);
	}
	
	public static long create(DbHelper dbh, Values values) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DbHelper.TIME_ENTRIES_C_HASHCODE[0], values.hashcode);
		initialValues.put(DbHelper.TIME_ENTRIES_C_DESCRIPTION[0], values.description);
		initialValues.put(DbHelper.TIME_ENTRIES_C_POSITION[0], values.position == null ? "" : values.position.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_AUDIO_RECORD[0], values.audoRecord == null ? "" : values.audoRecord.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_START[0], values.timeStart == null ? "" : values.timeStart.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_STOP[0], values.timeStop == null ? "" : values.timeStop.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_CUSTOMER_ID[0], values.customerId == null ? "" : values.customerId.toString());
		initialValues.put(DbHelper.TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID[0], values.timeEntryTypeId == null ? "" : values.timeEntryTypeId.toString());
		return dbh.insert(TABLE_NAME, initialValues);
	}

	public static void delete(DbHelper dbh, long id) {
		dbh.delete(TABLE_NAME, id);
	}

	public static Cursor all(DbHelper dbh) {
		return dbh.all(TABLE_NAME);
	}

	public static Cursor getById(DbHelper dbh, long id) {
		return dbh.findById(TABLE_NAME, id);
	}
	
	private Values parseCursor(Cursor c){
		Values v = new Values();
		return v;
	}

}
