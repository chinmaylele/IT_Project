package classes;

/**
 * 
 * @author leish
 *
 */
public class DeedCategory {

	private int deedCatID;
	private String deedCatName;
	private int dcatid;
	private int achieveCatID;
	private int achievementID;
	private int categories;
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public DeedCategory() {}

	// GETTER & SETTERS -----------------------------------------------------------------
	
	public int getDeedCatID() {
		return deedCatID;
	}

	public void setDeedCatID(int deedCatID) {
		this.deedCatID = deedCatID;
	}

	public String getDeedCatName() {
		return deedCatName;
	}

	public void setDeedCatName(String deedCatName) {
		this.deedCatName = deedCatName;
	}

	public int getAchieveCatID() {
		return achieveCatID;
	}

	public void setAchieveCatID(int achieveCatID) {
		this.achieveCatID = achieveCatID;
	}

	public int getAchievementID() {
		return achievementID;
	}

	public void setAchievementID(int achievementID) {
		this.achievementID = achievementID;
	}
	public int getdcatid()
	{
		return dcatid;
	}
	public void setdcatid(int dcatid)
	{
		this.dcatid=dcatid;
	}
	public int getcategories()
	{
		return categories;
	}
	public void setcategories(int categories)
	{
		this.categories=categories;
	}
}
