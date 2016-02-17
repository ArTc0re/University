package sepsis.gui;

import java.awt.Point;
import java.util.ArrayList;
import sepsis.audio.Audio.MUSIC;
import sepsis.audio.Audio.SOUND;
import sepsis.config.MeepleConfig;
import sepsis.config.MeepleSetup;
import sepsis.game.Game;
import sepsis.game.Unit;
import sepsis.game.UnitSetup;
import sepsis.game.UnitType;
import sepsis.game.WinConditions;
import sepsis.network.ComObject_chat;
import sepsis.network.ComObject_delPlayer;
import sepsis.network.ComObject_gameinfo;
import sepsis.network.ComObject_getData;
import sepsis.network.ComObject_removeGame;
import sepsis.network.ComObject_startGame;
import sepsis.network.ComObject_terrainMap;
import sepsis.network.ComObject_unitMap;
import sepsis.network.ComObject_unitinfo;
import sepsis.network.ComObject_unittype;
import sepsis.network.ComObject_winconditions;
import sepsis.network.IComObject.GAMESTATUS;
import sepsis.network.IComObject.STATUS;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Diese Klasse kontrolliert den SepsisWaitForPlayersScreen.<br>
 * 
 * @author Markus Strobel<br>
 * @since 16.12.2012<br>
 * 
 * 08.01.2013 (Markus Strobel) kickSelectedPlayer() edited.<br>
 * 
 * 08.01.2013 (Markus Strobel) removeGame added to backToGameSetup().<br>
 */
public class SepsisWaitForPlayersScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	@SuppressWarnings("unused")
	private AppStateManager stateManager;	
	private Game game;
	
	private boolean gameStarted = false;
	private float updateTimer = 0f;
	
	// Connected Players List
	private ListBox<String> connectedPlayersListBox;
	
	// Chat Window
	private ListBox<String> chatReceiveListBox;
	private TextField chatSendTextField;
	private Button sendButton;
	private DropDown<String> receiverDropDown;
	
	// loadingPopup
	private Element loadingPopup;
	@SuppressWarnings("unused")
	private boolean loadingScreen = false;
	private boolean privateChat = false;
	
	private ArrayList<String> connectedPlayersIDs;
	private ArrayList<String> connectedPlayerNames;
	
	/**
	 * Der Konstruktor des SepsisWaitForPlayersScreenController
	 * 
	 * @author Markus Strobel
	 * @since 13.12.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 * 
	 * 18.12.2012 (Markus Strobel) GUI Symbiose mit Netzwerk/Game hinzugefügt.<br>
	 * 
	 */
	public SepsisWaitForPlayersScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.stateManager = stateManager;
		this.nifty = nifty;	
		this.game = game;
		
		connectedPlayersIDs = new ArrayList<String>();
		connectedPlayerNames = new ArrayList<String>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.screen = screen;
		loadingPopup = nifty.createPopup("loadingScreenPopup");
		
		connectedPlayersListBox = screen.findNiftyControl("playersListBox", ListBox.class);

		chatReceiveListBox = screen.findNiftyControl("chatReceiveListBox", ListBox.class);
		chatReceiveListBox.disable();
		chatReceiveListBox.setFocusable(false);
		chatSendTextField = screen.findNiftyControl("chatSendTextField", TextField.class);
		sendButton = screen.findNiftyControl("sendButton", Button.class);
		sendButton.setFocusable(false);
		receiverDropDown = screen.findNiftyControl("receiverDropDown", DropDown.class);
		receiverDropDown.addItem("Server");
		receiverDropDown.addItem("Game");
		

		
	}

	@Override
	public void onEndScreen() {		
	}

	@Override
	public void onStartScreen() {
		getConnectedPlayers();
	}

	/** 
	 * Diese Methode schließt das Ladescreen Popup
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.01.2013<br>
	 */
	public void openLoadingScreenPopup()
	{
		nifty.showPopup(screen, loadingPopup.getId(), null);
		loadingScreen = true;
	}
	
	/** 
	 * Diese Methode schließt das Ladescreen Popup
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.01.2013<br>
	 */
	public void closeLoadingScreenPopup()
	{
		nifty.closePopup(loadingPopup.getId());
	}
	
	
	/**
	 * Diese Methode ist für kickPlayer gedacht, um dem gekickten Spieler mitzuteilen, dass er gekickt wurde und ihn einfacher zurück ins SetupMenu zu verfrachten.<br>
	 * @param kickMessage the info message that the player was kicked by the game owner
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 */
	public void backToGameSetup(String kickMessage)
	{
		nifty.gotoScreen("gameSetupScreen");	
		game.setupScreen.addChatMessage(kickMessage);
	}
	
	/**
	 * Mit dieser Methode kommt man zurück zum SetupScreen.<br>
	 * WICHTIG: Der Server unterstützt diese Funktion nicht wirklich.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 * 
	 * 08.01.2013 (Markus Strobel) removeGame added.<br>
	 */
	public void back()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		// Spiel löschen/entfernen
		if(game.gameOwner)
		{
			try 
			{
				ComObject_removeGame removeGame = (ComObject_removeGame)game.network.establishCommunication(new ComObject_removeGame(Game.clientID, Game.clientKey, Game.gameID, Game.clientKey));
			
				if(removeGame.getStatus().equals(STATUS.OK))
				{
					Game.logger.info("SepsisWaitForPlayersScreenController.backToGameSetup().removeGame: Game with GameID: " + Game.gameID + " successfully removed");
				}
				else
				{
					Game.logger.error("SepsisWaitForPlayersScreenController.backToGameSetup().removeGame: " + removeGame.getErrorInfo());
				}			
			} 
			catch (Exception e) 
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.backToGameSetup(): if catch: " + e.getMessage());
			}
		}
		else // Spieler der gejoint ist, löscht sich selbst.
		{
			try 
			{
				ComObject_delPlayer delPlayer = (ComObject_delPlayer)game.network.establishCommunication(new ComObject_delPlayer(Game.clientID, Game.clientKey, Game.gameID, Game.playerID, Game.playerID, null));
			
				if(delPlayer.getStatus().equals(STATUS.OK))
				{
					Game.logger.info("SepsisWaitForPlayersScreenController.backToGameSetup().delPlayer: Joined Player with ID: " + Game.playerID + " successfully removed");
				}
				else
				{
					Game.logger.error("SepsisWaitForPlayersScreenController.backToGameSetup().delPlayer: " + delPlayer.getErrorInfo());
				}			
			
			} 
			catch (Exception e) 
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.backToGameSetup(): else catch: " + e.getMessage());
			}
		}
		nifty.gotoScreen("gameSetupScreen");
	}
	
	/**
	 * Diese Methode kickt den ausgewählten Spieler<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 * 
	 * 08.01.2013 (Markus Strobel) Funktionalität hinzugefügt.<br>
	 */
	public void kickSelectedPlayer()	
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		if(!connectedPlayersListBox.getSelection().get(0).isEmpty())
		{
			try 
			{
				String delPlayerID = connectedPlayersListBox.getSelection().get(0);
				int PlayerIDIndex = connectedPlayerNames.indexOf(delPlayerID);
				delPlayerID = connectedPlayersIDs.get(PlayerIDIndex);

				// Damit man sich nicht selbst kicken kann und das Spiel als totes Spiel ohne game owner noch offen bleibt
				if(!delPlayerID.equals(Game.playerID))
				{
					ComObject_delPlayer delPlayer = (ComObject_delPlayer)game.network.establishCommunication(new ComObject_delPlayer(Game.clientID, Game.clientKey, Game.gameID, Game.clientKey, delPlayerID , Game.clientKey));

					if(delPlayer.getStatus().equals(STATUS.OK))
					{
						Game.logger.info("SepsisWaitForPlayersScreenController.kickSelectedPlayer().delPlayer: kicked Player with ID: " +  delPlayerID);
						connectedPlayersListBox.removeItem(delPlayerID);
					}
					else
					{
						Game.logger.error("SepsisWaitForPlayersScreenController.kickSelectedPlayer().delPlayer: " + delPlayer.getErrorInfo());
					}
				}
				else
				{
					chatReceiveListBox.addItem("you can't kick yourself");
					
					chatReceiveListBox.refresh();
					chatReceiveListBox.selectItemByIndex(chatReceiveListBox.itemCount() - 1);

					chatSendTextField.setText("");		
					chatReceiveListBox.refresh();
				}
			} 
			catch (Exception e) 
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.kickSelectedPlayer(): catch: " + e.getMessage());
			}
			
			
			
		}
	}
	
	/**
	 * Diese Methode startet das Spiel und sendet die notwendigen Informationen an den Server.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	public void startGame()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		try 
		{
			ComObject_gameinfo gameInfo = (ComObject_gameinfo)game.network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));

			if(gameInfo.getStatus().equals(STATUS.OK))
			{
				Game.playersList = gameInfo.getPlayerNames();
				Game.playerIDs = gameInfo.getPlayerIDs();
				
				ComObject_startGame startGame = (ComObject_startGame)game.network.establishCommunication(new ComObject_startGame(Game.clientID, Game.clientKey, Game.gameID, Game.clientKey));

				if(connectedPlayersListBox.getItems().size() == game.currentsMapPlayersCount)
				{
					if(startGame.getStatus().equals(STATUS.OK))
					{
						Game.logger.info("SepsisWaitForPlayersScreenController.startGame().startGame: SPIEL GESTARTET - STATUS: " + startGame.getStatus().toString());
						gameStarted = true;
					}
					else
					{
						Game.logger.error("SepsisWaitForPlayersScreenController.startGame().startGame: " + startGame.getErrorInfo());
					}
				}
				else
				{
					chatReceiveListBox.addItem("not enough players to start the game (" + connectedPlayersListBox.getItems().size() + "/" + game.currentsMapPlayersCount + ")");

					chatReceiveListBox.refresh();
					chatReceiveListBox.selectItemByIndex(chatReceiveListBox.itemCount() - 1);

					chatSendTextField.setText("");		
					chatReceiveListBox.refresh();
				}
			}
			else
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.startGame().gameInfo: " + gameInfo.getErrorInfo());
			}
		} 
		catch (Exception e)
		{
			Game.logger.error("SepsisWaitForPlayersScreenController.startGame(): catch: " + e.getMessage());
		}
	}
	
	/**
	 * Diese Methode überprüft die Spieler, welche dem Spiel beigetreten sind.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	public void getConnectedPlayers()
	{				
		try 
		{
			// FÜR DIE CONNECTED PLAYERS LISTE
			ComObject_gameinfo gameInfo = (ComObject_gameinfo)game.network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
			
			if(gameInfo.getStatus().equals(STATUS.OK))
			{				
				// Liste der Spieler empfangen und sie der Listbox hinzufügen	
				
				connectedPlayersIDs = gameInfo.getPlayerIDs();
				connectedPlayerNames = gameInfo.getPlayerNames();
				
				// Spieler die connected sind, aber noch nicht in der Listbox sind, werden hier hinzugefügt
				for(int i = 0; i < connectedPlayerNames.size(); i++)
				{					
					// Wenn ein Spieler der connectedPlayers-Liste nicht in der connectedPlayersListBox ist, dann wird er hinzugefügt
					if(!connectedPlayersListBox.getItems().contains(connectedPlayerNames.get(i)))
					{
						connectedPlayersListBox.addItem(connectedPlayerNames.get(i));
						
					}	
					
					// Wenn ein Spieler der connectedPlayers-Liste nicht in der receiverDropDown ist, dann wird er hinzugefügt
					if(!receiverDropDown.getItems().contains(connectedPlayerNames.get(i)) && !connectedPlayerNames.get(i).equals(Game.playerName))
					{
						receiverDropDown.addItem(connectedPlayerNames.get(i));
					}			
				}
				
				for(int i = 0; i < connectedPlayersListBox.getItems().size(); i++)
				{					
					// Wenn ein Spieler der connectedPlayersListBox nicht mehr in der connectedPlayers-Liste ist, dann wird er entfernt
					if(!connectedPlayerNames.contains(connectedPlayersListBox.getItems().get(i)))
					{
						connectedPlayersListBox.removeItemByIndex(i);
					}	
				}
				
				for(int i = 0; i < receiverDropDown.getItems().size(); i++)
				{				
					if(!receiverDropDown.getItems().get(i).equals("Game") && !receiverDropDown.getItems().get(i).equals("Server"))
					{
						// Wenn ein Spieler der receiverDropDown-Liste nicht mehr in der connectedPlayers-Liste ist, dann wird er entfernt
						if(!connectedPlayerNames.contains(receiverDropDown.getItems().get(i)))
						{
							receiverDropDown.removeItemByIndex(i);
						}							
					}
				}	
				
			}
			else
			{
				try 
				{
					backToGameSetup("you have been kicked from the game owner");
				} 
				catch (Exception e) 
				{
					Game.logger.error("SepsisWaitForPlayersScreenController.getConnectedPlayers().gameInfo: could not get gameInfo object and could not call backToGameSetup(): " + gameInfo.getErrorInfo());
				}
			}
	
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisWaitForPlayersScreenController.getConnectedPlayers(): catch: " + e.getMessage());
		}		
	}
	
	/**
	 * Diese Methode überprüft ob das Spiel gestartet wurde.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	public void getGameStartedInfo()
	{
		try 
		{
			ComObject_gameinfo gameInfo = (ComObject_gameinfo)game.network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));

			if(gameInfo.getStatus().equals(STATUS.OK))
			{
				if(gameInfo.getGameStatus().equals(GAMESTATUS.STARTED))
				{
					gameStarted = true;
				}			
			}
			else
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.getConnectedPlayers().gameInfo: " + gameInfo.getErrorInfo());
			}		
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisWaitForPlayersScreenController.getConnectedPlayers(): catch: " + e.getMessage());
		}		
	}
	
	/**
	 * Diese Methode initiiert die wesentlichen Spielbestandteile.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	private void initGame()
	{
		try
		{	
//			openLoadingScreenPopup();
			
			//Musik wechseln
			game.audio.playMusic(MUSIC.PLAYINGFIELD_MUSIC, 0);
			
			//Die ersten GameInfos abfragen.
			ComObject_gameinfo gameInfo = (ComObject_gameinfo)game.network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
			
			//Setzen der 1. Rundennummer
			game.RoundNumber = gameInfo.getTurn();
			
			// speichern der mapID
			Game.mapID = gameInfo.getMapID();

			//Spielernamen abfragen
			Game.playerIDs = gameInfo.getPlayerIDs();
			Game.playersList = gameInfo.getPlayerNames();

			//Einheitentypen abfragen
			MeepleConfig meepleConfig = new MeepleConfig();						
			ArrayList<UnitType> unitTypesList = meepleConfig.parseFromNetwork((ComObject_unittype)game.network.establishCommunication(new ComObject_unittype(Game.clientID, Game.clientKey, gameInfo.getMapID())));

			//Karteninformationen beziehen und Karte erstellen
			ComObject_terrainMap terrainMap = (ComObject_terrainMap)game.network.establishCommunication(new ComObject_terrainMap(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
			
			Game.playingField.height = terrainMap.getTerrainMap().get(0).size();
			Game.playingField.width = terrainMap.getTerrainMap().size();	
			
			int [][] map = new int[Game.playingField.width][Game.playingField.height];
			for(int spalte = 0; spalte < terrainMap.getTerrainMap().get(0).size(); spalte++)
			{	
				for(int zeile = 0; zeile < terrainMap.getTerrainMap().size(); zeile++)
				{
					map[zeile][spalte] = terrainMap.getTerrainMap().get(zeile).get(spalte);
				}
			}
			
			Game.playingField.map = map;
			Game.playingField.visibility = new boolean[Game.playingField.width][Game.playingField.height];
			for(int x = 0; x < Game.playingField.width; x++){
				for(int y = 0; y < Game.playingField.height; y++){
					Game.playingField.visibility[x][y] = true;
				}
			}

			//Einheiteninformationen beziehen
			ComObject_unitMap unitMap = (ComObject_unitMap)game.network.establishCommunication(new ComObject_unitMap(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
			ArrayList<Integer> unitIDsList = new ArrayList<Integer>();
			ArrayList<Point> unitPointsList = new ArrayList<Point>();						
			ArrayList<ArrayList<Integer>> unitmap = unitMap.getUnitMap();

			
			
			for(int spalte = 0; spalte < unitmap.get(0).size(); spalte++)
			{	
				for(int zeile = 0; zeile < unitmap.size(); zeile++)
				{
					//Wenn der Wert der Karte nicht 0 ist, 
					//dann ist es eine Einheiten ID und diese wird der IDsList hinzugefügt und deren Punkt wird gespeichert.
					if(!(unitmap.get(zeile).get(spalte) == 0))
					{
						unitIDsList.add(unitmap.get(zeile).get(spalte));
						unitPointsList.add(new Point(zeile, spalte));
					}			
				}			
			}
			//Für Jede Einheiten ID eine unitInfo erstellen und sie der unitInfosList hinzufügen.
			ArrayList<ComObject_unitinfo> unitInfosList = new ArrayList<ComObject_unitinfo>();
			for(int i = 0; i < unitIDsList.size(); i++)
			{
				ComObject_unitinfo unitinfo = (ComObject_unitinfo)game.network.establishCommunication(new ComObject_unitinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID, unitIDsList.get(i)));
				unitInfosList.add(unitinfo);					
			}
			MeepleSetup meepleSetup = new MeepleSetup();			
			ArrayList<UnitSetup> unitSetupsList = meepleSetup.parseFromNetwork(unitInfosList, unitIDsList, unitPointsList);
			Game.playingField.units = meepleSetup.initUnits(unitTypesList, unitSetupsList);

			for(int i = 0; i < Game.playingField.units.size(); i++){
				ComObject_unitinfo unitinfo = (ComObject_unitinfo)game.network.establishCommunication(new ComObject_unitinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID, unitIDsList.get(i)));
				
				if(unitinfo.getStatus() == STATUS.OK){
					Unit unit = game.getUnitByUnitID(unitinfo.getUnitID());
					
					if(unit.ownerID.equals(Game.playerID)){
						//Fracht aktualisieren
						unit.cargo_1 = unitinfo.getCargo().get(0);
						unit.cargo_2 = unitinfo.getCargo().get(1);
						unit.cargo_3 = unitinfo.getCargo().get(2);
						unit.cargo_4 = unitinfo.getCargo().get(3);
						unit.cargo_5 = unitinfo.getCargo().get(4);

						//Trefferpunkte und Bewegungsreichweite
						unit.hitpoints_curr = unitinfo.getHitpoints();
						unit.movement_curr = unitinfo.getMovement();
					} else {
						//Fracht aktualisieren
						unit.cargo_1 = 0;
						unit.cargo_2 = 0;
						unit.cargo_3 = 0;
						unit.cargo_4 = 0;
						unit.cargo_5 = 0;

						//Trefferpunkte und Bewegungsreichweite
						unit.hitpoints_curr = unit.hitpoints_max;
						unit.movement_curr = unit.movement_max;
					}
				}
			}
			
			// Spielfeld initialisieren
			game.initTerrain();
			
			//Gewinnbedingungen initialisieren
			ComObject_winconditions winconditions = (ComObject_winconditions)game.network.establishCommunication(new ComObject_winconditions(Game.clientID, Game.clientKey, gameInfo.getMapID()));
			game.winconditions = new WinConditions(winconditions, game, Game.playingField, gameInfo);
			
			//Zum Spiel Screen wechseln						
			nifty.gotoScreen("gameScreen");
			
			//Netzwerk Tread initialisieren
			game.networkThread.start();
			
			gameStarted = true;

			game.refreshUnits();
			game.checkTurn(gameInfo);
			game.setStatusBoxByUnitInfos();
			game.setHexagonBaseColorAll(2);
			game.setHexagonBorderColorAll(0);
			
//			closeLoadingScreenPopup();

		}
		catch (Exception e)
		{
			Game.logger.error("SepsisWaitForPlayersScreenController.initGame(): catch: " + e.getMessage());
		}		
	}
	
	
	/**
	 * Diese Methode gibt zurück, ob das ChatFenster zum Nachrichten senden, den Focus hat.<br>
	 * 
	 * @return Gibt true zurück, wenn ja, andernfalls false.<br>
	 * 
	 * @autor Markus Strobel<br>
	 * @since 12.12.2012<br>
	 */
	public boolean chatSendTextFieldHasFocus()
	{
		return chatSendTextField.hasFocus();
	}
	
	/**
	 * Diese Methode sendet die ChatNachricht in die Chatbox und übers Netzwerk.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 12.12.2012<br>
	 * 
	 * 20.12.2012 (Markus Strobel) Netzwerk-Kommunikation hinzugefügt.<br>
	 * 
	 * Quelle: timeStamp basiert auf folgender Quelle: http://www.java-forum.org/java-basics-anfaenger-themen/50032-uhrzeit-auslesen.html<br>
	 */
	public void sendChatMessage()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		//		SimpleDateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");			
		//		String chatMessage = String.valueOf(timeStamp.format(Calendar.getInstance().getTime()) + " " + playerName + ": " + chatSendTextField.getRealText());		

		String chatMessage = chatSendTextField.getRealText();	

		if(!chatMessage.isEmpty())
		{
			try 
			{
				String receiverGameID = "";
				String receiverPlayerID = "";

				// Hier wird die receiverPlayerID und receiverGameID zugewiesen.
				if(receiverDropDown.getSelection().equals("Server"))
				{
					receiverGameID = null;
					receiverPlayerID = null;
				}
				else if(receiverDropDown.getSelection().equals("Game"))
				{
					receiverGameID = Game.gameID;
					receiverPlayerID = null;
				}
				else
				{
					receiverGameID = Game.gameID;
					int receiverPlayerIDIndex = connectedPlayerNames.indexOf(receiverDropDown.getSelection());
					receiverPlayerID = connectedPlayersIDs.get(receiverPlayerIDIndex);
					privateChat = true;
				}

				ComObject_chat chat = (ComObject_chat)game.network.establishCommunication(new ComObject_chat(Game.clientID, Game.clientKey, Game.playerID, receiverGameID, receiverPlayerID, chatMessage));


				if(chat.getStatus().equals(STATUS.OK))
				{

					if(privateChat)
					{
						chatReceiveListBox.addItem("ME: " + chatMessage);
						privateChat = false;
					}
					
					chatReceiveListBox.refresh();
					chatReceiveListBox.selectItemByIndex(chatReceiveListBox.itemCount() - 1);

					chatSendTextField.setText("");		
					chatReceiveListBox.refresh();

					getChatMessages();
				}
				else
				{
					Game.logger.error("SepsisWaitForPlayersScreenController.sendChatMessage.ComObject_chat: " + chat.getErrorInfo());
				}
			}
			catch (Exception e) 
			{
				Game.logger.error("SepsisWaitForPlayersScreenController.sendChatMessage: catch: " + e.getMessage());
			}
		}
		
	}
	
	/**
	 * Diese Methode fragt die Chatnachrichten vom Server ab und übermittelt sie in die chatReceiveListBox des waitForPlayersScreen<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	private void getChatMessages()
	{
		try 
		{
			ComObject_getData getData = (ComObject_getData)game.network.establishCommunication(new ComObject_getData(Game.clientID, Game.clientKey));

			if(getData != null)
			{
				if(getData.getStatus().equals(STATUS.OK))
				{		
					if(getData.getMessages() != null)
					{
						if(getData.getMessages().size() > 0)
						{
							
							// Nachrichten abarbeiten.
							for(int i = 0; i < getData.getMessages().size(); i++)
							{
								// Empfänger == mein Name oder == null, dann kann ich die Nachricht bekommen
								if(getData.getPlayeridreciever().get(i).equals(Game.playerID) || getData.getPlayeridreciever().get(i).equals(""))
								{						
									// GameID == mein Spiel oder == null, dann kann ich die Nachricht bekommen
									if(getData.getGameID().get(i).equals(Game.gameID) || getData.getGameID().get(i).equals(""))
									{
										String finalMessage = "";
//										String timeStamp = "";
//										String timeStamp = getData.getIncommingTime().get(i);
//										String SystemInfo = getData.getDatatype().get(i).toString();
										
										
										int senderPlayerNameIndex = connectedPlayersIDs.indexOf(getData.getSenderplayerid().get(i));
										String senderName = connectedPlayerNames.get(senderPlayerNameIndex);

//										int playerNameReceiverIndex = connectedPlayersIDs.indexOf(getData.getPlayeridreciever().get(i));
//										String receiverName = connectedPlayerNames.get(playerNameReceiverIndex);										
										
										String messageContent = getData.getMessages().get(i);
										messageContent = messageContent.replace("Chat message:", "");	
										
										if(getData.getSenderplayerid().get(i).equals(Game.playerID))
										{
											senderName = "ME";
										}

										if(getData.getPlayeridreciever().get(i).equals("") && getData.getGameID().get(i).equals(""))
										{
											finalMessage = senderName + ": " + messageContent;
										}
										else if(getData.getGameID().get(i).equals(Game.gameID) && getData.getPlayeridreciever().get(i).equals(""))
										{											
											finalMessage = senderName + ": " + messageContent;
										}
										else if(getData.getGameID().get(i).equals(Game.gameID) && getData.getPlayeridreciever().get(i).equals(Game.playerID))
										{
											finalMessage = senderName + ": " + messageContent;
										}
										
										// Hinzufügen der Chat
										chatReceiveListBox.addItem(finalMessage);
										chatReceiveListBox.refresh();
										
										// Unterstes Element auswählen
										chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
										chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);

									}
								}
							}	
						}
					}
				}
				else
				{
					Game.logger.error("SepsisWaitForPlayersScreenController.sendChatMessage.ComObject_getData: " + getData.getErrorInfo());
				}
			}
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisWaitForPlayersScreenController.getChatMessages: catch: " + e.getMessage());
		}
	}

	/**
	 * Diese Methode updated Elemente des waitForPlayersScreen.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	@Override
	public void update(float tpf)
	{
		updateTimer += tpf;
		if(updateTimer > 1f)
		{
			// Prüft ob das Spiel gestartet wurde.
			if(!gameStarted && !game.gameOwner)
			{
				getGameStartedInfo();
			}

			updateTimer = 0;
			getConnectedPlayers();
			getChatMessages();
		}
		
		// Spiel starten, wenn gameStarted == true
		if(gameStarted)
		{
			initGame();		
			
			game.handleMainMenuAnimation = false;
			game.handleBloodAnimation = true;
			game.gameStarted = true;			
			
			nifty.gotoScreen("gameScreen");
		}
		
	}
	
}


