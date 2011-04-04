package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.R;
import ch.hsr.se2p.mrt.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

		// final Button button = (Button) findViewById(R.id.login_button);
		// final EditText usernameEditText = (EditText)
		// findViewById(R.id.login), passwordEditText = (EditText)
		// findViewById(R.id.password);
		// String[] usernameAndPassword = loadLoginFromCache();
		// if (usernameAndPassword != null) {
		// doRequests(usernameAndPassword[0], usernameAndPassword[1]);
		// }
		// button.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// String username = usernameEditText.getText().toString(), password =
		// passwordEditText.getText().toString();
		// doRequests(username, password);
		// }
		// });
    }
}