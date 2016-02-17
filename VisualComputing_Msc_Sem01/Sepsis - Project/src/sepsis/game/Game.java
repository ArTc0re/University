package sepsis.game;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import jme3test.input.combomoves.ComboMove;
import jme3test.input.combomoves.ComboMoveExecution;
import org.apache.log4j.Logger;
import sepsis.audio.Audio;
import sepsis.audio.Audio.SOUND;
import sepsis.config.GameConfig;
import sepsis.config.MapConfig;
import sepsis.config.MeepleConfig;
import sepsis.config.MeepleSetup;
import sepsis.game.Minimap.MINIMAPMODE;
import sepsis.game.Unit.ORDERS;
import sepsis.game.Unit.TYPE;
import sepsis.gui.SepsisAppSettingsContainer;
import sepsis.gui.SepsisCreateGameScreenController;
import sepsis.gui.SepsisGameScreenController;
import sepsis.gui.SepsisIntroScreenController;
import sepsis.gui.SepsisSetupScreenController;
import sepsis.gui.SepsisStartScreenController;
import sepsis.gui.SepsisStatisticsScreenController;
import sepsis.gui.SepsisWaitForPlayersScreenController;
import sepsis.network.ComObject_attack;
import sepsis.network.ComObject_chat;
import sepsis.network.ComObject_endTurn;
import sepsis.network.ComObject_gameinfo;
import sepsis.network.ComObject_getData;
import sepsis.network.ComObject_move;
import sepsis.network.ComObject_surrender;
import sepsis.network.ComObject_trade;
import sepsis.network.ComObject_tradeReply;
import sepsis.network.ComObject_tradestations;
import sepsis.network.ComObject_unitinfo;
import sepsis.network.ComObject_winconditions;
import sepsis.network.IComObject;
import sepsis.network.Network;
import sepsis.network.IComObject.RESPONSE;
import sepsis.network.IComObject.STATUS;
import sepsis.network.NetworkThread;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.shadow.PssmShadowRenderer;
import de.lessvoid.nifty.Nifty;


/**
 * Die Game-Klasse stellt den Schnittpunkt vieler weiterer Klassen des Programms dar und verbindet diese
 * zum fertigen Spiel. Hier werden beim Spielstart Daten geladen und während des Spiels die Spiellogik behandelt. <br><br>
 * Programmcode basiert auf folgenden Quellen: <br>
 * http://www.torsten-horn.de/techdocs/java-log4j.htm#Simple-Logging <br>
 * http://code.google.com/p/jmonkeyengine/source/browse/trunk/engine/src/test/jme3test/input/combomoves/TestComboMoves.java<br>
 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:particle_emitters<br>
 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:bloom_and_glow<br>
 * http://code.google.com/p/jmonkeyengine/source/browse/trunk/engine/src/test/jme3test/post/TestCartoonEdge.java<br>
 * 
 * 16.11.12 (Peter Dörr): Überarbeitung der Dokumentation.<br>
 * 
 * 16.11.12 (Peter Dörr): Log4j-Logger erstellt und in verschiedene Methoden anderer Klassen eingebaut.<br>
 * 
 * 17.11.12 (Markus Strobel): InitConfigParser erstellt und hinzugefügt.<br>
 * 
 * 18.11.12 (Peter Dörr): getSelectedPoint erstellt.()<br>
 * 
 * 20.11.12 (Markus Strobel): initKeys(), ActionListener(), AnalogListener() hinzugefügt.<br>
 * 
 * 20.11.12 (Markus Strobel): selectedUnit, currentTurn, currentState, comboKey hinzugefügt.<br>
 * 
 * 21.11.12 (Peter Dörr): MotionPath hinzugefügt.<br>
 * 
 * 21.11.12 (Peter Dörr): changeHexagonBorderColor und changeHexagonBaseColor hinzugefügt.<br>
 * 
 * 28.11.12 (Markus Strobel): added selectedFieldPoint und fieldSelected<br>
 * 
 * 29.11.12 (Markus Strobel): Bug bei moveUnitAlongMotionPath behoben, welche unter Umständen eine NullPointerException verursachen konnte.<br>
 * 
 * 29.11.12 (Markus Strobel): Wegpunkt und Kamera-Steuerung hinzugefügt bzw zugeschnitten. <br>
 * 
 * 29.11.12 (Peter Dörr): Blut-Animation hinzugefügt.<br>
 * 
 * 01.12.12 (Markus Strobel): UnitList-Erstellung im initConfigParser angepasst an die neue Methode der MeepleSetup.initUnits
 * 
 * 01.12.12 (Markus Strobel): added Variables: SepsisStartScreenController, SepsisGameScreenController, SepsisSetupScreenController, nifty, network
 * 								gameStarted, clientID, clientKey, gameID, playerID, player2ID, currentMap, pathFinding.<br>
 * 
 * 
 * 
 * 03.12.12 (Peter Dörr): Veränderungen an den Hexfeld-Farben.<br>
 * 
 * 05.12.12 (Peter Dörr): Verschiedene Methoden zum Einfärben der Hexfelder implementiert.<br>
 * 
 * 05.12.12 (Peter Dörr): Teile der Action- und Analog-Listener in eigene Methoden ausgelagert.<br>
 *
 * ToDo: mehr multithreading!
 * 
 * @author Peter Dörr, Markus Strobel
 * @since 16.11.12<br>
 */
@SuppressWarnings("deprecation")
public class Game extends SimpleApplication{

	/**
	 * Stellt den Log4j-Logger des Programms dar.
	 */
	public static Logger logger = Logger.getRootLogger();
	/**
	 * AssetManager des Programms, der von anderen Klassen mitverwendet werden kann.
	 */
	public static AssetManager AM;	
	/**
	 * Die Karte.
	 */
	public static PlayingField playingField;
	/**
	 * Gibt an, ob der Spieler oder der Gegner am Zug ist.<br>
	 * 
	 * Derzeit gilt owner = 0 ist PLAYER und owner = 1 ist ENEMY, wird noch variabel angepasst.<br>
	 */
	public enum TURN {PLAYER, ENEMY};
	/**
	 * Gibt an, in welchem Zustand sich das Spiel gerade befindet.
	 */
	public enum STATES {READY, MOVING, FIGHTING, TRADE_ATTEMPT_TO_TRADE, TRADE_WAIT_FOR_RESPONSE, TRADE_GOT_RESPONSE, TRADE_ENEMY_WANTS_TRADE, ENEMY_TURN};
	/**
	 * Die möglichen Aktionen die im Interaktions-Menu gewählt werden können.
	 */
	public enum INTERACTIONS {TRADE, FIGHT, FOLLOW, INTERCEPT, ABORT};	
	/**
	 * Mögliche Antworten auf eine Handelsanfrage.
	 */
	public enum TRADE_REPLY {ACCEPT, REJECT, DND};
	/**
	 * Gibt an, ob der Fog of War aus (OFF), statisch (STATIC) oder dynamisch (DYNAMIC) sein soll.
	 */
	public enum FOG {OFF, STATIC, DYNAMIC};
	/**
	 * Das Audio-Objekt zum Abspielen von Sounds und Musik.
	 */
	public Audio audio;
	/**
	 * Speichert eine Referenz auf die aktuell ausgewählte Einheit
	 */
	public Unit selectedUnit;
	/**
	 * Speichert welcher Spieler an der Reihe.
	 */
	public TURN currentTurn;
	/**
	 * Speichert den aktuellen Zustand/Status des Spiels.
	 */
	public STATES currentState;
	/**
	 * MotionPath-Objekt zur Visualisierung von Einheitenbewegungen.
	 */
	private MotionPath motionPath;	
	/** 
	 * Diese Menge wird für die Wegpunkte gebraucht
	 */
	private HashSet<String> pressedKeys = new HashSet<String>();
	/**
	 * Der ComboMove für das Wegpunkt setzen
	 */
	private ComboMove wayPointing;
	/**
	 * Der ComboMove wayPointing wird hierdurch ausgeführt.
	 */
	private ComboMoveExecution wayPointingExec;
	/**
	 * Der ComboMove um den Wegpunkt auszuführen
	 */
	private ComboMove wayPointingLock;
	/**
	 * Der ComboMove wayPointingLock wird hierdurch ausgeführt.
	 */
	private ComboMoveExecution wayPointingLockExec;
	/**
	 * Wird für die ComboMoves benötigt
	 */
	private int comboTime = 0;	
	/**
	 * Speichert das derzeitig ausgewählte Hex-Feld
	 */
	public Point selectedFieldPoint;	
	/**
	 * Speichert die aktuellen Wegpunkte, welche der Einheit hinzugefügt wird.
	 */
	private ArrayList<Point> WayPointList = new ArrayList<Point>();	
	/**
	 * Wenn dieser Wert auf true ist, dann ist bereits eine Einheit Aktiv und es kann keine Weitere ausgewählt oder bewegt werden, außer die selected Unit
	 */
	private boolean activatedUnit = false;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den StartScreen
	 */
	private SepsisStartScreenController startScreen;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den SetupScreen
	 */
	public SepsisSetupScreenController setupScreen;
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den GameScreen
	 */
	public SepsisGameScreenController gameScreen;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den createGameScreen
	 */
	private SepsisCreateGameScreenController createGameScreen;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den waitForPlayersScreen
	 */
	private SepsisWaitForPlayersScreenController waitForPlayersScreen;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den introScreen
	 */
	private SepsisIntroScreenController introScreen;	
	/**
	 * Diese Variable ist für die GUI notwendig und verwaltet den introScreen
	 */
	public SepsisStatisticsScreenController statisticsScreen;	
	/**
	 *  Die Nifty-Instanz
	 */
	private Nifty nifty;
	/**
	 * Ein Netzwerk-Objekt für nicht-Thread Serverkommunikation (veraltet, aus Kompatibilitätsgründen enthalten).
	 */
	public Network network;
	/**
	 * Das Netzwerkobjekt welches für die Netzwerkkommunikation benötigt wird.
	 */
	public NetworkThread networkThread;
	/**
	 * Diese Variable wird benötigt, damit im richtigen Moment die SimpleUpdate und die Spielsteuerung freigegeben wird.
	 */
	public boolean gameStarted = false;
	/**
	 * Die Client-ID
	 */
	public static String clientID = "";
	/** 
	 * Der Client-Key
	 */
	public static String clientKey = "";
	/**
	 * Die Game-ID
	 */
	public static String gameID = "";
	/** 
	 * Die Spieler-ID
	 */
	public static String playerID = "";
	/**
	 * Name des Spielers
	 */
	public static String playerName = "";
	/**
	 * Der Name der Karte
	 */
	public static String mapID = "";
	/**
	 * Die Objekt Instanz für die Wegfindung.
	 */
	public PathFinding pathFinding;
	/**
	 * In dieser Variablen werden die PlayerIDs gespeichert.
	 */
	public static ArrayList<String> playerIDs;
	/**
	 * In dieser Variablen werden die Spielernamen gespeichert.
	 */
	public static ArrayList<String> playersList;	
	/**
	 * Die PlayerID des Spielers der an der Reihe ist.
	 */
	public String activePlayerID;	
	/**
	 * Diese Variable regelt die Steuerung der Animation im Hauptmenu.
	 */
	public boolean handleMainMenuAnimation;	
	/**
	 * Gibt an, ob die Blut-Animation sichtbar ist.
	 */
	public boolean handleBloodAnimation;
	/**
	 * Gibt an, ob die Healthbars sichtbar sind.
	 */
	public boolean handleHealthBars = true;
	/**
	 * Diese Variable bestimmt, ob es sich bei dem Spiel um ein DebugGame handelt.
	 */
	public boolean debugGame;
	/**
	 * Die aktuelle Rundenzahl, diese wird dazu benutzt, um die Variablen zu resetten.
	 */
	public int RoundNumber;
	/**
	 * Timer Variable für die SimpleUpdate
	 */
	private float timer;
	/**
	 * Temporäre X-Koordinate, die beim Aufruf des FightTradeFollowIntercept-Popups benötigt wird.
	 */
	private int tmp_x;
	/**
	 * Temporäre Y-Koordinate, die beim Aufruf des FightTradeFollowIntercept-Popups benötigt wird.
	 */
	private int tmp_y;
	/**
	 * Greift man eine Einheit an, so rückt die aktive Einheit an diese heran und greift dann an.<br>
	 * Um diesen zeitverzögerten Angriff korrekt visualisieren zu können, wird diese Variable benötigt.
	 */
	private STATES actionAfterMovement;
	/**
	 * Kampfanimations-Timer
	 */
	private float fightAnimationTimer = -1.0f;
	/**
	 * Dieser Wert skaliert die Gesamtlautstärke
	 */
	public float masterSoundVolume = 50f;
	/**
	 * Dieser Wert sagt aus, ob man das Spiel erstellt hat oder nicht.
	 * Er wird gesetzt bei createGame -> true bzw joinGame -> false
	 */
	public boolean gameOwner;
	/**
	 * Dieses Objekt ist für das laden und speichern der gameSettings notwendig.
	 */
	public SepsisAppSettingsContainer sepsisAppSettings;
	/**
	 * Objekt zum Speichern von Statistiken.
	 */
	public Statistics statistics = new Statistics();
	/**
	 * Zähler zum Berechnen der gespielten Zeit (für Statistiken).
	 */
	private float playedGameTimeTimer = 0;
	/**
	 * Objekt zur Überprüfung der Siegesbedingungen.
	 */
	public WinConditions winconditions;
	/**
	 * Gibt an, welcher Fog of War Modus momentan eingestellt ist.
	 */
	public FOG visibilityMode = FOG.DYNAMIC;
	/**
	 * Gibt an, ob der Autofokus aktiviert ist oder nicht.
	 */
	public boolean autofocus_mode = false;
	/**
	 * Gibt an, in welche Richtung sich die Kamera bei einem Autofokusevent bewegt.
	 */
	public Vector3f autofocus_movementDirection = new Vector3f(0.0f, 0.0f, 0.0f);
	/**
	 * Speichert die momentane Bewegungsgeschwindigkeit bei einem Autofokusevent.
	 */
	public float autofocus_speedCurrent = 0.0f;
	/**
	 * Speichert die maximale Bewegungsgeschwindigkeit bei einem Autofokusevent.
	 */
	public float autofocus_speedMax = 60.0f;
	/**
	 * Gibt an, wie schnell die Kamera bei einem Autofokusevent beschleunigt.
	 */
	public float autofocus_speedUp = 2.0f;
	/**
	 * Gibt an, wie schnell die Kamera bei einem Autofokusevent abbremst.
	 */
	public float autofocus_speedDown = 1.0f;
	/**
	 * Verhindert, das Einheiten-Models und Bewegungen aktualisiert werden, solange die UnitInfos vom Server geholt werden.
	 */
	public boolean gameUpdateThreadIsActualizingUnitInfos = false;
	/**
	 * UnitID der angreifenden Einheit.
	 */
	public int unitID_Attacker;
	/**
	 * UnitID der verteidigenden Einheit.
	 */
	public int unitID_Defender;
	/**
	 * Speichert die letzte Mausposition bei einer Kamerabewegung per Maus-Drag.
	 */
	public Vector2f mousePositionLast;

	/**
	 * Die notwendige Spieleranzahl der aktuellen Karte
	 */
	public int currentsMapPlayersCount = 0;

	/**
	 * Diese Methode wird bei Prgrammstart aufgerufen und initialisiert Config-Parser, Spielfeld,
	 * Einheiten und GUI, sowie visuelle- und Audio-Effekte. <br><br>
	 * Konfiguriert den Log4J-Logger und stellt ihn damit den anderen Klassen zur Verfügung. <br><br>
	 * Programmcode basiert auf folgenden Quellen: <br>
	 * http://www.torsten-horn.de/techdocs/java-log4j.htm#Simple-Logging <br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:light_and_shadow<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:bloom_and_glow<br>
	 * http://code.google.com/p/jmonkeyengine/source/browse/trunk/engine/src/test/jme3test/post/TestCartoonEdge.java<br><br>
	 * 
	 * 16.11.12 (Peter Dörr): Initialisierung des Terrains eingefügt. <br>
	 * 
	 * 18.11.12 (Peter Dörr): Initialisierung der Hexfelders eingefügt. <br>
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12
	 */

	public void simpleInitApp() {	

		//Logger initialisieren
		logger = Logger.getLogger( Game.class );
		logger.info("Game.simpleInitApp: Initialisierung gestartet.");

		//Kamera konfigurieren
		cam.setLocation(new Vector3f(0.0f, 125.0f, 0.0f));
		cam.lookAt(new Vector3f(0.0f, 0.0f, -10.0f), new Vector3f(0.0f, 1.0f, 0.0f));		
		flyCam.setEnabled(false);
		inputManager.setCursorVisible(true);

		//AssetManager konfigurieren
		this.assetManager.registerLocator("data/", FileLocator.class);
		AM = this.assetManager;

		//Knoten erstellen
		rootNode.attachChild(new Node("Node_Hexagon"));
		((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Base"));
		((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Border"));
		((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Center"));
		rootNode.attachChild(new Node("Node_Unit"));
		((Node)rootNode.getChild("Node_Unit")).attachChild(new Node("Node_Units_Model"));
		((Node)rootNode.getChild("Node_Unit")).attachChild(new Node("Node_Units_Life"));
		rootNode.attachChild(new Node("Node_Terrain"));
		rootNode.attachChild(new Node("Node_BloodAnimation"));
		rootNode.attachChild(new Node("Node_MainMenuAnimation"));
		rootNode.attachChild(new Node("Node_FightAnimation"));


		//Licht initialisieren
		DirectionalLight light = new DirectionalLight();
		light.setColor(ColorRGBA.White);
		light.setDirection(new Vector3f(0.0f, -1.0f, 0.0f).normalizeLocal());
		rootNode.addLight(light);

		//Schatten initialisieren
		PssmShadowRenderer shadowRenderer = new PssmShadowRenderer(assetManager, 512, 3);
		shadowRenderer.setDirection(new Vector3f(-0.25f, -1.0f, -0.25f).normalizeLocal());
		viewPort.addProcessor(shadowRenderer);

		//Postprocessing initialisieren (Blur-Effekt)
		BloomFilter bloomFilter = new BloomFilter();
		bloomFilter.setBloomIntensity(2.5f);
		bloomFilter.setBlurScale(0.5f);
		FilterPostProcessor filterPostProcessor = new FilterPostProcessor(assetManager);
		filterPostProcessor.addFilter(bloomFilter);
		viewPort.addProcessor(filterPostProcessor);

		//Audio initialisieren
		audio = new Audio(audioRenderer);
		audio.playMusic(Audio.MUSIC.MENU_MUSIC, 0);  
		audio.setVolumeMusic((int)((sepsisAppSettings.mastervolume / 100f) * sepsisAppSettings.musicvolume));
		audio.setVolumeSound((int)((sepsisAppSettings.mastervolume / 100f) * sepsisAppSettings.soundvolume));
		masterSoundVolume = sepsisAppSettings.mastervolume;


		//Init Network
		networkThread = new NetworkThread();
		networkThread.setDaemon(true);
		network = new Network();

		flyCam.setDragToRotate(true);

		//Animation initialisieren
		handleMainMenuAnimation = true;
		handleBloodAnimation = false;

		// Spielerliste initialisieren
		playersList = new ArrayList<String>();

		//Spielfeld initialisieren
		playingField = new PlayingField();

		// SelectedFieldPoint initialisieren
		selectedFieldPoint = new Point();

		// Pathfinding Instanz init
		pathFinding = new PathFinding();		

		// init GUI
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		guiViewPort.addProcessor(niftyDisplay);
		//        nifty.setDebugOptionPanelColors(true);

		introScreen = new SepsisIntroScreenController(this, stateManager, nifty);
		startScreen = new SepsisStartScreenController(this, stateManager, nifty);
		setupScreen = new SepsisSetupScreenController(this, stateManager, nifty);
		createGameScreen = new SepsisCreateGameScreenController(this, stateManager, nifty);
		gameScreen = new SepsisGameScreenController(this, stateManager, nifty);
		//        statisticsScreen = new SepsisStatisticsScreenController(this, stateManager, nifty);
		waitForPlayersScreen = new SepsisWaitForPlayersScreenController(this, stateManager, nifty);

		nifty.registerScreenController(introScreen);
		nifty.registerScreenController(startScreen);
		nifty.registerScreenController(setupScreen);
		nifty.registerScreenController(createGameScreen);
		nifty.registerScreenController(waitForPlayersScreen);
		nifty.registerScreenController(gameScreen);
		//        nifty.registerScreenController(statisticsScreen);

		try 
		{
			// Styles laden
			nifty.loadStyleFile("data/gui/styles/" + sepsisAppSettings.style + ".xml");

			// Language (dialog.properties, dialog_de.properties, etc)
			// properties von hier laden mit aufrufen der werte aus der sepsisAppSettings

		} 
		catch (Exception e) 
		{
			logger.error("Game.simpleInitApp.loadStylesAndProperties: catch: " + e.getMessage());
			nifty.loadStyleFile("data/gui/styles/sepsis-styles.xml");
		}

		//        nifty.fromXml("gui/sepsis.xml", "statisticsScreen", introScreen, startScreen, setupScreen, createGameScreen, waitForPlayersScreen, gameScreen, statisticsScreen);
		nifty.fromXml("gui/sepsis.xml", "introScreen");

		// Inits		
		initComboMoves();
		initKeys();

		// Startzustände initialisieren
		currentTurn = TURN.PLAYER;
		currentState = STATES.READY;
		playerID = "";
		activePlayerID = playerID;		
		RoundNumber = 0;
		timer = 0f;
	}



	/**
	 * Die simpleUpdate-Methode behandelt die Hintergrund-Animationen des Spiels, sowie verschiedene andere, zeitabhängige Funktionen.
	 * 
	 * @author Peter Dörr, Markus Strobel
	 * @since 16.11.12
	 * 
	 * 21.11.2012 (Markus Strobel): ComboMoves hinzugefügt.
	 * 
	 * 28.11.2012 (Markus Strobel): Wegpunkt zeichnen von ausgewählter Unit hinzugefügt.
	 */
	public void simpleUpdate(float tpf){		

		//Hauptmenu Hintergrundanimation updaten
		handleMainMenuAnimation(tpf, handleMainMenuAnimation);
		handleBloodAnimation(tpf, handleBloodAnimation);


		// Updated die GUI im Startbildschirm
		if(!gameStarted)
		{
			if(nifty.getCurrentScreen().getScreenId().equals("introScreen"))
			{
				introScreen.update(tpf);
			}        	

			if(nifty.getCurrentScreen().getScreenId().equals("startScreen"))
			{
				startScreen.update(tpf);
			}

			if(nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
			{
				gameScreen.update(tpf);
			}

			if(nifty.getCurrentScreen().getScreenId().equals("createGameScreen"))
			{
				createGameScreen.update(tpf);
			}

			if(nifty.getCurrentScreen().getScreenId().equals("gameSetupScreen"))
			{
				setupScreen.update(tpf);
			}

			if(nifty.getCurrentScreen().getScreenId().equals("waitForPlayersScreen"))
			{
				waitForPlayersScreen.update(tpf);
			}

		}

		// Wird nur ausgeführt, wenn das Spiel gestartet wurde
		if(gameStarted)
		{
			//Server-Antworten behandeln
			IComObject iComObject;
			while((iComObject = networkThread.getNextIComObject()) != null){
				logger.debug("Game.simpleUpdate: Behandle IComObject...");


				//Move
				if(iComObject instanceof ComObject_move){
					ComObject_move move = (ComObject_move)iComObject;

					if(move.getStatus() == STATUS.OK) {

						activatedUnit = true;

						//Statistiken updaten
						statistics.unitMoved(selectedUnit, playerID, move.getPath().size());

						//HexagonBorder-Farbe des Ausgangspunktes der Einheit zurücksetzen
						changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 0);

						//Einheit über Motionpath bewegen
						moveUnitAlongMotionPath(selectedUnit, move.getPath(), 0.5f);
						currentState = STATES.MOVING;

						//Verbleibende Bewegungspunkte entfernen, da seit Serverversion 7.4 nur noch eine Bewegung pro Zug möglich ist
						selectedUnit.movement_curr = 0;

						//Follow-Funktion anderer Einheiten behandeln
						for(int i = 0; i < playingField.units.size(); i++){
							if(playingField.units.get(i).order == ORDERS.FOLLOW && playingField.units.get(i).orderUnitID == selectedUnit.ID){
								for(int j = 0; j < move.getPath().size(); j++){
									playingField.units.get(i).followBuffer.add(move.getPath().get(j));
								}
							}
						}

						//Alle Hexfeder wieder auf Standardfarben setzen
						setHexagonBaseColorAll(2);
						setHexagonBorderColorAll(0);

						//Sound abspielen
						audio.playSound(SOUND.PLAYINGFIELD_UNITMOVING, null);

						logger.info("Game.simpleUpdate: Bewegung erfolgreich.");
					} 
					else 
					{
						activatedUnit = false;
					}

					continue;
				}

				//Attack
				if(iComObject instanceof ComObject_attack){
					ComObject_attack attack = (ComObject_attack)iComObject;

					if(attack.getStatus() == STATUS.OK)
					{	
						//Statistiken updaten
						statistics.unitDamagedAnUnit(selectedUnit, playerID, attack.getDamage());

						//Angriff erfolgreich, verteidigender Einheit Schaden zufügen
						getUnitByUnitID(unitID_Attacker).doneThisRound = true;

						//Angriffsanimation abspielen
						showFightAnimation(getUnitByUnitID(unitID_Attacker).model.getLocalTranslation(), getUnitByUnitID(unitID_Defender).model.getLocalTranslation());

						//Angriffssound ausgeben
						audio.playSound(SOUND.PLAYINGFIELD_UNITATTACKING, null);

						//Überprüfen, ob die verteidigende Einheit tot ist, wenn ja, entferne diese aus dem Spiel
						networkThread.sendIComObject(new ComObject_unitinfo(clientID, clientKey, playerID, gameID, unitID_Defender));

						//Schaden ausgeben
						gameScreen.showEventPanel("Damage dealt: " + attack.getDamage(), 1.0f, 1.0f, 1.0f, 1.0f);

						//Zug beenden
						endTurn();

						logger.info("Game.attack: Angriff erfolgreich. Verursachter Schaden: " + String.valueOf(attack.getDamage()));
					}

					continue;
				}

				//Tradeoffer
				if(iComObject instanceof ComObject_trade){
					ComObject_trade trade = (ComObject_trade)iComObject;

					if(trade.getStatus() == STATUS.OK)
					{
						currentState = STATES.TRADE_WAIT_FOR_RESPONSE;
						gameScreen.showEventPanel("Tradeoffer send!", 1.0f, 1.0f, 1.0f, 1.0f);

						//Wird eine eigene Einheit angehandelt, grundsätzlich versuchen, den Handel anzunehmen
						if(getUnitByUnitID(trade.getTradePartnerUnitID()).ownerID != null){
							if(getUnitByUnitID(trade.getTradePartnerUnitID()).ownerID.equals(playerID)){
								currentState = STATES.READY;
								networkThread.sendIComObject(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.ACCEPTED, ""));

								//Statistiken updaten
								statistics.unitTraded(getUnitByUnitID(trade.getTradeOfferUnitID()), playerID);

								gameScreen.showEventPanel("Trade accepted!", 1.0f, 1.0f, 1.0f, 1.0f);
								endTurn();
							}
						}
					} else {
						currentState = STATES.READY;
						gameScreen.showEventPanel("Trade not possible!", 1.0f, 1.0f, 1.0f, 1.0f);
					}
				}

				//Tradereply
				if(iComObject instanceof ComObject_tradeReply){
					ComObject_tradeReply tradeReply = (ComObject_tradeReply)iComObject;
					if(tradeReply.getStatus() == STATUS.OK){
						refreshUnits();
						currentState = STATES.ENEMY_TURN;
						gameScreen.closeGetTradeOfferPopup();
						gameScreen.showEventPanel("Trade: " + tradeReply.getResponse() , 1.0f, 1.0f, 1.0f, 1.0f);
					} else {
						gameScreen.showEventPanel(60, 10, "Trade not possible! Maybe one of the trading units can't carry any more stuff?", 1.0f, 1.0f, 1.0f, 1.0f);
					}
				}

				// Tradestations
				if(iComObject instanceof ComObject_tradestations){
					ComObject_tradestations tradestations = (ComObject_tradestations)iComObject;

					gameScreen.storeTradeStationInformation(tradestations);					
					continue;
				}

				//EndTurn
				if(iComObject instanceof ComObject_endTurn){
					ComObject_endTurn endTurn = (ComObject_endTurn)iComObject;

					if(endTurn.getStatus() == STATUS.OK){

						//Aktive Einheit zurücksetzen
						selectedUnit.doneThisRound = true;
						selectedUnit = null;
						selectedFieldPoint = null;
						activatedUnit = false;

						//States setzen
						if (endTurn.getActivePlayerID().equals(playerID)){
							currentTurn = TURN.PLAYER;
							currentState = STATES.READY;
						} else {
							currentTurn = TURN.ENEMY;
							currentState = STATES.ENEMY_TURN;
						}

						//Falls eine neue Runde beginnt, alle Einheiten wieder verfügbar machen und Bewegungspunkte zurücksetzen
						if(endTurn.getRoundNo() > RoundNumber){
							for(int i = 0; i < playingField.units.size(); i++)
							{
								playingField.units.get(i).doneThisRound = false;
								playingField.units.get(i).movement_curr = playingField.units.get(i).movement_max;
							}				
							RoundNumber++;
						}

						//Alle Hexfeder wieder auf Standardfarben setzen
						setHexagonBaseColorAll(2);
						setHexagonBorderColorAll(0);

						//Beenden des Zuges erfolgreich
						logger.info("Game.simpleUpdate: Zug erfolgreich behandelt. Nächster Spieler: " + endTurn.getActivePlayerID());
					}

					continue;
				}

				//GameInfo
				if(iComObject instanceof ComObject_gameinfo){
					ComObject_gameinfo gameInfo = (ComObject_gameinfo)iComObject;

					//Gewinnbedingungen abfragen
					if(winconditions.checkForWinningPlayer(gameInfo) != null) {
						if(winconditions.checkForWinningPlayer(gameInfo).equals(Game.playerID)) {
							gameStarted = false;
							gameScreen.openStatisticsScreen(true);
						} else {
							gameStarted = false;
							gameScreen.openStatisticsScreen(false);
						}
					}

					//GUI Spielerliste eintragen
					gameScreen.getConnectedPlayersSended(gameInfo);

					//Handelsinformationen abfragen, wenn ein Angebot vom Spieler abgegeben wurde und auf die Antwort eines anderen Clients gewartet wird
					if(currentState == STATES.TRADE_WAIT_FOR_RESPONSE){
						checkTradeOfferResponse(gameInfo);
					}

					//Handelsinformationen abfragen, wenn ein Angebot von einem anderen Spieler kommt
					if(currentState == STATES.ENEMY_TURN){
						checkTradeRequests(gameInfo);
					}

					//Überprüfen, welcher Spieler am Zug ist 
					if(currentTurn == TURN.ENEMY){
						checkTurn(gameInfo);
					}

					continue;
				}

				//UnitInfo
				if(iComObject instanceof ComObject_unitinfo){
					ComObject_unitinfo unitinfo = (ComObject_unitinfo)iComObject;

					if(unitinfo.getStatus() == STATUS.OK){
						Unit unit = getUnitByUnitID(unitinfo.getUnitID());

						//Möglicherweise existiert die Einheit schon garnicht mehr... super das mit den Freds... i love them
						if(unit == null){
							continue;
						}

						//Einheitenbewegung zwischenspeichern
						unit.lastMovement = unitinfo.getLastMovement();

						if(unit.ownerID.equals(playerID)){
							//Fracht aktualisieren
							unit.cargo_1 = unitinfo.getCargo().get(0);
							unit.cargo_2 = unitinfo.getCargo().get(1);
							unit.cargo_3 = unitinfo.getCargo().get(2);
							unit.cargo_4 = unitinfo.getCargo().get(3);
							unit.cargo_5 = unitinfo.getCargo().get(4);

							//Trefferpunkte und Bewegungsreichweite
							unit.hitpoints_curr = unitinfo.getHitpoints();
							unit.movement_curr = unitinfo.getMovement();
						}

						unit.isDead = unitinfo.getDestroyed();
					}

					continue;
				}

				//Chat
				if(iComObject instanceof ComObject_chat){
					ComObject_chat chat = (ComObject_chat)iComObject;

					gameScreen.sendChatMessageSended(chat);

					continue;
				}

				//GetData
				if(iComObject instanceof ComObject_getData){
					ComObject_getData data = (ComObject_getData)iComObject;

					gameScreen.getChatMessagesSended(data);

					continue;
				}

				//Surrender
				if(iComObject instanceof ComObject_surrender){
					ComObject_surrender surrender = (ComObject_surrender)iComObject;

					gameScreen.surrenderSended(surrender);

					continue;
				}

				//Winconditions
				if(iComObject instanceof ComObject_winconditions){
					ComObject_winconditions winconditions = (ComObject_winconditions)iComObject;

					gameScreen.setWinConditionsSended(winconditions);

					continue;
				}
			}

			// Updated die GUI im Spielbildschirm
			gameScreen.update(tpf);

			//Statistiken updaten
			if(playedGameTimeTimer > 1.0f){
				statistics.addTime(1);
				playedGameTimeTimer -= 1.0f;
			} else {
				playedGameTimeTimer += tpf;
			}

			//Einheiten zu Animationszwecken langsam rotieren lassen
			rotateUnitModels(tpf);
			rotateHealthBars(tpf);

			//Autofokus
			handleAutofocus(tpf);

			// Für das WayPointing, also das setzen von Wegpunkten.
			comboTime += tpf;
			wayPointingExec.updateExpiration(comboTime);
			wayPointingLockExec.updateExpiration(comboTime);			

			if(selectedUnit != null && currentTurn.equals(TURN.PLAYER))
			{
				changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 1);
			}
			if(selectedUnit != null && currentTurn.equals(TURN.ENEMY))
			{
				changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 0);
			}

			//Entfernt die Kampfanimation nach einer bestimmten Zeit
			//Stoppt Animation nach 2.0 Sekunden
			//Entfernt PartikelEmitter nach 3.0 Sekunden
			if(fightAnimationTimer != -1.0f){
				fightAnimationTimer += tpf;

				if(fightAnimationTimer > 2.0f){
					ParticleEmitter particleEmitter = (ParticleEmitter)((Node)rootNode.getChild("Node_FightAnimation")).getChild(0);
					particleEmitter.setParticlesPerSec(0);
					particleEmitter = (ParticleEmitter)((Node)rootNode.getChild("Node_FightAnimation")).getChild(1);
					particleEmitter.setParticlesPerSec(0);
				}

				if(fightAnimationTimer > 3.0f){
					fightAnimationTimer = -1.0f;
					((Node)rootNode.getChild("Node_FightAnimation")).detachAllChildren();
				}
			}


			timer += tpf;
			if(timer > 0.25f * getUnitCount()) {

				//Timer zurücksetzen
				timer = 0f;

				//GameInfo senden
				networkThread.sendIComObject(new ComObject_gameinfo(clientID, clientKey, playerID, gameID));

				//Einheiten aktualisieren
				refreshUnits();

				//Einheiten-StatusBox aktualisieren
				setStatusBoxByUnitInfos();

				//Fog of War aktualisieren
				calculateVisibility(visibilityMode);

				for(int i = 0; i < playingField.units.size(); i++){
					Unit unit = playingField.units.get(i);

					//Überprüfen, ob eine Einheit tot ist, wenn ja, entferne diese aus dem Spiel
					if(unit.isDead){

						//Model aus dem Node_Units_Model-Knoten entfernen
						Node node = (Node)(((Node)rootNode.getChild("Node_Unit")).getChild("Node_Units_Model"));
						node.detachChild(unit.model);

						//Einheit aus dem units-Array entfernen und damit endgültig löschen
						playingField.units.remove(unit);

						//Sterbesound ausgeben
						audio.playSound(SOUND.PLAYINGFIELD_UNITDIEING, null);
					}
				}

				for(int i = 0; i < playingField.units.size(); i++){
					Unit unit = playingField.units.get(i);

					//Healthbar aktualisieren
					if(playingField.visibility[playingField.units.get(i).x][playingField.units.get(i).y]){
						unit.loadHealthbar(unit.hitpoints_curr, unit.hitpoints_max);
					}

					//(Feindliche) Einheiten bewegen
					if(unit.lastMovement != null && !unit.ownerID.equals(playerID)){
						if(unit.lastMovement.get(unit.lastMovement.size() - 1).x != -1 || unit.lastMovement.get(unit.lastMovement.size() - 1).y != -1){
							if(playingField.units.get(i).x != unit.lastMovement.get(unit.lastMovement.size() - 1).x || playingField.units.get(i).y != unit.lastMovement.get(unit.lastMovement.size() - 1).y){
								moveUnitAlongMotionPath(playingField.units.get(i), unit.lastMovement, 0.1f);
								playingField.units.get(i).x = unit.lastMovement.get(unit.lastMovement.size() - 1).x;
								playingField.units.get(i).y = unit.lastMovement.get(unit.lastMovement.size() - 1).y;
							}
						}
					}
				}

				//Unitpositionen setzen
				if(currentState != STATES.MOVING){
					setUnitModelPositions();
				}
			}
		}
	}	



	/**
	 * Wird von den Main-Klasse bei Programmstart vor simpleInitApp aufgerufen, um das JME3-Optionsmenü auszublenden oder anzuzeigen.
	 * Muss auf diese Weise gelöst werden, da man in der Main-Klasse keinen Zugriff auf showSettings erhält.
	 * 
	 * @param show True, wenn das JME3-Optionsmenü bei Programmstart gezeigt werden soll, sonst false.
	 * a
	 * @author Peter Dörr
	 * @since 16.11.12
	 */
	public void showSettings(boolean show){
		this.showSettings = show;
	}



	/**
	 * Veranlasst die Config-Parser dazu, die XML/CSV-Dateien auszulesen. Die erhaltenen Daten werden anschließend in
	 * map und units übertragen. <br><br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012
	 * 
	 * 01.12.2012 (Markus Strobel) UnitList erstellung angepasst an die neue Methode der MeepleSetup.initUnits
	 */
	public void initConfigParser(){

		GameConfig gameConfig = new GameConfig();
		gameConfig.parseFromXML("data/config/POV.map.config.xml");		
		MeepleConfig meepleConfig = new MeepleConfig();
		meepleConfig.parseFromXML("data/config/POV.meeple.config.xml");
		MeepleSetup meepleSetup = new MeepleSetup();
		meepleSetup.parseFromXML("data/config/POV.meeple.setup.xml");
		MapConfig mapConfig = new MapConfig();
		mapConfig.parseFromCSV("data/config/POV.terrain.map.csv", "");

		ArrayList<Unit> unitList = new ArrayList<Unit>();
		ArrayList<UnitType> unitTypesList = meepleConfig.getUnitTypes();
		ArrayList<UnitSetup> unitSetupList = meepleSetup.getUnitSetups();

		unitList = meepleSetup.initUnits(unitTypesList, unitSetupList);

		playingField.map = mapConfig.getMap();
		playingField.width = mapConfig.getWidth();
		playingField.height = mapConfig.getHeight();		
		playingField.units = unitList;
		playingField.visibility = new boolean[playingField.width][playingField.height];
	}	



	/**
	 * Diese Methode initialisiert das Terrain, nachdem alle benötigten Werte gesetzt wurden.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.12.2012<br>
	 */
	public void initTerrain()
	{
		//Terrain erstellen
		((Node)rootNode.getChild("Node_Terrain")).attachChild(playingField.initTerrain());

		//Hexagons erstellen
		initHexagons();

		//Einheiten-Models in Node_Units_Model-Knoten einfügen
		for(int i = 0; i < playingField.units.size(); i++){
			((Node)((Node)rootNode.getChild("Node_Unit")).getChild("Node_Units_Model")).attachChild(playingField.units.get(i).model);
		}

		//Models auf Position bringen
		setUnitModelPositions();

	}



	/**
	 * Erzeugt die Hexfeld-Basen, -Grenzen und -Mittelpunkte auf dem Spielfeld.
	 * 
	 * @author Peter Dörr
	 * @since 22.11.12
	 */
	private void initHexagons(){
		ArrayList<Geometry> geometries;
		geometries = playingField.initHexagonBases();
		for(int i = 0; i < geometries.size(); i++){
			((Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base")).attachChild(geometries.get(i));
		}
		geometries = playingField.initHexagonBorders();
		for(int i = 0; i < geometries.size(); i++){
			((Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Border")).attachChild(geometries.get(i));
		}
		geometries = playingField.initHexagonCenters();
		for(int i = 0; i < geometries.size(); i++){
			((Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Center")).attachChild(geometries.get(i));
		}
	}



	/**
	 * Setzt die Models der Einheiten an die korrekte Position auf dem Spielfeld.
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	private void setUnitModelPositions(){
		logger.debug("Game.setUnitModelPositions: Gleiche Position der Unit-Models mit der Position der Einheiten in Karten-Koordinaten ab.");

		//Auflösungsparameter zum Berechnen der Mittelpunkte
		double resolution_x = 1024 * playingField.width / 128.0d;
		double resolution_y = 1024 * playingField.height / 128.0d;

		//Mittelpunkte der Models
		double center_x;
		double center_x_offsetted;
		double center_y;

		for(int i = 0; i < playingField.units.size(); i++){
			//Position der Einheit in Karten-Koordinaten
			int x = playingField.units.get(i).x;
			int y = playingField.units.get(i).y;

			//X-Koordinate des Model-Mittelpunktes berechnen
			center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (playingField.width + 0.5d);
			center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (playingField.width + 0.5d);

			//Y-Koordinate des Model-Mittelpunktes berechnen
			center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * playingField.height + 1)) - (resolution_y / (3 * playingField.height + 1)));

			//Model-Position anpassen
			if(y % 2 == 1){
				playingField.units.get(i).model.setLocalTranslation((float)center_x, 5.0f, (float)center_y);
			} else {
				playingField.units.get(i).model.setLocalTranslation((float)center_x_offsetted, 5.0f, (float)center_y);
			}

			playingField.units.get(i).model.move((float)resolution_x / -2.0f, 0.0f, (float)resolution_y / -2.0f);
		}

		logger.debug("Game.setUnitModelPositions: Alle Positionen der Unit-Models abgeglichen.");
	}



	/**
	 * Lässt alle Einheiten-Models zu Animationszwecken ein klein wenig rotieren.
	 * 
	 * @param tpf Der Time-per-Frame Wert, der von der simpleUpdate-Methode erzeugt wird.
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	private void rotateUnitModels(float tpf){
		for(int i = 0; i < playingField.units.size(); i++){
			playingField.units.get(i).model.getChild(0).rotate(0.0f, tpf * 0.1f, tpf * 0.05f);
		}
	}



	/**
	 * Lässt alle Healthbars zu Animationszwecken ein klein wenig rotieren.
	 * 
	 * @param tpf Der Time-per-Frame Wert, der von der simpleUpdate-Methode erzeugt wird.
	 * 
	 * @author Peter Dörr
	 * @since 07.01.13
	 */
	private void rotateHealthBars(float tpf){
		if(handleHealthBars){
			for(int i = 0; i < playingField.units.size(); i++){
				if(playingField.units.get(i).model.getQuantity() > 2){
					playingField.units.get(i).model.getChild(2).rotate(0.0f, tpf * 0.2f, 0.0f);
					playingField.units.get(i).healthBarRotation += tpf * 0.2f;
					if(playingField.units.get(i).healthBarRotation > 2.0f * (float)Math.PI){
						playingField.units.get(i).healthBarRotation -= 2.0f * (float)Math.PI;
					}
				}
			}
		}
	}



	/**
	 * Bewegt alle Einheiten der Blut-Animation und versetzt diese, wenn sie das Spielfeld verlassen, bzw. erzeugt neue Modelle.
	 * 
	 * @param tpf Der Time-per-Frame Wert, der von der simpleUpdate-Methode erzeugt wird.
	 * @param show Gibt an, ob die Animation gezeigt werden soll.
	 * 
	 * @author Peter Dörr
	 * @since 29.11.12
	 */
	private void handleBloodAnimation(float tpf, boolean show){

		if(show){

			//Auflösungsparameter zum Berechnen der Mittelpunkte
			float resolution_x = 1024 * playingField.width / 128.0f;
			float resolution_y = 1024 * playingField.height / 128.0f;

			//Zufallszahlengenerator
			Random random = new Random();

			//Models bewegen
			Node tmp = ((Node)rootNode.getChild("Node_BloodAnimation"));
			for(int i = 0; i < tmp.getQuantity(); i++){
				tmp.getChild(i).move(5.0f * tpf, 0.0f, 0.0f);
				tmp.getChild(i).rotate(2.5f * tpf, 2.5f * tpf, 0.0f);
			}

			//Neue Models erzeugen, wenn es noch zu wenige gibt
			while(tmp.getQuantity() < 500){
				Spatial model = Game.AM.loadModel("models/BloodCell.obj");
				model.scale(0.3f);
				model.move(random.nextFloat() * resolution_x - resolution_x / 2.0f, 1.0f, random.nextFloat() * resolution_y - resolution_y / 2.0f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				tmp.attachChild(model);
			}

			//Models, die sich zu weit vom Spielfeld entfernen, zurücksetzen
			for(int i = 0; i < tmp.getQuantity(); i++){
				if(tmp.getChild(i).getLocalTranslation().x > resolution_x / 2.0f){
					tmp.getChild(i).move(-resolution_x, 0.0f, 0.0f);
				}
			}

		} else {

			if(((Node)rootNode.getChild("Node_BloodAnimation")).getQuantity() > 0){
				Node tmp = ((Node)rootNode.getChild("Node_BloodAnimation"));
				tmp.detachAllChildren();
			}
		}
	}



	/**
	 * Erzeugt und zeigt die Hauptmenü-Hintergrundanimation.
	 * 
	 * @param tpf Der Time-per-Frame Wert, der von der simpleUpdate-Methode erzeugt wird.
	 * @param show Gibt an, ob die Animation gezeigt werden soll.
	 * 
	 * @author Peter Dörr
	 * @since 04.12.12
	 */
	private void handleMainMenuAnimation(float tpf, boolean show){

		if(show){
			//Auflösungsparameter zum Berechnen der Mittelpunkte
			float resolution_x = 150f;
			float resolution_y = 125f;

			//Zufallszahlengenerator
			Random random = new Random();

			//Models bewegen
			Node tmp = ((Node)rootNode.getChild("Node_MainMenuAnimation"));
			for(int i = 0; i < tmp.getQuantity(); i++){
				tmp.getChild(i).move(0.033f * tpf * tmp.getChild(i).getWorldBound().getVolume(), 0.0f, 0.0f);
				tmp.getChild(i).rotate(tpf, tpf, 0.0f);
			}

			//Neue Models erzeugen, wenn es noch zu wenige gibt
			while(tmp.getQuantity() < 100){
				int randomModel = random.nextInt(10);
				Spatial model = null;
				switch(randomModel){
				case 0:
					model = Game.AM.loadModel("models/AntiVir_Fighter_1.obj");
					Material mat_default = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
					mat_default.setTexture("ColorMap", Game.AM.loadTexture("models/Granulozyten_Textur1.jpg"));
					model.setMaterial(mat_default);
					break;
				case 1:
					model = Game.AM.loadModel("models/Germ_Fighter_1.obj");
					break;
				case 2:
					model = Game.AM.loadModel("models/AntiVir_Fighter_2.obj");
					break;
				case 3:
					model = Game.AM.loadModel("models/Germ_Fighter_2.obj");
					break;
				case 4:
					model = Game.AM.loadModel("models/AntiVir_Fighter_3.obj");
					break;
				case 5:
					model = Game.AM.loadModel("models/Germ_Fighter_3.obj");
					break;
				case 6:
					model = Game.AM.loadModel("models/AntiVir_Transporter_1.obj");
					break;
				case 7:
					model = Game.AM.loadModel("models/Germ_Transporter_1.obj");
					break;
				case 8:
					model = Game.AM.loadModel("models/AntiVir_Transporter_2.obj");
					break;
				case 9:
					model = Game.AM.loadModel("models/Germ_Transporter_2.obj");
					break;
				}

				model.scale(random.nextFloat() * 2.0f);
				model.move(random.nextFloat() * resolution_x - resolution_x / 2.0f, 1.0f, random.nextFloat() * resolution_y - resolution_y / 2.0f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				tmp.attachChild(model);
			}

			//Models, die sich zu weit vom Spielfeld entfernen, zurücksetzen
			for(int i = 0; i < tmp.getQuantity(); i++){
				if(tmp.getChild(i).getLocalTranslation().x > resolution_x / 2.0f){
					tmp.getChild(i).move(-resolution_x, 0.0f, 0.0f);
				}
			}

		} else {

			if(((Node)rootNode.getChild("Node_MainMenuAnimation")).getQuantity() > 0){
				Node tmp = ((Node)rootNode.getChild("Node_MainMenuAnimation"));
				tmp.detachAllChildren();
			}
		}
	}



	/**
	 * Behandelt die Autofokusfunktion, d.h. verfolgung der momentan selektierten Einheit mit der Kamera.
	 * 
	 * @param tpf Der tpf Parameter der update Methode.
	 * 
	 * @author Peter Dörr
	 * @since 16.01.13
	 */
	private void handleAutofocus(float tpf){
		//Abbrechen, wenn Autofokus deaktiviert ist
		if(autofocus_mode == false){
			autofocus_speedCurrent = 0.0f;
			return;
		}

		//Abbrechen, wenn keine selectedUnit existiert
		if(selectedUnit == null){
			autofocus_speedCurrent = 0.0f;
			return;
		}

		//Position der selektierten Einheit und der Kamera erhalten
		Vector3f unitPosition = selectedUnit.model.getLocalTranslation();
		Vector3f camPosition = cam.getLocation().subtract(0.0f, 0.0f, 20.0f);

		//Entfernung zwischen Kameraposition und Endposition berechnen (Pythagoras)
		float distance = (float)Math.sqrt(Math.pow(unitPosition.x - camPosition.x, 2.0d) + Math.pow(unitPosition.z - camPosition.z, 2.0d));

		//Distanz zu gering, Kamera nicht weiter bewegen
		if(distance < 0.1f){
			autofocus_speedCurrent = 0.0f;
			return;
		}

		//Kamera abbremsen oder beschleunigen
		if(distance < autofocus_speedDown * 100.0f * tpf){
			autofocus_speedCurrent -= autofocus_speedDown * tpf;
			if(autofocus_speedCurrent < 0.0f){
				autofocus_speedCurrent = 0.0f;
			}
		} else {
			autofocus_speedCurrent += autofocus_speedUp * tpf;
			if(autofocus_speedCurrent > autofocus_speedMax){
				autofocus_speedCurrent = autofocus_speedMax;
			}
		}

		//Kamera auf neue Position setzen
		Vector3f camVector = unitPosition.subtract(camPosition).normalize().mult(autofocus_speedCurrent);
		cam.setLocation(new Vector3f(camPosition.x + camVector.x, cam.getLocation().y, camPosition.z + camVector.z + 20.0f));
	}



	/**
	 * Verändert die Farbe eines oder aller Hexfelder zu einer bestimmten Farbe, gegeben durch die colorID.
	 * 
	 * @param point Punkt, der die Karten-Koordinaten des zu verändernden Hexagons definiert, oder (-1,-1), wenn alle Hexfelder verändert werden sollen.
	 * @param colorID 0 = Rot (1.0f, 0.0f, 0.0f, 0.5f), 1 = Gelb (1.0f, 1.0f, 0.0f, 0.5f), 2 = Grün (0.0f, 1.0f, 0.0f, 0.5f), 3 = Weiß (1.0f, 1.0f, 1.0f, 0.5f), 4 = Grau (0.4f, 0.4f, 0.4f, 1.0f), 5 = Blau (0.0f, 0.0f, 0.3f, 1.0f) alle anderen Werte = Schwarz (0.0f, 0.0f, 0.0f, 1.0f)
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	private void changeHexagonBaseColor(Point point, int colorID){
		//Knoten mit den Hexagon_Base Geometry-Objekten finden
		Node node = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base");

		//Farbe bereitstellen
		ColorRGBA color;
		switch(colorID){
		case 0:
			color = new ColorRGBA(1.0f, 0.0f, 0.0f, 0.5f);
			break;
		case 1:
			color = new ColorRGBA(1.0f, 1.0f, 0.0f, 0.5f);
			break;
		case 2:
			color = new ColorRGBA(0.0f, 1.0f, 0.0f, 0.5f);
			break;
		case 3:
			color = new ColorRGBA(1.0f, 1.0f, 1.0f, 0.5f);
			break;
		case 4:
			color = new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f);
			break;
		case 5:
			color = new ColorRGBA(0.0f, 0.0f, 0.3f, 1.0f);
			break;
		default:
			color = new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f);
			break;
		}

		//Hexfeld-Farbe anpassen
		if(point.x == -1 && point.y == -1){
			//Alle Hexfelder anpassen
			for(int i = 0; i < node.getQuantity(); i++){
				Geometry geo = (Geometry)node.getChild(i);
				Material mat = geo.getMaterial();
				mat.setColor("Color", color);
			}
		} else {
			//Nur ein spezielles Hexfeld anpassen
			for(int i = 0; i < node.getQuantity(); i++){
				Geometry geo = (Geometry)node.getChild(i);
				if(geo.getName().equals("HexagonBase_" + String.valueOf(point.x) + "_" + String.valueOf(point.y))){
					Material mat = geo.getMaterial();
					mat.setColor("Color", color);
					break;
				}
			}
		}
	}



	/**
	 * Verändert die Farbe eines oder aller Hexfelder-Grenzen zu einer bestimmten Farbe, gegeben durch die colorID.
	 * 
	 * @param point Punkt, der die Karten-Koordinaten der zu verändernden Hexagon-Border definiert, oder (-1,-1), wenn alle Hexfelder verändert werden sollen.
	 * @param colorID 0 = Blau (0.0f, 0.0f, 0.5f, 0.75f), 1 = Hellgelb (1.0f, 1.0f, 0.0f, 1.0f), 2 = Schwarz (0.0f, 0.0f, 0.0f, 1.0f), 3 = Grün (0.0f, 1.0f, 0.0f, 1.0f), 4 = Rot (1.0f, 0.0f, 0.0f, 1.0f), 5 = Hellgrau (0.6f, 0.6f, 0.6f, 1.0f), alle anderen Werte = Weiß (0.0f, 0.0f, 0.0f, 1.0f)
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	private void changeHexagonBorderColor(Point point, int colorID){
		//Knoten mit den Hexagon_Base Geometry-Objekten finden
		Node node = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Border");

		//Farbe bereitstellen
		ColorRGBA color;
		switch(colorID){
		case 0:
			color = new ColorRGBA(0.0f, 0.0f, 0.5f, 0.75f);
			break;
		case 1:
			color = new ColorRGBA(1.0f, 1.0f, 0.0f, 1.0f);
			break;
		case 2:
			color = new ColorRGBA(0.0f, 0.0f, 0.0f, 1.0f);
			break;
		case 3:
			color = new ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f);
			break;
		case 4:
			color = new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f);
			break;
		case 5:
			color = new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f);
			break;
		default:
			color = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
			break;

		}

		//Hexfeld-Farbe anpassen
		if(point.x == -1 && point.y == -1){
			//Alle Hexfelder anpassen
			for(int i = 0; i < node.getQuantity(); i++){
				Geometry geo = (Geometry)node.getChild(i);
				Material mat = geo.getMaterial();
				mat.setColor("Color", color);
			}
		} else {
			//Nur ein spezielles Hexfeld anpassen
			for(int i = 0; i < node.getQuantity(); i++){
				Geometry geo = (Geometry)node.getChild(i);
				if(geo.getName().equals("HexagonBorder_" + String.valueOf(point.x) + "_" + String.valueOf(point.y))){
					Material mat = geo.getMaterial();
					mat.setColor("Color", color);
					break;
				}
			}
		}
	}



	/**
	 * Bewegt eine Einheit von ihrer Position entlang eines Motionpaths, der mittels des A* Algorithmus berechnet wird, zu gegebenen Endkoordinaten.<br>
	 * Setzt ebenfalls die Karten-Koordinaten des entsprechenden Unit-Objektes auf x_des/y_des und zieht Bewegungspunkte ab.<br>
	 * Es muss VOR DEM AUFRUF DIESER METHODE überprüft werden, ob die Einheit mit den momentanen Bewegungspunkten dieses Feld erreichen kann, diese Methode tut dies explizit nicht!
	 * Programmcode basiert auf folgenden Quellen: <br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:motionpath <br>
	 * 
	 * @param unit Die Einheit, die bewegt werden soll.
	 * @param shortestPath Die Liste des kürzesten Weges, welcher durch die Pathfinding Klasse berechnet wird.
	 * @param timePerField Zeit, die pro abgeschrittenem Feld verbraucht wird.
	 * 
	 * @author Peter Dörr
	 * @since 20.11.12
	 * 
	 * 21.11.2012 (Markus Strobel) Überprüfung der movement points hinzugefügt, sodass die Einheit sich nur soweit bewegen kann, wie ihre movement points reichen
	 * 
	 * 29.11.2012 (Markus Strobel) Bug gefixt, welche eine NullPointer Exception verursachen konnte, da Weglänge des MotionPath seinen Startknoten mitzählt......<br>
	 * 
	 */
	@SuppressWarnings("unused")
	private void moveUnitAlongMotionPath(Unit unit, ArrayList<Point> shortestPath, float timePerField){
		logger.debug("Game.moveUnitAlongMotionPath: Richte Motionpath für Unit ein von X:" + String.valueOf(unit.x) + " Y:" + String.valueOf(unit.y) + " nach X:" + String.valueOf(shortestPath.get(shortestPath.size() - 1).x) + " Y:" + String.valueOf(shortestPath.get(shortestPath.size() - 1).y));

		//Auflösungsparameter zum Berechnen der Wegpunkte
		double resolution_x = 1024 * playingField.width / 128.0d;
		double resolution_y = 1024 * playingField.height / 128.0d;

		//Mittelpunkte der Wegpunkte
		double center_x;
		double center_x_offsetted;
		double center_y;

		//Unit-Index finden
		int index = -1;
		for(int i = 0; i < playingField.units.size(); i++){
			if(unit == playingField.units.get(i)){
				index = i;
			}
		}

		//Einheit konnte nicht gefunden werden
		if(index == -1){
			logger.debug("Game.moveUnitAlongMotionPath: Einheit konnte nicht gefunden werden!");
			return;
		}

		//Kürzesten Weg übergeben
		ArrayList<Vector3f> waypoints = new ArrayList<Vector3f>();

		if(shortestPath != null)
		{
			// Berechnet den Punkt bis wohin die Einheit laufen kann		
			int possible_x_des;
			int possible_y_des;
			int pathLength;
			if(unit.movement_curr > 0)
			{
				if(unit.movement_curr <= shortestPath.size())
				{
					possible_x_des = shortestPath.get(unit.movement_curr - 1).x;
					possible_y_des = shortestPath.get(unit.movement_curr - 1).y;
					pathLength = unit.movement_curr;
				}
				else // unit.movement_curr > shortestPath.size()
				{
					possible_x_des = shortestPath.get(shortestPath.size() - 1).x;
					possible_y_des = shortestPath.get(shortestPath.size() - 1).y;
					pathLength = shortestPath.size();				
				}

				for(int i = 0; i < pathLength; i++){
					logger.debug("Game.moveUnitAlongMotionPath: Wegpunkt " + String.valueOf(i) + " X:" + String.valueOf(shortestPath.get(i).x) + " Y:" + String.valueOf(shortestPath.get(i).y));
				}

				//Anfangspunkt hinzufügen
				waypoints.add(unit.model.getLocalTranslation());

				for(int i = 0; i < pathLength; i++){
					//Position des Wegpunktes in Karten-Koordinaten
					int x = shortestPath.get(i).x;
					int y = shortestPath.get(i).y;

					//X-Koordinate des Wegpunkt-Mittelpunktes berechnen
					center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (playingField.width + 0.5d);
					center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (playingField.width + 0.5d);

					//Y-Koordinate des Wegpunkt-Mittelpunktes berechnen
					center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * playingField.height + 1)) - (resolution_y / (3 * playingField.height + 1)));

					//Wegpunkt-Position festlegen
					if(y % 2 == 1){
						waypoints.add(new Vector3f((float)center_x, 5.0f, (float)center_y));
					} else {
						waypoints.add(new Vector3f((float)center_x_offsetted, 5.0f, (float)center_y));
					}

					waypoints.get(i+1).addLocal((float)resolution_x / -2.0f, 0.0f, (float)resolution_y / -2.0f);
				}

				//MotionPath erstellen
				motionPath = new MotionPath();
				for(int i = 0; i < waypoints.size(); i++){
					motionPath.addWayPoint(waypoints.get(i));
				}
				//motionPath.enableDebugShape(assetManager, rootNode);
				motionPath.setCycle(false);

				//MotionEvent einrichten
				MotionEvent motionEvent = new MotionEvent(unit.model, motionPath);
				motionEvent.setDirectionType(MotionEvent.Direction.None);
				motionEvent.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
				motionEvent.setInitialDuration(timePerField * pathLength);
				motionEvent.setSpeed(1.0f);
				motionEvent.play();
				//unit.model = motionEvent.getSpatial();

				//Listener einrichten, der nach Ablaufen des Pfades den MotionPath entfernt und - falls nötig - den Zug automatisch beendet
				motionPath.addListener(new MotionPathListener() {
					public void onWayPointReach(MotionEvent control, int wayPointIndex) {

						if(motionPath.getNbWayPoints() == wayPointIndex + 1){
							//motionPath.disableDebugShape();
							motionPath = null;
							currentState = STATES.READY;

							//Angreifen, falls nach der Bewegung noch ein Angriff auf eine Einheit durchgeführt werden soll
							if(actionAfterMovement == STATES.FIGHTING){
								//Gegner angreifen, falls in Reichweite und Zug beenden, wenn Angriff erfolgreich
								if(selectedUnit != null){
									if(attack(selectedUnit.ID, getUnitByCoordinates(tmp_x, tmp_y).ID) != -1){
										actionAfterMovement = STATES.READY;
										//endTurn();
									}
								}
							}

							//Handelsmenü öffnen, falls nach der Bewegung ein Handel durchgeführt werden soll
							if(actionAfterMovement == STATES.TRADE_ATTEMPT_TO_TRADE){
								if(selectedUnit != null){
									if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) == 1){
										if(getUnitByCoordinates(tmp_x, tmp_y).type != TYPE.Tradestation){
											gameScreen.openMakeTradeOfferPopup();
										} else {
											gameScreen.openTradeStationPopup(getUnitByCoordinates(tmp_x, tmp_y).ID);
										}
										currentState = STATES.TRADE_ATTEMPT_TO_TRADE;
									}
								}
							}

							//Bei eigenen Einheitenbewegungen den Zug beenden, falls nach der Bewegung potentiell keine andere Aktion mehr durchgeführt werden kann
							if(selectedUnit != null && currentTurn == TURN.PLAYER){
								if(getUnitSurroundingUnits(selectedUnit) == null){
									actionAfterMovement = STATES.ENEMY_TURN;
								} else {
									actionAfterMovement = null;
								}
							}
							if(actionAfterMovement == STATES.ENEMY_TURN){
								endTurn();
							}

							//Einheiten-StatusBox aktualisieren
							setStatusBoxByUnitInfos();
						}
					}
				});

			}
			else
			{
				possible_x_des = shortestPath.get(0).x; // Startknoten Wert x
				possible_y_des = shortestPath.get(0).y; // Startknoten Wert y
				pathLength = 0;
				currentState = STATES.READY;
			}

			//Unit-Objekt Karten-Koordinaten updaten
			if(shortestPath.size() > 0)
			{
				unit.x = possible_x_des;
				unit.y = possible_y_des;
			}   

			unit.movement_curr = unit.movement_curr - pathLength;	
		}
		else
		{
			currentState = STATES.READY;
		}
	}



	/**
	 * Erzeugt mehrere Partikel-Effekte zur Animation eines Angriffs einer Einheit.<br>
	 * Programmcode basiert auf folgenden Quellen: <br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:particle_emitters
	 * 
	 * @param position Position, von der die Partikel ausgehen.
	 * @param direction Position, auf den sich die Partikel zubewegen.
	 * 
	 * @author Peter Dörr
	 * @since 10.12.12
	 */
	private void showFightAnimation(Vector3f position, Vector3f direction){
		//Bewegungsrichtung der Partikel berechnen
		Vector3f velocity = position.add(-1.0f * direction.x, -1.0f * direction.y, -1.0f * direction.z).normalize().mult(10.0f).negate();

		//Partikeleffekt erzeugen (Gas)
		ParticleEmitter particleEmitter = new ParticleEmitter("Greasy_Stuff", ParticleMesh.Type.Triangle, 30);
		particleEmitter.setLocalTranslation(position);
		particleEmitter.getParticleInfluencer().setInitialVelocity(velocity);
		particleEmitter.setParticlesPerSec(100);
		particleEmitter.setLowLife(0.5f);
		particleEmitter.setHighLife(1.2f);
		particleEmitter.setStartColor(ColorRGBA.Green);
		particleEmitter.setEndColor(ColorRGBA.Green);
		particleEmitter.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));

		//Material erzeugen
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		material.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		particleEmitter.setMaterial(material);
		particleEmitter.setImagesX(2);
		particleEmitter.setImagesY(2);

		//Patikeleffekt anzeigen
		((Node)rootNode.getChild("Node_FightAnimation")).attachChild(particleEmitter);
		particleEmitter.setEnabled(true);

		//Zweiten Partikeleffekt erzeugen (Kleine rote Partikel)
		ParticleEmitter particleEmitter2 = new ParticleEmitter("Greasy_Stuff", ParticleMesh.Type.Triangle, 30);
		particleEmitter2.setLocalTranslation(position);
		particleEmitter2.getParticleInfluencer().setInitialVelocity(velocity);
		particleEmitter2.setParticlesPerSec(20);
		particleEmitter2.setLowLife(0.5f);
		particleEmitter2.setHighLife(1.2f);
		particleEmitter2.setStartSize(1.5f);
		particleEmitter2.setEndSize(0.1f);
		particleEmitter2.setStartColor(ColorRGBA.Red);
		particleEmitter2.setEndColor(ColorRGBA.Red);
		particleEmitter2.setGravity(new Vector3f(0.0f, 0.0f, 0.0f));

		//Material erzeugen
		Material material2 = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		material2.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
		particleEmitter2.setMaterial(material2);
		particleEmitter2.setImagesX(3);
		particleEmitter2.setImagesY(3);

		//Patikeleffekt anzeigen
		((Node)rootNode.getChild("Node_FightAnimation")).attachChild(particleEmitter2);
		particleEmitter2.emitAllParticles();

		//Kampfanimations-Timer setzen
		//Dieser wird in der simpleUpdate-Methode abgerufen und beendet nach einer bestimmten Zeit die Animation
		fightAnimationTimer = 0.0f;
	}



	/**
	 * Gibt die Karten-Koordinaten zu einem Hexagon wieder, auf dem momentan der Mauscursor ruht.
	 * Programmcode basiert auf folgenden Quellen: <br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_picking
	 * 
	 * @return Der Punkt, der die Karten-Koordinaten enthält, z.B. x=10, y=24 auf einer Karte von mindestens 11x25 Feldern. Null, falls die Geometry nicht im Node_Hexagon_Base Knoten gefunden werden konnte oder das Hexagon momentan nicht sichtbar ist.
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	public Point getSelectedPoint(){
		logger.debug("Game.control_click_getSelectedPoint: Suche nach Karten-Koordinaten eines Hexfeldes beim Mauscursor.");

		//Knoten mit den Hexagon_Base Geometry-Objekten finden
		Node node = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base");

		//Objekte initialisieren
		CollisionResults results = new CollisionResults();
		Vector2f click2d = inputManager.getCursorPosition();
		Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

		//Kollisionen mit Hexagon_Base-Objekten finden
		Ray ray = new Ray(click3d, dir);
		node.collideWith(ray, results);

		//Falls mindestens eine Kollision gefunden wurde, die erste nehmen und Punkt-Objekt zurückgeben (oder keine, falls diese nicht sichtbar ist)
		if(results.size() > 0){
			logger.debug("Game.control_click_getSelectedPoint: Karten-Koordinaten gefunden.");
			String name = results.getCollision(0).getGeometry().getName();
			name = name.substring(name.indexOf("_") + 1);
			int x = Integer.parseInt(name.substring(0, name.indexOf("_")));
			int y = Integer.parseInt(name.substring(name.indexOf("_") + 1));
			if(playingField.visibility[x][y]){
				return getSelectedPoint(results.getCollision(0).getGeometry());
			} else {
				return null;
			}
		}

		//Objekt nicht gefunden
		logger.debug("Game.control_click_getSelectedPoint: Keine Karten-Koordinaten gefunden!");
		return null;
	}



	/**
	 * Gibt die Karten-Koordinaten zu einem gegebenen Hexagon wieder.<br>
	 * Nur Geometry-Objekte des Node_Hexagon_Base Knotens übergeben!
	 * 
	 * @param geo Das Geometry-Objekt, das das Hexfeld darstellt. Im Node_Hexagon_Base Knoten zu finden.
	 * @return Der Punkt, der die Karten-Koordinaten enthält, z.B. x=10, y=24 auf einer Karte von mindestens 11x25 Feldern. Null, falls die Geometry nicht im Node_Hexagon_Base Knoten gefunden werden konnte.
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	private Point getSelectedPoint(Geometry geo){
		logger.debug("Game.click_getSelectedPoint: Suche nach Karten-Koordinaten eines Hexfeldes.");

		//Knoten mit den Hexagon_Base Geometry-Objekten finden
		Node node = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base");

		//Knoten nach dem Objekt durchsuchen
		for(int i = 0; i < node.getQuantity(); i++){
			if(geo == node.getChild(i)){
				//Aus dem Namen des Objektes die Koordinaten ableiten
				String name = geo.getName().substring(geo.getName().indexOf("_") + 1);
				Point point = new Point();
				point.x = Integer.parseInt(name.substring(0, name.indexOf("_")));
				point.y = Integer.parseInt(name.substring(name.indexOf("_") + 1));

				//Punkt ausgeben
				logger.debug("Game.click_getSelectedPoint: Karten-Koordinaten gefunden: X=" + String.valueOf(point.x) + " Y=" + String.valueOf(point.y));
				return point;
			}
		}

		//Objekt nicht gefunden
		logger.debug("Game.click_getSelectedPoint: Keine Karten-Koordinaten gefunden!");
		return null;
	}



	/**
	 * Fäbt Felder, die von einem Punkt aus mit der gegebenen Anzahl Bewegungspunkte erreicht werden können, grün, den rest Rot.
	 * 
	 * @param point Der Ausgangspunkt der Bewegung.
	 * @param movement Die Bewegungspunkte.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	public void setHexagonColorByMovementRange(Point point, int movement){

		//Einheit an dieser Koordinate suchen
		Unit unit = null;
		for(int i = 0; i < playingField.units.size(); i++){
			if(point.x == playingField.units.get(i).x && point.y == playingField.units.get(i).y){
				unit = playingField.units.get(i);
			}
		}

		//Keine Einheit gefunden, abbruch
		if(unit == null){
			return;
		}

		//Abbruch bei Handelsstationen
		if(unit.type == TYPE.Tradestation){
			return;
		}

		//Nur die Punkte untersuchen, die potentiell überhaupt erreicht werden können
		for(int x = 0; x < playingField.width; x++){
			for(int y = 0; y < playingField.height; y++){

				//Unbegehbare Felder ignorieren
				if(playingField.map[x][y] == 0){
					continue;
				}

				//Falls Winconditions-Panel angezeigt wird: Goodplacement-Felder markieren
				if(winconditions.goodPlacements != null){
					boolean goodplacement = false;
					for(int i = 0; i < winconditions.goodPlacements.get(0).size(); i++){
						if(winconditions.goodPlacements.get(3).get(i) == x && winconditions.goodPlacements.get(4).get(i) == y){
							changeHexagonBaseColor(new Point(x, y), 5);
							goodplacement = true;
						}
					}
					if(goodplacement){
						continue;
					}
				}

				//Nicht sichtbare Felder ignorieren
				if(!playingField.visibility[x][y]){
					changeHexagonBaseColor(new Point(x, y), 4);
					changeHexagonBorderColor(new Point(x, y), 5);
					continue;
				}

				//Pauschal Feld rot färben
				changeHexagonBaseColor(new Point(x, y), 0);
				changeHexagonBorderColor(new Point(x, y), 0);

				//Wegpunkte einzeichnen
				for(int j = 0; j < WayPointList.size(); j++){
					if(WayPointList.get(j).x == x && WayPointList.get(j).y == y){
						changeHexagonBorderColor(new Point(x, y), -1);
					}
				}

				//Abbrechen, wenn Feld nicht erreichbar
				ArrayList<Point> shortestPath = pathFinding.getShortestPath(playingField.map, playingField.height, playingField.width, playingField.units, unit.ID, x, y, false);
				if(shortestPath == null){
					continue;
				}

				//Felder mit Einheiten in Bewegungsreichweite grün färben und für die Bewegung ignorieren
				boolean unitAtCoordinates = false;
				for(int i = 0; i < playingField.units.size(); i++){
					if(x == playingField.units.get(i).x && y == playingField.units.get(i).y){
						if(shortestPath.size() <= movement){
							changeHexagonBaseColor(new Point(x, y), 1);
							if(playingField.units.get(i).type != TYPE.Tradestation){
								//Hexfeldgrenzen bei freundlichen Einheiten grün, ansonsten rot färben
								if(playingField.units.get(i).ownerID.equals(unit.ownerID)){
									changeHexagonBorderColor(new Point(x, y), 3);
								} else {
									changeHexagonBorderColor(new Point(x, y), 4);
								}
							} else {
								//Hexfeldgrenzen bei freundlichen Handelsstationen gelb, ansonsten rot färben
								changeHexagonBorderColor(new Point(x, y), 4);
								if(playingField.units.get(i).tradestation_friendlyPlayerIndexes.get(0) == -1){
									changeHexagonBorderColor(new Point(x, y), 3);
								} else {
									for(int j = 0; j < playingField.units.get(i).tradestation_friendlyPlayerIndexes.size(); j++){
										if(playerIDs.get(playingField.units.get(i).tradestation_friendlyPlayerIndexes.get(j)).equals(unit.ownerID)){
											changeHexagonBorderColor(new Point(x, y), 3);
										}
									}
								}
							}
						}
						unitAtCoordinates = true;
					}
				}
				if(unitAtCoordinates){
					continue;
				}

				//Wenn Feld mit den Bewegungspunkten erreichbar, grün färben
				if(shortestPath.size() <= movement){
					changeHexagonBaseColor(new Point(x, y), 2);
				}
			}
		}
	}



	/**
	 * Setzt alle Hexagon-Bases auf eine durch die ColorID bestimmte Farbe. Die ColorID ist im Meta-Code der changeHexagonBaseColor-Methode einsehbar.
	 * 
	 * @param colorID Die ID der Farbe, die auf alle Hexagons angewendet werden soll.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	public void setHexagonBaseColorAll(int colorID){
		//Alle Felder einfärben
		for(int x = 0; x < playingField.width; x++){
			for(int y = 0; y < playingField.height; y++){
				if(playingField.visibility[x][y]){
					//Nicht sichtbares Feld
					changeHexagonBaseColor(new Point(x, y), colorID);
				} else {
					//Sichtbares Feld
					changeHexagonBaseColor(new Point(x, y), 4);
				}
				//Falls Winconditions-Panel angezeigt wird: Goodplacement-Felder markieren
				if(winconditions.goodPlacements != null){
					for(int i = 0; i < winconditions.goodPlacements.get(0).size(); i++){
						if(winconditions.goodPlacements.get(3).get(i) == x && winconditions.goodPlacements.get(4).get(i) == y){
							changeHexagonBaseColor(new Point(x, y), 5);
						}
					}
				}
			}
		}
	}



	/**
	 * Setzt alle Hexagon-Borders auf eine durch die ColorID bestimmte Farbe. Die ColorID ist im Meta-Code der changeHexagonBorderColor-Methode einsehbar.
	 * 
	 * @param colorID Die ID der Farbe, die auf alle Hexagons angewendet werden soll.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	public void setHexagonBorderColorAll(int colorID){
		//Alle Felder einfärben
		for(int x = 0; x < playingField.width; x++){
			for(int y = 0; y < playingField.height; y++){
				if(playingField.visibility[x][y]){
					changeHexagonBorderColor(new Point(x, y), colorID);
				} else {
					changeHexagonBorderColor(new Point(x, y), 5);
				}
			}
		}
	}



	/**
	 * Aktualisiert die StatusBox über den Einheiten.
	 * 
	 * @author Peter Dörr
	 * @since 14.12.12
	 */
	public void setStatusBoxByUnitInfos(){
		for(int i = 0; i < playingField.units.size(); i++){
			if(playingField.units.get(i).type != TYPE.Tradestation){
				//Eigene noch verfügbare Einheiten
				if(playingField.units.get(i).ownerID.equals(playerID) && playingField.units.get(i).doneThisRound == false){
					((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Green);
				}

				//Eigene bereits bewegte Einheiten
				if(playingField.units.get(i).ownerID.equals(playerID) && playingField.units.get(i).doneThisRound == true){
					((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Black);
				}

				//Feindliche Einheiten
				if(!playingField.units.get(i).ownerID.equals(playerID)){
					((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Red);
				}
			} else {
				//Feindliche Handelsstation
				((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Red);

				//Freundliche Handelsstation
				if(playingField.units.get(i).tradestation_friendlyPlayerIndexes.get(0) == -1){
					((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Green);
				} else {
					for(int j = 0; j < playingField.units.get(i).tradestation_friendlyPlayerIndexes.size(); j++){
						if(playerIDs.get(playingField.units.get(i).tradestation_friendlyPlayerIndexes.get(j)).equals(playerID)){
							((Geometry)playingField.units.get(i).model.getChild(1)).getMaterial().setColor("Color", ColorRGBA.Green);
						}
					}
				}
			}
		}
	}



	/**
	 * Berechnet den Fog of War.
	 * 
	 * @param mode Art des Nebels (aus, statisch oder dynamisch).
	 * 
	 * @author Peter Dörr
	 * @since 14.12.12
	 */
	public void calculateVisibility(FOG mode){
		try{
			switch(mode)
			{
			case OFF:
				//Alle Felder sichtbar
				for(int x = 0; x < playingField.width; x++){
					for(int y = 0; y < playingField.height; y++){
						playingField.visibility[x][y] = true;
					}	
				}
				break;

			case STATIC:
				for(int x = 0; x < playingField.width; x++){
					for(int y = 0; y < playingField.height; y++){

						//Bereits sichtbare Felder sichtbar lassen
						if(playingField.visibility[x][y]){
							continue;
						}

						for(int i = 0; i < playingField.units.size(); i++){
							if(playingField.units.get(i).type != TYPE.Tradestation){
								if(playingField.units.get(i).ownerID.equals(playerID)){
									if (pathFinding.getDistanceForFOW(new Point(playingField.units.get(i).x, playingField.units.get(i).y), new Point(x, y)) <= playingField.units.get(i).movement_max){
										playingField.visibility[x][y] = true;
										break;
									}
								}
							}
						}
					}
				}

				//				for(int x = 0; x < playingField.width; x++){
				//					for(int y = 0; y < playingField.height; y++){
				//
				//						//Bereits sichtbare Felder sichtbar lassen
				//						if(playingField.visibility[x][y]){
				//							continue;
				//						}
				//
				//						//Falls Feld noch nicht sichtbar, überprüfen, ob es von einer freundlichen Einheit eingesehen werden kann
				//						playingField.visibility[x][y] = calculateFieldVisibility(x, y);
				//					}
				//				}
				break;

			case DYNAMIC:

				for(int x = 0; x < playingField.width; x++){
					for(int y = 0; y < playingField.height; y++){

						playingField.visibility[x][y] = false;

						for(int i = 0; i < playingField.units.size(); i++){
							if(playingField.units.get(i).type != TYPE.Tradestation){
								if(playingField.units.get(i).ownerID.equals(playerID)){
									if (pathFinding.getDistanceForFOW(new Point(playingField.units.get(i).x, playingField.units.get(i).y), new Point(x, y)) <= playingField.units.get(i).movement_max){
										playingField.visibility[x][y] = true;
										break;
									}
								}
							}
						}
					}
				}



				//				for(int x = 0; x < playingField.width; x++){
				//					for(int y = 0; y < playingField.height; y++){
				//						playingField.visibility[x][y] = calculateFieldVisibility(x, y);
				//					}
				//				}
				break;
			}
		} catch (Exception e){
			logger.error("Game.calculateVisibility: Fehler bei LOS Berechnung!");
		}

		//Objekte nach Sichtbarkeit ein- und ausblenden
		showAssetsByVisibility();
	}



	/**
	 * Berechnet, ob ein bestimmtes Feld sichtbar ist oder nicht. Sollte nur durch die calculateVisibility-Methode aufgerufen werden. 
	 * 
	 * @param x X-Koordinate des Feldes.
	 * @param y Y-Koordinate des Feldes.
	 * @return True, wenn das Feld von mindestens einer Einheit eingesehen wird, ansonsten false.
	 * 
	 * @author Markus Strobel
	 * @since 16.01.2013<br>
	 */
	@SuppressWarnings("unused")
	private boolean calculateFieldVisibility(int x, int y){

		// Zielknoten
		Point targetPoint = new Point(x, y);

		try {

			// Hindernis-Menge
			HashSet<Point> obstaclesSet = generateObstaclesSet();

			// Liste der freundlichen Einheiten erstellen
			ArrayList<Unit> friendlyUnitsList = new ArrayList<Unit>(); 

			for(int i = 0; i < playingField.units.size(); i++)
			{
				if(playingField.units.get(i).ownerID.equals(playerID))
				{
					friendlyUnitsList.add(playingField.units.get(i));
				}
			}

			// Distanz von Start zu Zielknoten überprüfen
			// Und Einheiten rausfiltern die zu weit weg sind
			for(int i = 0; i < friendlyUnitsList.size(); i++)
			{
				int distance = pathFinding.getDistance(new Point(friendlyUnitsList.get(i).x, friendlyUnitsList.get(i).y) , targetPoint);
				int viewRange = friendlyUnitsList.get(i).visibilityRange;

				if(distance > viewRange)
				{
					friendlyUnitsList.remove(i);
					i--;
				}
			}	

			// Falls keine Einheit in Reichweite ist, dann ist das Feld nicht sichtbar
			if(friendlyUnitsList.size() == 0)
			{
				System.out.println("No Units in Range: false");
				return false;
			}		

			// Prüfen der Sichtlinie zwischen Einheit und Zielfeld
			ArrayList<Point> lineOfSightPointsList = new ArrayList<Point>();

			for(int i = 0; i < friendlyUnitsList.size(); i++)
			{

				Point unitPoint = new Point(friendlyUnitsList.get(i).x, friendlyUnitsList.get(i).y);
				lineOfSightPointsList = getLineOfSightList(unitPoint ,targetPoint);

				// Startknoten entfernen, da die Einheit ihr eigenes Feld natürlich sieht.
				lineOfSightPointsList.remove(0);

				// abbruch bedingung
				boolean visible = true;

				for(int j = 0; j <lineOfSightPointsList.size(); j++)
				{				
					if(visible)
					{
						// Prüfen ob Feld von einem Hindernis versehen ist, 
						// die Liste wird solange geleert bis sie auf ein Hindernis trifft oder leer is
						if(obstaclesSet.contains(new Point(lineOfSightPointsList.get(j).x, lineOfSightPointsList.get(j).y)))
						{
							System.out.println("Hindernis gefunden: X: " + lineOfSightPointsList.get(j).x + "  Y: " + lineOfSightPointsList.get(j).y);
							// Hindernis gefunden, dieses ist sichtbar und wurde entfernt, ab jetzt gehts im else weiter
							lineOfSightPointsList.remove(j);
							j--;
							visible = false;
						}
						else
						{
							// Wenn der Punkt nicht in den Hindernissen ist, dann ist er sichtbar und fällt raus
							lineOfSightPointsList.remove(j);
							j--;
						}
					}
					else
					{
						// Jetzt wird geprüft, ob sich der targetPoint innerhalb der verbliebenen lineOfSightPointsList, ob dieser verdeckt ist.
						if(lineOfSightPointsList.contains(targetPoint))
						{
							System.out.println("PUNKT: X:" +  targetPoint.x + " Y: " + targetPoint.y + " NICHT SICHTBAR");
							return false;
						}
					}
				}					
			}	
			// Wenn keine der lineOfSightPointsLists den targetPoint enthielten, dann ist dieser sichtbar also true.
			System.out.println("PUNKT: X:" +  targetPoint.x + " Y: " + targetPoint.y + " IST SICHTBAR");

			return true;

		} 
		catch (Exception e) 
		{
			System.out.println("CATCH: X:" +  targetPoint.x + " Y: " + targetPoint.y);
			logger.error("Game.calculateFieldVisibility: CATCH: X:" +  targetPoint.x + " Y: " + targetPoint.y);
			return false;
		}
	}

	/**
	 * Diese Methode ermittelt die Line of Sight zwischen 2 Hex-Feldern.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 16/17.01.2013<br>
	 * 
	 * @param start der Startknoten
	 * @param target der Zielknoten
	 * 
	 * @return die Hexagon-Punkte zwischen den 2 Start- und Zielknoten<br>
	 * 
	 * basiert auf folgender Quelle:
	 * http://zvold.blogspot.de/2010/02/line-of-sight-on-hexagonal-grid.html
	 * (last seen 16.01.2013 23:56)
	 */
	private ArrayList<Point> getLineOfSightList(Point start, Point target)
	{		
		PathFinding pathFinding = new PathFinding();

		if(start.x == 5 && start.y == 4)
		{
			if(target.x == 2 && target.y == 1)
			{
				System.out.println("BREAKPOINT START");		
			}
		}

		ArrayList<Point> temp = pathFinding.getDirectLineFromPointToPoint(playingField.map, playingField.height, playingField.width, start, target);

		return temp;


		//		Point current = start;
		//		ArrayList<Point> lineOfSightList = new ArrayList<Point>();		
		//		lineOfSightList.add(current);
		//		
		//		float deltaX = (2f * (target.x - start.x) + Math.abs(target.y % 2f) - Math.abs(start.y % 2f));
		//		float deltaY = target.y - start.y;		
		//		float signumX = Math.signum(deltaX);
		//		float signumY = Math.signum(deltaY);
		//		System.out.println("SignumX: " + signumX);
		//		System.out.println("SignumY: " + signumY);
		//		float absDeltaX = Math.abs(deltaX);
		//		float absDeltaY = Math.abs(deltaY);		
		//		
		//		
		//		float epsilon = 2f * absDeltaX;
		////		float epsilon = 0;
		//		
		//		
		//		while(current.x != target.x || current.y != target.y)
		//		{
		//			
		//			if(start.x == 7 && start.y == 16)
		//			{
		//				if(target.x == 0 && target.y == 9)
		//				{
		//					if(current.x == 1 && current.y == 9)
		//					{
		//						System.out.println("BREAKPOINT START");		
		//					}
		//				}
		//			}
		//			
		//			System.out.println("start: " + start.x + ", " + start.y +"    current: " + current.x + ", " + current.y + "     target: " + target.x + ", " + target.y);
		//			if(epsilon >= 0)
		//			{
		//				current = getDirection(current, (int) -signumX, (int) signumY);
		//				epsilon = epsilon - 3 * absDeltaY - 3 * absDeltaX;
		//			}
		//			else
		//			{
		//				epsilon = epsilon + 3 * absDeltaY;
		//				if(epsilon > -absDeltaX)
		//				{
		//					current = getDirection(current, (int) signumX, (int) signumY);
		//					epsilon = epsilon - 3 * absDeltaX;
		//				}
		//				else
		//				{
		//					System.out.println("EPSILON: " + epsilon);
		//					if(epsilon < -3 * absDeltaX)
		//					{
		//						current = getDirection(current, (int) signumX, (int) -signumY);
		//						epsilon = epsilon + 3 * absDeltaX;
		//					}
		//					else
		//					{
		//						current = getDirection(current, 0, (int) signumX);
		//						epsilon = epsilon + 3 * absDeltaY;
		//					}
		//				}
		//			}
		//			lineOfSightList.add(current);
		//		}


		//		while(current.x != target.x || current.y != target.y)
		//		{
		//			
		//			if(start.x == 7 && start.y == 16)
		//			{
		//				if(target.x == 0 && target.y == 9)
		//				{
		//					if(current.x == 1 && current.y == 9)
		//					{
		//						System.out.println("BREAKPOINT START");		
		//					}
		//				}
		//			}
		//			
		//			System.out.println("EPSILON: " + epsilon);
		//			System.out.println("start: " + start.x + ", " + start.y +"    current: " + current.x + ", " + current.y + "     target: " + target.x + ", " + target.y);
		//			if(epsilon >= 0)
		//			{
		//				current = getDirection(current, (int) -signumX, (int) signumY);
		//				epsilon = epsilon - 3 * absDeltaY - 3 * absDeltaX;
		//			}
		//			else
		//			{
		//				epsilon = epsilon + 3 * absDeltaY;
		//				System.out.println("EPSILON: " + epsilon);
		//				if(epsilon > -absDeltaX)
		//				{
		//					current = getDirection(current, (int) signumX, (int) signumY);
		//					epsilon = epsilon - 3 * absDeltaX;
		//				}
		//				else
		//				{
		//					if(epsilon < -3 * absDeltaX)
		//					{
		//						current = getDirection(current, (int) signumX, (int) -signumY);
		//						epsilon = epsilon + 3 * absDeltaX;
		//					}
		//					else
		//					{
		//						current = getDirection(current, 0, (int) signumX);
		//						epsilon = epsilon + 3 * absDeltaY;
		//					}
		//				}
		//			}
		//			lineOfSightList.add(current);
		//		}		
		//		return lineOfSightList;
	}


	/**
	 * Diese Methode ermittelt zu welchem Punkt als nächstes gegangen wird für die line of sight.<br>
	 * 
	 * @param current der aktuelle Punkt
	 * @param i das Mapping für das Ziel (switch(i))
	 * @param j das Mapping für das Ziel (switch(j))
	 * @return gibt den nächsten Punkt der line of sight aus
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	@SuppressWarnings("unused")
	private Point getDirection(Point current, int i, int j)
	{
		//		switch(j) // links unten 0,0
		//		{		
		//		case 1:
		//			switch(i)
		//			{
		//			case 1:
		//				return toUpRight(current);
		//			case 0:
		//				return toRight(current);
		//			case -1:
		//				return toUpLeft(current);
		//			default:
		//				return null;
		//			}
		//		case -1:
		//			switch(i)
		//			{
		//			case 1:
		//				return toDownRight(current);
		//			case 0:
		//				return toLeft(current);
		//			case -1:
		//				return toDownLeft(current);
		//			default:
		//				return null;
		//			}
		//		default:
		//			return null;
		//		}		

		switch(j) // rechts unten 0,0
		{		
		case 1:
			switch(i)
			{
			case 1:
				return toUpLeft(current);
			case 0:
				return toLeft(current);
			case -1:
				return toUpRight(current);
			default:
				return null;
			}
		case -1:
			switch(i)
			{
			case 1:
				return toDownLeft(current);
			case 0:
				return toRight(current);
			case -1:
				return toDownRight(current);
			default:
				return null;
			}
		default:
			return null;
		}

	}


	/**
	 * Diese Methode ermittelt den Punkt rechts vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt rechts von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toRight(Point current)
	{
		System.out.println("toRight");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x - 1, current.y);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x - 1, current.y);
		}

	}

	/**
	 * Diese Methode ermittelt den Punkt links vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt links von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toLeft(Point current)
	{
		System.out.println("toLeft");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x + 1, current.y);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x + 1, current.y);
		}
	}

	/**
	 * Diese Methode ermittelt den Punkt rechts oben vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt rechts oben von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toUpRight(Point current)
	{
		System.out.println("toUpRight");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x, current.y + 1);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x - 1, current.y + 1);
		}
	}

	/**
	 * Diese Methode ermittelt den Punkt links unten vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt links unten von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toDownLeft(Point current)
	{
		System.out.println("toDownLeft");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x + 1, current.y - 1);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x, current.y - 1);
		}
	}

	/**
	 * Diese Methode ermittelt den Punkt links oben vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt links oben von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toUpLeft(Point current)
	{
		System.out.println("toUpLeft");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x + 1, current.y + 1);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x, current.y + 1);
		}
	}

	/**
	 * Diese Methode ermittelt den Punkt rechts unten vom Ausgangspunkt current
	 * @param current der Ausgangspunkt
	 * @return gibt den Punkt rechts unten von current zurück
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	private Point toDownRight(Point current)
	{
		System.out.println("toDownRight");
		if(current.y % 2 == 0)
		{
			// GERADE REIHE
			return new Point(current.x, current.y - 1);
		}
		else
		{
			// UNGERADE REIHE
			return new Point(current.x - 1, current.y - 1);
		}
	}

	/**
	 * Diese Methode berechnet die Menge aller Hindernisse, welche das Sichtfeld beeinflussen können.
	 * 
	 * @author Markus Strobel<br>
	 * @since 16.01.2013<br>
	 * 
	 * @return die Menge der Hindernisse<br>
	 */
	private HashSet<Point> generateObstaclesSet()
	{
		// NICHT BEGEHBARE FELDER WERDEN ALS HINDERNISSE BETRACHTET
		// Terrain der Hindernis-Menge hinzufügen
		HashSet<Point> obstaclesSet = new HashSet<Point>();
		// X - Achse
		for(int xValue = 0; xValue < playingField.map.length; xValue++)
		{
			// Y - Achse
			for(int yValue = 0; yValue < playingField.map[0].length; yValue++)
			{
				if(playingField.map[xValue][yValue] == 0)
				{
					obstaclesSet.add(new Point(xValue, yValue));
				}
			}
		}

		// SICH IM WEG BEFINDLICHE EINHEITEN
		// Einheiten Hindernis-Menge hinzufügen
		for(int i = 0; i < playingField.units.size(); i++)
		{
			obstaclesSet.add(new Point(playingField.units.get(i).x, playingField.units.get(i).y));
		}

		return obstaclesSet;
	}


	/**
	 * Zeigt oder blendet Felder und Einheiten aus, je nachdem, ob sie im Sichtbereich liegen oder nicht.
	 * 
	 * @author Peter Dörr
	 * @since 15.01.13
	 */
	private void showAssetsByVisibility(){
		for(int x = 0; x < playingField.width; x++){
			for(int y = 0; y < playingField.height; y++){
				//Sichtbare Felder und Einheiten
				if(playingField.visibility[x][y]){

					//Einheiten einblenden
					if(getUnitByCoordinates(x, y) != null){
						for(int i = 0; i < getUnitByCoordinates(x, y).model.getQuantity(); i++){
							getUnitByCoordinates(x, y).model.getChild(i).setCullHint(CullHint.Dynamic);
						}
					}

					//Hexfelder einblenden
					Node node_hexbase = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base");
					Node node_hexborder = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Border");
					Node node_hexcenter = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Center");

					for(int i = 0; i < node_hexbase.getQuantity(); i++){
						Geometry geo = (Geometry)node_hexbase.getChild(i);
						if(geo.getName().equals("HexagonBase_" + String.valueOf(x) + "_" + String.valueOf(y))){
							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
						}
						geo = (Geometry)node_hexborder.getChild(i);
						if(geo.getName().equals("HexagonBorder_" + String.valueOf(x) + "_" + String.valueOf(y))){
							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
						}
						geo = (Geometry)node_hexcenter.getChild(i);
						if(geo.getName().equals("HexagonCenter_" + String.valueOf(x) + "_" + String.valueOf(y))){
							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
						}
					}
				}

				//Unsichtbare Felder und Einheiten
				else {
					//Einheiten ausblenden
					if(getUnitByCoordinates(x, y) != null){
						for(int i = 0; i < getUnitByCoordinates(x, y).model.getQuantity(); i++){
							getUnitByCoordinates(x, y).model.getChild(i).setCullHint(CullHint.Always);
						}
					}

					changeHexagonBaseColor(new Point(x, y), 4);
					changeHexagonBorderColor(new Point(x, y), 5);
					
					//					//Hexfelder ausblenden
					//					Node node_hexbase = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Base");
					//					Node node_hexborder = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Border");
					//					Node node_hexcenter = (Node)((Node)rootNode.getChild("Node_Hexagon")).getChild("Node_Hexagon_Center");
					//					
					//					for(int i = 0; i < node_hexbase.getQuantity(); i++){
					//						Geometry geo = (Geometry)node_hexbase.getChild(i);
					//						if(geo.getName().equals("HexagonBase_" + String.valueOf(x) + "_" + String.valueOf(y))){
					//							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.FrontAndBack);
					//						}
					//						geo = (Geometry)node_hexborder.getChild(i);
					//						if(geo.getName().equals("HexagonBorder_" + String.valueOf(x) + "_" + String.valueOf(y))){
					//							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.FrontAndBack);
					//						}
					//						geo = (Geometry)node_hexcenter.getChild(i);
					//						if(geo.getName().equals("HexagonCenter_" + String.valueOf(x) + "_" + String.valueOf(y))){
					//							geo.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.FrontAndBack);
					//						}
					//					}
				}
				
				//Falls Winconditions-Panel angezeigt wird: Goodplacement-Felder markieren
				if(winconditions.goodPlacements != null){
					for(int i = 0; i < winconditions.goodPlacements.get(0).size(); i++){
						if(winconditions.goodPlacements.get(3).get(i) == x && winconditions.goodPlacements.get(4).get(i) == y){
							changeHexagonBaseColor(new Point(x, y), 5);
						}
					}
				}
			}
		}
	}



	/**
	 * Bewegt eine Einheit zu den gegebenen Koordinaten, wenn das entsprechende Feld frei ist und die Bewegungspunkte ausreichen.<br>
	 * Bewegt eine Einheit in die Nähe der gegebenen Koordinaten, falls die Bewegungspunkte nicht ausreichen oder das Zielfeld von einer Einheit belegt ist.
	 * 
	 * @param unitID UnitID der Einheit, die bewegt werden soll.
	 * @param x X-Koordinate des Ziel-Feldes.
	 * @param y Y-Koordinate des Ziel-Feldes.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	private void move(int unitID, int x, int y){

		logger.info("Game.move: Einheit " + String.valueOf(unitID) + " bewegt sich von X:" + String.valueOf(this.getUnitByUnitID(unitID).x) + " Y:" + String.valueOf(this.getUnitByUnitID(unitID).y) + " nach X:" + String.valueOf(x) + " Y:" + String.valueOf(y));

		//Abbrechen, wenn Handelsstation
		if(getUnitByUnitID(unitID).type == TYPE.Tradestation){
			return;
		}

		//Abbrechen, wenn die Einheit garkeine Bewegungspunkte mehr besitzt
		if(getUnitByUnitID(unitID).movement_curr <= 0){
			return;
		}

		//Pfad berechnen
		ArrayList<Point> shortestPath = null;
		if(WayPointList.size() == 0){
			//Es existieren keine Wegpunkte, direkten Weg nehmen (+Follow-Funktion)
			if(getUnitByUnitID(unitID).order == ORDERS.FOLLOW){

				shortestPath = new ArrayList<Point>();
				int movement_curr = getUnitByUnitID(unitID).movement_curr;
				int followBufferSize = getUnitByUnitID(unitID).followBuffer.size();
				for(int i = 0; i < Math.min(movement_curr, followBufferSize); i++){
					if(getUnitByCoordinates(getUnitByUnitID(unitID).followBuffer.get(0).x, getUnitByUnitID(unitID).followBuffer.get(0).y) == null){
						shortestPath.add(getUnitByUnitID(unitID).followBuffer.remove(0));
					} else {
						break;
					}
				}
				if(shortestPath.size() == 0){
					shortestPath = null;
				}
			} else {
				shortestPath = pathFinding.getShortestPath(playingField.map, playingField.height, playingField.width, playingField.units, selectedUnit.ID, x, y, false);
			}
		} else {
			//Es existieren Wegpunkte, diese ablaufen und anschließend zur x/y Position bewegen
			shortestPath = new ArrayList<Point>();
			ArrayList<Point> shortestPathSlice = new ArrayList<Point>();
			boolean wayPointsOK = true;

			//Ausgangspunkt und Zielpunkt der Einheit als Wegpunkt betrachten
			WayPointList.add(0, new Point(selectedUnit.x, selectedUnit.y));
			WayPointList.add(new Point(x, y));

			//Wegpunkte ablaufen
			for(int i = 0; i < WayPointList.size() - 1; i++){
				if(wayPointsOK){
					shortestPathSlice = pathFinding.getShortestPathFromPointToPoint(playingField.map, playingField.height, playingField.width, playingField.units, WayPointList.get(i), WayPointList.get(i + 1), false);
					if(shortestPathSlice == null){
						wayPointsOK = false;
						break;
					}

					for(int j = 0; j < shortestPathSlice.size(); j++){
						shortestPath.add(shortestPathSlice.get(j));
					}
				}
			}

			//Wegpunktliste löschen
			WayPointList.clear();

			//Wenn es beim Suchen des Weges einen Fehler gab, keine Bewegung ausführen
			if(!wayPointsOK){
				shortestPath = null;
				setHexagonBaseColorAll(2);
				setHexagonBorderColorAll(0);
			}
		}

		//Es existiert kein Weg zu den gegebenen Koordinaten
		if(shortestPath == null){
			return;
		}

		//Es wurde auf das Feld der Einheit selbst geklickt
		if(shortestPath.size() == 0){
			return;
		}

		//Wird auf eine (andere) Einheit geklickt, wird auf das nächstgelegene Feld gezogen (d.h. vom Pfad wird der letzte Punkt entfernt)
		for(int i = 0; i < playingField.units.size(); i++){
			if(shortestPath.get(shortestPath.size() - 1).x == playingField.units.get(i).x && shortestPath.get(shortestPath.size() - 1).y == playingField.units.get(i).y)	
			{												
				shortestPath.remove(shortestPath.size() - 1);												
			}
		}

		//Die Bewegungspunkte reichen nicht aus, der Pfad wird gekürzt
		while(shortestPath.size() > getUnitByUnitID(unitID).movement_curr){
			shortestPath.remove(shortestPath.size() - 1);
		}

		networkThread.sendIComObject(new ComObject_move(clientID, clientKey, playerID, gameID, selectedUnit.ID, shortestPath));

		//		//Bewegungsbefehl an Server senden
		//		ComObject_move move;
		//		try {
		//			move = (ComObject_move)network.establishCommunication(new ComObject_move(clientID, clientKey, playerID, gameID, selectedUnit.ID, shortestPath));
		//			
		//			//Server gibt OK
		//			if(move.getStatus() == STATUS.OK) {
		//				//HexagonBorder-Farbe des Ausgangspunktes der Einheit zurücksetzen
		//				changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 0);
		//				
		//				//Einheit über Motionpath bewegen
		//				moveUnitAlongMotionPath(selectedUnit, x, y, shortestPath, 0.5f);
		//				activatedUnit = true;
		//				currentState = STATES.MOVING;
		//				
		//				//Verbleibende Bewegungspunkte entfernen, da seit Serverversion 7.4 nur noch eine Bewegung pro Zug möglich ist
		//				selectedUnit.movement_curr = 0;
		//				
		//				//Follow-Funktion anderer Einheiten behandeln
		//				for(int i = 0; i < playingField.units.size(); i++){
		//					if(playingField.units.get(i).order == ORDERS.FOLLOW && playingField.units.get(i).orderUnitID == selectedUnit.ID){
		//						for(int j = 0; j < shortestPath.size(); j++){
		//							playingField.units.get(i).followBuffer.add(shortestPath.get(j));
		//						}
		//					}
		//				}
		//				
		//				//Alle Hexfeder wieder auf Standardfarben setzen
		//				setHexagonBaseColorAll(2);
		//				setHexagonBorderColorAll(0);
		//				
		//				//Sound abspielen
		//				audio.playSound(SOUND.PLAYINGFIELD_UNITMOVING, null);
		//				
		//				//Statistiken updaten
		//				statistics.unitMoved(selectedUnit, playerName, shortestPath.size());
		//				
		//				logger.info("Game.move: Bewegung erfolgreich. Verbleibende Bewegungspunkte: " + String.valueOf(selectedUnit.movement_curr));
		//			}
		//		} catch (Exception e) {
		//			logger.error("Game.move: Fehler bei der Server-Kommunikation.");
		//		}
	}



	/**
	 * Überprüft, ob die Voraussetzungen für einen Handel gegeben sind und führt diesen anschließend aus.
	 * 
	 * @param unitID_Offer UnitID der anhandelnden Einheit.
	 * @param unitID_Receiver UnitID der angehandelten Einheit.
	 * @param offer Liste an angebotenen Waren.
	 * @param receive Liste an geforderten Waren.
	 * @param message Die beim Handel mitgegebene Nachricht.
	 * 
	 * @author Peter Dörr
	 * @since 12.12.12
	 */
	private void trade(int unitID_Offer, int unitID_Receiver, ArrayList<Integer> offer, ArrayList<Integer> receive, String message){

		logger.info("Game.trade: Einheit " + String.valueOf(unitID_Offer) + " handelt Einheit " + String.valueOf(unitID_Receiver) + " an.");

		//Abbrechen, wenn die anbietende Einheit nicht die aktive Einheit ist
		if(getUnitByUnitID(unitID_Offer) != selectedUnit){
			return;
		}

		//Abbrechen, wenn die anbietende Einheit bereits gehandelt hat
		if(getUnitByUnitID(unitID_Offer).doneThisRound){
			return;
		}

		//Abbrechen, wenn die handelnde und angehandelte Einheit zu weit voneinander entfernt sind
		if(pathFinding.getDistance(new Point(getUnitByUnitID(unitID_Offer).x, getUnitByUnitID(unitID_Offer).y), new Point(getUnitByUnitID(unitID_Receiver).x, getUnitByUnitID(unitID_Receiver).y)) > 1){
			return;
		}

		//Befehl an den Server senden
		networkThread.sendIComObject(new ComObject_trade(clientID, clientKey, playerID, gameID, unitID_Offer, unitID_Receiver, offer, receive, message));
	}



	/**
	 * Überprüft, ob ein eingehendes Handelsangebot vorliegt und öffnet das Handelsfenster.
	 * 
	 * @param gameInfo Das GameInfo-ComObject, in welchem der aktuelle Tradestatus gespeichert ist.
	 * 
	 * @author Peter Dörr
	 * @since 12.12.12
	 */
	public void checkTradeRequests(ComObject_gameinfo gameInfo){
		if(gameInfo.getTradePartner().equals(playerName)){
			currentState = STATES.TRADE_ENEMY_WANTS_TRADE;

			gameScreen.openGetTradeOfferPopup(gameInfo.getTradeGive(), gameInfo.getTradeGet());
		}
	}



	/**
	 * Überprüft, nach einem ausgehenden Handelsangebot, welche Antwort auf das Handelsangebot gegeben wird.
	 * 
	 * @param gameInfo Das GameInfo-ComObject, in welchem der aktuelle Tradestatus gespeichert ist.
	 * 
	 * @author Peter Dörr
	 * @since 12.12.12
	 */
	public void checkTradeOfferResponse(ComObject_gameinfo gameInfo){

		//Handel wurde angenommen, Fracht der Einheit aktualisieren und Zug beenden
		if(gameInfo.getTradeStatusLast() == IComObject.TRADESTATUSLAST.ACCEPTED){
			//			try{
			//				ComObject_unitinfo unitinfo = (ComObject_unitinfo)network.establishCommunication(new ComObject_unitinfo(clientID, clientKey, playerID, gameID, selectedUnit.ID));
			//				selectedUnit.cargo_1 = unitinfo.getCargo().get(0);
			//				selectedUnit.cargo_2 = unitinfo.getCargo().get(1);
			//				selectedUnit.cargo_3 = unitinfo.getCargo().get(2);
			//				selectedUnit.cargo_4 = unitinfo.getCargo().get(3);
			//				selectedUnit.cargo_5 = unitinfo.getCargo().get(4);
			//			} catch (Exception e){
			//				logger.error("Game.checkTradeOfferResponse: " + e.getMessage() + " | Fehler bei der Server-Kommunikation.");
			//			}

			//Statistiken updaten





			statistics.unitTraded(selectedUnit, playerName);
			gameScreen.showEventPanel("Trade accepted!", 1.0f, 1.0f, 1.0f, 1.0f);
			endTurn();
		}

		//Handel wurde abgelehnt, Status des Spiels zurücksetzen
		if(gameInfo.getTradeStatusLast() == IComObject.TRADESTATUSLAST.REJECTED){

			gameScreen.showEventPanel("Trade rejected!", 1.0f, 1.0f, 1.0f, 1.0f);
			currentState = STATES.READY;
		}


		//Handel wurde grundsätzlich abgelehnt, Status des Spiels zurücksetzen
		if(gameInfo.getTradeStatusLast() == IComObject.TRADESTATUSLAST.DND){

			gameScreen.showEventPanel("Tradepartner is DND!", 1.0f, 1.0f, 1.0f, 1.0f);
			currentState = STATES.READY;
		}
	}



	/**
	 * Behandelt den Angriff einer Einheit. Hierzu wird der entsprechende Befehl an den Server gesendet, der Schaden von den Lebenspunkten der veteidigenden Einheit
	 * abgezogen und evtl. die verteidigende Einheit aus dem Spiel entfernt, wenn sie keine Lebenspunkte mehr besitzt. Gibt den verursachten Schaden zurück, falls
	 * dieser noch zu Visualisierungszwecken benötigt wird. Nach dem Angriff sollte direkt die EndTurn-Methode aufgerufen werden.
	 * 
	 * @param unitID_Attacker UnitID des Angreifers.
	 * @param unitID_Defender UnitID des Verteidigers.
	 * @return Schaden, der durch den Angriff verursacht wurde, oder -1, falls der Angriff nicht erfolgreich war oder es einen Fehler in der Client-Server Kommunikation gab.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	private int attack(int unitID_Attacker, int unitID_Defender){

		logger.info("Game.attack: Einheit " + String.valueOf(unitID_Attacker) + " greift Einheit " + String.valueOf(unitID_Defender) + " an.");	

		//Abbrechen, wenn die angreifende Einheit nicht die aktive Einheit ist
		if(getUnitByUnitID(unitID_Attacker) != selectedUnit){
			return -1;
		}

		//Abbrechen, wenn die angreifende Einheit bereits angegriffen hat
		if(getUnitByUnitID(unitID_Attacker).doneThisRound){
			return -1;
		}

		//Abbrechen, wenn die angreifende und verteidigende Einheit zu weit voneinander entfernt sind
		if(pathFinding.getDistance(new Point(getUnitByUnitID(unitID_Attacker).x, getUnitByUnitID(unitID_Attacker).y), new Point(getUnitByUnitID(unitID_Defender).x, getUnitByUnitID(unitID_Defender).y)) > 1){
			return -1;
		}

		//Abbrechen, wenn die angegriffene Einheit eine Handelsstation ist oder der Angreifer selbst eine Handelsstation ist
		if(getUnitByUnitID(unitID_Attacker).type == TYPE.Tradestation || getUnitByUnitID(unitID_Defender).type == TYPE.Tradestation){
			gameScreen.showEventPanel("You can't attack Tradestations!", 1.0f, 1.0f, 1.0f, 1.0f);
			return -1;
		}

		this.unitID_Attacker = unitID_Attacker;
		this.unitID_Defender = unitID_Defender;
		networkThread.sendIComObject(new ComObject_attack(clientID, clientKey, playerID, gameID, unitID_Attacker, unitID_Defender));

		//		//Attack-ComObject erstellen
		//		ComObject_attack attack = null;
		//		
		//		try{
		//			//Befehl an den Server senden
		//			attack = (ComObject_attack)network.establishCommunication(new ComObject_attack(clientID, clientKey, playerID, gameID, unitID_Attacker, unitID_Defender));
		//			
		//			if(attack.getStatus() == STATUS.OK)
		//			{	
		//				//Angriff erfolgreich, verteidigender Einheit Schaden zufügen
		//				getUnitByUnitID(unitID_Attacker).doneThisRound = true;
		//				
		//				//Angriffsanimation abspielen
		//				showFightAnimation(getUnitByUnitID(unitID_Attacker).model.getLocalTranslation(), getUnitByUnitID(unitID_Defender).model.getLocalTranslation());
		//				
		//				//Angriffssound ausgeben
		//				audio.playSound(SOUND.PLAYINGFIELD_UNITATTACKING, null);
		//				
		//				//Überprüfen, ob die verteidigende Einheit tot ist, wenn ja, entferne diese aus dem Spiel
		//				ComObject_unitinfo unitinfo = (ComObject_unitinfo)network.establishCommunication(new ComObject_unitinfo(clientID, clientKey, playerID, gameID, unitID_Defender));
		//				if(unitinfo.getDestroyed()){
		//					
		//					//Model aus dem Node_Units_Model-Knoten entfernen
		//					Node node = (Node)(((Node)rootNode.getChild("Node_Unit")).getChild("Node_Units_Model"));
		//					node.detachChild(getUnitByUnitID(unitID_Defender).model);
		//					
		//					//Einheit aus dem units-Array entfernen und damit endgültig löschen
		//					playingField.units.remove(getUnitByUnitID(unitID_Defender));
		//					
		//					//Sterbesound ausgeben
		//					audio.playSound(SOUND.PLAYINGFIELD_UNITDIEING, null);
		//					
		//					//Statistiken updaten
		//					statistics.unitKilledAnUnit(selectedUnit, playerName);
		//				}
		//				
		//				//Schaden ausgeben
		//				gameScreen.showEventPanel("Damage dealt: " + attack.getDamage(), 1.0f, 1.0f, 1.0f, 1.0f);
		//				
		//				logger.info("Game.attack: Angriff erfolgreich. Verursachter Schaden: " + String.valueOf(attack.getDamage()));
		//				return attack.getDamage();
		//				
		//			} else {
		//				//Angriff konnte laut Server-Antwort nicht durchgeführt werden
		//				return -1;
		//			}
		//		} catch (Exception e){
		//			logger.error("Game.attack: " + e.getMessage() + " | Fehler bei der Server-Kommunikation.");
		//		}

		//Wird nur ausgeführt, wenn ein Fehler bei der Kommunikation auftrat
		return 0;
	}



	/**
	 * Versucht, den aktuellen Zug zu beenden.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	private void endTurn(){

		logger.info("Game.endTurn: Beende den aktuellen Zug.");

		if(selectedUnit != null){
			networkThread.sendIComObject(new ComObject_endTurn(clientID, clientKey, playerID, gameID, selectedUnit.ID));
		}


		//		logger.info("Game.endTurn: Beende den aktuellen Zug.");	
		//		
		//		//EndTurn-ComObject erstellen
		//		ComObject_endTurn endTurn = null;
		//		
		//		try{
		//			//Befehl an den Server senden
		//			endTurn = (ComObject_endTurn)network.establishCommunication(new ComObject_endTurn(clientID, clientKey, playerID, gameID,selectedUnit.ID));
		//			
		//			if(endTurn.getStatus() == STATUS.OK){
		//				//Aktive Einheit zurücksetzen
		//				selectedUnit.doneThisRound = true;
		//				selectedUnit = null;
		//				selectedFieldPoint = null;
		//				activatedUnit = false;
		//				
		//				//States setzen
		//				if (endTurn.getActivePlayerID().equals(playerID)){
		//					currentTurn = TURN.PLAYER;
		//					currentState = STATES.READY;
		//				} else {
		//					currentTurn = TURN.ENEMY;
		//					currentState = STATES.ENEMY_TURN;
		//				}
		//				
		//				//Falls eine neue Runde beginnt, alle Einheiten wieder verfügbar machen und Bewegungspunkte zurücksetzen
		//				if(endTurn.getRoundNo() > RoundNumber){
		//					for(int i = 0; i < playingField.units.size(); i++)
		//					{
		//						playingField.units.get(i).doneThisRound = false;
		//						playingField.units.get(i).movement_curr = playingField.units.get(i).movement_max;
		//					}				
		//					RoundNumber++;
		//				}
		//				
		//				//Alle Hexfeder wieder auf Standardfarben setzen
		//				setHexagonBaseColorAll(2);
		//				setHexagonBorderColorAll(0);
		//				
		//				//Beenden des Zuges erfolgreich
		//				logger.info("Game.endTurn: Zug erfolgreich behandelt. Nächster Spieler: " + endTurn.getActivePlayerID());
		//				return;
		//				
		//			} else {
		//				//Das zugende konnte laut Server-Antwort nicht durchgeführt werden
		//				return;
		//			}
		//			
		//			//Falls neue Runde beginnt,
		//		} catch (Exception e){
		//			logger.error("Game.endTurn: " + e.getMessage() + " | Fehler bei der Server-Kommunikation.");
		//		}
		//		
		//		//Ein Fehler ist aufgetreten
		//		return;
	}



	/**
	 * Ruft mittels des gameInfo-Befehls alle wichtigen Informationen vom Server ab und verarbeitet diese.
	 * 
	 * @param gameInfo Ein ComObject_gameinfo-Objekt mit Spielinformationen.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	public void checkTurn(ComObject_gameinfo gameInfo){
		try {
			//Überprüfen, ob der Spieler wieder am Zug ist
			if(gameInfo.getActivePlayerID().equals(playerID)){
				currentTurn = TURN.PLAYER;
				currentState = STATES.READY;
			} else {
				currentTurn = TURN.ENEMY;
				currentState = STATES.ENEMY_TURN;
			}

			//Überprüfen, ob eine Runde beendet wurde
			if(gameInfo.getTurn() > RoundNumber){
				for(int i = 0; i < playingField.units.size(); i++)
				{
					if(playingField.units.get(i).type != TYPE.Tradestation){
						playingField.units.get(i).doneThisRound = false;
						playingField.units.get(i).movement_curr = playingField.units.get(i).movement_max;
					}
				}
				RoundNumber++;
			}
		} catch (Exception e) {
			logger.error("Game.gameInfo: Fehler bei der Server-Kommunikation.");
		}
	}



	/**
	 * Ruft die UnitInfo zu allen im Spiel befindlichen Einheiten ab.
	 * 
	 * @author Peter Dörr
	 * @since 13.12.12
	 */
	public void refreshUnits(){
		for(int i = 0; i < playingField.units.size(); i++){
			if(playingField.units.get(i).type != TYPE.Tradestation){
				networkThread.sendIComObject(new ComObject_unitinfo(clientID, clientKey, playerID, gameID, playingField.units.get(i).ID));
			}
		}
	}



	/**
	 * Gibt zu einer gegebenen UnitID das entspechende Unit-Objekt aus, oder null, wenn zu dieser UnitID keine Unit existiert.
	 * 
	 * @param unitID UnitID der Einheit, die gesucht wird.
	 * @return Die Einheit als Unit-Objekt oder null, wenn die Unit nicht existiert.
	 * 
	 * @author Peter Dörr
	 * @since 05.12.12
	 */
	public Unit getUnitByUnitID(int unitID){
		for(int i = 0; i < playingField.units.size(); i++){
			if(playingField.units.get(i).ID == unitID){
				return playingField.units.get(i);
			}
		}
		return null;
	}



	/**
	 * Sucht die Einheit bei den gegebenen Koordinaten und gibt diese zurück, falls sie existiert.
	 * 
	 * @param x X-Koordinate der gesuchten Einheit.
	 * @param y Y-Koordinate der gesuchten Einheit.
	 * @return Die Einheit oder null, wenn keine Einheit bei diesen Koordinaten existiert.
	 * 
	 * @author Peter Dörr
	 * @since 08.12.12
	 */
	public Unit getUnitByCoordinates(int x, int y){
		for(int i = 0; i < playingField.units.size(); i++){
			if(playingField.units.get(i).x == x && playingField.units.get(i).y == y){
				return playingField.units.get(i);
			}
		}
		return null;
	}



	/**
	 * Überprüft, ob eine Einheit von anderen Einheiten direkt umgeben ist und gibt diese zurück.
	 * 
	 * @param unit Die Einheit, deren Umgebung untersucht werden soll.
	 * @return Einheiten, die an die gegebene Einheit direkt angrenzen oder null, falls keine entsprechenden Einheiten existieren.
	 * 
	 * @author Peter Dörr
	 * @since 08.12.12
	 */
	public ArrayList<Unit> getUnitSurroundingUnits(Unit unit){
		ArrayList<Unit> surroundingUnits = new ArrayList<Unit>();
		for(int i = 0; i < playingField.units.size(); i++){			
			if(pathFinding.getDistance(new Point(unit.x, unit.y), new Point(playingField.units.get(i).x, playingField.units.get(i).y)) == 1){
				surroundingUnits.add(playingField.units.get(i));
			}
		}

		//Falls umgebende Einheiten gefunden wurden, werden diese zurückgegeben, ansonsten null
		if(surroundingUnits.size() > 0){
			return surroundingUnits;
		} else {
			return null;
		}
	}
	
	
	
	/**
	 * Gibt die Anzahl der noch im Spiel vorhandenen Einheiten zurück.
	 * 
	 * @return Anzahl der Einheiten.
	 * 
	 * @author Peter Dörr
	 * @since 22.02.13
	 */
	public int getUnitCount(){
		return playingField.units.size();
	}



	/**
	 * Diese Methode initialisiert die Tastenbelegung der Steuerung<br><br>
	 * 
	 * Programmcode basiert auf dem jME Tutorial HelloInput<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 20.11.2012<br><br>
	 * 
	 * 28.11.2012 (Markus Strobel) Wegpunkte Steuerung ergänzt.<br>
	 * 
	 * 28.11.2012 (Markus Strobel) Kamerasteuerung angepasst bzw zugeschnitten.<br>
	 */
	private void initKeys()
	{
		// Kamera-Steuerung
		inputManager.addMapping("camLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("camRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("camUp", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("camDown", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("camDrag", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

		inputManager.addMapping("DEBUG_BUTTON", new KeyTrigger(KeyInput.KEY_K));

		// Zoom
		inputManager.addMapping("zoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("zoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

		// Hilfe
		inputManager.addMapping("openHelp", new KeyTrigger(KeyInput.KEY_F1));

		// Shift für z.B. Waypoints setzen
		inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_RSHIFT));



		// Maus		
		// Spiel-Steuerung
		inputManager.addMapping("mouseLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("mouseRight", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		// Endturn -> ENTER
		inputManager.addMapping("return", new KeyTrigger(KeyInput.KEY_RETURN));

		// Follow- oder Intercept Befehl weiter ausführen
		inputManager.addMapping("space", new KeyTrigger(KeyInput.KEY_SPACE));

		//Wegpunkte löschen (den letzten und alle)
		inputManager.addMapping("deleteLastWaypoint", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("deleteAllWaypoints", new KeyTrigger(KeyInput.KEY_Q));

		// Game GUI Steuerungen
		inputManager.addMapping("toggleHealthBars", new KeyTrigger(KeyInput.KEY_F2));
		inputManager.addMapping("debug", new KeyTrigger(KeyInput.KEY_F3));
		inputManager.addMapping("toggleFOG", new KeyTrigger(KeyInput.KEY_F4));

		// Remove der STATS Visualisierung mit F5 und remappen auf toggleAutofokus
		inputManager.deleteMapping(INPUT_MAPPING_HIDE_STATS);
		inputManager.addMapping("toggleAutofokus", new KeyTrigger(KeyInput.KEY_F5));

		// Menu
		inputManager.deleteMapping(INPUT_MAPPING_EXIT); // Escape Taste vom ursprünglichen Mapping lösen
		inputManager.addMapping("gameMenu", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("gameMenu", new KeyTrigger(KeyInput.KEY_F10));

		//Gewinnbedingungen
		inputManager.addMapping("winConditions", new KeyTrigger(KeyInput.KEY_F6));

		//Minimap-Modi wechseln
		inputManager.addMapping("minimap", new KeyTrigger(KeyInput.KEY_F7));

		// Mappings zum Listener hinzufügen
		inputManager.addListener(actionListener, new String[]{"mouseLeft", "mouseRight", "openHelp", "shift", "zoomIn", "zoomOut", "return", "space", "deleteLastWaypoint", "deleteAllWaypoints", "toggleHealthBars", "debug", "toggleFOG", "toggleAutofokus", "gameMenu", "winConditions", "minimap", "camDrag"});  	
		inputManager.addListener(analogListener, new String[]{"camLeft", "camRight", "camUp", "camDown", "camDrag"});
	}



	/**
	 * Diese Methode stellt den ActionListener dar, welche auf Tastatur- und Mauseingaben reagiert.<br><br>
	 * 
	 * Programmcode basiert auf dem jME Tutorial HelloInput<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 20.11.2012<br><br>
	 */
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {


			// Lässt im gameScreen das MenuPopup erscheinen bzw verschwinden.
			if(name.equals("winConditions") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
			{
				gameScreen.toggleWinConditions();
			}

			// Lässt im gameScreen das MenuPopup erscheinen bzw verschwinden.
			if(name.equals("gameMenu") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
			{
				gameScreen.toggleMenu();
			}


			// Chat Eingabe absenden im SetupScreen
			if(name.equals("return") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("gameSetupScreen"))
			{
				// Überprüft ob das Chat-Fenster des GAME SETUP SCREENS im FOCUS ist
				if(setupScreen.chatSendTextFieldHasFocus())
				{
					setupScreen.sendChatMessage();
				}
			}	

			// Chat Eingabe absenden im WaitForPlayersScreen
			if(name.equals("return") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("waitForPlayersScreen"))
			{
				// Überprüft ob das Chat-Fenster des waitForPlayersScreens im FOCUS ist
				if(waitForPlayersScreen.chatSendTextFieldHasFocus())
				{
					waitForPlayersScreen.sendChatMessage();
				}
			}	

			//			// Chat Eingabe absenden im gameScreen
			//			if(name.equals("return") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
			//			{
			//				// Überprüft ob das Chat-Fenster des gameScreens im FOCUS ist
			//				if(gameScreen.chatSendTextFieldHasFocus())
			//				{
			//					gameScreen.sendChatMessage();
			//				}
			//			}	


			/*
			 * DEBUG CONTROLS
			 * 
			 */
			// Lässt im gameScreen das TradeFightPopup erscheinen bzw verschwinden. -> NUR DEBUG
			if(name.equals("debug") && !keyPressed && nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
			{
				gameScreen.toggleDebugWindow();
			}

			if(gameStarted)
			{

				// Kamera-Steuerung (ZoomIn)
				if(name.equals("zoomIn"))
				{
					//Kamera zu Nah, Zoom abbrechen
					if(cam.getLocation().y < 20){
						return;
					}

					cam.setLocation(cam.getLocation().add(new Vector3f(0.0f, -10.0f, 0.0f)));
					cam.lookAt(new Vector3f(cam.getLocation().x, 0.0f, cam.getLocation().z - 20.0f), new Vector3f(0.0f, 1.0f, 0.0f));
				}

				// Kamera-Steuerung (ZoomOut)
				if(name.equals("zoomOut"))
				{
					//Kamera zu weit weg, Zoom abbrechen
					if(cam.getLocation().y > 100){
						return;
					}

					cam.setLocation(cam.getLocation().add(new Vector3f(0.0f, 10.0f, 0.0f)));
					cam.lookAt(new Vector3f(cam.getLocation().x, 0.0f, cam.getLocation().z - 20.0f), new Vector3f(0.0f, 1.0f, 0.0f));
				}

				//Kamera-Drag resetten
				if(name.equals("camDrag")){
					mousePositionLast = null;
				}

				//Healthbars ein-/ausblenden
				if(name.equals("toggleHealthBars") && !keyPressed)
				{
					//Healthbars-Flag setzen
					handleHealthBars = !handleHealthBars;

					//Vorhandene Healthbars löschen
					for(int i = 0; i < playingField.units.size(); i++){
						playingField.units.get(i).showHealtBar = handleHealthBars;
						playingField.units.get(i).loadHealthbar(playingField.units.get(i).hitpoints_curr, playingField.units.get(i).hitpoints_max);
					}
				}

				//Fog of War umschalten
				if(name.equals("toggleFOG") && !keyPressed)
				{
					switch(visibilityMode){
					case OFF:
						for(int x = 0; x < playingField.width; x++){
							for(int y = 0; y < playingField.height; y++){
								playingField.visibility[x][y] = false;
							}
						}
						visibilityMode = FOG.STATIC;
						Minimap.showFOW = true;
						break;
					case STATIC:
						visibilityMode = FOG.DYNAMIC;
						Minimap.showFOW = true;
						break;
					case DYNAMIC:
						visibilityMode = FOG.OFF;
						Minimap.showFOW = false;
						break;
					}
					calculateVisibility(visibilityMode);
					if(selectedUnit != null){
						setHexagonColorByMovementRange(new Point(selectedUnit.x, selectedUnit.y), selectedUnit.movement_curr);
					} else {
						setHexagonBaseColorAll(2);
						setHexagonBorderColorAll(0);
					}

					gameScreen.showEventPanel("Fog of War: " + visibilityMode.toString(), 1.0f, 1.0f, 1.0f, 1.0f);
				}

				//Minimap-Modi umschalten
				if(name.equals("minimap") && !keyPressed)
				{
					switch(Minimap.mode){
					case TERRAIN:
						Minimap.mode = MINIMAPMODE.UNITS;
						break;
					case UNITS:
						Minimap.mode = MINIMAPMODE.TERRAIN_UNITS;
						break;
					case TERRAIN_UNITS:
						Minimap.mode = MINIMAPMODE.TERRAIN;
						break;
					case TERRAIN_UNITS_MOVEMENTRANGE:
						Minimap.mode = MINIMAPMODE.TERRAIN_UNITS_MOVEMENTRANGE;
						break;
					}
					gameScreen.showEventPanel("Minimap: " + Minimap.mode.toString(), 1.0f, 1.0f, 1.0f, 1.0f);
				}

				//Autofokus an-/ausschalten
				if(name.equals("toggleAutofokus") && !keyPressed)
				{
					autofocus_mode = !autofocus_mode;
					gameScreen.showEventPanel("Autofokus: " + String.valueOf(autofocus_mode), 1.0f, 1.0f, 1.0f, 1.0f);
				}

				/*
				 * PLAYER TURN
				 */
				if(currentTurn.equals(TURN.PLAYER) && currentState == STATES.READY) // Steuerung nur möglich, wenn man an der Reihe ist
				{

					if(name.equals("return") && !keyPressed)
					{
						//Beendet die Runde mit der ausgewählten Einheit
						if(selectedUnit != null)
						{
							//							// Überprüft ob das Chat-Fenster des gameScreens im FOCUS ist
							//							if(!gameScreen.chatSendTextFieldHasFocus())
							//							{
							endTurn();
							//							}			

						}
					}

					if(name.equals("space") && !keyPressed)
					{
						//Führt einen Follow- oder Intercept-Befehl weiter aus
						if(selectedUnit != null)
						{
							if(selectedUnit.order == ORDERS.FOLLOW){
								//Verfolgte Einheit existiert nicht mehr
								if(getUnitByUnitID(selectedUnit.orderUnitID) == null){
									selectedUnit.order = ORDERS.NONE;
									selectedUnit.followBuffer.clear();
									return;
								}

								//Einheit zu Nah für Follow
								if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(getUnitByUnitID(selectedUnit.orderUnitID).x, getUnitByUnitID(selectedUnit.orderUnitID).y)) <= 1){
									return;
								}

								//Wegpunkte löschen, diese werden nicht benötigt
								selectedUnit.wayPointsList.clear();

								//Einheit bewegen
								move(selectedUnit.ID, -1, -1);
								if(getUnitSurroundingUnits(selectedUnit) == null){
									actionAfterMovement = STATES.ENEMY_TURN;
								} else {
									actionAfterMovement = null;
								}

							}

							if(selectedUnit.order == ORDERS.INTERCEPT){
								//Verfolgte Einheit existiert nicht mehr
								if(getUnitByUnitID(selectedUnit.orderUnitID) == null){
									selectedUnit.order = ORDERS.NONE;
									return;
								}

								//Einheit zu Nah für Intercept
								if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(getUnitByUnitID(selectedUnit.orderUnitID).x, getUnitByUnitID(selectedUnit.orderUnitID).y)) == 1){
									return;
								}

								//Wegpunkte löschen, diese werden nicht benötigt
								selectedUnit.wayPointsList.clear();

								//Einheit zur Zieleinheit bewegen.
								//Befindet sich nach der Bewegung auf einem angrenzenden Feld eine Einheit, kann potentiell
								//noch ein Handel oder Kampf durchgeführt werden. Ansonsten wird der Zug automatisch beeendet.
								activatedUnit = true;
								move(selectedUnit.ID, getUnitByUnitID(selectedUnit.orderUnitID).x, getUnitByUnitID(selectedUnit.orderUnitID).y);
								if(getUnitSurroundingUnits(selectedUnit) == null){
									actionAfterMovement = STATES.ENEMY_TURN;
								} else {
									actionAfterMovement = null;
								}
							}
						}
					}

					if(keyPressed)
					{
						pressedKeys.add(name);
					}
					else
					{
						pressedKeys.remove(name);
					}


					/*
					 * LINKE MAUSTASTE
					 */
					// Wählt das derzeitige Hex-Feld aus und wählt eine Einheit aus, sofern sie sich darauf befindet.
					if(name.equals("mouseLeft") && !keyPressed && (!pressedKeys.contains("shift") && currentTurn.equals(TURN.PLAYER)))
					{

						if(nifty.getCurrentScreen().getScreenId().equals("gameScreen"))
						{
							if(gameScreen.chatSendTextFieldHasFocus())
							{
								gameScreen.removeFocusFromTextField();
							}
						}

						//			        	//Keinen Klick annehmen, wenn das TradeFightMenu geöffnet ist
						//			        	if(ignoreNextLeftClick){
						//			        		ignoreNextLeftClick = false;
						//			        		return;
						//			        	}

						//Kein Klick auf ein freies Feld, wenn es bereits eine aktive Einheit gibt
						if(activatedUnit){
							return;
						}


						Point pos = getSelectedPoint();
						if(pos != null)
						{
							if(selectedFieldPoint != null)
							{

								// Alte Wegpunktliste löschen
								if(WayPointList.size() > 0)
								{
									for(int j = 0; j < WayPointList.size(); j++)
									{
										changeHexagonBorderColor(new Point(WayPointList.get(j).x, WayPointList.get(j).y), 0);
									}
									WayPointList.clear();
								}


								if(pos == selectedFieldPoint)
								{ 
									changeHexagonBorderColor(pos, 0);
								} 
								else 
								{
									changeHexagonBorderColor(selectedFieldPoint, 0);
									selectedFieldPoint = pos;
									changeHexagonBorderColor(selectedFieldPoint, 9);
									logger.info("Game.actionListener.onAction: selected Field: (" + selectedFieldPoint.x + "," + selectedFieldPoint.y + ")");
								}
							} 
							else 
							{
								selectedFieldPoint = pos;
								changeHexagonBorderColor(selectedFieldPoint, 9);
								logger.info("Game.actionListener.onAction: selected Field: (" + selectedFieldPoint.x + "," + selectedFieldPoint.y + ")");
							}							


							// Wählt die Einheit aus, wenn sich die Maus über einem Hexfeld befindet auf der eine Einheit steht, sofern sie dem Spieler gehört.
							if(name.equals("mouseLeft") && !activatedUnit && currentState.equals(STATES.READY) && !keyPressed)
							{
								boolean found = false;								

								for(int i = 0; i < playingField.units.size(); i++)
								{	

									// Wenn die Koordinaten gleich sind, dann wird diese Einheit ausgewählt.
									if(playingField.units.get(i).x == pos.x && playingField.units.get(i).y == pos.y)
									{				
										// Alte Wegpunktliste löschen
										if(WayPointList.size() > 0)
										{
											for(int j = 0; j < WayPointList.size(); j++)
											{
												changeHexagonBorderColor(new Point(WayPointList.get(j).x, WayPointList.get(j).y), 0);
											}
											WayPointList.clear();
										}

										// freundliche Einheit auswählen
										if(playingField.units.get(i).ownerID != null){
											if(playingField.units.get(i).ownerID.equals(playerID))
											{
												if(playingField.units.get(i).doneThisRound == false)
												{
													audio.playSound(SOUND.PLAYINGFIELD_UNITSELECTED, null);

													selectedUnit = playingField.units.get(i);
													changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 1);
													setHexagonColorByMovementRange(new Point(selectedUnit.x, selectedUnit.y), selectedUnit.movement_curr);

													logger.info("Game.actionListener.onAction: selectedUnit: (" + selectedUnit.x + "," + selectedUnit.y + ")" + " Unit-ID: " + selectedUnit.ID + " owner ID: " + selectedUnit.ownerID);
													found = true;
												}
											}
											else // feindliche Einheit auswählen
											{
												setHexagonBaseColorAll(2);
												setHexagonBorderColorAll(0);
												selectedUnit = playingField.units.get(i);
												changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 3);

												logger.info("Game.actionListener.onAction: selectedUnit: (" + selectedUnit.x + "," + selectedUnit.y + ")" + " Unit-ID: " + selectedUnit.ID + " owner ID: " + selectedUnit.ownerID);
												found = true;
											}
										}
										else //Handelsstation ausgewählt
										{
											setHexagonBaseColorAll(2);
											setHexagonBorderColorAll(0);
											selectedUnit = playingField.units.get(i);
											changeHexagonBorderColor(new Point(selectedUnit.x, selectedUnit.y), 3);

											logger.info("Game.actionListener.onAction: selectedUnit: (" + selectedUnit.x + "," + selectedUnit.y + ")" + " Unit-ID: " + selectedUnit.ID + " owner ID: No Owner -> Tradestation");
											found = true;
										}
									}
								}

								if(!found)
								{
									// zurück färben
									selectedUnit = null;
									setHexagonBaseColorAll(2);
									setHexagonBorderColorAll(0);
									if(selectedFieldPoint != null)
									{
										changeHexagonBorderColor(selectedFieldPoint, 9);										
									}
									logger.info("Game.actionListener.onAction: selected Unit: keine");
								}										

							}
						}
						else
						{
							selectedFieldPoint = null;
							selectedUnit = null;
							setHexagonBaseColorAll(2);
							setHexagonBorderColorAll(0);
						}
					}


					/*
					 * RECHTE MAUSTASTE
					 */
					// Rechte Maustaste abwählen eines Hex-Feldes bzw bewegen einer Einheit zum Zielpunkt, falls eine angewählt ist
					if(name.equals("mouseRight") && !keyPressed && (!pressedKeys.contains("shift")) && currentTurn.equals(TURN.PLAYER))
					{
						// Bewegungs-Befehl geben
						if(name.equals("mouseRight") && currentState.equals(STATES.READY) && selectedUnit != null && !keyPressed)
						{
							if(selectedUnit.ownerID != null)
							{
								if(selectedUnit.ownerID.equals(playerID)){
									Point pos = getSelectedPoint();
									if(pos != null)
									{
										audio.playSound(SOUND.PLAYINGFIELD_UNITSELECTED, null);

										//Ist eine Eineit auf dem Zielfeld, öffne das FightTradeFollowIntercept-Popup. Ansonsten Bewege die Einheit zum Zielfeld
										if(!selectedUnit.doneThisRound){
											if(selectedUnit != getUnitByCoordinates(pos.x, pos.y) && getUnitByCoordinates(pos.x, pos.y) != null){
												tmp_x = pos.x;
												tmp_y = pos.y;
												gameScreen.toggleTradeFightMenu();
											} else {
												//Ist das Zielfeld leer, wird die Einheit dorthin bewegt. Befindet sich auf einem angrenzenden Feld eine Einheit, kann potentiell
												//noch ein Handel oder Kampf durchgeführt werden. Ansonsten wird der Zug automatisch beeendet.

												move(selectedUnit.ID, pos.x, pos.y);
												selectedUnit.order = ORDERS.NONE;

												//											if(getUnitSurroundingUnits(selectedUnit) == null){
												//												actionAfterMovement = STATES.ENEMY_TURN;
												//											} else {
												//												actionAfterMovement = null;
												//											}
											}
										}
									}
								}
							}
						}
					}



					//Wegpunkt hinzufügen
					if(wayPointingExec.updateState(pressedKeys, tpf) && selectedUnit != null && currentState.equals(STATES.READY) && currentTurn.equals(TURN.PLAYER))
					{
						Point pos = getSelectedPoint();

						if(pressedKeys.contains("shift") && pressedKeys.contains("mouseLeft"))
						{
							if(pos != null)
							{
								//Kein Wegpunkt hinzufügen, wenn der neuste Wegpunkt identisch mit dem zuletzt hinzugefügten Wegpunkt ist
								if(WayPointList.size() > 0){
									if(pos.x == WayPointList.get(WayPointList.size() - 1).x && pos.y == WayPointList.get(WayPointList.size() - 1).y){
										return;
									}
								}

								//Wegpunkt hinzufügen
								WayPointList.add(pos);
								logger.info("Game.actionListener.onAction: SHIFT + mouseLeft: WayPoint added (" + pos.x +"," + pos.y + ")");
								setHexagonColorByMovementRange(new Point(selectedUnit.x, selectedUnit.y), selectedUnit.movement_curr);
							}						
						}
					}

					//Letzten Wegpunkt löschen
					if(name.equals("deleteLastWaypoint") && !keyPressed && selectedUnit != null && currentState.equals(STATES.READY) && currentTurn.equals(TURN.PLAYER)) {
						if(WayPointList.size() > 0) {
							logger.info("Game.actionListener.onAction: E: WayPoint deleted (" + WayPointList.get(WayPointList.size() - 1).x +"," + WayPointList.get(WayPointList.size() - 1).y + ")");
							WayPointList.remove(WayPointList.size() - 1);
							setHexagonColorByMovementRange(new Point(selectedUnit.x, selectedUnit.y), selectedUnit.movement_curr);
						}
					}

					//Alle Wegpunkte löschen
					if(name.equals("deleteAllWaypoints") && !keyPressed && selectedUnit != null && currentState.equals(STATES.READY) && currentTurn.equals(TURN.PLAYER)) {
						logger.info("Game.actionListener.onAction: Q: All WayPoints deleted!");
						WayPointList.clear();
						setHexagonColorByMovementRange(new Point(selectedUnit.x, selectedUnit.y), selectedUnit.movement_curr);
					}
				}
			}
		}
	};



	/**
	 * Diese Methode stellt den AnalogListener dar, welche auf Tastatur für die Kamera-Steuerung reagiert.<br><br>
	 * 
	 * Programmcode basiert auf dem jME Tutorial HelloInput<br><br>
	 * 
	 * 29.11.2012 (Markus Strobel) Kamerasteuerung angepasst und zugeschnitten.
	 * 
	 * @author Markus Strobel
	 * @since 20.11.2012<br><br>
	 */
	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {

			if(gameStarted)
			{
				// Kamera-Steuerung per Tastatur
				if(name.equals("camRight") && !(autofocus_mode == true && selectedUnit != null))
				{	
					if(cam.getLocation().x < 4 * playingField.width)
					{
						Vector3f vec = new Vector3f(cam.getLocation().x + 30.0f * tpf, cam.getLocation().y, cam.getLocation().z);
						cam.setLocation(vec);
					}
				}
				if(name.equals("camLeft") && !(autofocus_mode == true && selectedUnit != null))
				{					
					if(cam.getLocation().x > -4 * playingField.width)
					{
						Vector3f vec = new Vector3f(cam.getLocation().x - 30.0f * tpf, cam.getLocation().y, cam.getLocation().z);
						cam.setLocation(vec);
					}
				}
				if(name.equals("camUp") && !(autofocus_mode == true && selectedUnit != null))
				{					
					if(cam.getLocation().z > -4 * playingField.height)
					{
						Vector3f vec = new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z - 30.0f * tpf);
						cam.setLocation(vec);
					}
				}
				if(name.equals("camDown") && !(autofocus_mode == true && selectedUnit != null))
				{					
					if(cam.getLocation().z < 4.25 * playingField.height)
					{
						Vector3f vec = new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z + 30.0f * tpf);
						cam.setLocation(vec);
					}
				}

				//Kamerasteuerung per Maus
				if(name.equals("camDrag") && !(autofocus_mode == true && selectedUnit != null)){
					Vector2f positionCurr = inputManager.getCursorPosition();
					Vector2f positionLast = mousePositionLast;

					//Wird beim allerersten Aufruf einmalig ausgeführt
					if(positionLast == null){
						positionLast = positionCurr;
					}

					//Zurückgelegte Mausdistanz berechnen
					Vector2f distance = positionCurr.subtract(positionLast);
					Vector3f newCamLocation = cam.getLocation();
					if(cam.getLocation().x < 4 * playingField.width && distance.x < 0.0f)
					{
						newCamLocation.x -= distance.x * 0.5f;
					}
					if(cam.getLocation().x > -4 * playingField.width && distance.x > 0.0f)
					{
						newCamLocation.x -= distance.x * 0.5f;
					}
					if(cam.getLocation().z > -4 * playingField.height && distance.y < 0.0f){
						newCamLocation.z += distance.y * 0.5f;
					}
					if(cam.getLocation().z < 4 * playingField.height && distance.y > 0.0f){
						newCamLocation.z += distance.y * 0.5f;
					}

					//Neue Kameraposition bestimmen und letzte Mausposition speichern
					cam.setLocation(newCamLocation);
					mousePositionLast = new Vector2f();
					mousePositionLast.x = positionCurr.x;
					mousePositionLast.y = positionCurr.y;
				}
			}
		}
	};



	/**
	 * Diese Methode initialisiert die ComboMoves für die Möglichkeit Wegpunkte zu setzen.<br><br>
	 * 
	 * Programmcode basiert auf: http://code.google.com/p/jmonkeyengine/source/browse/trunk/engine/src/test/jme3test/input/combomoves/TestComboMoves.java<b><br>
	 * 
	 * 
	 * @author Markus Strobel
	 * @since 20.11.2012<br><br>
	 * 
	 */
	private void initComboMoves()
	{
		// ComboMove für SHIFT + Linksklick, damit sollen Wegpunkte gesetzt bzw entfernt werden
		wayPointing = new ComboMove("WayPointing");
		wayPointing.press("shift").press("mouseLeft").done();	
		wayPointing.setUseFinalState(true);
		wayPointingExec = new ComboMoveExecution(wayPointing);

		// ComboMove für SHIFT + Rechtsklick, damit sollen die gesetzten Wegpunkte ausgeführt werden
		wayPointingLock = new ComboMove("WayPointingLock");
		wayPointingLock.press("shift").press("mouseRight").done();	
		wayPointingLock.setUseFinalState(true);
		wayPointingLockExec = new ComboMoveExecution(wayPointingLock);

	}



	/**
	 * Diese Methode wird von der SepsisGameScreenController aufgerufen, wenn einer der Interaktions-Buttons gedrückt wurde.<br>
	 * Also entweder Kampf, Handel oder Abbruch.<br>
	 * 
	 * @param selectedButtonName Der Name des Buttons der gedrückt wurde.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 04.12.2012<br>
	 */
	public void buttonClick_FightTradeFollowIntercept(INTERACTIONS selectedInteraction)
	{

		switch(selectedInteraction){
		case FIGHT:	

			//Heranbewegen an Einheit, falls nötig (dann wird der Angriff in der moveUnitAlongMotionPath-Methode nach Ablauf der Bewegung behandelt), ansonsten direkt angreifen
			if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) > 1){
				activatedUnit = true;
				actionAfterMovement = STATES.FIGHTING;
				move(selectedUnit.ID, tmp_x, tmp_y);
				selectedUnit.order = ORDERS.NONE;
			} else {
				if(attack(selectedUnit.ID, getUnitByCoordinates(tmp_x, tmp_y).ID) != -1){
					activatedUnit = true;
					actionAfterMovement = STATES.READY;
					gameScreen.toggleTradeFightMenu();
					break;
				}
			}

			//			//Zug nur beenden, wenn die Bewegungspunkte nicht ausreichen, um den Gegner zu erreichen, aber nun potentielle neue Ziele neben der Einheit stehen
			//			if(getUnitSurroundingUnits(selectedUnit) == null){
			//				endTurn();
			//			}

			//Popup schließen
			gameScreen.toggleTradeFightMenu();
			break;

		case TRADE:

			//Heranbewegen an Einheit, falls nötig (dann wird der Handel in der moveUnitAlongMotionPath-Methode nach Ablauf der Bewegung behandelt), ansonsten direkt Handelsfenster öffnen
			if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) > 1){
				activatedUnit = true;
				actionAfterMovement = STATES.TRADE_ATTEMPT_TO_TRADE;
				move(selectedUnit.ID, tmp_x, tmp_y);
				selectedUnit.order = ORDERS.NONE;
			} else {
				activatedUnit = true;
				if(getUnitByCoordinates(tmp_x, tmp_y).type == TYPE.Tradestation){
					gameScreen.openTradeStationPopup(getUnitByCoordinates(tmp_x, tmp_y).ID);
				} else {
					gameScreen.openMakeTradeOfferPopup();
				}
				currentState = STATES.TRADE_ATTEMPT_TO_TRADE;
			}

			//Popup schließen
			gameScreen.toggleTradeFightMenu();
			break;

		case FOLLOW:

			//			//Heranbewegen an Einheit, falls nötig
			//			if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) > 1){
			//				move(selectedUnit.ID, tmp_x, tmp_y);
			//				selectedUnit.order = ORDERS.FOLLOW;
			//				selectedUnit.orderUnitID = getUnitByCoordinates(tmp_x, tmp_y).ID;
			//				if(getUnitSurroundingUnits(selectedUnit) == null){
			//					actionAfterMovement = STATES.ENEMY_TURN;
			//				} else {
			//					actionAfterMovement = null;
			//				}
			//			}
			//			
			//			//Zug nur beenden, wenn die Bewegungspunkte nicht ausreichen, um den Gegner zu erreichen, aber nun potentielle neue Ziele neben der Einheit stehen
			//			if(getUnitSurroundingUnits(selectedUnit) == null){
			//				endTurn();
			//			}

			if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) == 1){
				//Alten FollowBuffer löschen und Position der verfolgten Einheit hinzufügen
				activatedUnit = true;
				selectedUnit.followBuffer.clear();
				selectedUnit.followBuffer.add(new Point(tmp_x, tmp_y));
				selectedUnit.order = ORDERS.FOLLOW;
				selectedUnit.orderUnitID = getUnitByCoordinates(tmp_x, tmp_y).ID;
			} else {
				gameScreen.showEventPanel("Follow only works with adjanced units!", 1.0f, 1.0f, 1.0f, 1.0f);
			}


			//Popup schließen
			gameScreen.toggleTradeFightMenu();
			break;

		case INTERCEPT:

			//Heranbewegen an Einheit, falls nötig
			if(pathFinding.getDistance(new Point(selectedUnit.x, selectedUnit.y), new Point(tmp_x, tmp_y)) > 1){
				activatedUnit = true;
				move(selectedUnit.ID, tmp_x, tmp_y);
				selectedUnit.order = ORDERS.INTERCEPT;
				selectedUnit.orderUnitID = getUnitByCoordinates(tmp_x, tmp_y).ID;
				if(getUnitSurroundingUnits(selectedUnit) == null){
					actionAfterMovement = STATES.ENEMY_TURN;
				} else {
					actionAfterMovement = null;
				}
			} else {
				activatedUnit = true;
				selectedUnit.order = ORDERS.INTERCEPT;
				selectedUnit.orderUnitID = getUnitByCoordinates(tmp_x, tmp_y).ID;
			}

			//Zug nur beenden, wenn die Bewegungspunkte nicht ausreichen, um den Gegner zu erreichen, aber nun potentielle neue Ziele neben der Einheit stehen
			if(getUnitSurroundingUnits(selectedUnit) == null){
				endTurn();
			}

			//Popup schließen
			gameScreen.toggleTradeFightMenu();
			break;

		case ABORT:

			//Abbrechen, Popup schließen
			gameScreen.toggleTradeFightMenu();
			break;
		}
	}



	/**
	 * Wird aufgerufen, sobald das Angebots-Handelsfenster geschlossen wird.
	 * 
	 * @param tradeInfo Das TradeInfo-Objekt, in welchem Angebot und Forderung enthalten sind.
	 * 
	 * @author Peter Dörr
	 * @since 08.12.12
	 */
	public void buttonClick_TradeOffer(TradeInfo tradeInfo){
		if(tradeInfo != null)
		{
			trade(selectedUnit.ID, getUnitByCoordinates(tmp_x, tmp_y).ID, tradeInfo.offer, tradeInfo.request, tradeInfo.message);
			//			try{
			//				if(getUnitByCoordinates(tmp_x, tmp_y).ownerID.equals(playerID)){
			//					ComObject_tradeReply tradeReply = (ComObject_tradeReply)network.establishCommunication(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.ACCEPTED));
			//					if(tradeReply.getStatus() == STATUS.ERROR){
			//						tradeReply = (ComObject_tradeReply)network.establishCommunication(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.REJECTED));
			//						currentState = STATES.READY;
			//					} else {
			//						currentState = STATES.READY;
			//						endTurn();
			//					}
			//					
			//				}
			//			} catch (Exception e) {
			//				logger.error("Game.buttonClick_TradeOffer: Fehler bei der Server-Kommunikation.");
			//			}
		} else 
		{
			currentState = STATES.READY;
		}
	}



	/**
	 * Wird aufgerufen, sobald das Reply-Handelsfenster geschlossen wird.
	 * 
	 * @param reply Die Antwort der angehandelten Einheit.
	 * 
	 * @author Peter Dörr
	 * @since 08.12.12
	 */
	public void buttonClick_TradeReply(TRADE_REPLY reply, String tradeMessage){
		switch(reply){
		case ACCEPT:
			networkThread.sendIComObject(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.ACCEPTED, tradeMessage));



			//			try{
			//				ComObject_tradeReply tradeReply = (ComObject_tradeReply)network.establishCommunication(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.ACCEPTED));
			//				if(tradeReply.getStatus() == STATUS.OK){
			//					refreshUnits();
			//					currentState = STATES.ENEMY_TURN;
			//					gameScreen.closeGetTradeOfferPopup();
			//					gameScreen.showEventPanel("Trade accepted!", 1.0f, 1.0f, 1.0f, 1.0f);
			//				} else {
			//					gameScreen.showEventPanel("Trade not possible! Maybe one of the trading units can't carry any more stuff?", 1.0f, 1.0f, 1.0f, 1.0f);
			//				}
			//			} catch (Exception e) {
			//				logger.error("Game.buttonClick_TradeRepeat: Fehler bei der Server-Kommunikation.");
			//			}
			break;

		case REJECT:
			networkThread.sendIComObject(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.REJECTED, tradeMessage));



			//			try{
			//				network.establishCommunication(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.REJECTED));
			//				currentState = STATES.ENEMY_TURN;
			//				gameScreen.closeGetTradeOfferPopup();
			//				gameScreen.showEventPanel("Trade rejected!", 1.0f, 1.0f, 1.0f, 1.0f);
			//			} catch (Exception e) {
			//			}
			break;

		case DND:
			networkThread.sendIComObject(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.DND, tradeMessage));



			//			try{
			//				network.establishCommunication(new ComObject_tradeReply(clientID, clientKey, playerID, gameID, RESPONSE.DND));
			//				currentState = STATES.ENEMY_TURN;
			//				gameScreen.closeGetTradeOfferPopup();
			//				gameScreen.showEventPanel("Unit is DND now!", 1.0f, 1.0f, 1.0f, 1.0f);
			//			} catch (Exception e) {
			//				logger.error("Game.buttonClick_TradeRepeat: Fehler bei der Server-Kommunikation.");
			//			}
			break;
		}
	}



	/**
	 * Startet ein Debug-Spiel. Wird nicht dem fertigen Spiel beiliegen!
	 * 
	 * @author Peter Dörr<br>
	 * @since 05.12.2012<br>
	 */
	public void START_DEBUG_GAME(){
		//		try{
		//			Network_AutoResponse bot = new Network_AutoResponse();
		//			
		//			Network.IP = "127.0.0.1"; // DEFAULT IP
		//			Network.PORT = 1504; // DEFAULT PORT
		//			String selectedMap = "POV_OneKill";
		//			
		//			//ClientID festlegen
		//			clientID = "Player";
		//
		//			//Einloggen auf den Server
		//			ComObject_logon logon = (ComObject_logon)network.establishCommunication(new ComObject_logon(clientID));
		//			
		//			//ClientKey auslesen und speichern.
		//			clientKey = logon.getClientKey();
		//			
		//			//GameInfo abfragen		
		//			ComObject_gamelist gamelist = (ComObject_gamelist)network.establishCommunication(new ComObject_gamelist(clientID, clientKey));
		//			gameID = gamelist.getGamelist().get(0);
		//			
		//			//Spieler hinzufügen
		//			playerName = "Wir";
		//			ComObject_addPlayer addPlayer = (ComObject_addPlayer)network.establishCommunication(new ComObject_addPlayer(clientID, clientKey, gameID, playerName));
		//			playerID = addPlayer.getPlayerID();
		//			
		//			//Einheitentypen abfragen
		//			MeepleConfig meepleConfig = new MeepleConfig();						
		//			ArrayList<UnitType> unitTypesList = meepleConfig.parseFromNetwork((ComObject_unittype)network.establishCommunication(new ComObject_unittype(clientID, clientKey, selectedMap)));
		//			
		//			//Spiel starten
		//			network.establishCommunication(new ComObject_startGame(clientID, clientKey, gameID, clientKey));
		//			
		//			//Die ersten GameInfos abfragen.
		//			ComObject_gameinfo gameInfo = (ComObject_gameinfo)network.establishCommunication(new ComObject_gameinfo(clientID, clientKey, playerID, gameID));
		//				
		//			//Setzen der 1. Rundennummer
		//			RoundNumber = gameInfo.getTurn();
		//			
		//			//Spielernamen abfragen
		//			Game.playersList = gameInfo.getPlayerNames();
		//			Game.playerIDs = gameInfo.getPlayerIDs();
		//			
		//			//Karteninformationen beziehen und Karte erstellen
		//			ComObject_terrainMap terrainMap = (ComObject_terrainMap)network.establishCommunication(new ComObject_terrainMap(clientID, clientKey, playerID, gameID));
		//			Game.playingField.height = terrainMap.getTerrainMap().size();
		//			Game.playingField.width = terrainMap.getTerrainMap().get(0).size();	
		//			int [][] map = new int[Game.playingField.width][Game.playingField.height];				
		//			for(int spalte = terrainMap.getTerrainMap().get(0).size() - 1; spalte >= 0; spalte--)
		//			{	
		//				for(int zeile = terrainMap.getTerrainMap().size() - 1; zeile >= 0; zeile--)
		//				{
		//					map[zeile][spalte] = terrainMap.getTerrainMap().get(terrainMap.getTerrainMap().get(0).size() - spalte - 1).get(terrainMap.getTerrainMap().size() - zeile - 1);
		//				}
		//			}
		//
		//			Game.playingField.map = map;
		//			Game.playingField.visibility = new boolean[Game.playingField.width][Game.playingField.height];
		//
		//			//Einheiteninformationen beziehen
		//			ComObject_unitMap unitMap = (ComObject_unitMap)network.establishCommunication(new ComObject_unitMap(clientID, clientKey, playerID, gameID));
		//			ArrayList<Integer> unitIDsList = new ArrayList<Integer>();
		//			ArrayList<Point> unitPointsList = new ArrayList<Point>();						
		//			ArrayList<ArrayList<Integer>> unitmap = unitMap.getUnitMap();
		//
		//			for(int spalte = unitmap.get(0).size() - 1; spalte >= 0; spalte--)
		//			{	
		//				for(int zeile = unitmap.size() - 1; zeile >= 0; zeile--)
		//				{
		//					//Wenn der Wert der Karte nicht 0 ist, 
		//					//dann ist es eine Einheiten ID und diese wird der IDsList hinzugefügt und deren Punkt wird gespeichert.
		//					if(!(unitmap.get(zeile).get(spalte) == 0))
		//					{
		//						unitIDsList.add(unitmap.get(zeile).get(spalte));
		//						unitPointsList.add(new Point(unitmap.get(0).size() - spalte - 1, unitmap.size() - zeile - 1)); 
		//					}			
		//				}			
		//			}
		//
		//			//Für Jede Einheiten ID eine unitInfo erstellen und sie der unitInfosList hinzufügen.
		//			ArrayList<ComObject_unitinfo> unitInfosList = new ArrayList<ComObject_unitinfo>();
		//			for(int i = 0; i < unitIDsList.size(); i++)
		//			{
		//				ComObject_unitinfo unitinfo = (ComObject_unitinfo)network.establishCommunication(new ComObject_unitinfo(clientID, clientKey, playerID, gameID, unitIDsList.get(i)));
		//				unitInfosList.add(unitinfo);					
		//			}
		//			MeepleSetup meepleSetup = new MeepleSetup();			
		//			ArrayList<UnitSetup> unitSetupsList = meepleSetup.parseFromNetwork(unitInfosList, unitIDsList, unitPointsList);
		//			Game.playingField.units = meepleSetup.initUnits(unitTypesList, unitSetupsList);
		//
		//			//Animationen festlegen
		//			handleMainMenuAnimation = false;
		//			handleBloodAnimation = true;
		//			
		//			// Spielfeld initialisieren						
		//			initTerrain();
		//
		//			//Zum Spiel Screen wechseln						
		//			nifty.gotoScreen("gameScreen");		
		//			gameStarted = true;
		//			
		//			checkTurn();
		//			
		//			//BOT starten
		//			bot.start();
		//			
		//		} catch (Exception e){
		//			e.printStackTrace();
		//		}
	}	

	/**
	 * Diese Methode resetted alle Spielzustände.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 30.01.2013<br>
	 */
	public void resetGameStates()
	{
		try{
			this.rootNode.detachAllChildren();

			// RE-INIT GAME STATES ETC	
			//Kamera konfigurieren
			cam.setLocation(new Vector3f(0.0f, 125.0f, 0.0f));
			cam.lookAt(new Vector3f(0.0f, 0.0f, -10.0f), new Vector3f(0.0f, 1.0f, 0.0f));		
			flyCam.setEnabled(false);
			inputManager.setCursorVisible(true);			
			//AssetManager konfigurieren
			this.assetManager.registerLocator("data/", FileLocator.class);
			AM = this.assetManager;		
			//Knoten erstellen
			rootNode.attachChild(new Node("Node_Hexagon"));
			((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Base"));
			((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Border"));
			((Node)rootNode.getChild("Node_Hexagon")).attachChild(new Node("Node_Hexagon_Center"));
			rootNode.attachChild(new Node("Node_Unit"));
			((Node)rootNode.getChild("Node_Unit")).attachChild(new Node("Node_Units_Model"));
			((Node)rootNode.getChild("Node_Unit")).attachChild(new Node("Node_Units_Life"));
			rootNode.attachChild(new Node("Node_Terrain"));
			rootNode.attachChild(new Node("Node_BloodAnimation"));
			rootNode.attachChild(new Node("Node_MainMenuAnimation"));
			rootNode.attachChild(new Node("Node_FightAnimation"));	
			//Licht initialisieren
			DirectionalLight light = new DirectionalLight();
			light.setColor(ColorRGBA.White);
			light.setDirection(new Vector3f(0.0f, -1.0f, 0.0f).normalizeLocal());
			rootNode.addLight(light);		
//			viewPort.clearProcessors();
//			//Schatten initialisieren
//			PssmShadowRenderer shadowRenderer = new PssmShadowRenderer(assetManager, 512, 3);
//			shadowRenderer.setDirection(new Vector3f(-0.25f, -1.0f, -0.25f).normalizeLocal());
//			viewPort.addProcessor(shadowRenderer);
			//Audio initialisieren
			audio = new Audio(audioRenderer);
			audio.playMusic(Audio.MUSIC.MENU_MUSIC, 0);  
			audio.setVolumeMusic((int)((sepsisAppSettings.mastervolume / 100f) * sepsisAppSettings.musicvolume));
			audio.setVolumeSound((int)((sepsisAppSettings.mastervolume / 100f) * sepsisAppSettings.soundvolume));
			masterSoundVolume = sepsisAppSettings.mastervolume;    
			// init Network
			if(networkThread != null){
				networkThread.endThread();
			}
			networkThread = new NetworkThread();
			networkThread.setDaemon(true);
			network = new Network();
			flyCam.setDragToRotate(true);        
			//Animation initialisieren
			handleMainMenuAnimation = true;
			handleBloodAnimation = false;        
			// Spielerliste initialisieren
			playersList = new ArrayList<String>();	
			playerIDs = new ArrayList<String>();
			//Spielfeld initialisieren
			playingField = new PlayingField();		
			// SelectedFieldPoint initialisieren
			selectedFieldPoint = new Point();
			// Pathfinding Instanz init
			pathFinding = new PathFinding();	

			// init GUI		
			guiViewPort.clearProcessors();
			NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
			nifty = niftyDisplay.getNifty();
			guiViewPort.addProcessor(niftyDisplay);
			//        nifty.setDebugOptionPanelColors(true);

			//        introScreen = new SepsisIntroScreenController(this, stateManager, nifty);
			startScreen = new SepsisStartScreenController(this, stateManager, nifty);
			setupScreen = new SepsisSetupScreenController(this, stateManager, nifty);
			createGameScreen = new SepsisCreateGameScreenController(this, stateManager, nifty);
			gameScreen = new SepsisGameScreenController(this, stateManager, nifty);
			//        statisticsScreen = new SepsisStatisticsScreenController(this, stateManager, nifty);
			waitForPlayersScreen = new SepsisWaitForPlayersScreenController(this, stateManager, nifty);

			//        nifty.registerScreenController(introScreen);
			nifty.registerScreenController(startScreen);
			nifty.registerScreenController(setupScreen);
			nifty.registerScreenController(createGameScreen);
			nifty.registerScreenController(waitForPlayersScreen);
			nifty.registerScreenController(gameScreen);

			try 
			{
				// Styles laden
				nifty.loadStyleFile("data/gui/styles/" + sepsisAppSettings.style + ".xml");
			} 
			catch (Exception e) 
			{
				logger.error("Game.simpleInitApp.loadStylesAndProperties: catch: " + e.getMessage());
				nifty.loadStyleFile("data/gui/styles/sepsis-styles.xml");
			}

			//        nifty.fromXml("gui/sepsis.xml", "statisticsScreen", introScreen, startScreen, setupScreen, createGameScreen, waitForPlayersScreen, gameScreen, statisticsScreen);
			nifty.fromXml("gui/sepsis.xml", "startScreen");

			// VARIABLEN RESETTEN
			comboTime = 0;
			selectedUnit = null;
			selectedFieldPoint = null;

			gameStarted = false;
			clientID = "";
			clientKey = "";
			gameID = "";
			playerID = "";
			playerName = "";
			mapID = "";
			playerIDs = new ArrayList<String>();
			activePlayerID = "";	
			handleHealthBars = true;
			debugGame = false;
			RoundNumber = 0;
			timer = 0;
			fightAnimationTimer = -1.0f;
			masterSoundVolume = 50f;
			gameOwner = false;		
			statistics = new Statistics();
			playedGameTimeTimer = 0;
			visibilityMode = FOG.OFF;
			autofocus_mode = false;
			autofocus_movementDirection = new Vector3f(0.0f, 0.0f, 0.0f);
			autofocus_speedCurrent = 0.0f;
			autofocus_speedMax = 60.0f;
			autofocus_speedUp = 2.0f;
			autofocus_speedDown = 1.0f;
		} catch (Exception e){
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			logger.error(e.getCause());
		}
	}
}
