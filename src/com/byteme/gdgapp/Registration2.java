package com.byteme.gdgapp;

import java.io.IOException;
import java.io.InputStream;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
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
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * public class Registration2 extends com.codename1.ui.Form
 * This is the second form in the registration process, asking a new user to setup password recovery and privacy.
 * 
 * @author leish & kurtg
 * @Created 28/12/2018
 */
public class Registration2 extends com.codename1.ui.Form {

	private Resources theme;
	
	private GUIHandler guiHandler = new GUIHandler();
	private User user = new User();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
    public Registration2() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Registration2(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    }
    
    public Registration2(User user) {
        this(com.codename1.ui.util.Resources.getGlobalResources());
        
        
        this.user = user;
        createForm();
    }
    
    // METHODS ---------------------------------------------------------------------------
    /**
     * The method that contains all the GUI information
     */
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
		 * Code found start
				author: Codename One
				source:	https://www.codenameone.com/javadoc/com/codename1/ui/spinner/Picker.html
				date:	-
		 */
        // got rid of all the other pickers as only needed a string picker
        Picker sqPicker = new Picker();
		sqPicker.setType(Display.PICKER_TYPE_STRINGS);
		// changed strings to personalise for this app
		sqPicker.setStrings(
				"What is your favorite movie?", 
				"Where did you vacation last year?",
				"What's your favourite music genre?",
				"What was your college mascot?",
				"What's the name of your first pet?",
				"What's your sibling's middle name?",
				"In what town was your first job?");
		sqPicker.setSelectedString("please select option");
		/**
		 * Code found stop
		 */
		
		TextField answer = new TextField("", "security answer", 20, TextArea.ANY);
		Picker privacyPicker = new Picker();
        privacyPicker.setStrings(
				"Public",
				"Private");
        privacyPicker.setSelectedString("please select option");
        
        Button btnAccept = new Button("Accept");
        Button btnBack = new Button("Back");
        setSameWidth(btnAccept, btnBack);
        
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(spanButton);
        cn.setHorizontalAlign(Component.CENTER);
        
        // INFORMATION BUTTONS - Leish ------------------------------------------------------------------------
        Button infoSQ = new Button("?","ButtonForInfo");
        infoSQ.addActionListener(e -> {
        	ToastBar.showMessage("Select a security question to help you in case you forget your password", FontImage.MATERIAL_INFO);
        });
        Button infoSA = new Button("?","ButtonForInfo");
        infoSA.addActionListener(e -> {
        	ToastBar.showMessage("Enter an answer to your security question to help you in case you forget your password", FontImage.MATERIAL_INFO);
        });
        Button infoPrivacy = new Button("?","ButtonForInfo");
        infoPrivacy.addActionListener(e -> {
        	ToastBar.showMessage("Private: Only your username will be displayed for all to see\n"
        			+ "Public: Your username, name, and email will be displayed for all to see\n\n"
        			+ "This can be changed later in account settings", FontImage.MATERIAL_INFO);
        });
        
        Container c = TableLayout.encloseIn(3, new Label(" "), sqPicker, infoSQ,
        										new Label(" "), answer, infoSA,
        										new Label(" "), privacyPicker, infoPrivacy);
        
        // TERMS AND CONDITIONS - Leish ---------------------------------------------------------------------------------------
        // the following creates the T&C's in parts. A checkbox first, then a label, then a button
        CheckBox chkTCs = new CheckBox();
		chkTCs.setUIID("CheckBox2");
		
		Label labelTCs = new Label("I agree to the");
		labelTCs.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
		
        Button btnTCs = new Button("Terms and Conditions", "Label");
        btnTCs.getAllStyles().setTextDecoration(Style.TEXT_DECORATION_UNDERLINE);
        btnTCs.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));

        // BUTTON EVENTS -----------------------------------------------------------------------------------------
        btnTCs.addActionListener(e -> {
        	Dialog dialog = new Dialog("Terms and Conditions");
            // T&C's located in the src folder
            InputStream is = Display.getInstance().getResourceAsStream(this.getClass(), "/TCs.txt");
            String TCs;
            try {
            	TCs = Util.readToString(is, "UTF-8");
    		} catch (IOException e1) {
    			e1.printStackTrace();
    			TCs = "Cannot load Terms & Conditions";
    		}
         // SpanLabels allow for multi-line text. Also recognises escape characters in a string (\n, \t, \r, etc...)
        	// where Labels do not
        	SpanLabel spanLabel = new SpanLabel(TCs, "SpanLabelTCs");
        	spanLabel.getUnselectedStyle().setFgColor(0xffffff);
        	
        	Button btnClose = new Button("Close");
        	btnClose.addActionListener(l -> dialog.dispose());
        	
        	dialog.setDisposeWhenPointerOutOfBounds(true);
        	dialog.add(spanLabel);
        	dialog.setScrollableY(true);
        	dialog.add(btnClose);
        	
        	// how much space it takes up. 0 = right against margin
        	// 							   Display.getInstance().getDisplayHeight()/2 = halfway down the screen
        	dialog.show(Display.getInstance().getDisplayHeight()/2,0,0,0);
        });
		// END TCs

        btnBack.addActionListener(e -> {
        	// CN1 bug, doesn't completely remove with GC, sometimes loads on random form
        	infoSQ.remove();
    		infoSA.remove();
			infoPrivacy.remove();
        	new Registration().show();
    	});
        btnAccept.addActionListener(e -> {
        	// if all inputs are valid, the user object will be updated and a new user created in database 
        	if(guiHandler.validInput(sqPicker.getText()) == false || guiHandler.validInput(answer.getText()) == false
        			|| guiHandler.validInput(privacyPicker.getText()) == false) {
        		Dialog.show(null, 
	        			"Please ensure all fields are entered.", 
	        			"OK", null);
        	} else {
        		if(chkTCs.isSelected()) {
        			guiHandler.makeFailedConnection();
            		guiHandler.createUserQA(user, sqPicker.getText(), answer.getText(), privacyPicker.getText());
                	
            		Dialog.show(null, 
                			"Thank You!\nYour registration is complete.", 
                			"OK", null);

            		infoSQ.remove();
            		infoSA.remove();
					infoPrivacy.remove();
            		
                	new Login().show();
        		} else {
        			Dialog.show(null, 
    	        			"You must accept the Terms and Conditions before continuing", 
    	        			"OK", null);
        		}
        	}
        });
        
        // ADDING COMPONENTS --------------------------------------------------------------------------------------
        this.add(cn, lblTitle);
        this.add(c);
        this.add(cn, FlowLayout.encloseMiddle(chkTCs, labelTCs, btnTCs));
        this.add(cn, btnAccept).add(cn, btnBack); 
        this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Registration");
        setName("Registration2");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}