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
 * terrainmappreview<br>
 * unitmappreview<br>
 * mapdescription<br>
 * numberofplayers<br><br>
 * 
 * @author Peter D�rr
 * @since 22.11.12
 */
public class ComObject_mappreview implements IComObject {
	
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
	 * TODO
	 */
	private ArrayList<ArrayList<Integer>> terrainmappreview;
	/**
	 * TODO
	 */
	private ArrayList<ArrayList<Integer>> unitmappreview;
	/**
	 * Beschreibender Text der Karte.
	 */
	private String mapDescription;
	/**
	 * Anzahl der Spieler.
	 */
	private int numberofplayers;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_mappreview Objekt, das zum Austausch von Daten mit dem Server ben�tigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param mapID ID der Karte.
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public ComObject_mappreview(String clientID, String clientKey, String mapID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.mapID = mapID;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
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
	 * @since 21.11.12
	 */
	protected String getClientKey(){
		return clientKey;
	}
	
	/**
	 * Getter mapID.
	 * 
	 * @return mapID
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected String getMapID(){
		return mapID;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
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
	 * @since 21.11.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter terrainmappreview.
	 * 
	 * @param terrainmappreview terrainmappreview
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected void setTerrainmappreview(ArrayList<ArrayList<Integer>> terrainmappreview){
		this.terrainmappreview = terrainmappreview;
	}
	
	/**
	 * Setter unitmappreview.
	 * 
	 * @param unitmappreview unitmappreview
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected void setUnitmappreview(ArrayList<ArrayList<Integer>> unitmappreview){
		this.unitmappreview = unitmappreview;
	}
	
	/**
	 * Setter mapDescription.
	 * 
	 * @param mapDescription mapDescription
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected void setMapDescription(String mapDescription){
		this.mapDescription = mapDescription;
	}
	
	/**
	 * Setter numberofplayers.
	 * 
	 * @param numberofplayers numberofplayers
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected void setNumberofplayers(int numberofplayers){
		this.numberofplayers = numberofplayers;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
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
	 * @since 21.11.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
	
	/**
	 * Getter terrainmappreview.
	 * 
	 * @return terrainmappreview
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public ArrayList<ArrayList<Integer>> getTerrainmappreview(){
		return terrainmappreview;
	}
	
	/**
	 * Getter unitmappreview.
	 * 
	 * @return unitmappreview
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public ArrayList<ArrayList<Integer>> getUnitmappreview(){
		return unitmappreview;
	}
	
	/**
	 * Getter mapDescription.
	 * 
	 * @return mapDescription
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public String getMapDescription(){
		return mapDescription;
	}
	
	/**
	 * Getter numberofplayers.
	 * 
	 * @return numberofplayers
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public int getNumberofplayers(){
		return numberofplayers;
	}
}
