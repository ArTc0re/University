package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * gameID<br>
 * playerName<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * playerID<br><br>
 * 
 * @author Peter Dörr
 * @since 25.11.12
 */
public class ComObject_addPlayer implements IComObject {
	
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
	 * Name des Spielers.
	 */
	private String playerName;
			
			
		
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
	 * ID des Spielers.
	 */
	private String playerID;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_addPlayer Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param gameID GameID des Spiels.
	 * @param playerName Name des Spielers.
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ComObject_addPlayer(String clientID, String clientKey, String gameID, String playerName){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.gameID = gameID;
		this.playerName = playerName;
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
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getGameID(){
		return gameID;
	}
	
	/**
	 * Getter playerName.
	 * 
	 * @return playerName
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getPlayerName(){
		return playerName;
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
	 * Setter playerID.
	 * 
	 * @param playerID playerID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setPlayerID(String playerID){
		this.playerID = playerID;
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
	 * Getter playerID.
	 * 
	 * @return playerID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getPlayerID(){
		return playerID;
	}
}
