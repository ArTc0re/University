package sepsis.game;

import sepsis.game.Game.STATES;
import sepsis.game.Game.TURN;
import sepsis.network.ComObject_gameinfo;
import sepsis.network.Network;

/**
 * Diese Klasse wird verwendet, um zeitraubende Anfragen an den Server (Game- und UnitInfo-Anfragen) in einem Thread zu kapseln.
 * 
 * @author Peter Dörr
 * @since 31.01.13
 */
public class GameUpdateThread extends Thread {

	/**
	 * Zählervariable der Instanzen dieser Klasse.
	 */
	public static int entities;
	/**
	 * Referenz auf das Game-Objekt.
	 */
	public Game game;
	
	
	
	/**
	 * Ruft Einheiteninformationen ab und aktualisiert die Einheiten des Spiels.
	 * 
	 * @author Peter Dörr
	 * @since 31.01.13
	 */
	public void run(){
		Network network = new Network();
		
		try {			
			//GameInfo abfragen		
			ComObject_gameinfo gameInfo = (ComObject_gameinfo)network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));

			//Gewinnbedingungen abfragen
			if(game.winconditions.checkForWinningPlayer(gameInfo) != null) {
				if(game.winconditions.checkForWinningPlayer(gameInfo).equals(Game.playerID)) {
					game.gameStarted = false;
					game.gameScreen.openStatisticsScreen(true);
				} else {
					game.gameStarted = false;
					game.gameScreen.openStatisticsScreen(false);
				}
			}

			//Handelsinformationen abfragen, wenn ein Angebot vom Spieler abgegeben wurde und auf die Antwort eines anderen Clients gewartet wird
			if(game.currentState == STATES.TRADE_WAIT_FOR_RESPONSE){
				game.checkTradeOfferResponse(gameInfo);
			}

			//Handelsinformationen abfragen, wenn ein Angebot von einem anderen Spieler kommt
			if(game.currentState == STATES.ENEMY_TURN){
				game.checkTradeRequests(gameInfo);
			}

			//Einheitenpositionen aktualisieren
//			game.gameUpdateThreadIsActualizingUnitInfos = true;
			game.refreshUnits();
//			game.gameUpdateThreadIsActualizingUnitInfos = false;

			//Überprüfen, welcher Spieler am Zug ist 
			if(game.currentTurn == TURN.ENEMY){
				game.checkTurn(gameInfo);
			}
		} catch (Exception e) {
			Game.logger.error("GameUpdateThread.run: Fehler bei der Server-Kommunikation.");
		}
		
		entities--;
	}
}
