import java.io.Console;
import java.math.BigInteger;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class PCSC {
        
        private byte[] atr = null;
        private String protocol = null;
        private byte[] historical = null;
        
        public CardTerminal selectDefaultCardTerminal() {
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
        
        public CardTerminal selectCardTerminal()
        {
                try
                {
                        // show the list of available terminals
                        TerminalFactory factory = TerminalFactory.getDefault();
                        List<CardTerminal> terminals = factory.terminals().list();
                        ListIterator<CardTerminal> terminalsIterator = terminals.listIterator();
                        CardTerminal terminal = null;
                        CardTerminal defaultTerminal = null;
                        if(terminals.size() > 1)
                        {
                                System.out.println("Please choose one of these card terminals (1-" + terminals.size() + "):");
                                int i = 1;
                                while(terminalsIterator.hasNext())
                                {
                                        terminal = terminalsIterator.next();
                                        System.out.print("["+ i + "] - " + terminal + ", card present: "+terminal.isCardPresent());
                                        if(i == 1) 
                                        {
                                                defaultTerminal = terminal;
                                                System.out.println(" [default terminal]");
                                        }
                                        else
                                        {
                                                System.out.println();
                                        }                                       
                                        i++;
                                }
                                Scanner in = new Scanner(System.in);
                                try
                                {
                                        int option = in.nextInt();
                                        terminal = terminals.get(option-1);                                     
                                }
                                catch(Exception e2)
                                {
                                        //System.err.println("Wrong value, selecting default terminal!");
                                        terminal = defaultTerminal;
                                        
                                }
                                System.out.println("Selected: "+terminal.getName());
                                //Console console = System.console(); 
                                return terminal;
                        }
                        

                }
                catch(Exception e)
                {
                        System.err.println("Error occured:");
                        e.printStackTrace();
                }
                return null;
        }

        public String byteArrayToHexString(byte[] b) 
        {
            StringBuffer sb = new StringBuffer(b.length * 2);
            for (int i = 0; i < b.length; i++) {
            	if(b[i] == 92) {
            		break;
            	}
              int v = b[i] & 0xff;
              if (v < 16) {
                sb.append('0');
              }
              sb.append(Integer.toHexString(v));
            }
            return sb.toString().toUpperCase();
         }
        
        public String hexToASCII(String hexValue)
        {
           StringBuilder output = new StringBuilder("");
           for (int i = 0; i < hexValue.length(); i += 2)
           {
              String str = hexValue.substring(i, i + 2);
              output.append((char) Integer.parseInt(str, 16));
           }
           return output.toString();
        }

        public String asciiToHex(String asciiValue)
        {
            char[] chars = asciiValue.toCharArray();
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < chars.length; i++)
            {
                hex.append(Integer.toHexString((int) chars[i]));
            }
            return hex.toString();
        }
        
        public byte[] hexStringToByteArray(String s) 
        {     
                int len = s.length();     
                byte[] data = new byte[len / 2];     
                for (int i = 0; i < len; i += 2) 
                {         
                        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));     
                }     
                return data; 
        }
        
        public byte[] hexStringToByteArray1(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                     + Character.digit(s.charAt(i+1), 16));
            }
            return data;
        }

        public Card establishConnection(CardTerminal ct)
        {
                this.atr = null;
                this.historical = null;
                this.protocol = null;

                /*System.out.println("To establish connection, please choose one of these protocols (1-4):");
                System.out.println("[1] - T=0");
                System.out.println("[2] - T=1");
                System.out.println("[3] - T=CL");
                System.out.println("[4] - * [default]");
                
                String p = "*";
                Scanner in = new Scanner(System.in);
                
                try
                {
                        int option = in.nextInt();
                        
                        if(option == 1) p = "T=0";
                        if(option == 2) p = "T=1";
                        if(option == 3) p = "T=CL";
                        if(option == 4) p = "*";                        
                }
                catch(Exception e)
                {
                        //System.err.println("Wrong value, selecting default protocol!");
                        p = "*";
                }
                
                System.out.println("Selected: "+p);
                */
                
                //Select automatically protocol T=1
                String p = "T=1";
                Card card = null;
                try 
                {
                        card = ct.connect(p);
                } 
                catch (CardException e) 
                {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return null;
                }
                ATR atr = card.getATR();
                System.out.println("Connected:");       
                System.out.println(" - ATR:  "+ byteArrayToHexString(atr.getBytes()));
                System.out.println(" - Historical: "+ byteArrayToHexString(atr.getHistoricalBytes()));
                System.out.println(" - Protocol: "+card.getProtocol());
                
                this.atr = atr.getBytes();
                this.historical = atr.getHistoricalBytes();
                this.protocol = card.getProtocol();
                
                return card;            
                
        }
        
       

}