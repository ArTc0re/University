package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br>
 * tradeOfferUnitID<br>
 * tradePartnerUnitID<br>
 * giveGoods<br>
 * getGoods<br>
 * tradeMessage<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br><br>
 * 
 * @author Peter D�rr
 * @since 28.11.12
 */
public class ComObject_trade implements IComObject {
	
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
	 * ID der Einheit, die den Handel anbietet.
	 */
	private int tradeOfferUnitID;
	/**
	 * ID der Einheit, die den Handel angeboten bekommt.
	 */
	private int tradePartnerUnitID;
	/**
	 * Angebotene G�ter.
	 */
	private ArrayList<Integer> giveGoods;
	/**
	 * Geforderte G�ter.
	 */
	private ArrayList<Integer> getGoods;
	/**
	 * Die beim Handel mitgegebene Nachricht.
	 */
	private String tradeMessage;
	
	
	
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
	 * Erzeugt ein neues ComObject_trade Objekt, das zum Austausch von Daten mit dem Server ben�tigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * @param tradeOfferUnitID ID der Einheit, die den Handel anbietet.
	 * @param tradePartnerUnitID ID der Einheit, die den Handel angeboten bekommt.
	 * @param giveGoods Angebotene G�ter.
	 * @param getGoods Geforderte G�ter.
	 * @param tradeMessage Die beim Handel mitgegebene Nachricht.
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	public ComObject_trade(String clientID, String clientKey, String playerID, String gameID, int tradeOfferUnitID, int tradePartnerUnitID, ArrayList<Integer> giveGoods, ArrayList<Integer> getGoods, String tradeMessage){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
		this.tradeOfferUnitID = tradeOfferUnitID;
		this.tradePartnerUnitID = tradePartnerUnitID;
		this.giveGoods = giveGoods;
		this.getGoods = getGoods;
		this.tradeMessage = tradeMessage;
	}
	
	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter D�rr
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
	 * @author Peter D�rr
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
	 * @author Peter D�rr
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
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	protected String getGameID() {
		return gameID;
	}
	
	/**
	 * Getter tradeOfferUnitID.
	 * 
	 * @return tradeOfferUnitID
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	public int getTradeOfferUnitID() {
		return tradeOfferUnitID;
	}
	
	/**
	 * Getter tradePartnerUnitID.
	 * 
	 * @return tradePartnerUnitID
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	public int getTradePartnerUnitID() {
		return tradePartnerUnitID;
	}
	
	/**
	 * Getter giveGoods.
	 * 
	 * @return giveGoods
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	protected ArrayList<Integer> getGiveGoods() {
		return giveGoods;
	}
	
	/**
	 * Getter getGoods.
	 * 
	 * @return getGoods
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	protected ArrayList<Integer> getGetGoods() {
		return getGoods;
	}
	
	/**
	 * Getter tradeMessage.
	 * 
	 * @return tradeMessage
	 * 
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	protected String getTradeMessage() {
		return tradeMessage;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter D�rr
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
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	protected void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter D�rr
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
	 * @author Peter D�rr
	 * @since 28.11.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}
}
