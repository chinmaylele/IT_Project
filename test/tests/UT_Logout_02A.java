package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.LogoutTest
 */
public class UT_Logout_02A extends AbstractTest {
	
	GUIHandler gh;
	User u;
	
	public void prepare(){
		 gh = new GUIHandler();
		 u = new User();
		 u.setUsername("username");
	}
	
	public boolean runTest() throws Exception {
		gh.logout(u);
		
		TestUtils.assertNull(u.getUsername());
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    	 u = null;
    }
}
