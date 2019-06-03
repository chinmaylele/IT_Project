package classes;

public class Deed {

	private int deedID;
	private int userID;
	private int deedCatID;
	private String deedName;
	
	/*
	Deed Name examples to include in GUIHandler uploadDeed:
	
	Community:
	"Picked someone up from a fall"
	Animals:
	"Gave a dog a pat"
	"Walked neighbour's dog"
	"Rescued cat from tree"
	Flatmates:
	"Bought pizza"
	 * */
	
	private int assignedWeighting;
	private int points;
	private String proofLink; // Needs validation is a hyperlink either here or in wherever it is set
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public Deed() {}
	public Deed(int dID, int uID, int dcID, String dN, int aW, int vP, String pL) {
		this.deedID = dID;
		this.userID = uID;
		this.deedCatID = dcID;
		this.deedName = dN;
		this.assignedWeighting = aW;
		this.points = vP;
		this.proofLink = pL;
	}
	public Deed(int dID,int uID,int dcID, String dN)
	{
		this.deedID = dID;
		this.userID = uID;
		this.deedCatID = dcID;
		this.deedName = dN;
		
	}
	
	// GETTER & SETTERS -----------------------------------------------------------------
	public int getDeedID() {
		return deedID;
	}
	public void setDeedID(int deedID) {
		this.deedID = deedID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getDeedCatID() {
		return deedCatID;
	}
	public void setDeedCatID(int deedCatID) {
		this.deedCatID = deedCatID;
	}
	public String getDeedName() {
		return deedName;
	}
	public void setDeedName(String deedName) {
		this.deedName = deedName;
	}
	public int getAssignedWeighting() {
		return assignedWeighting;
	}
	public void setAssignedWeighting(int assignedWeighting) {
		this.assignedWeighting = assignedWeighting;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getProofLink() {
		return proofLink;
	}
	public void setProofLink(String proofLink) {
		this.proofLink = proofLink;
	}
}
