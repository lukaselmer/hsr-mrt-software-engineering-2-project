package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateHelper {
	private static final String TAG = DateHelper.class.getSimpleName();
	private static SimpleDateFormat iso8601formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public static Date parse(String date) {
		try {
			return iso8601formatter.parse(date);
		} catch (ParseException e) {
			// Invalid date, pass
			Log.e(TAG, "Date not parsable", e);
		}
		return null;
	}

	public static String format(Date date) {
		return iso8601formatter.format(date);
	}

	public static Date formatAndParse(Date date) {
		return parse(format(date));
	}

	public static Timestamp formatAndParseToTimestamp(Date date) {
		return new Timestamp(parse(format(date)).getTime());
	}
}
