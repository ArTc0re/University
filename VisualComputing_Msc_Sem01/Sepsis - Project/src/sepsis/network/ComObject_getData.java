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
 * errorInfo<br><br>
 * 
 * @author Peter Dörr
 * @since 29.11.12
 */
public class ComObject_getData implements IComObject {
	
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
	 * Zeit, zu der die Nachricht kam.
	 */
	private ArrayList<String> incommingTime;
	/**
	 * ClientID des Senders.
	 */
	private ArrayList<String> senderID;
	/**
	 * Der Datentyp der Nachricht (SYSTEM oder CHAT).
	 */
	private ArrayList<DATATYPE> datatype;
	/**
	 * ID des Spielers, von dem die Nachricht kam.
	 */
	private ArrayList<String> senderplayerid;
	/**
	 * ID des Spielers, an den die Nachricht gerichtet war.
	 */
	private ArrayList<String> playeridreciever;
	/**
	 * Die GameID, falls eine Nachricht speziell an ein Spiel gerichtet ist.
	 */
	private ArrayList<String> gameID;
	/**
	 * Der Inhalt der Nachricht.
	 */
	private ArrayList<String> messages;
	
	
	
	/**
	 * Erzeugt ein neues ComObject_getData Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ComObject_getData(String clientID, String clientKey){
		this.clientID = clientID;
		this.clientKey = clientKey;
	}
	
	
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
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
	 * @since 29.11.12
	 */
	protected String getClientKey() {
		return clientKey;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
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
	 * @since 29.11.12
	 */
	protected void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Setter incommingTime.
	 * 
	 * @param incommingTime incommingTime
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setIncommingTime(ArrayList<String> incommingTime) {
		this.incommingTime = incommingTime;
	}
	
	/**
	 * Setter datatype.
	 * 
	 * @param datatype datatype
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setDatatype(ArrayList<DATATYPE> datatype) {
		this.datatype = datatype;
	}
	
	/**
	 * Setter senderID.
	 * 
	 * @param senderID senderID
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setSenderID(ArrayList<String> senderID) {
		this.senderID = senderID;
	}
	
	/**
	 * Setter senderplayerid.
	 * 
	 * @param senderplayerid senderplayerid
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setSenderplayerid(ArrayList<String> senderplayerid) {
		this.senderplayerid = senderplayerid;
	}
	
	/**
	 * Setter playeridreciever.
	 * 
	 * @param playeridreciever playeridreciever
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setPlayeridreciever(ArrayList<String> playeridreciever) {
		this.playeridreciever = playeridreciever;
	}
	
	/**
	 * Setter gameID.
	 * 
	 * @param gameID gameID
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setGameID(ArrayList<String> gameID) {
		this.gameID = gameID;
	}
	
	/**
	 * Setter messages.
	 * 
	 * @param messages messages
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	protected void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
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
	 * @since 29.11.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
	
	/**
	 * Getter incommingTime.
	 * 
	 * @return incommingTime
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getIncommingTime() {
		return incommingTime;
	}
	
	/**
	 * Getter senderID.
	 * 
	 * @return senderID
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getSenderID() {
		return senderID;
	}
	
	/**
	 * Getter datatype.
	 * 
	 * @return datatype
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<DATATYPE> getDatatype() {
		return datatype;
	}
	
	/**
	 * Getter senderplayerid.
	 * 
	 * @return senderplayerid
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getSenderplayerid() {
		return senderplayerid;
	}
	
	/**
	 * Getter playeridreciever.
	 * 
	 * @return playeridreciever
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getPlayeridreciever() {
		return playeridreciever;
	}
	
	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getGameID() {
		return gameID;
	}
	
	/**
	 * Getter messages.
	 * 
	 * @return messages
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	public ArrayList<String> getMessages() {
		return messages;
	}
}
