package sepsis.network;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * message<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 09.01.13
 */
public class ComObject_globalchat implements IComObject {
	
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
	 * Die zu versendende Nachricht.
	 */
	private String message;

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
	 * Erzeugt ein neues ComObject_globalchat Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param message Die zu versendende Nachricht.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public ComObject_globalchat(String clientID, String clientKey, String message){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.message = message; 
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
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
	 * @since 09.01.13
	 */
	protected String getClientKey() {
		return clientKey;
	}
	
	/**
	 * Getter message.
	 * 
	 * @return message
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	protected String getMessage() {
		return message;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
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
	 * @since 09.01.13
	 */
	protected void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
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
	 * @since 09.01.13
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
}
