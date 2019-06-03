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
User u;
GUIHandler gh;




public void prepare(){
gh = new GUIHandler();
u = new User();
deed=new Deed();

}

public boolean runTest() throws Exception {
	deed.setAssignedWeighting(50);
TestUtils.assertEqual(50,deed.getAssignedWeighting());

return true;
}

public void cleanup() {
	 gh = null;
	 u = null;
}
}