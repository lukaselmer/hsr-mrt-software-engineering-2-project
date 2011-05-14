package ch.hsr.se2p.mrt;

import java.sql.Timestamp;
import java.util.Date;

import ch.hsr.se2p.mrt.models.ISO8601DateParser;

public class ISO8601DateParserHelperForTests extends ISO8601DateParser {
	public static Date formatAndParse(Date date) {
		return parse(toString(date));
	}

	public static Timestamp formatAndParseToTimestamp(Date date) {
		return new Timestamp(parse(toString(date)).getTime());
	}
}
