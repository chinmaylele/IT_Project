package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.ValidEmailTest
 */
public class UT_ValidEmail_01A extends AbstractTest {
	
	GUIHandler gh;
	String emailPass, emailFail;
	
	public void prepare(){
		 gh = new GUIHandler();
		 emailPass = "email@weltec.ac.nz";
		 emailFail = "email@email.com";
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertTrue(gh.validEmail(emailPass));
		TestUtils.assertFalse(gh.validEmail(emailFail));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
