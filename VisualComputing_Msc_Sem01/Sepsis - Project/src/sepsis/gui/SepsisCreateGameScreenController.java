package sepsis.gui;

import sepsis.audio.Audio.SOUND;
import sepsis.game.Game;
import sepsis.game.Minimap;
import sepsis.network.ComObject_addPlayer;
import sepsis.network.ComObject_createGame;
import sepsis.network.ComObject_gameinfo;
import sepsis.network.ComObject_gamelist;
import sepsis.network.ComObject_maplist;
import sepsis.network.ComObject_mappreview;
import sepsis.network.ComObject_winconditions;
import sepsis.network.IComObject.STATUS;
import sepsis.network.Network;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.layout.align.HorizontalAlign;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;


/**
 * Diese Klasse kontrolliert den createGameScreen.<br>
 * 
 * @author Markus Strobel<br>
 * @since 13.12.2012<br>
 * 
 * 18.12.2012 (Markus Strobel) GUI Symbiose mit Netzwerk/Game hinzugefügt.<br>
 * 
 */
public class SepsisCreateGameScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	@SuppressWarnings("unused")
	private AppStateManager stateManager;	
	private Game game;
	private boolean inited = false;
	
	private boolean activeInfoText = false;
	private Element infoText;
	private float infoTextCounter = 0f;	
	
	// CREATE GAME SETTINGS
	private TextField gameName;
	private TextField players;
	
	// MAP DESCRIPTION
	private ListBox<String> mapDescriptionListBox;
	
	// MAP SETTINGS
	private DropDown<String> availableMapsList;
	private Element mapPanel;

	private Element winConditionsExtinctionText;
	private Element winConditionsGoodplacementText;
	
	private Minimap mapPreview;
	private static int mapPreviewCount = 0;
	
	private int selectedIndex = 0;
	private int oldSelectedIndex = 0;
	
	private String mapDescription = "";
	private int playersCount = 0;
	
	
	/**
	 * Der Konstruktor des SepsisCreateGameScreenController
	 * 
	 * @author Markus Strobel
	 * @since 13.12.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 * 
	 */
	public SepsisCreateGameScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.stateManager = stateManager;
		this.nifty = nifty;	
		this.game = game;
		
		//Mappreview
		mapPreview = new Minimap();
		mapPreview.setResolution(320);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {
		
		this.screen = screen;
		
		infoText = screen.findElementByName("infoText");
		infoText.hide();
		
		// CREATE GAME SETTINGS
		gameName = screen.findNiftyControl("gameID", TextField.class);	
		gameName.setText("game01");
		
		// MAP DESCRIPTION
		players = screen.findNiftyControl("playersCount", TextField.class);
		players.setText("2");
		players.disable();
		
		mapDescriptionListBox = screen.findNiftyControl("mapDescriptionListBox", ListBox.class);
		mapDescriptionListBox.setFocusable(false);
		mapDescriptionListBox.disable();
		
		
		// MAP SETTINGS
		mapPanel = screen.findElementByName("mapPanel");
		availableMapsList = screen.findNiftyControl("availableMapsDropDown", DropDown.class);
		winConditionsExtinctionText = screen.findElementByName("winConditionsExtinctionText");
		winConditionsGoodplacementText = screen.findElementByName("winConditionsGoodplacementText");
		
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {
		loadServerMapList();	
		inited = true;
		showMapPreviewInMapPanel();
	}
	
	/**
	 * Diese Methode zeigt infoTexte an, um irgend welche Nachrichten auf dem Bildschirm anzuzeigen.<br>
	 * 
	 * @param message der Text der Nachricht
	 * @param fontColor die Schriftfarbe der Nachricht
	 * 
	 * @author Markus Strobel<br>
	 * @since 21.02.2013<br>
	 */
	public void showInfoText(String message, Color fontColor)
	{
		infoText.show();
		infoText.getRenderer(TextRenderer.class).setText(message);
		infoText.getRenderer(TextRenderer.class).setColor(fontColor);
		activeInfoText = true;
	}
	
	/**
	 * Diese Methode lädt die Kartenliste des Servers und fügt sie der DropDownListe hinzu
	 * 
	 * @author Markus Strobel<br>
	 * @since 16.01.2013<br>
	 */
	public void loadServerMapList()
	{
		try 
		{
			ComObject_maplist mapList = (ComObject_maplist)game.network.establishCommunication(new ComObject_maplist(Game.clientID, Game.clientKey));
			
			if(mapList.getStatus().equals(STATUS.OK))
			{
				// Kartennamen der DropDown hinzufügen
				availableMapsList.clear();
				availableMapsList.addAllItems(mapList.getMaplist());					
			}
			else
			{
				Game.logger.error("SepsisCreateGameScreenController.loadServerMapList.mapList: " + mapList.getErrorInfo());
			}
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisCreateGameScreenController.loadServerMapList: catch: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Diese Methode wechselt das Vorschau-Bild der Karte
	 * 
	 * @author Markus Strobel<br>
	 * @since 16.01.2013<br>
	 */
	private void showMapPreviewInMapPanel()
	{
		// Vorschau Siegbedingungen
		getWinConditionsPreview(availableMapsList.getSelection());
		
		// Vorschau Bild
		ComObject_mappreview mappreview = null;
		try
		{

			mappreview = (ComObject_mappreview)game.network.establishCommunication(new ComObject_mappreview(Game.clientID, Game.clientKey, availableMapsList.getItems().get(availableMapsList.getSelectedIndex())));
		
		} 
		catch (Exception e){
			Game.logger.error("SepsisCreateGameScreenController.showMapPreviewInMapPanel: catch: " + e.getMessage());
		}
			if(mappreview.getStatus().equals(STATUS.OK))
			{		
				// Skalierung der Panelgröße an die Kartengröße
				// Basis Höhe = 100%, Breite = 50%				
				int height = mappreview.getTerrainmappreview().get(0).size();
				int width = mappreview.getTerrainmappreview().size();
				
				if(height > width)
				{
					float temp = 25f / height;
					
					height = (int) (height * temp);
					width = (int) (width * temp);
				}
				else if(width > height)
				{
					float temp = 25f / width;
					
					height = (int) (height * temp);
					width = (int) (width * temp);
				}
				else
				{
					height = 25;
					width = 25;
				}				
				mapPanel.setConstraintHeight(SizeValue.percent(height * 4));
				mapPanel.setConstraintWidth(SizeValue.percent(width * 4));		
				screen.layoutLayers();
				
				// Map Description
				mapDescription = mappreview.getMapDescription();
				setMapDescriptionAtListBox(mapDescription);
				
				// Spieleranzahl der Karte
				playersCount = mappreview.getNumberofplayers();
				game.currentsMapPlayersCount = playersCount;
				players.setText(String.valueOf(playersCount));
				
				AWTLoader loader = new AWTLoader();
				Texture texture = new Texture2D(loader.load(mapPreview.drawMapPreview(mappreview), true));
				((DesktopAssetManager)Game.AM).clearCache();
				((DesktopAssetManager)Game.AM).addToCache(new TextureKey("MAPPREVIEW" + String.valueOf(mapPreviewCount)), texture);
				NiftyImage mapPreviewImage = nifty.getRenderEngine().createImage("MAPPREVIEW" + String.valueOf(mapPreviewCount), false);
				mapPanel.getRenderer(ImageRenderer.class).setImage(mapPreviewImage);
				
				screen.layoutLayers();
				mapPreviewCount++;
			}
//		} 
//		catch (Exception e){
//			Game.logger.error("SepsisCreateGameScreenController.showMapPreviewInMapPanel: catch: " + e.getMessage());
//		}
		
		screen.resetLayout();		
	}
	
	/**
	 * Diese Methode formatiert die mapDescription für die Map Vorschau<br>
	 * @param mapDescription
	 * @return den formatierten mapDescription String
	 * 
	 * @author Markus Strobel<br>
	 * @since 09.02.2013<br>
	 */
	public void setMapDescriptionAtListBox(String mapDescription)
	{
		mapDescriptionListBox.clear();
		String mapDesc = " ";
		
		if(mapDescription.length() > 150)
		{
			String[] words = mapDescription.split(" ");  

			for(int i = 0; i < words.length; i++)
			{
				if(mapDesc.length() <= 90)
				{
					mapDesc += words[i] + " ";
				}
				else
				{
					mapDesc += words[i];
					mapDescriptionListBox.addItem(mapDesc);
					mapDesc = " ";
				}
			}

			if(!mapDesc.equals(""))
			{
				mapDescriptionListBox.addItem(mapDesc);
			}
		}
		else
		{
			mapDescriptionListBox.addItem(mapDescription);
		}
	}
	
	
	
	/**
	 * Diese Methode wechselt zurück zum gameSetupScreen.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	public void backToGameSetupScreen()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		nifty.gotoScreen("gameSetupScreen");
	}

	/**
	 * Diese Methode erstellt das Spiel auf dem Server und führt zum Bildschirm, bei dem auf Mitspieler gewartet wird.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	public void createGame()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		String mapName = availableMapsList.getSelection();
		String createGameName = gameName.getRealText();
		try
		{
			ComObject_gamelist gameList = (ComObject_gamelist)game.network.establishCommunication(new ComObject_gamelist(Game.clientID, Game.clientKey));

			// GAME LIST ÜBERPRÜFEN
			if(gameList.getStatus().equals(STATUS.OK))
			{
				// Existiert der Spielname schon?
				if(gameList.getGamelist().contains(createGameName))
				{

					showInfoText("game name already exists", new Color(1f,0f,0f,1f));
					
					ComObject_gameinfo gameInfo = (ComObject_gameinfo)game.network.establishCommunication(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
					
					if(gameInfo.getStatus().equals(STATUS.OK))
					{
						nifty.gotoScreen("waitForPlayersScreen");
					}
					
				}
				// Wenn er nicht existiert, dann kann das Spiel einfach erstellt werden.
				else
				{
					// Wenn Spielname und Karte ausgewählt sind.
					if(!mapName.equals("") && !gameName.equals(""))
					{
						try 
						{
							ComObject_createGame createGame = (ComObject_createGame)game.network.establishCommunication(new ComObject_createGame(Game.clientID, Game.clientKey, mapName, createGameName, Game.clientKey));

							if(createGame.getStatus().equals(STATUS.OK))
							{
								// gameID speichern
								Game.gameID = createGame.getGameID();
								
								// Eigenen Spieler hinzufügen
								ComObject_addPlayer addPlayer = (ComObject_addPlayer)game.network.establishCommunication(new ComObject_addPlayer(Game.clientID, Game.clientKey, Game.gameID, Game.playerName));

								if(addPlayer.getStatus().equals(STATUS.OK))
								{
									Game.playerID = addPlayer.getPlayerID();
									game.gameOwner = true;
									nifty.gotoScreen("waitForPlayersScreen");
								}
								else
								{
									Game.logger.error("SepsisCreateGameScreenController.createGame().addPlayer: " + addPlayer.getErrorInfo());
								}
							}
							else
							{
								Game.logger.error("SepsisCreateGameScreenController.createGame().createGame: " + createGame.getErrorInfo());
							}
						} 
						catch (Exception e) 
						{
							Game.logger.error("SepsisCreateGameScreenController.createGame(): catch: " + e.getMessage());
						}
					}
				}
			}
			else
			{
				Game.logger.error("SepsisCreateGameScreenController.createGame().gameList: " + gameList.getErrorInfo());
			}
		} 
		catch (Exception e)
		{
			Game.logger.error("SepsisCreateGameScreenController.createGame(): catch: " + e.getMessage());
		}
	}
	
	public void getWinConditionsPreview(String mapID)
	{
		Network network = new Network();
		
		try 
		{
			ComObject_winconditions winconditions = (ComObject_winconditions)network.establishCommunication(new ComObject_winconditions(Game.clientID, Game.clientKey, mapID));
		
			if(winconditions.getStatus().equals(STATUS.OK))
			{
				
				String endOfLine = System.getProperty("line.separator"); 
				String extinctionText = "";
				String goodPlacementText = "";
				
				// WinConditions Vorschau Extinction
				if(winconditions.getExtinction())
				{
					extinctionText = "Extinction: YES" + endOfLine;
				}
				else
				{
					extinctionText = "Extinction: NO" + endOfLine;
				}
				
				if(winconditions.getGoodplacement())
				{					

					goodPlacementText = "Goodplacement: YES" + endOfLine;
						
				}
				else
				{
					goodPlacementText = "Goodplacement: NO" + endOfLine;						
				}
				
				winConditionsExtinctionText.getRenderer(TextRenderer.class).setText(extinctionText);
				winConditionsExtinctionText.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
				
				winConditionsGoodplacementText.getRenderer(TextRenderer.class).setText(goodPlacementText);
				winConditionsGoodplacementText.getRenderer(TextRenderer.class).setTextHAlign(HorizontalAlign.left);
								
			}
			else
			{
				Game.logger.error("SepsisCreateGameScreenController.getWinConditionsPreview().winconditions: " + winconditions.getErrorInfo());
			}
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisCreateGameScreenController.getWinConditionsPreview(): catch: " + e.getMessage());
		}
		
		
	}
	

	
	

	/**
	 * Diese Methode updated Elemente des createGameScreens.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	@Override
	public void update(float tpf)
	{			
		if(inited)
		{
			selectedIndex = availableMapsList.getSelectedIndex();
			
			if(selectedIndex != oldSelectedIndex)
			{
				oldSelectedIndex = selectedIndex;
				showMapPreviewInMapPanel();
			}
		}
		
		if(activeInfoText)
		{
			infoTextCounter += tpf;
			if(infoTextCounter > 2f)
			{
				activeInfoText = false;
				infoTextCounter = 0f;
				infoText.hide();
			}			
		}
	}

}




