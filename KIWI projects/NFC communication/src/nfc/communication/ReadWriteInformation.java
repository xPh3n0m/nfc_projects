package nfc.communication;
import java.math.BigInteger;
import java.util.Arrays;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class ReadWriteInformation {
	
	private ACR_Communication acr;
	private CardChannel cc;
	private String buffer;
	
	public ReadWriteInformation(ACR_Communication a) {
		this.acr = a;
		this.cc = null;
		this.buffer = "";
	}

	public String ACR_readASCIIData() throws CardException {
		this.buffer = "";
        this.cc = acr.getCardChannel();
        if(cc == null) {
        	return null;
        }
        String result = "";
        for(int i = 4; i < 35; i = i+2) {
        	if(readPage(i)) {
        		break;
        	}
        }
        result += buffer;
        return Utility.hexToASCII(result);
	}
	
	public boolean ACR_writeData(String data) throws CardException {
		this.cc = acr.getCardChannel();
		
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
        	writePage(4+(2*i), b_split[i]);
        }

        return true;
	}
	
	/**
	 * Returns true if the page contains a halt character ("\")
	 * @param bufferint
	 * @return
	 * @throws CardException 
	 */
	private boolean readPage(int offset) throws CardException {
        
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
	
	
	private void writePage(int offset, byte[] dataOut) throws CardException {
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
	
    }
    
