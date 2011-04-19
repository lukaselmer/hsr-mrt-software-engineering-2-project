package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences  extends PreferenceActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	}

}
