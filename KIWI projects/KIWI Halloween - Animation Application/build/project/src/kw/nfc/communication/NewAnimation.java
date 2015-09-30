package kw.nfc.communication;

import java.sql.SQLException;

import javax.smartcardio.CardException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class NewAnimation extends Service<String>{
	
	private NFCCommunication nfc;
	private ConnectDB connDB;
	private Task<String> task;
	
	private int gid;
	
	public NewAnimation() {
		this.nfc = new NFCCommunication();
		if(nfc.getStatus() != NFCCommunication.CONNECTED) {
			nfc.connectToDefaultTerminal();
		}
	}

	@Override
	protected Task<String> createTask() {
		task = new Task<String>() {
            @Override
            protected String call() throws CardException, SQLException, ParseException {
            	int gid = readGid();
            	String name = getGuestName(gid);
            	return name;
            }

		};
		return task;
	}

	private String getGuestName(int gid) throws SQLException {
		Guest g = connDB.getGuestInfo(gid);
		return g.getName();
	}
	
	public int readGid() throws CardException, ParseException {
		String data;
		data = nfc.readData();

		JSONParser jparser = new JSONParser();
		JSONObject jsonData;
		System.out.println(data);
		jsonData = (JSONObject) jparser.parse(data);
		Long g = (long) jsonData.get("gid");
		this.gid = g.intValue();
		return gid;

	}
	
	public void stopTask() {
		nfc.cancel();
		task.cancel();
	}

	public void setConnDB(ConnectDB connDB) {
		this.connDB = connDB;
	}

	public int getGid() {
		return gid;
	}

}
