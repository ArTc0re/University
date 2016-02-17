package sepsis.network;

import java.util.ArrayList;

/**
 * Objekt zur Kommunikation mit dem Server.<br><br>
 * 
 * Input:<br>
 * clientID<br>
 * clientKey<br>
 * playerID<br>
 * gameID<br><br>
 * 
 * Output:<br>
 * status<br>
 * errorInfo<br>
 * gameID<br>
 * mapID<br>
 * name<br>
 * gameStatus<br>
 * turn<br>
 * winner<br>
 * activeUnit<br>
 * activeUnitLastAction<br>
 * tradePartner<br>
 * tradePartnerUnit<br>
 * tradeGive<br>
 * tradeGet<br>
 * tradeStatusLast<br>
 * playerIDs<br>
 * playerNames<br>
 * turnsTaken<br><br>
 * 
 * @author Peter Dörr
 * @since 25.11.12
 */
public class ComObject_gameinfo implements IComObject {
	
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
	 * MapID der Karte.
	 */
	private String mapID;
	/**
	 * Name des Spiels.
	 */
	private String name;
	/**
	 * Status des Spiels.
	 */
	private GAMESTATUS gameStatus;
	/**
	 * Rundenzähler.
	 */
	private int turn;
	/**
	 * Spieler, der das Spiel gewonnen hat.
	 */
	private String winner;
	/**
	 * Spieler, der gerade am Zug ist.
	 */
	private String activePlayer;
	/**
	 * Aktive Einheit.
	 */
	private int activeUnit;
	/**
	 * Von der zuletzt 
	 */
	private ACTIVEUNITSLASTACTION activeUnitsLastAction;
	/**
	 * Name des Spielers der Einheit, mit der gerade gehandelt wird.
	 */
	private String tradePartner;
	/**
	 * UnitID der Einheit, mit der gehandelt wird.
	 */
	private int tradePartnerUnit;
	/**
	 * Waren, die angeboten werden.
	 */
	private ArrayList<Integer> tradeGive;
	/**
	 * Waren, die gefordert werden.
	 */
	private ArrayList<Integer> tradeGet;
	/**
	 * Letzter Status des Handels.
	 */
	private TRADESTATUSLAST tradeStatusLast;
	/**
	 * PlayerIDs aller Spieler, die im Spiel sind.
	 */
	private ArrayList<String> playerIDs;
	/**
	 * Spielernamen aller Spieler, die im Spiel sind.
	 */
	private ArrayList<String> playerNames;
	/**
	 * Zeigt an, welche Einheit bereits ihren Zug diese Runde durchgeführt hat.
	 */
	private ArrayList<Boolean> turnsTaken;
		
		
		
	/**
	 * Erzeugt ein neues ComObject_gameinfo Objekt, das zum Austausch von Daten mit dem Server benötigt wird.
	 * 
	 * @param clientID ClientID des Clients.
	 * @param clientKey ClientKey des Clients.
	 * @param playerID Name des Spielers.
	 * @param gameID GameID des Spiels.
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ComObject_gameinfo(String clientID, String clientKey, String playerID, String gameID){
		this.clientID = clientID;
		this.clientKey = clientKey;
		this.playerID = playerID;
		this.gameID = gameID;
	}

	/**
	 * Getter clientID.
	 * 
	 * @return clientID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
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
	 * @since 25.11.12
	 */
	protected String getClientKey() {
		return clientKey;
	}

	/**
	 * Getter playerID.
	 * 
	 * @return playerID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected String getPlayerID() {
		return playerID;
	}

	/**
	 * Getter gameID.
	 * 
	 * @return gameID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getGameID() {
		return gameID;
	}
	
	/**
	 * Setter status.
	 * 
	 * @param status status
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
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
	 * @since 25.11.12
	 */
	protected void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	/**
	 * Setter mapID.
	 * 
	 * @param mapID mapID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setMapID(String mapID) {
		this.mapID = mapID;
	}

	/**
	 * Setter name.
	 * 
	 * @param name name
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Setter gameStatus.
	 * 
	 * @param gameStatus gameStatus
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setGameStatus(GAMESTATUS gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * Setter turn.
	 * 
	 * @param turn turn
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTurn(int turn) {
		this.turn = turn;
	}

	/**
	 * Setter winner.
	 * 
	 * @param winner winner
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setWinner(String winner) {
		this.winner = winner;
	}

	/**
	 * Setter activePlayer.
	 * 
	 * @param activePlayer activePlayer
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setActivePlayer(String activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * Setter activeUnit.
	 * 
	 * @param activeUnit activeUnit
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setActiveUnit(int activeUnit) {
		this.activeUnit = activeUnit;
	}

	/**
	 * Setter activeUnitsLastAction.
	 * 
	 * @param activeUnitsLastAction activeUnitsLastAction
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setActiveUnitsLastAction(ACTIVEUNITSLASTACTION activeUnitsLastAction) {
		this.activeUnitsLastAction = activeUnitsLastAction;
	}

	/**
	 * Setter tradePartner.
	 * 
	 * @param tradePartner tradePartner
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTradePartner(String tradePartner) {
		this.tradePartner = tradePartner;
	}

	/**
	 * Setter tradePartnerUnit.
	 * 
	 * @param tradePartnerUnit tradePartnerUnit
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTradePartnerUnit(int tradePartnerUnit) {
		this.tradePartnerUnit = tradePartnerUnit;
	}

	/**
	 * Setter tradeGive.
	 * 
	 * @param tradeGive tradeGive
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTradeGive(ArrayList<Integer> tradeGive) {
		this.tradeGive = tradeGive;
	}

	/**
	 * Setter tradeGet.
	 * 
	 * @param tradeGet tradeGet
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTradeGet(ArrayList<Integer> tradeGet) {
		this.tradeGet = tradeGet;
	}

	/**
	 * Setter tradeStatusLast.
	 * 
	 * @param tradeStatusLast tradeStatusLast
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTradeStatusLast(TRADESTATUSLAST tradeStatusLast) {
		this.tradeStatusLast = tradeStatusLast;
	}

	/**
	 * Setter playerIDs.
	 * 
	 * @param playerIDs playerIDs
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setPlayerIDs(ArrayList<String> playerIDs) {
		this.playerIDs = playerIDs;
	}
	
	/**
	 * Setter playerNames.
	 * 
	 * @param playerNames playerNames
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setPlayerNames(ArrayList<String> playerNames) {
		this.playerNames = playerNames;
	}

	/**
	 * Setter turnsTaken.
	 * 
	 * @param turnsTaken turnsTaken
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	protected void setTurnsTaken(ArrayList<Boolean> turnsTaken) {
		this.turnsTaken = turnsTaken;
	}

	/**
	 * Getter status.
	 * 
	 * @return status
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
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
	 * @since 25.11.12
	 */
	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * Getter mapID.
	 * 
	 * @return mapID
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getMapID() {
		return mapID;
	}

	/**
	 * Getter name.
	 * 
	 * @return name
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter gameStatus.
	 * 
	 * @return gameStatus
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public GAMESTATUS getGameStatus() {
		return gameStatus;
	}

	/**
	 * Getter turn.
	 * 
	 * @return turn
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Getter winner.
	 * 
	 * @return winner
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getWinnerID() {
		return winner;
	}

	/**
	 * Getter activePlayer.
	 * 
	 * @return activePlayer
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getActivePlayerID() {
		return activePlayer;
	}

	/**
	 * Getter activeUnit.
	 * 
	 * @return activeUnit
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public int getActiveUnit() {
		return activeUnit;
	}

	/**
	 * Getter activeUnitsLastAction.
	 * 
	 * @return activeUnitsLastAction
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ACTIVEUNITSLASTACTION getActiveUnitsLastAction() {
		return activeUnitsLastAction;
	}

	/**
	 * Getter tradePartner.
	 * 
	 * @return tradePartner
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public String getTradePartner() {
		return tradePartner;
	}

	/**
	 * Getter tradePartnerUnit.
	 * 
	 * @return tradePartnerUnit
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public int getTradePartnerUnit() {
		return tradePartnerUnit;
	}

	/**
	 * Getter tradeGive.
	 * 
	 * @return tradeGive
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<Integer> getTradeGive() {
		return tradeGive;
	}

	/**
	 * Getter tradeGet.
	 * 
	 * @return tradeGet
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<Integer> getTradeGet() {
		return tradeGet;
	}

	/**
	 * Getter tradeStatusLast.
	 * 
	 * @return tradeStatusLast
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public TRADESTATUSLAST getTradeStatusLast() {
		return tradeStatusLast;
	}

	/**
	 * Getter playerIDs.
	 * 
	 * @return playerIDs
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<String> getPlayerIDs() {
		return playerIDs;
	}
	
	/**
	 * Getter playerNames.
	 * 
	 * @return playerNames
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}

	/**
	 * Getter turnsTaken.
	 * 
	 * @return turnsTaken
	 * 
	 * @author Peter Dörr
	 * @since 25.11.12
	 */
	public ArrayList<Boolean> getTurnsTaken() {
		return turnsTaken;
	}
}
