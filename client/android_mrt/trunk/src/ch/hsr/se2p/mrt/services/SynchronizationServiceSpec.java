package ch.hsr.se2p.mrt.services;

class SynchronizationServiceSpec {
	public static final Class<?>[] SYNCHRONIZERS = { TimeEntrySynchronizer.class, CustomerSynchronizer.class, TimeEntryTypeSynchronizer.class };
}
