package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.ChangeDOBformatTest
 */
public class UT_DOBString_02B extends AbstractTest {
	
	GUIHandler gh;
	String dateSlashed, dateString;
	
	public void prepare(){
		 gh = new GUIHandler();

		 dateSlashed = "01/01/2000";
		 dateString = "01 January 2000";
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertEqual("01/01/2000", gh.changeDOBFormat(dateString));
		TestUtils.assertEqual("01 January 2000", gh.changeDOBFormat(dateSlashed));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
