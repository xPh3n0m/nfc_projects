package kw.nfc.communication;

import javax.smartcardio.CardException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class WriteNewCloackId extends Service<Integer>{
	
	private int cid;
	private NFCCommunication nfc;
	private ConnectDB connDB;
	private Task<Integer> task;
	
	public WriteNewCloackId(int cid) {
		this.nfc = new NFCCommunication();
		if(nfc.getStatus() != NFCCommunication.CONNECTED) {
			nfc.connectToDefaultTerminal();
		}
		this.cid = cid;
	}

	@Override
	protected Task<Integer> createTask() {
		task = new Task<Integer>() {
            @Override
            protected Integer call() throws CardException {
            	int gid = readGid();
            	connDB.updateCloackId(gid, cid);
            	return cid;
            }

		};
		return task;
	}
	
	public int readGid() {
		String data;
		try {
			data = nfc.readData();

			JSONParser jparser = new JSONParser();
			JSONObject jsonData;
			try {
				System.out.println(data);
				jsonData = (JSONObject) jparser.parse(data);
				Long gid = (long) jsonData.get("gid");
				return gid.intValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (CardException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return -1;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public void stopTask() {
		nfc.cancel();
		task.cancel();
	}
	
	public void setConnDB(ConnectDB connDB) {
		this.connDB = connDB;
	}

}
