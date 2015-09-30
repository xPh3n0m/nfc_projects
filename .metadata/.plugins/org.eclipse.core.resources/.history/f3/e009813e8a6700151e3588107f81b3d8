package nfc.communication;

import java.util.Scanner;

import javax.smartcardio.CardException;

public class CommunicationTesting {
	
	 public static void main(String[] args) {
		 NFCCommunication reader = new NFCCommunication();
		 reader.connectToDefaultTerminal();
		 while(true) {
			 System.out.println("Write 1, Read 2: ");
			 int choice = (new Scanner(System.in)).nextInt();
			 if(choice==1) {
			 try {
				 
				 System.out.print("Enter guest id: ");
				 int gid = (new Scanner(System.in)).nextInt();
				 
				reader.writeData("{\"gid\":" + gid + "}");
			} catch (CardException e) {
				System.out.println("Error writing on wristband");
				e.printStackTrace();
			}
			 
			 System.out.println("Succesfully wrote on wristband");
			 
			 try {
				System.out.println(reader.readData());
			} catch (CardException e) {
				System.out.println("Error reading from wristband");
				e.printStackTrace();
			}
			 } else {
				 try {
						System.out.println(reader.readData());
					} catch (CardException e) {
						System.out.println("Error reading from wristband");
						e.printStackTrace();
					}
			 }
		 }
	 }

}
