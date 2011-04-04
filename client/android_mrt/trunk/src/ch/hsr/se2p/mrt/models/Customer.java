package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import ch.hsr.se2p.mrt.models.Customer.Values;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;

public class Customer implements BaseColumns {
	private static String TABLE_NAME = DatabaseOpenHelper.TABLE_CUSTOMERS;

	public class Values {
		private String firstName, lastName, phone;
		private Location position;
		private Timestamp updatedAt;
	}

	private Values values;

	public static long createCustomer(SQLiteDatabase db, Values values) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("first_name", values.firstName);
		initialValues.put("last_name", values.lastName);
		initialValues.put("phone", values.phone);
		initialValues.put("position", values.position.toString());
		initialValues.put("updated_at", values.updatedAt.toString());
		return db.insert(TABLE_NAME, null, initialValues);
	}

	public static void deleteCustomer(SQLiteDatabase db, long rowId) {
		db.delete(TABLE_NAME, "id=" + rowId, null);
	}
}
