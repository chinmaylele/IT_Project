package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;
import com.codename1.util.Base64;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.CreateUserTest
 */
public class UT_CreateUser_02A extends AbstractTest {
	
	GUIHandler gh;
	User u;
	String realName, username, email, password, encryptedPW, dob;
	
	public void prepare(){
		 gh = new GUIHandler();
		 u = new User();
		 
		 realName = "realName"; 
		 username = "username"; 
		 email = "email";
		 password = "password";
		 dob = "01/01/2000";
	}
	
	public boolean runTest() throws Exception {
		u = gh.createUser(realName, username, email, password, dob);

		TestUtils.assertEqual("realName", u.getRealName());
		TestUtils.assertEqual("username", u.getUsername());
		TestUtils.assertEqual("email", u.getEmail());
		TestUtils.assertEqual("d2R6dn5yeWc=", u.getPassword());
		TestUtils.assertEqual("01/01/2000", u.getDOB());
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    	 u = null;
    }
}
