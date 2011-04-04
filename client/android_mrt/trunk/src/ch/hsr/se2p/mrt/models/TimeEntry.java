package ch.hsr.se2p.mrt.models;

import java.sql.Blob;
import java.sql.Timestamp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;

public class TimeEntry {
	private static String TABLE_NAME = DbHelper.TABLE_TIME_ENTRIES;

	public static class Values {
		public String hashcode, description;
		public Location position;
		public Timestamp timeStart, timeStop;
		public Integer customerId, timeEntryTypeId;
		public Blob audoRecord;
	}

	private Values values = new Values();

	public static long create(SQLiteDatabase db, Values values) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("hashcode", values.hashcode);
		initialValues.put("description", values.description);
		initialValues.put("position", values.position == null ? "" : values.position.toString());
		initialValues.put("time_start", values.timeStart == null ? "" : values.timeStart.toString());
		initialValues.put("time_stop", values.timeStop == null ? "" : values.timeStop.toString());
		initialValues.put("customer_id", values.customerId == null ? "" : values.customerId.toString());
		initialValues.put("time_entry_type_id", values.timeEntryTypeId == null ? "" : values.timeEntryTypeId.toString());
		return db.insert(TABLE_NAME, null, initialValues);
	}

	public static void delete(SQLiteDatabase db, long id) {
		db.delete(TABLE_NAME, BaseColumns._ID + "=" + id, null);
	}

	public static Cursor getAll(SQLiteDatabase db) {
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public static Cursor getById(SQLiteDatabase db, long id) {
		return db.query(TABLE_NAME, null, BaseColumns._ID + "=" + id, null, null, null, null);
	}

}
