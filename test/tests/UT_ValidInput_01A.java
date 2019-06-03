package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.ValidInputTest
 */
public class UT_ValidInput_01A extends AbstractTest {
	
	GUIHandler gh;
	String strPass, strFail;
	
	public void prepare(){
		 gh = new GUIHandler();
		 strFail = "please select option";
		 strPass = "string";
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertFalse(gh.validInput(""));
		TestUtils.assertFalse(gh.validInput(strFail));
		TestUtils.assertTrue(gh.validInput(strPass));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
