package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.MakeDOBStringTest
 */
public class UT_DOBString_02A extends AbstractTest {
	
	GUIHandler gh;
	int d, m, y;
	
	public void prepare(){
		 gh = new GUIHandler();

		 d = 1;
		 m = 1;
		 y = 2000;
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertEqual("01/01/2000", gh.makeDOBString(d, m, y));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
