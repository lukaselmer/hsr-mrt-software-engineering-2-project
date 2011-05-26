package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import ch.hsr.se2p.mrt.application.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.models.GpsPosition;
import ch.hsr.se2p.mrt.network.CustomerHelper;
import ch.hsr.se2p.mrt.network.SynchronizationException;

import com.j256.ormlite.dao.Dao;

/**
 * Synchronizes the clients customer data with the customer data on the server.
 */
class CustomerSynchronizer implements Synchronizer {
	private static final String TAG = CustomerSynchronizer.class.getSimpleName();

	private final DatabaseHelper databaseHelper;
	private final MRTApplication mrtApplication;

	public CustomerSynchronizer(DatabaseHelper databaseHelper, MRTApplication mrtApplication) {
		this.databaseHelper = databaseHelper;
		this.mrtApplication = mrtApplication;
	}

	@Override
	public void synchronize() {
		Log.d(TAG, "Synchronizing customers");
		try {
			Dao<Customer, Integer> dao = databaseHelper.getCustomerDao();
			List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
			synchronizeCustomers(dao, new CustomerHelper(mrtApplication.getHttpHelper()), receivables);
		} catch (SQLException e) {
			Log.e(TAG, "Database error", e);
		} catch (Exception e) {
			Log.e(TAG, "Customer sync error", e);
		}
	}

	protected void synchronizeCustomers(Dao<Customer, Integer> dao, CustomerHelper ch, List<Receivable> receivables) throws SQLException {
		try {
			if (ch.synchronize(receivables, Customer.class)) {
				for (Receivable receivable : receivables) {
					processCustomer(dao, (Customer) receivable);
				}
			}
		} catch (SynchronizationException e) {
			Log.e(TAG, "Error synchronizing customers", e);
		}
	}

	protected void processCustomer(Dao<Customer, Integer> dao, Customer c) throws SQLException {
		try {
			if (handleCreation(dao, c))
				return;
			if (handleDeletion(dao, c))
				return;
			else
				handleUpdate(dao, c);
		} finally {
			c.setChanged(false);
		}
	}

	private boolean existingCustomer(Customer c) {
		return c.getId() != null && c.getId() > 0;
	}

	private void handleUpdate(Dao<Customer, Integer> dao, Customer c) throws SQLException {
		if (!c.hasChanged())
			return;
		updatePosition(c);
		Log.d(TAG, "Updating " + c);
		dao.update(c);
	}

	private void updatePosition(Customer c) throws SQLException {
		handleGpsPosition(c);
		if (c.getGpsPosition() != null) {
			databaseHelper.getGpsPositionDao().create(c.getGpsPosition());
			c.setGpsPositionId(c.getGpsPosition().getId());
		}
	}

	private boolean handleCreation(Dao<Customer, Integer> dao, Customer c) throws SQLException {
		if (existingCustomer(c)) {
			return false;
		}
		Dao<GpsPosition, Integer> positionDao = databaseHelper.getGpsPositionDao();
		if (c.getGpsPosition() != null) {
			positionDao.create(c.getGpsPosition());
			c.setGpsPositionId(c.getGpsPosition().getId());
		}
		Log.d(TAG, "Creating " + c);
		dao.create(c);
		return true;
	}

	private boolean handleDeletion(Dao<Customer, Integer> dao, Customer c) throws SQLException {
		if (!c.hasChanged())
			return false;
		handleGpsPosition(c);
		if (!c.isDeleted() && existingCustomer(c)) {
			Log.d(TAG, "Deleting " + c);
			dao.delete(c);
			return true;
		}
		return false;
	}

	private void handleGpsPosition(Customer c) throws SQLException {
		Dao<GpsPosition, Integer> positionDao = databaseHelper.getGpsPositionDao();
		if (c.hasGpsPosition()) {
			GpsPosition oldPosition = positionDao.queryForId(c.getGpsPositionId());
			positionDao.delete(oldPosition);
		}
	}
}
