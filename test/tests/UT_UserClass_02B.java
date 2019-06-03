package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * User.GetGroupArraySingleIndexTest
 */
public class UT_UserClass_02B extends AbstractTest {
	
	GUIHandler gh;
	User u;
	int gID;
	
	public void prepare(){
		gh = new GUIHandler();
		u = new User();

		gID = 2;
		u.addGroupArray(1); // 0
		u.addGroupArray(2); // 1
		u.addGroupArray(3); // 2
		u.addGroupArray(4); // 3
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertEqual(3, u.getGroupArraySingleIndex(gID));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    	 u = null;
    }
}
