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
 * extinction<br>
 * goodplacement<br>
 * neededConditions<br>
 * good<br>
 * amount<br>
 * playerindex<br>
 * fieldX<br>
 * fieldY<br><br>
 * 
 * @author Peter Dörr
 * @since 20.12.12
 */
public class ComObject_winconditions implements IComObject {
	
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
	 * Gibt an, ob das Auslöschen des Gegners eine Siegesoption ist.
	 */
	private boolean extinction;
	/**
	 * Gibt an, ob das Platzieren von Gütern eine Siegesoption ist.
	 */
	private boolean goodplacement;
	/**
	 * Gibt an, wieviele der Güter-Platzieren Siegesbedingungen erfüllt sein müssen.
	 */
	private int neededConditions;
	/**
	 * Die zu platzierenden Güter.
	 */
	private ArrayList<Integer> good;
	/**
	 * Die Anzahl der zu platzierenden Güter.
	 */
	private ArrayList<Integer> amount;
	/**
	 * Der Spieler, für den die Siegesbedingung der Güter-Platzierung zutrifft.
	 */
	private ArrayList<Integer> playerindex;
	/**
	 * X-Koordinate der zu platzierenden Güter.
	 */
	private ArrayList<Integer> fieldX;
	/**
	 * Y-Koordinate der zu platzierenden Güter.
	 */
	private ArrayList<Integer> fieldY;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_winconditions Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param mapID MapID der Karte.
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ComObject_winconditions(String clientID, String clientKey, String mapID){
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
	 * Setter extinction.
	 * 
	 * @param extinction extinction
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setExtinction(boolean extinction){
		this.extinction = extinction;
	}
	
	/**
	 * Setter goodplacement.
	 * 
	 * @param goodplacement goodplacement
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setGoodplacement(boolean goodplacement){
		this.goodplacement = goodplacement;
	}
	
	/**
	 * Setter neededConditions.
	 * 
	 * @param neededConditions neededConditions
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setNeededConditions(int neededConditions){
		this.neededConditions = neededConditions;
	}
	
	/**
	 * Setter good.
	 * 
	 * @param good good
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setGood(ArrayList<Integer> good){
		this.good = good;
	}
	
	/**
	 * Setter amount.
	 * 
	 * @param amount amount
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setAmount(ArrayList<Integer> amount){
		this.amount = amount;
	}
	
	/**
	 * Setter playerindex.
	 * 
	 * @param playerindex playerindex
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setPlayerindex(ArrayList<Integer> playerindex){
		this.playerindex = playerindex;
	}
	
	/**
	 * Setter fieldX.
	 * 
	 * @param fieldX fieldX
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setFieldX(ArrayList<Integer> fieldX){
		this.fieldX = fieldX;
	}
	
	/**
	 * Setter fieldY.
	 * 
	 * @param fieldY fieldY
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	protected void setFieldY(ArrayList<Integer> fieldY){
		this.fieldY = fieldY;
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
	 * Getter extinction.
	 * 
	 * @return extinction
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public boolean getExtinction(){
		return extinction;
	}
	
	/**
	 * Getter goodplacement.
	 * 
	 * @return goodplacement
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public boolean getGoodplacement(){
		return goodplacement;
	}
	
	/**
	 * Getter neededConditions.
	 * 
	 * @return neededConditions
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public int getNeededConditions(){
		return neededConditions;
	}
	
	/**
	 * Getter good.
	 * 
	 * @return good
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getGood(){
		return good;
	}
	
	/**
	 * Getter amount.
	 * 
	 * @return amount
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getAmount(){
		return amount;
	}
	
	/**
	 * Getter playerindex.
	 * 
	 * @return playerindex
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getPlayerindex(){
		return playerindex;
	}
	
	/**
	 * Getter fieldX.
	 * 
	 * @return fieldX
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getFieldX(){
		return fieldX;
	}
	
	/**
	 * Getter fieldY.
	 * 
	 * @return fieldY
	 * 
	 * @author Peter Dörr
	 * @since 20.12.12
	 */
	public ArrayList<Integer> getFieldY(){
		return fieldY;
	}
}
