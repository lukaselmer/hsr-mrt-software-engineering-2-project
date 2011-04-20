package ch.hsr.se2p.mrt.models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;

public class TimeEntryType implements Receivable {
	private Integer id;
	private String name;

	public TimeEntryType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	@Override
	public int getIdOnServer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Timestamp getUpdatedAt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fromJSON(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		return false;
	}

}
