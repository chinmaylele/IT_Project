package classes;

import java.util.ArrayList;

/**
 * @author 	leish
 * 
 */
public class Administrator extends User {

	private int adminID;
	private boolean admin;
	
	private DBHandler dbHandler = new DBHandler();

	// CONSTRUCTORS ---------------------------------------------------------------------
	public Administrator() {}
	public Administrator(int id) {
		this.adminID = id;
		this.admin = true;
	}
	
	// GETTER & SETTERS -----------------------------------------------------------------
	public int getAdminID() {
		return adminID;
	}
	public void setAdminID(int adminID) {
		this.adminID = adminID;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	// ACHIEVEMENT METHODS --------------------------------------------------------------
	public ArrayList<AchievementCategory> reportAllAchievementCategories() {
		return dbHandler.getAllAchievementCats();
	}
	
	public AchievementCategory reportAchievementCategory(int acID) {
		
		// TODO
		AchievementCategory ac = new AchievementCategory();
		return ac;
	}
	
	public void addAchievementCategory(String nameAC, String typeAC) {
		
		// TODO
	}
	
	public void updateAchievementCategory(String nameAC, String typeAC) {
		
		// TODO
	}
	
	public void deleteAchievementCategory() {
		
		// TODO
	}	
	
	// DEED METHODS --------------------------------------------------------------------
	public ArrayList<DeedCategory> reportAllDeedCategories() {
		return dbHandler.getAllDeedCats();
	}

	public DeedCategory reportDeedCategory() {
		
		// TODO
		DeedCategory dc = new DeedCategory();
		return dc;
	}
	
	public void addDeedCategory(int acID, String nameDC, int tierDC, int deedSlots, int deedSlotWeights) {
		
		// TODO
	}
	
	public void updateDeedCategory(String nameDC, int tierDC, int deedSlots, int deedSlotWeights) {
		
		// TODO
	}
	
	public void deleteDeedCategory() {
		
		// TODO
	}
}
