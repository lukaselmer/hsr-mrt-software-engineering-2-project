package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.GpsPosition;

public class CustomerHelper {
	protected HttpHelper httpHelper;

	public CustomerHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public boolean synchronize(List<Receivable> receivables, Class<? extends Receivable> clazz) throws SynchronizationException {
		try {
			String ret = synchronizeRequest(receivables);
			JSONArray arr = new JSONArray(ret);
			if (arr.length() == 0)
				return false;
			updateOrCreateReceivables(receivables, arr, clazz);
			return true;
		} catch (Exception e) {
			// Request failed, throw synchronization exception
			throw new SynchronizationException(e);
		}
	}

	private void updateOrCreateReceivables(List<Receivable> receivables, JSONArray arrayOfJSONReceivables, Class<? extends Receivable> clazz)
			throws JSONException, IllegalAccessException, InstantiationException {
		for (int i = 0; i < arrayOfJSONReceivables.length(); i++) {
			JSONObject o = arrayOfJSONReceivables.getJSONObject(i).getJSONObject("customer");
			Receivable r = clazz.newInstance();
			r.fromJSON(o);
			
			updateOrCreateReceivable(r, receivables, o);
		}
	}

	private void updateOrCreateReceivable(Receivable r, List<Receivable> receivables, JSONObject jsonObject) throws JSONException {
		for (Receivable receivable : receivables) {
			if (receivable.getIdOnServer() == r.getIdOnServer()) {
				receivable.fromJSON(jsonObject);
				return;
			}
		}
		receivables.add(r);
	}

	private String synchronizeRequest(List<Receivable> receivables) throws IOException, JSONException {
		return httpHelper.doHttpPost(generateJSONRequest(receivables), NetworkConfig.SYNCHRONIZE_CUSTOMERS_URL);
	}

	protected JSONObject generateJSONRequest(List<Receivable> receivables) throws JSONException {
		JSONObject ret = new JSONObject();
		Timestamp maxUpdatedAt = new Timestamp(0);
		for (Receivable receivable : receivables) {
			if (maxUpdatedAt.before(receivable.getUpdatedAt())) {
				maxUpdatedAt = receivable.getUpdatedAt();
			}
		}
		ret.put("last_update", maxUpdatedAt);
		return ret;
	}
	
	public static void calculateDistances(Dao<GpsPosition, Integer> dao, List<Customer> customers, GpsPosition currentPosition) {
		
		for (Customer c : customers) {
			if (c.hasGpsPosition()){
				GpsPosition customerPosition;
				try {
					customerPosition = dao.queryForId(c.getGpsPositionId());
					double distance = currentPosition.distanceTo(customerPosition);
					
					// Circle 1000m
					if (distance <= 1000) c.setDistance(distance);
					else c.setDistance(null);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				c.setDistance(null);
			}
		}
	}
}
