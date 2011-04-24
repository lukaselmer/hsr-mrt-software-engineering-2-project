package ch.hsr.se2p.mrt.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	private static SimpleDateFormat iso8601formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public static Date parse(String date) {
		try {
			return iso8601formatter.parse(date);
		} catch (ParseException e) {
			// Invalid date, pass
		}
		return null;
	}

	public static String format(Date date) {
		return iso8601formatter.format(date);
	}

	public static Date formatAndParse(Date date) {
		return parse(format(date));
	}
}
