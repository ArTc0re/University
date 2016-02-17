package sepsis.network;

import java.util.ArrayList;
import sepsis.game.Game;

/**
 * Startet einen Thread, der IComObjects erhält, diese über das Netzwerk mit dem Server verarbeitet und dann zurück gibt.
 * 
 * @author Peter Dörr
 * @since 03.02.13
 */
public class NetworkThread extends Thread {
	
	/**
	 * Enthält alle ausgehenden IComObjects.
	 */
	private ArrayList<IComObject> outgoing = new ArrayList<IComObject>();
	/**
	 * Enthält alle eingehenden IComObjects. 
	 */
	private ArrayList<IComObject> incomming = new ArrayList<IComObject>();
	/**
	 * Gibt an, ob der Thread weiterlaufen oder nach dem nächsten Durchlauf beendet werden soll.
	 */
	private boolean running = true;
	/**
	 * Die Network-Instanz zum Versenden und Empfangen der Nachrichten.
	 */
	private Network network = new Network();
	
	
	
	/**
	 * Konstruktor des Netzwerk-Threads.
	 * 
	 * @author Peter Dörr
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
	 * Sendet alle IComObjects an den Server und Empfängt die Antworten, die anschließend
	 * über getNextIComObject abgerufen werden können.
	 * 
	 * @author Peter Dörr
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
	 * Fügt das IComObject der Liste für ausgehende Server-Anfragen hinzu. Diese werden vom Thread zeitnah bearbeitet.
	 * Die Antwort auf die Server-Anfrage kann über die getNextIComObject erhalten werden.
	 * 
	 * @param iComObject Das zu sendende IComObject.
	 * 
	 * @author Peter Dörr
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
	 * Fügt das IComObject der Liste für ausgehende Server-Anfragen hinzu. Diese werden vom Thread zeitnah bearbeitet.
	 * Die Antwort auf die Server-Anfrage kann über die getNextIComObject erhalten werden.
	 * 
	 * @return Das nächste IComObject oder null, wenn kein eingehendes IComObject bereit liegt.
	 * 
	 * @author Peter Dörr
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
	 * Beendet den Thread nach dem nächsten Durchlauf. Alle verbleibenden ausgehenden Nachrichten gehen verloren,
	 * die bisher noch nicht abgefragten Antworten sind jedoch noch verfügbar.
	 * 
	 * @author Peter Dörr
	 * @since 03.02.13
	 */
	public void endThread()
	{
		try 
		{
			Game.logger.info("NetworkThread.endThread: Netzwerk-Thread wird nach dem nächsten Durchlauf beendet.");
			running = false;
		} 
		catch (Exception e) 
		{
			Game.logger.error("NetworkThread.endThread(): " + e.getMessage());
		}
	}
}
