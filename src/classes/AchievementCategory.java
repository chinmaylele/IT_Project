package classes;

/**
 * 
 * @author leish
 *
 */
public class AchievementCategory {
	
	private int achieveCatID;
	private String achieveCatName;
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public AchievementCategory() {}
	public AchievementCategory(int id, String name) {
		this.achieveCatID = id;
		this.achieveCatName = name;
	}
	
	// GETTERS & SETTERS ----------------------------------------------------------------
	public int getAchieveCatID() {
		return achieveCatID;
	}
	public void setAchieveCatID(int achieveCatID) {
		this.achieveCatID = achieveCatID;
	}
	public String getAchieveCatName() {
		return achieveCatName;
	}
	public void setAchieveCatName(String achieveCatName) {
		this.achieveCatName = achieveCatName;
	}
}
