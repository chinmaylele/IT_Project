package com.byteme.gdgapp;

import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import classes.User;
import com.byteme.gdgapp.PostDeed;

import java.io.IOException;
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
 * Class to show volunteer deeds
 * @author Chinmay
 *
 */

public class Volunteer extends com.codename1.ui.Form {
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
	Picker pvol=new Picker();
	Accordion avol=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	
	Label lach=new Label();
	Label achic=new Label();
	Button sub = new Button("Submit");
	Button res = new Button("Reset");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 3)));
	Container main = new Container();
	
	public Volunteer() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public Volunteer(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public Volunteer(User user) {
		this(com.codename1.ui.util.Resources.getGlobalResources());

		this.guiHandler = new GUIHandler(theme);
		this.user = user;

		// createForm();
	}
	public void createForm(){

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
		
		pvol.setType(Display.PICKER_TYPE_STRINGS);
		pvol.setStrings(
				"Joining a feeding program.",
				"Participating an event for a good cause. ",
				"Supporting an NGO.",
				"Volunteer at a public library",
				"Volunteer to do an easy task"
				);
		pvol.setSelectedString("Click Here to Select a Deed");
		avol.addContent("Volunteer Deeds", pvol);
		lach.setText("Complete all of above to get this.");
		Image imgTitle = theme.getImage("ach_16.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		//Label lblTitle = new Label(imgTitle);
		//achic.setIcon(Image.createImage("D:\\BACKUP\\images\\achievements\\achievements\\ach1.png"));
		//aach.addContent("Achievement",lach);
		//aach.addComponent(lach,lblTitle);
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement - Celebrated."), new Label(imgTitle)));
		//aach.addComponent(achic);
		//Font f = FontImage.getMaterialDesignFont();
		//FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
		
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getVolDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getVolDeeds(user, null, true);
	    }
		comdeeds.addContent("Completed Deeds", cntDeeds);
		//achic.setIcon(fi);
		
		avol.setAutoClose(true);
		avol.setScrollableY(false);
		comdeeds.setAutoClose(true);
		comdeeds.setScrollableY(false);
		aach.setAutoClose(true);
		aach.setScrollableY(false);
		
sub.addActionListener(e -> {
			
			pdeed = new PostDeed(user,did,ach,achcat,dcat);
			//String r=pcle.getText();
			
			if (pvol.getSelectedString() == "Click Here to Select a Deed") 
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			
			}
			else
			{
				VolpointSystem();
				pdeed.setError((String) pvol.getValue());
				pdeed.createForm();
			}
			});
		
		res.addActionListener(e -> {
			pvol.setSelectedString("Click Here to Select a Deed");
			
			
		});
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		
		c.add(back);
		c.add(sub);
		c.add(res);
		//this.add(TableLayout.encloseIn(1, true,avol).add(aach).add(comdeeds).add(c));
		//main.add(TableLayout.encloseIn(1,avol,aach,comdeeds,c));
		//this.add(main).add(c);
		//this.add(main);
		this.add(avol).add(aach).add(comdeeds).add(c);
		
		this.show();
}
	public void VolpointSystem() 
	{

		int a = pvol.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(71);
		}
		else if(a==1)
		{
			dcat.setcategories(72);
		}
		else if(a==2)
		{
			dcat.setcategories(73);
		}
		else if(a==3)
		{
			dcat.setcategories(74);
		}
		else if(a==4)
		{
			dcat.setcategories(75);
		}
		
		
		int dcatid=15;
		int achid =16;
		int achcatid =15;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {90,70,60,80,10};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	//-- DON'T EDIT BELOW THIS LINE!!!


	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
	        setLayout(new com.codename1.ui.layouts.LayeredLayout());
	        setInlineStylesTheme(resourceObjectInstance);
	                setInlineStylesTheme(resourceObjectInstance);
	        setTitle("Volunteer");
	        setName("Volunteer");
	    }// </editor-fold>

	//-- DON'T EDIT ABOVE THIS LINE!!!
}
