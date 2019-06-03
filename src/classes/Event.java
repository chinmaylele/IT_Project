package classes;

import java.util.ArrayList;
import java.util.Date;

import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;

/**
 * 
 * @author kurtg
 *
 */
public class Event {
	
	private int eventID;
	private String eventName;
	private Date eventStart;
	private Date eventEnd;
	private int groupID;
	
	private ArrayList<Integer> eventCompetitorsArray = new ArrayList<>(); //array of user ID's


	// CONSTRUCTORS ---------------------------------------------------------------------
	public Event() {}
	
	// GETTERS & SETTERS ----------------------------------------------------------------
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int achivementID) {
		this.eventID = achivementID;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String name) {
		this.eventName = name;
	}
	public Date getEventStart() {
		return eventStart;
	}
	public void setEventStart(Date eventStart) {
		this.eventStart = eventStart;
	}
	public void setEventStart(String esString) {
		Date eventStart = new Date();
		try {
			eventStart = new SimpleDateFormat("dd/MM/yyyy").parse(esString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.eventStart = eventStart;
	}
	public Date getEventEnd() {
		return eventEnd;
	}
	public void setEventEnd(Date eventEnd) {
		this.eventEnd = eventEnd;
	}
	public void setEventEnd(String eeString) {
		Date eventEnd = new Date();
		try {
			eventEnd = new SimpleDateFormat("dd/MM/yyyy").parse(eeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.eventEnd = eventEnd;
	}
	public void addEventCompetitorsArray(int uID) {
		this.eventCompetitorsArray.add(uID);
	}
	public ArrayList<Integer> getEventCompetitorsArray() {
		return this.eventCompetitorsArray;
	}
	public Integer getEventCompetitorsArraySingleIndex(int uID) {
		return this.eventCompetitorsArray.get(uID);
	}
	public void clearEventCompetitorsArray() {
		this.eventCompetitorsArray.clear();
	}
	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

}
