package sepsis.gui;
import java.util.ArrayList;

import sepsis.audio.Audio.SOUND;
import sepsis.game.Game;
import sepsis.game.Server_Thread;
import sepsis.game.TextContainer;
import sepsis.network.ComObject_getClients;
import sepsis.network.ComObject_logon;
import sepsis.network.Network;
import sepsis.network.IComObject.STATUS;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.filters.GammaCorrectionFilter;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Diese Klasse kontrolliert den Startbildschirm.<br>
 * @author Markus Strobel<br>
 * @since 28.11.2012<br>
 * 
 * 01.12.2012 (Markus Strobel) Debugstart hinzugefügt, damit kann sofern der Server läuft(0.7.0, 0.7.2) <br>
 * und die IP in der SimpleInitApp angepasst wurde, das Spiel gestartet werden.<br>
 * Spielfeld, GUI und Netzwerk wurden miteinander soweit wie möglich verknüpft.<br>
 * 
 * 18.12.2012 (Markus Strobel) GUI Symbiose mit Netzwerk/Game hinzugefügt.<br>
 * 
 */
public class SepsisStartScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private Element settingsPopup;
	private Element creditsPopup;
	private Element helpPopup;
	@SuppressWarnings("unused")
	private AppStateManager stateManager;
	private Game game;
	
	private boolean activeSettingsPopup;
	
	private AppSettings settings;
	
	private Element infoText;
	private boolean activeInfoText = false;
	private float infoTextCounter = 0f;
	
	private DropDown<String> resolution;
	private DropDown<String> colorDepth;
	private DropDown<String> antiAliasing;
	private DropDown<String> style;
	private DropDown<String> language;
	
	private CheckBox fullScreenCheckBox;
	private CheckBox vSyncCheckBox;
	private CheckBox saveSettingsCheckBox;
	
	private TextField serverIpTextField;
	private TextField serverPortTextField;
	
	private Slider masterVolumeSliderH;
	private Slider musicVolumeSliderH;
	private Slider soundVolumeSliderH;
	
	private Element masterVolumeText;
	private Element musicVolumeText;
	private Element soundVolumeText;
	
	private Element serverConnectionStatusLabel;
	private Element serverConnectionStatusText;
	private Element serverConnectionTestButton;
	
	private Element gammaText;
	private Element brightnessText;
	private float gamma;
	private float brightness;
	
	private TextContainer textContainer;
	private Element creditsText;
	private Element helpText;
	private Element controlsText;
	
	/**
	 * Der Konstruktor des SepsisStartScreenController
	 * 
	 * @author Markus Strobel
	 * @since 29.11.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 */
	public SepsisStartScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.stateManager = stateManager;
		this.nifty = nifty;		
		this.game = game;
		
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.screen = screen;
		this.settingsPopup = nifty.createPopup("settingsPopup");
		this.creditsPopup = nifty.createPopup("creditsPopup");
		this.helpPopup = nifty.createPopup("helpPopup");
	
		infoText = screen.findElementByName("infoText");
		
		activeSettingsPopup = false;
		
		// Texte für Credits und Hilfe vorbereiten
		textContainer = new TextContainer();
		
		// Texte setzen für Credits und Hilfe
		creditsText = creditsPopup.findElementByName("creditsText");
		creditsText.getRenderer(TextRenderer.class).setText(textContainer.getCredits());
		helpText = helpPopup.findElementByName("storyText");
		helpText.getRenderer(TextRenderer.class).setText(textContainer.getStory());
		controlsText = helpPopup.findElementByName("controlText");
		controlsText.getRenderer(TextRenderer.class).setText(textContainer.getControls());

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
	 * Diese Methode startet den Lokalen Server<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 16.01.2013<br>
	 */
	public void startLocalServer()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		try 
		{
			new Thread(new Server_Thread()).start();
		}	
		catch (Exception e) 
		{
			Game.logger.error("SepsisStartScreenController.startLocalServer(): " + e.getMessage());
		}
	}
	
	
	/**
	 * Diese Methode erhöht den Gamma-Wert um 1.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void incrementGamma()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		if(gamma < 1f)
		gamma += 0.01f;
		
		if(gamma > 1f)
			gamma = 1f;
		
		int integerValue = (int) (gamma);
		int floatingValue = (int) (gamma * 100) % 100;	
		String tempValue = String.valueOf(integerValue) + "." + String.valueOf(floatingValue);
		gamma = Float.parseFloat(tempValue);
	}
	
	/**
	 * Diese Methode verringert den Gamma-Wert um 1.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void decrementGamma()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		if(gamma > 0f)
		gamma -= 0.01f;
		
		if(gamma < 0f)
			gamma = 0f;
		
		int integerValue = (int) (gamma);
		int floatingValue = (int) (gamma * 100) % 100;	
		String tempValue = String.valueOf(integerValue) + "." + String.valueOf(floatingValue);
		gamma = Float.parseFloat(tempValue);		
	}
	
	
	/**
	 * Diese Methode erhöht den Brightness-Wert um 1.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void incrementBrightness()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		if(brightness < 1f)
			brightness += 0.01f;
		
		if(brightness > 1f)
			brightness = 1f;
		
		int integerValue = (int) (brightness);
		int floatingValue = (int) (brightness * 100) % 100;	
		String tempValue = String.valueOf(integerValue) + "." + String.valueOf(floatingValue);
		brightness = Float.parseFloat(tempValue);	
	}
	
	/**
	 * Diese Methode verringert den Brightness-Wert um 1.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void decrementBrightness()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		if(brightness > 0f)
			brightness -= 0.01f;
		
		if(brightness < 0f)
			brightness = 0f;
		
		int integerValue = (int) (brightness);
		int floatingValue = (int) (brightness * 100) % 100;	
		String tempValue = String.valueOf(integerValue) + "." + String.valueOf(floatingValue);
		brightness = Float.parseFloat(tempValue);	
	}
	

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {

	}
	
	/**
	 * Diese Methode öffnet das Hilfe Popup, wo die Story und Steurung angezeigt wird.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void openHelpPopop()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.showPopup(screen, helpPopup.getId(), null);
	}
	
	/**
	 * Diese Methode schließt das Hilfe Popup.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 */
	public void closeHelpPopop()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.closePopup(helpPopup.getId());
	}
	
	/**
	 * Diese Methode pingt den Server und zeigt den Status an.
	 */
	public void serverConnectionTest()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		/*
		 * Network Settings
		 */
		Network.IP = serverIpTextField.getRealText();
		Network.PORT = Integer.parseInt(serverPortTextField.getRealText());
		
		// Server wird gepingt
		boolean status = game.network.ping();
		
		if(status)
		{
			serverConnectionStatusLabel.show();
			serverConnectionStatusText.show();
			serverConnectionStatusText.getRenderer(TextRenderer.class).setColor(new de.lessvoid.nifty.tools.Color(0f, 1f, 0f, 1f));
			serverConnectionStatusText.getRenderer(TextRenderer.class).setText("OK");
		}
		else
		{
			serverConnectionStatusLabel.show();
			serverConnectionStatusText.show();
			serverConnectionStatusText.getRenderer(TextRenderer.class).setColor(new de.lessvoid.nifty.tools.Color(1f, 0f, 0f, 1f));
			serverConnectionStatusText.getRenderer(TextRenderer.class).setText("ERROR");
		}		
	}
	
	/**
	 * Diese Methode startet den SetupScreen um Spieleinstellungen vornehmen zu können. <br>
	 * Dort kann ein Spiel gejoint oder erstellt werden.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 28.11.2012<br>
	 * 
	 * 18.12.2012 (Markus Strobel) Server Logon integriert.<br>
	 */
	public void setupGame()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		int client_ID_counter = 0;
		String clientID = "sepsisID_" + client_ID_counter;

		if(Game.clientKey.equals(""))
		{
			ArrayList<String> clientsList = new ArrayList<String>();
			// CLIENT-Abfrage für Login
			try 
			{
				// CLIENTS abfragen
				ComObject_getClients getClients = (ComObject_getClients) game.network.establishCommunication(new ComObject_getClients(clientID));

				if(getClients.getStatus().equals(STATUS.OK))
				{
					// Client-Liste abfragen
					clientsList = getClients.getClients();

					// FALLS clientID schon vorhanden wird hier eine andere ausgewählt
					while(clientsList.contains(clientID))
					{
						client_ID_counter++;
						clientID = "sepsisID_" + client_ID_counter;
					}

					// clientID speichern
					Game.clientID = clientID;

					// LOGON mit ClientID ausführen
					ComObject_logon logon = (ComObject_logon) game.network.establishCommunication(new ComObject_logon(clientID));

					if(logon.getStatus().equals(STATUS.OK))
					{
						// clientKey speichern
						Game.clientKey = logon.getClientKey();
						nifty.gotoScreen("gameSetupScreen");
					}
					else
					{						
						Game.logger.error("SepsisStartScreenController.setupGame().getClients.logon: " + logon.getErrorInfo());
					}
				}
				else
				{
					showInfoText(getClients.getErrorInfo(), new Color(1f,0f,0f,1f));
					Game.logger.error("SepsisStartScreenController.setupGame().getClients: " + getClients.getErrorInfo());
				}
			} 
			catch (Exception e) 
			{
				showInfoText("Server not found at IP: " + Network.IP + " with PORT: "  + String.valueOf(Network.PORT), new Color(1f,0f,0f,1f));
				Game.logger.error("SepsisStartScreenController.setupGame(): " + e.getMessage());
			}
		}
		else
		{
			nifty.gotoScreen("gameSetupScreen");
		}
	}

	/**
	 * Diese Methode öffnet das Einstellungsfenster.<br>
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void openSettingsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		activeSettingsPopup = true;		
		this.settingsPopup = nifty.createPopup("settingsPopup");
		
		/*
		 * SETTINGS POPUP
		 */
		settings = new AppSettings(true);
		// GRAPHIC SETTINGS		
		// RESOLUTION DROPDOWN
		resolution = settingsPopup.findNiftyControl("resolutionDropDownControl", DropDown.class);
		resolution.addItem("1024x768");
		resolution.addItem("1152x864");
		resolution.addItem("1280x720");
		resolution.addItem("1280x1024");
		resolution.addItem("1600x1200");
		resolution.addItem("1680x1050");
		resolution.addItem("1920x1080");
		resolution.setWidth(SizeValue.percent(80));
		resolution.setHeight(SizeValue.percent(70));
		
		// COLORDEPTH DROPDOWN
		colorDepth = settingsPopup.findNiftyControl("colorDepthDropDownControl", DropDown.class);
		colorDepth.addItem("16");
		colorDepth.addItem("24");
		colorDepth.setWidth(SizeValue.percent(80));
		colorDepth.setHeight(SizeValue.percent(70));
		
		// ANTIALIASING DROPDOWN
		antiAliasing = settingsPopup.findNiftyControl("antiAliasingDropDownControl", DropDown.class);
		antiAliasing.addItem("0");
		antiAliasing.addItem("2");
		antiAliasing.addItem("4");
		antiAliasing.addItem("6");
		antiAliasing.addItem("8");
		antiAliasing.addItem("16");
		antiAliasing.setWidth(SizeValue.percent(80));
		antiAliasing.setHeight(SizeValue.percent(70));

		// BRIGHTNESS
		brightnessText = settingsPopup.findElementByName("brightnessText");
		brightness = game.sepsisAppSettings.brightness;
		brightnessText.getRenderer(TextRenderer.class).setText(String.valueOf(brightness));
		
		// GAMMA
		gammaText = settingsPopup.findElementByName("gammaText");
		gamma = game.sepsisAppSettings.gamma;
		gammaText.getRenderer(TextRenderer.class).setText(String.valueOf(gamma));
		
		// FULLSCREEN CHECKBOX
		fullScreenCheckBox = settingsPopup.findNiftyControl("fullScreenCheckBox", CheckBox.class);
		fullScreenCheckBox.setWidth(SizeValue.percent(80));
		fullScreenCheckBox.setHeight(SizeValue.percent(70));
		
		// VSYNC CHECKBOX
		vSyncCheckBox = settingsPopup.findNiftyControl("vSyncCheckBox", CheckBox.class);
		vSyncCheckBox.setWidth(SizeValue.percent(80));
		vSyncCheckBox.setHeight(SizeValue.percent(70));		

		
		// MISC SETTINGS
		// LANGUAGE DROPDOWN
		language = settingsPopup.findNiftyControl("languageDropDownControl", DropDown.class);
		language.addItem("english");
		language.setWidth(SizeValue.percent(80));
		language.setHeight(SizeValue.percent(70));
		
		// STYLE DROPDOWN
		style = settingsPopup.findNiftyControl("styleDropDownControl", DropDown.class);
		style.addItem("sepsis-styles");
		style.setWidth(SizeValue.percent(80));
		style.setHeight(SizeValue.percent(70));
		
		// SAVE SETTINGS CHECKBOX
		saveSettingsCheckBox = settingsPopup.findNiftyControl("saveSettingsCheckBox", CheckBox.class);
		saveSettingsCheckBox.setWidth(SizeValue.percent(15));
		saveSettingsCheckBox.setHeight(SizeValue.percent(70));
		
		// AUDIO SETTINGS
		// MASTER VOLUME TEXT
		masterVolumeText = settingsPopup.findElementByName("masterVolumeText");
		masterVolumeSliderH = settingsPopup.findNiftyControl("masterVolumeSliderH", Slider.class);
		masterVolumeSliderH.setWidth(SizeValue.percent(100));
		masterVolumeSliderH.setHeight(SizeValue.percent(70));
		masterVolumeSliderH.setButtonStepSize(1f);
		masterVolumeSliderH.setMin(0f);
		masterVolumeSliderH.setMax(100f);
		masterVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(masterVolumeSliderH.getValue()).replace(".0",  "") + "%");
		
		// MUSIC VOLUME SLIDER
		musicVolumeText = settingsPopup.findElementByName("musicVolumeText");
		musicVolumeSliderH = settingsPopup.findNiftyControl("musicVolumeSliderH", Slider.class);
		musicVolumeSliderH.setWidth(SizeValue.percent(100));
		musicVolumeSliderH.setHeight(SizeValue.percent(70));
		musicVolumeSliderH.setButtonStepSize(1f);
		musicVolumeSliderH.setMin(0f);
		musicVolumeSliderH.setMax(100f);
		musicVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(musicVolumeSliderH.getValue()).replace(".0",  "") + "%");
		
		// SOUND VOLUME SLIDER
		soundVolumeText = settingsPopup.findElementByName("soundVolumeText");
		soundVolumeSliderH = settingsPopup.findNiftyControl("soundVolumeSliderH", Slider.class);
		soundVolumeSliderH.setWidth(SizeValue.percent(100));
		soundVolumeSliderH.setHeight(SizeValue.percent(70));
		soundVolumeSliderH.setButtonStepSize(1f);
		soundVolumeSliderH.setMin(0f);
		soundVolumeSliderH.setMax(100f);
		soundVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(soundVolumeSliderH.getValue()).replace(".0",  "") + "%");
		
		// NETWORK SETTINGS
		// SERVER IP TEXTFIELD
		serverIpTextField = settingsPopup.findNiftyControl("serverIpTextField", TextField.class);
		serverIpTextField.setText(Network.IP);
		
		// SERVER PORT TEXTFIELD		
		serverPortTextField = settingsPopup.findNiftyControl("serverPortTextField", TextField.class);
		serverPortTextField.setText(String.valueOf(Network.PORT));
		
		// SERVER CONNECTION TEST LABEL(S) AND BUTTON
		serverConnectionStatusLabel = settingsPopup.findElementByName("serverStatusLabel");
		serverConnectionStatusLabel.hide();
		serverConnectionStatusText = settingsPopup.findElementByName("serverStatusText");
		serverConnectionStatusText.hide();
		serverConnectionTestButton = settingsPopup.findElementByName("serverConnectionTestButton");
		serverConnectionTestButton.show();
		
		// Settings Audio Slider Values
		masterVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.sepsisAppSettings.mastervolume) + "%");
		musicVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.sepsisAppSettings.musicvolume) + "%");
		soundVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.sepsisAppSettings.soundvolume) + "%");
		masterVolumeSliderH.setValue(game.sepsisAppSettings.mastervolume);
		musicVolumeSliderH.setValue(game.sepsisAppSettings.musicvolume);
		soundVolumeSliderH.setValue(game.sepsisAppSettings.soundvolume);
		
		// SepsisAppSettings laden
		resolution.selectItem(game.sepsisAppSettings.resolution);
		colorDepth.selectItem(game.sepsisAppSettings.colordepth);
		antiAliasing.selectItem(game.sepsisAppSettings.multisample);
		language.selectItem(game.sepsisAppSettings.language);
		style.selectItem(game.sepsisAppSettings.style);
		serverIpTextField.setText(game.sepsisAppSettings.ip);
		serverPortTextField.setText(game.sepsisAppSettings.port);
		fullScreenCheckBox.setChecked(game.sepsisAppSettings.fullscreen);
		vSyncCheckBox.setChecked(game.sepsisAppSettings.vsync);
		language.selectItem(game.sepsisAppSettings.language);
		saveSettingsCheckBox.setChecked(game.sepsisAppSettings.savesettings);
		Network.IP = game.sepsisAppSettings.ip;
		Network.PORT = Integer.parseInt(game.sepsisAppSettings.port);
		
		nifty.showPopup(screen, settingsPopup.getId(), null);
	}
	
	/**
	 * Diese Methode schließt das Einstellungsfenster.<br>
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void closeSettingsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		activeSettingsPopup = false;
		nifty.closePopup(settingsPopup.getId());
	}
	
	/**
	 * Diese Methode speichert die Einstellungen aus dem SettingsPopup<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.12.2012<br>
	 */
	public void saveSettings()
	{
		/*
		 * Graphic Settings
		 */
		// RESOLUTION
		int resolutionIndex = resolution.getSelectedIndex();
		switch(resolutionIndex)
		{
		case 0:
			settings.setResolution(1024, 768);
			game.sepsisAppSettings.resolution = "1024x768";
			break;
		case 1:
			settings.setResolution(1152, 864);
			game.sepsisAppSettings.resolution = "1152x864";
			break;
		case 2:
			settings.setResolution(1280, 720);
			game.sepsisAppSettings.resolution = "1280x720";
			break;
		case 3:
			settings.setResolution(1280, 1024);
			game.sepsisAppSettings.resolution = "1280x1024";
			break;
		case 4:
			settings.setResolution(1600, 1200);
			game.sepsisAppSettings.resolution = "1600x1200";
			break;
		case 5:
			settings.setResolution(1680, 1050);
			game.sepsisAppSettings.resolution = "1680x1050";
			break;		
		case 6:
			settings.setResolution(1920, 1080);
			game.sepsisAppSettings.resolution = "1920x1080";
			break;	
		}		
		
		// COLORDEPTH
		int colorDepthIndex = colorDepth.getSelectedIndex();
		switch(colorDepthIndex)
		{
		case 0:
			settings.setDepthBits(16);
			game.sepsisAppSettings.colordepth = "16";
			break;
		case 1:
			settings.setDepthBits(24);
			game.sepsisAppSettings.colordepth = "24";
			break;
		}
		
		// ANTIALIASING
		int antiAliasingIndex = antiAliasing.getSelectedIndex();
		switch(antiAliasingIndex)
		{
		case 0:
			settings.setSamples(0);
			game.sepsisAppSettings.multisample = "0";
			break;
		case 1:
			settings.setSamples(2);
			game.sepsisAppSettings.multisample = "2";
			break;
		case 2:
			settings.setSamples(4);
			game.sepsisAppSettings.multisample = "4";
			break;
		case 3:
			settings.setSamples(6);
			game.sepsisAppSettings.multisample = "6";
			break;
		case 4:
			settings.setSamples(8);
			game.sepsisAppSettings.multisample = "8";
			break;
		case 5:
			settings.setSamples(16);
			game.sepsisAppSettings.multisample = "16";
			break;			
		}
		
		// FULLSCREEN		
		if(fullScreenCheckBox.isChecked())
		{
			game.sepsisAppSettings.fullscreen = true;
			settings.setFullscreen(true);
		}
		else
		{
			game.sepsisAppSettings.fullscreen = false;
			settings.setFullscreen(false);
		}
		
		//VSYNC
		if(vSyncCheckBox.isChecked())
		{
			game.sepsisAppSettings.vsync = true;
			settings.setVSync(true);
		}
		else
		{
			game.sepsisAppSettings.vsync = false;
			settings.setVSync(false);
		}
		
		// GAMMA
		GammaCorrectionFilter gammaCorrection = new GammaCorrectionFilter();
		gammaCorrection.setGamma(gamma);
		game.sepsisAppSettings.gamma = gamma;
		
		
		// BRIGHTNESS		
		game.sepsisAppSettings.brightness = brightness;
		
		/*
		 * Misc Settings
		 */
		// LANGUAGE
		int languageIndex = language.getSelectedIndex();
		switch(languageIndex)
		{
		default:
			game.sepsisAppSettings.language = "english";
			break;
		case 1:
			game.sepsisAppSettings.language = "german";
			break;		
		}
		
		
		// STYLES
		int styleIndex = style.getSelectedIndex();
		switch(styleIndex)
		{
		case 0:
			game.sepsisAppSettings.style = "sepsis";
			break;
		case 1:
			game.sepsisAppSettings.style = "sepsis2";
			break;		
		}
		
		
		// SAVE SETTINGS TO FILE
		if(saveSettingsCheckBox.isChecked())
		{
			game.sepsisAppSettings.savesettings = true;
		}
		else
		{
			game.sepsisAppSettings.savesettings = false;
		}
		
		/*
		 * Audio Settings
		 */		
		game.audio.setVolumeMusic((int)((masterVolumeSliderH.getValue() / 100f) * musicVolumeSliderH.getValue()));
		game.sepsisAppSettings.musicvolume = musicVolumeSliderH.getValue();
		game.audio.setVolumeSound((int)((masterVolumeSliderH.getValue() / 100f) * soundVolumeSliderH.getValue()));
		game.sepsisAppSettings.soundvolume = soundVolumeSliderH.getValue();
		game.masterSoundVolume = masterVolumeSliderH.getValue();
		game.sepsisAppSettings.mastervolume = masterVolumeSliderH.getValue();
		
		/*
		 * Network Settings
		 */
		Network.IP = serverIpTextField.getRealText();
		game.sepsisAppSettings.ip = serverIpTextField.getRealText();
		Network.PORT = Integer.parseInt(serverPortTextField.getRealText());
		game.sepsisAppSettings.port = serverPortTextField.getRealText();
		
		// Save Settings to File
		if(game.sepsisAppSettings.savesettings)
		{
			game.sepsisAppSettings.saveSepsisAppSettings();
		}

		// RESTART GUI
		game.setSettings(settings);
		game.restart();
//		resolution.setFocus();
		nifty.resolutionChanged();	
		
		closeSettingsPopup();
	}
		
	/**
	 * Diese Methode öffnet das Credits-Fenster.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 28.11.2012<br>
	 */
	public void openCreditsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.showPopup(screen, creditsPopup.getId(), null);
	}
	
	/**
	 * Mit dieser Methode wird das Credits-Fenster geschlossen.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 28.11.2012<br>
	 */
	public void closeCreditsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.closePopup(creditsPopup.getId());
	}
	
	/**
	 * Diese Methode beendet das Spiel.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 28.11.2012<br>
	 */
	public void onExitButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		game.stop();
	}
	
	/** 
	 * Diese Methode wird beim drücken des Debug Start Buttons ausgeführt und startet das Spiel in Verbindung mit dem Netzwerk.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.12.2012<br>
	 */
	public void debugStart()
	{		
		game.START_DEBUG_GAME();
	}
	
	/**
	 * Diese Methode updated Elemente des createGameScreens.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 07.12.2012<br>
	 */
	@Override
	public void update(float tpf)
	{
		if(activeSettingsPopup)
		{
			// Settings Audio Slider Values
			masterVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(masterVolumeSliderH.getValue()).replace(".0",  "") + "%");
			musicVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(musicVolumeSliderH.getValue()).replace(".0",  "") + "%");
			soundVolumeText.getRenderer(TextRenderer.class).setText(String.valueOf(soundVolumeSliderH.getValue()).replace(".0",  "") + "%");
			
			// Brightness & Gamma Values
			brightnessText.getRenderer(TextRenderer.class).setText(String.valueOf(brightness));
			gammaText.getRenderer(TextRenderer.class).setText(String.valueOf(gamma));			
		}
		
		if(activeInfoText)
		{
			infoTextCounter += tpf;
			if(infoTextCounter > 4f)
			{
				activeInfoText = false;
				infoTextCounter = 0f;
				infoText.hide();
			}			
		}
	}
	
}
