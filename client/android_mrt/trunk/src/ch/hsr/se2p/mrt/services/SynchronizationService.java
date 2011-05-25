package ch.hsr.se2p.mrt.services;

import java.util.Timer;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import ch.hsr.se2p.mrt.database.DatabaseHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class SynchronizationService extends OrmLiteBaseService<DatabaseHelper> {
	static {
		OpenHelperManager.setOpenHelperClass(OrmLiteSqliteOpenHelper.class);
	}
	private static final String TAG = SynchronizationService.class.getSimpleName();

	private Timer timer;
	private SynchronizationServiceTask serviceTask;

	private static final int UPDATE_INTERVAL = 1000 * 30; // every 30 seconds
	private static final int START_DELAY = 1000; // 1 seconds

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
		serviceTask = new SynchronizationServiceTask(this);
		timer = new Timer("SynchronizationServiceTimer");
		timer.schedule(serviceTask, START_DELAY, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");
		timer.cancel();
		timer = null;
	}
}