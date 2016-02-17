package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br>
 * unitID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * activePlayerID<br>
 * actualPlayerID<br>
 * roundNo<br>
 * winner<br><br>
 * 
 * @author Peter Dörr
 * @since 28.11.12
 */
public class ComObject_endTurn implements IComObject {
	
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
	 * UnitID der sich bewegenden Einheit.
	 */
	private int unitID;
		
		
		
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
	 * Der aktive Spieler.
	 */
	private String activePlayerID;
	/**
	 * Rundenzähler.
	 */
	private int roundNo;
	/**
	 * Spieler, der das Spiel gewonnen hat.
	 */
	private String winner;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_endTurn Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * @param unitID UnitID der sich bewegenden Einheit.
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ComObject_endTurn(String clientID, String clientKey, String playerID, String gameID, int unitID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
		this.unitID = unitID;
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
	 * Getter unitID.
	 * 
	 * @return unitID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected int getUnitID() {
		return unitID;
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
	 * Setter activePlayerID.
	 * 
	 * @param activePlayerID activePlayerID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setActivePlayerID(String activePlayerID) {
		this.activePlayerID = activePlayerID;
	}

	/**
	 * Setter roundNo.
	 * 
	 * @param roundNo roundNo
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setRoundNo(int roundNo) {
		this.roundNo = roundNo;
	}
	
	/**
	 * Setter winner.
	 * 
	 * @param winner winner
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setWinner(String winner) {
		this.winner = winner;
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
	
	/**
	 * Getter activePlayerID.
	 * 
	 * @return activePlayerID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public String getActivePlayerID() {
		return activePlayerID;
	}

	/**
	 * Getter roundNo.
	 * 
	 * @return roundNo
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public int getRoundNo() {
		return roundNo;
	}
	
	/**
	 * Getter winner.
	 * 
	 * @return winner
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public String getWinner() {
		return winner;
	}
}
