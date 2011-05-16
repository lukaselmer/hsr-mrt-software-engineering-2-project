package ch.hsr.se2p.mrt.database.mocks;

import android.content.Context;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.database.DatabaseSpec;

public class DatabaseHelperMock extends DatabaseHelper {
	private final Context mContext;

	public DatabaseHelperMock(Context context) {
		super(context);
		this.mContext = context;
	}

	public void deleteDatabase() {
		mContext.deleteDatabase(DatabaseSpec.DATABASE_NAME);
	}
}
