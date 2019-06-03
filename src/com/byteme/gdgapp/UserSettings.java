package com.byteme.gdgapp;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * 
 * @author kurtg
 *
 */
public class UserSettings extends com.codename1.ui.Form {

	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public UserSettings() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public UserSettings(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public UserSettings(User user) {
        this(com.codename1.ui.util.Resources.getGlobalResources());
        
    	this.guiHandler = new GUIHandler(theme);
        this.user = user;
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    public void createForm() {
    	
    	Toolbar tb = this.getToolbar();

    	guiHandler.menu(tb, this.user);
    	
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

        /**
         * text fields for users name and email input
         */
        TextField name = new TextField(user.getRealName(), "name", 20, TextArea.ANY);
        TextField email = new TextField(user.getEmail(), "email", 20, TextArea.EMAILADDR);
        
        /**
         * Picker to display users security question options
         */
        Picker sqPicker = new Picker();
        sqPicker.setType(Display.PICKER_TYPE_STRINGS);
		sqPicker.setStrings(
				"What is your favorite movie?", 
				"Where did you vacation last year?",
				"What's your favourite music genre?",
				"What was your college mascot?",
				"What's the name of your first pet?",
				"What's your sibling's middle name?",
				"In what town was your first job?");
		sqPicker.setSelectedString(user.getSecurityQuestion());
        
		/**
		 * text fields for users security answer input
		 */
        TextField answer = new TextField(user.getSecurityAnswer(), "security answer", 20, TextArea.ANY);
        
        /**
         * Picker to display users privacy options options
         */
        Picker privacyPicker = new Picker();
        privacyPicker.setStrings(
				"Public",
				"Private");
        privacyPicker.setSelectedString(guiHandler.displayPrivacy(user.isPrivacy()));
        
      /**
       * text fields for users current password and new passwords input
       */
        TextField currentPassword = new TextField("", "enter current password", 20, TextArea.PASSWORD);
        TextField newPassword = new TextField("", "enter new password", 20, TextArea.PASSWORD);
        TextField newPassword2 = new TextField("", "re-enter new password", 20, TextArea.PASSWORD);
        
        // Leish added
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue897", f, 0x000000, 50, 50);
        currentPassword.setHintIcon(fi);
        newPassword.setHintIcon(fi);
        newPassword2.setHintIcon(fi);

        Button btnSave = new Button("Save");
        Button btnDeleteAccount = new Button("Delete Account");
        Button btnReset = new Button("Reset");
        setSameWidth(btnSave, btnDeleteAccount, btnReset);
        
        /**
         * Button to take all text field inputs and pass them to the GUIHandler function updateUserSettings()
         */
        btnSave.addActionListener(e ->{
        	user = guiHandler.updateUserSettings(user, name.getText(), email.getText(), sqPicker.getText(), answer.getText(),
        			privacyPicker.getText(), currentPassword.getText(), newPassword.getText(), newPassword2.getText());
        	currentPassword.clear();
        	newPassword.clear();
        	newPassword2.clear();
        });
        
        /**
         * Button to set all fields back to the user object defaults
         */
        btnReset.addActionListener(e -> {
        	new UserSettings(user).show();
        });
        
        /**
         * Button to call the deleteAccount() function from the GUIHandler
         */
        btnDeleteAccount.addActionListener(e ->{
        	guiHandler.deleteAccount(user, currentPassword.getText());
    	});
        
        /**
         * Table layout for UserSettings display
         */
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        this.add("Name").add(name).
                add("Email").add(email).
                add("Security Question").add(sqPicker).
                add("Answer").add(answer).
                add("Privacy").add(privacyPicker).
                add("Current Password").add(currentPassword).
                add("New Password").add(newPassword).
                add("Re-Enter New Password").add(newPassword2).
                add(cn, btnSave).
                add(cn, btnReset).
                add(cn, btnDeleteAccount);

    	this.show();
    }
    
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Settings");
        setName("UserSettings");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}