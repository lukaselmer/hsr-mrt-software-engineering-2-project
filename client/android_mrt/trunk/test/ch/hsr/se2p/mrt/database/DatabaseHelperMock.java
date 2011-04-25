package ch.hsr.se2p.mrt.database;

import android.content.Context;
import ch.hsr.se2p.mrt.database.DatabaseHelper;

public class DatabaseHelperMock extends DatabaseHelper {
	private final Context mContext;

	public DatabaseHelperMock(Context context) {
		super(context);
		this.mContext = context;
	}

	public void deleteDatabase() {
		mContext.deleteDatabase(DATABASE_NAME);
	}
}
