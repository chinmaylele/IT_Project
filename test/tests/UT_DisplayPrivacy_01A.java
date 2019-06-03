package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;

/**
 * GUIHandler.DisplayPrivacyTest
 */
public class UT_DisplayPrivacy_01A extends AbstractTest {
	
	GUIHandler gh;
	
	boolean boolF, boolT;
	String privacyF, privacyT;
	
	public void prepare(){
		gh = new GUIHandler();
		boolF = false;
		boolT = true;
	}
	
	public boolean runTest() throws Exception {
		privacyF = gh.displayPrivacy(boolF);
		privacyT = gh.displayPrivacy(boolT);
		
		TestUtils.assertEqual("Public", privacyF);
		TestUtils.assertEqual("Private", privacyT);
		return true;
    }
    
    public void cleanup() {
    	gh = null;
    }
}
