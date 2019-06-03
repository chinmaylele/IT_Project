package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.MatchPasswordTest
 */
public class UT_MatchPassword_01A extends AbstractTest {
	
	GUIHandler gh;
	String pw, pw2, pw3;
	
	public void prepare(){
		 gh = new GUIHandler();
		 pw = "password";
		 pw2 = "password";
		 pw3 = "fail";
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertTrue(gh.matchPassword(pw, pw2));
		TestUtils.assertFalse(gh.matchPassword(pw, pw3));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
