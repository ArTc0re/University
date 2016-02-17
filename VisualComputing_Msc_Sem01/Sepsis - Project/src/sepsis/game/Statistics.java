package sepsis.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Zeichnet Statistiken auf und speichert diese in einer Datei.
 * 
 * @author Peter Dörr
 * @since 09.01.13
 */
public class Statistics {
	
	/**
	 * Anzahl der Züge, die der Spiele in diesem Spiel gemacht hat.
	 */
	private static int   game_player_moves = 0;
	/**
	 * Anzahl der Züge, die die Einheiten in diesem Spiel gemacht haben.
	 */
	private static int[] game_units_moves = {0, 0, 0, 0, 0};
	/**
	 * Anzahl der vom Spieler in diesem Spiel getöteten Einheiten.
	 */
	private static int   game_player_damage = 0;
	/**
	 * Anzahl der von eigenen Einheiten in diesem Spiel getöteten Einheiten.
	 */
	private static int[] game_units_damage = {0, 0, 0, 0, 0};
	/**
	 * Anzahl der erfolgreichen Handelsangebote, die der Spiele in diesem Spiel gemacht hat.
	 */
	private static int   game_player_trades = 0;
	/**
	 * Anzahl der erfolgreichen Handelsangebote, die die Einheiten in diesem Spiel gemacht haben.
	 */
	private static int[] game_units_trades = {0, 0, 0, 0, 0};
	/**
	 * Spielzeit des Spielers in diesem Spiel.
	 */
	private static int   game_player_wastedTime = 0;
	
	/**
	 * Anzahl der Züge, die der Spieler in allen Spielen gemacht hat.
	 */
	private static int   all_player_moves = 0;
	/**
	 * Anzahl der Züge, die die Einheiten in allen Spielen gemacht haben.
	 */
	private static int[] all_units_moves = {0, 0, 0, 0, 0};
	/**
	 * Anzahl der vom Spieler in allen Spielen getöteten Einheiten.
	 */
	private static int   all_player_damage = 0;
	/**
	 * Anzahl der von eigenen Einheiten in allen Spielen getöteten Einheiten.
	 */
	private static int[] all_units_damage = {0, 0, 0, 0, 0};
	/**
	 * Anzahl der erfolgreichen Handelsangebote, die der Spiele in allen Spielen gemacht hat.
	 */
	private static int   all_player_trades = 0;
	/**
	 * Anzahl der erfolgreichen Handelsangebote, die die Einheiten in allen Spielen gemacht haben.
	 */
	private static int[] all_units_trades = {0, 0, 0, 0, 0};
	/**
	 * Spielzeit des Spielers in allen Spielen.
	 */
	private static int   all_player_wastedTime = 0;
	
	
	
	/**
	 * Erzeugt eine neue statistics.dat Datei, falls noch keine existiert und liest die dort enthalten Informationen aus.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public Statistics(){
		//Überprüfen, ob eine Statistik-Datei existiert und legt gegebenenfalls eine neue Datei an.
		File file = new File("data/config/statistics.dat");
		if(file.exists()){
			readStatisticsFromFile();
		} else {
			writeStatisticsToFile();
		}
	}
	
	
	
	/**
	 * Aufzurufen, nachdem sich eine Einheit bewegt hat.
	 * 
	 * @param unit Die sich bewegende Einheit.
	 * @param playerID Die PlayerID des Clients, die zum Abgleich mit dem Owner der Einheit benötigt wird.
	 * @param range Felder, die sich die Einheit bewegt hat.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public void unitMoved(Unit unit, String playerID, int range){
		
		if(unit.ownerID.equals(playerID)){
			game_player_moves += range;
			all_player_moves += range;
			
			switch(unit.type){
			case Fighter_1:
				game_units_moves[0] += range;
				all_units_moves[0] += range;
				break;
			case Fighter_2:
				game_units_moves[1] += range;
				all_units_moves[1] += range;
				break;
			case Fighter_3:
				game_units_moves[2] += range;
				all_units_moves[2] += range;
				break;
			case Transporter_1:
				game_units_moves[3] += range;
				all_units_moves[3] += range;
				break;
			case Transporter_2:
				game_units_moves[4] += range;
				all_units_moves[4] += range;
				break;
			default:
				break;
			}
			
			writeStatisticsToFile();
		}
	}
	
	
	
	/**
	 * Aufzurufen, nachdem eine Einheit eine andere Einheit angegriffen hat.
	 * 
	 * @param unit Die angreifende Einheit.
	 * @param playerID Die PlayerID des Clients, die zum Abgleich mit dem Owner der Einheit benötigt wird.
	 * @param damage Der verursachte Schaden.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public void unitDamagedAnUnit(Unit unit, String playerID, int damage){
		if(unit.ownerID.equals(playerID)){
			game_player_damage += damage;
			all_player_damage += damage;
			
			switch(unit.type){
			case Fighter_1:
				game_units_damage[0] += damage;
				all_units_damage[0] += damage;
				break;
			case Fighter_2:
				game_units_damage[1] += damage;
				all_units_damage[1] += damage;
				break;
			case Fighter_3:
				game_units_damage[2] += damage;
				all_units_damage[2] += damage;
				break;
			case Transporter_1:
				game_units_damage[3] += damage;
				all_units_damage[3] += damage;
				break;
			case Transporter_2:
				game_units_damage[4] += damage;
				all_units_damage[4] += damage;
				break;
			default:
				break;
			}
			
			writeStatisticsToFile();
		}
	}
	
	
	
	/**
	 * Aufzurufen, nachdem eine Einheit erfolgreich gehandelt hat.
	 * 
	 * @param unit Die anhandelnde Einheit.
	 * @param playerID Die PlayerID des Clients, die zum Abgleich mit dem Owner der Einheit benötigt wird.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public void unitTraded(Unit unit, String playerID){
		if(unit.ownerID.equals(playerID)){
			game_player_trades += 1;
			all_player_trades += 1;
			
			switch(unit.type){
			case Fighter_1:
				game_units_trades[0] += 1;
				all_units_trades[0] += 1;
				break;
			case Fighter_2:
				game_units_trades[1] += 1;
				all_units_trades[1] += 1;
				break;
			case Fighter_3:
				game_units_trades[2] += 1;
				all_units_trades[2] += 1;
				break;
			case Transporter_1:
				game_units_trades[3] += 1;
				all_units_trades[3] += 1;
				break;
			case Transporter_2:
				game_units_trades[4] += 1;
				all_units_trades[4] += 1;
				break;
			default:
				break;
			}
			
			writeStatisticsToFile();
		}
	}
	
	
	
	/**
	 * Fügt die angegebene Zeit der Gesamtspielzeit hinzu.
	 * Diese Methode sollte in der update-Methode der Game.java aufgerufen werden.
	 * ACHTUNG: Bei Aufruf der Methode wird die Spielzeit explizit NICHT direkt in der statistics.dat Datei gespeichert,
	 * dies geschieht nur bei Aufruf von unitMoved, unitKilledAnUnit, unitTraded.
	 * 
	 * @param seconds Anzahl Sekunden, sie seit dem letzten Aufruf gespielt wurden.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public void addTime(int seconds){
		game_player_wastedTime += seconds;
		all_player_wastedTime += seconds;
	}
	
	
	
	/**
	 * Schreibt die spielübergreifenden Statistiken in die statistics.dat Datei.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public void writeStatisticsToFile(){
		//Alte Datei löschen
		File file = new File("data/config/statistics.dat");
		if(file.exists()){
			file.delete();
		}
		file = null;
		
		try {
			//Neue Datei erzeugen
			FileWriter fileWriter = new FileWriter("data/config/statistics.dat");
			
			//Statistiken in Datei schreiben
			fileWriter.write(String.valueOf(all_player_moves));
			fileWriter.write(",");
			for(int i = 0; i < all_units_moves.length; i++){
				fileWriter.write(String.valueOf(all_units_moves[i]));
				fileWriter.write(",");
			}
			fileWriter.write(String.valueOf(all_player_damage));
			fileWriter.write(",");
			for(int i = 0; i < all_units_damage.length; i++){
				fileWriter.write(String.valueOf(all_units_damage[i]));
				fileWriter.write(",");
			}
			fileWriter.write(String.valueOf(all_player_trades));
			fileWriter.write(",");
			for(int i = 0; i < all_units_trades.length; i++){
				fileWriter.write(String.valueOf(all_units_trades[i]));
				fileWriter.write(",");
			}
			fileWriter.write(String.valueOf(all_player_wastedTime));
			
			fileWriter.close();
		} catch (IOException e) {
			return;
		}
	}
	
	
	
	/**
	 * Liest die spielübergreifenden Statistiken aus der statistics.dat Datei.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	private void readStatisticsFromFile(){
		//Überprüfen, ob Datei existiert
		File file = new File("data/config/statistics.dat");
		if(!file.exists()){
			file = null;
			return;
		}
		file = null;
		
		//Informationen auslesen
		try{
			FileReader fileReader = new FileReader("data/config/statistics.dat");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String tmp;
			String text = "";
			
			while((tmp = bufferedReader.readLine()) != null){
				text += tmp;
			}
			
			all_player_moves = Integer.parseInt(text.substring(0, text.indexOf(",")));
			text = text.substring(text.indexOf(",") + 1);
			for(int i = 0; i < all_units_moves.length; i++){
				all_units_moves[i] = Integer.parseInt(text.substring(0, text.indexOf(",")));
				text = text.substring(text.indexOf(",") + 1);
			}
			all_player_damage = Integer.parseInt(text.substring(0, text.indexOf(",")));
			text = text.substring(text.indexOf(",") + 1);
			for(int i = 0; i < all_units_damage.length; i++){
				all_units_damage[i] = Integer.parseInt(text.substring(0, text.indexOf(",")));
				text = text.substring(text.indexOf(",") + 1);
			}
			all_player_trades = Integer.parseInt(text.substring(0, text.indexOf(",")));
			text = text.substring(text.indexOf(",") + 1);
			for(int i = 0; i < all_units_trades.length; i++){
				all_units_trades[i] = Integer.parseInt(text.substring(0, text.indexOf(",")));
				text = text.substring(text.indexOf(",") + 1);
			}
			all_player_wastedTime = Integer.parseInt(text);
			
			bufferedReader.close();
			fileReader.close();

		} catch (IOException e) {
			return;
		}
	}



	/**
	 * Getter game_player_moves;
	 * 
	 * @return Anzahl der Züge, die der Spiele in diesem Spiel gemacht hat.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getGame_player_moves() {
		return game_player_moves;
	}



	/**
	 * Getter game_units_moves;
	 * 
	 * @return Anzahl der Züge, die die Einheiten in diesem Spiel gemacht haben.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getGame_units_moves() {
		return game_units_moves;
	}



	/**
	 * Getter game_player_damage;
	 * 
	 * @return Anzahl des vom Spieler in diesem Spiel verursachten Schadens.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getGame_player_damage() {
		return game_player_damage;
	}



	/**
	 * Getter game_units_damage;
	 * 
	 * @return Anzahl des von eigenen Einheiten in diesem Spiel verursachten Schadens.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getGame_units_damage() {
		return game_units_damage;
	}



	/**
	 * Getter game_player_trades;
	 * 
	 * @return Anzahl der erfolgreichen Handelsangebote, die der Spiele in diesem Spiel gemacht hat.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getGame_player_trades() {
		return game_player_trades;
	}



	/**
	 * Getter game_units_trades;
	 * 
	 * @return Anzahl der erfolgreichen Handelsangebote, die die Einheiten in diesem Spiel gemacht haben.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getGame_units_trades() {
		return game_units_trades;
	}



	/**
	 * Getter game_player_wastedTime;
	 * 
	 * @return Spielzeit des Spielers in diesem Spiel.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getGame_player_wastedTime() {
		return game_player_wastedTime;
	}



	/**
	 * Getter all_player_moves;
	 * 
	 * @return Anzahl der Züge, die der Spiele in allen Spielen gemacht hat.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getAll_player_moves() {
		return all_player_moves;
	}



	/**
	 * Getter all_units_moves;
	 * 
	 * @return Anzahl der Züge, die die Einheiten in allen Spielen gemacht haben.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getAll_units_moves() {
		return all_units_moves;
	}



	/**
	 * Getter all_player_damage;
	 * 
	 * @return Anzahl des vom Spieler in allen Spielen verursachten Schadens.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getAll_player_damage() {
		return all_player_damage;
	}



	/**
	 * Getter all_units_damage;
	 * 
	 * @return Anzahl des von eigenen Einheiten in allen Spielen verursachten Schadens.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getAll_units_damage() {
		return all_units_damage;
	}



	/**
	 * Getter all_player_trades;
	 * 
	 * @return Anzahl der erfolgreichen Handelsangebote, die der Spiele in allen Spielen gemacht hat.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getAll_player_trades() {
		return all_player_trades;
	}



	/**
	 * Getter all_units_trades;
	 * 
	 * @return Anzahl der erfolgreichen Handelsangebote, die die Einheiten in allen Spielen gemacht haben.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int[] getAll_units_trades() {
		return all_units_trades;
	}



	/**
	 * Getter all_player_wastedTime;
	 * 
	 * @return Spielzeit des Spielers in allen Spielen.
	 * 
	 * @author Peter Dörr
	 * @since 09.01.13
	 */
	public int getAll_player_wastedTime() {
		return all_player_wastedTime;
	}
}
