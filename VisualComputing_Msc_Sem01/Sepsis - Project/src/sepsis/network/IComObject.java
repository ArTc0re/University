package sepsis.network;

/**
 * Interface für alle Klassen des Typs ComObject...
 * 
 * @author Peter Dörr
 * @since 18.11.12
 */
public interface IComObject {
	
	/**
	 * Mögliche Status-Antworten des Servers.
	 */
	public enum STATUS {OK, ERROR};
	/**
	 * Gibt die Spielstatus-Antwort des Servers an.
	 */
	public enum GAMESTATUS {INITED, STARTED, ENDED};
	/**
	 * Gibt an, welche Aktion die aktive Einheit zuletzt durchgeführt hat.
	 */
	public enum ACTIVEUNITSLASTACTION {NONE};
	/**
	 * Gibt an, welcher Zustand der Handel zuletzt eingenommen hat.
	 */
	public enum TRADESTATUSLAST {NONE, ACCEPTED, REJECTED, DND};
	/**
	 * Gibt an, ob ein Handelsangebot angenommen oder ablegehnt wurde.
	 */
	public enum RESPONSE {ACCEPTED, REJECTED, DND};
	/**
	 * Gibt an, welcher Typ von Nachricht von getData zurückgegeben wird.
	 */
	public enum DATATYPE {SYSTEM, CHAT};
}
