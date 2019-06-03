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

import classes.GUIHandler;
import classes.User;

/**
 * public class Registration extends com.codename1.ui.Form
 * This is the first form in the registration process, asking user to enter their details and choose a username.
 * 
 * @author kurtg
 * @Created 23/01/2019
 */
public class CreateGroup extends com.codename1.ui.Form {

	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public CreateGroup() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public CreateGroup(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public CreateGroup(User user) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        
        createForm();
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
        TextField groupName = new TextField("", "enter group name", 20, TextArea.ANY);
       
        Picker groupTypePicker = new Picker();
        groupTypePicker.setType(Display.PICKER_TYPE_STRINGS);
		// changed strings to personalise for this app
        groupTypePicker.setStrings(
				"Family", 
				"Workplace",
				"University",
				"School",
				"Unwaged",
				"Associates");
        groupTypePicker.setSelectedString("please select option");

        // physically cannot enter more than 25 character into the text field
        groupName.setMaxSize(25);

        
        Button btnCreate = new Button("Create");
        Button btnCancel = new Button("Cancel");
        setSameWidth(btnCreate, btnCancel);
        
        // sets constraints to apply to both buttons
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        
        // INFORMATION BUTTONS ----------------------------------------------------------------------------
        // Changing infoGroupName UIID allows you to create a theme for it, and applies to all components with the same UIID
        Button infoGroupName = new Button("?","ButtonForInfo");
        infoGroupName.addActionListener(e -> {
        	// Popup message that appears at the bottom of screen for a small amount of time. 
        	// Could maybe change the default time, haven't looked into it.
        	ToastBar.showMessage("Enter your group name into this field.\nMax of 25 characters.", FontImage.MATERIAL_INFO);
        });
        Button infoGroupType = new Button("?","ButtonForInfo");
        infoGroupType.addActionListener(e -> {
        	ToastBar.showMessage("Select the type of group this will be", FontImage.MATERIAL_INFO);
        });
        
        // Adding all labels and input text fields to a container for good formatting
        Container c = TableLayout.encloseIn(3, new Label(" "), new Label("Group Name"), new Label(""),
							        		   new Label(" "), groupName, infoGroupName,
							        	   	   new Label(" "), new Label("Group Type"), new Label(""),
							        		   new Label(" "), groupTypePicker, infoGroupType); // inserts blank line  
        
        // BUTTON EVENTS --------------------------------------------------------------------------------------------
        btnCancel.addActionListener(e ->{
        	infoGroupName.remove();
        	infoGroupType.remove();
        	new Home(user).show();
    	});
        
        btnCreate.addActionListener(e ->{
        	/**
        	 * Proposed - Unsure how to retrieve groupID of the group just created. We navigate to Home instead, as this
        	 * refreshes the list.
        	 */
        	if(guiHandler.validInput(groupName.getText()) == false || 
        			guiHandler.validInput(groupTypePicker.getText()) == false) {
        		Dialog.show(null, 
	        			"Please ensure all fields are entered.", 
	        			"OK", null);
        		
        	} else {
        		this.user = guiHandler.createGroup(groupName.getText(), groupTypePicker.getText(), user);
        		infoGroupName.remove();
        		infoGroupType.remove();
        		Dialog.show(null, 
	        			"Your group has been created. \nReturning to homepage", 
	        			"OK", null);
        		
            	new Home(user).show();
        	}
       	
    	});
        
        // following adds all information to the form
        this.add(c);
        this.add(cn, btnCreate).
        add(cn, btnCancel);
        
        this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Create Group");
        setName("Create Group");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}