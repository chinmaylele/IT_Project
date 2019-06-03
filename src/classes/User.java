package classes;

import java.util.ArrayList;

/**
 * 
 * @author 	leish
 * 
 */
public class User {
	
	private int userID;
	private String username;
	private String realName;
	private String email;
	private String password;
	private boolean accType;
	private int kudosTotal;
	private String DOB;
	private String occupation;
	
	private String profilePicture;
	
	private String securityQuestion;
	private String securityAnswer;
	private boolean privacy;
	
	private ArrayList<Integer> groupArray = new ArrayList<>();
	private ArrayList<Achievement> achievementArray = new ArrayList<>();
	private ArrayList<Associate> associateArray = new ArrayList<>();
	private ArrayList<Deed> deedArray = new ArrayList<>();
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public User() {}
	
	// GETTERS & SETTERS ----------------------------------------------------------------
	public void addGroupArray(int gID) {
		this.groupArray.add(gID);
	}
	public ArrayList<Integer> getGroupArray() {
		return this.groupArray;
	}
	public void addAchievementArray(Achievement ach) {
		this.achievementArray.add(ach);
	}
	public ArrayList<Achievement> getAchievementArray() {
		return this.achievementArray;
	}
	public void addAssociateArray(Associate ass) {
		this.associateArray.add(ass);
	}
	public ArrayList<Associate> getAssociateArray() {
		return this.associateArray;
	}
	public void addDeedArray(Deed d) {
		this.deedArray.add(d);
	}
	public ArrayList<Deed> getDeedArray() {
		return this.deedArray;
	}
	
	public void setDeedArray(ArrayList<Deed> arr) {
		 this.deedArray = arr;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) { // Never called from user, only ever system
		this.userID = userID;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) { // Never called from user, only ever system
		this.privacy = privacy;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityQuestion() {
		return securityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public boolean isAccType() {
		return accType;
	}
	public void setAccType(boolean accType) {
		this.accType = accType;
	}
	public int getKudosTotal() {
		return kudosTotal;
	}
	public void setKudosTotal(int kudosTotal) {
		this.kudosTotal = kudosTotal;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dob) {
		this.DOB = dob;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String dp) {
		this.profilePicture = dp;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	// METHODS ---------------------------------------------------------------------------------------------------
	/**
	 * Sets all  to null for disposal
	 * @author Leish
	 */
	public void disposeUser() {
		this.groupArray = null;
		this.achievementArray = null;
		this.deedArray = null;
		
		this.userID = 0;
		this.username = null;
		this.realName = null;
		this.email = null;
		this.password = null;
		this.accType = false;
		this.kudosTotal = 0;
		this.DOB = null;
		this.securityQuestion = null;
		this.securityAnswer = null;
		this.privacy = true;
	}
	
	/**
	 * @author kurtg
	 * @param gID
	 * @return
	 */
	public int getGroupArraySingleIndex(int gID) {
		return this.groupArray.get(gID);
	}
}
