package ch.hsr.se2p.mrt.activities;

import java.sql.Timestamp;

import ch.hsr.se2p.mrt.models.TimeEntry;

class Measurement {
	private TimeEntry timeEntry;
	private boolean started = false;

	public Measurement() {
		this(true);
	}

	public Measurement(boolean start) {
		if (start)
			start();
	}

	private void start() {
		started = true;
		timeEntry = new TimeEntry(new Timestamp(System.currentTimeMillis()));
	}

	public TimeEntry getTimeEntry() {
		return timeEntry;
	}

	public void stop() {
		started = false;
	}

	public boolean isStarted() {
		return started;
	}
}
