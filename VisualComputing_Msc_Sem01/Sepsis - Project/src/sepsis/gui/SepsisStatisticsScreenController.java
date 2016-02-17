package sepsis.gui;

import sepsis.game.Game;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

/**
 * Diese Klasse kontrolliert den Statisticsbildschirm.<br>
 * 
 * @author Markus Strobel<br>
 * @since 15.01.2013<br>
 * 
 */
public class SepsisStatisticsScreenController extends AbstractAppState implements ScreenController {

	@SuppressWarnings("unused")
	private Nifty nifty;
	private Screen screen;
	private Game game;
	
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
	
	/**
	 * Der Konstruktor des SepsisStatisticsScreenController
	 * 
	 * @author Markus Strobel
	 * @since 15.01.2013
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 */
	public SepsisStatisticsScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.game = game;
		this.nifty = nifty;		
	}
	
	/**
	 * Diese Methode instanziert die Nifty Elemente<br>
	 * 
	 * @autohor Markus Strobel<br>
	 * @since 15.01.2013<br>
	 */
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.screen = screen;
		
		// currentGamePanels
		playedTime = screen.findElementByName("playedTimePanel");
		
		// "current" Barriers
		movesbarrier = this.screen.findElementByName("movesbarrier");
		f1movesbarrier = this.screen.findElementByName("f1movesbarrier");
		f2movesbarrier = this.screen.findElementByName("f2movesbarrier");
		f3movesbarrier = this.screen.findElementByName("f3movesbarrier");
		t1movesbarrier = this.screen.findElementByName("t1movesbarrier");
		t2movesbarrier = this.screen.findElementByName("t2movesbarrier");		
		killsbarrier = this.screen.findElementByName("killsbarrier");
		f1killsbarrier = this.screen.findElementByName("f1killsbarrier");
		f2killsbarrier = this.screen.findElementByName("f2killsbarrier");
		f3killsbarrier = this.screen.findElementByName("f3killsbarrier");
		t1killsbarrier = this.screen.findElementByName("t1killsbarrier");
		t2killsbarrier = this.screen.findElementByName("t2killsbarrier");		
		tradesbarrier = this.screen.findElementByName("tradesbarrier");
		f1tradesbarrier = this.screen.findElementByName("f1tradesbarrier");
		f2tradesbarrier = this.screen.findElementByName("f2tradesbarrier");
		f3tradesbarrier = this.screen.findElementByName("f3tradesbarrier");
		t1tradesbarrier = this.screen.findElementByName("t1tradesbarrier");
		t2tradesbarrier = this.screen.findElementByName("t2tradesbarrier");
		
		// currentGameText
		movesText = this.screen.findElementByName("movesText");
		f1movesText = this.screen.findElementByName("f1movesText");
		f2movesText = this.screen.findElementByName("f2movesText");
		f3movesText = this.screen.findElementByName("f3movesText");
		t1movesText = this.screen.findElementByName("t1movesText");
		t2movesText = this.screen.findElementByName("t2movesText");	
		killsText = this.screen.findElementByName("killsText");
		f1killsText = this.screen.findElementByName("f1killsText");
		f2killsText = this.screen.findElementByName("f2killsText");
		f3killsText = this.screen.findElementByName("f3killsText");
		t1killsText = this.screen.findElementByName("t1killsText");
		t2killsText = this.screen.findElementByName("t2killsText");
		tradesText = this.screen.findElementByName("tradesText");
		f1tradesText = this.screen.findElementByName("f1tradesText");
		f2tradesText = this.screen.findElementByName("f2tradesText");
		f3tradesText = this.screen.findElementByName("f3tradesText");
		t1tradesText = this.screen.findElementByName("t1tradesText");
		t2tradesText = this.screen.findElementByName("t2tradesText");
		playedTimeText = this.screen.findElementByName("playedTimeText");
		
		// allGamesPanels
		allplayedTime = this.screen.findElementByName("allplayedTimePanel");
		
		// "all" Barriers
		allmovesbarrier = this.screen.findElementByName("allmovesbarrier");
		allf1movesbarrier = this.screen.findElementByName("allf1movesbarrier");
		allf2movesbarrier = this.screen.findElementByName("allf2movesbarrier");
		allf3movesbarrier = this.screen.findElementByName("allf3movesbarrier");
		allt1movesbarrier = this.screen.findElementByName("allt1movesbarrier");
		allt2movesbarrier = this.screen.findElementByName("allt2movesbarrier");		
		allkillsbarrier = this.screen.findElementByName("allkillsbarrier");
		allf1killsbarrier = this.screen.findElementByName("allf1killsbarrier");
		allf2killsbarrier = this.screen.findElementByName("allf2killsbarrier");
		allf3killsbarrier = this.screen.findElementByName("allf3killsbarrier");
		allt1killsbarrier = this.screen.findElementByName("allt1killsbarrier");
		allt2killsbarrier = this.screen.findElementByName("allt2killsbarrier");		
		alltradesbarrier = this.screen.findElementByName("alltradesbarrier");
		allf1tradesbarrier = this.screen.findElementByName("allf1tradesbarrier");
		allf2tradesbarrier = this.screen.findElementByName("allf2tradesbarrier");
		allf3tradesbarrier = this.screen.findElementByName("allf3tradesbarrier");
		allt1tradesbarrier = this.screen.findElementByName("allt1tradesbarrier");
		allt2tradesbarrier = this.screen.findElementByName("allt2tradesbarrier");
		
		
		
		// allGamesText
		allmovesText = this.screen.findElementByName("allmovesText");
		allf1movesText = this.screen.findElementByName("allf1movesText");
		allf2movesText = this.screen.findElementByName("allf2movesText");
		allf3movesText = this.screen.findElementByName("allf3movesText");
		allt1movesText = this.screen.findElementByName("allt1movesText");
		allt2movesText = this.screen.findElementByName("allt2movesText");	
		allkillsText = this.screen.findElementByName("allkillsText");
		allf1killsText = this.screen.findElementByName("allf1killsText");
		allf2killsText = this.screen.findElementByName("allf2killsText");
		allf3killsText = this.screen.findElementByName("allf3killsText");
		allt1killsText = this.screen.findElementByName("allt1killsText");
		allt2killsText = this.screen.findElementByName("allt2killsText");
		alltradesText = this.screen.findElementByName("alltradesText");
		allf1tradesText = this.screen.findElementByName("allf1tradesText");
		allf2tradesText = this.screen.findElementByName("allf2tradesText");
		allf3tradesText = this.screen.findElementByName("allf3tradesText");
		allt1tradesText = this.screen.findElementByName("allt1tradesText");
		allt2tradesText = this.screen.findElementByName("allt2tradesText");
		allplayedTimeText = this.screen.findElementByName("allplayedTimeText");
		
		// statistics
		currentGameStatisticsPanel = this.screen.findElementByName("currentGameStatisticsPanel");
		allGamesStatisticsPanel = this.screen.findElementByName("allGamesStatisticsPanel");
		
		// WinnerText
		winnerLooserText = this.screen.findElementByName("winnerLooserText");
	}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
		

		setCurrentGameStatistics();
		setAllGamesStatistics();
		allGamesStatisticsPanel.hide();

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
	
	
	
	
	
	
	
}
