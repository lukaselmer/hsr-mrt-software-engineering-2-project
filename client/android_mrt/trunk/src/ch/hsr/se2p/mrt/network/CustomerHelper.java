package ch.hsr.se2p.mrt.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			updateReceivables(receivables, ret);
			return true;
		} catch (NullPointerException e) {
			// Request failed, pass
		} catch (IOException e) {
			// Request failed, pass
		} catch (JSONException e) {
			// Request failed, pass
		}
		return false;
	}

	private void updateReceivables(List<Receivable> receivables, String ret) {
		// return receivable.fromJSON(new JSONObject(ret));

	}

	private String synchronizeRequest(List<Receivable> receivables) throws IOException, JSONException {
		return httpHelper.doHttpPost(generateJSONRequest(receivables), NetworkConfig.SYNCHRONIZE_CUSTOMERS_URL);
	}

	protected JSONObject generateJSONRequest(List<Receivable> receivables) throws JSONException {
		JSONObject ret = new JSONObject();
		for (Receivable receivable : receivables) {
			JSONObject j = new JSONObject();
			j.put("updated_at", receivable.getUpdatedAt());
			ret.put("" + receivable.getIdOnServer(), j);
		}
		return ret;
	}
}
