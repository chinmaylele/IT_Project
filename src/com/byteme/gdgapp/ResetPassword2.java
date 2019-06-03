package com.byteme.gdgapp;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
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
 * @author leish
 * created - 03/01/2019
 *
 */
public class ResetPassword2 extends com.codename1.ui.Form {
   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public ResetPassword2() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public ResetPassword2(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
        
    	createForm();
    }
    
    public ResetPassword2(User user) {
        this(com.codename1.ui.util.Resources.getGlobalResources());
        
        this.user = user;
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

        TextField password = new TextField("", "enter new password", 20, TextArea.PASSWORD);
        TextField password2 = new TextField("", "re-enter new password", 20, TextArea.PASSWORD);
        
        // adds the lock icon to the textfield's hint
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue897", f, 0x000000, 50, 50);
        password.setHintIcon(fi);
        password2.setHintIcon(fi);

        Button btnResetPassword = new Button("Reset Password");
        Button btnCancel = new Button("Cancel");
        setSameWidth(btnResetPassword, btnCancel);
        
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER); 
        
        // BUTTON EVENTS --------------------------------------------------------------------------------------------
        // calls the functions that reset the password
        btnResetPassword.addActionListener((e) ->{
        	if(guiHandler.validInput(password.getText()) == false || guiHandler.validInput(password2.getText()) == false ) {
        		Dialog.show(null, "Please enter new passwords", "OK", null);
        		password.clear();
    			password2.clear();
        	} else {
        		if(guiHandler.matchPassword(password.getText(), password2.getText()) == true){
	        		guiHandler.makeFailedConnection();
	        		guiHandler.resetPassword(user, password.getText());
	        		Dialog.show(null, 
	            			"Password successfully changed.", 
	            			"Login", null);
	        		new Login().show();
	        	} else {
	        		Dialog.show(null, 
	        			"Passwords do not match.\nPlease re-enter passwords.", 
	        			"OK", null);
	    			password.clear();
	    			password2.clear();
	        	}
        	}
        });
        
        // take user back to the Login
        btnCancel.addActionListener((e) ->{
        	new Login().show();
        });
        
        // ADDING COMPONENTS --------------------------------------------------------------------------------------
        this.add(cn, lblTitle)
        	.add(cn, password)
            .add(cn, password2)
            .add(cn, btnResetPassword)
            .add(cn, btnCancel);

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