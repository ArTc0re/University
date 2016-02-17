package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * unitmap<br><br>
 * 
 * @author Peter Dörr
 * @since 26.11.12
 */
public class ComObject_unitMap implements IComObject {
	
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
	 * Karte mit Informationen über Einheiten.
	 */
	private ArrayList<ArrayList<Integer>> unitMap;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_unitMap Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	public ComObject_unitMap(String clientID, String clientKey, String playerID, String gameID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
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
	 * Getter playerID.
	 * 
	 * @return playerID
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
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
	 * @since 26.11.12
	 */
	protected String getGameID() {
		return gameID;
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
	 * Setter unitMap.
	 * 
	 * @param unitMap unitMap
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	protected void setUnitMap(ArrayList<ArrayList<Integer>> unitMap){
		this.unitMap = unitMap;
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
		
	/**
	 * Getter unitMap.
	 * 
	 * @return unitMap
	 * 
	 * @author Peter Dörr
	 * @since 26.11.12
	 */
	public ArrayList<ArrayList<Integer>> getUnitMap() {
		return unitMap;
	}
		
}
