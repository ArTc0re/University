package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * mapID<br>
 * gameName<br>
 * gameOwnerKey<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * gameID<br><br>
 * 
 * @author Peter Dörr
 * @since 25.11.12
 */
public class ComObject_createGame implements IComObject {
	
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
	 * MapID der Karte.
	 */
	private String mapID;
	/**
	 * Name des Spiels.
	 */
	private String gameName;
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
	 * GameID des Spiels.
	 */
	private String gameID;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_createGame Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param mapID MapID der Karte.
	 * @param gameName Name des Spiels.
	 * @param gameOwnerKey Key, der einen Spieler als Ersteller eines Spieles dazu berechtigt, bestimmte Aktionen auszuführen.
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ComObject_createGame(String clientID, String clientKey, String mapID, String gameName, String gameOwnerKey){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.mapID = mapID;
		this.gameName = gameName;
		this.gameOwnerKey = gameOwnerKey;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getClientID(){
		return clientID;
	}
	
	/**
	 * Getter clientKey.
	 * 
	 * @return clientKey
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getClientKey(){
		return clientKey;
	}
	
	/**
	 * Getter mapID.
	 * 
	 * @return mapID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getMapID(){
		return mapID;
	}
	
	/**
	 * Getter gameName.
	 * 
	 * @return gameName
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getGameName(){
		return gameName;
	}
	
	/**
	 * Getter gameOwnerKey.
	 * 
	 * @return gameOwnerKey
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getGameOwnerKey(){
		return gameOwnerKey;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setStatus(STATUS status){
		this.status = status;
	}
	
	/**
	 * Setter errorInfo.
	 * 
	 * @param errorInfo errorInfo
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter gameID.
	 * 
	 * @param gameID gameID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setGameID(String gameID){
		this.gameID = gameID;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public STATUS getStatus(){
		return status;
	}
	
	/**
	 * Getter errorInfo.
	 * 
	 * @return errorInfo
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
	
	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getGameID(){
		return gameID;
	}
}