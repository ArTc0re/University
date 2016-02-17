package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * clientKey<br>
 * 
 * @author Peter D�rr
 * @since 21.11.12
 */
public class ComObject_logon implements IComObject {
	
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
	 * Zuk�nftiger ClientKey des Clients.
	 */
	private String clientKey;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_logon Objekt, das zum Austausch von Daten mit dem Server ben�tigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public ComObject_logon(String clientID){
		this.clientID = clientID;
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
	 * Setter clientKey.
	 * 
	 * @param clients clients
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	protected void setClientKey(String clientKey){
		this.clientKey = clientKey;
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
	 * Getter clientKey.
	 * 
	 * @return clients
	 * 
	 * @author Peter D�rr
	 * @since 21.11.12
	 */
	public String getClientKey(){
		return clientKey;
	}
}
