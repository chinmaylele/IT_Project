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
 * Class to show recycling deeds
 * @author Chinmay
 *
 */

public class Recycling extends com.codename1.ui.Form {
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
	Picker prec=new Picker();
	Accordion arec=new Accordion();
	Accordion aach=new Accordion();
	Accordion comdeeds=new Accordion();
	
	Label lach=new Label();
	Label achic=new Label();
	Button sub = new Button("Submit");
	Button res = new Button("Reset");
	Button back=new Button("Back");
	Container c = new Container((new GridLayout(1, 3)));
	Container main = new Container();
	
	public Recycling() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public Recycling(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public Recycling(User user) {
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
		
		prec.setType(Display.PICKER_TYPE_STRINGS);
		prec.setStrings(
				"Buying/ using items with reusable packaging." ,
				"Buying/using recyclable batteries.",
				"Printing on both sides.",
				"Recycling old electronic devices.",
				"Using reusable bags when buying groceries."
				);
		prec.setSelectedString("Click Here to Select a Deed");
		arec.addContent("Recycling Deeds", prec);
		lach.setText("Complete all of above to get this.");
		Image imgTitle = theme.getImage("ach_3.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		//Label lblTitle = new Label(imgTitle);
		//achic.setIcon(Image.createImage("D:\\BACKUP\\images\\achievements\\achievements\\ach1.png"));
		//aach.addContent("Achievement",lach);
		//aach.addComponent(lach,lblTitle);
		aach.addContent("Achievements", BoxLayout.encloseY(new Label("Complete all of above to Unlock This."),new Label("Achievement - Green Hands."), new Label(imgTitle)));
		//aach.addComponent(achic);
		//Font f = FontImage.getMaterialDesignFont();
		//FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
		
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getRecyclingDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getRecyclingDeeds(user, null, true);
	    }
		comdeeds.addContent("Completed Deeds", cntDeeds);
		//achic.setIcon(fi);
		
		arec.setAutoClose(true);
		arec.setScrollableY(false);
		comdeeds.setAutoClose(true);
		comdeeds.setScrollableY(false);
		aach.setAutoClose(true);
		aach.setScrollableY(false);
		
sub.addActionListener(e -> {
			
			pdeed = new PostDeed(user,did,ach,achcat,dcat);
			//String r=pcle.getText();
			
			if (prec.getSelectedString() == "Click Here to Select a Deed") 
			{
				Dialog.show(null,"Please Select at least 1 Deed.","OK",null);
			
			}
			else
			{
				RecyclingpointSystem();
				pdeed.setError((String) prec.getValue());
				pdeed.createForm();
			}
			});
		
		res.addActionListener(e -> {
			prec.setSelectedString("Click Here to Select a Deed");
			
			
		});
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		
		c.add(back);
		c.add(sub);
		c.add(res);
		//this.add(TableLayout.encloseIn(1, true,arec).add(aach).add(comdeeds).add(c));
		//main.add(TableLayout.encloseIn(1,arec,aach,comdeeds,c));
		//this.add(main).add(c);
		//this.add(main);
		this.add(arec).add(aach).add(comdeeds).add(c);
		
		this.show();
}
	public void RecyclingpointSystem() 
	{

		int a = prec.getSelectedStringIndex();
		if(a==0)
		{
			dcat.setcategories(66);
		}
		else if(a==1)
		{
			dcat.setcategories(67);
		}
		else if(a==2)
		{
			dcat.setcategories(68);
		}
		else if(a==3)
		{
			dcat.setcategories(69);
		}
		else if(a==4)
		{
			dcat.setcategories(70);
		}
		
		
		int dcatid=14;
		int achid =3;
		int achcatid =14;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		ach.setAchievementID(achid);
		achcat.setAchieveCatID(achcatid);
		dcat.setdcatid(dcatid);
		
			 
		int[] pointsArray = new int[] {25,20,5,15,20};
		
		
		
		
		did.setAssignedWeighting(pointsArray[a]);
		
		
	}
	
	//-- DON'T EDIT BELOW THIS LINE!!!


	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
	        setLayout(new com.codename1.ui.layouts.LayeredLayout());
	        setInlineStylesTheme(resourceObjectInstance);
	                setInlineStylesTheme(resourceObjectInstance);
	        setTitle("Recycling");
	        setName("Recycling");
	    }// </editor-fold>

	//-- DON'T EDIT ABOVE THIS LINE!!!
}
