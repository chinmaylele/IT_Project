package com.byteme.gdgapp;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Toolbar;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.Administrator;
import classes.GUIHandler;
import classes.User;

/**
 * Placeholder form for now. No content yet.
 * @author leish
 *
 */
public class AdminSettings extends com.codename1.ui.Form {

	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	private Administrator admin = new Administrator();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public AdminSettings() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public AdminSettings(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public AdminSettings(User user) {
        this(com.codename1.ui.util.Resources.getGlobalResources());
        
    	this.guiHandler = new GUIHandler(theme);
    	this.user = user;
        this.admin.setAdminID(user.getUserID());
        this.admin.setAdmin(true);
        
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {
    	
    	Toolbar tb = this.getToolbar();

    	guiHandler.menu(tb, this.user);
    	
    	TableLayout tl;
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(7, 2);
        } else {
            tl = new TableLayout(14, 1);
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);

        Dialog.show(null, "This page does not yet exist.\nPlease wait while we fix this.", "OK", null);

    	this.show();
    }
    
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Administrator Settings");
        setName("AdminSettings");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}