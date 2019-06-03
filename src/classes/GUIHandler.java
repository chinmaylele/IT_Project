package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.byteme.gdgapp.AdminSettings;
import com.byteme.gdgapp.CreateGroup;
import com.byteme.gdgapp.GroupPage;
import com.byteme.gdgapp.Help;
import com.byteme.gdgapp.Home;
import com.byteme.gdgapp.Login;
import com.byteme.gdgapp.PostDeed;
import com.byteme.gdgapp.Profile;
import com.byteme.gdgapp.UserSettings;
import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.Resources;
import com.codename1.util.Base64;
import com.codename1.util.StringUtil;
import classes.Deed;
import classes.DeedCate;
import classes.GUIHandler;
/**
 * This class is the primary controller for all GUI's (forms), effectively the central location for all functions related to a GUI.
 * 
 * @author leish & kurtg & Chinmay
 * @Created	21/12/2018
 */
public class GUIHandler {

	private Resources theme;
	
	private DBHandler dbHandler = new DBHandler();
	private Deed deed=new Deed();
	private User user = new User();
	private DeedCategory dcat=new DeedCategory();
	private User owner = new User();
	private Group group = new Group();
	private Event event = new Event();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	/**
	 * A function may require the form's theme to make additions to it. Use this constructor if that is the case.
	 * @param resourceObjectInstance - the form's theme
	 */
	public GUIHandler(com.codename1.ui.util.Resources resourceObjectInstance) {
		this.theme = resourceObjectInstance;
	}
	public GUIHandler() {}
	
	// TOOLBAR METHODS ---------------------------------------------------------------------------
	/**
	 * Adds a navigation menu to the top left of a form.
	 * 
	 * @author kurtg & leish
	 * @param tb form's toolbar
	 * @param u user object
	 * 
	 * @Note This function requires the form's theme. You must use the GUIHandler(com.codename1.ui.util.Resources resourceObjectInstance) 
	 * constructor to invoke this method
	 */
	public void menu(Toolbar tb, User user) {
		this.user = user;
		Label label = new Label("Navigation Menu", "SideMenu");
		label.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_LARGE));
		label.getUnselectedStyle().setFgColor(0xffffff); //white
		
		Image imgTitle = theme.getImage("title.png"); // blue background and white logo
		imgTitle = imgTitle.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 2));
		
		/**
		 * Code found start
				author: Shau
				source:	https://www.codenameone.com/blog/tutorial-create-a-gorgeous-sidemenu.html
				date:	12 May 2017
		 */
		// moved toolbar invoke to method parameter
        Container topBar = BorderLayout.west(new Label(imgTitle));
        topBar.add(BorderLayout.SOUTH, label);
        topBar.setUIID("SideCommand");
        tb.addComponentToSideMenu(topBar);
        
        // only took the one line for an example
        tb.addMaterialCommandToSideMenu("Home", FontImage.MATERIAL_HOME, e ->{
        	new Home(user).show();
        });
        /**
    	 * Code found stop
    	 */
        tb.addMaterialCommandToSideMenu("Profile", FontImage.MATERIAL_PERSON, e ->{
        	new Profile(user).show();
        });
        tb.addMaterialCommandToSideMenu("Settings", FontImage.MATERIAL_SETTINGS, e ->{
        	new UserSettings(user).show();
        	
        	/**
        	 * The following code will be used to lock an adminstrator from being able to manipulate 
        	 * their own data.
        	 * Proposed - Create a new application seperate from Kudos that is designed specifically 
        	 * 			and only for encryption. This will allow an admin to encrypt a new password 
        	 * 			they can then manually input into the db in case they need to change their 
        	 * 			password.
        	 * 
        	 * start.
        	 */
//        	if(u.getUserID() == 1) { // if new administrators are added, this will need to be changed accordingly
//    		Dialog.show(null, "Cannot access user settings for administrators", "OK", null);
//	    	} else {
//	        	new UserSettings(u).show();
//	    	}
        	/**
        	 * end.
        	 */
        });

    	if(user.getUserID() == 1) { // if new administrators are added, this will need to be changed accordingly
    		tb.addMaterialCommandToSideMenu("Admin Settings", FontImage.MATERIAL_SETTINGS_APPLICATIONS, e ->{
            	new AdminSettings(user).show();
            });
    	}
        tb.addMaterialCommandToSideMenu("Logout", FontImage.MATERIAL_EXIT_TO_APP, e ->{
        	logout(user);
        });
        tb.addMaterialCommandToSideMenu("Help", FontImage.MATERIAL_HELP, e ->{
        	new Help(user).show();
        });
    }	
	
	// RANDOM METHODS --------------------------------------------------------------------------------------
	/**
	 * This function's sole purpose is to make an initial connection to the database in order to fail.
	 * App will fail for some unknown reason without this function call in the forms. 
	 * Perhaps it's because of the SQL API that is unsupported by CN1.
	 * 
	 * @author leish
	 */
	public void makeFailedConnection() {
		dbHandler.connect();
	}
	
	/**
	 * Immediately calls DBHandler Class and passes parameters along to log a user into the system.
	 * 
	 * @author leish
	 * @param username input to check against stored username in database
	 * @param password input to check against stored password in database
	 * @return user object
	 */
	public User login(String username, String password) {

    	String encryptedPW = encrypt(password);
		user = dbHandler.login(username, encryptedPW);
		
		return user;
	}
	
	/**
	 * Resets all objects in memory to null. Moves user from whatever form they were on to Login.
	 * 
	 * @author leish
	 * @param user user object to null
	 */
	public void logout(User user) {
		this.theme = null;
		this.dbHandler = null;
		this.user = null;

		user.disposeUser();
		
		new Login().show();
	}
	
	/**
	 * Creates an initial partially filled user object
	 * 
	 * @author leish
	 * @param realName 
	 * @param username 
	 * @param email 
	 * @param password 
	 * @return user object
	 */
	public User createUser(String realName, String username, String email, String password, String dob, String occ) {

    	String encryptedPW = encrypt(password);
		
		user.setRealName(realName);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(encryptedPW);
		user.setDOB(dob);
		user.setOccupation(occ);

		return user;
	}
	
	public User uploadDeedwcat(String deedcatname, User user,Deed deed,DeedCategory dcat, Achievement ach,AchievementCategory achcat)
	{
		//dcate.pointSystem();
		
		//dcat.setDeedCatID(deedcatid);
		 dcat.setDeedCatName(deedcatname);
		dbHandler.uploadDeedwcat(dcat,user,deed,ach,achcat);
		
		return user;
		
	}
	
	/**
	 * Completes a user object and calls DBHandler Class to create a user in the database
	 * 
	 * @author leish & kurtg
	 * @param user
	 * @param securityQuestion
	 * @param securityAnswer
	 * @param privacy
	 */
	public void createUserQA(User user, String securityQuestion, String securityAnswer, String privacy) {
		
		user.setSecurityQuestion(securityQuestion);
		user.setSecurityAnswer(securityAnswer);
		
		if(privacy.toLowerCase().contains("public")) {
			user.setPrivacy(false);
		} else {
			user.setPrivacy(true);
		}
		dbHandler.createUser(user);
	}
	
	/**
	 * Updates all information at once.
	 * 
	 * @author kurtg
	 * @param user user object with current user data
	 * @param name
	 * @param email
	 * @param securityQ
	 * @param securityA
	 * @param privacy
	 * @param currentPW
	 * @param newPW
	 * @param newPW2
	 * @return user new user object to refresh the form with
	 * 
	 * @Note may look into further separating this update into different functions, e.g. making password update a separate function.
	 */
	public User updateUserSettings(User user, String name, String email, String securityQ, String securityA, 
			String privacy, String currentPW, String newPW, String newPW2) {
		
		boolean passwordUpdated = true;
		String encryptedPW = encrypt(currentPW);
		String encryptedNewPW = encrypt(newPW);
		boolean boolEmail = false;
		
		if(Dialog.show(null,
    			"Are you sure you wish to save these settings?", 
    			"No", "Yes")) { 
			// No
			// do nothing
    		return user;
		} else { 
			// Yes
			if(user.getEmail().equals(email)) {
				boolEmail = true;
			} else {
				boolEmail = uniqueEmail(email);
			}
			if(boolEmail == true) {
				if(encryptedPW.equals(user.getPassword())) {
					this.user = user;
					if(!newPW.equals("") || !newPW2.equals("")) {
						if(newPW.equals(newPW2)) {				    	
							user.setPassword(encryptedNewPW); 
							dbHandler.resetPassword(user, user.getPassword());
	    				} else {
	    					passwordUpdated = false;
	    				}
					}
					user.setRealName(name);
					user.setEmail(email);
					user.setSecurityQuestion(securityQ);
					user.setSecurityAnswer(securityA);
					if(privacy.toLowerCase().contains("public")) {
						user.setPrivacy(false);
					} else {
						user.setPrivacy(true);
					}
	    			dbHandler.updateUserSettingsDB(user);
	    			if(passwordUpdated == true) {
	    				Dialog.show(null,
	    	        			"All Updates completed.", 
	    	        			"OK", null);
	    			} else {
	    				Dialog.show(null,
	    	        			"User details update complete. "
	    	        			+ "\nUnable to update password as new passwords do not match.", 
	    	        			"OK", null);
	    			}
				} else if(currentPW.equals("")) {
					Dialog.show(null,
	        			"Please enter password to save changes.", 
	        			"OK", null);
				} else if(encryptedPW != user.getPassword()) {
					Dialog.show(null,
	        			"Please enter correct password to save changes.", 
	        			"OK", null);
				} else {
					Dialog.show(null,
	        			"Error.\nPlease try again later.", 
	        			"OK", null);   
				}
			} else {
				Dialog.show(null,
	        			"Email is not unique.\nPlease choose another email.", 
	        			"OK", null);
			}
			
			return user;
		}
	}
	
	/**
	 * Immediately calls DBHandler Class to search database for a user
	 * 
	 * @author leish & kurtg
	 * @param identity username or email input
	 * @return user object
	 */
	public User findUsernameForPWReset(String identity) {

		user = dbHandler.findUsernameForPWReset(identity);
		return user;
	}
	
	/**
	 * Compares input against the user object's security answer.
	 * 
	 * @author leish
	 * @param u user object to be compared to
	 * @param ans input
	 * @return true = matching, false = not-matching
	 */
	public boolean compareSecurityAnswer(User u, String ans) {
		
		if(u.getSecurityAnswer().toLowerCase().equals(ans.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Immediately calls DBHandler Class to update the password in the database
	 * 
	 * @author leish
	 * @param u user object
	 * @param pw new password
	 */
	public void resetPassword(User u, String pw) {

    	String encryptedPW = encrypt(pw);
		dbHandler.resetPassword(u, encryptedPW);
	}
	
	/**
	 * Checks the password matches against the user object. Prompts user several times to confirm deletion. 
	 * Call the DBHandler Class to delete account from database if user confirms all prompts.
	 * 
	 * @author kurtg
	 * @param user user object
	 * @param pw password input
	 */
	public void deleteAccount(User user, String pw) {
		if(Dialog.show(null,
    			"Are you sure you wish to delete your account?", 
    			"No", "Yes")) {
    		// do nothing
		} else { 
			if(Dialog.show(null,
	    			"This action cannot be undone.\nDelete account?", 
	    			"No", "Yes")) {
	    		// do nothing
			} else {
				String encryptedPW = encrypt(pw);
		    	
				if(encryptedPW.equals(user.getPassword())) {
					dbHandler.deleteAccountDB(user);
					logout(user);
				} else if(pw.equals("")) {
					Dialog.show(null,
		        			"Please enter password to confirm account deletion.", 
		        			"OK", null);
				} else if(encryptedPW != user.getPassword()) {
					Dialog.show(null,
	        			"Please enter correct password to confirm account deletion.", 
	        			"OK", null);
				} else {
					Dialog.show(null,
	        			"Error.\nPlease try again later.", 
	        			"OK", null);   
				}
			}
		}
	}
	
	/**
	 * @author kurtg
	 * @param searchedUser
	 * @return
	 */
	public User searchForUser(String searchedUsername) {
		// call dbhander to search		
		return dbHandler.searchForUserDB(0, searchedUsername);
	}
	/**
	 * @author kurtg
	 * @param searchedUser
	 * @return
	 */
	public User searchForUser(int searchedUserID) {
		// call dbhander to search
		return dbHandler.searchForUserDB(searchedUserID, "");
	}
	
	/**
	 * Displays a message with contact information depending on the privacy settings of the user
	 * 
	 * @author leish
	 * @param u user object
	 */
	public void contactUser(User u) {
		
		if(u.isPrivacy() == true) {
			//private acc
			Dialog.show(null,
        			"This person does not wish to be contacted.", 
        			"OK", null);
		} else {
			//public acc
			Dialog.show(null,
        			"This person accepts contact requests.\n"
        			+ "Please contact administration to find out more.", 
        			"OK", null);
		}
	}
	
	/**
	 * Makes a date string in the format DD/MM/YYYY for SQLite DB storage.
	 * @author leish
	 * @param d
	 * @param m
	 * @param y
	 * @return
	 */
	public String makeDOBString(int d, int m, int y) {
		
		String strDOB = "";
		
		if(d<10) {
			strDOB += "0";
		}
		strDOB += d + "/";
		if(m<10) {
			strDOB += "0";
		}
		strDOB += m + "/" + y;
		
		return strDOB;
	}
	
	/**
	 * Converts a date string into either full word format or slashed
	 * 
	 * @author leish
	 * @param d date as a string. Either DD/MM/YYYY or DD Month YYYY
	 * @return new date string
	 */
	public String changeDOBFormat(String d) {
		
		String[] strMonths = {"January","Febuary","March","April","May","June","July","August","September",
				"October","Novemeber","December"};
		String strDOB;
		
		if(d.contains("/")) {
			List<String> lstDOBToLong = StringUtil.tokenize(d, "/");
			strDOB = lstDOBToLong.get(0) + " " + strMonths[Integer.parseInt(lstDOBToLong.get(1))-1] + " " + lstDOBToLong.get(2);
		} else {
			List<String> lstDOBToShort = StringUtil.tokenize(d, " ");
			strDOB = lstDOBToShort.get(0) + "/";
			for(int i = 0; i < strMonths.length; i++) {
				if(lstDOBToShort.get(1).equals(strMonths[i])) {
					if(i + 1 < 10) {
						strDOB += "0";
					}
					strDOB += (i + 1) + "/" + lstDOBToShort.get(2);
				}
			}
		}
		return strDOB;
	}
	
	/**
	 * Turns boolean value into it's String counterpart
	 * 
	 * @author kurtg
	 * @param privacy true or false
	 * @return true = Private, false = Public
	 */
	public String displayPrivacy(boolean privacy) {
		if(privacy == true ) {
			return "Private";
		} else {
			return "Public";
		}
	}
	
	// ACHIEVEMENTS AND DEEDS METHODS ------------------------------------------------------------------------
	
	/**
	 * Methods for upvoting & downvoting
	 * @author Chinmay
	 *
	 */
	
	public int upvoteDeed(User user, Deed deed) 
	{

		return dbHandler.upvoteDeed(user,deed);
	}
	
	public int  downvoteDeed(User user, Deed deed) 
	{
		return dbHandler.downvoteDeed(user,deed);
	}
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void toggleDeeds() {
		// TODO - maybe, deeds are stored in a container for now. Maybe needed for the Deed Feed.
	}
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void deleteDeed(int dID) {
		Dialog.show(null, "Deleting a deed doesn't work yet.\nPlease hold while we fix this.", 
				"OK", null);
	}
	
	/**
	 * Calls database equivalent function and reloads the profile page.
	 * @author leish
	 * @param dID deed id of the deed being reported
	 * @param u user object for the logged in user
	 * @param o user object for the visited user
	 */
	public void reportDeed(int dID, User u, User o) {
		
		dbHandler.reportDeed(dID);
		Dialog.show(null, "You have reported this deed.", "OK", null);
		new Profile(u, o).show();
	}
	
	/**
	 * Immediately calls the database equivalent function to find and store achievements
	 * @author leish
	 * @param u user object
	 * @return user object with achievements added
	 */
	public User getAchievements(User u) {
		return dbHandler.getAchievements(u);
	}
	
	/**
	 * Primarily called from Profile (maybe can be used in other forms, haven't tested), will populate an Accordion
	 * with all a user's deeds.
	 * @author Leish
	 * @param deedOwner user object of the page being viewed
	 * @param u user object if visiting another profile, null if owns the viewed profile
	 * @param bool true if the user owns the viewed profile, false if the user is visiting
	 * 			another profile
	 * @return container object with all Deeds components added
	 */
	public Container getSingleUserDeeds(User deedOwner, User u, boolean bool) {
		
		int deedCount = 0;
		Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		DeedCategory dc = new DeedCategory();
		AchievementCategory ac = new AchievementCategory();
    	
		deedOwner = dbHandler.getSingleUserDeeds(deedOwner);
		
		if(deedOwner.getDeedArray().size() == 0) { // no deeds
			SpanLabel lblNone = new SpanLabel("No recorded Deeds");
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			// adding to outermost container
			cntDeeds.add(lblNone);
		} else { // one or more deeds
			for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
				Deed dd= deedOwner.getDeedArray().get(i);
				dc = dbHandler.getDeedCat(deedOwner.getDeedArray().get(i).getDeedCatID());
				ac = dbHandler.getAchievementCat(dc);
				
				SpanLabel lblAchCatName = new SpanLabel(ac.getAchieveCatName());
				SpanLabel lblDeedCatName = new SpanLabel(dc.getDeedCatName());
				SpanLabel lblPointAssigned = new SpanLabel("" 
															+ deedOwner.getDeedArray().get(i).getAssignedWeighting() 
															+ " points");
				// adding to 1st container
				Container cntSingleDeed = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	        	cntSingleDeed.add(lblAchCatName)
	        				 .add(lblDeedCatName)
	        				 .add(lblPointAssigned);
	        				 
				if(deedOwner.getDeedArray().get(i).getProofLink().equals("No proof uploaded")) { // no proof
					SpanLabel lblProof = new SpanLabel(deedOwner.getDeedArray().get(i).getProofLink(), "NoProofLabel");
					lblProof.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
					lblProof.getTextAllStyles().setFgColor(0x6C6E70);
					// adding to 1st container
					cntSingleDeed.add(lblProof);
				} else { // there is proof
					String link = deedOwner.getDeedArray().get(i).getProofLink();
					Button btnProof = new Button(link, "Label");
					// following changes the style of a hyperlink to look like a hyerlink
					btnProof.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
					btnProof.getUnselectedStyle().setUnderline(true);
					btnProof.getUnselectedStyle().setFgColor(0x0645AD);
					btnProof.addActionListener(e -> {
						Display.getInstance().execute(link);
					});
					// adding to 1st container
		        	cntSingleDeed.add(btnProof);
				}
				
				Container cntDeedHeading = new Container(new BorderLayout());
				// adding to 2nd container
				cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));

		        Deed tempDeed = deedOwner.getDeedArray().get(i);
				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				if(bool == true) { 
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = deedOwner;
					btnUsername.addActionListener(e -> {
						new Profile(user).show();
					});
					
					Button btnDelete = new Button();
					btnDelete.setUIID("DeedMoreButton");
					// set button icon to a trash can symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue872", f, 0x000000, 50, 50);
			        btnDelete.setIcon(fi);
		        	btnDelete.addActionListener(e ->{
			        	deleteDeed(tempDeed.getDeedID());
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
			        			  .add(BorderLayout.EAST, btnDelete);
				} else if(u.isAccType() == true) {
					// below declared to avoid final error in actionListener
					User owner = deedOwner;
					User user = u;
					btnUsername.addActionListener(e -> {
						new Profile(user, owner).show();
					});
					
					Button btnDelete = new Button();
					btnDelete.setUIID("DeedMoreButton");
					// set button icon to a trash can symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue872", f, 0x000000, 50, 50);
			        btnDelete.setIcon(fi);
		        	btnDelete.addActionListener(e ->{
			        	deleteDeed(tempDeed.getDeedID());
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
			        			  .add(BorderLayout.EAST, btnDelete);
				} else {
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = u;
					User owner = deedOwner;
					btnUsername.addActionListener(e -> {
						new Profile(user, owner).show();
					});
					
					Button btnReport = new Button();
					btnReport.setUIID("DeedMoreButton");
					// set button icon to a report symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue160", f, 0x000000, 50, 50);
			        btnReport.setIcon(fi);
			        btnReport.addActionListener(e ->{
			        	reportDeed(tempDeed.getDeedID(), user, owner);
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
	  			  				  .add(BorderLayout.EAST, btnReport);
				}
				
	        	Container cntVotes = new Container(new BoxLayout(BoxLayout.Y_AXIS));
				SpanLabel lblPoints = new SpanLabel(Integer.toString(deedOwner.getDeedArray().get(i).getPoints()));
				Button btnUpvote = new Button();
				
				// sets button icon to up or down arrow
				Font f = FontImage.getMaterialDesignFont();
		        FontImage fi = FontImage.createFixed("\ue5d8", f, 0x000000, 50, 50); // up arrow icon
		        btnUpvote.setIcon(fi);
				Button btnDownvote = new Button();
				fi = FontImage.createFixed("\ue5db", f, 0x000000, 50, 50); // down arrow icon
				btnDownvote.setIcon(fi);
				btnUpvote.addActionListener(e -> {
					int result = upvoteDeed(user,dd);
					//dd.setPoints(1);
					//dd.setPoints(dd.getPoints() + 1);
					if(Integer.parseInt(lblPoints.getText())==result)
					{
						
						Dialog.show(null,"You Can Upvote Only Once!","OK",null);
						
					}
					else
					{

						//btnDownvote.setEnabled(false);
						lblPoints.setText(Integer.toString(result));
					}
					
					//}	 
				});
				btnDownvote.addActionListener(e -> {
					int result = downvoteDeed(user,dd);
					//dd.setPoints(0);
					//dd.setPoints(dd.getPoints()- 1);
					if(Integer.parseInt(lblPoints.getText())==result)
					{
						Dialog.show(null,"You Can Downvote Only Once!","OK",null);
						
					}
					else
					{
					lblPoints.setText(Integer.toString(result));
					}
					//Dialog.show(null,"You Can Downvote Only Once!","OK",null);
	
					
				});
				// adding to 3rd container
	        	cntVotes.add(btnUpvote).add(lblPoints).add(btnDownvote);
	        	
	        	Container cntCombined;
	        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
	        	if(deedCount % 2 == 0) { // even
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
	            	// adding to 4th container
	        		cntCombined.add(cntVotes).add(cntSingleDeed);
	            	deedCount += 1;
	        	} else { // odd
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
	        		// adding to 4th container
	        		cntCombined.add(cntVotes).add(cntSingleDeed);
	            	deedCount += 1;
	        	}
	        	// adding to outermost container
	        	cntDeeds.add(cntDeedHeading).add(cntCombined);
			}
		}
		return cntDeeds; // return outermost container
	}
	
	
	
public Container getMultiUserDeeds(User deedOwner, User u, boolean bool, String type) {
		
		int deedCount = 0;
		Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		DeedCategory dc = new DeedCategory();
		AchievementCategory ac = new AchievementCategory();
    	
		deedOwner = dbHandler.getMultipleUserDeeds(deedOwner);
		
		ArrayList<Deed> deeds  = deedOwner.getDeedArray();
		
		//sort
		
		if(type != null && type.equals("id")) {
			Collections.sort(deeds,new Comparator<Deed>() {

				@Override
				public int compare(Deed object1, Deed object2) {
					return Integer.compare(object2.getDeedID(), object1.getDeedID());
				}
			});
		}
		
		if(type != null && type.equals("vote")) {
			Collections.sort(deeds,new Comparator<Deed>() {

				@Override
				public int compare(Deed object1, Deed object2) {
					return Integer.compare(object2.getPoints(), object1.getPoints());
				}
			});
		}
		 
		
		deedOwner.setDeedArray(deeds);
		
		if(deedOwner.getDeedArray().size() == 0) { // no deeds
			SpanLabel lblNone = new SpanLabel("No recorded Deeds");
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			// adding to outermost container
			cntDeeds.add(lblNone);
		} else { // one or more deeds
			for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
				Deed dd= deedOwner.getDeedArray().get(i);
				dc = dbHandler.getDeedCat(deedOwner.getDeedArray().get(i).getDeedCatID());
				ac = dbHandler.getAchievementCat(dc);
				
				SpanLabel lblAchCatName = new SpanLabel(ac.getAchieveCatName());
				SpanLabel lblDeedCatName = new SpanLabel(dc.getDeedCatName());
				SpanLabel lblPointAssigned = new SpanLabel("" 
															+ deedOwner.getDeedArray().get(i).getAssignedWeighting() 
															+ " points");
				// adding to 1st container
				Container cntSingleDeed = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	        	cntSingleDeed.add(lblAchCatName)
	        				 .add(lblDeedCatName)
	        				 .add(lblPointAssigned);
	        				 
				if(deedOwner.getDeedArray().get(i).getProofLink().equals("No proof uploaded")) { // no proof
					SpanLabel lblProof = new SpanLabel(deedOwner.getDeedArray().get(i).getProofLink(), "NoProofLabel");
					lblProof.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
					lblProof.getTextAllStyles().setFgColor(0x6C6E70);
					// adding to 1st container
					cntSingleDeed.add(lblProof);
				} else { // there is proof
					String link = deedOwner.getDeedArray().get(i).getProofLink();
					Button btnProof = new Button(link, "Label");
					// following changes the style of a hyperlink to look like a hyerlink
					btnProof.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
					btnProof.getUnselectedStyle().setUnderline(true);
					btnProof.getUnselectedStyle().setFgColor(0x0645AD);
					btnProof.addActionListener(e -> {
						Display.getInstance().execute(link);
					});
					// adding to 1st container
		        	cntSingleDeed.add(btnProof);
				}
				
				Container cntDeedHeading = new Container(new BorderLayout());
				// adding to 2nd container
				cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));

		        Deed tempDeed = deedOwner.getDeedArray().get(i);
				Button btnUsername = new Button(dbHandler.getUserNameByUser(tempDeed.getUserID()), "Label");
				if(bool == true) { 
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = deedOwner;
					btnUsername.addActionListener(e -> {
						new Profile(user,dbHandler.searchForUserById(tempDeed.getUserID())).show();
					});
					
					Button btnDelete = new Button();
					btnDelete.setUIID("DeedMoreButton");
					// set button icon to a trash can symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue872", f, 0x000000, 50, 50);
			        btnDelete.setIcon(fi);
		        	btnDelete.addActionListener(e ->{
			        	deleteDeed(tempDeed.getDeedID());
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
			        			  .add(BorderLayout.EAST, btnDelete);
				} else if(u.isAccType() == true) {
					// below declared to avoid final error in actionListener
					User owner = deedOwner;
					User user = u;
					btnUsername.addActionListener(e -> {
						new Profile(user, owner).show();
					});
					
					Button btnDelete = new Button();
					btnDelete.setUIID("DeedMoreButton");
					// set button icon to a trash can symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue872", f, 0x000000, 50, 50);
			        btnDelete.setIcon(fi);
		        	btnDelete.addActionListener(e ->{
			        	deleteDeed(tempDeed.getDeedID());
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
			        			  .add(BorderLayout.EAST, btnDelete);
				} else {
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = u;
					User owner = deedOwner;
					btnUsername.addActionListener(e -> {
						new Profile(user, owner).show();
					});
					
					Button btnReport = new Button();
					btnReport.setUIID("DeedMoreButton");
					// set button icon to a report symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue160", f, 0x000000, 50, 50);
			        btnReport.setIcon(fi);
			        btnReport.addActionListener(e ->{
			        	reportDeed(tempDeed.getDeedID(), user, owner);
			        });
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername)
	  			  				  .add(BorderLayout.EAST, btnReport);
				}
				
	        	Container cntVotes = new Container(new BoxLayout(BoxLayout.Y_AXIS));
				SpanLabel lblPoints = new SpanLabel(Integer.toString(deedOwner.getDeedArray().get(i).getPoints()));
				Button btnUpvote = new Button();
				
				// sets button icon to up or down arrow
				Font f = FontImage.getMaterialDesignFont();
		        FontImage fi = FontImage.createFixed("\ue5d8", f, 0x000000, 50, 50); // up arrow icon
		        btnUpvote.setIcon(fi);
				Button btnDownvote = new Button();
				fi = FontImage.createFixed("\ue5db", f, 0x000000, 50, 50); // down arrow icon
				btnDownvote.setIcon(fi);
				btnUpvote.addActionListener(e -> {
					int result = upvoteDeed(user,dd);
					//dd.setPoints(1);
					//dd.setPoints(dd.getPoints() + 1);
					if(Integer.parseInt(lblPoints.getText())==result)
					{
						Dialog.show(null,"You Can Upvote Only Once!","OK",null);
						
					}
					else
					{
						lblPoints.setText(Integer.toString(result));
					}
					
					//}	 
				});
				btnDownvote.addActionListener(e -> {
					int result = downvoteDeed(user,dd);
					//dd.setPoints(0);
					//dd.setPoints(dd.getPoints()- 1);
					if(Integer.parseInt(lblPoints.getText())==result)
					{
						Dialog.show(null,"You Can Downvote Only Once!","OK",null);
						
					}
					else
					{
					lblPoints.setText(Integer.toString(result));
					}
					//Dialog.show(null,"You Can Downvote Only Once!","OK",null);
	
					
				});
				// adding to 3rd container
	        	cntVotes.add(btnUpvote).add(lblPoints).add(btnDownvote);
	        	
	        	Container cntCombined;
	        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
	        	if(deedCount % 2 == 0) { // even
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
	            	// adding to 4th container
	        		cntCombined.add(cntVotes).add(cntSingleDeed);
	            	deedCount += 1;
	        	} else { // odd
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
	        		// adding to 4th container
	        		cntCombined.add(cntVotes).add(cntSingleDeed);
	            	deedCount += 1;
	        	}
	        	// adding to outermost container
	        	cntDeeds.add(cntDeedHeading).add(cntCombined);
			}
		}
		return cntDeeds; // return outermost container
	}
	
/**
 * below methods to show accomplished deeds from specific deed types. 
 * @author Chinmay
 *
 */
	
public Container getCleaningDeeds(User deedOwner, User u, boolean bool) {
		
		int deedCount = 0;
		Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
		Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		
		deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 1);
		if(deedOwner.getDeedArray().size() == 0) { // no deeds
			SpanLabel lblNone = new SpanLabel("No Deeds");
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			// adding to outermost container
			cntDeeds.add(lblNone);
		} else { // one or more deeds
			for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
				Deed dd= deedOwner.getDeedArray().get(i);
				
				Container cntDeedHeading = new Container(new BorderLayout());
				// adding to 2nd container
				cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
				
		        Deed tempDeed = deedOwner.getDeedArray().get(i);
				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				if(bool == true) { 
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = deedOwner;
					
					
					
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
			        			 
				} else if(u.isAccType() == true) {
					// below declared to avoid final error in actionListener
					User owner = deedOwner;
					User user = u;
				
					
					
			        // adding to 2nd container
			        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
			        cntDeedHeading.add(BorderLayout.NORTH,kt);
			        		
				} else {
//					Button btnUsername = new Button(deedOwner.getUsername(), "Label");
					// below declared to avoid final error in actionListener
					User user = u;
					User owner = deedOwner;
				
					
					
				}
				
	        	
	        	
	        	Container cntCombined;
	        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
	        	if(deedCount % 2 == 0) { // even
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
	            	// adding to 4th container
	        	
	            	deedCount += 1;
	        	} else { // odd
	        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
	        		// adding to 4th container
	        	
	            	deedCount += 1;
	        	}
	        	// adding to outermost container
	        	cntDeeds.add(cntDeedHeading).add(cntCombined);
			}
		}
		return cntDeeds; // return outermost container
	}



public Container getCommunityDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 2);
	
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No blah blah Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));

	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        		//cntCombined.add(cntSingleDeed);
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        		//cntCombined.add(cntSingleDeed);
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}



public Container getDisabilityDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 3);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}


public Container getElderlyDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 4);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getFlatmatesDDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 5);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getFlatmatesPDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 6);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getFlatmatesLDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 7);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}






public Container getFriendDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 8);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getHomelessDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 9);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}


public Container getKidsDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 10);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}




public Container getPetADeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 11);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getPetCDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 12);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getPetTDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 13);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}


public Container getRecyclingDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 14);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getVolDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 15);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}

public Container getWatDeeds(User deedOwner, User u, boolean bool) {
	
	int deedCount = 0;
	//Label kt = new Label(Integer.toString(deed.getAssignedWeighting()));
	Container cntDeeds = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	
	deedOwner = dbHandler.getDeedsByCategoryAndUser(deedOwner, 16);
	if(deedOwner.getDeedArray().size() == 0) { // no deeds
		SpanLabel lblNone = new SpanLabel("No Deeds");
		lblNone.getTextAllStyles().setFgColor(0x6C6E70);
		// adding to outermost container
		cntDeeds.add(lblNone);
	} else { // one or more deeds
		for(int i = 0; i < deedOwner.getDeedArray().size(); i++) {
			Deed dd= deedOwner.getDeedArray().get(i);
			
			Container cntDeedHeading = new Container(new BorderLayout());
			// adding to 2nd container
			cntDeedHeading.add(BorderLayout.WEST, new SpanLabel(deedOwner.getDeedArray().get(i).getDeedName()));
			
	        Deed tempDeed = deedOwner.getDeedArray().get(i);
			Button btnUsername = new Button(deedOwner.getUsername(), "Label");
			if(bool == true) { 
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = deedOwner;
				
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        			 
			} else if(u.isAccType() == true) {
				// below declared to avoid final error in actionListener
				User owner = deedOwner;
				User user = u;
			
				
				
		        // adding to 2nd container
		        cntDeedHeading.add(BorderLayout.NORTH, btnUsername);
		        //cntDeedHeading.add(BorderLayout.NORTH,kt);
		        		
			} else {
//				Button btnUsername = new Button(deedOwner.getUsername(), "Label");
				// below declared to avoid final error in actionListener
				User user = u;
				User owner = deedOwner;
			
				
				
			}
			
        	
        	
        	Container cntCombined;
        	// determines odd or even Deed number, changes the colour of the line underneath a Deed
        	if(deedCount % 2 == 0) { // even
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont"); // blue line
            	// adding to 4th container
        	
            	deedCount += 1;
        	} else { // odd
        		cntCombined = new Container(new BoxLayout(BoxLayout.X_AXIS), "SingleDeedCont2"); // yellow line
        		// adding to 4th container
        	
            	deedCount += 1;
        	}
        	// adding to outermost container
        	cntDeeds.add(cntDeedHeading).add(cntCombined);
		}
	}
	return cntDeeds; // return outermost container
}
	
	/**
	 * Retrieves all achievements a user has gained and lumps them together in a container that is
	 * stored in a single accordion.
	 * @author leish
	 * @param u user object
	 * @return accordion with all achievements
	 */
	public Accordion toggleAchievements(User u) {
		
		Accordion accrAchievements = new Accordion();
	 	accrAchievements.setScrollableY(false);
		Container cntAchievements = new Container(new GridLayout(2,2));
    	
		u = getAchievements(u);
		
		if(u.getAchievementArray().size() == 0) { // no achievements
			SpanLabel lblNone = new SpanLabel("No recorded Achievements");
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			Label lblAchieveTitle = new Label("Achievements");
			lblAchieveTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
			accrAchievements.addContent(lblAchieveTitle, lblNone);
		} else { // one or more achievements
			for(int i = 0; i < u.getAchievementArray().size(); i++) {
				// following sets the achievement icon
				Image imgAchievement = theme.getImage(u.getAchievementArray().get(i).getIcon());
	        	Image resizedImage = imgAchievement.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 3));	  
	        	Label lblAchieve = new Label(resizedImage);
	        	
	        	SpanLabel lblAchievementName = new SpanLabel(u.getAchievementArray().get(i).getAchievementName());
	        	// add name and icon to container
	        	Container cntAchieve2 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	        	cntAchieve2.add(lblAchieve).add(lblAchievementName);
	        	// add this loop's achievement to outer container
	        	cntAchievements.add(cntAchieve2);
			}
			// following creates and stores all achievements in a single accordion
			Label lblAchieveTitle = new Label("Achievements", "ProfileTitles");
			lblAchieveTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
			accrAchievements.addContent(lblAchieveTitle, cntAchievements);
		}
		return accrAchievements;
	}
	
	// ASSOCIATE METHODS ----------------------------------------------------------------------------------
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void addAssociate() {
		// TODO
		Dialog.show(null, "Adding an associate doesn't work yet.\nPlease hold while we fix this.", 
				"OK", null);
	}
	
	/**
	 * @author Leish
	 * @param associatesOwner user object of the page being viewed
	 * @param u user object if visiting another profile, null if owns the viewed profile
	 * @param bool true if the user owns the viewed profile, false if the user is visiting
	 * 			another profile
	 * 
	 * @return accordion filled with associate objects
	 */
	public Accordion toogleAssociates(User associatesOwner, User u, boolean bool) {
		Accordion accrAssociates = new Accordion();
		accrAssociates.setScrollableY(false);
    	Container cntSingleAssociate = new Container(new BoxLayout(BoxLayout.Y_AXIS));
    	
    	associatesOwner = dbHandler.getAssociates(associatesOwner);
		
		if(associatesOwner.getAssociateArray().size() == 0) { // no associates
			SpanLabel lblNone = new SpanLabel("No recorded Associates");
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			SpanLabel lblNoneTitle = new SpanLabel("Associates");
			lblNoneTitle.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
			accrAssociates.addContent(lblNoneTitle, lblNone);
		} else { // one or more associates
			for(int i = 0; i < associatesOwner.getAssociateArray().size(); i++) {
	        	Button btnUsername = new Button(associatesOwner.getAssociateArray().get(i).getUsername(), "Label");
	        	if(bool == true) { // own profile
		        	// user objects declared to avoid final error in actionListener
	        		User user = associatesOwner;
					User owner = searchForUser(associatesOwner.getAssociateArray().get(i).getUsername()); 
					btnUsername.addActionListener(e -> {
						new Profile(user, owner).show();
					});
	        	} else { // visiting profile
		        	// user objects declared to avoid final error in actionListener
					User owner = searchForUser(associatesOwner.getAssociateArray().get(i).getUsername()); 
					btnUsername.addActionListener(e -> {
						new Profile(u, owner).show();
					});
	        	}
				
	        	SpanLabel lblKudos = new SpanLabel(Integer.toString(associatesOwner.getAssociateArray().get(i).getKudosTotal()) + " kudos");
	        	lblKudos.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_MEDIUM));
	        	SpanLabel lblRelationship = new SpanLabel(associatesOwner.getAssociateArray().get(i).getRelationship());
	        	
	        	Container cntAssociate = new Container(new BoxLayout(BoxLayout.Y_AXIS));
	        	// adding associate info to container
	        	cntAssociate.add(btnUsername)
	        				.add(lblKudos)
	        				.add(lblRelationship);
	        	// associate's profile picture
	        	Image imgUserDP = theme.getImage(associatesOwner.getAssociateArray().get(i).getProfilePicture());
	        	Image resizedImage = imgUserDP.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 4));
	        	Label lblDP = new Label();
	        	lblDP.setIcon(resizedImage);

	        	Container cntAssociate2 = new Container(new BoxLayout(BoxLayout.X_AXIS));
	        	cntAssociate2.add(lblDP)
	        				 .add(cntAssociate);
	        	
	        	cntSingleAssociate.add(cntAssociate2);
			}
			
			Label lblAssociateTitle = new Label("Associates", "ProfileTitles");
			lblAssociateTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));			
			accrAssociates.addContent(lblAssociateTitle, cntSingleAssociate);
		}
		return accrAssociates;
	}
	
	// GROUP & EVENT METHODS ------------------------------------------------------------------------------
	/**
	 * @author kurtg
	 * @param gname
	 * @param gtype
	 * @param user 
	 * @return 
	 */
	public User createGroup(String gname, String gtype, User user) {
		user = dbHandler.createGroupDB(gname, gtype, user);
		return user;
	}
	
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void deleteGroup() {
		// TODO
		Dialog.show(null, "Deleting a group doesn't work yet.\nPlease hold while we fix this.", 
				"OK", null);
	}
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void leaveGroup() {
		// TODO
		Dialog.show(null, "Leaving a group doesn't work yet.\nPlease hold while we fix this.", 
				"OK", null);
	}
	
	/**
	 * @author kurtg
	 * @param groupID
	 * @return group
	 */
	public Group getGroup(int groupID) {
		this.group = dbHandler.getGroupDB(groupID);
		return this.group;
		
	}
	
	/**
	 * @author kurtg
	 * @param addedUser
	 * @param group
	 */
	public void addGroupMember(User addedUser, Group group) {
		dbHandler.addGroupMemberDB(addedUser, group);
	}
	
	/**
	 * Placeholder function; only contains a prompt explaining that the function doesn't do anything.
	 */
	public void deleteEvent() {
		// TODO
		Dialog.show(null, "Delete event function doesn't exist yet.\nPlease hold while we fix this.", 
				"OK", null);
	}

	/**
	 * Calls database equivalent function and reloads the group page.
	 * @author leish
	 * @param eID event id for the reported event
	 * @param u user object for logged in user
	 * @param g group object for the group page visited
	 */
	public void reportEvent(int eID, User u, Group g) {
		dbHandler.reportEvent(eID);
		Dialog.show(null, "You have reported this event.", "OK", null);
		
		g = dbHandler.getGroupDB(g.getGroupID());
		new GroupPage(u, g).show();
	}
	
	/**
	 * @author kurtg
	 * @param user
	 * @param group
	 * @return
	 */
	public Accordion showGroupEvents(User user, Group group) {
		Accordion accrEvents = new Accordion();
		accrEvents.setScrollableY(false);
		Container cntEvents = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		Container cntEventSingle = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		
		if(group.getGroupEventArray().size() == 0) { // no events
			SpanLabel lblNone = new SpanLabel("No recorded Events for this group");			
			lblNone.getTextAllStyles().setFgColor(0x6C6E70);
			SpanLabel lblNoneTitle = new SpanLabel("Events");
			lblNoneTitle.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
			accrEvents.addContent(lblNoneTitle, lblNone);
		} else { // one or more events
			for(int i = 0; i < group.getGroupEventArray().size(); i++) {
				
				int eventID = group.getGroupEventArraySingleIndex(i); 
				this.event = dbHandler.searchEvent(eventID);
				ArrayList<Integer> eventCompetitorsArray = new ArrayList<>(event.getEventCompetitorsArray());
				String eventEndDate = new SimpleDateFormat("dd/MM/yyy").format(event.getEventEnd());
				
				Button btnEventName = new Button(event.getEventName(), "Label");
				
				Label lblEventEnd = new Label("End Date: " + eventEndDate);
				lblEventEnd.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
				
				btnEventName.addActionListener(e -> {
					if(eventCompetitorsArray.contains(user.getUserID())) {
						// ask if user would like to drop out of event
						if(Dialog.show(null,
				    			"Would you like to drop out of this event?", 
				    			"No", "Yes")) { 
							// No
							//Do nothing
						} else { 
							// Yes
							dbHandler.deleteEventCompetitor(user, eventID);
							new GroupPage(user, group).show();
							}
					} else {
						// ask user if they would like to join event
						if(Dialog.show(null,
				    			"Would you like to join of this event?", 
				    			"No", "Yes")) { 
							// No
							//Do nothing
						} else { 
							// Yes
							dbHandler.addEventCompetitor(user, eventID);
							new GroupPage(user, group).show();
						}
					}

	        	});
				
				Container cntEventHeading = new Container(new BorderLayout());
				cntEventHeading.add(BorderLayout.WEST, btnEventName);
				if(group.getGroupOwner() == user.getUserID() || user.isAccType() == true) {
					Button btnDelete = new Button();
					btnDelete.setUIID("DeedMoreButton");
					// set button icon to a trash can symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue872", f, 0x000000, 50, 50);
			        btnDelete.setIcon(fi);
			        btnDelete.addActionListener(e ->{
			        	deleteEvent();
			        });
			        // adding to 2nd container
			        cntEventHeading.add(BorderLayout.EAST, btnDelete);
				} else {
					Button btnReport = new Button();
					btnReport.setUIID("DeedMoreButton");
					// set button icon to a report symbol
			        Font f = FontImage.getMaterialDesignFont();
			        FontImage fi = FontImage.createFixed("\ue160", f, 0x000000, 50, 50);
			        btnReport.setIcon(fi);
			        btnReport.addActionListener(e ->{
			        	reportEvent(eventID, user, group);
			        });
			        // adding to 2nd container
			        cntEventHeading.add(BorderLayout.EAST, btnReport);
				}				
				cntEventSingle.add(cntEventHeading).add(lblEventEnd);

				Container cntEventCompetitors = new Container(new GridLayout(2,2));
				
				for(int j = 0; j < event.getEventCompetitorsArray().size(); j++) {
					int userID = event.getEventCompetitorsArraySingleIndex(j);
					this.owner = dbHandler.searchForUserDB(userID, "");
					int competitorsKudos = owner.getKudosTotal() - dbHandler.getCompetitorsKudos(userID, eventID);

					SpanLabel lblCompetitor = new SpanLabel(owner.getUsername() + ": " + competitorsKudos + " kudos");
					lblCompetitor.getTextAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_MEDIUM));
					
					cntEventCompetitors.add(lblCompetitor);
					
					// the following adds labels to keep formatting within a grid array
					// only run on the last loop of the j for-loop
					if(j + 1 == event.getEventCompetitorsArray().size()) {
						if(event.getEventCompetitorsArray().size() % 4 == 0) { // even
							cntEventCompetitors.add(new Label(" "));
						} else { // odd	
							cntEventCompetitors.add(new Label(" ")).add(new Label(" "));
						}
					}
				}
				cntEventSingle.add(cntEventCompetitors);
			}
			cntEvents.add(cntEventSingle);
			
			Label lblEventsTitle = new Label("Events", "ProfileTitles");
			lblEventsTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));			
			accrEvents.addContent(lblEventsTitle, cntEvents);
		}
		
		return accrEvents;
	}
	
	/**
	 * @author kurtg
	 * @param group
	 * @param eventName
	 * @param selDate
	 * @param curDate
	 * @return 
	 */
	public Group createGroupEvent(Group group, String eventName, String selDate, String curDate) {
		group = dbHandler.createGroupEventDB(group, eventName, selDate, curDate);
		
		return group;
	}
	
	/**
	 * @author kurtg
	 * @param user
	 * @return
	 */
	public Accordion showGroups(User user) {
		Accordion accrGroups = new Accordion();
		accrGroups.setScrollableY(false);
		Container cntGroups = new Container(BoxLayout.y());
		
		Button btnAddGroup = new Button("+ Group");
        btnAddGroup.addActionListener(e ->{
     		new CreateGroup(user).show();
        });
        cntGroups.add(btnAddGroup);
        
		if(user.getGroupArray().size() < 1) {
			SpanLabel lblNoGroups = new SpanLabel("You are not a member of any groups");
			lblNoGroups.getTextAllStyles().setFgColor(0x6C6E70);
			cntGroups.add(lblNoGroups);
		} else {
			for(int i = 0; i < user.getGroupArray().size(); i++) {
				int groupID = user.getGroupArraySingleIndex(i);
				this.group = getGroup(groupID);
				
				Button btnGroupName = new Button(this.group.getGroupName(), "Label");
				
				btnGroupName.addActionListener(e -> {
					new GroupPage(user, this.group = getGroup(groupID)).show();
	        	});
				cntGroups.add(btnGroupName);
				}
			}
		
		Label lblGroupTitle = new Label("Groups");
		lblGroupTitle.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		accrGroups.addContent(lblGroupTitle, cntGroups);
		
		return accrGroups;
	}
	
	/**
	 * @author kurtg
	 * @param group
	 * @return
	 */
	public Accordion showGroupMembers(User u, Group group) {
		Accordion accrGroupMembers = new Accordion();
		accrGroupMembers.setScrollableY(false);
		Container cntGroupMembers = new Container(BoxLayout.y());
	
		group = dbHandler.getGroupDB(group.getGroupID());
		
		if(group.getGroupMemberArray().size() == 0) {
			SpanLabel lblNoMembers = new SpanLabel("No members recorded in this group");
			lblNoMembers.getTextAllStyles().setFgColor(0x6C6E70);
			cntGroupMembers.add(lblNoMembers);
		} else {
			for(int i = 0; i < group.getGroupMemberArray().size(); i++) {
				Container cntGroupMemberSingle = new Container(BoxLayout.x());
				
				int memberID = group.getGroupMemberArraySingleIndex(i);
	        	this.owner = searchForUser(memberID);
	        	
	        	Image imgUserDP = theme.getImage(this.owner.getProfilePicture());
				Image resizedImage = imgUserDP.scaledWidth(Math.round(Display.getInstance().getDisplayWidth() / 5));
				Label lblResizedImage = new Label(resizedImage);
				
				Button btnGroupMemberName = new Button(this.owner.getUsername(), "Label");
				Label lblMemberKudos = new Label(String.valueOf(this.owner.getKudosTotal()) + " kudos");
				lblMemberKudos.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_MEDIUM));
				Container cntGroupMemberInfo = new Container(BoxLayout.y());
				cntGroupMemberInfo.add(btnGroupMemberName).add(lblMemberKudos);

				
				btnGroupMemberName.addActionListener(e -> {
					new Profile(u, this.owner = searchForUser(memberID)).show();
	        	});
				cntGroupMemberSingle.add(lblResizedImage).add(cntGroupMemberInfo);
				cntGroupMembers.add(cntGroupMemberSingle);

			}
		}
		Label lblMembers = new Label("Members", "ProfileTitles");
		lblMembers.getUnselectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		accrGroupMembers.addContent(lblMembers, cntGroupMembers);
		
		return accrGroupMembers;
	}
	
	// BASE64 ENCRYPTION/DECRYPTION METHODS ----------------------------------------------
	/**
	 * Uses Base64 to encode strings. 
	 * 
	 * @author leish
	 * @param s string to encode
	 * @return encoded string
	 */
	public String encrypt(String s) {
		
		String encryptedString = secret(s);
    	byte[] byteArr = encryptedString.getBytes();
    	String encodedEncryptedString = Base64.encode(byteArr);
    	
    	return encodedEncryptedString;
	}
	
	/**
	 * Uses Base64 to decode strings.
	 * 
	 * @author leish
	 * @param s string to decode
	 * @return decoded string
	 */
	public String decrypt(String s) {
		
    	byte[] byteArr = Base64.decode(s.getBytes());
        String decryptedString = new String(byteArr);
        String decodedDecryptedString = removeSecret(decryptedString); 
        	
        return decodedDecryptedString;
	}
	
	/**
	 * Uses a key to encrypt.
	 * 
	 * @author Leish
	 * @param s
	 * @return
	 */
	private String secret(String s) {
		
		char[] charstr = s.toCharArray();
		String encrypted = "";
		
		int i = 0;
		for(char c : charstr) {
			
			int[] key = {7,3};
			if(i % 2 == 0) {
				c += key[0];
				encrypted += c;
			} else {
				c += key[1];
				encrypted += c;
			}
			i++;
			key = null;
		}
		
		return encrypted;
	}
	/**
	 * Uses a key to decrypt.
	 * @author Leish
	 * @param s
	 * @return
	 */
	private String removeSecret(String s) {
		
		char[] charstr = s.toCharArray();
		String decrypted = "";
		int i = 0;
		for(char c : charstr) {
			
			int[] key = {7,3};
			if(i % 2 == 0) {
				c -= key[0];
				decrypted += c;
			} else {
				c -= key[1];
				decrypted += c;
			}
			i++;
			key = null;
		}
		
		return decrypted;
	}
	
	// Input Validation Methods ----------------------------------------------------------
	/**
	 * Takes an input to find that @ symbol.
	 * 
	 * @author leish
	 * @param username
	 * @return true = valid, false = invalid
	 */
	public boolean validUsername(String username) {
		
		if(username.contains("@")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Takes an input to ensure that "weltec.ac.nz" is located within it. 
	 * This is important for beta testing to remain within WelTec.
	 * 
	 * @author leish
	 * @param email
	 * @return true = valid, false = invalid
	 */
	public boolean validEmail(String email) {
		
		if(email.toLowerCase().contains("weltec.ac.nz")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Takes two inputs to ensure that two strings are the same.
	 * 
	 * @author leish
	 * @param password
	 * @param password2
	 * @return true = valid, false = invalid
	 */
	public boolean matchPassword(String password, String password2) {
		
		if(password.equals(password2)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Takes an input to ensure it contains something.
	 * 
	 * @author leish
	 * @param s
	 * @return  true = valid, false = invalid
	 */
	public boolean validInput(String s) {
		
		// for no entry
		if(s.equals("")) {
			return false;	
		} else {
			// for default entries#1
			if(s.equals("please select option")) {
				return false;
			} else {
				// for default entries#2
				if(s.equals("please select occupation")) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
	
	/**
	 * Takes an input to ensure it is a unique entry. Calls DBHandler to search the database for a matching username.
	 * 
	 * @author leish
	 * @param username
	 * @return  true = valid, false = invalid
	 */
	public boolean uniqueUsername(String username) {
		
		boolean unique = false;
		unique = dbHandler.uniqueUsername(username);
		
		return unique; 
	}
	
	/**
	 * Takes an input to ensure it is a unique entry. Calls DBHandler to search the database for a matching email.
	 * 
	 * @author leish
	 * @param email
	 * @return  true = valid, false = invalid
	 */
	public boolean uniqueEmail(String email) {
		
		boolean unique = false;
		unique = dbHandler.uniqueEmail(email);
		
		return unique; 
	}
	public int getLatestKudos(int userID) {
		return dbHandler.getKudosByUser(userID);
	}
}
