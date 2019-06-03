package com.byteme.gdgapp;

import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import classes.User;
import com.byteme.gdgapp.PostDeed;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;

import classes.Achievement;
import classes.AchievementCategory;
import classes.Deed;

import classes.GUIHandler;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import classes.User;
import com.byteme.gdgapp.PostDeed;

import java.sql.Connection;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import classes.Deed;
import classes.DeedCate;
import classes.DeedCategory;
import classes.GUIHandler;
/**
 * Class to show flatmates deeds
 * @author Chinmay
 *
 */

public class FlatmatesD extends com.codename1.ui.Form {
	private Resources theme;
	private Connection conn = null;
	private String url;
	private Achievement ach=new Achievement();
	private AchievementCategory achcat=new AchievementCategory();
	private PostDeed pdeed;
	private DeedCategory dcat=new DeedCategory();
	private GUIHandler guiHandler;
	private User user = new User();
	private User owner = new User();
	private Deed did = new Deed();
	Picker pfla=new Picker();
	Picker pfla2=new Picker();
	Picker pfla3=new Picker();
	Label info=new Label();
	Accordion afla=new Accordion();
	Accordion afla2=new Accordion();
	Accordion afla3=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	Label lach=new Label();
	Label achic=new Label();
	//Button pet2=new Button("Navigate to Pets Category 2");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 1)));
	
	public FlatmatesD() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public FlatmatesD(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public FlatmatesD(User user) {
		this(com.codename1.ui.util.Resources.getGlobalResources());

		this.guiHandler = new GUIHandler(theme);
		this.user = user;

		// createForm();
	}
	public void createForm() {

		Toolbar tb = this.getToolbar();
		guiHandler.menu(tb, this.user);

		// default code from CN1
		TableLayout tl;
		int spanButton = 2;
		if (Display.getInstance().isTablet()) {
			tl = new TableLayout(14, 1);
		} else {
			tl = new TableLayout(14, 1);
			spanButton = 1;
		}
		tl.setGrowHorizontally(true);
		this.setLayout(tl);
		
		
		afla.setAutoClose(true);
		afla.setScrollableY(false);
		comdeeds.setAutoClose(true);
		comdeeds.setScrollableY(false);
		aach.setAutoClose(true);
		aach.setScrollableY(false);
		
		pfla.setType(Display.PICKER_TYPE_STRINGS);
		pfla.setStrings(
				"Helping a flatmate clean a room.",
				"Helping a flatmate with the dishes.",
				"Lend flatmate something.",
				"Make an extra portion of food for them",
				"Sharing stuffs with a flatmate."
				);
		pfla.setSelectedString("Click Here to Select a Deed");
		afla.addContent("Helping Hand", pfla);
		
		pfla2.setType(Display.PICKER_TYPE_STRINGS);
		pfla2.setStrings(
				"Getting all the mails and parcels in the letter box.", 
				"Giving a flatmate a free ride. ",
				"Giving a flatmate emotional support when down.",
				"Helping a flatmate carry the groceries.",
				"Invite a flatmate to a party."
				);
		pfla2.setSelectedString("Click Here to Select a Deed");
		afla2.addContent("Best Buddy", pfla2);
		
		pfla3.setType(Display.PICKER_TYPE_STRINGS);
		pfla3.setStrings(
				"Accompanying a flatmate when flatmate needs someone." ,
				"Helping a flatmate with household chores.",
				"Helping flatmate with schoolworks." ,
				"Referencing a flatmate to get a job.",
				"Sharing goodnews to a flatmate."
				);
		pfla3.setSelectedString("Click Here to Select a Deed");
		afla3.addContent("Ranger", pfla3);
		
		
		
		
		
		Image imgTitle = theme.getImage("ach_11.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		Image imgTitle2 = theme.getImage("ach_12.png"); // white background blue logo
		imgTitle2 = imgTitle2.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		Image imgTitle3 = theme.getImage("ach_13.png"); // white background blue logo
		imgTitle3 = imgTitle3.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement 1 - Doormat Key."), new Label(imgTitle),new Label("Achievement 2 - Planter Key."), new Label(imgTitle2),new Label("Achievement 3 - Locker Key."), new Label(imgTitle3)));
		//lach.setText("Complete all of above to get this.");
		//aach.addContent("Achievement",lach);
		//Font f = FontImage.getMaterialDesignFont();
		//FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
		//info.setText("This Page has Multiple Deeds & Achievements.");
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		Container cntDeeds2 = new Container();
		Container cntDeeds3 = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds =  guiHandler.getFlatmatesDDeeds(owner, user, false);
			cntDeeds2 = guiHandler.getFlatmatesPDeeds(owner, user, false);
			cntDeeds3 = guiHandler.getFlatmatesLDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds =guiHandler.getFlatmatesDDeeds(user, null, true);
	    	cntDeeds2=guiHandler.getFlatmatesPDeeds(user, null, true);
			cntDeeds3=guiHandler.getFlatmatesLDeeds(user, null, true);
	    }
		//comdeeds.addContent("Completed Deeds", cntDeeds);
		comdeeds.addContent("Completed Deeds",BoxLayout.encloseY(new Label("Helping Hand Deeds"),cntDeeds,new Label("Best Buddy Deeds"),cntDeeds2,new Label("Ranger Deeds"),cntDeeds3));
		//achic.setIcon(fi);
		
		
		pdeed = new PostDeed(user,did,ach,achcat,dcat);
		
		pfla.addActionListener(e -> {
			if (pfla.getValue() != "Click Here to Select a Deed") 
			{
				FlatmatesDpointSystem();
				pdeed.setError((String) pfla.getValue());
				pdeed.createForm();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		pfla2.addActionListener(e -> {
			if (pfla2.getValue() != "Click Here to Select a Deed") 
			{
				FlatmatesPpointSystem();
				pdeed.setError((String) pfla2.getValue());
				pdeed.createForm();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		pfla3.addActionListener(e -> {
			if (pfla3.getValue() != "Click Here to Select a Deed") 
			{
				FlatmatesLpointSystem();
				pdeed.setError((String) pfla3.getValue());
				pdeed.createForm();
				
				//pflaa();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		
		
		
		
		/*sub.addActionListener(e -> {
			
			
			if (pfla.getValue() != "Click Here to Select a Deed") 
			{
				pdeed.setError((String) pfla.getValue());
				
				pdeed.createForm();
				
			
			}
			else if(pfla2.getValue() != "Click Here to Select a Deed")
			{
				pdeed.setError((String) pfla2.getValue());
				
				pdeed.createForm();
				
			}
			else if(pfla3.getValue() != "Click Here to Select a Deed")
			{
				
				
				
			}
			else
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			}
			});
		
		
		
	
			
		
				
		
		
		res.addActionListener(e -> {
			pfla.setSelectedString("Click Here to Select a Deed");
			pfla2.setSelectedString("Click Here to Select a Deed");
			pfla3.setSelectedString("Click Here to Select a Deed");
			
			
		});*/
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		
		//pet2.addActionListener(e -> {
			//new PetC(user).createForm();
		//});
		//FlatP.addActionListener(e -> {
			//new FlatmatesL(user).createForm();
		//});
		
		c.add(back);
		//c.add(sub);
		//c.add(res);
		this.add(afla).add(afla2).add(afla3).add(aach).add(achic).add(comdeeds).add(c);
		this.show();
}
	
	public void FlatmatesDpointSystem() 
	{

		int a = pfla.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(21);
		}
		else if(a==1)
		{
			dcat.setcategories(22);
		}
		else if(a==2)
		{
			dcat.setcategories(23);
		}
		else if(a==3)
		{
			dcat.setcategories(24);
		}
		else if(a==4)
		{
			dcat.setcategories(25);
		}
		
		
		int dcatid=5;
		int achid=11;
		int achcatid =5;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {60,20,40,80,40};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	public void FlatmatesPpointSystem() 
	{

		int a = pfla2.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(26);
		}
		else if(a==1)
		{
			dcat.setcategories(27);
		}
		else if(a==2)
		{
			dcat.setcategories(28);
		}
		else if(a==3)
		{
			dcat.setcategories(29);
		}
		else if(a==4)
		{
			dcat.setcategories(30);
		}
		
		
		int dcatid=6;
		int achid=12;
		int achcatid =6;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {50,80,70,40,10};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	public void FlatmatesLpointSystem() 
	{

		int a = pfla3.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(31);
		}
		else if(a==1)
		{
			dcat.setcategories(32);
		}
		else if(a==2)
		{
			dcat.setcategories(33);
		}
		else if(a==3)
		{
			dcat.setcategories(34);
		}
		else if(a==4)
		{
			dcat.setcategories(35);
		}
		
		
		int dcatid=7;
		int achid=13;
		int achcatid =7;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {10,30,50,15,10};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
			setLayout(new com.codename1.ui.layouts.LayeredLayout());
			setInlineStylesTheme(resourceObjectInstance);
			setInlineStylesTheme(resourceObjectInstance);
			setTitle("Flatmates");
			setName("Flatmates");
		}// </editor-fold>

		// -- DON'T EDIT ABOVE THIS LINE!!!
}
