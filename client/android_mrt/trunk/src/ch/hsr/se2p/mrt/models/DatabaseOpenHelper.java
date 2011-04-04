package ch.hsr.se2p.mrt.models;

import ch.hsr.se2p.mrt.util.Config;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME_CACHED_LOGINS = "cached_logins";
	public static final String CACHED_LOGINS_USERNAME = "username";
	public static final String CACHED_LOGINS_PASSWORD = "password";

	public static final String TABLE_ADDRESSES = "addresses";
	public static final String TABLE_TIME_ENTRIES = "time_entries";
	public static final String TABLE_TIME_ENTRY_TYPES = "time_entry_types";
	public static final String TABLE_CUSTOMERS = "customers";

	private static final String ID_AND_SERVER_ID = "id INTEGER PRIMARY KEY ASC, server_id INTEGER, ";
	public static final String CREATE_ADRESSES = "CREATE TABLE " + TABLE_ADDRESSES + " (" + ID_AND_SERVER_ID + "line1 TEXT, line2 TEXT, line2 TEXT, zip INTEGER, place TEXT);";
	public static final String CREATE_TIME_ENTRIES = "CREATE TABLE " + TABLE_TIME_ENTRIES + " (" + ID_AND_SERVER_ID
			+ " hashcode TEXT, time_start INTEGER, time_stop INTEGER, description TEXT, position TEXT, audo_record BLOB, customer_id INTEGER, time_entry_type_id INTEGER);";
	public static final String CREATE_TIME_ENTRY_TYPES = "CREATE TABLE " + TABLE_TIME_ENTRY_TYPES + " (" + ID_AND_SERVER_ID + " description TEXT, updated_at INTEGER);";
	public static final String CREATE_TIME_CUSTOMERS = "CREATE TABLE " + TABLE_CUSTOMERS + " (" + ID_AND_SERVER_ID
			+ " first_name TEXT, last_name TEXT, phone TEXT, position TEXT, updated_at INTEGER, address_id INTEGER);";

	private static final String TABLES_CREATE = CREATE_ADRESSES + CREATE_TIME_CUSTOMERS + CREATE_TIME_ENTRIES + CREATE_TIME_ENTRY_TYPES;
	private static final String TABLES_DELETE = "DROP TABLE " + TABLE_ADDRESSES + ";" + "DROP TABLE " + TABLE_TIME_ENTRIES + ";" + "DROP TABLE " + TABLE_TIME_ENTRY_TYPES + ";" + "DROP TABLE "
			+ TABLE_CUSTOMERS + ";";

	public DatabaseOpenHelper(Context context) {
		super(context, Config.DATABASE_NAME, null, DATABASE_VERSION); // context, name, factory, version
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLES_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLES_DELETE);
		db.execSQL(TABLES_CREATE);
	}

	public static SQLiteDatabase getDb(ContextWrapper applicationContext) {
		return new DatabaseOpenHelper(applicationContext).getReadableDatabase();
	}
}