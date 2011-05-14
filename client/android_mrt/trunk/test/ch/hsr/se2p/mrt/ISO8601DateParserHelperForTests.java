package ch.hsr.se2p.mrt;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class ISO8601DateParserHelperForTests {
	public static Date parse(String input) {
		// NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
		// things a bit. Before we go on we have to repair this.
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

		// this is zero time so we need to add that TZ indicator for
		if (input.endsWith("Z")) {
			input = input.substring(0, input.length() - 1) + "GMT-00:00";
		} else {
			int inset = 6;
			String s0 = input.substring(0, input.length() - inset);
			String s1 = input.substring(input.length() - inset, input.length());
			input = s0 + "GMT" + s1;
		}
		try {
			return df.parse(input);
		} catch (ParseException e) {
			Log.w(ISO8601DateParserHelperForTests.class.getSimpleName(), "Unparsable date", e);
			return new Date(0);
		}
	}

	public static String toString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
		TimeZone tz = TimeZone.getTimeZone("UTC");
		df.setTimeZone(tz);
		String output = df.format(date);
		int inset0 = 9;
		int inset1 = 6;
		String s0 = output.substring(0, output.length() - inset0);
		String s1 = output.substring(output.length() - inset1, output.length());
		String result = s0 + s1;
		result = result.replaceAll("UTC", "+00:00");
		return result;
	}

	public static Date formatAndParse(Date date) {
		return parse(toString(date));
	}

	public static Timestamp formatAndParseToTimestamp(Date date) {
		return new Timestamp(parse(toString(date)).getTime());
	}
}
