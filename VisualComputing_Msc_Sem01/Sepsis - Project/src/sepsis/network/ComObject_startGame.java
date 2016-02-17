package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * gameID<br>
 * gameOwnerKey<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 26.11.12
 */
public class ComObject_startGame implements IComObject {
	
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
	 * GameID des Spiels.
	 */
	private String gameID;
	/**
	 * Key, der einen Spieler als Ersteller eines Spieles dazu berechtigt, bestimmte Aktionen auszuführen.
	 */
	private String gameOwnerKey;
	
	
	
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
	 * Erzeugt ein neues ComObject_startGame Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param gameID GameID des Spiels.
	 * @param gameOwnerKey Key, der einen Spieler als Ersteller eines Spieles dazu berechtigt, bestimmte Aktionen auszuführen.
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	public ComObject_startGame(String clientID, String clientKey, String gameID, String gameOwnerKey){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.gameID = gameID;
		this.gameOwnerKey = gameOwnerKey;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
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
	 * @since 26.11.12
	 */
	protected String getClientKey() {
		return clientKey;
	}

	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	public String getGameID() {
		return gameID;
	}
	
	/**
	 * Getter gameOwnerKey.
	 * 
	 * @return gameOwnerKey
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	public String getGameOwnerKey() {
		return gameOwnerKey;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
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
	 * @since 26.11.12
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
	 * @since 26.11.12
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
	 * @since 26.11.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
}
