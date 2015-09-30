package nfc.communication;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;

import javax.smartcardio.CardException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NFC_Communication extends Service<String>{
	
	private ACR_Communication acr;
	private ReadWriteInformation ri;
	private ConnectDB connDB;
	private String data;
	
	private int noBeers;
	private int noSpirits;
	
	public NFC_Communication() {
		acr = new ACR_Communication();
		ri = new ReadWriteInformation(acr);
		
		connDB = new ConnectDB();
		connDB.connect();
	}
	
	
	public void setNewOrder() throws CardException {
		data = ri.ACR_readASCIIData();
		
		if(data == null) {
			return;
		}
		
		JSONParser jparser = new JSONParser();
		JSONObject jsonData;
		try {
			jsonData = (JSONObject) jparser.parse(data);
			Long gid = (long) jsonData.get("gid");
			connDB.newOrder(gid.intValue(), noBeers, noSpirits);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
            @Override
            protected String call() throws CardException {
            	setNewOrder();
            	return "Order confirmed";
            }
    };
	}


	public void setOrder(int nB, int nS) {
		noBeers = nB;
		noSpirits = nS;
	}

}
