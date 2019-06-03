package com.byteme.gdgapp;

import java.io.IOException;
import java.io.InputStream;

import com.codename1.components.SpanLabel;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.Toolbar;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;

import classes.GUIHandler;
import classes.User;

/**
 * @author leish
 * @Created 07/02/2019
 *
 */

public class Help extends com.codename1.ui.Form {
	   
	private Resources theme;
	private GUIHandler guiHandler;
	private User user = new User();
		
	// CONSTRUCTORS ---------------------------------------------------------------------
	public Help() {
    	this(com.codename1.ui.util.Resources.getGlobalResources());
    }
    
    public Help(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        
        this.theme = resourceObjectInstance;
    	this.guiHandler = new GUIHandler(theme);
    }
    
    public Help(User user) {
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
        if(Display.getInstance().isTablet()) {
            tl = new TableLayout(14, 1);
        } else {
            tl = new TableLayout(14, 1);
        }
        tl.setGrowHorizontally(true);
        this.setLayout(tl);
        
        TableLayout.Constraint cn = tl.createConstraint();
        cn.setHorizontalSpan(1);
        cn.setHorizontalAlign(Component.CENTER);

        InputStream is = Display.getInstance().getResourceAsStream(this.getClass(), "/HelpText.txt");
        String helpText;
        try {
        	helpText = Util.readToString(is, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
			helpText = "Cannot load Help";
		}
        SpanLabel lblText = new SpanLabel(helpText);
        lblText.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        
        Button devGuide = new Button("User Guide");
        devGuide.addActionListener(e -> {
            FileSystemStorage fs = FileSystemStorage.getInstance();
            String guideFile = fs.getAppHomePath() + "user-guide-eg.pdf";
            if(!fs.exists(guideFile)) {
                Util.downloadUrlToFile("https://www.keepandshare.com/doc3/58210/user-guide-example-pdf-119k?da=y", guideFile, true);
            }
            Display.getInstance().execute(guideFile);
        });
        
        this.add(lblText)
        	.add(cn, devGuide);
        
        this.show();
    }
    
//-- DON'T EDIT BELOW THIS LINE!!!


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("Help");
        setName("Help");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}