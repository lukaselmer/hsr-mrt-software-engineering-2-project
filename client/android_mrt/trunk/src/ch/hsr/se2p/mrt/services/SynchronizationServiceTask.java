package ch.hsr.se2p.mrt.services;

import java.util.TimerTask;

import android.content.Context;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;
import ch.hsr.se2p.mrt.activities.MRTApplication;
import ch.hsr.se2p.mrt.database.DatabaseHelper;
import ch.hsr.se2p.mrt.network.UserHelper;

/**
 * Each synchronization iteration, a synchronization service task is created and run.
 */
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
		initGpsListener();
	}

	private void initGpsListener() {
		LocationManager locationManager = (LocationManager) service.getSystemService(Context.LOCATION_SERVICE);
		long locationUpdateInterval = 77; // update location maximal every 1 minute TODO: update this!! 1 * 60 * 1000
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationUpdateInterval, 0, new LocationListenerAdapter());
		locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		mrtApplication.setLocationManager(locationManager);
	}

	@Override
	public void run() {
		loadPreferences();
		if (!login()) {
			Log.e(TAG, "Login failed!");
			return;
		}
		runSynchronizers();
	}

	private void runSynchronizers() {
		Class<?>[] classes = { TimeEntrySynchronizer.class, CustomerSynchronizer.class, TimeEntryTypeSynchronizer.class };
		for (Class<?> cls : classes) {
			try {
				@SuppressWarnings("unchecked")
				Class<Synchronizer> syncCls = (Class<Synchronizer>) cls;
				newSynchronizerInstance(syncCls).synchronize();
				Log.d(TAG, "Synchronizer " + cls.getSimpleName() + " created!");
			} catch (Exception e) {
				Log.e(TAG, "Synchronizer " + cls.getSimpleName() + " creation failed!", e);
			}
		}
	}

	private Synchronizer newSynchronizerInstance(Class<Synchronizer> syncCls) throws Exception {
		return syncCls.getConstructor(DatabaseHelper.class, MRTApplication.class).newInstance(databaseHelper, mrtApplication);
	}

	private void loadPreferences() {
		mrtApplication.setPreferences(PreferenceManager.getDefaultSharedPreferences(service));
	}

	private boolean login() {
		return new UserHelper(mrtApplication.getHttpHelper()).login(mrtApplication);
	}

};
