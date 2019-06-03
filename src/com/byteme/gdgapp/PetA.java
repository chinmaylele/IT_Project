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
 * Class to show pet deeds
 * @author Chinmay
 *
 */
public class PetA extends com.codename1.ui.Form {
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
	Picker ppet=new Picker();
	Picker ppet2=new Picker();
	Picker ppet3=new Picker();
	Label info=new Label();
	Accordion apet=new Accordion();
	Accordion apet2=new Accordion();
	Accordion apet3=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	Label lach=new Label();
	Label achic=new Label();
	//Button pet2=new Button("Navigate to Pets Category 2");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 1)));
	
	public PetA() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public PetA(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public PetA(User user) {
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
		
		
		apet.setAutoClose(true);
		apet.setScrollableY(false);
		comdeeds.setAutoClose(true);
		comdeeds.setScrollableY(false);
		aach.setAutoClose(true);
		aach.setScrollableY(false);
		
		ppet.setType(Display.PICKER_TYPE_STRINGS);
		ppet.setStrings(
				"Giving water to drink for an animal.",
				"Picking up pet's poo.",
				"Putting a bird feeder. ",
				"Taking a pet for a walk.",
				"Taking a pet to the vet."
				);
		ppet.setSelectedString("Click Here to Select a Deed");
		apet.addContent("Animal Welfare", ppet);
		
		ppet2.setType(Display.PICKER_TYPE_STRINGS);
		ppet2.setStrings(
				"Pet Sitting",
				"Reporting a stray animal.",
				"Taking care of someone else's pet in the mean time. ",
				"Training a pet",
				"Visiting a local animal shelter."
				);
		ppet2.setSelectedString("Click Here to Select a Deed");
		apet2.addContent("Critter Lover", ppet2);
		
		ppet3.setType(Display.PICKER_TYPE_STRINGS);
		ppet3.setStrings(
				"Foster a rescue",
				"Give a rescue a home",
				"Giving way to a person with a pet. ",
				"Join local pet companion program",
				"Rescue an animal"
				);
		ppet3.setSelectedString("Click Here to Select a Deed");
		apet3.addContent("Tarzan", ppet3);
		
		
		
		
		
		Image imgTitle = theme.getImage("ach_6.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		Image imgTitle2 = theme.getImage("ach_4.png"); // white background blue logo
		imgTitle2 = imgTitle2.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		Image imgTitle3 = theme.getImage("ach_7.png"); // white background blue logo
		imgTitle3 = imgTitle3.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement 1 - Animal Welfare Warrior."), new Label(imgTitle),new Label("Achievement 2 - Critter Lover."), new Label(imgTitle2),new Label("Achievement 3 - Tarzan."), new Label(imgTitle3)));
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
			cntDeeds =  guiHandler.getPetADeeds(owner, user, false);
			cntDeeds2 = guiHandler.getPetCDeeds(owner, user, false);
			cntDeeds3 = guiHandler.getPetTDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds =guiHandler.getPetADeeds(user, null, true);
	    	cntDeeds2=guiHandler.getPetCDeeds(user, null, true);
			cntDeeds3=guiHandler.getPetTDeeds(user, null, true);
	    }
		//comdeeds.addContent("Completed Deeds", cntDeeds);
		comdeeds.addContent("Completed Deeds",BoxLayout.encloseY(new Label("Animal Welfare Deeds"),cntDeeds,new Label("Critter Deeds"),cntDeeds2,new Label("Tarzan Deeds"),cntDeeds3));
		//achic.setIcon(fi);
		
		
		pdeed = new PostDeed(user,did,ach,achcat,dcat);
		
		ppet.addActionListener(e -> {
			if (ppet.getValue() != "Click Here to Select a Deed") 
			{
				PetApointSystem();
				pdeed.setError((String) ppet.getValue());
				pdeed.createForm();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		ppet2.addActionListener(e -> {
			if (ppet2.getValue() != "Click Here to Select a Deed") 
			{
				PetCpointSystem();
				pdeed.setError((String) ppet2.getValue());
				pdeed.createForm();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		ppet3.addActionListener(e -> {
			if (ppet3.getValue() != "Click Here to Select a Deed") 
			{
				PetTpointSystem();
				pdeed.setError((String) ppet3.getValue());
				pdeed.createForm();
				
				//ppeta();
				//Dialog.show(null," 1 Deed at a time.","OK",null);
			}
		});
		
		
		
		
		
		/*sub.addActionListener(e -> {
			
			
			if (ppet.getValue() != "Click Here to Select a Deed") 
			{
				pdeed.setError((String) ppet.getValue());
				
				pdeed.createForm();
				
			
			}
			else if(ppet2.getValue() != "Click Here to Select a Deed")
			{
				pdeed.setError((String) ppet2.getValue());
				
				pdeed.createForm();
				
			}
			else if(ppet3.getValue() != "Click Here to Select a Deed")
			{
				
				
				
			}
			else
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			}
			});
		
		
		
	
			
		
				
		
		
		res.addActionListener(e -> {
			ppet.setSelectedString("Click Here to Select a Deed");
			ppet2.setSelectedString("Click Here to Select a Deed");
			ppet3.setSelectedString("Click Here to Select a Deed");
			
			
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
		this.add(apet).add(apet2).add(apet3).add(aach).add(achic).add(comdeeds).add(c);
		this.show();
}
	
	public void PetApointSystem() 
	{

		int a = ppet.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(51);
		}
		else if(a==1)
		{
			dcat.setcategories(52);
		}
		else if(a==2)
		{
			dcat.setcategories(53);
		}
		else if(a==3)
		{
			dcat.setcategories(54);
		}
		else if(a==4)
		{
			dcat.setcategories(55);
		}
		
		
		int dcatid=11;
		int achid=6;
		int achcatid =11;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {10,10,20,10,20};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	public void PetCpointSystem() 
	{

		int a = ppet2.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(56);
		}
		else if(a==1)
		{
			dcat.setcategories(57);
		}
		else if(a==2)
		{
			dcat.setcategories(58);
		}
		else if(a==3)
		{
			dcat.setcategories(59);
		}
		else if(a==4)
		{
			dcat.setcategories(60);
		}
		
		
		int dcatid=12;
		int achid=4;
		int achcatid =12;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {80,50,50,50,70};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	public void PetTpointSystem() 
	{

		int a = ppet3.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(61);
		}
		else if(a==1)
		{
			dcat.setcategories(62);
		}
		else if(a==2)
		{
			dcat.setcategories(63);
		}
		else if(a==3)
		{
			dcat.setcategories(64);
		}
		else if(a==4)
		{
			dcat.setcategories(65);
		}
		
		
		int dcatid=13;
		int achid=7;
		int achcatid =13;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {150,200,5,70,90};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
			setLayout(new com.codename1.ui.layouts.LayeredLayout());
			setInlineStylesTheme(resourceObjectInstance);
			setInlineStylesTheme(resourceObjectInstance);
			setTitle("Pet");
			setName("Pet");
		}// </editor-fold>

		// -- DON'T EDIT ABOVE THIS LINE!!!
}
