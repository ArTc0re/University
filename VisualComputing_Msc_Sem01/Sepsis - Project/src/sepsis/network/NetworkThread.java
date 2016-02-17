package sepsis.network;

import java.util.ArrayList;
import sepsis.game.Game;

/**
 * Startet einen Thread, der IComObjects erh�lt, diese �ber das Netzwerk mit dem Server verarbeitet und dann zur�ck gibt.
 * 
 * @author Peter D�rr
 * @since 03.02.13
 */
public class NetworkThread extends Thread {
	
	/**
	 * Enth�lt alle ausgehenden IComObjects.
	 */
	private ArrayList<IComObject> outgoing = new ArrayList<IComObject>();
	/**
	 * Enth�lt alle eingehenden IComObjects. 
	 */
	private ArrayList<IComObject> incomming = new ArrayList<IComObject>();
	/**
	 * Gibt an, ob der Thread weiterlaufen oder nach dem n�chsten Durchlauf beendet werden soll.
	 */
	private boolean running = true;
	/**
	 * Die Network-Instanz zum Versenden und Empfangen der Nachrichten.
	 */
	private Network network = new Network();
	
	
	
	/**
	 * Konstruktor des Netzwerk-Threads.
	 * 
	 * @author Peter D�rr
	 * @since 05.02.13
	 */
	public NetworkThread()
	{
		try 
		{
			setDaemon(true);
		} 
		catch (Exception e) 
		{
			Game.logger.error("NetworkThread.NetworkThread(): " + e.getMessage());
		}
	}
	
	/**
	 * Sendet alle IComObjects an den Server und Empf�ngt die Antworten, die anschlie�end
	 * �ber getNextIComObject abgerufen werden k�nnen.
	 * 
	 * @author Peter D�rr
	 * @since 03.02.13
	 */
	public void run(){
		Game.logger.info("NetworkThread.run: Netzwerk-Thread gestartet.");
		while(running){
			if(outgoing.size() > 0){
				IComObject iComObject = outgoing.remove(0);
				try 
				{
					incomming.add(network.establishCommunication(iComObject));
				} 
				catch (Exception e) 
				{
					outgoing.add(0, iComObject);
					Game.logger.error("NetworkThread.run(): inner try/catch: " + e.getMessage());
				}
			}
		}
		Game.logger.info("NetworkThread.run: Netzwerk-Thread beendet.");
	}

	
	
	/**
	 * F�gt das IComObject der Liste f�r ausgehende Server-Anfragen hinzu. Diese werden vom Thread zeitnah bearbeitet.
	 * Die Antwort auf die Server-Anfrage kann �ber die getNextIComObject erhalten werden.
	 * 
	 * @param iComObject Das zu sendende IComObject.
	 * 
	 * @author Peter D�rr
	 * @since 03.02.13
	 */
	public void sendIComObject(IComObject iComObject){
		try 
		{
			outgoing.add(iComObject);
		} 
		catch (Exception e) 
		{
			Game.logger.error("NetworkThread.sendIComObject(): " + e.getMessage());
		}
	}	
	
	
	/**
	 * F�gt das IComObject der Liste f�r ausgehende Server-Anfragen hinzu. Diese werden vom Thread zeitnah bearbeitet.
	 * Die Antwort auf die Server-Anfrage kann �ber die getNextIComObject erhalten werden.
	 * 
	 * @return Das n�chste IComObject oder null, wenn kein eingehendes IComObject bereit liegt.
	 * 
	 * @author Peter D�rr
	 * @since 03.02.13
	 */
	public IComObject getNextIComObject(){
		try {
			if(incomming.size() > 0){
				return incomming.remove(0);
			} else 
			{
				return null;
			}
		} catch (Exception e) {
			Game.logger.error("NetworkThread.getNextIComObject(): " + e.getMessage());
			return null;
		}
	}	
	
	
	/**
	 * Beendet den Thread nach dem n�chsten Durchlauf. Alle verbleibenden ausgehenden Nachrichten gehen verloren,
	 * die bisher noch nicht abgefragten Antworten sind jedoch noch verf�gbar.
	 * 
	 * @author Peter D�rr
	 * @since 03.02.13
	 */
	public void endThread()
	{
		try 
		{
			Game.logger.info("NetworkThread.endThread: Netzwerk-Thread wird nach dem n�chsten Durchlauf beendet.");
			running = false;
		} 
		catch (Exception e) 
		{
			Game.logger.error("NetworkThread.endThread(): " + e.getMessage());
		}
	}
}
