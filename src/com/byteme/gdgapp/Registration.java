package com.byteme.gdgapp;

import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.DateSpinner;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * public class Registration extends com.codename1.ui.Form
 * This is the first form in the registration process, asking user to enter their details and choose a username.
 * 
 * @author leish & kurtg
 * @Created 28/12/2018
 */
@SuppressWarnings("deprecation")
public class Registration extends com.codename1.ui.Form {
	
	private Resources theme;

	private GUIHandler guiHandler = new GUIHandler();
	private User user = new User();
	
	// globally needed as form was buggy with nested if-else with components
	boolean uUsername, uEmail, allEntered, matchedPW, vEmail, vUsername = false; 
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public Registration() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Registration(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
        
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    /**
     * The method that contains all the GUI information
     */
	public void createForm() {

    	// default code from CN1
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

        // input fields
        TextField name = new TextField("", "enter name", 20, TextArea.ANY);
        TextField username = new TextField("", "enter username", 20, TextArea.ANY);
        TextField email = new TextField("", "enter email", 20, TextArea.EMAILADDR);
        TextField password = new TextField("", "enter password", 20, TextArea.PASSWORD);
        TextField password2 = new TextField("", "re-enter password", 20, TextArea.PASSWORD);
        
        // picker to choose your career path
        Picker pickOccupation = new Picker();
        pickOccupation.setType(Display.PICKER_TYPE_STRINGS);
        pickOccupation.setStrings(
				"Administration",
				"Agriculture", 
				"Architect",
				"Arts",
				"Audio/Video Technology",
				"Business Management",
				"Construction",
				"Corrections and Security",
				"Education and Training",
				"Finance",
				"Government",
				"Health Science",
				"Hospitaliy and Tourism",
				"Human Services",
				"Information Technology",
				"Law and Public Safety",
				"Manufacturing",
				"Marketing, Sales, and Service",
				"Science, Technology, and Enginering",
				"Student",
				"Transport and Distribution",
				"Other",
				"Unwaged");
        pickOccupation.setSelectedString("please select occupation");
        
        // deprecated as Picker() exists, however picker doesn't allow for min/max dates
        // CN1 staff say it should be fine, it just doesn't make use of the native picker feature
        DateSpinner spinnerDOB = new DateSpinner();
        spinnerDOB.setStartYear(1920);
        spinnerDOB.setCurrentDay(01);
        spinnerDOB.setCurrentMonth(01);
        spinnerDOB.setCurrentYear(spinnerDOB.getCurrentYear());
        // to set an age limit, just minus the age limit:
        // E.g. spinnerDOB.setEndYear(spinnerDOB.getCurrentYear() - 18) forces an age of at least 18
        spinnerDOB.setEndYear(spinnerDOB.getCurrentYear()); 

        // physically cannot enter more than 15 character into the text field
        username.setMaxSize(15);
        Font f = FontImage.getMaterialDesignFont();
        FontImage fi = FontImage.createFixed("\ue897", f, 0x000000, 50, 50);
        password.setHintIcon(fi);
        password2.setHintIcon(fi);

        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");
        setSameWidth(btnLogin, btnRegister);
        
        // sets constraints. Will be used to align components in a container
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        
        // INFORMATION BUTTONS - Leish ----------------------------------------------------------------------------
        // Changing the UIID allows you to create a theme for it, and applies to all components with the same UIID
        Button infoName = new Button("?","ButtonForInfo");
        infoName.addActionListener(e -> {
        	// Popup message that appears at the bottom of screen for a small amount of time. 
        	// Could maybe change the default time, haven't looked into it.
        	ToastBar.showMessage("Enter your name into this field.", FontImage.MATERIAL_INFO);
        });
        Button infoUsername = new Button("?","ButtonForInfo");
        infoUsername.addActionListener(e -> {
        	ToastBar.showMessage("Username cannot contain the @ symbol, cannot exceed 15 characters, and is case-sensitive." +
        						"\nYour username is how people find and recognise you. It cannot be changed, so choose wisely.", 
        						FontImage.MATERIAL_INFO);
        });
        Button infoEmail = new Button("?","ButtonForInfo");
        infoEmail.addActionListener(e -> {
        	ToastBar.showMessage("Must be a valid WelTec email address." + 
        						"\nOne account per email.", FontImage.MATERIAL_INFO);
        });
        Button infoPW = new Button("?","ButtonForInfo");
        infoPW.addActionListener(e -> {
        	ToastBar.showMessage("Your password can contain any combination of letters, numbers, and special characters.", 
        						FontImage.MATERIAL_INFO);
        });
        Button infoREPW = new Button("?","ButtonForInfo");
        infoREPW.addActionListener(e -> {
        	ToastBar.showMessage("Must be the same entry as the Password field", FontImage.MATERIAL_INFO);
        });
        Button infoDOB = new Button("?","ButtonForInfo");
        infoDOB.addActionListener(e -> {
        	ToastBar.showMessage("Select your birthday.\nThis will not be displayed to anyone. Ever.", FontImage.MATERIAL_INFO);
        });
        Button infoOccupation = new Button("?","ButtonForInfo");
        infoOccupation.addActionListener(e -> {
        	ToastBar.showMessage("Select your occupation or career.\nThis will be displayed"
        			+ " depending on your privacy", FontImage.MATERIAL_INFO);
        });
        
        Label lblDOBTitle = new Label("DOB:");
        lblDOBTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        Container cntDOB = new Container(new BoxLayout(BoxLayout.X_AXIS));
        cntDOB.add(lblDOBTitle).add(spinnerDOB);
        
        Container c = TableLayout.encloseIn(3, new Label(" "), name, infoName,
        									   	new Label(" "), username, infoUsername,
												new Label(" "), email, infoEmail,
												new Label(" "), pickOccupation, infoOccupation,
												new Label(" "), password, infoPW,
												new Label(" "), password2, infoREPW,
												new Label(" "), cntDOB, infoDOB);
        
        // BUTTON EVENTS --------------------------------------------------------------------------------------------
        btnRegister.addActionListener(e ->{
        	
        	guiHandler.makeFailedConnection();
        	
        	if(!username.getText().equals("")) { // something in the text field
        		uUsername = guiHandler.uniqueUsername(username.getText());
        	}
        	if(!email.getText().equals("")) { // something in the text field
        		uEmail = guiHandler.uniqueEmail(email.getText());
        	}
        	if(guiHandler.validInput(name.getText()) == true &&  guiHandler.validInput(username.getText()) == true &&
        			guiHandler.validInput(email.getText()) == true && guiHandler.validInput(password.getText()) == true &&
        			guiHandler.validInput(password2.getText()) == true && guiHandler.validInput(pickOccupation.getText()) == true) {
        		allEntered = true;
        	} else {
        		allEntered = false;
        	}
        	if(guiHandler.matchPassword(password.getText(), password2.getText()) == true ) {
        		matchedPW = true;
        	} else {
        		matchedPW = false;
        	}
			if(guiHandler.validEmail(email.getText()) == true) {
				vEmail = true;
    		} else {
    			vEmail = false;
    		}
			if(guiHandler.validUsername(username.getText()) == true) {
				vUsername = true;
    		} else {
    			vUsername = false;
    		}
			
			// if all inputs are valid, a user object will be created and sent through to the next stage of registration 
			if(allEntered == true) {
				if(vUsername == true) {
					if(uUsername == true) {
						if(uEmail == true) {
							if(matchedPW == true) {
								if(vEmail == true) {
						        	String strDOB = guiHandler.makeDOBString(spinnerDOB.getCurrentDay(), spinnerDOB.getCurrentMonth(), 
						        								spinnerDOB.getCurrentYear());
									user = guiHandler.createUser(name.getText(), username.getText(), email.getText(), password.getText(), 
											strDOB, pickOccupation.getText());	
									
									// CN1 bug, doesn't completely remove with GC, sometimes loads on random form
									infoName.remove();
									infoUsername.remove();
									infoEmail.remove();
									infoPW.remove();
									infoREPW.remove();
									infoDOB.remove();
									
									new Registration2(user).show();
								} else {
									Dialog.show(null, "Invalid Email.\nPlease enter a WelTec email.", "OK", null);
								}
							} else {
								Dialog.show(null, "Passwords do not match.\nPlease re-enter passwords.", "OK", null);
			        			password.clear();
			        			password2.clear();
							}
						} else {
							Dialog.show(null, "Email already exists", "OK", null);
							email.clear();
						}
					} else {
						Dialog.show(null, "Username already exists", "OK", null);
						username.clear();
					}
				} else {
					Dialog.show(null, "Username cannot contain \"@\" character", "OK", null);
					username.clear();
				}
			} else {
				Dialog.show(null, "Please ensure all fields are entered", "OK", null);
			}
    	});
        btnLogin.addActionListener(e ->{
        	// CN1 bug, doesn't completely remove with GC, sometimes loads on random form
			infoName.remove();
			infoUsername.remove();
			infoEmail.remove();
			infoPW.remove();
			infoREPW.remove();
			infoDOB.remove();
			
        	new Login().show();
    	});
               
        // ADDING COMPONENTS --------------------------------------------------------------------------------------
        this.add(cn, lblTitle);
        this.add(c);
        this.add(cn, btnRegister).add(cn, btnLogin);
        
        this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Registration");
        setName("Registration");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}