package sepsis.game;

import java.util.ArrayList;

import sepsis.network.ComObject_gameinfo;
import sepsis.network.ComObject_winconditions;

/**
 * Klasse zur Überprüfung der Siegesbedingungen.
 * 
 * @author Peter Dörr
 * @since 10.01.13
 */
public class WinConditions {
	
	/**
	 * Gibt an, ob die Vernichtung aller Gegner eine Siegesmöglichkeit ist.
	 */
	private boolean extinction;
	/**
	 * Gibt an, ob goodPlacement eine Siegesoption ist.
	 */
	private boolean goodPlacement;
	/**
	 * Gibt an, wieviele goodPlacements für einen Sieg erfüllt sein müssen.
	 */
	private int goodPlacementCount;
	/**
	 * Informationen, wo und wieviel Waren von einem Spieler platziert werden müssen.
	 */
	public ArrayList<ArrayList<Integer>> goodPlacements;
	/**
	 * Ein Game Objekt, aus welchem Informationen ausgelesen werden.
	 */
	private Game game;
	/**
	 * Das Spielfeld, aus dem Einheiteninformationen ausgelesen werden.
	 */	
	private PlayingField playingField;
	/**
	 * Liste an Spielern zu Spielstart.
	 */
	private ArrayList<String> playerIDs;
	
	/**
	 * Konstruktor der Klasse.
	 * 
	 * @param winconditions Das vom Server erhaltene ComObject_winconditions Objekt, das die Gewinnbedingungen der aktuellen Karte beinhaltet.
	 * @param game Ein Game Objekt, aus welchem Spielinformationen abgerufen werden.
	 * @param playingField Ein PlayingField Objekt, aus welchem Spielinformationen abgerufen werden.
	 * @param gameInfo Ein ComObject_gameinfo Objekt, aus welchem Informationen entnommen werden.
	 * 
	 * @author Peter Dörr
	 * @since 10.01.13
	 */
	public WinConditions(ComObject_winconditions winconditions, Game game, PlayingField playingField, ComObject_gameinfo gameInfo){
		this.game = game;
		this.playingField = playingField;
		this.playerIDs = gameInfo.getPlayerIDs();
		
		extinction = winconditions.getExtinction();
		goodPlacement = winconditions.getGoodplacement();
		
		if(winconditions.getGoodplacement()){
			goodPlacementCount = winconditions.getNeededConditions();
			ArrayList<Integer> good = winconditions.getGood();
			ArrayList<Integer> amount = winconditions.getAmount();
			ArrayList<Integer> playerindex = winconditions.getPlayerindex();
			ArrayList<Integer> fieldX = winconditions.getFieldX();
			ArrayList<Integer> fieldY = winconditions.getFieldY();
			
			goodPlacements = new ArrayList<ArrayList<Integer>>();
			//0: Good
			goodPlacements.add(new ArrayList<Integer>());
			//1: Amount
			goodPlacements.add(new ArrayList<Integer>());
			//2: Playerindex
			goodPlacements.add(new ArrayList<Integer>());
			//3: Field_X
			goodPlacements.add(new ArrayList<Integer>());
			//4: Field_Y
			goodPlacements.add(new ArrayList<Integer>());
			
			for(int i = 0; i < good.size(); i++){
				goodPlacements.get(0).add(good.get(i));
				goodPlacements.get(1).add(amount.get(i));
				goodPlacements.get(2).add(playerindex.get(i));
				goodPlacements.get(3).add(fieldX.get(i));
				goodPlacements.get(4).add(fieldY.get(i));
			}
		}
	}
	
	
	
	/**
	 * Überprüft, ob das Spiel von einem Spieler gewonnen wurde. Gibt den Namen des Spielers, der das Spiel gewonnen hat, zurück, oder null, falls es noch keinen Gewinner gibt.
	 * 
	 * @param gameInfo Ein ComObject_gameinfo Objekt, aus welchem Informationen entnommen werden.
	 * 
	 * @author Peter Dörr
	 * @since 10.01.13
	 */
	public String checkForWinningPlayer(ComObject_gameinfo gameInfo){
		//Überprüfen, ob der Server einen Gewinner festgestellt hat (da dieser nicht mehr über den normalen Weg ermittelt werden kann, wenn ein anderer Spieler das Spiel beendet hat)
		if(!gameInfo.getWinnerID().equals("")){
			Game.logger.debug("WinConditions.checkForWinningPlayer: Spieler hat gewonnen: " + gameInfo.getWinnerID());
			return gameInfo.getWinnerID();
		}
		
		//Überprüfen, wenn Auslöschung aller Feine eine Siegesoption ist
		if(extinction){
			boolean[] playerHasUnits = new boolean[playerIDs.size()];
			for(int i = 0; i < playerHasUnits.length; i++){
				playerHasUnits[i] = false;
			}
			
			//Einheiten-Besitzer mit playerNames vergleichen
			for(int i = 0; i < playingField.units.size(); i++){
				for(int j = 0; j < playerIDs.size(); j++){
					if(playingField.units.get(i).ownerID != null){
						if(playingField.units.get(i).ownerID.equals(playerIDs.get(j))){
							playerHasUnits[j] = true;
						}
					}
				}
			}
			
			//Überprüfen, ob nur noch ein Spieler Einheiten besitzt, wenn ja, ist er der Sieger
			int count = 0;
			for(int i = 0; i < playerHasUnits.length; i++){
				if(playerHasUnits[i]){
					count++;
				}
			}
			if(count == 1){
				for(int i = 0; i < playerHasUnits.length; i++){
					if(playerHasUnits[i]){
						Game.logger.debug("WinConditions.checkForWinningPlayer: Spieler hat gewonnen: " + playerIDs.get(i));
						return playerIDs.get(i);
					}
				}
			}
		}
		
		//Überprüfen, wenn Warentransport eine Siegesoption ist
		if(goodPlacement){
			boolean[] goodPlacementsFulfilled = new boolean[goodPlacements.get(0).size()];
			for(int i = 0; i < goodPlacementsFulfilled.length; i++){
				goodPlacementsFulfilled[i] = false;
			}
			
			//Erfüllte Wartentransporte überprüfen
			for(int i = 0; i < goodPlacements.get(0).size(); i++){
				//Keine Einheit auf dem Zielfeld
				if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)) == null){
					continue;
				}
				
				//Einheit auf dem Zielfeld gehört nicht dem Spieler, der dort Waren abzuliefern hat
				if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).ownerID == null){
					continue;
				}
				if(!game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).ownerID.equals(playerIDs.get(goodPlacements.get(2).get(i)))){
					continue;
				}
				
				//Einheit auf dem Zielfeld hat nicht die benötigten Waren
				switch(goodPlacements.get(0).get(i)){
				case 1:
					if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).cargo_1 < goodPlacements.get(1).get(i)){
						continue;
					}
					break;
				case 2:
					if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).cargo_2 < goodPlacements.get(1).get(i)){
						continue;
					}
					break;
				case 3:
					if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).cargo_3 < goodPlacements.get(1).get(i)){
						continue;
					}
					break;
				case 4:
					if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).cargo_4 < goodPlacements.get(1).get(i)){
						continue;
					}
					break;
				case 5:
					if(game.getUnitByCoordinates(goodPlacements.get(3).get(i), goodPlacements.get(4).get(i)).cargo_5 < goodPlacements.get(1).get(i)){
						continue;
					}
					break;
				}
				
				//Alle Bedinungen erfüllt
				goodPlacementsFulfilled[i] = true;
			}
			
			//Ermitteln, ob ein Spieler den Sieg durch Warentransporte errungen hat
			int[] count = new int[playerIDs.size()];
			for(int i = 0; i < goodPlacementsFulfilled.length; i++){
				if(goodPlacementsFulfilled[i]){
					count[goodPlacements.get(2).get(i)] += 1;
				}
			}
			for(int i = 0; i < count.length; i++){
				if(count[i] >= goodPlacementCount){
					Game.logger.debug("WinConditions.checkForWinningPlayer: Spieler hat gewonnen: " + playerIDs.get(i));
					return playerIDs.get(i);
				}
			}
		}
		
		//Kein Spieler hat bisher gewonnen
		return null;
	}
}
