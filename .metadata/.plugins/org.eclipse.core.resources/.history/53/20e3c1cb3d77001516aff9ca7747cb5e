package nfc.testing;

import java.util.Scanner;

import javax.smartcardio.CardException;

import nfc.communication.NFCCommunication;

/**
 * Simple class to test the read/write functionalities of the NFC terminal
 * Allows to write an ID on an NFC tag and read it
 * @author Thierry
 *
 */
public class CommunicationTesting {
	
	 public static void main(String[] args) {
		 NFCCommunication reader = new NFCCommunication();
		 reader.connectToDefaultTerminal();
		 while(true) {
			 System.out.println("Write 1, Read 2: ");
			 int choice = (new Scanner(System.in)).nextInt();
			 if(choice==1) {
			 try {
				 
				 System.out.print("Enter identifier: ");
				 int gid = (new Scanner(System.in)).nextInt();
				 
				reader.writeData("{\"id\":" + gid + "}");
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
