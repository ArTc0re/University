package sepsis.gui;

import java.util.ArrayList;
import sepsis.audio.Audio.SOUND;
import sepsis.game.Game;
import sepsis.game.Minimap;
import sepsis.game.TextContainer;
import sepsis.game.TradeInfo;
import sepsis.game.Game.INTERACTIONS;
import sepsis.game.Game.TRADE_REPLY;
import sepsis.game.Game.TURN;
import sepsis.network.ComObject_chat;
import sepsis.network.ComObject_endTurn;
import sepsis.network.ComObject_gameinfo;
import sepsis.network.ComObject_getData;
import sepsis.network.ComObject_surrender;
import sepsis.network.ComObject_tradestations;
import sepsis.network.ComObject_winconditions;
import sepsis.network.IComObject.DATATYPE;
import sepsis.network.Network;
import sepsis.network.IComObject.STATUS;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.post.filters.GammaCorrectionFilter;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.asset.DesktopAssetManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;


/**
 * Diese Klasse kontrolliert den Spielbildschirm.<br>
 * 
 * @author Markus Strobel<br>
 * @since 29.11.2012<br>
 * 
 * 18.12.2012 (Markus Strobel) GUI Symbiose mit Netzwerk/Game hinzugefügt.<br>
 * 
 * 08.01.2013 (Markus Strobel) Surrender added.<br>
 * 
 */
public class SepsisGameScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	@SuppressWarnings("unused")
	private AppStateManager stateManager;	
	private boolean menuPopupState = true;
	private boolean tradeFightPopupState = true;
	private Game game;
	
	private boolean openWindow = false;
	private boolean privateChat = false;
	private String privateChatText = "";
	private Element chatText;
	
	private ArrayList<Integer> UnitIDs = new ArrayList<Integer>();
	private ArrayList<Integer> UnitHPs = new ArrayList<Integer>();

	private Element settingsPopup;
	private Element creditsPopup;
	private Element menuPopup;
	private Element tradeFightPopup;
	public Element getTradePopup;
	public boolean getTradePopupVisible = false;
	private Element makeTradePopup;
	private Element unitFieldInformationPanel;
	private Element unitTitlePanel;
	private Element unitContentPanel;
	private Element helpPopup;
	private Element exitValidationPopup;

	private TextContainer textContainer;
	private Element helpText;
	private Element controlsText;

	// TOP INFORMATION PANEL
	private int menuInfo_RoundNumber = 0;
	private TURN menuInfo_currentTurn = TURN.PLAYER;

	private Element roundNumberText;
	private Element currentTurnText;
	//	private Element turnsTakenListText;

	private Element eventPanel;
	private Element eventPanelText;
	private boolean activeEventPanel = false;
	private float eventTimer = 0f;
	private float updateTimer = 0f;

	// Field Information
	private Element positionXText;
	private Element positionYText;

	// Unit Information
	private Element unitInfo_IDText;
	private Element unitInfo_TypeText;
	private Element unitInfo_HPText;
	private Element unitInfo_APText;
	private Element unitInfo_MPText;
	private Element unitInfo_cargo1Text;
	private Element unitInfo_cargo2Text;
	private Element unitInfo_cargo3Text;
	private Element unitInfo_cargo4Text;
	private Element unitInfo_cargo5Text;	
	private Element unitInfo_CargoStatusText;

	// MiniMap 
	private Minimap miniMap;
	private Element miniMapPanel;
	private float miniMapRefreshTimer = 20f;

	// debug
	private Element debugLayer;
	private Element debugSelectedUnitID;
	private Element debugDoneThisRound;
	private Element debugType;
	private Element debugOwner;
	private Element debugXpos;
	private Element debugYpos;
	private Element debugHitpoints_curr;
	private Element debugHitpoints_max;
	private Element debugMovement_curr;
	private Element debugMovement_max;
	private Element debugAttackPower;
	private Element debugCargo_1;
	private Element debugCargo_2;
	private Element debugCargo_3;
	private Element debugCargo_4;
	private Element debugCargo_5;
	private Element debugIsActive;
	private Element debugWayPointsList;
	private Element debugRoundNumber;
	private Element debugTurnsTaken;
	private Element debugCurrentTurn;
	private Element debugCurrentState;

	// getTrade	
	private TextField getTradeBidGood1TextField;
	private TextField getTradeBidGood2TextField;
	private TextField getTradeBidGood3TextField;
	private TextField getTradeBidGood4TextField;
	private TextField getTradeBidGood5TextField;	
	private TextField getTradeAskGood1TextField;
	private TextField getTradeAskGood2TextField;
	private TextField getTradeAskGood3TextField;
	private TextField getTradeAskGood4TextField;
	private TextField getTradeAskGood5TextField;	
	private Element getgood1supplyText;
	private Element getgood2supplyText;
	private Element getgood3supplyText;
	private Element getgood4supplyText;
	private Element getgood5supplyText;
	private Element gettradeCargoInfoText;
	
	// tradestation
	
	private Element tradestationgood1supplyText;
	private Element tradestationgood2supplyText;
	private Element tradestationgood3supplyText;
	private Element tradestationgood4supplyText;
	private Element tradestationgood5supplyText;
	private Element tradestationtradeCargoInfoText;
	
	
	// offerTrade
	private TextField makeTradeBidGood1TextField;
	private TextField makeTradeBidGood2TextField;
	private TextField makeTradeBidGood3TextField;
	private TextField makeTradeBidGood4TextField;
	private TextField makeTradeBidGood5TextField;	
	private TextField makeTradeAskGood1TextField;
	private TextField makeTradeAskGood2TextField;
	private TextField makeTradeAskGood3TextField;
	private TextField makeTradeAskGood4TextField;
	private TextField makeTradeAskGood5TextField;	
	private Element tradeCargoInfoText;
	private Element good1supplyText;
	private Element good2supplyText;
	private Element good3supplyText;
	private Element good4supplyText;
	private Element good5supplyText;
	
	/**
	 * Dieser Wert bestimmt um wieviel im Handelsfenster mit den + und - Buttons die Handelsmenge verändert wird.
	 */
	private int tradeValue;

	// SETTINGS	
	private boolean activeSettingsPopup;	
	private AppSettings settings;	
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
	private float gamma = 0.5f;
	private float brightness = 0.5f;

	// Chat Window
	private ListBox<String> chatReceiveListBox;
	private TextField chatSendTextField;
	private Button sendButton;
	private DropDown<String> receiverDropDown;
	private boolean receiverDropDownFilled = false;
	private Element chatFrameShow;	

	// TradeStation
	private boolean activeTradeStationOffer = false;
	private boolean activeMakeTradeOffer = false;
	private boolean activeGetTradeOffer = false;
	private Element tradeStationPopup;
	private Element moneyValue;

	private Element good1ratio;
	private Element good2ratio;
	private Element good3ratio;
	private Element good4ratio;
	private Element good5ratio;

	private Element good1amount;
	private Element good2amount;
	private Element good3amount;
	private Element good4amount;
	private Element good5amount;

	private TextField tradeStationBidGood1TextField;
	private TextField tradeStationBidGood2TextField;
	private TextField tradeStationBidGood3TextField;
	private TextField tradeStationBidGood4TextField;
	private TextField tradeStationBidGood5TextField;	
	private TextField tradeStationAskGood1TextField;
	private TextField tradeStationAskGood2TextField;
	private TextField tradeStationAskGood3TextField;
	private TextField tradeStationAskGood4TextField;
	private TextField tradeStationAskGood5TextField;

	private TextField sendTradeMessageTextField;
	private TextField getTradeMessageTextField;

	// statisticsPopup	
	private Element statisticsPopup;
	// statistic Panels
	private Element currentGameStatisticsPanel;
	private Element allGamesStatisticsPanel;	

	// currentGamePanels
	private Element playedTime;

	// "current" Barriers
	private Element movesbarrier;
	private Element f1movesbarrier;
	private Element f2movesbarrier;
	private Element f3movesbarrier;
	private Element t1movesbarrier;
	private Element t2movesbarrier;
	private Element killsbarrier;
	private Element f1killsbarrier;
	private Element f2killsbarrier;
	private Element f3killsbarrier;
	private Element t1killsbarrier;
	private Element t2killsbarrier;	
	private Element tradesbarrier;
	private Element f1tradesbarrier;
	private Element f2tradesbarrier;
	private Element f3tradesbarrier;
	private Element t1tradesbarrier;
	private Element t2tradesbarrier;

	// currentGameText
	private Element movesText;
	private Element f1movesText;
	private Element f2movesText;
	private Element f3movesText;
	private Element t1movesText;
	private Element t2movesText;	
	private Element killsText;
	private Element f1killsText;
	private Element f2killsText;
	private Element f3killsText;
	private Element t1killsText;
	private Element t2killsText;
	private Element tradesText;
	private Element f1tradesText;
	private Element f2tradesText;
	private Element f3tradesText;
	private Element t1tradesText;
	private Element t2tradesText;
	private Element playedTimeText;

	// allGamesPanels
	private Element allplayedTime;

	// "all" Barriers
	private Element allmovesbarrier;
	private Element allf1movesbarrier;
	private Element allf2movesbarrier;
	private Element allf3movesbarrier;
	private Element allt1movesbarrier;
	private Element allt2movesbarrier;
	private Element allkillsbarrier;
	private Element allf1killsbarrier;
	private Element allf2killsbarrier;
	private Element allf3killsbarrier;
	private Element allt1killsbarrier;
	private Element allt2killsbarrier;	
	private Element alltradesbarrier;
	private Element allf1tradesbarrier;
	private Element allf2tradesbarrier;
	private Element allf3tradesbarrier;
	private Element allt1tradesbarrier;
	private Element allt2tradesbarrier;

	// allGamesText
	private Element allmovesText;
	private Element allf1movesText;
	private Element allf2movesText;
	private Element allf3movesText;
	private Element allt1movesText;
	private Element allt2movesText;	
	private Element allkillsText;
	private Element allf1killsText;
	private Element allf2killsText;
	private Element allf3killsText;
	private Element allt1killsText;
	private Element allt2killsText;
	private Element alltradesText;
	private Element allf1tradesText;
	private Element allf2tradesText;
	private Element allf3tradesText;
	private Element allt1tradesText;
	private Element allt2tradesText;
	private Element allplayedTimeText;

	// WinnerText
	private Element winnerLooserText;

	// WinConditions
	public Element winConditionsPanel;
	private Element extinctionCondition;
	private Element goodPlacementCondition;
	private Element goodsNeeded;
	private Element goodPlacementDescription;
	private Element goodPlacementsPlayerText;
	private Element goodReference;

	private static int minimapCount = 0;

	//Hilfvariable
	private boolean connectedPlayersGameInfoHandled = false;
	
	private ArrayList<Integer> tradeStationIDs;
	private ArrayList<ArrayList<Integer>> tradeStationsRatiosList;
	private ArrayList<ArrayList<Integer>> tradeStationAmounts;
	private int currentTradeStationID = 0;
	
	private ArrayList<String> connectedPlayersIDs;
	private ArrayList<String> connectedPlayerNames;
	
	/**
	 * Der Konstruktor des SepsisGameScreenController
	 * 
	 * @author Markus Strobel
	 * @since 29.11.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 * 
	 * 
	 * 04.12.2012 (Markus Strobel) MenuPopup hinzugefügt.<br>
	 * 
	 * 05.12.2012 (Markus Strobel) Debug Layer hinzugefügt.<br>
	 */
	public SepsisGameScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.stateManager = stateManager;
		this.nifty = nifty;
		this.game = game;

		// MiniMap
		miniMap = new Minimap();
		miniMap.setResolution(320);
		
		tradeStationIDs = new ArrayList<Integer>();
		tradeStationsRatiosList = new ArrayList<ArrayList<Integer>>();
		tradeStationAmounts = new ArrayList<ArrayList<Integer>>();
		
		connectedPlayersIDs = new ArrayList<String>();
		connectedPlayerNames = new ArrayList<String>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bind(Nifty nifty, Screen screen) {

		this.screen = screen;
		this.settingsPopup = nifty.createPopup("settingsPopup");
		this.creditsPopup = nifty.createPopup("creditsPopup");
		this.menuPopup = nifty.createPopup("menuPopup");
		this.tradeFightPopup = nifty.createPopup("tradeFightPopup");
		this.makeTradePopup = nifty.createPopup("makeTradePopup");
		this.getTradePopup = nifty.createPopup("getTradePopup");
		this.helpPopup = nifty.createPopup("helpPopup");
		this.exitValidationPopup = nifty.createPopup("exitValidationPopup");

		debugLayer = screen.findElementByName("debugLayer");
		debugLayer.setVisible(false);

		textContainer = new TextContainer();
		helpText = helpPopup.findElementByName("storyText");
		helpText.getRenderer(TextRenderer.class).setText(textContainer.getStory());
		controlsText = helpPopup.findElementByName("controlText");
		controlsText.getRenderer(TextRenderer.class).setText(textContainer.getControls());

		/*
		 * TOP INFORMATION PANEL
		 */
		//		turnsTakenListText = screen.findElementByName("movableUnitsText");
		//		turnsTakenListText.getRenderer(TextRenderer.class).setText(menuInfo_movableUnitIDsList.toString());
		currentTurnText = screen.findElementByName("currentTurnText");
		currentTurnText.getRenderer(TextRenderer.class).setText(menuInfo_currentTurn.toString());
		roundNumberText = screen.findElementByName("roundNrText");
		roundNumberText.getRenderer(TextRenderer.class).setText(String.valueOf(menuInfo_RoundNumber));
		chatText = screen.findElementByName("chatText");
		
		
		/*
		 * FIELD INFORMATION PANEL
		 */
		positionXText = screen.findElementByName("fieldInfo_Position_X");		
		positionYText = screen.findElementByName("fieldInfo_Position_Y");			
		// Das übergeordnete Panel
		unitFieldInformationPanel = screen.findElementByName("unitInformationPanel");
		//		unitFieldInformationPanel.hide();
		// Die beiden Untergeordneten Unit Panel, diese müssen ausgeblendet werden, wenn nur ein Feld, jedoch keine Unit selected ist.		
		unitTitlePanel = screen.findElementByName("unitTitlePanel");
		unitContentPanel = screen.findElementByName("unitContentPanel");

		/* 
		 * UNIT INFORMATION PANEL
		 * 
		 */
		unitInfo_IDText = screen.findElementByName("unitInfo_ID");
		unitInfo_TypeText = screen.findElementByName("unitInfo_Type");
		unitInfo_HPText = screen.findElementByName("unitInfo_HP");
		unitInfo_APText = screen.findElementByName("unitInfo_AP");
		unitInfo_MPText = screen.findElementByName("unitInfo_MP");
		unitInfo_cargo1Text = screen.findElementByName("unitInfo_cargo1");
		unitInfo_cargo2Text = screen.findElementByName("unitInfo_cargo2");
		unitInfo_cargo3Text = screen.findElementByName("unitInfo_cargo3");
		unitInfo_cargo4Text = screen.findElementByName("unitInfo_cargo4");
		unitInfo_cargo5Text = screen.findElementByName("unitInfo_cargo5");	
		unitInfo_CargoStatusText = screen.findElementByName("unitCargoInfo");

		/*
		 * DEBUG PANEL
		 */
		debugSelectedUnitID = nifty.getCurrentScreen().findElementByName("selectedUnitID");
		debugDoneThisRound = nifty.getCurrentScreen().findElementByName("doneThisRound");
		debugType = nifty.getCurrentScreen().findElementByName("type");
		debugOwner = nifty.getCurrentScreen().findElementByName("owner");
		debugXpos = nifty.getCurrentScreen().findElementByName("xpos");
		debugYpos = nifty.getCurrentScreen().findElementByName("ypos");
		debugHitpoints_curr = nifty.getCurrentScreen().findElementByName("hitpoints_curr");
		debugHitpoints_max = nifty.getCurrentScreen().findElementByName("hitpoints_max");
		debugMovement_curr = nifty.getCurrentScreen().findElementByName("movement_curr");
		debugMovement_max = nifty.getCurrentScreen().findElementByName("movement_max");
		debugAttackPower = nifty.getCurrentScreen().findElementByName("attackPower");
		debugCargo_1 = nifty.getCurrentScreen().findElementByName("cargo_1");
		debugCargo_2 = nifty.getCurrentScreen().findElementByName("cargo_2");
		debugCargo_3 = nifty.getCurrentScreen().findElementByName("cargo_3");
		debugCargo_4 = nifty.getCurrentScreen().findElementByName("cargo_4");
		debugCargo_5 = nifty.getCurrentScreen().findElementByName("cargo_5");
		debugIsActive = nifty.getCurrentScreen().findElementByName("isActive");
		debugWayPointsList = nifty.getCurrentScreen().findElementByName("wayPointsList");
		debugRoundNumber = nifty.getCurrentScreen().findElementByName("roundNumber");
		debugTurnsTaken = nifty.getCurrentScreen().findElementByName("turnsTaken");
		debugCurrentTurn = nifty.getCurrentScreen().findElementByName("currentTurn");
		debugCurrentState = nifty.getCurrentScreen().findElementByName("currentState");


		/*
		 *  getTrade
		 */
		getTradeAskGood1TextField = getTradePopup.findNiftyControl("askGood1TextField", TextField.class);
		getTradeAskGood1TextField.setText("0");	
		getTradeAskGood1TextField.disable();
		getTradeAskGood2TextField = getTradePopup.findNiftyControl("askGood2TextField", TextField.class);
		getTradeAskGood2TextField.setText("0");
		getTradeAskGood2TextField.disable();
		getTradeAskGood3TextField = getTradePopup.findNiftyControl("askGood3TextField", TextField.class);
		getTradeAskGood3TextField.setText("0");
		getTradeAskGood3TextField.disable();
		getTradeAskGood4TextField = getTradePopup.findNiftyControl("askGood4TextField", TextField.class);
		getTradeAskGood4TextField.setText("0");
		getTradeAskGood4TextField.disable();
		getTradeAskGood5TextField = getTradePopup.findNiftyControl("askGood5TextField", TextField.class);
		getTradeAskGood5TextField.setText("0");
		getTradeAskGood5TextField.disable();
		getTradeBidGood1TextField = getTradePopup.findNiftyControl("bidGood1TextField", TextField.class);
		getTradeBidGood1TextField.setText("0");
		getTradeBidGood1TextField.disable();
		getTradeBidGood2TextField = getTradePopup.findNiftyControl("bidGood2TextField", TextField.class);
		getTradeBidGood2TextField.setText("0");
		getTradeBidGood2TextField.disable();
		getTradeBidGood3TextField = getTradePopup.findNiftyControl("bidGood3TextField", TextField.class);
		getTradeBidGood3TextField.setText("0");
		getTradeBidGood3TextField.disable();
		getTradeBidGood4TextField = getTradePopup.findNiftyControl("bidGood4TextField", TextField.class);
		getTradeBidGood4TextField.setText("0");
		getTradeBidGood4TextField.disable();
		getTradeBidGood5TextField = getTradePopup.findNiftyControl("bidGood5TextField", TextField.class);
		getTradeBidGood5TextField.setText("0");
		getTradeBidGood5TextField.disable();		
		getTradeMessageTextField = getTradePopup.findNiftyControl("getTradeMessage", TextField.class);
		getgood1supplyText = getTradePopup.findElementByName("good1supply");
		getgood2supplyText = getTradePopup.findElementByName("good2supply");
		getgood3supplyText = getTradePopup.findElementByName("good3supply");
		getgood4supplyText = getTradePopup.findElementByName("good4supply");
		getgood5supplyText = getTradePopup.findElementByName("good5supply");
		gettradeCargoInfoText = getTradePopup.findElementByName("tradeCargoInfo");
		gettradeCargoInfoText.getRenderer(TextRenderer.class).setText("");


		/*
		 * makeTrade
		 */
		makeTradeAskGood1TextField = makeTradePopup.findNiftyControl("askGood1TextField", TextField.class);
		makeTradeAskGood1TextField.setText("0");	
		makeTradeAskGood1TextField.disable();
		makeTradeAskGood2TextField = makeTradePopup.findNiftyControl("askGood2TextField", TextField.class);
		makeTradeAskGood2TextField.setText("0");
		makeTradeAskGood2TextField.disable();
		makeTradeAskGood3TextField = makeTradePopup.findNiftyControl("askGood3TextField", TextField.class);
		makeTradeAskGood3TextField.setText("0");
		makeTradeAskGood3TextField.disable();
		makeTradeAskGood4TextField = makeTradePopup.findNiftyControl("askGood4TextField", TextField.class);
		makeTradeAskGood4TextField.setText("0");
		makeTradeAskGood4TextField.disable();
		makeTradeAskGood5TextField = makeTradePopup.findNiftyControl("askGood5TextField", TextField.class);
		makeTradeAskGood5TextField.setText("0");
		makeTradeAskGood5TextField.disable();
		makeTradeBidGood1TextField = makeTradePopup.findNiftyControl("bidGood1TextField", TextField.class);
		makeTradeBidGood1TextField.setText("0");
		makeTradeBidGood1TextField.disable();
		makeTradeBidGood2TextField = makeTradePopup.findNiftyControl("bidGood2TextField", TextField.class);
		makeTradeBidGood2TextField.setText("0");
		makeTradeBidGood2TextField.disable();
		makeTradeBidGood3TextField = makeTradePopup.findNiftyControl("bidGood3TextField", TextField.class);
		makeTradeBidGood3TextField.setText("0");
		makeTradeBidGood3TextField.disable();
		makeTradeBidGood4TextField = makeTradePopup.findNiftyControl("bidGood4TextField", TextField.class);
		makeTradeBidGood4TextField.setText("0");
		makeTradeBidGood4TextField.disable();
		makeTradeBidGood5TextField = makeTradePopup.findNiftyControl("bidGood5TextField", TextField.class);
		makeTradeBidGood5TextField.setText("0");
		makeTradeBidGood5TextField.disable();
		sendTradeMessageTextField = makeTradePopup.findNiftyControl("sendTradeMessage", TextField.class);
		tradeCargoInfoText = makeTradePopup.findElementByName("tradeCargoInfo");
		tradeCargoInfoText.getRenderer(TextRenderer.class).setText("0");
		good1supplyText = makeTradePopup.findElementByName("good1supply");
		good2supplyText = makeTradePopup.findElementByName("good2supply");
		good3supplyText = makeTradePopup.findElementByName("good3supply");
		good4supplyText = makeTradePopup.findElementByName("good4supply");
		good5supplyText = makeTradePopup.findElementByName("good5supply");

		tradeValue = 1;

		/*
		 * tradeStation
		 */
		this.tradeStationPopup = nifty.createPopup("tradeStationPopup");

		moneyValue = tradeStationPopup.findElementByName("moneyValue");
		
		tradeStationAskGood1TextField = tradeStationPopup.findNiftyControl("askGood1TextField", TextField.class);
		tradeStationAskGood1TextField.setText("0");	
		tradeStationAskGood1TextField.disable();
		tradeStationAskGood2TextField = tradeStationPopup.findNiftyControl("askGood2TextField", TextField.class);
		tradeStationAskGood2TextField.setText("0");
		tradeStationAskGood2TextField.disable();
		tradeStationAskGood3TextField = tradeStationPopup.findNiftyControl("askGood3TextField", TextField.class);
		tradeStationAskGood3TextField.setText("0");
		tradeStationAskGood3TextField.disable();
		tradeStationAskGood4TextField = tradeStationPopup.findNiftyControl("askGood4TextField", TextField.class);
		tradeStationAskGood4TextField.setText("0");
		tradeStationAskGood4TextField.disable();
		tradeStationAskGood5TextField = tradeStationPopup.findNiftyControl("askGood5TextField", TextField.class);
		tradeStationAskGood5TextField.setText("0");
		tradeStationAskGood5TextField.disable();
		tradeStationBidGood1TextField = tradeStationPopup.findNiftyControl("bidGood1TextField", TextField.class);
		tradeStationBidGood1TextField.setText("0");
		tradeStationBidGood1TextField.disable();
		tradeStationBidGood2TextField = tradeStationPopup.findNiftyControl("bidGood2TextField", TextField.class);
		tradeStationBidGood2TextField.setText("0");
		tradeStationBidGood2TextField.disable();
		tradeStationBidGood3TextField = tradeStationPopup.findNiftyControl("bidGood3TextField", TextField.class);
		tradeStationBidGood3TextField.setText("0");
		tradeStationBidGood3TextField.disable();
		tradeStationBidGood4TextField = tradeStationPopup.findNiftyControl("bidGood4TextField", TextField.class);
		tradeStationBidGood4TextField.setText("0");
		tradeStationBidGood4TextField.disable();
		tradeStationBidGood5TextField = tradeStationPopup.findNiftyControl("bidGood5TextField", TextField.class);
		tradeStationBidGood5TextField.setText("0");
		tradeStationBidGood5TextField.disable();
		good1ratio = tradeStationPopup.findElementByName("good1ratio");
		good2ratio = tradeStationPopup.findElementByName("good2ratio");
		good3ratio = tradeStationPopup.findElementByName("good3ratio");
		good4ratio = tradeStationPopup.findElementByName("good4ratio");
		good5ratio = tradeStationPopup.findElementByName("good5ratio");
		good1amount = tradeStationPopup.findElementByName("good1amount");
		good2amount = tradeStationPopup.findElementByName("good2amount");
		good3amount = tradeStationPopup.findElementByName("good3amount");
		good4amount = tradeStationPopup.findElementByName("good4amount");
		good5amount = tradeStationPopup.findElementByName("good5amount");
		tradestationgood1supplyText = tradeStationPopup.findElementByName("good1supply");
		tradestationgood2supplyText = tradeStationPopup.findElementByName("good2supply");
		tradestationgood3supplyText = tradeStationPopup.findElementByName("good3supply");
		tradestationgood4supplyText = tradeStationPopup.findElementByName("good4supply");
		tradestationgood5supplyText = tradeStationPopup.findElementByName("good5supply");
		tradestationtradeCargoInfoText = tradeStationPopup.findElementByName("tradeCargoInfo");
		tradestationtradeCargoInfoText.getRenderer(TextRenderer.class).setText("0");

		// event panel
		eventPanel = screen.findElementByName("eventPanel");
		eventPanelText = screen.findElementByName("eventPanelText");
		eventPanel.setVisible(false);

		// MiniMap
		miniMapPanel = screen.findElementByName("miniMapPanel");

		// Chat Window
		chatReceiveListBox = screen.findNiftyControl("chatReceiveListBox", ListBox.class);
		chatReceiveListBox.disable();
		chatReceiveListBox.setFocusable(false);
		chatSendTextField = screen.findNiftyControl("chatSendTextField", TextField.class);
		sendButton = screen.findNiftyControl("sendButton", Button.class);
		sendButton.setFocusable(true);
		receiverDropDown = screen.findNiftyControl("receiverDropDown", DropDown.class);
		receiverDropDown.addItem("Server");
		receiverDropDown.addItem("Game");
		screen.setDefaultFocusElement("sendButton");
		chatFrameShow = screen.findElementByName("chatFrameShow");

		// WinConditionsPanel
		winConditionsPanel = screen.findElementByName("winConditionsPanel");
		winConditionsPanel.hide();
		extinctionCondition = screen.findElementByName("extinctionCondition");
		goodPlacementCondition = screen.findElementByName("goodPlacementCondition");
		goodsNeeded = screen.findElementByName("goodsNeeded");
		goodsNeeded.hide();
		goodPlacementDescription = screen.findElementByName("goodPlacementDescription");
		goodPlacementDescription.hide();
		goodPlacementsPlayerText = screen.findElementByName("goodPlacementsPlayerText");
		goodPlacementsPlayerText.hide();
		goodReference = screen.findElementByName("goodReference");
		goodReference.hide();




		// Statistiscs Popup	
		statisticsPopup = nifty.createPopup("statisticsPopup");
		// currentGamePanels
		playedTime = statisticsPopup.findElementByName("playedTimePanel");

		// "current" Barriers
		movesbarrier = statisticsPopup.findElementByName("movesbarrier");
		f1movesbarrier = statisticsPopup.findElementByName("f1movesbarrier");
		f2movesbarrier = statisticsPopup.findElementByName("f2movesbarrier");
		f3movesbarrier = statisticsPopup.findElementByName("f3movesbarrier");
		t1movesbarrier = statisticsPopup.findElementByName("t1movesbarrier");
		t2movesbarrier = statisticsPopup.findElementByName("t2movesbarrier");		
		killsbarrier = statisticsPopup.findElementByName("killsbarrier");
		f1killsbarrier = statisticsPopup.findElementByName("f1killsbarrier");
		f2killsbarrier = statisticsPopup.findElementByName("f2killsbarrier");
		f3killsbarrier = statisticsPopup.findElementByName("f3killsbarrier");
		t1killsbarrier = statisticsPopup.findElementByName("t1killsbarrier");
		t2killsbarrier = statisticsPopup.findElementByName("t2killsbarrier");		
		tradesbarrier = statisticsPopup.findElementByName("tradesbarrier");
		f1tradesbarrier = statisticsPopup.findElementByName("f1tradesbarrier");
		f2tradesbarrier = statisticsPopup.findElementByName("f2tradesbarrier");
		f3tradesbarrier = statisticsPopup.findElementByName("f3tradesbarrier");
		t1tradesbarrier = statisticsPopup.findElementByName("t1tradesbarrier");
		t2tradesbarrier = statisticsPopup.findElementByName("t2tradesbarrier");

		// currentGameText
		movesText = statisticsPopup.findElementByName("movesText");
		f1movesText = statisticsPopup.findElementByName("f1movesText");
		f2movesText = statisticsPopup.findElementByName("f2movesText");
		f3movesText = statisticsPopup.findElementByName("f3movesText");
		t1movesText = statisticsPopup.findElementByName("t1movesText");
		t2movesText = statisticsPopup.findElementByName("t2movesText");	
		killsText = statisticsPopup.findElementByName("killsText");
		f1killsText = statisticsPopup.findElementByName("f1killsText");
		f2killsText = statisticsPopup.findElementByName("f2killsText");
		f3killsText = statisticsPopup.findElementByName("f3killsText");
		t1killsText = statisticsPopup.findElementByName("t1killsText");
		t2killsText = statisticsPopup.findElementByName("t2killsText");
		tradesText = statisticsPopup.findElementByName("tradesText");
		f1tradesText = statisticsPopup.findElementByName("f1tradesText");
		f2tradesText = statisticsPopup.findElementByName("f2tradesText");
		f3tradesText = statisticsPopup.findElementByName("f3tradesText");
		t1tradesText = statisticsPopup.findElementByName("t1tradesText");
		t2tradesText = statisticsPopup.findElementByName("t2tradesText");
		playedTimeText = statisticsPopup.findElementByName("playedTimeText");

		// allGamesPanels
		allplayedTime = statisticsPopup.findElementByName("allplayedTimePanel");

		// "all" Barriers
		allmovesbarrier = statisticsPopup.findElementByName("allmovesbarrier");
		allf1movesbarrier = statisticsPopup.findElementByName("allf1movesbarrier");
		allf2movesbarrier = statisticsPopup.findElementByName("allf2movesbarrier");
		allf3movesbarrier = statisticsPopup.findElementByName("allf3movesbarrier");
		allt1movesbarrier = statisticsPopup.findElementByName("allt1movesbarrier");
		allt2movesbarrier = statisticsPopup.findElementByName("allt2movesbarrier");		
		allkillsbarrier = statisticsPopup.findElementByName("allkillsbarrier");
		allf1killsbarrier = statisticsPopup.findElementByName("allf1killsbarrier");
		allf2killsbarrier = statisticsPopup.findElementByName("allf2killsbarrier");
		allf3killsbarrier = statisticsPopup.findElementByName("allf3killsbarrier");
		allt1killsbarrier = statisticsPopup.findElementByName("allt1killsbarrier");
		allt2killsbarrier = statisticsPopup.findElementByName("allt2killsbarrier");		
		alltradesbarrier = statisticsPopup.findElementByName("alltradesbarrier");
		allf1tradesbarrier = statisticsPopup.findElementByName("allf1tradesbarrier");
		allf2tradesbarrier = statisticsPopup.findElementByName("allf2tradesbarrier");
		allf3tradesbarrier = statisticsPopup.findElementByName("allf3tradesbarrier");
		allt1tradesbarrier = statisticsPopup.findElementByName("allt1tradesbarrier");
		allt2tradesbarrier = statisticsPopup.findElementByName("allt2tradesbarrier");

		// allGamesText
		allmovesText = statisticsPopup.findElementByName("allmovesText");
		allf1movesText = statisticsPopup.findElementByName("allf1movesText");
		allf2movesText = statisticsPopup.findElementByName("allf2movesText");
		allf3movesText = statisticsPopup.findElementByName("allf3movesText");
		allt1movesText = statisticsPopup.findElementByName("allt1movesText");
		allt2movesText = statisticsPopup.findElementByName("allt2movesText");	
		allkillsText = statisticsPopup.findElementByName("allkillsText");
		allf1killsText = statisticsPopup.findElementByName("allf1killsText");
		allf2killsText = statisticsPopup.findElementByName("allf2killsText");
		allf3killsText = statisticsPopup.findElementByName("allf3killsText");
		allt1killsText = statisticsPopup.findElementByName("allt1killsText");
		allt2killsText = statisticsPopup.findElementByName("allt2killsText");
		alltradesText = statisticsPopup.findElementByName("alltradesText");
		allf1tradesText = statisticsPopup.findElementByName("allf1tradesText");
		allf2tradesText = statisticsPopup.findElementByName("allf2tradesText");
		allf3tradesText = statisticsPopup.findElementByName("allf3tradesText");
		allt1tradesText = statisticsPopup.findElementByName("allt1tradesText");
		allt2tradesText = statisticsPopup.findElementByName("allt2tradesText");
		allplayedTimeText = statisticsPopup.findElementByName("allplayedTimeText");

		// statistics
		currentGameStatisticsPanel = statisticsPopup.findElementByName("currentGameStatisticsPanel");
		allGamesStatisticsPanel = statisticsPopup.findElementByName("allGamesStatisticsPanel");

		// WinnerText
		winnerLooserText = statisticsPopup.findElementByName("winnerLooserText");
	}

	/**
	 * Diese Methode fragt die WinConditions ab und leitet diese Anfrage an den NetzwerkThread weiter<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br>
	 */
	public void setWinConditions()
	{
		game.networkThread.sendIComObject(new ComObject_winconditions(Game.clientID, Game.clientKey, Game.mapID));
	}

	/**
	 * Diese Methode bekommt ein ComObject_winconditions und speichert dieses local ab, da dies für die WinConditions-Visualisierung benötigt wird.
	 * @param winCon das ComObject_wincondition
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br>
	 * 
	 */
	public void setWinConditionsSended(ComObject_winconditions winCon)
	{
		// Extinction
		if(winCon.getExtinction())
		{
			extinctionCondition.getRenderer(TextRenderer.class).setText("Extinction to WIN: True");
		}
		else
		{
			extinctionCondition.getRenderer(TextRenderer.class).setText("Extinction to WIN: False");
		}
		
		if(winCon.getGoodplacement())
		{
			int needed = 0;
			try 
			{
				needed = winCon.getNeededConditions();
			} 
			catch (Exception e) 
			{
				Game.logger.error("SepsisGameScreenController.setWinConditionsSended() catch: " + e.getMessage());
				needed = 0;
			}
			goodPlacementCondition.getRenderer(TextRenderer.class).setText("GoodPlacement to WIN: True");
			goodsNeeded.getRenderer(TextRenderer.class).setText(String.valueOf(needed) + " GoodPlacements needed to WIN");
			goodPlacementDescription.getRenderer(TextRenderer.class).setText("each line is one condition for goodplacement " + System.lineSeparator() +" with its respective goods and target field-coordinates");
			
			String endOfLine = System.getProperty("line.separator"); 

			String playerGoods = endOfLine + "";
			String enemyGoods = endOfLine + "";

			for(int i = 0; i < winCon.getPlayerindex().size(); i++)
			{
				String line = "";

				String amount = String.valueOf(winCon.getAmount().get(i));
				String fieldX = String.valueOf(winCon.getFieldX().get(i));
				String fieldY = String.valueOf(winCon.getFieldY().get(i));

				switch(winCon.getGood().get(i))
				{
				default:	
					line = "Goods (" + amount + ",0,0,0,0) at field-coordinates (" + fieldX + "," + fieldY + ")";
					break;
				case 1:
					line = "Goods (" + amount + ",0,0,0,0) at field-coordinates (" + fieldX + "," + fieldY + ")";
					break;
				case 2:
					line = "Goods (0," + amount + ",0,0,0) at field-coordinates (" + fieldX + "," + fieldY + ")";
					break;
				case 3:
					line = "Goods (0,0,"+ amount + ",0,0) at field-coordinates (" + fieldX + "," + fieldY + ")";
					break;
				case 4:
					line = "Goods (0,0, 0," + amount + ",0) at field-coordinates (" + fieldX + "," + fieldY + ")";
					break;
				case 5: 
					line = "Goods (0,0,0,0," + amount + ") at field-coordinates ( " + fieldX + "," + fieldY + ")";
					break;
				}

				if(game.gameOwner)
				{						
					if(winCon.getPlayerindex().get(i) == 0)
					{
						playerGoods += line + endOfLine;
					}
					else
					{
						enemyGoods += line + endOfLine;
					}
				}
				else
				{
					if(winCon.getPlayerindex().get(i) == 1)
					{
						playerGoods += line + endOfLine;
					}
					else
					{
						enemyGoods += line + endOfLine;
					}
				}

			}

			String conditions = "Player" + endOfLine +  playerGoods + endOfLine + endOfLine + "Enemy" + endOfLine + enemyGoods;

			goodPlacementsPlayerText.getRenderer(TextRenderer.class).setText(conditions);
			//				goodPlacementsEnemyText.getRenderer(TextRenderer.class).setText(enemyGoods);

			goodsNeeded.show();	
			goodPlacementDescription.show();
			goodPlacementsPlayerText.show();
			goodReference.show();			
		}
		else
		{
			goodPlacementCondition.getRenderer(TextRenderer.class).setText("GoodPlacement to WIN: False");
			goodsNeeded.hide();	
			goodPlacementDescription.hide();
			goodPlacementsPlayerText.hide();
			goodReference.hide();	
		}
		
		
	}	
	
	
//	/**
//	 * Diese Methode soll Zeile für Zeile in das WinConditionsPanel einfügen, allerdings ist diese Methode nicht fertig implementiert...
//	 * 
//	 * @author Markus Strobel<br>
//	 * @since 20.02.2013<br>
//	 */
//	public void generateWinConditionsLinePanels()
//	{
//		int line = 0;
//		
//		 FUNKTIONIERT BIS HIER HIN NOCH NICHT, DAHER ERST MAL ANDERWEITIG GELÖST...
//		
//		// Line Panel erstellen
//		String lineName = "line" + String.valueOf(line);
//		new PanelBuilder(lineName) {{
//			
//				childLayout(ChildLayoutType.Horizontal);
//				width(percentage(90));
//				height(percentage(4));
//				backgroundColor(new Color(0.3f, 0.3f, 0.7f, 1.0f));
//				
//				// Adenin
//			    panel(new PanelBuilder() {{
//			        id("adenin");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(15));
//			        backgroundColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
//			      }});
//			    
//				// cythosin
//			    panel(new PanelBuilder() {{
//			        id("cythosin");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(15));
//			        backgroundColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
//			      }});
//			    
//				// Guanin
//			    panel(new PanelBuilder() {{
//			        id("guanin");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(15));
//			        backgroundColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
//			      }});
//			    
//				// Thymin
//			    panel(new PanelBuilder() {{
//			        id("thymin");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(15));
//			        backgroundColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
//			      }});
//			    
//				// uracil
//			    panel(new PanelBuilder() {{
//			        id("uracil");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(15));
//			        backgroundColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
//			      }});
//			    
//				// X
//			    panel(new PanelBuilder() {{
//			        id("x");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(10));
//			        backgroundColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
//			      }});
//			    
//				// Y
//			    panel(new PanelBuilder() {{
//			        id("y");
//			        childLayoutCenter();
//			        height(percentage(5));
//			        width(percentage(10));
//			        backgroundColor(new Color(0.1f, 0.1f, 0.1f, 1.0f));
//			      }});	
//		
//			}}.build(nifty, screen, winConditionLinesPanel);
//
//			
//			
//
//	}

	/**
	 * Diese Methode lässt das winConditionsPanel erscheinen bzw verschwinden<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.01.2013<br>
	 */
	public void toggleWinConditions()
	{
		if(winConditionsPanel.isVisible())
		{
			winConditionsPanel.hide();
		}
		else
		{
			setWinConditions();
			winConditionsPanel.show();
		}
	}

	/**
	 * Diese Methode setzt die Statistikwerte des aktuellen Spiels<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	public void setCurrentGameStatistics()
	{
		// TEXT
		// moves		
		movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_player_moves()));
		f1movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_moves()[0]));
		f2movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_moves()[1]));
		f3movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_moves()[2]));
		t1movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_moves()[3]));
		t2movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_moves()[4]));

		// kills		
		killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_player_damage()));
		f1killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[0]));
		f2killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[1]));
		f3killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[2]));
		t1killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[3]));
		t2killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[4]));

		// trades		
		tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_player_trades()));
		f1tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_trades()[0]));
		f2tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_trades()[1]));
		f3tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_trades()[2]));
		t1tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_trades()[3]));
		t2tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_trades()[4]));

		// playedTime
		playedTimeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_player_wastedTime()));


		// PANELS
		// moves
		int movesValue = game.statistics.getGame_player_moves();
		if(movesValue != 0)
		{
			int allf1Percent = (int) (game.statistics.getGame_units_moves()[0] / (float)movesValue * 100f);
			int allf2Percent = (int) (game.statistics.getGame_units_moves()[1] / (float)movesValue * 100f);
			int allf3Percent = (int) (game.statistics.getGame_units_moves()[2] / (float)movesValue * 100f);
			int allt1Percent = (int) (game.statistics.getGame_units_moves()[3] / (float)movesValue * 100f);
			int allt2Percent = (int) (game.statistics.getGame_units_moves()[4] / (float)movesValue * 100f);

			// Barriergröße setzen
			f1movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			f2movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			f3movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			t1movesbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			t2movesbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));	
		}
		else
		{
			f1movesbarrier.setConstraintWidth(SizeValue.percent(100));
			f2movesbarrier.setConstraintWidth(SizeValue.percent(100));
			f3movesbarrier.setConstraintWidth(SizeValue.percent(100));
			t1movesbarrier.setConstraintWidth(SizeValue.percent(100));
			t2movesbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		if(movesValue >= 1)
		{
			movesbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			movesbarrier.setConstraintWidth(SizeValue.percent(100));
		}


		// kills
		int killsValue = game.statistics.getGame_player_damage();
		if(killsValue != 0)
		{
			int allf1Percent = (int) (game.statistics.getGame_units_damage()[0] / (float)killsValue * 100f);
			int allf2Percent = (int) (game.statistics.getGame_units_damage()[1] / (float)killsValue * 100f);
			int allf3Percent = (int) (game.statistics.getGame_units_damage()[2] / (float)killsValue * 100f);
			int allt1Percent = (int) (game.statistics.getGame_units_damage()[3] / (float)killsValue * 100f);
			int allt2Percent = (int) (game.statistics.getGame_units_damage()[4] / (float)killsValue * 100f);

			// Barriergröße setzen
			f1killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			f2killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			f3killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			t1killsbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			t2killsbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));	
		}
		else
		{
			f1killsbarrier.setConstraintWidth(SizeValue.percent(100));
			f2killsbarrier.setConstraintWidth(SizeValue.percent(100));
			f3killsbarrier.setConstraintWidth(SizeValue.percent(100));
			t1killsbarrier.setConstraintWidth(SizeValue.percent(100));
			t2killsbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		if(killsValue >= 1)
		{
			killsbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			killsbarrier.setConstraintWidth(SizeValue.percent(100));
		}



		// trades
		int tradesValue = game.statistics.getGame_player_trades();
		if(tradesValue != 0)
		{
			int allf1Percent = (int) (game.statistics.getGame_units_trades()[0] / (float)tradesValue * 100f);
			int allf2Percent = (int) (game.statistics.getGame_units_trades()[1] / (float)tradesValue * 100f);
			int allf3Percent = (int) (game.statistics.getGame_units_trades()[2] / (float)tradesValue * 100f);
			int allt1Percent = (int) (game.statistics.getGame_units_trades()[3] / (float)tradesValue * 100f);
			int allt2Percent = (int) (game.statistics.getGame_units_trades()[4] / (float)tradesValue * 100f);

			// Barriergröße setzen
			f1tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			f2tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			f3tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			t1tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			t2tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));		
		}
		else
		{
			f1tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			f2tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			f3tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			t1tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			t2tradesbarrier.setConstraintWidth(SizeValue.percent(100));	
		}

		if(tradesValue >= 1)
		{
			tradesbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			tradesbarrier.setConstraintWidth(SizeValue.percent(100));
		}		
		// playedTime
		playedTime.setConstraintWidth(SizeValue.percent(75));
		screen.layoutLayers(); // WICHTIGSTER PART, OHNE DIES WERDEN DIE PROZENTE NICHT AKTUALISIERT
	}

	/**
	 * Diese Methode setzt die Statistikwerte aller bisherigen Spiele<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	public void setAllGamesStatistics()
	{
		// TEXT
		// moves		
		allmovesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_player_moves()));
		allf1movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_moves()[0]));
		allf2movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_moves()[1]));
		allf3movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_moves()[2]));
		allt1movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_moves()[3]));
		allt2movesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_moves()[4]));

		// kills		
		allkillsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_player_damage()));
		allf1killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[0]));
		allf2killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[1]));
		allf3killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[2]));
		allt1killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[3]));
		allt2killsText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getGame_units_damage()[4]));

		// trades		
		alltradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_player_trades()));
		allf1tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_trades()[0]));
		allf2tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_trades()[1]));
		allf3tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_trades()[2]));
		allt1tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_trades()[3]));
		allt2tradesText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_units_trades()[4]));

		// playedTime
		allplayedTimeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.statistics.getAll_player_wastedTime()));

		// PANELS
		// moves
		int movesValue = game.statistics.getAll_player_moves();
		if(movesValue != 0)
		{	
			int allf1Percent = (int) (game.statistics.getAll_units_moves()[0] / (float)movesValue * 100f);
			int allf2Percent = (int) (game.statistics.getAll_units_moves()[1] / (float)movesValue * 100f);
			int allf3Percent = (int) (game.statistics.getAll_units_moves()[2] / (float)movesValue * 100f);
			int allt1Percent = (int) (game.statistics.getAll_units_moves()[3] / (float)movesValue * 100f);
			int allt2Percent = (int) (game.statistics.getAll_units_moves()[4] / (float)movesValue * 100f);

			// Barriergröße setzen
			allf1movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			allf2movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			allf3movesbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			allt1movesbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			allt2movesbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));	

		}
		else
		{
			allf1movesbarrier.setConstraintWidth(SizeValue.percent(100));
			allf2movesbarrier.setConstraintWidth(SizeValue.percent(100));
			allf3movesbarrier.setConstraintWidth(SizeValue.percent(100));
			allt1movesbarrier.setConstraintWidth(SizeValue.percent(100));
			allt2movesbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		if(movesValue >= 1)
		{
			allmovesbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			allmovesbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		// kills
		int killsValue = game.statistics.getAll_player_damage();
		if(killsValue != 0)
		{			
			int allf1Percent = (int) (game.statistics.getGame_units_damage()[0] / (float)killsValue * 100f);
			int allf2Percent = (int) (game.statistics.getGame_units_damage()[1] / (float)killsValue * 100f);
			int allf3Percent = (int) (game.statistics.getGame_units_damage()[2] / (float)killsValue * 100f);
			int allt1Percent = (int) (game.statistics.getGame_units_damage()[3] / (float)killsValue * 100f);
			int allt2Percent = (int) (game.statistics.getGame_units_damage()[4] / (float)killsValue * 100f);

			// Barriergröße setzen
			allf1killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			allf2killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			allf3killsbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			allt1killsbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			allt2killsbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));	

		}
		else
		{
			allf1killsbarrier.setConstraintWidth(SizeValue.percent(100));
			allf2killsbarrier.setConstraintWidth(SizeValue.percent(100));
			allf3killsbarrier.setConstraintWidth(SizeValue.percent(100));
			allt1killsbarrier.setConstraintWidth(SizeValue.percent(100));
			allt2killsbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		if(killsValue >= 1)
		{
			allkillsbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			allkillsbarrier.setConstraintWidth(SizeValue.percent(100));
		}


		// trades
		int tradesValue = game.statistics.getAll_player_trades();
		if(tradesValue != 0)
		{
			int allf1Percent = (int) (game.statistics.getAll_units_trades()[0] / (float)tradesValue * 100f);
			int allf2Percent = (int) (game.statistics.getAll_units_trades()[1] / (float)tradesValue * 100f);
			int allf3Percent = (int) (game.statistics.getAll_units_trades()[2] / (float)tradesValue * 100f);
			int allt1Percent = (int) (game.statistics.getAll_units_trades()[3] / (float)tradesValue * 100f);
			int allt2Percent = (int) (game.statistics.getAll_units_trades()[4] / (float)tradesValue * 100f);

			// Barriergröße setzen
			allf1tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf1Percent));
			allf2tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf2Percent));
			allf3tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allf3Percent));
			allt1tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allt1Percent));
			allt2tradesbarrier.setConstraintWidth(SizeValue.percent(100 - allt2Percent));		
		}
		else
		{
			allf1tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			allf2tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			allf3tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			allt1tradesbarrier.setConstraintWidth(SizeValue.percent(100));
			allt2tradesbarrier.setConstraintWidth(SizeValue.percent(100));		
		}

		if(tradesValue >= 1)
		{
			alltradesbarrier.setConstraintWidth(SizeValue.percent(0));
		}
		else
		{
			alltradesbarrier.setConstraintWidth(SizeValue.percent(100));
		}

		// playedTime
		allplayedTime.setConstraintWidth(SizeValue.percent(75));
		screen.layoutLayers(); // WICHTIGSTER PART, OHNE DIES WERDEN DIE PROZENTE NICHT AKTUALISIERT
	}

	/**
	 * Diese Methode setzt den Sieger/Verlierer Text<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	public void setWinnerText(boolean wonTheGame)
	{
		if(wonTheGame)
		{
			winnerLooserText.getRenderer(TextRenderer.class).setColor(new Color(0, 1, 0, 1));
			winnerLooserText.getRenderer(TextRenderer.class).setText("You have Won!");
		}
		else
		{
			winnerLooserText.getRenderer(TextRenderer.class).setColor(new Color(1, 0, 0, 1));
			winnerLooserText.getRenderer(TextRenderer.class).setText("You have Lost!");
		}
	}

	/**
	 * Diese Methode wechselt zum CurrentGameStatisticsPanel<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	public void showCurrentStatistics()
	{
		allGamesStatisticsPanel.hide();
		currentGameStatisticsPanel.show();
	}

	/**
	 * Diese Methode wechselt zum AllGamesStatisticsPanel<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	public void showAllGamesStatistics()
	{
		currentGameStatisticsPanel.hide();
		allGamesStatisticsPanel.show();
	}

	/**
	 * Diese Methode öffnet das Statistik Popup am Ende des Spiels<br>
	 * 
	 * @param wonTheGame true für SIEG, false für NIEDERLAGE
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.01.2012<br>
	 */
	public void openStatisticsScreen(boolean wonTheGame)
	{
		game.gameStarted = false;
		setCurrentGameStatistics();
		setAllGamesStatistics();
		setWinnerText(wonTheGame);
		allGamesStatisticsPanel.hide();
		nifty.showPopup(screen, statisticsPopup.getId(), null);
	}

	/**
	 * Diese Methode öffnet das Tradestation Popup.
	 * 
	 * @author Markus Strobel<br>
	 * @since 11.01.2013<br>
	 */
	public void openTradeStationPopup(int stationsUnitID)
	{
		setTradeStationPopupSettings(stationsUnitID);
		
		// reset der Werte
		tradeStationBidGood1TextField.setText("0");
		tradeStationBidGood2TextField.setText("0");
		tradeStationBidGood3TextField.setText("0");
		tradeStationBidGood4TextField.setText("0");
		tradeStationBidGood5TextField.setText("0");
		
		tradeStationAskGood1TextField.setText("0");
		tradeStationAskGood2TextField.setText("0");
		tradeStationAskGood3TextField.setText("0");
		tradeStationAskGood4TextField.setText("0");
		tradeStationAskGood5TextField.setText("0");
		
		activeTradeStationOffer = true;
		calculateTradeStationMoneyValue(stationsUnitID);
		updateTradePopupValues();
		nifty.showPopup(screen, tradeStationPopup.getId(), null);
	}
	
	
	/**
	 * Diese Methode schließt das Tradestation Popup.
	 * 
	 * @author Markus Strobel<br>
	 * @since 11.01.2013<br>
	 */
	public void closeTradeStationPopup()
	{
		try 
		{
			activeTradeStationOffer = false;
			nifty.closePopup(tradeStationPopup.getId());
		} 
		catch (Exception e) 
		{
			// Nicht notwendig, da der try catch ausschließlich dafür ist, da das Popup oft auf Verdacht vom Thread geschlossen wird.
		}

	}

	/** 
	 * Diese Method fragt das ComObject_tradestations ab.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 */
	public void requestTradeStationsInformation()
	{
		game.networkThread.sendIComObject(new ComObject_tradestations(Game.clientID, Game.clientKey, Game.mapID));
	}
	
	/**
	 * Diese Methode visualisiert die Werte der tradestations
	 * 
	 * @param tradeStations das comObject_tradestations
	 * 
	 * @author Markus Strobel<br>
	 * @since 21.02.2013<br>
	 */
	public void storeTradeStationInformation(ComObject_tradestations tradeStations)
	{
		tradeStationIDs = tradeStations.getUnitID();
		tradeStationsRatiosList = tradeStations.getTradeequivalences();
		tradeStationAmounts = tradeStations.getAvailablegoods();
		
		// Dieser Abschnitt ändert den Wert -1, wenn eine tradestation quasi unendlich viele Einheiten im Angebot hat, auf 999999 sodass er unerreichbar hoch ist
		// entspricht zwar nicht unendlich, aber sollte ausreichen und ist ein schnellerer fix :)
		if(tradeStationAmounts != null)
		{
			for(int i = 0; i < tradeStationAmounts.size(); i++)
			{
				for(int j = 0; j < tradeStationAmounts.get(i).size(); j++)
				{
					if(tradeStationAmounts.get(i).get(j) == -1)
					{
						tradeStationAmounts.get(i).set(j, 999999);
					}
				}
			}
		}
	}
	
	/**
	 * Diese Methode setzt die Ratio und Amount Werte der Tradestation, also in welchem Verhältnis die Waren eingetauscht werden können.<br>
	 * 
	 * @param stationsUnitID Die UnitID der aktuellen Handelsstation.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 11.01.2013<br>
	 * 
	 */
	public void setTradeStationPopupSettings(int stationsUnitID)
	{
		currentTradeStationID = stationsUnitID;
		int unitID = tradeStationIDs.indexOf(stationsUnitID);
		
		good1ratio.getRenderer(TextRenderer.class).setText("  " + String.valueOf(tradeStationsRatiosList.get(unitID).get(0)) + ":1");
		good2ratio.getRenderer(TextRenderer.class).setText("  " + String.valueOf(tradeStationsRatiosList.get(unitID).get(1)) + ":1");
		good3ratio.getRenderer(TextRenderer.class).setText("  " + String.valueOf(tradeStationsRatiosList.get(unitID).get(2)) + ":1");
		good4ratio.getRenderer(TextRenderer.class).setText("  " + String.valueOf(tradeStationsRatiosList.get(unitID).get(3)) + ":1");
		good5ratio.getRenderer(TextRenderer.class).setText("  " + String.valueOf(tradeStationsRatiosList.get(unitID).get(4)) + ":1");

		good1amount.getRenderer(TextRenderer.class).setText("      " + String.valueOf(tradeStationAmounts.get(unitID).get(0)));
		good2amount.getRenderer(TextRenderer.class).setText("      " + String.valueOf(tradeStationAmounts.get(unitID).get(1)));
		good3amount.getRenderer(TextRenderer.class).setText("      " + String.valueOf(tradeStationAmounts.get(unitID).get(2)));
		good4amount.getRenderer(TextRenderer.class).setText("      " + String.valueOf(tradeStationAmounts.get(unitID).get(3)));
		good5amount.getRenderer(TextRenderer.class).setText("      " + String.valueOf(tradeStationAmounts.get(unitID).get(4)));
	}
	
	/**
	 * Diese Methode berechnet den Wert des Handelsvolumens mit einer TradeStation<br>
	 * 
	 * @param ratios der Kurs des Handelsguts bzw Verhältnis mit dem eine Währungseinheit bekommen werden kann.<br>
	 * @return der Wert des Handelsvolumens
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 */
	public void calculateTradeStationMoneyValue(int stationsUnitID)
	{
		try 
		{
			int unitID = tradeStationIDs.indexOf(stationsUnitID);
			
			float money = 0f;

			float value1 = Float.parseFloat(tradeStationBidGood1TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(0);
			float value2 = Float.parseFloat(tradeStationBidGood2TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(1);
			float value3 = Float.parseFloat(tradeStationBidGood3TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(2);
			float value4 = Float.parseFloat(tradeStationBidGood4TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(3);
			float value5 = Float.parseFloat(tradeStationBidGood5TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(4);

			float cost1 = Float.parseFloat(tradeStationAskGood1TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(0);
			float cost2 = Float.parseFloat(tradeStationAskGood2TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(1);
			float cost3 = Float.parseFloat(tradeStationAskGood3TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(2);
			float cost4 = Float.parseFloat(tradeStationAskGood4TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(3);
			float cost5 = Float.parseFloat(tradeStationAskGood5TextField.getRealText()) / tradeStationsRatiosList.get(unitID).get(4);

			money = (value1 + value2 + value3 + value4 + value5) - (cost1 + cost2 + cost3 + cost4 + cost5);
			
			// Färbung des moneyValue Textes
			if(money > 0)
			{
				moneyValue.getRenderer(TextRenderer.class).setColor(new Color(0f, 1f, 0f, 1f));
			}
			else if(money < 0)
			{
				moneyValue.getRenderer(TextRenderer.class).setColor(new Color(1f, 0f, 0f, 1f));
			}
			else
			{
				moneyValue.getRenderer(TextRenderer.class).setColor(new Color(1f, 1f, 1f, 1f));
			}
			
			int integerValue = (int) (money);
			int floatingValue = (int) (money * 100) % 100;			

			String formatedMoneyValue = String.valueOf(integerValue) + "." + String.valueOf(floatingValue).replace("-", "") ;

			// Setzen des moneyValue Textes
			moneyValue.getRenderer(TextRenderer.class).setText(formatedMoneyValue);
			
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisGameScreenController.calculateTradeStationMoneyValue() catch: " + e.getMessage());
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

	/**
	 * Diese Methode überprüft die Spieler, welche dem Spiel beigetreten sind.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.12.2012<br>
	 */
	public void getConnectedPlayers()
	{
		// FÜR DIE CONNECTED PLAYERS LISTE
		game.networkThread.sendIComObject(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
	}

	/**
	 * Diese Methode handelt das ComObject_gameinfo ab, welches vom Server übergeben wird.<br>
	 * 
	 * @param gameInfo das ComObject_gameinfo
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br>
	 */
	public void getConnectedPlayersSended(ComObject_gameinfo gameInfo)
	{
		if(!connectedPlayersGameInfoHandled){

			if(gameInfo.getStatus().equals(STATUS.OK))
			{				
				// Liste der Spieler empfangen und sie der Listbox hinzufügen				
				connectedPlayersIDs = gameInfo.getPlayerIDs();
				connectedPlayerNames = gameInfo.getPlayerNames();
				
				// Spieler die connected sind, aber noch nicht in der Listbox sind, werden hier hinzugefügt
				for(int i = 0; i < connectedPlayerNames.size(); i++)
				{						
					// Wenn ein Spieler der connectedPlayers-Liste nicht in der receiverDropDown ist, dann wird er hinzugefügt
					if(!receiverDropDown.getItems().contains(connectedPlayerNames.get(i)) && !connectedPlayerNames.get(i).equals(Game.playerName))
					{
						receiverDropDown.addItem(connectedPlayerNames.get(i));
					}					
				}
			}
			else
			{
				Game.logger.error("SepsisGameScreenController.getConnectedPlayersSended().getClients: " + gameInfo.getErrorInfo());
			}			
			connectedPlayersGameInfoHandled = true;
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
	 * Diese Methode entfernt den Focus vom ChatTextField
	 * 
	 * @author Markus Strobel<br>
	 * @since 18.01.2013<br>
	 */
	public void removeFocusFromTextField()
	{
		chatSendTextField.disable();
		chatSendTextField.enable();
		screen.setDefaultFocus();
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

		String chatMessage = chatSendTextField.getRealText();	

		if(!chatMessage.isEmpty())
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
				privateChatText = chatMessage;
			}
			game.networkThread.sendIComObject((new ComObject_chat(Game.clientID, Game.clientKey, Game.playerID, receiverGameID, receiverPlayerID, chatMessage)));
		}

	}

	/**
	 * Diese Methode wird aufgerufen, wenn die Chatnachricht abgesendet wurde und löscht den Inhalt des Textfeldes.<br>
	 * 
	 * @param chat das ComObject_chat welches die Antwort enthält, ob die Chatnachricht abgesendet wurde.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br>
	 */
	public void sendChatMessageSended(ComObject_chat chat)
	{
		if(chat.getStatus().equals(STATUS.OK))
		{
			if(privateChat)
			{
				chatReceiveListBox.addItem("ME: " + privateChatText);
				privateChatText = "";
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
			Game.logger.error("SepsisGameScreenController.chatMessageSended.ComObject_chat: " + chat.getErrorInfo());
		}
	}

	/**
	 * Diese Methode fragt die Chatnachrichten vom Server ab<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 20.12.2012<br>
	 * 
	 * 05.02.2013 (Markus Strobel> An neue Netzwerkstruktur angepasst.<br>
	 */
	private void getChatMessages()
	{
		game.networkThread.sendIComObject(new ComObject_getData(Game.clientID, Game.clientKey));
	}

	/**
	 * Diese Methode füllt das Chatfenster mit den Nachrichten, welche vom ComObject_getData vom Server empfangen werden.<br>
	 * @param getData das ComObject_getData, welches die Chatnachrichten beinhaltet.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br>
	 */
	public void getChatMessagesSended(ComObject_getData getData)
	{
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
							boolean sendmessage = false;

							if(getData.getDatatype().get(i).equals(DATATYPE.SYSTEM))
							{
								sendmessage = true;

								String finalMessage = getData.getMessages().get(i);

								if(sendmessage)
								{
									
									// Hinzufügen der Chat
									chatReceiveListBox.addItem(finalMessage);
									chatReceiveListBox.refresh();

									// Unterstes Element auswählen
									chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
									chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);	
									
									
									// funktioniert noch nicht 100% korrekt
//									if(finalMessage.contains("attacked"))
//									{ 
//										int dmg = calculateReceivedDamage();										
//										finalMessage = dmg + " damage received";
//										
//										// Hinzufügen der Chat
//										chatReceiveListBox.addItem(finalMessage);
//										chatReceiveListBox.refresh();
//
//										// Unterstes Element auswählen
//										chatReceiveListBox.selectItemByIndex(chatReceiveListBox.getItems().size() - 1);
//										chatReceiveListBox.deselectItemByIndex(chatReceiveListBox.getItems().size() - 1);	
//									}		
								}
							}
							else
							{
								// Empfänger == mein Name oder == null, dann kann ich die Nachricht bekommen
								if(getData.getPlayeridreciever().get(i).equals(Game.playerID) || getData.getPlayeridreciever().get(i).equals(""))
								{						
									// GameID == mein Spiel oder == null, dann kann ich die Nachricht bekommen
									if(getData.getGameID().get(i).equals(Game.gameID) || getData.getGameID().get(i).equals(""))
									{										
										String finalMessage = "";
										//									String timeStamp = "";
										//									String timeStamp = getData.getIncommingTime().get(i);
										//									String SystemInfo = getData.getDatatype().get(i).toString();
										String senderName = "";		
										
										
										if(!getData.getSenderplayerid().get(i).equals(""))
										{
											int playerNameIndex = connectedPlayersIDs.indexOf(getData.getSenderplayerid().get(i));
																						
											senderName = connectedPlayerNames.get(playerNameIndex);
										}
										else
										{
											senderName = getData.getSenderplayerid().get(i);
										}

										// String receiverName = getData.getPlayeridreciever().get(i);
										String messageContent = getData.getMessages().get(i);
										messageContent = messageContent.replace("Chat message:", "");
										
										if(messageContent.contains("_trade_"))
										{									
											
											if(connectedPlayersIDs.contains(getData.getSenderplayerid().get(i)))
											{
												int index = connectedPlayersIDs.indexOf(getData.getSenderplayerid().get(i));
												senderName = connectedPlayerNames.get(index);
												
												// BEIM TRADE REPLY WIRD DER SENDER NICHT ANGEZEIGT, DA DER SERVER KEINE Senderplayerid mitschickt...
											}
											else
											{
												senderName = getData.getSenderplayerid().get(i);	
											}											
																		
											messageContent = messageContent.replace("_trade_message_", "");			
										}

																				
										
										if(getData.getSenderplayerid().get(i).equals(Game.playerID))
										{
											senderName = "ME";
										}

										sendmessage = false;

										if(getData.getPlayeridreciever().get(i).equals("") && getData.getGameID().get(i).equals(""))
										{
											finalMessage = senderName + ": " + messageContent;
											sendmessage = true;
										}
										else if(getData.getGameID().get(i).equals(Game.gameID) && getData.getPlayeridreciever().get(i).equals(""))
										{											
											finalMessage = senderName + ": " + messageContent;
											sendmessage = true;
										}
										else if(getData.getGameID().get(i).equals(Game.gameID) && getData.getPlayeridreciever().get(i).equals(Game.playerID))
										{
											finalMessage = senderName + ": " + messageContent;
											sendmessage = true;
										}

										// Wenn die Nachricht leer ist, wird sie nicht im Chat angezeigt.
										if(messageContent.equals(""))
										{
											sendmessage = false;
										}
										
										if(sendmessage)
										{
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
				}
			}
			else
			{
				Game.logger.error("SepsisGameScreenController.getChatMessageSended.ComObject_getData: " + getData.getErrorInfo());
			}
		}
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
		openWindow = true;
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
		openWindow = false;
	}

	/**
	 * Diese Methode lässt das Info-Panel mit den Einheiten- bzw. Feld-Informationen erscheinen.
	 * 
	 * @author Markus Strobel<br>
	 * @since 17.12.2012<br>
	 */
	public void showFieldUnitInformationPanel()
	{
		if(!unitFieldInformationPanel.isVisible())
		{
			unitFieldInformationPanel.show();
		}
	}

	/**
	 * Diese Methode lässt das Info-Panel mit den Einheiten- bzw. Feld-Informationen verschwinden.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 17.12.2012<br>
	 */
	public void hideFieldUnitInformationPanel()
	{
		if(unitFieldInformationPanel.isVisible())
		{
			unitFieldInformationPanel.hide();
		}
	}

	/**
	 * Diese Methode aktualisiert die Werte des Unit Information Panels.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 17.12.2012<br>
	 */
	public void setUnitInformation()
	{
		if(game.selectedUnit != null)
		{
			unitInfo_IDText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.ID));
			unitInfo_TypeText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.type));
			unitInfo_HPText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.hitpoints_curr) + "/" + String.valueOf(game.selectedUnit.hitpoints_max));
			unitInfo_APText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.attackPower));
			unitInfo_MPText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.movement_curr)  + "/" + String.valueOf(game.selectedUnit.movement_max));
			positionXText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.x));
			positionYText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.y));
			
			// Für die Tradestations notwendige Abfrage
			if(game.selectedUnit.ownerID != null)
			{
				if(game.selectedUnit.ownerID.equals(Game.playerID))
				{ // freundliche Einheit - Cargo bekannt
					unitInfo_cargo1Text.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_1));
					unitInfo_cargo2Text.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_2));
					unitInfo_cargo3Text.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_3));
					unitInfo_cargo4Text.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_4));
					unitInfo_cargo5Text.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_5));	
				
					int totalCargo = game.selectedUnit.cargo_1 + game.selectedUnit.cargo_2 + game.selectedUnit.cargo_3 + game.selectedUnit.cargo_4 + game.selectedUnit.cargo_5;
					unitInfo_CargoStatusText.getRenderer(TextRenderer.class).setText(String.valueOf(totalCargo) + "/" + String.valueOf(game.selectedUnit.cargo_max));	
				}
				else
				{ // feindliche Einheit - Cargo unbekannt
					unitInfo_cargo1Text.getRenderer(TextRenderer.class).setText("?");
					unitInfo_cargo2Text.getRenderer(TextRenderer.class).setText("?");
					unitInfo_cargo3Text.getRenderer(TextRenderer.class).setText("?");
					unitInfo_cargo4Text.getRenderer(TextRenderer.class).setText("?");
					unitInfo_cargo5Text.getRenderer(TextRenderer.class).setText("?");					
					unitInfo_CargoStatusText.getRenderer(TextRenderer.class).setText("?" + "/" + String.valueOf(game.selectedUnit.cargo_max));	
				}
			}
		}
		else
		{
			unitInfo_IDText.getRenderer(TextRenderer.class).setText("-");
			unitInfo_TypeText.getRenderer(TextRenderer.class).setText("-");
			unitInfo_HPText.getRenderer(TextRenderer.class).setText("-");
			unitInfo_APText.getRenderer(TextRenderer.class).setText("-");
			unitInfo_MPText.getRenderer(TextRenderer.class).setText("-");
			unitInfo_cargo1Text.getRenderer(TextRenderer.class).setText("-");
			unitInfo_cargo2Text.getRenderer(TextRenderer.class).setText("-");
			unitInfo_cargo3Text.getRenderer(TextRenderer.class).setText("-");
			unitInfo_cargo4Text.getRenderer(TextRenderer.class).setText("-");
			unitInfo_cargo5Text.getRenderer(TextRenderer.class).setText("-");	
		}

	}

	/**
	 * Diese Methode aktualisiert die Werte des Field Information Panels.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 17.12.2012<br>
	 */
	public void setFieldInformation()
	{
		if(game.selectedFieldPoint != null)
		{
			positionXText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedFieldPoint.x));
			positionYText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedFieldPoint.y));
		}
	}

	/**
	 * Diese Methode aktualisiert die Werte des Top Panels.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 17.12.2012<br>
	 */
	public void setTopPanelInformation()
	{
		// RundenNummer
		roundNumberText.getRenderer(TextRenderer.class).setText(String.valueOf(game.RoundNumber));

		// Spieler der an der Reihe ist
		if(game.currentTurn.equals(TURN.PLAYER))
		{
			currentTurnText.getRenderer(TextRenderer.class).setColor(new de.lessvoid.nifty.tools.Color(0f, 1f, 0f, 1f));
		}
		else
		{
			currentTurnText.getRenderer(TextRenderer.class).setColor(new de.lessvoid.nifty.tools.Color(1f, 0f, 0f, 1f));
		}

		currentTurnText.getRenderer(TextRenderer.class).setText(game.currentTurn.toString());

		//		// Einheiten die noch bewegt werden können
		//		menuInfo_movableUnitIDsList.clear();
		//		for(int i = 0; i  < Game.playingField.units.size(); i++)
		//		{
		//			// fügt der menuInfo_movableUnitIDsList die IDs hinzu von Spieler 0, welche doneThisRound = false sind
		//			if(Game.playingField.units.get(i).doneThisRound == false)
		//			{
		//				if(Game.playingField.units.get(i).ownerID != null)
		//				{
		//					if(Game.playingField.units.get(i).ownerID.equals(Game.playerID))
		//					{
		//						menuInfo_movableUnitIDsList.add(Game.playingField.units.get(i).ID);
		//					}
		//				}
		//			}
		//		}		
		//		turnsTakenListText.getRenderer(TextRenderer.class).setText(menuInfo_movableUnitIDsList.toString());
	}

	/**
	 * Diese Methode beendet das Spiel.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.12.2012<br>
	 */
	public void exitGame()
	{
		game.stop();
	}
	

	/**
	 * Diese Methode öffnet das Fenster, welches eingehende Handelsangebote anzeigt.
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void openGetTradeOfferPopup(ArrayList<Integer> offer, ArrayList<Integer> request)
	{
		activeGetTradeOffer = true;
		getTradePopupVisible = true;
		Game.logger.debug("SepsisGameScreenController.openGetTradeOfferPopup: getTradeOffer Fenster geöffnet");
		setGetTradeOffer(offer, request);		
		nifty.showPopup(screen, getTradePopup.getId(), null);
	}

	/**
	 * Diese Methode schließt das Fenster, welches eingehende Handelsangebote anzeigt.
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void closeGetTradeOfferPopup()
	{
		if(getTradePopupVisible){
			Game.logger.debug("SepsisGameScreenController.closeGetTradeOfferPopup: getTradeOffer Fenster geschlossen");
			nifty.closePopup(getTradePopup.getId());
			activeGetTradeOffer = false;
			getTradePopupVisible = false;
		}
	}

	/**
	 * Diese Methode öffnet das Fenster, welches eigene Handelsangebote anzeigt.
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void openMakeTradeOfferPopup() 
	{

		activeMakeTradeOffer = true;
		Game.logger.debug("SepsisGameScreenController.openMakeTradeOfferPopup: makeTradeOffer Fenster geöffnet");

		makeTradeAskGood1TextField.setText("0");	
		makeTradeAskGood2TextField.setText("0");	
		makeTradeAskGood3TextField.setText("0");	
		makeTradeAskGood4TextField.setText("0");	
		makeTradeAskGood5TextField.setText("0");	
		makeTradeBidGood1TextField.setText("0");
		makeTradeBidGood2TextField.setText("0");
		makeTradeBidGood3TextField.setText("0");
		makeTradeBidGood4TextField.setText("0");
		makeTradeBidGood5TextField.setText("0");
		
		updateTradePopupValues();

		sendTradeMessageTextField.setText("");

		nifty.showPopup(screen, makeTradePopup.getId(), null);
	}

	/**
	 * Diese Methode schließt das Fenster, welches eigene Handelsangebote anzeigt.
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void closeMakeTradeOfferPopup()
	{
		try 
		{
			Game.logger.debug("SepsisGameScreenController.closeMakeTradeOfferPopup: makeTradeOffer Fenster geschlossen");
			nifty.closePopup(makeTradePopup.getId());
			activeMakeTradeOffer = false;
		} 
		catch (Exception e)
		{

		}

	}

	/**
	 * Diese Methode öffnet das Einstellungsfenster.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>	 *
	 */
	@SuppressWarnings("unchecked")
	public void openSettingsPopup()
	{
		activeSettingsPopup = true;		
		this.settingsPopup = nifty.createPopup("settingsPopup");
		openWindow = true;
		
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
		serverConnectionTestButton.hide();

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

		// SepsisAppSettings laden
		resolution.selectItem(game.sepsisAppSettings.resolution);
		colorDepth.selectItem(game.sepsisAppSettings.colordepth);
		antiAliasing.selectItem(game.sepsisAppSettings.multisample);
		fullScreenCheckBox.setChecked(game.sepsisAppSettings.fullscreen);
		vSyncCheckBox.setChecked(game.sepsisAppSettings.vsync);
		language.selectItem(game.sepsisAppSettings.language);
		style.selectItem(game.sepsisAppSettings.style);
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
		openWindow = false;
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
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void openCreditsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		nifty.showPopup(screen, creditsPopup.getId(), null);
		openWindow = true;
	}

	/** Diese Methode schließt das Credits-Fenster.<br>
	 * @author Markus Strobel<br>
	 * @since 29.11.2012<br>
	 */
	public void closeCreditsPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		nifty.closePopup(creditsPopup.getId());
		openWindow = false;
	}	

	/**
	 * Diese Methode öffnet bzw schließt das Menu-Fenster.
	 * @author Markus Strobel
	 * @since 29.11.2012
	 */
	public void toggleMenu()
	{
		if(!openWindow)
		{
			if(menuPopupState)
			{
				nifty.showPopup(screen, menuPopup.getId(), null);
				menuPopupState = !menuPopupState;
			}
			else
			{
				nifty.closePopup(menuPopup.getId());
				menuPopupState = !menuPopupState;
			}
		}
	}

	/**
	 * Diese Methode öffnet bzw schließt das Kampf- und Handels-Menu.
	 * @author Markus Strobel
	 * @since 29.11.2012
	 */
	public void toggleTradeFightMenu()
	{		
		if(tradeFightPopupState)
		{
			nifty.showPopup(screen, tradeFightPopup.getId(), null);	
			tradeFightPopupState = !tradeFightPopupState;
		}
		else
		{
			nifty.closePopup(tradeFightPopup.getId());
			tradeFightPopupState = !tradeFightPopupState;
		}
	}
	
	/**
	 * Gibt an, ob das TradeFightMenu sichtbar ist.
	 * 
	 * @return True, wenn das Menü sichtbar ist, ansonsten false.
	 * 
	 * @author Peter Dörr
	 * @since 07.02.13
	 */
	public boolean getTradeFightMenuState(){
		return tradeFightPopupState;
	}

	/**
	 * Diese Methode übergibt den Fight-Status des Kampf und Handel Interaktions-Menus<br>
	 * @author Markus Strobel<br>
	 * @since 04.12.2012<br>
	 */
	public void fightButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_FightTradeFollowIntercept(INTERACTIONS.FIGHT);
	}

	/**
	 * Diese Methode übergibt den Trade-Status des Kampf und Handel Interaktions-Menus<br>
	 * @author Markus Strobel<br>
	 * @since 04.12.2012<br>
	 */
	public void tradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_FightTradeFollowIntercept(INTERACTIONS.TRADE);
	}

	/**
	 * Diese Methode übergibt den Follow-Status des Kampf und Handel Interaktions-Menus<br>
	 * @author Markus Strobel<br>
	 * @since 07.12.2012<br>
	 */
	public void followButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_FightTradeFollowIntercept(INTERACTIONS.FOLLOW);
	}

	/**
	 * Diese Methode übergibt den Intercept-Status des Kampf und Handel Interaktions-Menus<br>
	 * @author Markus Strobel<br>
	 * @since 07.12.2012<br>
	 */
	public void interceptButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_FightTradeFollowIntercept(INTERACTIONS.INTERCEPT);
	}

	/**
	 * Diese Methode übergibt den Abort-Status des Kampf und Handel Interaktions-Menus<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 04.12.2012<br>
	 */
	public void abortButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_FightTradeFollowIntercept(INTERACTIONS.ABORT);
	}	

	/**
	 * Diese Methode nimmt den Handel an.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void acceptTradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		String message = getTradeMessageTextField.getRealText();
		
		game.buttonClick_TradeReply(TRADE_REPLY.ACCEPT, message);
	}

	/**
	 * Diese Methode lehnt den Handel ab.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void rejectTradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		
		String message = getTradeMessageTextField.getRealText();

		game.buttonClick_TradeReply(TRADE_REPLY.REJECT, message);
	}

	/**
	 * Diese Methode ignoriert den Handelspartner.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void dndTradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		String message = getTradeMessageTextField.getRealText();
		
		game.buttonClick_TradeReply(TRADE_REPLY.DND, message);
	}

	/**
	 * Diese Methode aktualisiert alle Werte der selectedUnit im DebugPanel.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.12.2012<br>
	 */
	public void updateDebugPanel()
	{		
		if(game.selectedUnit != null)
		{
			// SelectedUnit Values
			debugSelectedUnitID.getRenderer(TextRenderer.class).setText("Unit ID: " + String.valueOf(game.selectedUnit.ID));
			debugDoneThisRound.getRenderer(TextRenderer.class).setText("doneThisRound: " + String.valueOf(game.selectedUnit.doneThisRound));
			debugType.getRenderer(TextRenderer.class).setText("type: " + String.valueOf(game.selectedUnit.type));
			debugOwner.getRenderer(TextRenderer.class).setText("owner: " + String.valueOf(game.selectedUnit.ownerID));
			debugXpos.getRenderer(TextRenderer.class).setText("X: " + String.valueOf(game.selectedUnit.x));
			debugYpos.getRenderer(TextRenderer.class).setText("Y: " + String.valueOf(game.selectedUnit.y));
			debugHitpoints_curr.getRenderer(TextRenderer.class).setText("HP_cur: " + String.valueOf(game.selectedUnit.hitpoints_curr));
			debugHitpoints_max.getRenderer(TextRenderer.class).setText("HP_max: " + String.valueOf(game.selectedUnit.hitpoints_max));
			debugMovement_curr.getRenderer(TextRenderer.class).setText("MOV_cur: " + String.valueOf(game.selectedUnit.movement_curr));
			debugMovement_max.getRenderer(TextRenderer.class).setText("MOV_max: " + String.valueOf(game.selectedUnit.movement_max));
			debugAttackPower.getRenderer(TextRenderer.class).setText("attackPower: " + String.valueOf(game.selectedUnit.attackPower));
			debugCargo_1.getRenderer(TextRenderer.class).setText("cargo_1: " + String.valueOf(game.selectedUnit.cargo_1));
			debugCargo_2.getRenderer(TextRenderer.class).setText("cargo_2: " + String.valueOf(game.selectedUnit.cargo_2));
			debugCargo_3.getRenderer(TextRenderer.class).setText("cargo_3: " + String.valueOf(game.selectedUnit.cargo_3));
			debugCargo_4.getRenderer(TextRenderer.class).setText("cargo_4: " + String.valueOf(game.selectedUnit.cargo_4));
			debugCargo_5.getRenderer(TextRenderer.class).setText("cargo_5: " + String.valueOf(game.selectedUnit.cargo_5));
			debugIsActive.getRenderer(TextRenderer.class).setText("isActive: " + String.valueOf(game.selectedUnit.isActive));
			debugWayPointsList.getRenderer(TextRenderer.class).setText("wayPointsList: " + String.valueOf(game.selectedUnit.wayPointsList));
		}
		else
		{
			// NO UNIT SELECTED Values
			debugSelectedUnitID.getRenderer(TextRenderer.class).setText("----");
			debugDoneThisRound.getRenderer(TextRenderer.class).setText("----");
			debugType.getRenderer(TextRenderer.class).setText("----");
			debugOwner.getRenderer(TextRenderer.class).setText("----");
			debugXpos.getRenderer(TextRenderer.class).setText("----");
			debugYpos.getRenderer(TextRenderer.class).setText("----");
			debugHitpoints_curr.getRenderer(TextRenderer.class).setText("----");
			debugHitpoints_max.getRenderer(TextRenderer.class).setText("----");
			debugMovement_curr.getRenderer(TextRenderer.class).setText("----");
			debugMovement_max.getRenderer(TextRenderer.class).setText("----");
			debugAttackPower.getRenderer(TextRenderer.class).setText("----");
			debugCargo_1.getRenderer(TextRenderer.class).setText("----");
			debugCargo_2.getRenderer(TextRenderer.class).setText("----");
			debugCargo_3.getRenderer(TextRenderer.class).setText("----");
			debugCargo_4.getRenderer(TextRenderer.class).setText("----");
			debugCargo_5.getRenderer(TextRenderer.class).setText("----");
			debugIsActive.getRenderer(TextRenderer.class).setText("----");
			debugWayPointsList.getRenderer(TextRenderer.class).setText("----");

		}
		// Misc Values
		debugRoundNumber.getRenderer(TextRenderer.class).setText("roundNumber: " + String.valueOf(game.RoundNumber));
		debugCurrentTurn.getRenderer(TextRenderer.class).setText("currentTurn: " + String.valueOf(game.currentTurn));
		debugCurrentState.getRenderer(TextRenderer.class).setText("currentState: " + String.valueOf(game.currentState));

		// turnsTakenList	[[true],[false],[...],...] etc
		ArrayList<Boolean> turnsTakenList = new ArrayList<Boolean>();
		for(int i = 0; i < Game.playingField.units.size(); i++)
		{
			turnsTakenList.add(Game.playingField.units.get(i).doneThisRound);
		}			
		debugTurnsTaken.getRenderer(TextRenderer.class).setText("turnsTaken: " + String.valueOf(turnsTakenList));

	}

	/**
	 * Diese Methode speichert das Handelsangebot und sendet es weiter an die game-Klasse.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void offerTradeButtonClicked()
	{
		try {
			game.audio.playSound(SOUND.MENU_CLICK, null);

			TradeInfo tradeInfo = new TradeInfo();
			ArrayList<Integer> offer = new ArrayList<Integer>();
			ArrayList<Integer> request = new ArrayList<Integer>();

			offer.add(Integer.parseInt(makeTradeBidGood1TextField.getRealText()));
			offer.add(Integer.parseInt(makeTradeBidGood2TextField.getRealText()));
			offer.add(Integer.parseInt(makeTradeBidGood3TextField.getRealText()));
			offer.add(Integer.parseInt(makeTradeBidGood4TextField.getRealText()));
			offer.add(Integer.parseInt(makeTradeBidGood5TextField.getRealText()));

			request.add(Integer.parseInt(makeTradeAskGood1TextField.getRealText()));
			request.add(Integer.parseInt(makeTradeAskGood2TextField.getRealText()));
			request.add(Integer.parseInt(makeTradeAskGood3TextField.getRealText()));
			request.add(Integer.parseInt(makeTradeAskGood4TextField.getRealText()));
			request.add(Integer.parseInt(makeTradeAskGood5TextField.getRealText()));

			tradeInfo.offer = offer;
			tradeInfo.request = request;
			// Die Handelsnachricht vom Anbieter an den Handelspartner
			tradeInfo.message = sendTradeMessageTextField.getRealText();

			game.buttonClick_TradeOffer(tradeInfo);
			closeMakeTradeOfferPopup();
		} 
		catch (Exception e) 
		{
			// Damit es nicht zu Fehlern kommt, falls das Window auf Verdacht vom Thread geschlossen wird.
		}
	}

	/**
	 * Diese Methode schließt das Handelfenster und sendet dies der game-Klasse.<br>
	 * 
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void cancelTradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_TradeOffer(null);
		closeMakeTradeOfferPopup();
	}

	/**
	 * Diese Methode speichert das Handelsangebot an die Tradestation und sendet es weiter an die game-Klasse.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 11.01.2013<br>
	 */
	public void offerTradeStationTradeButtonClicked()
	{
		try {
			game.audio.playSound(SOUND.MENU_CLICK, null);

			TradeInfo tradeInfo = new TradeInfo();
			ArrayList<Integer> offer = new ArrayList<Integer>();
			ArrayList<Integer> request = new ArrayList<Integer>();

			offer.add(Integer.parseInt(tradeStationBidGood1TextField.getRealText()));
			offer.add(Integer.parseInt(tradeStationBidGood2TextField.getRealText()));
			offer.add(Integer.parseInt(tradeStationBidGood3TextField.getRealText()));
			offer.add(Integer.parseInt(tradeStationBidGood4TextField.getRealText()));
			offer.add(Integer.parseInt(tradeStationBidGood5TextField.getRealText()));

			request.add(Integer.parseInt(tradeStationAskGood1TextField.getRealText()));
			request.add(Integer.parseInt(tradeStationAskGood2TextField.getRealText()));
			request.add(Integer.parseInt(tradeStationAskGood3TextField.getRealText()));
			request.add(Integer.parseInt(tradeStationAskGood4TextField.getRealText()));
			request.add(Integer.parseInt(tradeStationAskGood5TextField.getRealText()));

			tradeInfo.offer = offer;
			tradeInfo.request = request;

			game.buttonClick_TradeOffer(tradeInfo);
			closeTradeStationPopup();
		} 
		catch (Exception e) 
		{
			// Damit es nicht zu Fehlern kommt, falls das Window auf Verdacht vom Thread geschlossen wird.
		}
	}

	/**
	 * Diese Methode schließt das Handelfenster der Tradestation und sendet dies der game-Klasse.<br>
	 * 
	 * 
	 * @author Markus Strobel<br>
	 * @since 11.01.2013<br>
	 */
	public void cancelTradeStationTradeButtonClicked()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		game.buttonClick_TradeOffer(null);
		closeTradeStationPopup();
	}

	/**
	 * Diese Methode setzt die Werte eines Handels, welcher ein Spieler angeboten bekommt.<br>
	 * 
	 * @param offer Das Angebot des Spielers der den Handel erfragt.<br>
	 * @param request Die Forderung des Spielers der den Handel erfragt.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.12.2012<br>
	 */
	public void setGetTradeOffer(ArrayList<Integer> offer, ArrayList<Integer> request)
	{
		if(offer != null && request != null)
		{
			getTradeAskGood1TextField.setText(String.valueOf(offer.get(0)));
			getTradeAskGood2TextField.setText(String.valueOf(offer.get(1)));
			getTradeAskGood3TextField.setText(String.valueOf(offer.get(2)));
			getTradeAskGood4TextField.setText(String.valueOf(offer.get(3)));
			getTradeAskGood5TextField.setText(String.valueOf(offer.get(4)));

			getTradeBidGood1TextField.setText(String.valueOf(request.get(0)));
			getTradeBidGood2TextField.setText(String.valueOf(request.get(1)));
			getTradeBidGood3TextField.setText(String.valueOf(request.get(2)));
			getTradeBidGood4TextField.setText(String.valueOf(request.get(3)));
			getTradeBidGood5TextField.setText(String.valueOf(request.get(4)));
		}
	}

	// Ask-methods	
	/**
	 * Diese Methode erhöht die (cargo1) Ask-Adenin-Menge um 1.
	 */
	public void incrementAskAdenin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood1TextField.getRealText());
			value += tradeValue;
			tradeStationAskGood1TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood1TextField.getRealText());
			value += tradeValue;
			makeTradeAskGood1TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo2) Ask-Cytosin-Menge um 1.
	 */
	public void incrementAskCytosin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood2TextField.getRealText());
			value += tradeValue;
			tradeStationAskGood2TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood2TextField.getRealText());
			value += tradeValue;
			makeTradeAskGood2TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo3) Ask-Guanin-Menge um 1.
	 */
	public void incrementAskGuanin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood3TextField.getRealText());
			value += tradeValue;
			tradeStationAskGood3TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood3TextField.getRealText());
			value += tradeValue;
			makeTradeAskGood3TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo4) Ask-Thymin-Menge um 1.
	 */
	public void incrementAskThymin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood4TextField.getRealText());
			value += tradeValue;
			tradeStationAskGood4TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood4TextField.getRealText());
			value += tradeValue;
			makeTradeAskGood4TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo5) Ask-Uracil-Menge um 1.
	 */
	public void incrementAskUracil()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood5TextField.getRealText());
			value += tradeValue;
			tradeStationAskGood5TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood5TextField.getRealText());
			value += tradeValue;
			makeTradeAskGood5TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo1) Ask-Adenin-Menge um 1.
	 */
	public void decrementAskAdenin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood1TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationAskGood1TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood1TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeAskGood1TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo2) Ask-Cytosin-Menge um 1.
	 */
	public void decrementAskCytosin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood2TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationAskGood2TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood2TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeAskGood2TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo3) Ask-Guanin-Menge um 1.
	 */
	public void decrementAskGuanin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood3TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationAskGood3TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood3TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeAskGood3TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo4) Ask-Thymin-Menge um 1.
	 */
	public void decrementAskThymin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood4TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationAskGood4TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood4TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeAskGood4TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo5) Ask-Uracil-Menge um 1.
	 */
	public void decrementAskUracil()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationAskGood5TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationAskGood5TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeAskGood5TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeAskGood5TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	// Bid-methods	
	/**
	 * Diese Methode erhöht die (cargo1) Bid-Adenin-Menge um 1.
	 */
	public void incrementBidAdenin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood1TextField.getRealText());
			value += tradeValue;
			
			if(value <= game.selectedUnit.cargo_1)
			{
				tradeStationBidGood1TextField.setText(String.valueOf(value));
				calculateTradeStationMoneyValue(currentTradeStationID);
			}			
		}
		else
		{			
			int value = Integer.parseInt(makeTradeBidGood1TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_1)
			{
				makeTradeBidGood1TextField.setText(String.valueOf(value));
			}			
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo2) Bid-Cytosin-Menge um 1.
	 */
	public void incrementBidCytosin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood2TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_2)
			{
				tradeStationBidGood2TextField.setText(String.valueOf(value));
				calculateTradeStationMoneyValue(currentTradeStationID);
			}	
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood2TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_2)
			{
				makeTradeBidGood2TextField.setText(String.valueOf(value));
			}	
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo3) Bid-Guanin-Menge um 1.
	 */
	public void incrementBidGuanin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood3TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_3)
			{
				tradeStationBidGood3TextField.setText(String.valueOf(value));
				calculateTradeStationMoneyValue(currentTradeStationID);
			}	
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood3TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_3)
			{
				makeTradeBidGood3TextField.setText(String.valueOf(value));
			}	
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo4) Bid-Thymin-Menge um 1.
	 */
	public void incrementBidThymin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood4TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_4)
			{
				tradeStationBidGood4TextField.setText(String.valueOf(value));
				calculateTradeStationMoneyValue(currentTradeStationID);
			}	
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood4TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_4)
			{
				makeTradeBidGood4TextField.setText(String.valueOf(value));
			}	
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode erhöht die (cargo5) Bid-Uracil-Menge um 1.
	 */
	public void incrementBidUracil()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood5TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_5)
			{
				tradeStationBidGood5TextField.setText(String.valueOf(value));
				calculateTradeStationMoneyValue(currentTradeStationID);
			}	
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood5TextField.getRealText());
			value += tradeValue;
			if(value <= game.selectedUnit.cargo_5)
			{
				makeTradeBidGood5TextField.setText(String.valueOf(value));
			}	
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo1) Bid-Adenin-Menge um 1.
	 */
	public void decrementBidAdenin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood1TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationBidGood1TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood1TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeBidGood1TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo2) Bid-Cytosin-Menge um 1.
	 */
	public void decrementBidCytosin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood2TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationBidGood2TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood2TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeBidGood2TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo3) Bid-Guanin-Menge um 1.
	 */
	public void decrementBidGuanin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood3TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationBidGood3TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood3TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeBidGood3TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo4) Ask-Thymin-Menge um 1.
	 */
	public void decrementBidThymin()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood4TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationBidGood4TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood4TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeBidGood4TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode verringert die (cargo5) Bid-Uracil-Menge um 1.
	 */
	public void decrementBidUracil()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);

		if(activeTradeStationOffer)
		{
			int value = Integer.parseInt(tradeStationBidGood5TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			tradeStationBidGood5TextField.setText(String.valueOf(value));
			calculateTradeStationMoneyValue(currentTradeStationID);
		}
		else
		{
			int value = Integer.parseInt(makeTradeBidGood5TextField.getRealText());
			value -= tradeValue;
			if(value < 0)
				value = 0;
			makeTradeBidGood5TextField.setText(String.valueOf(value));
		}
		updateTradePopupValues();
	}

	/**
	 * Diese Methode lässt der Debug-Fenster erscheinen bzw verschwinden.
	 */
	public void toggleDebugWindow()
	{
		if(debugLayer.isVisible())
		{
			debugLayer.hide();
		}
		else
		{
			debugLayer.show();
		}		
	}

	/**
	 * Diese Methode erstellt ein EventPanel, welches oben in der Mitte der Karte auftaucht und 3 sekunden später verschwindet.<br>
	 * @param message Die Textnachricht, welche innerhalb des eventPanels stehen soll<br>
	 * @param red Rot-Anteil der Schriftfarbe<br>
	 * @param green Grün-Anteil der Schriftfarbe<br>
	 * @param blue Blau-Anteil der Schriftfarbe<br>
	 * @param alpha Alpha-Wert der Farbe<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 19.12.2012<br>
	 * 
	 */
	public void showEventPanel(String message, float red, float green, float blue, float alpha)
	{		
		eventPanel.show();
		eventPanelText.getRenderer(TextRenderer.class).setColor(new Color(red, green, blue, alpha));
		eventPanelText.getRenderer(TextRenderer.class).setText(message);
		activeEventPanel = true;
	}
	
	/**
	 * Diese Methode erstellt ein EventPanel, welches oben in der Mitte der Karte auftaucht und 3 sekunden später verschwindet.<br>
	 * @param width Die Breite des EventPanels
	 * @param height Die Höhe des EventPanels
	 * @param message Die Textnachricht, welche innerhalb des eventPanels stehen soll<br>
	 * @param red Rot-Anteil der Schriftfarbe<br>
	 * @param green Grün-Anteil der Schriftfarbe<br>
	 * @param blue Blau-Anteil der Schriftfarbe<br>
	 * @param alpha Alpha-Wert der Farbe<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 * 
	 */
	public void showEventPanel(int width, int height, String message, float red, float green, float blue, float alpha)
	{
		// sizeable
		eventPanel.setConstraintHeight(SizeValue.percent(height));
		eventPanel.setConstraintWidth(SizeValue.percent(width));
		eventPanel.resetLayout();
		eventPanel.resetEffects();
		eventPanel.layoutElements();
		
		eventPanel.show();
		eventPanelText.getRenderer(TextRenderer.class).setColor(new Color(red, green, blue, alpha));
		eventPanelText.getRenderer(TextRenderer.class).setText(message);
		activeEventPanel = true;
	}
	
	

	/**
	 * Diese Methode erstellt erst ein Bild der MiniMap und aktualisiert dann das miniMapPanel der GUI.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 19.12.2012<br>
	 */
	private void setMiniMapFromPNG()
	{
		try 
		{		
			// Skalierung der Panelgröße an die Kartengröße
			// Basis Höhe = 100%, Breite = 50%				
			int height = Game.playingField.height;
			int width = Game.playingField.width;
			
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
			miniMapPanel.setConstraintHeight(SizeValue.percent(height * 4));
			miniMapPanel.setConstraintWidth(SizeValue.percent(width * 4));		
			screen.layoutLayers();
			
			AWTLoader loader = new AWTLoader();
			Texture texture = new Texture2D(loader.load(miniMap.draw(false), true));
			((DesktopAssetManager)Game.AM).clearCache();
			((DesktopAssetManager)Game.AM).addToCache(new TextureKey("MINIMAPSHIT" + String.valueOf(minimapCount)), texture);
			NiftyImage miniMapImage = nifty.getRenderEngine().createImage("MINIMAPSHIT" + String.valueOf(minimapCount), false);
			miniMapPanel.getRenderer(ImageRenderer.class).setImage(miniMapImage);
			screen.layoutLayers();
			minimapCount++;
			//			miniMap.draw(true);
			//			NiftyImage miniMapImage = nifty.getRenderEngine().createImage("images/minimap.png", false);
			//			miniMapPanel.getRenderer(ImageRenderer.class).setImage(miniMapImage);
			//			screen.layoutLayers();
		} 
		catch (Exception e)
		{
			Game.logger.error("SepsisGameScreenController.setMiniMapFromPNG: catch: " + e.getMessage());
		}

	}

	/**
	 * Diese Methode lässt den Spieler aufgeben.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 08.01.2013<br>
	 */
	public void surrender()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		if(game.currentTurn.equals(TURN.PLAYER))
		{
			game.networkThread.sendIComObject(new ComObject_surrender(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));
			for(int i = 0; i < Game.playingField.units.size(); i++){
				if(Game.playingField.units.get(i) != null){
					if(Game.playingField.units.get(i).ownerID.equals(Game.playerID) && Game.playingField.units.get(i).doneThisRound == false){
						game.selectedUnit = game.getUnitByUnitID(Game.playingField.units.get(i).ID);
						game.networkThread.sendIComObject(new ComObject_endTurn(Game.clientID, Game.clientKey, Game.playerID, Game.gameID, Game.playingField.units.get(i).ID));
						break;
					}
				}
			}
		}
		else
		{
			showEventPanel("You can't surrender! It's not your TURN!", 1, 1, 1, 1);
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein ComObject_surrener vom Netzwerkthread abgearbeitet wurde.<br>
	 * 
	 * @param surrender das ComObject_surrender welches vom Server zurückkommt.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 05.02.2013<br<
	 */
	public void surrenderSended(ComObject_surrender surrender)
	{
		if(surrender.getStatus().equals(STATUS.OK))
		{
			Game.logger.info("SepsisGameScreenController.surrenderSended: Surrender successful");
			game.networkThread.sendIComObject(new ComObject_gameinfo(Game.clientID, Game.clientKey, Game.playerID, Game.gameID));	
		}
		else
		{
			Game.logger.error("SepsisGameScreenController.surrender: " + surrender.getErrorInfo());
		}
	}

	/**
	 * Diese Methode updated Inhalte des GameScreens.<br>
	 * @author Markus Strobel<br>
	 * @since 13.12.2012<br>
	 */
	@Override
	public void update(float tpf) 
	{
		// Regelt die Anzeige der Soundeinstellungen im Settings Popup
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

		// SPIEL GESTARTET
		if(game.gameStarted)
		{

			// einmalig am anfang ausführen, warum hier? da OnStartScreen bei nifty teilweise buggt und es nicht ausführt -.-
			if(!receiverDropDownFilled)
			{
				getConnectedPlayers();
				setWinConditions();
				requestTradeStationsInformation();
				receiverDropDownFilled = true;
			}			

			updateTimer += tpf;
			if(updateTimer > 3.0f)
			{				
				// CHAT
				getChatMessages();

				// TOP INFORMATION PANEL
				setTopPanelInformation();
				updateTimer = 0f;
			}

			// MiniMap alle 1 sek updaten
			miniMapRefreshTimer += tpf;
			if(miniMapRefreshTimer > 1.0f)
			{				
				setMiniMapFromPNG();
				miniMapRefreshTimer = 0f;
			}

			// Aktualisiert das Feld- bzw. Einheiteninformationsfenster
			if(game.selectedUnit != null)
			{
				if(!unitContentPanel.isVisible())
				{
					unitContentPanel.show();
				}

				if(!unitTitlePanel.isVisible())
				{
					unitTitlePanel.show();
				}

				setUnitInformation();
				showFieldUnitInformationPanel();
			}
			else
			{				
				if(game.selectedFieldPoint != null)
				{
					if(unitContentPanel.isVisible())
					{
						unitContentPanel.hide();
					}

					if(unitTitlePanel.isVisible())
					{
						unitTitlePanel.hide();
					}
					showFieldUnitInformationPanel();
					setFieldInformation();
				}
				else
				{
					hideFieldUnitInformationPanel();
				}
			}

			// Wenn ein eventPanel erschienen ist, dann wird es nach 3 Sekunden wieder verschwinden
			if(activeEventPanel)
			{
				eventTimer += tpf;
				if(eventTimer > 3.0f)
				{					
					eventPanel.hide();
					eventTimer = 0f;
					activeEventPanel = false;

				}
			}

			// Aktualisiert die Werte der Debug-Anzeige, welche mit F3 geöffnet bzw. geschlossen werden kann
			if(debugLayer.isVisible())
			{
				updateDebugPanel();
			}
		}
	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {

	}	

	/**
	 * Diese Methode wechselt vom Statistik Screen zum StartScreen.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 30.01.2013<br>
	 */
	public void backToMainMenu()
	{
		nifty.closePopup(statisticsPopup.getId());

		// Resetten der Game Variablen etc
		game.resetGameStates();

		nifty.gotoScreen("startScreen");
		screen.resetLayout();
		screen.startScreen();
		screen.layoutLayers();
	}

	/**
	 * Diese Methode blendet das ChatFenster ein bzw aus.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.02.2013<br>
	 */
	public void toggleChat()
	{
		if(chatFrameShow.isVisible())
		{
			chatText.getRenderer(TextRenderer.class).setText("${dialog.chatFrameButtonShow}");
			chatFrameShow.hide();
		}
		else
		{
			chatText.getRenderer(TextRenderer.class).setText("${dialog.chatFrameButtonHide}");
			chatFrameShow.show();
		}
	}
	
	/**
	 * Diese Methode öffnet das exitValidationPopup.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 */
	public void openExitValidationPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.showPopup(screen, exitValidationPopup.getId(), null);
		toggleMenu();
		openWindow = true;
	}
	
	/**
	 * Diese Methode schließt das exitValidationPopup.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 10.02.2013<br>
	 */
	public void closeExitValidationPopup()
	{
		game.audio.playSound(SOUND.MENU_CLICK, null);
		nifty.closePopup(exitValidationPopup.getId());
		toggleMenu();
		openWindow = false;
	}
	
	
	/**
	 * Diese Methode aktualisiert die Cargo/Supply anzeigen
	 * 
	 * @author Markus Strobel<br>
	 * @since 14.02.2013<br>
	 */
	public void updateTradePopupValues()	
	{		
		if(activeMakeTradeOffer)
		{
			int cargo = game.selectedUnit.cargo_1 + game.selectedUnit.cargo_2 + game.selectedUnit.cargo_3 + game.selectedUnit.cargo_4 + game.selectedUnit.cargo_5;

			int valueBidGood1 = Integer.parseInt(makeTradeBidGood1TextField.getRealText());
			int valueBidGood2 = Integer.parseInt(makeTradeBidGood2TextField.getRealText());
			int valueBidGood3 = Integer.parseInt(makeTradeBidGood3TextField.getRealText());
			int valueBidGood4 = Integer.parseInt(makeTradeBidGood4TextField.getRealText());
			int valueBidGood5 = Integer.parseInt(makeTradeBidGood5TextField.getRealText());
			
			int valueAskGood1 = Integer.parseInt(makeTradeAskGood1TextField.getRealText());
			int valueAskGood2 = Integer.parseInt(makeTradeAskGood2TextField.getRealText());
			int valueAskGood3 = Integer.parseInt(makeTradeAskGood3TextField.getRealText());
			int valueAskGood4 = Integer.parseInt(makeTradeAskGood4TextField.getRealText());
			int valueAskGood5 = Integer.parseInt(makeTradeAskGood5TextField.getRealText());
			
			int tempCargo = (valueAskGood1 + valueAskGood2 + valueAskGood3 + valueAskGood4 + valueAskGood5) - (valueBidGood1 + valueBidGood2 + valueBidGood3 + valueBidGood4 + valueBidGood5);

			tradeCargoInfoText.getRenderer(TextRenderer.class).setText("Total Cargo: " + String.valueOf(cargo + tempCargo) + "/" + String.valueOf(game.selectedUnit.cargo_max));

			if((tempCargo + cargo) > game.selectedUnit.cargo_max)
			{
				tradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(1f, 0f, 0f, 1f));
			}
			else
			{
				tradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(0f, 1f, 0f, 1f));
			}
			good1supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_1 - valueBidGood1 + valueAskGood1));
			good2supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_2 - valueBidGood2 + valueAskGood2));
			good3supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_3 - valueBidGood3 + valueAskGood3));
			good4supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_4 - valueBidGood4 + valueAskGood4));
			good5supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_5 - valueBidGood5 + valueAskGood5));
		}
		
		if(activeGetTradeOffer)
		{
			int cargo = game.selectedUnit.cargo_1 + game.selectedUnit.cargo_2 + game.selectedUnit.cargo_3 + game.selectedUnit.cargo_4 + game.selectedUnit.cargo_5;

			int valueBidGood1 = Integer.parseInt(getTradeBidGood1TextField.getRealText());
			int valueBidGood2 = Integer.parseInt(getTradeBidGood2TextField.getRealText());
			int valueBidGood3 = Integer.parseInt(getTradeBidGood3TextField.getRealText());
			int valueBidGood4 = Integer.parseInt(getTradeBidGood4TextField.getRealText());
			int valueBidGood5 = Integer.parseInt(getTradeBidGood5TextField.getRealText());
			
			int valueAskGood1 = Integer.parseInt(getTradeAskGood1TextField.getRealText());
			int valueAskGood2 = Integer.parseInt(getTradeAskGood2TextField.getRealText());
			int valueAskGood3 = Integer.parseInt(getTradeAskGood3TextField.getRealText());
			int valueAskGood4 = Integer.parseInt(getTradeAskGood4TextField.getRealText());
			int valueAskGood5 = Integer.parseInt(getTradeAskGood5TextField.getRealText());
			
			int tempCargo = (valueAskGood1 + valueAskGood2 + valueAskGood3 + valueAskGood4 + valueAskGood5) - (valueBidGood1 + valueBidGood2 + valueBidGood3 + valueBidGood4 + valueBidGood5);

			gettradeCargoInfoText.getRenderer(TextRenderer.class).setText("Total Cargo: " + String.valueOf(cargo + tempCargo) + "/" + String.valueOf(game.selectedUnit.cargo_max));

			if((tempCargo + cargo) > game.selectedUnit.cargo_max)
			{
				gettradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(1f, 0f, 0f, 1f));
			}
			else
			{
				gettradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(0f, 1f, 0f, 1f));
			}
			getgood1supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_1 - valueBidGood1 + valueAskGood1));
			getgood2supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_2 - valueBidGood2 + valueAskGood2));
			getgood3supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_3 - valueBidGood3 + valueAskGood3));
			getgood4supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_4 - valueBidGood4 + valueAskGood4));
			getgood5supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_5 - valueBidGood5 + valueAskGood5));
		}
		
		if(activeTradeStationOffer)
		{
			int cargo = game.selectedUnit.cargo_1 + game.selectedUnit.cargo_2 + game.selectedUnit.cargo_3 + game.selectedUnit.cargo_4 + game.selectedUnit.cargo_5;

			int valueBidGood1 = Integer.parseInt(tradeStationBidGood1TextField.getRealText());
			int valueBidGood2 = Integer.parseInt(tradeStationBidGood2TextField.getRealText());
			int valueBidGood3 = Integer.parseInt(tradeStationBidGood3TextField.getRealText());
			int valueBidGood4 = Integer.parseInt(tradeStationBidGood4TextField.getRealText());
			int valueBidGood5 = Integer.parseInt(tradeStationBidGood5TextField.getRealText());
			
			int valueAskGood1 = Integer.parseInt(tradeStationAskGood1TextField.getRealText());
			int valueAskGood2 = Integer.parseInt(tradeStationAskGood2TextField.getRealText());
			int valueAskGood3 = Integer.parseInt(tradeStationAskGood3TextField.getRealText());
			int valueAskGood4 = Integer.parseInt(tradeStationAskGood4TextField.getRealText());
			int valueAskGood5 = Integer.parseInt(tradeStationAskGood5TextField.getRealText());
			
			int tempCargo = (valueAskGood1 + valueAskGood2 + valueAskGood3 + valueAskGood4 + valueAskGood5) - (valueBidGood1 + valueBidGood2 + valueBidGood3 + valueBidGood4 + valueBidGood5);

			tradestationtradeCargoInfoText.getRenderer(TextRenderer.class).setText("Total Cargo: " + String.valueOf(cargo + tempCargo) + "/" + String.valueOf(game.selectedUnit.cargo_max));

			if((tempCargo + cargo) > game.selectedUnit.cargo_max)
			{
				tradestationtradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(1f, 0f, 0f, 1f));
			}
			else
			{
				tradestationtradeCargoInfoText.getRenderer(TextRenderer.class).setColor(new Color(0f, 1f, 0f, 1f));
			}
			tradestationgood1supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_1 - valueBidGood1 + valueAskGood1));
			tradestationgood2supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_2 - valueBidGood2 + valueAskGood2));
			tradestationgood3supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_3 - valueBidGood3 + valueAskGood3));
			tradestationgood4supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_4 - valueBidGood4 + valueAskGood4));
			tradestationgood5supplyText.getRenderer(TextRenderer.class).setText(String.valueOf(game.selectedUnit.cargo_5 - valueBidGood5 + valueAskGood5));
		}
	}
	
	/**
	 * Diese Methode gibt an ob die winConditions sichtbar sind oder nicht
	 * @return true, wenn sichtbar und false wenn nicht
	 * 
	 * @author Markus Strobel<br>
	 * @since 14.02.2013<br>
	 */
	public boolean getWinConditionsVisibilityStatus()
	{	
		return winConditionsPanel.isVisible();
	}	
	
	/**
	 * Diese Methode berechnet den erlittenen Schaden einer Einheit.<br>
	 * 
	 * @param victimUnitID die ID der betroffene Einheit<br>
	 * @return der Schadenswert den die Einheit erlitten hat<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 14.02.2013<br>
	 */
	public int calculateReceivedDamage()
	{
		int damageValue = 0;
		for(int i = 0; i < UnitIDs.size(); i++)
		{
			// Wie toll fände ich hier Lambda Ausdrücke -.- aber die gibs erst mit Java 8
			int currentUnitIndex = -1;
			for(int j = 0; j < Game.playingField.units.size(); j++)
			{
				if(Game.playingField.units.get(j).ID == UnitIDs.get(i))
				{
					currentUnitIndex = j;
				}
			}
			
			if(currentUnitIndex != -1)
			{
				if(UnitHPs.get(i) != Game.playingField.units.get(currentUnitIndex).hitpoints_curr)
				{
					damageValue = UnitHPs.get(i) - Game.playingField.units.get(currentUnitIndex).hitpoints_curr;
//					UnitHPs.set(i, Game.playingField.units.get(currentUnitIndex).hitpoints_curr);
					return damageValue;
				}
			}
		}		
		return damageValue;
	}
	
	
	
	
}
