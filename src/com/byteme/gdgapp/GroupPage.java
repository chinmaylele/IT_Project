package com.byteme.gdgapp;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.Group;
import classes.User;

/**
 * 
 * @author kurtg
 *
 */

public class GroupPage extends com.codename1.ui.Form {
	
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	private Group group = new Group();
	private User owner = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public GroupPage() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public GroupPage(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public GroupPage(User user, Group group) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        this.group = group;
        
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {
    	
    	Toolbar tb = this.getToolbar();
    	guiHandler.menu(tb, this.user);
    	
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
        
        
        // the search bar
        Button btnSearch = new Button();
        Button btnAddMember = new Button();
        setSameWidth(btnSearch, btnAddMember);
        TextField txtSearch = new TextField("", "search for user", 20, TextArea.ANY);
        Container cntSearch = TableLayout.encloseIn(3,txtSearch,btnSearch,btnAddMember);
        // set search button icon to a search symbol
        Font fSB = FontImage.getMaterialDesignFont();
        FontImage fiSB = FontImage.createFixed("\ue8b6", fSB, 0x000000, 50, 50);
        btnSearch.setIcon(fiSB);
        // set add button icon to a add symbol
        Font fAM = FontImage.getMaterialDesignFont();
        FontImage fiAM = FontImage.createFixed("\ue145", fAM, 0x000000, 50, 50);
        btnAddMember.setIcon(fiAM);
        

        // Group name label
        Label lblGroupname = new Label(group.getGroupName());
        lblGroupname.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
        
        // group type
        Label lblGroupType = new Label("(" + group.getGroupType() + ")");
        lblGroupType.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        Container cntGroupName = new Container(new BoxLayout(BoxLayout.X_AXIS));
        cntGroupName.add(lblGroupname).add(lblGroupType);
        		
        //Members heading label
     	Label lblMembers = new Label("Members");
     	lblMembers.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
     	
     	// members list 
     	 Accordion accrMembers = new Accordion();
     	accrMembers = guiHandler.showGroupMembers(user, group);
        
     	
     	// add button for events
        Button btnAddEvent = new Button("+ Event");
        Container cntAddEvent = new Container(new BorderLayout());
        cntAddEvent.add(BorderLayout.EAST, btnAddEvent);
        
        // Event list
        Accordion accrEvents = new Accordion();
        accrEvents = guiHandler.showGroupEvents(user, group);
     	
        
        //Group Deed heading label
     	Label lblDeeds = new Label("Deed Feed");
     	lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
     	
     	
     	// TODO add members deeds list
     	// updating deed feed with member's deeds
     	Container cntDeedsHold = new Container();
     	SpanLabel lblNone = new SpanLabel("Deed Feed does not exist.");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		cntDeedsHold.add(lblNone);
     	
     	
     	//Button to delete group
     	Button btnDeleteGroup = new Button("Delete Group");
     	
     	//Button to leave group
     	Button btnLeaveGroup = new Button("Leave Group");
     	
     	
     	//Button action for search to pass search to GUIHandler
        btnSearch.addActionListener(e ->{
        	if(txtSearch.getText() != "") {
        		owner = guiHandler.searchForUser(txtSearch.getText());
            	if(owner.getUserID() != 0) {
            		new Profile(this.user, owner).show();   
            	} else {
            		Dialog.show(null,
                			"User not found, please check the username is correct", 
                			"OK", null);
            	}
        	} else {
        		Dialog.show(null,
            			"Please input a username to search for", 
            			"OK", null);
        	}
        	txtSearch.clear();
        });
        
        // Button to search database for user and add them to group
        btnAddMember.addActionListener(e ->{
        	if(txtSearch.getText() != "") {
        		owner = guiHandler.searchForUser(txtSearch.getText());
            	if(owner.getUserID() != 0) {
            		
            		if(owner.getGroupArray().contains(group.getGroupID())) {
            			Dialog.show(null,
                    			"User is already a member", 
                    			"OK", null);
            		} else {
            			guiHandler.addGroupMember(owner, group);
            			Dialog.show(null,
                    			"User "+ owner.getUsername()+ " added to group", 
                    			"OK", null);
            			new GroupPage(user, group).show();
            		}
            	} else {
            		Dialog.show(null,
                			"User not found, please check the username is correct", 
                			"OK", null);
            	}
        	} else {
        		Dialog.show(null,
            			"Please input a username to add to group", 
            			"OK", null);
        	}
        	txtSearch.clear();
        });
        
        btnAddEvent.addActionListener(e ->{
        	new CreateGroupEvent(user, group).show(); 
        });
        
        
      //TODO Button to delete group, should only show up if you are group owner
        btnDeleteGroup.addActionListener(e ->{
        		guiHandler.deleteGroup();
        });
        
      //TODO Button to leave group, should only show up if user is not group owner
        btnLeaveGroup.addActionListener(e ->{
        	guiHandler.leaveGroup();
        });
        
        
        TableLayout.Constraint cn = tl.createConstraint();
	    cn.setHorizontalSpan(spanButton);
	    cn.setHorizontalAlign(Component.CENTER); //changed to center
	    
	    // first add inserts labels for text fields, second add adds the components
	    this.add(cntSearch).
	    add(cntGroupName).add(" ").
	    add(accrMembers).
	    add(cntAddEvent).
	    add(accrEvents).
	    add(" ").
	    add(lblDeeds).
	    add(cntDeedsHold);
	    
	    // delete group button, will only display if the user is the group owner or admin
	    if(user.getUserID() == group.getGroupOwner() || user.isAccType() == true) {
	    	this.add("\n").add(cn, btnDeleteGroup);
	    } else {
	    	this.add("\n").add(cn, btnLeaveGroup);
	    }
    	
    	this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
        setScrollableY(true);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Group Page");
        setName("Group Page");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}