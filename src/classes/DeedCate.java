package classes;

import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import classes.User;

import com.byteme.gdgapp.Cleaning;
import com.byteme.gdgapp.Community;
import com.byteme.gdgapp.Disability;
import com.byteme.gdgapp.Elderly;
import com.byteme.gdgapp.FlatmatesD;
import com.byteme.gdgapp.Friend;
import com.byteme.gdgapp.Homeless;
import com.byteme.gdgapp.Kids;
import com.byteme.gdgapp.PetA;
import com.byteme.gdgapp.PostDeed;
import com.byteme.gdgapp.Recycling;
import com.byteme.gdgapp.Volunteer;
import com.byteme.gdgapp.WaterCon;

import java.sql.Connection;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import classes.Deed;
import classes.DeedCate;
import classes.GUIHandler;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import classes.User;
import com.byteme.gdgapp.PostDeed;

import java.sql.Connection;

import com.codename1.components.Accordion;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import classes.Deed;
import classes.DeedCate;
import classes.GUIHandler;
/**
 * This is a class for user to select deed category.
 * @author Chinmay
 *
 */
public class DeedCate extends com.codename1.ui.Form {
	private Resources theme;
	private Achievement ach=new Achievement();
	private AchievementCategory achcat=new AchievementCategory();
	private PostDeed pdeed;
	private DeedCategory dcat=new DeedCategory();
	private GUIHandler guiHandler;
	private User user = new User();
	private User owner = new User();
	private Deed did = new Deed();
	public String s1;
	//private GUIHandler guihandler = new GUIHandler();
	private Connection conn = null;
	private String url;
	private boolean autoClose = true;
	Label l2 = new Label("Enter Type of Deed");
	Button back = new Button("Back");

	Button sub = new Button("Submit");
	Button deedtype=new Button("Select Deed Type");
	Button res = new Button("Reset");
	Container c = new Container((new GridLayout(1, 3)));
	Picker pall = new Picker();

	// c.setLayout(new FlowLayout(FlowLayout.CENTER));

	public DeedCate() {
		this(com.codename1.ui.util.Resources.getGlobalResources());
	}

	public DeedCate(com.codename1.ui.util.Resources resourceObjectInstance) {
		initGuiBuilderComponents(resourceObjectInstance);

		this.theme = resourceObjectInstance;
		this.guiHandler = new GUIHandler(theme);
	}

	public DeedCate(User user) {
		this(com.codename1.ui.util.Resources.getGlobalResources());

		this.guiHandler = new GUIHandler(theme);
		this.user = user;

		// createForm();
	}

	// METHODS
	// ---------------------------------------------------------------------------
	/**
	 * The method that contains all the GUI information
	 */
	public void createForm() {

		Toolbar tb = this.getToolbar();
		guiHandler.menu(tb, this.user);

		// default code from CN1
		TableLayout tl;
		int spanButton = 2;
		if (Display.getInstance().isTablet()) {
			tl = new TableLayout(14, 1);
		} else {
			tl = new TableLayout(14, 1);
			spanButton = 1;
		}
		tl.setGrowHorizontally(true);
		this.setLayout(tl);
		
		pall.setType(Display.PICKER_TYPE_STRINGS);
		pall.setStrings(
				"Cleaning",
				 "Community",
				 "Disability",
				 "Elderly",
				 "Flatmates",
				 "Friend",
				 "Homeless",
				 "Kids",
				 "Pet",
				 "Recycling",
				 "Volunteer",
				 "Water Conservation"
				);
		pall.setSelectedString("Click Here to Select a Deed Category");
		
		back.addActionListener(e -> {
			new PostDeed(user,null,null,null,null).createForm();
		});
		sub.addActionListener(e -> {
			//pointSystem();
			if(pall.getSelectedString() =="Cleaning")
			{
				new Cleaning(user).createForm();
			}
			else if(pall.getSelectedString() == "Community")
			{
				new Community(user).createForm();
			}
			else if(pall.getSelectedString() =="Disability")
			{
				new Disability(user).createForm();
			}
			else if(pall.getSelectedString() =="Elderly")
			{
				new Elderly(user).createForm();
			}
			else if(pall.getSelectedString() =="Flatmates")
			{
				new FlatmatesD(user).createForm();
			}
			else if(pall.getSelectedString() =="Friend")
			{
				new Friend(user).createForm();
			}
			else if(pall.getSelectedString() =="Homeless")
			{
				new Homeless(user).createForm();
			}
			else if(pall.getSelectedString() =="Kids")
			{
				new Kids(user).createForm();
			}
			else if(pall.getSelectedString() =="Pet")
			{
				new PetA(user).createForm();
			}
			else if(pall.getSelectedString() =="Recycling")
			{
				new Recycling(user).createForm();
			}
			else if(pall.getSelectedString() =="Volunteer")
			{
				new Volunteer(user).createForm();
			}
			else if(pall.getSelectedString() =="Water Conservation")
			{
				new WaterCon(user).createForm();
			}
			else
			{
				Dialog.show(null,"Please Select a Deed Category","OK",null);
			}
		});
		
		res.addActionListener(e -> {
			
			pall.setSelectedString("Click Here to Select a Deed Category");
		});
		
		this.add(l2);
		add(pall);
		c.add(back);
		c.add(sub);
		c.add(res);
		add(c);
		this.show();
		

	}

	/*public void pointSystem() 
	{

		int a = pall.getSelectedStringIndex();
		
		
		//int dcatid=a;
		//int achid = a+1;
		//int achcatid = a+1;		 
		//int[] pointsArray = new int[] {80,5,45};
		
		
		//did.setAssignedWeighting(pointsArray[a]);
		//ach.setAchievementID(achid);
		//achcat.setAchieveCatID(achcatid);
		//dcat.setdcatid(dcatid);
		
	}*/

	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
		setLayout(new com.codename1.ui.layouts.LayeredLayout());
		setInlineStylesTheme(resourceObjectInstance);
		setInlineStylesTheme(resourceObjectInstance);
		setTitle("Set Deed Type");
		setName("Set Deed Type");
	}// </editor-fold>

	// -- DON'T EDIT ABOVE THIS LINE!!!

}
