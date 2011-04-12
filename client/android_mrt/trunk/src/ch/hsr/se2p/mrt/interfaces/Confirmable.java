package ch.hsr.se2p.mrt.interfaces;

import org.json.JSONObject;

public interface Confirmable {
	public JSONObject toJSONObject();
	public int getRailsId();
}
