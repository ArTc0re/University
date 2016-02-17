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
 * stationID<br>
 * unitID<br>
 * friendlyplayerindexes<br>
 * availablegoods<br>
 * tradeequivalences<br><br>
 * 
 * @author Peter Dörr
 * @since 20.12.12
 */
public class ComObject_tradestations implements IComObject {
	
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
	 * Name der Handelsstation.
	 */
	private ArrayList<String> stationID;
	/**
	 * UnitID der Handelsstation.
	 */
	private ArrayList<Integer> unitID;
	/**
	 * Spielerindices, mit denen gehandelt werden darf.
	 */
	private ArrayList<ArrayList<Integer>> friendlyplayerindexes;
	/**
	 * Liste an verfügbaren Gütern.
	 */
	private ArrayList<ArrayList<Integer>> availablegoods;
	/**
	 * Handelsratios der Handelsstation.
	 */
	private ArrayList<ArrayList<Integer>> tradeequivalences;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_tradestations Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param mapID MapID der Karte.
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ComObject_tradestations(String clientID, String clientKey, String mapID){
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
	 * @since 20.12.12
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
	 * @since 20.12.12
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
	 * @since 20.12.12
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
	 * @since 20.12.12
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
	 * @since 20.12.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter stationID.
	 * 
	 * @param stationID stationID
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setStationID(ArrayList<String> stationID){
		this.stationID = stationID;
	}
	
	/**
	 * Setter unitID.
	 * 
	 * @param unitID unitID
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setUnitID(ArrayList<Integer> unitID){
		this.unitID = unitID;
	}
	
	/**
	 * Setter friendlyplayerindexes.
	 * 
	 * @param friendlyplayerindexes friendlyplayerindexes
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setFriendlyplayerindexes(ArrayList<ArrayList<Integer>> friendlyplayerindexes){
		this.friendlyplayerindexes = friendlyplayerindexes;
	}
	
	/**
	 * Setter availablegoods.
	 * 
	 * @param availablegoods availablegoods
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setAvailablegoods(ArrayList<ArrayList<Integer>> availablegoods){
		this.availablegoods = availablegoods;
	}
	
	/**
	 * Setter tradeequivalences.
	 * 
	 * @param tradeequivalences tradeequivalences
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setTradeequivalences(ArrayList<ArrayList<Integer>> tradeequivalences){
		this.tradeequivalences = tradeequivalences;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
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
	 * @since 20.12.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
	
	/**
	 * Getter stationID.
	 * 
	 * @return stationID
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<String> getStationID(){
		return stationID;
	}
	
	/**
	 * Getter unitID.
	 * 
	 * @return unitID
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getUnitID(){
		return unitID;
	}
	
	/**
	 * Getter friendlyplayerindexes.
	 * 
	 * @return friendlyplayerindexes
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<ArrayList<Integer>> getFriendlyplayerindexes(){
		return friendlyplayerindexes;
	}
	
	/**
	 * Getter availablegoods.
	 * 
	 * @return availablegoods
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<ArrayList<Integer>> getAvailablegoods(){
		return availablegoods;
	}
	
	/**
	 * Getter tradeequivalences.
	 * 
	 * @return tradeequivalences
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<ArrayList<Integer>> getTradeequivalences(){
		return tradeequivalences;
	}
}
