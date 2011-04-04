package ch.hsr.se2p.mrt.util;

public class Config {
	private Config() {
	}

	public static final String DATABASE_NAME = "mrt";
	public static final String MRT_HOST = "mrt.elmermx.ch";
	public static final String MRT_SERVER = "http://" + MRT_HOST;
	// public static final String CHECK_INTERNET_CONNTECTION_URL = MRT_SERVER + "/logins/new.tt";
	public static final String LOGIN_URL_P1 = MRT_SERVER + "/logins.tt?username=";
	public static final String LOGIN_URL_P2 = "&password=";
	// public static final String STUDENTS_URL = MRT_SERVER + "/students.tt";
	// public static final String OWN_TIMETABLE_URL = MRT_SERVER + "/timetables/own.tt";
}
