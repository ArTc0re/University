package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * attackerID<br>
 * defenderID<br>
 * gameID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 28.11.12
 */
public class ComObject_attack implements IComObject {
	
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
	 * ID der angreifenden Einheit.
	 */
	private int attackerID;
	/**
	 * ID der verteidigenden Einheit.
	 */
	private int defenderID;
	
	
	
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
	 * Der verursachte Schaden.
	 */
	private int damage;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_attack Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * @param attackerID ID der angreifenden Einheit.
	 * @param defenderID ID der verteidigenden Einheit.
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ComObject_attack(String clientID, String clientKey, String playerID, String gameID, int attackerID, int defenderID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
		this.attackerID = attackerID;
		this.defenderID = defenderID;
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
	 * Getter attackerID.
	 * 
	 * @return attackerID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected int getAttackerID() {
		return attackerID;
	}
	
	/**
	 * Getter defenderID.
	 * 
	 * @return defenderID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected int getDefenderID() {
		return defenderID;
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
	 * Setter damage.
	 * 
	 * @param damage damage
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setDamage(int damage) {
		this.damage = damage;
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
	 * Getter damage.
	 * 
	 * @return damage
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public int getDamage() {
		return damage;
	}
}
