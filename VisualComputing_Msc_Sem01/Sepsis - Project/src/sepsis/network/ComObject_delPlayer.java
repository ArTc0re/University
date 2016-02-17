package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * gameID<br>
 * playerID<br>
 * playerDelID<br>
 * gameOwnerKey<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter D�rr
 * @since 19.12.12
 */
public class ComObject_delPlayer implements IComObject {
	
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
	 * ID des Spielers.
	 */
	private String playerID;
	/**
	 * ID des Spielers, der entfernt werden soll.
	 */
	private String playerDelID;
	/**
	 * Key, der einen Spieler als Ersteller eines Spieles dazu berechtigt, bestimmte Aktionen auszuf�hren.
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
	 * Erzeugt ein neues ComObject_delPlayer Objekt, das zum Austausch von Daten mit dem Server ben�tigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param gameID GameID des Spiels.
	 * @param playerID ID des Spielers.
	 * @param playerDelID ID des Spielers, der entfernt werden soll.
	 * @param gameOwnerKey Key, der einen Spieler als Ersteller eines Spieles dazu berechtigt, bestimmte Aktionen auszuf�hren.
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	public ComObject_delPlayer(String clientID, String clientKey, String gameID, String playerID, String playerDelID, String gameOwnerKey){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.gameID = gameID;
		this.playerID = playerID;
		this.playerDelID = playerDelID;
		this.gameOwnerKey = gameOwnerKey;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getClientID(){
		return clientID;
	}
	
	/**
	 * Getter clientKey.
	 * 
	 * @return clientKey
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getClientKey(){
		return clientKey;
	}
	
	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getGameID(){
		return gameID;
	}
	
	/**
	 * Getter playerID.
	 * 
	 * @return playerID
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getPlayerID(){
		return playerID;
	}
	
	/**
	 * Getter playerDelID.
	 * 
	 * @return playerDelID
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getPlayerDelID(){
		return playerDelID;
	}
	
	/**
	 * Getter gameOwnerKey.
	 * 
	 * @return gameOwnerKey
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected String getGameOwnerKey(){
		return gameOwnerKey;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected void setStatus(STATUS status){
		this.status = status;
	}
	
	/**
	 * Setter errorInfo.
	 * 
	 * @param errorInfo errorInfo
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	public STATUS getStatus(){
		return status;
	}
	
	/**
	 * Getter errorInfo.
	 * 
	 * @return errorInfo
	 * 
	 * @author Peter D�rr
	 * @since 19.12.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
}
