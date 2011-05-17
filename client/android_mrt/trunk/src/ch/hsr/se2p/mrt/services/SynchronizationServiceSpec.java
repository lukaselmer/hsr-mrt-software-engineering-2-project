package ch.hsr.se2p.mrt.services;

class SynchronizationServiceSpec {
	protected static final Class<?>[] SYNCHRONIZERS = { TimeEntrySynchronizer.class, CustomerSynchronizer.class, TimeEntryTypeSynchronizer.class };

	protected Class<?>[] getSynchronizers() {
		return SYNCHRONIZERS;
	}
}
