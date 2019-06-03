package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * User.DisposeUserTest
 */
public class UT_UserClass_02A extends AbstractTest {
	
	GUIHandler gh;
	User u;
	
	public void prepare(){
		gh = new GUIHandler();
		u = new User();
		u.setUserID(1);
		u.setUsername("username");
		u.setRealName("real name");
		u.setEmail("email@email.com");
		u.setPassword("pw");
		u.setDOB("");
		u.setAccType(true);
		u.setKudosTotal(230);
		u.setSecurityQuestion("question");
		u.setSecurityAnswer("answer");
		u.setPrivacy(false);
	}
	
	public boolean runTest() throws Exception {
		u.disposeUser();
		
		TestUtils.assertEqual(0, u.getUserID());
		TestUtils.assertNull(u.getUsername());
		TestUtils.assertNull(u.getRealName());
		TestUtils.assertNull(u.getEmail());
		TestUtils.assertNull(u.getPassword());
		TestUtils.assertFalse(u.isAccType());
		TestUtils.assertEqual(0, u.getKudosTotal());
		TestUtils.assertNull(u.getDOB());
		TestUtils.assertNull(u.getEmail());
		TestUtils.assertNull(u.getEmail());
		TestUtils.assertTrue(u.isPrivacy());
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    	 u = null;
    }
}
