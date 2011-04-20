package ch.hsr.se2p.mrt.activities;

import ch.hsr.se2p.mrt.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
	
	public LoginActivityTest(String pkg, Class<LoginActivity> activityClass) {
		super("ch.hsr.se2p.mrt.activities", LoginActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	private LoginActivity activity;
    private EditText editUsername;
    private EditText editPasswort;
    private CheckBox checkbox;
	
    @Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = this.getActivity();
        editUsername = (EditText) activity.findViewById(R.id.editUsername);
        editPasswort = (EditText) activity.findViewById(R.id.editPassword);
        checkbox = (CheckBox) activity.findViewById(R.id.cBsaveLogin);
	}
	
    @Override
    public void runTestOnUiThread(Runnable r) throws Throwable {
    	// TODO Auto-generated method stub
    	super.runTestOnUiThread(r);
    }
    
	public void testPreconditions() {
	    assertNull(editUsername);
	    assertNull(editPasswort);
	    assertEquals(true, checkbox.isChecked());
	}
	
	public void testNoLoginDataSupplied() {
		editUsername = null;
		editPasswort = null;
		//AlertDialog.Builder b = new AlertDialog.Builder(ch.hsr.se2p.mrt.activities.LoginActivity);
		//b.setTitle("Fehler");
		//b.setMessage("Bitte Benutzernamen und Passwort angeben!");
		
		final Button loginBtn = (Button) activity.findViewById(R.id.loginButton);
	    activity.runOnUiThread(new Runnable()
	    {
	        public void run()
	        {
	            loginBtn.performClick();
	        }
	    });
	    
	
		
		assertNull(editUsername);
		assertNull(editPasswort);
		//assertTrue(b.isShowing());
		
	}
	
	
	public void testNoUsernameSupplied() {
		
	}
	public void testNoPasswordSupplied() {
		
	}

	
//	@Override
//	protected void tearDown() throws Exception {
//		// TODO Auto-generated method stub
//		super.tearDown();
//	}
}
