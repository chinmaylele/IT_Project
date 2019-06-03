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
 * @author kurtg
 *
 */
public class ResetPasswordQA extends com.codename1.ui.Form {
   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public ResetPasswordQA() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public ResetPasswordQA(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public ResetPasswordQA(User user) {
        this(com.codename1.ui.util.Resources.getGlobalResources());
        
        this.guiHandler = new GUIHandler(theme);
        this.user = user;
        
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

        /**
         * Initial creation of all buttons, labels and fields used in the ResetPasswordQA class
         */
        Button btnBack = new Button("Back");
        Button btnResetPassword = new Button("Reset Password");
        setSameWidth(btnResetPassword, btnBack);
        TextField securityAns = new TextField("", "security answer", 20, TextArea.ANY);
        Label userSecQuestionLbl = new Label(user.getSecurityQuestion());

        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        
        /**
         * Layout for ResetPasswordQA display
         */
        this.add(cn, lblTitle)
        	.add(cn, userSecQuestionLbl)
        	.add(cn, securityAns)
        	.add(cn, btnResetPassword)
    		.add(cn, btnBack);
        
        /**
         * Button to call the compareSecurityAnswer() function from the GUIHandler
         */
        btnResetPassword.addActionListener((e) ->{
        	if(guiHandler.compareSecurityAnswer(user, securityAns.getText()) == true) {
        		new ResetPassword2(user).show();
        	} else {
        		Dialog.show(null, 
	        			"Incorrect security answer.\nPlease try again", 
	        			"OK", null);
        		securityAns.clear();
        	}
         });
        
        /**
         * Button to take the user back the the ResetPassword page
         */
        btnBack.addActionListener((e) ->{
        	new ResetPassword().show();
        });
        
        
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