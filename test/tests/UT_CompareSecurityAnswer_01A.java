package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.CompareSecurityAnswerTest
 */
public class UT_CompareSecurityAnswer_01A extends AbstractTest {
	
	GUIHandler gh;
	User u;
	String ans, wrg;
	boolean bool;
	
	public void prepare(){
		 gh = new GUIHandler();
		 u = new User();
		 u.setSecurityAnswer("answer");
		 ans = "answer";
		 wrg = "wrong answer";
	}
	
	public boolean runTest() throws Exception {
		TestUtils.assertTrue(gh.compareSecurityAnswer(u, ans));
		TestUtils.assertFalse(gh.compareSecurityAnswer(u, wrg));
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    	 u = null;
    }
}
