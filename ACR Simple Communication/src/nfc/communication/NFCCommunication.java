package nfc.communication;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class NFCCommunication {
		
		private CardTerminal terminal;
		
		private int status;
		
		private String buffer;
		
		public static final int READY = 0;
		public static final int CONNECTED = 1;
		public static final int DISCONNECTED = 2;
		
		public NFCCommunication() {
			status = READY;
		}
		
		/**
		 * Connects to the default terminal
		 * Changes the status to either CONNECTED or DISCONNECTED
		 */
		public void connectToDefaultTerminal() {
			CardTerminal ct = selectDefaultCardTerminal();
			if(ct != null) {
				terminal = ct;
				status = CONNECTED;
			} else {
				status = DISCONNECTED;
			}
		}
		
		/**
		 * method selectDefaultCardTerminal
		 * Selects the default terminal among all available terminals and returns it
		 * @return
		 */
		private CardTerminal selectDefaultCardTerminal() {
			TerminalFactory factory = TerminalFactory.getDefault();
	        List<CardTerminal> terminals;
			try {
				terminals = factory.terminals().list();
				ListIterator<CardTerminal> terminalsIterator = terminals.listIterator();
				CardTerminal terminal = terminalsIterator.next();
				return terminal;
			} catch (CardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	   }
		
		/**
		 * Method reads data from an NFC device
		 * Reads one page after the other until a halt character is found
		 * @return
		 * @throws CardException
		 */
		public String readData() throws CardException {
			buffer = "";
	        CardChannel cc = getCardChannel();
	        if(cc == null) {
	        	return null;
	        }
	        String result = "";
	        for(int i = 4; i < 35; i = i+2) {
	        	if(readPage(cc, i)) {
	        		break;
	        	}
	        }

	        result += buffer;
	        
	        terminal.waitForCardAbsent(0);
	        
	        return Utility.hexToASCII(buffer);
		}
		
		/**
		 * Method used to write some String data on an NFC tag
		 * Waits until the NFC tag is removed to return (blocking method)
		 * @param data
		 * @return
		 * @throws CardException
		 */
		public boolean writeData(String data) throws CardException {
			CardChannel cc = getCardChannel();
			
			// Add the halt character to the string, and change it to a HEX string
			String d = Utility.asciiToHex(new String(data + "\\"));
			
			// Change the string to a byte array
	        byte[] b = new BigInteger(d,16).toByteArray();
	        
	        // Split the array in chunks of 8 bytes
	        byte[][] b_split = new byte[b.length/8 + 1][b.length];
	        int j = 0;
	        for(int i = 0; i < b.length; i=i+8) {
	        	b_split[j] = Arrays.copyOfRange(b, i, i+8);
	        	j++;
	        }
	        
	        //Write all pages
	        for(int i = 0; i < b_split.length; i++) {
	        	writePage(cc, 4+(2*i), b_split[i]);
	        }

	        // Wait until the NFC tag is removed from the wristband (block)
	        terminal.waitForCardAbsent(0);
	        
	        return true;
		}
		
		/**
		 * Returns true if the page contains a halt character ("\")
		 * @param bufferint
		 * @return
		 * @throws CardException 
		 */
		private boolean readPage(CardChannel cc, int offset) throws CardException {
	        
	        byte[] readCommand = {(byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) offset, (byte) 0x08};
	    	
	    	CommandAPDU readData = new CommandAPDU(readCommand);
	    	ResponseAPDU responseReadData = cc.transmit(readData);
	    	
	    	byte[] dataOut = responseReadData.getData();
	    	
			StringBuilder sb = new StringBuilder();
			for(byte b : dataOut) {
				if(b == 92) {
					buffer += sb.toString();
					return true;
				}
				sb.append(String.format("%02X", b));
			}
	    		
	    	buffer += sb.toString();
	        return false;
		}
		
		/**
		 * Method used to write a page of data dataOut using the card channel cc, at an offset
		 * @param cc
		 * @param offset
		 * @param dataOut
		 * @throws CardException
		 */
		private void writePage(CardChannel cc, int offset, byte[] dataOut) throws CardException {
	        // Encode the length of the string in a byte
	        byte bArraySize = new Integer(dataOut.length).byteValue();
	        
	        // Write command starting in page offset
	        byte[] writeCommand = {(byte) 0xFF, (byte) 0xD6, (byte) 0x00, (byte) offset};
	        
	        // The byte array for the full command
	        byte[] command = new byte[writeCommand.length + 1 + dataOut.length];
	        for(int i = 0; i < writeCommand.length; i++) {
	        	command[i] = writeCommand[i];
	        }
	        command[writeCommand.length] = bArraySize;
	        for(int i = 0; i < dataOut.length; i++) {
	        	command[i + writeCommand.length + 1] = dataOut[i];
	        }
	        
	        CommandAPDU writeData = new CommandAPDU(command);
	        ResponseAPDU responseWriteData = cc.transmit(writeData);	
		}
		
		/**
		 * Method used to establish a connection with a NFC device
		 * Tries to find a NFC device, connects to it if found otherwise sleeps
		 * @return
		 */
		private CardChannel getCardChannel() {
			try {
				while(!terminal.isCardPresent()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.out.println("Error thread sleep");
						e.printStackTrace();
					}
				}
				
				Card card = establishConnection();
				CardChannel cc = card.getBasicChannel();
				return cc;
			} catch (CardException e) {
				status = DISCONNECTED;
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * This method establishes a connection with a NFC device (use of a default protocol)
		 * @param ct
		 * @return
		 */
	    private Card establishConnection()
	    {
	            //Select automatically protocol T=1
	            String p = "T=1";
	            Card card = null;
	            try 
	            {
	                    card = terminal.connect(p);
	            } 
	            catch (CardException e) 
	            {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    return null;
	            }
	            ATR atr = card.getATR();
	            
	            return card;            
	            
	    }

}
