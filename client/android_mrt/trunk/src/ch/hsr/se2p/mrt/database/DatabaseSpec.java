package ch.hsr.se2p.mrt.database;

import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.GpsPosition;
import ch.hsr.se2p.mrt.models.TimeEntry;
import ch.hsr.se2p.mrt.models.TimeEntryType;

class DatabaseSpec {
	protected static final String DATABASE_NAME = "mrt.db";
	protected static final int DATABASE_VERSION = 8;
	static final Class<?> MODEL_CLASSES[] = { TimeEntry.class, Customer.class, TimeEntryType.class, GpsPosition.class };
}
