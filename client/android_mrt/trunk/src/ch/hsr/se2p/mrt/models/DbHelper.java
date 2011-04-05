package ch.hsr.se2p.mrt.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import ch.hsr.se2p.mrt.util.Config;

public class DbHelper {

	// Customers table
	// public static final String CUSTOMERS_TABLE_NAME = "customers";
	// public static final String[] CUSTOMERS_C_FIRST_NAME = { "first_name", TEXT };
	// public static final String[] CUSTOMERS_C_LAST_NAME = { "last_name", TEXT };
	// public static final String[] CUSTOMERS_C_PHONE = { "phone", TEXT };
	// public static final String[] CUSTOMERS_C_POSITION = { "position", TEXT };
	// public static final String[] CUSTOMERS_C_UPDATED_AT = { "updated_at", INTEGER };
	// public static final String[] CUSTOMERS_C_ADDRESS_ID = { "address_id", INTEGER };
	// public static final String[][] CUSTOMERS_COLUMNS = { CUSTOMERS_C_FIRST_NAME, CUSTOMERS_C_LAST_NAME, CUSTOMERS_C_PHONE, CUSTOMERS_C_POSITION, CUSTOMERS_C_UPDATED_AT, CUSTOMERS_C_ADDRESS_ID };

	// Time entry table
	// public static final String TIME_ENTRY_TYPES_TABLE_NAME = "time_entry_types";
	// public static final String[] TIME_ENTRY_TYPES_C_FIRST_NAME = { "description", TEXT };
	// public static final String[] TIME_ENTRY_TYPES_C_UPDATED_AT = { "updated_at", INTEGER };
	// public static final String[][] TIME_ENTRY_TYPES_COLUMNS = { TIME_ENTRY_TYPES_C_FIRST_NAME, TIME_ENTRY_TYPES_C_UPDATED_AT };

	// Address table
	// public static final String ADDRESSES_TABLE_NAME = "addresses";
	// private static final String ID_AND_SERVER_ID = BaseColumns._ID + " INTEGER PRIMARY KEY ASC, server_id INTEGER, ";
	// public static final String CREATE_ADRESSES = "CREATE TABLE " + TABLE_ADDRESSES + " (" + ID_AND_SERVER_ID + "line1 TEXT, line2 TEXT, line3 TEXT, zip INTEGER, place TEXT);";

	private static final String DATABASE_NAME = "mrt.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TEXT = "TEXT";
	public static final String INTEGER = "INTEGER";
	public static final String BLOB = "BLOB";

	public static final String TIME_ENTRIES_TABLE_NAME = "time_entry_types";
	public static final String[] TIME_ENTRIES_C_HASHCODE = { "hashcode", TEXT };
	public static final String[] TIME_ENTRIES_C_DESCRIPTION = { "description", TEXT };
	public static final String[] TIME_ENTRIES_C_POSITION = { "position", TEXT };
	public static final String[] TIME_ENTRIES_C_AUDIO_RECORD = { "audo_record", BLOB };
	public static final String[] TIME_ENTRIES_C_TIME_START = { "time_start", INTEGER };
	public static final String[] TIME_ENTRIES_C_TIME_STOP = { "time_stop", INTEGER };
	public static final String[] TIME_ENTRIES_C_CUSTOMER_ID = { "customer_id", INTEGER };
	public static final String[] TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID = { "time_entry_type_id", INTEGER };
	public static final String[][] TIME_ENTRIES_COLUMNS = { TIME_ENTRIES_C_HASHCODE, TIME_ENTRIES_C_DESCRIPTION, TIME_ENTRIES_C_POSITION, TIME_ENTRIES_C_AUDIO_RECORD, TIME_ENTRIES_C_TIME_START,
			TIME_ENTRIES_C_TIME_STOP, TIME_ENTRIES_C_CUSTOMER_ID, TIME_ENTRIES_C_TIME_ENTRY_TYPE_ID };

	/* Add the other tables */
	private static final String[][][][] TABLE_DEFINITIONS = new String[][][][] { { { { TIME_ENTRIES_TABLE_NAME } }, TIME_ENTRIES_COLUMNS } };

	private Context context;
	private SQLiteDatabase db;

	public DbHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
	}

	private static String generateCreateTablesScript() {
		String generatedScript = "";
		for (String[][][] tableDefinition : TABLE_DEFINITIONS) {
			String tableName = tableDefinition[0][0][0];
			String[][] tableColumns = tableDefinition[1];
			generatedScript += generateCreateTablesScript(tableName, tableColumns) + "\n";
		}
		return generatedScript;
	}

	private static String generateCreateTablesScript(String tableName, String[][] tableColumns) {
		String generatedScript = "CREATE TABLE " + tableName + " (";
		boolean first = false;
		for (String[] tableColumn : tableColumns) {
			if (first) {
				first = false;
			} else {
				generatedScript += ", ";
			}
			String tableColumnName = tableColumn[0], tableColumnType = tableColumn[1];
			generatedScript += tableColumnName + " " + tableColumnType;
		}
		generatedScript += ");";
		return generatedScript;
	}

	private static String generateDropTablesScript() {
		String generatedScript = "";
		for (String[][][] tableDefinition : TABLE_DEFINITIONS) {
			String tableName = tableDefinition[0][0][0];
			generatedScript += generateDropTablesScript(tableName) + "\n";
		}
		return generatedScript;
	}

	private static String generateDropTablesScript(String tableName) {
		return "DROP TABLE IF EXISTS " + tableName + ";";
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(generateCreateTablesScript());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(generateDropTablesScript());
			onCreate(db);
		}
	}
}