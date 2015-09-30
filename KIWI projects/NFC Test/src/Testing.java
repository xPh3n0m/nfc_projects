import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.smartcardio.CardException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Testing {

    public static void main(String[] args) throws CardException, SQLException {
    	ACR_Communication acr = new ACR_Communication();
    	ReadWriteInformation ri = new ReadWriteInformation(acr);
    	
    	ConnectDB conn = new ConnectDB();
    	conn.connect();

    	do {
    	System.out.println("Please select:\n[1] Write information on the wristband\n[2] "
    			+ "Read Information from the wristband\n[3] CloakRoom\n[4] New order\n[5] Order Status");
    	int choice = (new Scanner(System.in)).nextInt();
    	if(choice == 1) {
    		System.out.print("Enter guest id: ");
    		int gid = (new Scanner(System.in)).nextInt();
    		
    		Guest guest = conn.getGuestInfo(gid);
    		
        	ri.ACR_writeData(guest.getJSONString().toJSONString());

        	System.out.println("\nSuccess\n\n");
    	} else if (choice == 2){
    		
    		String data = ri.ACR_readASCIIData();
    		JSONParser jparser = new JSONParser();
    		
    		JSONObject jsonData;
			try {
				jsonData = (JSONObject) jparser.parse(data);
				System.out.println("Data on the wristband:\nID: " + jsonData.get("gid") + "\nName: " + jsonData.get("guest_name") + "\nNumero vestiaire: " + jsonData.get("cid") + "\n\n");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
        	
    	} else if (choice == 3){
    		String data = ri.ACR_readASCIIData();
    		JSONParser jparser = new JSONParser();
    		
    		JSONObject jsonData;
    		
    		try {
    			jsonData = (JSONObject) jparser.parse(data);
    			Long gid = (long) jsonData.get("gid");
    			Guest guest = conn.getGuestInfo(gid.intValue());
    			int cid = (int) guest.getCloakId();
    			
    			if(cid == 0) {
    				System.out.println("Entrez le numero du vestiaire: ");
    				int newCid = (new Scanner(System.in)).nextInt();
    				
    				conn.updateCloackId(gid.intValue(), newCid);
    				System.out.println("\nSuccess\n\n");
    			} else {
    				System.out.println("Numero du vestiaire: " + cid);
    			}
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	} else if (choice == 4) {
    		System.out.println("Nombre de bieres: ");
    		int nbBeers = (new Scanner(System.in)).nextInt();
    		System.out.println("Nombre de spiritueux: ");
    		int nbSpirits = (new Scanner(System.in)).nextInt();
    		
    		String data = ri.ACR_readASCIIData();
    		JSONParser jparser = new JSONParser();
    		JSONObject jsonData;
    		try {
				jsonData = (JSONObject) jparser.parse(data);
				Long gid = (long) jsonData.get("gid");
				conn.newOrder(gid.intValue(), nbBeers, nbSpirits);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	} else if (choice == 5) {
    		String data = ri.ACR_readASCIIData();
    		JSONParser jparser = new JSONParser();
    		JSONObject jsonData;
    		try {
				jsonData = (JSONObject) jparser.parse(data);
				Long gid = (long) jsonData.get("gid");
				Order o = conn.guestOrderStatus(gid.intValue());
				System.out.println(o.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	System.out.print("Continue? (y/n) ");
    	} while((new Scanner(System.in)).nextLine().equals("y"));
    	System.out.println();
    }
}
