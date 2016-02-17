package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * clients<br>
 * 
 * @author Peter Dörr
 * @since 18.11.12
 */
public class ComObject_getClients implements IComObject {
	
	//Input-Daten (Client -> Server)
	/**
	 * ClientID des Clients.
	 */
	private String clientID;
	
	
	
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
	 * Clients, die auf dem Server angemeldet sind.
	 */
	private ArrayList<String> clients;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_getClients Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public ComObject_getClients(String clientID){
		this.clientID = clientID;
	}
	
	
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	protected String getClientID(){
		return clientID;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
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
	 * @since 18.11.12
	 */
	protected void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter clients.
	 * 
	 * @param clients clients
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	protected void setClients(ArrayList<String> clients){
		this.clients = clients;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
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
	 * @since 18.11.12
	 */
	public String getErrorInfo(){
		return errorInfo;
	}
	
	/**
	 * Getter clients.
	 * 
	 * @return clients
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public ArrayList<String> getClients(){
		return clients;
	}
}
