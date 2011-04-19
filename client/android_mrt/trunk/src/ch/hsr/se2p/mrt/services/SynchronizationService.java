package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.Customer;
import ch.hsr.se2p.mrt.network.CustomerHelper;
import ch.hsr.se2p.mrt.network.HttpHelper;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;

public class SynchronizationService extends OrmLiteBaseService<DatabaseHelper> {
	private static final String TAG = SynchronizationService.class.getSimpleName();

	private Timer timer;

	private static final int UPDATE_INTERVAL = 1000 * 30; // every 30 seconds TODO: Adjust value for production
	private static final int START_DELAY = 1000; // 1 seconds

	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			Log.i(TAG, "Synchronizing customers");
			try {
				Dao<Customer, Integer> dao = getHelper().getDao(Customer.class);
				CustomerHelper ch = new CustomerHelper(HttpHelper.inst());
				List<Receivable> receivables = new ArrayList<Receivable>(dao.queryForAll());
				synchronizeReceivables(dao, ch, receivables);
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Not bindable, so return null
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");

		timer = new Timer("SynchronizationServiceTimer");
		timer.schedule(updateTask, START_DELAY, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");

		timer.cancel();
		timer = null;
	}
}