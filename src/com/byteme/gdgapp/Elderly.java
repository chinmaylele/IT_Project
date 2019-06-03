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
 * Class to show elderly deeds
 * @author Chinmay
 *
 */

public class Elderly extends com.codename1.ui.Form {
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
	Picker peld=new Picker();
	Accordion aeld=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	Label lach=new Label();
	Label achic=new Label();
	Button sub = new Button("Submit");
	Button res = new Button("Reset");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 3)));
	
	public Elderly() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public Elderly(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public Elderly(User user) {
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
		
		peld.setType(Display.PICKER_TYPE_STRINGS);
		peld.setStrings(
				"Assisting a person while crossing the road.",
				"Giving a hand to an elderly getting on a vehicle.",
				"Giving priority to an elderly.",
				"Giving way to an elderly",
				"Spending time with an elderly at a home"
				);
		peld.setSelectedString("Click Here to Select a Deed");
		aeld.addContent("Elderly Deeds", peld);
		lach.setText("Complete all of above to get this.");
		//aach.addContent("Achievement",lach);
		
		Image imgTitle = theme.getImage("ach_9.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement - Walking Frame."), new Label(imgTitle)));
		//Font f = FontImage.getMaterialDesignFont();
		//FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
		
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getElderlyDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getElderlyDeeds(user, null, true);
	    }
		comdeeds.addContent("Completed Deeds", cntDeeds);
		//achic.setIcon(fi);
		
		
		
sub.addActionListener(e -> {
			
			pdeed = new PostDeed(user,did,ach,achcat,dcat);
			//String r=pcle.getText();
			
			if (peld.getSelectedString() == "Click Here to Select a Deed") 
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			
			}
			else
			{
				ElderlypointSystem();
				pdeed.setError((String) peld.getValue());
				pdeed.createForm();
			}
			});
		
		res.addActionListener(e -> {
			peld.setSelectedString("Click Here to Select a Deed");
			
			
		});
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		
		c.add(back);
		c.add(sub);
		c.add(res);
		this.add(aeld).add(aach).add(achic).add(comdeeds).add(c);
		this.show();
}
	public void ElderlypointSystem() 
	{

		int a = peld.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(16);
		}
		else if(a==1)
		{
			dcat.setcategories(17);
		}
		else if(a==2)
		{
			dcat.setcategories(18);
		}
		else if(a==3)
		{
			dcat.setcategories(19);
		}
		else if(a==4)
		{
			dcat.setcategories(20);
		}
		
		
		int dcatid=4;
		int achid =9;
		int achcatid =4;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {15,50,10,10,80};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
			setLayout(new com.codename1.ui.layouts.LayeredLayout());
			setInlineStylesTheme(resourceObjectInstance);
			setInlineStylesTheme(resourceObjectInstance);
			setTitle("Elderly");
			setName("Elderly");
		}// </editor-fold>

		// -- DON'T EDIT ABOVE THIS LINE!!!
}
