package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * mapID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * type<br>
 * maxhitpoints<br>
 * maxfirepower<br>
 * maxcargo<br>
 * maxmovement<br><br>
 * 
 * @author Peter Dörr
 * @since 24.11.12
 */
public class ComObject_unittype implements IComObject {
	
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
	 * Type (Name) der Einheit.
	 */
	private ArrayList<String> type;
	/**
	 * Maximale Trefferpunkte der Einheit.
	 */
	private ArrayList<Integer> maxhitpoints;
	/**
	 * Maximale Angriffskraft der Einheit.
	 */
	private ArrayList<Integer> maxfirepower;
	/**
	 * Maximale Lagerkapazität der Einheit.
	 */
	private ArrayList<Integer> maxcargo;
	/**
	 * Maximale Bewegungsreichweite der Einheit.
	 */
	private ArrayList<Integer> maxmovement;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_unittype Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param mapID ID der Karte.
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ComObject_unittype(String clientID, String clientKey, String mapID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.mapID = mapID;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
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
	 * @since 24.11.12
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
	 * @since 24.11.12
	 */
	
	protected String getMapID(){
		return mapID;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
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
	 * @since 24.11.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter type.
	 * 
	 * @param type type
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	protected void setType(ArrayList<String> type){
		this.type = type;
	}
	
	/**
	 * Setter maxhitpoints.
	 * 
	 * @param maxhitpoints maxhitpoints
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	protected void setMaxhitpoints(ArrayList<Integer> maxhitpoints){
		this.maxhitpoints = maxhitpoints;
	}
	
	/**
	 * Setter maxfirepower.
	 * 
	 * @param maxfirepower maxfirepower
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	protected void setMaxfirepower(ArrayList<Integer> maxfirepower){
		this.maxfirepower = maxfirepower;
	}
	
	/**
	 * Setter maxcargo.
	 * 
	 * @param maxcargo maxcargo
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	protected void setMaxcargo(ArrayList<Integer> maxcargo){
		this.maxcargo = maxcargo;
	}
	
	/**
	 * Setter maxmovement.
	 * 
	 * @param maxmovement maxmovement
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	protected void setMaxmovement(ArrayList<Integer> maxmovement){
		this.maxmovement = maxmovement;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
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
	 * @since 24.11.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
	
	/**
	 * Getter type.
	 * 
	 * @return type
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ArrayList<String> getType(){
		return type;
	}
	
	/**
	 * Getter maxhitpoints.
	 * 
	 * @return maxhitpoints
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ArrayList<Integer> getMaxhitpoints(){
		return maxhitpoints;
	}
	
	/**
	 * Getter maxfirepower.
	 * 
	 * @return maxfirepower
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ArrayList<Integer> getMaxfirepower(){
		return maxfirepower;
	}
	
	/**
	 * Getter maxcargo.
	 * 
	 * @return maxcargo
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ArrayList<Integer> getMaxcargo(){
		return maxcargo;
	}
	
	/**
	 * Getter maxmovement.
	 * 
	 * @return maxmovement
	 * 
	 * @author Peter Dörr
	 * @since 24.11.12
	 */
	public ArrayList<Integer> getMaxmovement(){
		return maxmovement;
	}
}
