package com.byteme.gdgapp;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * 
 * @author leish & kurtg
 * @Created 02/01/2019
 *
 */
public class ResetPassword extends com.codename1.ui.Form {
   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public ResetPassword() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public ResetPassword(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
        
    	createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {

    	TableLayout tl;
        int spanButton = 2;
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(7, 2);
        } else {
            tl = new TableLayout(14, 1);
            spanButton = 1;
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);
        
        // title logo
        Image imgTitle = theme.getImage("title2.png");
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/3));
		Label lblTitle = new Label(imgTitle);

		// Initial creation of all buttons, labels and fields used in the ResetPassword class
        TextField identity = new TextField("", "username or email", 20, TextArea.ANY);
        
        Button btnSearch = new Button("Search");
        Button btnBack = new Button("Back");
        setSameWidth(btnSearch, btnBack);
        
        // Button to take use user input and pass it to the ResetPasswordQA() function in the GUIHandler
        btnSearch.addActionListener((e) ->{
        	if(guiHandler.validInput(identity.getText()) == false) {
        		Dialog.show(null, 
	        			"Please enter your username or email.", 
	        			"OK", null);
        	} else {
        		guiHandler.makeFailedConnection();
        		user = guiHandler.findUsernameForPWReset(identity.getText());

        		if(user.getUserID() == 0) {
        			Dialog.show(null, 
    	        			"User does not exist.", 
    	        			"OK", null);
        		} else {              	
                	new ResetPasswordQA(user).show();
        		}
        	}
        });
        
        // Button to take the user back to the login page
        btnBack.addActionListener((e) ->{
        	new Login().show();
        });
        
        //Table layout for ResetPassword display
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER); 
        
        // ADDING COMPONENTS --------------------------------------------------------------------------------------
        this.add(cn, lblTitle)
        	.add(cn, identity)
		    .add(cn, btnSearch)
		    .add(cn, btnBack);
        
        this.show();
    }
    
  //-- DON'T EDIT BELOW THIS LINE!!!


 // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
     private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
         setLayout(new com.codename1.ui.layouts.LayeredLayout());
         setInlineStylesTheme(resourceObjectInstance);
                 setInlineStylesTheme(resourceObjectInstance);
         setTitle("Reset Password");
         setName("Reset Password");
     }// </editor-fold>

 //-- DON'T EDIT ABOVE THIS LINE!!!
}