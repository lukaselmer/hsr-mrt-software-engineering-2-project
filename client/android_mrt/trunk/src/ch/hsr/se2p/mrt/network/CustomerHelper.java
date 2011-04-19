package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;

public class CustomerHelper {
	protected HttpHelper httpHelper;

	public CustomerHelper(HttpHelper httpHelper) {
		this.httpHelper = httpHelper;
	}

	public static List<Customer> hackForTest() {
		List<Customer> list = new ArrayList<Customer>();
		list.add(new Customer(1, "Peter", "Muster"));
		list.add(new Customer(2, "Hans", "Bla"));
		list.add(new Customer(3, "Buuuu", "Baaaa"));
		list.add(new Customer(4, "Eufrosiene", "Katzenstein"));
		list.add(new Customer(5, "Papa", "Moll"));
		return list;
	}

	public boolean synchronize(List<Receivable> receivables, Class<Receivable> clazz) {
		try {
			String ret = synchronizeRequest(receivables);
			updateOrCreateReceivables(receivables, new JSONArray(ret), clazz);
			return true;
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		} catch (JSONException e) {
			// Request failed, pass
		} catch (IllegalAccessException e) {
			// Request failed, pass
			e.printStackTrace();
		} catch (InstantiationException e) {
			// Request failed, pass
			e.printStackTrace();
		}
		return false;
	}

	private void updateOrCreateReceivables(List<Receivable> receivables, JSONArray jsonArray, Class<Receivable> clazz) throws JSONException, IllegalAccessException, InstantiationException {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject o = jsonArray.getJSONObject(i);
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

	// JSONArray ret = new JSONArray(); for (Receivable receivable : receivables) { JSONObject j = new JSONObject();
	// j.put("id", receivable.getIdOnServer()); j.put("updated_at", receivable.getUpdatedAt()); ret.put(j); } return ret;
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
}
