package sepsis.gui;

import sepsis.audio.Audio.SOUND;
import sepsis.game.Game;
import sepsis.network.ComObject_addPlayer;
import sepsis.network.ComObject_gamelist;
import sepsis.network.ComObject_getData;
import sepsis.network.ComObject_globalchat;
import sepsis.network.IComObject.STATUS;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


/**
 * Diese Klasse kontrolliert den Setupbildschirm.<br>
 * @author Markus Strobel<br>
 * @since 28.11.2012<br>
 * 
 * 12.12.2012 (Markus Strobel) GameSetup-Bildschirm weiterentwickelt.<br>
 * 
 * 18.12.2012 (Markus Strobel) GUI Symbiose mit Netzwerk/Game hinzugef�gt.<br>
 * 
 */
public class SepsisSetupScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	@SuppressWarnings("unused")
	private Screen screen;
	@SuppressWarnings("unused")
	private AppStateManager stateManager;
	private Game game;
	
	private String selectedGameID = "";
	
	// Games List
	private ListBox<String> availableGamesListBox;
	
	// Map Preview
	private ListBox<String> mapPreviewListBox;
	private Element mapPanel;
	private NiftyImage mapPreviewImage;
	
	// Chat Window
	private ListBox<String> chatReceiveListBox;
	private TextField chatSendTextField;
	private Button sendButton;
	private DropDown<String> receiverDropDown;
	
	// Player Settings
	private TextField playerNameTextField;
	private ImageSelect playerImageSelect;
	private DropDown<String> playerColorSelected;	
	private String playerName;
	
	private Element backButton;

	private NiftyImage playerLogo1;
	private NiftyImage playerLogo2;
	private NiftyImage playerLogo3;
	private NiftyImage playerLogo4;
	private NiftyImage playerLogo5;
	
	private float updateTimer = 0f;
	private float gameListRefreshTimer = 0f;
	
	/**
	 * Der Konstruktor des SepsisSetupScreenController
	 * 
	 * @author Markus Strobel
	 * @since 29.11.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager f�r eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz f�r die GUI
	 */
	public SepsisSetupScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.stateManager = stateManager;
		this.nifty = nifty;		
		this.game = game;
		this.playerName = "newPlayer";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.screen = screen;
		
		// AVAILABLE GAMES LIST
		availableGamesListBox = screen.findNiftyControl("availableGamesListBox", ListBox.class);

		
		backButton = screen.findElementByName("backButton");
		backButton.setFocus();
		
		// MAPS
		mapPreviewImage = nifty.getRenderEngine().createImage("images/sepsis_mini_logo.jpeg", false);
		
		// MAP PREVIEW
		mapPreviewListBox = screen.findNiftyControl("mapListBox", ListBox.class);
		mapPreviewListBox.addItem("no preview available");
		
		mapPanel = screen.findElementByName("mapPanel");
		mapPanel.getRenderer(ImageRenderer.class).setImage(mapPreviewImage);

		// CHAT CONTROL		
		chatSendTextField = screen.findNiftyControl("chatSendTextField", TextField.class);	
	
		chatReceiveListBox = screen.findNiftyControl("chatReceiveListBox", ListBox.class);
		chatReceiveListBox.disable();
		chatReceiveListBox.setFocusable(false);
	
		sendButton = screen.findNiftyControl("sendButton", Button.class);
		sendButton.setFocusable(false);
		receiverDropDown = screen.findNiftyControl("receiverDropDown", DropDown.class);
		receiverDropDown.addItem("Server");
		
		// PLAYER SETTINGS
		playerNameTextField = screen.findNiftyControl("playerNameTextField", TextField.class);
		playerNameTextField.setText(playerName);
		// Namen auf L�nge 18 begrenzt
		playerNameTextField.setMaxLength(18);
		
		// PLAYER IMAGES
		playerLogo1 = nifty.getRenderEngine().createImage("images/Adenin.png", false);
		playerLogo2 = nifty.getRenderEngine().createImage("images/Cytosin.png", false);
		playerLogo3 = nifty.getRenderEngine().createImage("images/Guanin.png", false);
		playerLogo4 = nifty.getRenderEngine().createImage("images/Thymin.png", false);
		playerLogo5 = nifty.getRenderEngine().createImage("images/Uracil.png", false);
		
		playerImageSelect = screen.findNiftyControl("playerImageSelected", ImageSelect.class);
		playerImageSelect.addImage(playerLogo1);
		playerImageSelect.addImage(playerLogo2);
		playerImageSelect.addImage(playerLogo3);
		playerImageSelect.addImage(playerLogo4);
		playerImageSelect.addImage(playerLogo5);
	
		playerColorSelected = screen.findNiftyControl("playerColorDropDown", DropDown.class);
		
		playerColorSelected.addItem("Blue");
		playerColorSelected.addItem("Red");
		playerColorSelected.addItem("Green");
	
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen()
	{
		refreshAvailableGames();
	}
	
	/**
	 * Diese Methode fragt die verf�gbaren Spiele des Servers ab.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	public void refreshAvailableGames()
	{
		if(!Game.clientID.equals("") && !Game.clientKey.equals("") && (Game.clientID != null) && (Game.clientKey != null))
		{
			try 
			{
				ComObject_gamelist gameList = (ComObject_gamelist)game.network.establishCommunication(new ComObject_gamelist(Game.clientID, Game.clientKey));

				if(gameList.getStatus().equals(STATUS.OK))
				{

					if(gameList.getGamelist().size() > 0)
					{ 
						// Spiele die auf dem Server angezeigt werden, der availableGamesListBox hinzuf�gen
						for(int i = 0; i < gameList.getGamelist().size(); i++)
						{
							if(!availableGamesListBox.getItems().contains(gameList.getGamelist().get(i)))
							{
								availableGamesListBox.addItem(gameList.getGamelist().get(i));
							}					
						}
					}		
					
					if(availableGamesListBox.getItems().size() > 0)
					{
						// Spiele die auf dem Server nicht mehr vorhanden sind, aber noch in der availableGamesListBox vorhanden sind, entfernen
						for(int i = 0; i < availableGamesListBox.getItems().size(); i++)
						{
							if(!gameList.getGamelist().contains(availableGamesListBox.getItems().get(i)))
							{
								availableGamesListBox.removeItem(availableGamesListBox.getItems().get(i));
							}					
						}
					}
					
				}
				else
				{
					Game.logger.error("SepsisSetupScreenController.refreshAvailableGames: gameList.Status: " + gameList.getErrorInfo());
				}			
			}
			catch (Exception e)
			{
				Game.logger.error("SepsisSetupScreenController.refreshAvailableGames: catch: " + e.getMessage());
			}
		}
	}

	/**
	 * Diese Methode f�hrt zur�ck zum Startbildschirm.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void backToStartScreen()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		
		
		nifty.gotoScreen("startScreen");
	}
	
	/**
	 * Diese Methode f�hrt zur�ck zum Startbildschirm.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void createGame()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		nifty.gotoScreen("createGameScreen");
	}
	
	
	/**
	 * Diese Methode f�hrt weiter zum Spielbildschirm.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void joinGame()
	{		
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		if(availableGamesListBox.getSelection().size() > 0 && availableGamesListBox.getSelection().size() < 2)
		{
			selectedGameID = availableGamesListBox.getSelection().get(0);

			try
			{
				ComObject_addPlayer addPlayer = (ComObject_addPlayer)game.network.establishCommunication(new ComObject_addPlayer(Game.clientID, Game.clientKey, selectedGameID, Game.playerName));

				if(addPlayer.getStatus().equals(STATUS.OK))
				{					
					Game.gameID = selectedGameID;
					// playerID speichern
					Game.playerID = addPlayer.getPlayerID();
					nifty.getScreen("waitForPlayersScreen").findElementByName("kickButton").hide();
					nifty.getScreen("waitForPlayersScreen").findElementByName("startButton").hide();
					game.gameOwner = false;
					nifty.gotoScreen("waitForPlayersScreen");
				}
				else
				{
					Game.logger.error("SepsisSetupScreenController.joinGame().addPlayer: " + addPlayer.getErrorInfo());
					
					chatReceiveListBox.addItem("name already in use");					
					chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
					chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);					
					chatReceiveListBox.refresh();
				}
			}
			catch (Exception e)
			{
				Game.logger.error("SepsisSetupScreenController.joinGame(): catch: " + e.getMessage());
			}
		}
	}	
	
	
	/**
	 * Diese Methode gibt zur�ck, ob das ChatFenster zum Nachrichten senden, den Focus hat.<br>
	 * 
	 * @return Gibt true zur�ck, wenn ja, andernfalls false.<br>
	 * 
	 * @autor Markus Strobel<br>
	 * @since 12.12.2012<br>
	 */
	public boolean chatSendTextFieldHasFocus()
	{
		return chatSendTextField.hasFocus();
	}

	/**
	 * Diese Methode erm�glicht es eine Chat Nachricht dem Chatfenster hinzuzuf�gen.<br>
	 * 
	 * @param message
	 */
	public void addChatMessage(String message)
	{
		chatReceiveListBox.clear();
		
		// Hinzuf�gen der Chat
		chatReceiveListBox.addItem(message);
		
		// Unterstes Element ausw�hlen
		chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
		chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);
		
		chatReceiveListBox.refresh();
	}
	
	/**
	 * Diese Methode sendet die ChatNachricht in die Chatbox und �bers Netzwerk.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 12.12.2012<br>
	 * 
	 * 20.12.2012 (Markus Strobel) Netzwerk-Kommunikation hinzugef�gt.<br>
	 * 
	 * Quelle: timeStamp basiert auf folgender Quelle: http://www.java-forum.org/java-basics-anfaenger-themen/50032-uhrzeit-auslesen.html<br>
	 */
	public void sendChatMessage()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		String chatMessage = chatSendTextField.getRealText();
		
		if(!chatMessage.isEmpty())
		{

			try 
			{
				ComObject_globalchat globalchat = (ComObject_globalchat)game.network.establishCommunication(new ComObject_globalchat(Game.clientID, Game.clientKey, chatMessage));

				if(globalchat.getStatus().equals(STATUS.OK))
				{

					chatSendTextField.setText("");		
					chatReceiveListBox.refresh();

					getChatMessages();
				}
				else
				{
					Game.logger.error("SepsisSetupScreenController.sendChatMessage.ComObject_globalchat: " + globalchat.getErrorInfo());
				}
			}
			catch (Exception e) 
			{
				Game.logger.error("SepsisSetupScreenController.sendChatMessage: catch: " + e.getMessage());
			}
		}
		
	}
	
	/**
	 * Diese Methode fragt die Chatnachrichten vom Server ab und �bermittelt sie in die chatReceiveListBox des waitForPlayersScreen<br>
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
								// Empf�nger == mein Name oder == null, dann kann ich die Nachricht bekommen
								if(getData.getPlayeridreciever().get(i).equals(Game.playerID) || getData.getPlayeridreciever().get(i).equals(""))
								{						
									// GameID == mein Spiel oder == null, dann kann ich die Nachricht bekommen
									if(getData.getGameID().get(i).equals(Game.gameID) || getData.getGameID().get(i).equals(""))
									{
										String finalMessage = "";
										String timeStamp = "";
//										String SystemInfo = getData.getDatatype().get(i).toString();
										String senderName = getData.getSenderID().get(i);
										String messageContent = getData.getMessages().get(i);
										messageContent = messageContent.replace("global chatmessage", "");
										
										if(getData.getSenderID().get(i).equals(Game.clientID))
										{
											senderName = "ME";
										}

										if(getData.getPlayeridreciever().get(i).equals("") && getData.getGameID().get(i).equals(""))
										{
											finalMessage = timeStamp + senderName + ": " + messageContent;
										}

										// Hinzuf�gen der Chat
										chatReceiveListBox.addItem(finalMessage);
										
										// Unterstes Element ausw�hlen
										chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
										chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);
										
										chatReceiveListBox.refresh();
									}
								}
							}	
						}
					}
				}
				else
				{
					Game.logger.error("SepsisSetupScreenController.sendChatMessage.ComObject_getData: " + getData.getErrorInfo());
				}
			}
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisSetupScreenController.getChatMessages: catch: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Diese Methode updated Inhalte des GameSetupScreens<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	@Override
	public void update(float tpf)
	{
		playerName = playerNameTextField.getRealText();
		Game.playerName = playerName;
		
		updateTimer += tpf;
		if(updateTimer > 1f)
		{
			updateTimer = 0;
			getChatMessages();

		}
		
		gameListRefreshTimer += tpf;		
		if(gameListRefreshTimer > 3f)
		{
			refreshAvailableGames();
			gameListRefreshTimer = 0;
		}
		
	}
	
	
	
	

	
	
}
