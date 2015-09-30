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

public class NFCReaderCommunication {
	
	private CardTerminal terminal;
	private Card card;
	private CardChannel cardChannel;
	
	String buffer = "";
	
	public static final int READY = 0;
	public static final int CONNECTED = 1;
	public static final int DISCONNECTED = 2;
	public static final int CHIP_IS_PRESENT = 3;
	public static final int CHIP_IS_NOT_PRESENT = 4;
	
	private int status;

	public NFCReaderCommunication() {
		this.status = READY;
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
	 * Connects to the next NFC Chip placed on the reader
	 * @throws CardException
	 */
	public void connectToNextNFCChip() throws CardException {
		while(!terminal.isCardPresent()) {
			terminal.waitForCardPresent(500);
		}
		card = connectToCard();
		cardChannel = card.getBasicChannel();
	}
	
	/**
	 * Method connectToCard
	 * Connects with a card
	 * @param ct
	 * @return
	 * @throws CardException 
	 */
    private Card connectToCard() throws CardException
    {
            //Select automatically protocol T=1
            String p = "T=1";
            Card card = null;
            card = terminal.connect(p);

            ATR atr = card.getATR();
            
            return card;            
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
	
	public String ACR_readASCIIData() throws Exception {
        String result = "";
        for(int i = 4; i < 35; i = i+2) {
        	if(readPage(i, buffer)) {
        		break;
        	}
        }
        result += buffer;
        return Utility.hexToASCII(result);
	}
	
	public boolean ACR_writeData(String data) {
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
        	if(!writePage(4+(2*i), b_split[i])) {
        		return false;
        	}
        }

        return true;
	}
	
	/**
	 * Returns true if the page contains a halt character ("\")
	 * @param bufferint
	 * @return
	 * @throws Exception 
	 * @throws CardException 
	 */
	private boolean readPage(int offset, String buffer) throws CardException {
        byte[] readCommand = {(byte) 0xFF, (byte) 0xB0, (byte) 0x00, (byte) offset, (byte) 0x08};
    	
    	CommandAPDU readData = new CommandAPDU(readCommand);
    	ResponseAPDU responseReadData;
		responseReadData = cardChannel.transmit(readData);
    	
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
	
	
	private boolean writePage(int offset, byte[] dataOut) {
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
        try {
			ResponseAPDU responseWriteData = cardChannel.transmit(writeData);
			if(responseWriteData.getSW() == Utility.APDU_COMMAND_SUCCESS) {
				return true;
			}
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	
	public int getStatus() {
		return status;
	}

}
