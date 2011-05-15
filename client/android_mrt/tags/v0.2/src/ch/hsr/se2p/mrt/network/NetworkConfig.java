package ch.hsr.se2p.mrt.network;

class NetworkConfig {
	private NetworkConfig() {
	}

	// public static final String MRT_HOST = "local.mrt.elmermx.ch:3000";

	// 10.0.2.2 represents the host the emulator is running on
	// public static final String MRT_HOST = "10.0.2.2:3000";

	protected static final String MRT_HOST = "mrt.elmermx.ch";
	protected static final String MRT_SERVER = "http://" + MRT_HOST;
	protected static final String TIME_ENTRY_CREATE_URL = MRT_SERVER + "/time_entries.json";
	protected static final String TIME_ENTRY_CONFIRM_URL = MRT_SERVER + "/time_entries/%d/remove_hashcode.json";
	protected static final String LOGIN_URL = MRT_SERVER + "/users/sign_in.json";
	protected static final String SYNCHRONIZE_CUSTOMERS_URL = MRT_SERVER + "/customers/synchronize.json";
	protected static final String SYNCHRONIZE_TIME_ENTRY_TYPES_URL = MRT_SERVER + "/time_entry_types/synchronize.json";

}
