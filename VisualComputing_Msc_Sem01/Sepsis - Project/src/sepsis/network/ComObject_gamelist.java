package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * gamelist<br><br>
 * 
 * @author Peter Dörr
 * @since 25.11.12
 */
public class ComObject_gamelist implements IComObject {
	
	//Input-Daten (Client -> Server)
	/**
	 * ClientID des Clients.
	 */
	private String clientID;
	/**
	 * ClientKey des Clients.
	 */
	private String clientKey;
		
		
		
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
	 * Liste an verfügbaren Spielen.
	 */
	private ArrayList<String> gamelist;
	
		
		
	/**
	 * Erzeugt ein neues ComObject_gamelist Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ComObject_gamelist(String clientID, String clientKey){
		this.clientID = clientID;
		this.clientKey = clientKey;
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
	 * Setter gamelist.
	 * 
	 * @param gamelist gamelist
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setGamelist(ArrayList<String> gamelist){
		this.gamelist = gamelist;
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
	 * Getter gamelist.
	 * 
	 * @return gamelist
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<String> getGamelist(){
		return gamelist;
	}
}
