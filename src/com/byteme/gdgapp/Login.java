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
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * 
 * @author leish & kurtg
 * @Created 28/12/2018
 *
 */
public class Login extends com.codename1.ui.Form {
   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public Login() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Login(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    	
    	createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    /**
     * The method that contains all the GUI information
     */
    public void createForm() {
    	
    	// title logo
        Image imgTitle = theme.getImage("title2.png"); // white background blue logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth()/2));
		Label lblTitle = new Label(imgTitle);
        
    	/**
		 * Code found start
				author: Codename One
				source:	https://www.codenameone.com/javadoc/com/codename1/ui/TextField.html
				date:	-
		 */
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

        // modified fields to personalise for this app
        TextField username = new TextField("", "username", 20, TextArea.ANY);
        TextField password = new TextField("", "password", 20, TextArea.PASSWORD);
        
        // Leish added - this adds a lock symbol as a hint to the text field
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue897", f, 0x000000, 50, 50);
        password.setHintIcon(fi);
        
        // created another button
        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");
        setSameWidth(btnLogin, btnRegister);
        
        // added event listeners for button click
        btnLogin.addActionListener(e ->{
        	if(guiHandler.validInput(username.getText()) == true) {
        		if(guiHandler.validInput(password.getText()) == true) {
        			guiHandler.makeFailedConnection();
                	user = guiHandler.login(username.getText(), password.getText());

                	if(user.getUserID() == 0) {
                		if(Dialog.show(null, "Incorrect username or password", 
        	        			"Reset Password", "OK")) {
                			new ResetPassword().show();
                		} else {
                			password.clear();
                		}
                	} else {
                		new Home(user).show();
                	}
            	} else {
            		Dialog.show(null, "Enter a password", "OK", null);
            	}
        	} else {
        		Dialog.show(null, "Enter a username", "OK", null);
        	}
    	});
        btnRegister.addActionListener(e ->{
        		new Registration().show();
        	});
        
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER); //changed to center
        
        this.add(new Label(" "))
	    	.add(cn, lblTitle)
	    	.add(cn, username)
	        .add(cn, password)
	        .add(cn, btnLogin)
	        .add(cn, btnRegister);
        /**
		 * Code found stop
		 */
        // .add(ClearableTextField.wrap(username)) 	// example code of adding an x to clear text field
        											// not used as it adds a buffer around a text field, didn't look into how to prevent that
        
        Label labelForgotPW = new Label("Forgot your password?");
        // overrides the font style of the default UIID for a Label
        labelForgotPW.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		
        Button btnForgotPW = new Button("Reset password", "Label");
        btnForgotPW.getAllStyles().setTextDecoration(Style.TEXT_DECORATION_UNDERLINE);
        btnForgotPW.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));

        btnForgotPW.addActionListener(e -> {
        	new ResetPassword().show();
        });
        this.add(cn, FlowLayout.encloseMiddle(labelForgotPW, btnForgotPW));

        this.show();
    }
    
  //-- DON'T EDIT BELOW THIS LINE!!!


 // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
     private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
         setLayout(new com.codename1.ui.layouts.LayeredLayout());
         setInlineStylesTheme(resourceObjectInstance);
                 setInlineStylesTheme(resourceObjectInstance);
         setTitle("Login");
         setName("Login");
     }// </editor-fold>

 //-- DON'T EDIT ABOVE THIS LINE!!!
}