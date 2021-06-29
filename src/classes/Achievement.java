package classes;

/**
 * 
 * @author leish
 *
 */
public class Achievement {
	
	private int achievementID;
	private String achievementName;
	private String icon;

	// CONSTRUCTORS ---------------------------------------------------------------------
	public Achievement() {}
	public Achievement(int id) {
		this.achievementID = id;
	}
	public Achievement(int id, String name) {
		this.achievementID = id;
		this.achievementName = name;
		// Sets to the name of the achievement in the theme. 
		// Make sure additional achievements added are loaded in the "ach_##.png" format, where ## is the
		// achievementID stored in the DB
		this.icon = "ach_" + this.achievementID + ".png"; 
	}
	
	// GETTERS & SETTERS ----------------------------------------------------------------
	public int getAchievementID() {
		return achievementID;
		
	}
	public void setAchievementID(int achivementID) {
		this.achievementID = achivementID;
		System.out.println("Hello");
	}
	public String getAchievementName() {
		return achievementName;
	}
	public void setAchievementName(String name) {
		this.achievementName = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
