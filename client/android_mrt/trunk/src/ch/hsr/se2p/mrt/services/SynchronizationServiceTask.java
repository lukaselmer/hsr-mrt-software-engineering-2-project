package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import android.util.Log;
import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import ch.hsr.se2p.mrt.network.CustomerHelper;
import ch.hsr.se2p.mrt.network.UserHelper;

import com.j256.ormlite.dao.Dao;

class SynchronizationServiceTask extends TimerTask {
	private static final String TAG = SynchronizationService.class.getSimpleName();
	private DatabaseHelper databaseHelper;
	private MRTApplication mrtApplication;

	public SynchronizationServiceTask(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void run() {
		login();
		syncCustomers();
		syncTimeEntryTypes();
	}

	private boolean login() {
		return new UserHelper(mrtApplication.getHttpHelper()).login(mrtApplication.getEmail(), mrtApplication.getEmail(),
				mrtApplication.getCurrentUser());
	}

	protected void syncCustomers() {
		Log.i(TAG, "Synchronizing customers");
		try {
			Dao<Customer, Integer> dao = databaseHelper.getDao(Customer.class);
			List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
			synchronizeReceivables(dao, new CustomerHelper(mrtApplication.getHttpHelper()), receivables);
		} catch (SQLException e) {
			Log.e(TAG, "Database error", e);
		}
	}

	protected void syncTimeEntryTypes() {
		if (true)
			return; // TODO: implement this
		Log.i(TAG, "Synchronizing time entry types");
		try {
			Dao<TimeEntryType, Integer> dao = databaseHelper.getDao(TimeEntryType.class);
			List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
			// TODO: synchronizeReceivables(dao, new CustomerHelper(mrtApplication.getHttpHelper()), receivables);
		} catch (SQLException e) {
			Log.e(TAG, "Database error", e);
		}
	}

	protected void synchronizeReceivables(Dao<Customer, Integer> dao, CustomerHelper ch, List<Receivable> receivables) throws SQLException {
		if (ch.synchronize(receivables, Customer.class)) {
			for (Receivable receivable : receivables) {
				processClient(dao, (Customer) receivable);
			}
		} else {
			Log.e(TAG, "Error synchronizing customers");
		}
	}

	protected void processClient(Dao<Customer, Integer> dao, Customer c) throws SQLException {
		if (c.isDeleted()) {
			if (c.getId() != null && c.getId() > 0) {
				dao.delete(c);
				return;
			}
		}
		if (c.getId() != null && c.getId() > 0) {
			dao.update(c);
		} else {
			dao.create(c);
		}
	}
};
