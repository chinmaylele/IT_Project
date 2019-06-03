package com.byteme.gdgapp;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * 
 * @author leish & Kurt
 *
 */
public class Profile extends com.codename1.ui.Form {
	
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	private User owner = new User();
	
	/**
	 * Future Dev: consider separating this form into two, one remaining for the logged in user,
	 * 			   another for whomever the user searches for
	 */
    
	// CONSTRUCTOR ---------------------------------------------------------------------------
	public Profile() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Profile(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public Profile(User user) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        this.owner.setUserID(0);
        
        createForm();
    }
    
    public Profile(User user, User owner) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        this.owner = owner;
        
        createForm();
    }
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {
    	
    	Toolbar tb = this.getToolbar();
    	guiHandler.menu(tb, this.user);
        // get the latest user from DB using user id.
    	int kudos  =  guiHandler.getLatestKudos(this.user.getUserID());
    	this.user.setKudosTotal(kudos);
    	TableLayout tl;
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(14, 2);
        } else {
            tl = new TableLayout(14, 1);
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);   
        
        // Search bar
        Button btnSearch = new Button();
        TextField txtSearch = new TextField("", "search for user", 20, TextArea.ANY);
        Container cntSearch = TableLayout.encloseIn(2, txtSearch, btnSearch);
        // set button icon to a search symbol
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
        btnSearch.setIcon(fi);
     
        // Container to hold username and button, button changes depending on display type
        Container cntUsernameAndBtn = new Container(new BorderLayout());
        Button btnAddAssociate = new Button("+ Add");
        Button btnUpload = new Button("+ Deed");
        if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0) {
        	//user is visiting
            cntUsernameAndBtn.add(BorderLayout.WEST, owner.getUsername())
            				 .add(BorderLayout.EAST, btnAddAssociate);
        } else {
        	//user owns profile
        	Label lblUsername = new Label(user.getUsername());
        	lblUsername.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
        	cntUsernameAndBtn.add(BorderLayout.WEST, lblUsername)
			 				 .add(BorderLayout.EAST, btnUpload);
        }
        
        // Container for user kudos total
        Container cntKudos = new Container();
        if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0) {
        	// user visiting a profile
        	//Displays owners kudos total
        	Label lblKudosTotal = new Label(String.valueOf(owner.getKudosTotal()) + " kudos");
        	lblKudosTotal.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
         	
        	cntKudos = TableLayout.encloseIn(1,lblKudosTotal);
        } else {
        	//user owns profile
        	//Displays users kudos total
        	Label lblKudosTotal = new Label(String.valueOf(user.getKudosTotal()) + " kudos");
        	lblKudosTotal.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));

        	cntKudos = TableLayout.encloseIn(1,lblKudosTotal);	
        }
        
        //Container to hold the user's display picture and another container holding user information depending on privacy type
        Container cntPicAndInfo = new Container(new BoxLayout(BoxLayout.X_AXIS));
        Container cntUserInfo = new Container();
        
        if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
        	//user visiting a profile

        	// Displays users profile picture
        	/**
        	 * Proposed - pre-selected display images saved to the theme. Allow user to select during registration.
        	 */
        	Image imgUserDP = theme.getImage(owner.getProfilePicture());
        	Image resizedImage = imgUserDP.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 3));
        	Label lblUserDP = new Label();
        	lblUserDP.setIcon(resizedImage);

        	if (owner.isPrivacy() == true) {
        		//visited is private
             	
             	Label lblUserInfo = new Label("Private User");
             	lblUserInfo.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
             	cntUserInfo.add(lblUserInfo);
        	} else {
        		//visited is public
        		// get owner information and store in string to add to label, Public
            	String strUserInfo = "Name:       "+ owner.getRealName();
            	SpanLabel lblUserInfo = new SpanLabel(strUserInfo);
            	lblUserInfo.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));

            	String strUserInfo2 = "Occupation: " + owner.getOccupation();
            	SpanLabel lblUserInfo2 = new SpanLabel(strUserInfo2);
            	lblUserInfo2.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
             	
             	cntUserInfo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
             	cntUserInfo.add(lblUserInfo).add(lblUserInfo2);	
        	}
        	cntPicAndInfo.add(lblUserDP).add(cntUserInfo);
        } else {
        	//user owns profile
        	
        	// Displays users profile picture
        	/**
        	 * Proposed - pre-selected display images saved to the theme. Allow user to select during registration.
        	 */
        	Image imgUserDP = theme.getImage(user.getProfilePicture());
        	Image resizedImage = imgUserDP.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 3));
        	Label lblUserDP = new Label();
        	lblUserDP.setIcon(resizedImage);
        	
        	// get owner information and store in string to add to label, Public
        	String strUserInfo = "Name:       "+ user.getRealName();
        	SpanLabel lblUserInfo = new SpanLabel(strUserInfo);
        	lblUserInfo.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));

        	String strUserInfo2 = "Occupation: " + user.getOccupation();
        	SpanLabel lblUserInfo2 = new SpanLabel(strUserInfo2);
        	lblUserInfo2.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
         	
         	cntUserInfo = new Container(new BoxLayout(BoxLayout.Y_AXIS));
         	cntUserInfo.add(lblUserInfo).add(lblUserInfo2);
        	
         	cntPicAndInfo.add(lblUserDP).add(cntUserInfo);
        }

	  	// ACHIEVEMENTS
		// Accordion (expandable menu) to hold the user's Achievements
		Accordion accrAchievements = new Accordion();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
			//user visiting a profile
		 	accrAchievements = guiHandler.toggleAchievements(owner);
		} else {
			//user owns profile
			accrAchievements = guiHandler.toggleAchievements(user);
		}
        
        // ASSOCIATES
		// Accordion (expandable menu) to hold the user's Associates
	  	Accordion accrAssociates = new Accordion();
  		accrAssociates.setAutoClose(true);
	  	if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
	  		accrAssociates = guiHandler.toogleAssociates(owner, user, false);

	    } else {
	    	//user owns profile
	    	accrAssociates = guiHandler.toogleAssociates(user, null, true);
	    }

		// DEEDS
		Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds = new Container();
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getSingleUserDeeds(owner, user, false);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getSingleUserDeeds(user, null, true);
	    }
		
        Button btnContact = new Button("Contact"); // for contacting user
		
        // BUTTON FUNCTIONS --------------------------------------------------------------------------------
		// Button action for search to pass search to GUIHandler
	    btnSearch.addActionListener(e ->{
	    	if(!txtSearch.getText().equals("")) {
	    		owner = guiHandler.searchForUser(txtSearch.getText());
	    		
	        	if(owner.getUserID() == 0) {
	        		Dialog.show(null, "User not found", "OK", null);
	        	} else {
	        		new Profile(this.user, owner).show();
	        	}
	    	} else {
	    		Dialog.show(null, "Enter a username", "OK", null);
	    	}
	    });
	    btnContact.addActionListener(e -> {
	    	guiHandler.contactUser(owner);
    	});
	    btnUpload.addActionListener(e -> {
	    	new PostDeed(user,null,null,null,null).createForm();
	    	
	    });
	    btnAddAssociate.addActionListener(e -> {
	    	guiHandler.addAssociate();
	    });
	    // ADDING COMPONENTS ------------------------------------------------------------------------------
	    this.add(cntSearch)
		    .add(cntUsernameAndBtn)
		    .add(cntKudos)
		    .add(cntPicAndInfo)
		    .add(btnContact)
		    .add(accrAchievements)
		    .add(accrAssociates)
		    .add(lblDeeds)
		    .add(cntDeeds);
	    
	    if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0) {
	    	//user visiting a profile
	    	btnContact.setHidden(false);
	    } else {
	    	//user owns profile
	    	btnContact.setHidden(true);
	    }
		
		this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Profile");
        setName("Profile");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}