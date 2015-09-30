import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;


public class SuceMaBitte {
    
    /**
     * @param args
     * @throws CardException 
     */
    public static void main(String[] args) throws CardException {
            // TODO Auto-generated method stub
	
	// show the list of available terminals
    TerminalFactory factory = TerminalFactory.getDefault();
    List<CardTerminal> terminals = factory.terminals().list();
    System.out.println("Terminals: " + terminals);
    // get the first terminal
    CardTerminal terminal = terminals.get(0);
    // establish a connection with the card
    Card card = terminal.connect("*");
    System.out.println("card: " + card);
    CardChannel channel = card.getBasicChannel();
    //ResponseAPDU r = channel.transmit(new CommandAPDU(c1));
    //System.out.println("response: " + toString(r.getBytes()));
    // disconnect
    card.disconnect(false);
    
    }

}
