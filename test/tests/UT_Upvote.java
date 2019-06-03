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
import classes.DBHandler;

public class UT_Upvote extends AbstractTest {


Deed deed;
DBHandler db;


public void prepare(){


deed=new Deed();
db=new DBHandler();
deed.setPoints(2);


//deed.setAssignedWeighting(50);
}
public boolean runTest() throws Exception 
{
	
	TestUtils.assertEqual(2,deed.getPoints());

return true;
}

public void cleanup() {
	 deed=null;
	 db=null;
}
}