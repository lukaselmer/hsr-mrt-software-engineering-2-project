package ch.hsr.se2p.mrt.network;

public class NetworkConfig {
	private NetworkConfig() {
	}

	// public static final String MRT_HOST = "local.mrt.elmermx.ch:3000";
	// public static final String MRT_HOST = "10.0.2.2:3000";
	public static final String MRT_HOST = "mrt.elmermx.ch";
	public static final String MRT_SERVER = "http://" + MRT_HOST;
	public static final String TIME_ENTRY_CREATE_URL = MRT_SERVER + "/time_entries.json";
	public static final String TIME_ENTRY_CONFIRM_URL = MRT_SERVER + "/time_entries/%d/remove_hashcode.json";
	public static final String LOGIN_URL = MRT_SERVER + "/login.json";
	public static final String SYNCHRONIZE_CUSTOMERS_URL = MRT_SERVER + "/customers/synchronize.json";

}
