package com.byteme.gdgapp;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.Achievement;
import classes.AchievementCategory;
import classes.Deed;
import classes.DeedCate;
import classes.DeedCategory;
import classes.GUIHandler;
import classes.User;

/**
 * public class PostDeed extends com.codename1.ui.Form
 * This is the class which posts the deed user selected.
 * 
 * @author Chinmay
 * 
 */
public class PostDeed extends com.codename1.ui.Form {

	private Resources theme;
	private Achievement ach=new Achievement();
	private AchievementCategory achcat=new AchievementCategory();
	private GUIHandler guiHandler;
	private User user;
	private DeedCategory dcat=new DeedCategory();
	private Home h=new Home();
	Button type=new Button("Select Deed Type");
    Deed deed;
	
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public PostDeed() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public PostDeed(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public PostDeed(User user,  Deed did, Achievement ach, AchievementCategory achcat, DeedCategory dcat) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());
        this.deed = did;
    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        this.ach = ach;
        this.achcat= achcat;
        this.dcat =dcat;
        
        //createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    /**
     * The method that contains all the GUI information
     */
    public void createForm() { 

    	Toolbar tb = this.getToolbar();
    	guiHandler.menu(tb, this.user);
    	
    	// default code from CN1
    	TableLayout tl;
        int spanButton = 2;
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(14, 1);
        } else {
            tl = new TableLayout(14, 1);
            spanButton = 1;
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);
        

        // input fields
        Label l=new Label("Please Select Deed Type First");
        
		
		Button s=new Button("Share");
		Button c=new Button("Cancel");
        this.add(l).
        add(type).
        add(s).
        add(c);
        this.show();
        
        c.addActionListener(e -> 
		{
			new Home(user).show();
		});
        
        type.addActionListener(e -> 
		{

			new DeedCate(user).createForm();
		});
        
        
        
        s.addActionListener(e -> 
		{
			if(type.getText().equals("Select Deed Type"))
			{
				Dialog.show(null,"Please Select a Deed Type","OK",null);
			}
			else
			{
				user=guiHandler.uploadDeedwcat(type.getText(),user,deed,dcat,ach,achcat);
				//System.out.println("points " + deed.getPoints());
				//System.out.println(dcat.getcategories());
				Dialog.show(null,"Successfully Uploaded","OK",null);
				type.setText("Select Deed Type");
			}
		});
    }
    
    public void setError(String txt) 
	 {
	     type.setText(txt);
	}
 // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Post Deed");
        setName("Post Deed");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}