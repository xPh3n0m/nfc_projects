package epfl.tnyfeler.eventmanager.model.events;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
	
	private final StringProperty eventName;
	private final StringProperty eventStatus;
	private final IntegerProperty numAttendees;
	
	public Event(String eventName, String eventStatus, int numAttendees) {
		this.eventName = new SimpleStringProperty(eventName);
		this.eventStatus = new SimpleStringProperty(eventStatus);
		this.numAttendees = new SimpleIntegerProperty(numAttendees);
	}
	
	public IntegerProperty numAttendees() {
		return numAttendees;
	}

	public StringProperty eventStatus() {
		return eventStatus;
	}

	public StringProperty eventName() {
		return eventName;
	}

	public int getNumAttendees() {
		return numAttendees.get();
	}

	public String getEventStatus() {
		return eventStatus.get();
	}

	public String getEventName() {
		return eventName.get();
	}
	
	public void setNumAttendees(int numAttendees) {
		this.numAttendees.set(numAttendees);
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus.set(eventStatus);
	}

	public void setEventName(String eventName) {
		this.eventName.set(eventName);
	}

}
