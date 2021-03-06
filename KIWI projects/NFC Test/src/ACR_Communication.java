import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;


public class ACR_Communication {

	private CardTerminal ct;
	private Card c;
	private CardChannel cc;

	public ACR_Communication() {
		this.ct = selectDefaultCardTerminal();
		this.c = null;
		this.cc = null;
	}
	
	public CardChannel getCardChannel() {
		try {
			if(!ct.isCardPresent()) {
				System.out.println("Place wristband on the reader...");
			}
			ct.waitForCardPresent(0);	
			c = establishConnection();
			cc = c.getBasicChannel();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cc;
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
	 * Method establishConnection
	 * Connects with a car
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
                    card = ct.connect(p);
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
