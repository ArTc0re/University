package sepsis.network;

import java.awt.Point;
import java.util.ArrayList;

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
 * unitID<br>
 * ownerID<br>
 * utype<br>
 * destroyed<br>
 * lastMovement<br>
 * hitpoints<br>
 * movement<br>
 * cargo<br><br>
 * 
 * @author Peter Dörr
 * @since 28.11.12
 */
public class ComObject_unitinfo implements IComObject {
	
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
	 * ID der Einheit.
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
	 * Besitzer der Einheit.
	 */
	private String ownerID;
	/**
	 * Typ der Einheit.
	 */
	private String utype;
	/**
	 * Zeigt an, ob eine Einheit noch lebt oder nicht.
	 */
	private boolean destroyed;
	/**
	 * Die zuletzt von dieser Einheit durchgeführte Bewegung.
	 */
	private ArrayList<Point> lastMovement;
	/**
	 * Aktuelle Trefferpunkte der Einheit. WIRD NUR ANGEZEIGT, WENN UNITINFO VON DEM SPIELER ABGEFRAGT WIRD, DEM DIE EINHEIT GEHÖRT!
	 */
	private int hitpoints;
	/**
	 * Aktuelle Bewegungspunkte der Einheit. WIRD NUR ANGEZEIGT, WENN UNITINFO VON DEM SPIELER ABGEFRAGT WIRD, DEM DIE EINHEIT GEHÖRT!
	 */
	private int movement;
	/**
	 * Aktuelle Fracht der Einheit. WIRD NUR ANGEZEIGT, WENN UNITINFO VON DEM SPIELER ABGEFRAGT WIRD, DEM DIE EINHEIT GEHÖRT!
	 */
	private ArrayList<Integer> cargo;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_unitinfo Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * @param unitID ID der Einheit.
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ComObject_unitinfo(String clientID, String clientKey, String playerID, String gameID, int unitID){
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
	public int getUnitID() {
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
	 * Setter ownerID.
	 * 
	 * @param ownerID ownerID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}
	
	/**
	 * Setter utype.
	 * 
	 * @param utype utype
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setUtype(String utype) {
		this.utype = utype;
	}
	
	/**
	 * Setter destroyed.
	 * 
	 * @param destroyed destroyed
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	/**
	 * Setter lastMovement.
	 * 
	 * @param lastMovement lastMovement
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setLastMovement(ArrayList<Point> lastMovement) {
		this.lastMovement = lastMovement;
	}
	
	/**
	 * Setter hitpoints.
	 * 
	 * @param hitpoints hitpoints
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}
	
	/**
	 * Setter movement.
	 * 
	 * @param movement movement
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setMovement(int movement) {
		this.movement = movement;
	}
	
	/**
	 * Setter cargo.
	 * 
	 * @param cargo cargo
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	protected void setCargo(ArrayList<Integer> cargo) {
		this.cargo = cargo;
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
	 * Getter ownerID.
	 * 
	 * @return ownerID
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public String getOwnerID() {
		return ownerID;
	}

	/**
	 * Getter utype.
	 * 
	 * @return utype
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public String getUtype() {
		return utype;
	}

	/**
	 * Getter destroyed.
	 * 
	 * @return destroyed
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public boolean getDestroyed() {
		return destroyed;
	}

	/**
	 * Getter lastMovement.
	 * 
	 * @return lastMovement
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ArrayList<Point> getLastMovement() {
		return lastMovement;
	}

	/**
	 * Getter hitpoints.
	 * 
	 * @return hitpoints
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * Getter movement.
	 * 
	 * @return movement
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public int getMovement() {
		return movement;
	}
	
	/**
	 * Getter cargo.
	 * 
	 * @return cargo
	 * 
	 * @author Peter Dörr
	 * @since 28.11.12
	 */
	public ArrayList<Integer> getCargo() {
		return cargo;
	}
}
