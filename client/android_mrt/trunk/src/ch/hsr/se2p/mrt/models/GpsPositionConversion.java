package ch.hsr.se2p.mrt.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts the GPS Position in degres to the Swiss Coordinates.
 * Source: http://www.swisstopo.admin.ch/internet/swisstopo/de/home/topics/survey/sys/refsys.parsysrelated1.23611.downloadList.11186.DownloadFile.tmp/swissprojectionde.pdf
 */
public class GpsPositionConversion {
	
	public static List<Double> calculateWGSToLV03(double latitude, double longitude, double ellHeight) {
		double eastCoordinate = WGStoCHy(latitude, longitude);
		double northCoordinate = WGStoCHx(latitude, longitude);
		// height = WGStoCHh(latitude, longitude, ellHeight);
		List<Double> l = new ArrayList<Double>();
		l.add(eastCoordinate);
		l.add(northCoordinate);
		return l;
	}

	// Convert WGS lat/long (° dec) to CH y
	private static double WGStoCHy(double lat, double lng) {
		// Converts degrees dec to sex
		lat = DecToSexAngle(lat);
		lng = DecToSexAngle(lng);

		// Converts degrees to seconds (sex)
		lat = SexAngleToSeconds(lat);
		lng = SexAngleToSeconds(lng);

		// Axiliary values (% Bern)
		double lat_aux = (lat - 169028.66) / 10000;
		double lng_aux = (lng - 26782.5) / 10000;

		// Process Y
		double y = 600072.37 + 211455.93 * lng_aux - 10938.51 * lng_aux * lat_aux - 0.36 * lng_aux * Math.pow(lat_aux, 2) - 44.54 * Math.pow(lng_aux, 3);

		return y;
	}

	// Convert WGS lat/long (° dec) to CH x
	private static double WGStoCHx(double lat, double lng) {
		// Converts degrees dec to sex
		lat = DecToSexAngle(lat);
		lng = DecToSexAngle(lng);

		// Converts degrees to seconds (sex)
		lat = SexAngleToSeconds(lat);
		lng = SexAngleToSeconds(lng);

		// Axiliary values (% Bern)
		double lat_aux = (lat - 169028.66) / 10000;
		double lng_aux = (lng - 26782.5) / 10000;

		// Process X
		double x = 200147.07 + 308807.95 * lat_aux + 3745.25 * Math.pow(lng_aux, 2) + 76.63 * Math.pow(lat_aux, 2) - 194.56 * Math.pow(lng_aux, 2)
				* lat_aux + 119.79 * Math.pow(lat_aux, 3);

		return x;
	}

	// Convert WGS lat/long (° dec) and height to CH h
	private static double WGStoCHh(double lat, double lng, double h) {
		// Converts degrees dec to sex
		lat = DecToSexAngle(lat);
		lng = DecToSexAngle(lng);

		// Converts degrees to seconds (sex)
		lat = SexAngleToSeconds(lat);
		lng = SexAngleToSeconds(lng);

		// Axiliary values (% Bern)
		double lat_aux = (lat - 169028.66) / 10000;
		double lng_aux = (lng - 26782.5) / 10000;

		// Process h
		h = h - 49.55 + 2.73 * lng_aux + 6.94 * lat_aux;

		return h;
	}

	// Convert decimal angle (degrees) to sexagesimal angle (degrees, minutes and seconds dd.mmss,ss)
	private static double DecToSexAngle(double dec) {
		int deg = (int) Math.floor(dec);
		int min = (int) Math.floor((dec - deg) * 60);
		double sec = (((dec - deg) * 60) - min) * 60;

		// Output: dd.mmss(,)sshs
		return deg + (double) min / 100 + (double) sec / 10000;
	}

	// Convert sexagesimal angle (degrees, minutes and seconds dd.mmss,ss) to seconds
	private static double SexAngleToSeconds(double dms) {
		double deg = 0, min = 0, sec = 0;
		deg = Math.floor(dms);
		min = Math.floor((dms - deg) * 100);
		sec = (((dms - deg) * 100) - min) * 100;

		// Result in degrees sex (dd.mmss)
		return sec + (double) min * 60 + (double) deg * 3600;
	}

}
