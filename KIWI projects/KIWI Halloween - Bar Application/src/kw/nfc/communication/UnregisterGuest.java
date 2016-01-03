package kw.nfc.communication;

import java.sql.SQLException;

import application.model.Guest;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UnregisterGuest extends Service<Guest> {

	private NFCCommunication nfcComm;
	private Task<Guest> task;
	private Guest guest;
	private NFCCard card;
	
	private ConnectDB connDB;
	
	public UnregisterGuest(Guest g, NFCCommunication nfcComm, NFCCard card, ConnectDB connDB) {
		this.guest = g;
		this.nfcComm = nfcComm;
		this.connDB = connDB;
		this.card = card;
	}

	@Override
	protected Task<Guest> createTask() {
		task = new Task<Guest>() {
            @Override
            protected Guest call() throws NFCCardException, SQLException {
            	// Erases the data on the wristband and changes the status of the current guest
            	nfcComm.eraseDataFromWristband(card);
            	
            	guest.updateDatabase();
            	
            	return guest;
            }
		};
		return task;
	}
}
