package tests;

import com.codename1.testing.AbstractTest;
import com.codename1.testing.TestUtils;
import com.codename1.ui.Display;

import classes.GUIHandler;
import classes.User;
import classes.Deed;
import classes.Achievement;
import classes.DeedCategory;
import classes.AchievementCategory;

public class UT_PointSystem extends AbstractTest {


Deed deed;




public void prepare(){
	

deed=new Deed();

deed.setAssignedWeighting(50);
}
public boolean runTest() throws Exception {
	
	TestUtils.assertEqual(50,deed.getAssignedWeighting());

return true;
}

public void cleanup() {
	 deed=null;
}
}