package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.services.SynchronizationService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;

class ActivityHelper {

	protected static void displayAlertDialog(String title, String message, Activity activity) {
		getAlertDialog(title, message, activity).show();
	}

	private static Dialog getAlertDialog(String title, String message, Activity activity) {
		AlertDialog.Builder b = new AlertDialog.Builder(activity);
		b.setTitle(title);
		b.setMessage(message);
		b.setPositiveButton("Ok", null);
		Dialog d = b.create();
		return d;
	}

	public static void startSyncService(Activity activity) {
		activity.startService(new Intent(SynchronizationService.class.getName()));
	}
}