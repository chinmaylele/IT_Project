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
public class UT_PostDeed extends AbstractTest 


{

GUIHandler gh;
User u;
Deed deed;
DeedCategory dcat;
Achievement ach;
AchievementCategory achcat;
String deedcatname;
//int dcatid;



public void prepare(){
gh = new GUIHandler();
u = new User();
ach=new Achievement();
achcat=new AchievementCategory();
dcat=new DeedCategory();
deed=new Deed();



dcat.setdcatid(1);
u.setUserID(15);
dcat.setcategories(2);
achcat.setAchieveCatID(1);
ach.setAchievementID(2);
deed.setAssignedWeighting(10);
dcat.setDeedCatName("abc");
}
public boolean runTest() throws Exception {
		
		TestUtils.assertEqual(1,dcat.getdcatid());
		TestUtils.assertEqual(15,u.getUserID());
		TestUtils.assertEqual(2,dcat.getcategories());
		TestUtils.assertEqual(1,achcat.getAchieveCatID());
		TestUtils.assertEqual(2,ach.getAchievementID());
		TestUtils.assertEqual(10,deed.getAssignedWeighting());
		TestUtils.assertEqual("abc",dcat.getDeedCatName());
	return true;
	}

public void cleanup() {
	 gh = null;
	 u = null;
	 deed=null;
	 dcat=null;
	 ach=null;
	 achcat=null;
	 
	}
}