package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;
import com.codename1.util.Base64;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.EncryptTest
 */
public class UT_EncryptTest_02A extends AbstractTest {
	
	GUIHandler gh;
	String str, strR;
	byte[] byteArr;
	
	public void prepare(){
		 gh = new GUIHandler();
		 str = "string";
	}
	
	public boolean runTest() throws Exception {
		strR = gh.encrypt(str);
		
		TestUtils.assertEqual("end5bHVq", strR);
		return true;
    }
    
    public void cleanup() {
    	 gh = null;
    }
}
