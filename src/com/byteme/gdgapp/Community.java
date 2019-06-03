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
 * Class to show community deeds
 * @author Chinmay
 *
 */
public class Community extends com.codename1.ui.Form {
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
	Picker pcom=new Picker();
	Accordion acom=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	Label lach=new Label();
	Label achic=new Label();
	Button sub = new Button("Submit");
	Button res = new Button("Reset");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 3)));
	
	
	public Community() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public Community(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public Community(User user) {
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
		
		
		
		acom.setAutoClose(true);
		acom.setScrollableY(false);
		comdeeds.setAutoClose(true);
		comdeeds.setScrollableY(false);
		aach.setAutoClose(true);
		aach.setScrollableY(false);
		
		pcom.setType(Display.PICKER_TYPE_STRINGS);
		pcom.setStrings(
				"Community Service",
				"Fund raising",
				"Help promote a community event",
				"Joining a campaign",
				"Joining a community event."
				);
		pcom.setSelectedString("Click Here to Select a Deed");
		acom.addContent("Community Deeds", pcom);
		Image imgTitle = theme.getImage("ach_17.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement - Famous."), new Label(imgTitle)));
		//Font f = FontImage.getMaterialDesignFont();
		//FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
		
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getCommunityDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getCommunityDeeds(user, null, true);
	    }
		comdeeds.addContent("Completed Deeds", cntDeeds);
		//achic.setIcon(fi);
		
sub.addActionListener(e -> {
			
			pdeed = new PostDeed(user,did,ach,achcat,dcat);
			//String r=pcle.getText();
			
			if (pcom.getSelectedString() == "Click Here to Select a Deed") 
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			
			}
			else
			{
				CommunitypointSystem();
				pdeed.setError((String) pcom.getValue());
				pdeed.createForm();
			}
			});
		
		res.addActionListener(e -> {
			pcom.setSelectedString("Click Here to Select a Deed");
			
			
		});
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		
		
		c.add(back);
		c.add(sub);
		c.add(res);
		this.add(acom).add(aach).add(achic).add(comdeeds).add(c);
		this.show();
}
	public void CommunitypointSystem() 
	{

		int a = pcom.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(6);
		}
		else if(a==1)
		{
			dcat.setcategories(7);
		}
		else if(a==2)
		{
			dcat.setcategories(8);
		}
		else if(a==3)
		{
			dcat.setcategories(9);
		}
		else if(a==4)
		{
			dcat.setcategories(10);
		}
		
		int dcatid=2;
		int achid = 17;
		int achcatid = 2;		 
		int[] pointsArray = new int[] {200,100,80,90,100};
		
		
		
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
		did.setAssignedWeighting(pointsArray[a]);
	}
	
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
			setLayout(new com.codename1.ui.layouts.LayeredLayout());
			setInlineStylesTheme(resourceObjectInstance);
			setInlineStylesTheme(resourceObjectInstance);
			setTitle("Community");
			setName("Community");
		}// </editor-fold>

		// -- DON'T EDIT ABOVE THIS LINE!!!
}
