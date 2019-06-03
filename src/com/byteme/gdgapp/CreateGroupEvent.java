package com.byteme.gdgapp;

import java.util.Date;

import com.codename1.components.ToastBar;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.Group;
import classes.User;

/**
 * public class Registration extends com.codename1.ui.Form
 * This is the first form in the registration process, asking user to enter their details and choose a username.
 * 
 * @author kurtg
 * @Created 23/01/2019
 */
public class CreateGroupEvent extends com.codename1.ui.Form {

	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	private Group group = new Group();
	
	private Date selectedDate = new Date();
	private Date currentDate = new Date();
	
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public CreateGroupEvent() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public CreateGroupEvent(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public CreateGroupEvent(User user, Group group) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        this.group = group;
        
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
        TextField eventName = new TextField("", "enter group name", 20, TextArea.ANY);
        
     // physically cannot enter more than 30 character into the text field
        eventName.setMaxSize(30);

        // Add calendar that takes a selected date and saves it as a string dd/MM/yyyy
        Calendar cldEventEnd = new Calendar();
        
        cldEventEnd.addActionListener((e) ->{
//        	selectedDate = new SimpleDateFormat("dd/MM/yyy").format(new Date(cldEventEnd.getSelectedDay()));
//        	test = new SimpleDateFormat(selectedDate);
        	selectedDate = new Date(cldEventEnd.getSelectedDay());
        	});
        
        
        Button btnCreate = new Button("Create");
        Button btnCancel = new Button("Cancel");
        setSameWidth(btnCreate, btnCancel);
        
        // sets constraints to apply to both buttons
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        
        // INFORMATION BUTTONS ----------------------------------------------------------------------------
        // Changing infoGroupName UIID allows you to create a theme for it, and applies to all components with the same UIID
        Button infoEventName = new Button("?","ButtonForInfo");
        infoEventName.addActionListener(e -> {
        	// Popup message that appears at the bottom of screen for a small amount of time. 
        	// Could maybe change the default time, haven't looked into it.
        	ToastBar.showMessage("Enter your event name into this field.\nMax of 30 characters.", FontImage.MATERIAL_INFO);
        });
        Button infoEventEnd = new Button("?","ButtonForInfo");
        infoEventEnd.addActionListener(e -> {
        	ToastBar.showMessage("Select the date that this event will end on", FontImage.MATERIAL_INFO);
        });
        
        // Adding all labels and input text fields to a container for good formatting
        Container c = TableLayout.encloseIn(3, new Label(" "), new Label("Event Name"), new Label(""),
							        		   new Label(" "), eventName, infoEventName,
							        	   	   new Label(" "), new Label("End Date"), new Label(""),
							        		   new Label(" "), cldEventEnd, infoEventEnd
							        	   	   ); // inserts blank line  
        
        // BUTTON EVENTS --------------------------------------------------------------------------------------------
        btnCancel.addActionListener(e ->{
        	infoEventName.remove();
        	infoEventEnd.remove();
        	new GroupPage(user, group).show();
    	});
        
        btnCreate.addActionListener(e ->{
        	if(guiHandler.validInput(eventName.getText()) == true) {
        		
        		if(selectedDate.getTime() > currentDate.getTime()) {
        			String curDate = new SimpleDateFormat("dd/MM/yyy").format(currentDate);
        			String selDate = new SimpleDateFormat("dd/MM/yyy").format(selectedDate);
        			
        			group = guiHandler.createGroupEvent(group, eventName.getText(), selDate, curDate);
        			Dialog.show(null, "Event has been created", "OK", null);
        			new GroupPage(user, group).show();
        			
        			
        		} else {
        			Dialog.show(null, "Please ensure selected date is a future date", "OK", null);
        		}
        	} else {
        		Dialog.show(null, "Please ensure all fields are entered", "OK", null);
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
        setTitle("Create Event");
        setName("Create Event");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}