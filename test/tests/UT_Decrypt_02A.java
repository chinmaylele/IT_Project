package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;
import com.codename1.util.Base64;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.DecryptTest
 */
public class UT_Decrypt_02A extends AbstractTest {
	
	GUIHandler gh;
	String str, strR;
	byte[] byteArr;
	
	public void prepare(){
		 gh = new GUIHandler();
		 str = "end5bHVq"; // "string" encrypted
	}
	
	public boolean runTest() throws Exception {
		strR = gh.decrypt(str);
		
		TestUtils.assertEqual("string", strR);
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
