package ch.hsr.se2p.mrt.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import android.preference.PreferenceManager;
import android.util.Log;
import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.interfaces.Receivable;
import ch.hsr.se2p.mrt.models.TimeEntryType;
import ch.hsr.se2p.mrt.network.UserHelper;

import com.j256.ormlite.dao.Dao;

class SynchronizationServiceTask extends TimerTask {
	private static final String TAG = SynchronizationService.class.getSimpleName();
	private DatabaseHelper databaseHelper;
	private MRTApplication mrtApplication;
	private SynchronizationService service;

	public SynchronizationServiceTask(SynchronizationService service) {
		this.service = service;
		this.databaseHelper = service.getHelper();
		this.mrtApplication = (MRTApplication) service.getApplication();
		mrtApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(service));
	}

	@Override
	public void run() {
		loadPreferences();
		if (!login()) {
			Log.e(TAG, "Login failed!");
			return;
		}
		Synchronizer[] synchronizers = { new TimeEntrySynchronizer(databaseHelper, mrtApplication),
				new CustomerSynchronizer(databaseHelper, mrtApplication), new TimeEntryTypeSynchronizer(databaseHelper, mrtApplication) };
		for (Synchronizer synchronizer : synchronizers) {
			synchronizer.synchronize();
		}
	}

	private void loadPreferences() {
		mrtApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(service));
	}

	private boolean login() {
		return new UserHelper(mrtApplication.getHttpHelper()).login(mrtApplication);
	}

};
