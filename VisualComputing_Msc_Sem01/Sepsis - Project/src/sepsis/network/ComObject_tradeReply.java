package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br>
 * response<br>
 * tradeMessage<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 28.11.12
 */
public class ComObject_tradeReply implements IComObject {
	
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
	 * Antwot auf die Handelsanfrage.
	 */
	private RESPONSE response;
	/**
	 * Die beim Handel mitgegebene Nachricht.
	 */
	private String tradeMessage;
	
	
	
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
	 * Erzeugt ein neues ComObject_tradeReply Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * @param response Antwot auf die Handelsanfrage.
	 * @param tradeMessage Die beim Handel mitgegebene Nachricht.
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ComObject_tradeReply(String clientID, String clientKey, String playerID, String gameID, RESPONSE response, String tradeMessage){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
		this.response = response;
		this.tradeMessage = tradeMessage;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
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
	 * @since 28.11.12
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
	 * @since 28.11.12
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
	 * @since 28.11.12
	 */
	protected String getGameID() {
		return gameID;
	}
	
	/**
	 * Getter response.
	 * 
	 * @return response
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public RESPONSE getResponse() {
		return response;
	}
	
	/**
	 * Getter tradeMessage.
	 * 
	 * @return tradeMessage
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected String getTradeMessage() {
		return tradeMessage;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
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
	 * @since 28.11.12
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
	 * @since 28.11.12
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
	 * @since 28.11.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
}
