package classes;

/**
 * 
 * @author leish
 * @Created 31/01/2019
 *
 */
public class Associate extends User {

	private String relationship;
	
	// CONSTRUCTORS ---------------------------------------------------------------------
	public Associate() {}

	// GETTER & SETTERS -----------------------------------------------------------------
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
