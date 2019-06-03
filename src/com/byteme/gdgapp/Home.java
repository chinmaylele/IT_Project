package com.byteme.gdgapp;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
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
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * @author kurtg
 *
 */

public class Home extends com.codename1.ui.Form {
	   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	private User owner = new User();
	Accordion accr=new Accordion();
		
	// CONSTRUCTORS ---------------------------------------------------------------------
	public Home() {
    	this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Home(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public Home(User user) {
    	this(com.codename1.ui.util.Resources.getGlobalResources());

    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {
    	
    	Toolbar tb = this.getToolbar();
    	guiHandler.menu(tb, this.user);
    	int kudos  =  guiHandler.getLatestKudos(this.user.getUserID());
    	this.user.setKudosTotal(kudos);
    	TableLayout tl;
        int spanButton = 2;
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(14, 1);
        } else {
            tl = new TableLayout(20, 1);
            spanButton = 1;
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);


        // the search bar
        Button btnSearch = new Button();
        TextField txtSearch = new TextField("", "search for user", 20, TextArea.ANY);
        Container cntSearch = TableLayout.encloseIn(2,txtSearch,btnSearch);
        // set button icon to a search symbol
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue8b6", f, 0x000000, 50, 50);
        btnSearch.setIcon(fi);
        
        
        // container to hold username and upload button
        Container cntUsernameAndBtn = new Container(new BorderLayout());
        Button btnUpload = new Button("+ Deed");
	    btnUpload.addActionListener(e -> {
	    	new PostDeed(user,null,null,null,null).createForm();
	    	//guiHandler.uploadDeed();
	    });
        Label lblUsername = new Label(user.getUsername());
    	lblUsername.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
    	cntUsernameAndBtn.add(BorderLayout.WEST, lblUsername)
		 				 .add(BorderLayout.EAST, btnUpload);
        
        //Users kudos total
        Label lblKudosTotal = new Label(String.valueOf(user.getKudosTotal()) + " kudos");
    	lblKudosTotal.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
     	
        // group list 
        Accordion accrGroups = new Accordion();
        accrGroups = guiHandler.showGroups(user);

        
     	//Deed feed heading
     	Label lblDeedFeed = new Label(" Deed Feed"); // whitespace to align with group accordion
     	lblDeedFeed.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
     	
     	
     	
     	
     	Label lblDeeds = new Label(" Deeds", "ProfileTitles"); // whitespace so aligned with Accordions
		lblDeeds.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		Container cntDeeds;
		if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
	    	//user visiting a profile
			cntDeeds = guiHandler.getMultiUserDeeds(owner, user, false,null);
	     	
	    } else {
	    	//user owns profile
	    	cntDeeds = guiHandler.getMultiUserDeeds(user, null, true,null);
	    }
     	
     	
     	
		
		/**
		 * combobox to select most recent or most upvoted.
		 * Methods to run when above option selected.
		 * @author Chinmay
		 *
		 */
		
		
		ComboBox c=new ComboBox();
		//c.addItem("Normal");
		c.addItem("Most Recent");
		c.addItem("Most UpVoted");
		
		Label l=new Label("");
		Label l2=new Label("");
		Container addcombo=new Container();
		addcombo.setLayout(new GridLayout(1,2));
		
		addcombo.add(l);
		//addcombo.add(l2);
		addcombo.add(c);
		
		
		
		
		
     	// TODO add deed feed
     	Container cntDeedFeedHold = new Container();
     	// following code is a place holder while there is no deed feed.
     	//SpanLabel lblNone = new SpanLabel("Deed Feed does not exist.");
		//lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		cntDeedFeedHold.add(cntDeeds);
		accr.addContent(lblDeedFeed,BoxLayout.encloseY(addcombo,cntDeedFeedHold));
		accr.setScrollableY(false);
     	
     	//Button action for search to pass search to GUIHandler
        btnSearch.addActionListener(e ->{
        	if(txtSearch.getText() != "") {
        		owner = guiHandler.searchForUser(txtSearch.getText());
            	if(owner.getUserID() != 0) {
            		new Profile(this.user, owner).show();   
            	} else {
            		Dialog.show(null,
                			"User not found", 
                			"OK", null);
            	}
        	} else {
        		Dialog.show(null,
            			"Enter a username", 
            			"OK", null);
        	}
        });
        
        c.addActionListener(e -> {
			String s= (String) c.getSelectedItem();
			cntDeedFeedHold.removeAll();
			String type = null;
			if(s.equals("Most Recent"))
			{
				type ="id";
			}
			else if(s.equals("Most UpVoted")) {
				type ="vote";
			}
			
			
			Container newcntDeeds;
			
			if (user.getUserID() != owner.getUserID() && owner.getUserID() != 0 ) {
		    	//user visiting a profile
				 newcntDeeds = guiHandler.getMultiUserDeeds(owner, user, false,type);
		     	
		    } else {
		    	//user owns profile
		    	newcntDeeds = guiHandler.getMultiUserDeeds(user, null, true,type);
		    }
			cntDeedFeedHold.add(newcntDeeds);
		});
     	
        
        TableLayout.Constraint cn = tl.createConstraint();
	    cn.setHorizontalSpan(spanButton);
	    cn.setHorizontalAlign(Component.CENTER); //changed to center
	    
	    // first add inserts labels for text fields, second add adds the components
	    this.add(cntSearch)
	    .add(cntUsernameAndBtn).
	    add(lblKudosTotal).
	    add(" ").
	    add(accrGroups).
	    add(" ").
	   // add(lblDeedFeed).
	    add(accr);
	    
	    //add(cntDeedFeedHold);
        
        this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Home");
        setName("Home");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}