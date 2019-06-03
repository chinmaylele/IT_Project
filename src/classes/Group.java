package classes;

import java.util.ArrayList;

/**
 * 
 * @author kurtg
 *
 */
public class Group {
	private int groupID;
	private String groupName;
	private String groupType;
	private int groupOwner;
	
	private ArrayList<Integer> groupMemberArray = new ArrayList<Integer>();
	private ArrayList<Integer> groupEventArray = new ArrayList<Integer>();

	// CONSTRUCTORS ---------------------------------------------------------------------
		public Group() {}
		
	// GETTERS & SETTERS ----------------------------------------------------------------
		public void addGroupMemberArray(int gID) {
			this.groupMemberArray.add(gID);
		}
		public ArrayList<Integer> getGroupMemberArray() {
			return this.groupMemberArray;
		}
		public Integer getGroupMemberArraySingleIndex(int gID) {
			return this.groupMemberArray.get(gID);
		}
		public void clearMemberArray() {
			this.groupMemberArray.clear();
		}
		
		public void addGroupEventArray(int gID) {
			this.groupEventArray.add(gID);
		}
		public ArrayList<Integer> getGroupEventArray() {
			return this.groupEventArray;
		}
		public Integer getGroupEventArraySingleIndex(int gID) {
			return this.groupEventArray.get(gID);
		}
		public void clearEventArray() {
			this.groupEventArray.clear();
		}

		public int getGroupID() {
			return groupID;
		}

		public int getGroupOwner() {
			return groupOwner;
		}

		public void setGroupOwner(int groupOwner) {
			this.groupOwner = groupOwner;
		}

		public void setGroupID(int groupID) {
			this.groupID = groupID;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getGroupType() {
			return groupType;
		}

		public void setGroupType(String groupType) {
			this.groupType = groupType;
		}
}
