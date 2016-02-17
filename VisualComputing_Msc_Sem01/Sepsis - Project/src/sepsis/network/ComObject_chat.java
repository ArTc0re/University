package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br>
 * playerIDReceiver<br>
 * message<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 19.12.12
 */
public class ComObject_chat implements IComObject {

	//Input-Daten (Client -> Server)
	/**
	 * ClientID des Clients.
	 */
	private String clientID;
	/**
	 * ClientKey des Clients.
	 */
	private String clientKey;
	/**
	 * ID des Spielers.
	 */
	private String playerID;
	/**
	 * GameID des Spiels.
	 */
	private String gameID;
	/**
	 * Die PlayerID des Spielers, an den die Nachricht gerichtet ist.
	 */
	private String playerIDReceiver;
	/**
	 * Die zu versendende Nachricht.
	 */
	private String message;
	
	
	
	//Output-Daten (Server -> Client)
	/**
	 * Status-Antwort des Servers.
	 */
	private STATUS status;
	/**
	 * Fehlerinformationen bei einer Error Status-Antwort des Servers.
	 */
	private String errorInfo;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_chat Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels, an die die Nachricht gerichtet ist (darf null sein, falls die Nachricht global ist).
	 * @param playerIDReceiver Die PlayerID des Spielers, an den die Nachricht gerichtet ist (Darf null sein, falls die Nachricht an alle Spieler eines Spieles gerichtet ist).
	 * @param message Die zu versendende Nachricht.
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	public ComObject_chat(String clientID, String clientKey, String playerID, String gameID, String playerIDReceiver, String message){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
		this.playerIDReceiver = playerIDReceiver;
		this.message = message;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getClientID() {
		return clientID;
	}

	/**
	 * Getter clientKey.
	 * 
	 * @return clientKey
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getClientKey() {
		return clientKey;
	}

	/**
	 * Getter playerID.
	 * 
	 * @return playerID
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getPlayerID() {
		return playerID;
	}
		
	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getGameID() {
		return gameID;
	}
	
	/**
	 * Getter playerIDReceiver.
	 * 
	 * @return playerIDReceiver
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getPlayerIDReceiver() {
		return playerIDReceiver;
	}
		
	/**
	 * Getter message.
	 * 
	 * @return message
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected String getMessage() {
		return message;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected void setStatus(STATUS status) {
		this.status = status;
	}

	/**
	 * Setter errorInfo.
	 * 
	 * @param errorInfo errorInfo
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	protected void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	public STATUS getStatus() {
		return status;
	}

	/**
	 * Getter errorInfo.
	 * 
	 * @return errorInfo
	 * 
	 * @author Peter Dörr
	 * @since 19.12.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
}
