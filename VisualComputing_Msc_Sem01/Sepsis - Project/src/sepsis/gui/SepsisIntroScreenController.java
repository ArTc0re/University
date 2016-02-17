package sepsis.gui;

import sepsis.game.Game;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Diese Klasse kontrolliert den Introbildschirm.<br>
 * 
 * @author Markus Strobel<br>
 * @since 19.12.2012<br>
 * 
 */
public class SepsisIntroScreenController extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	@SuppressWarnings("unused")
	private Screen screen;
	private boolean skipIntro = false;
	private float introTimer = 0f;
	
	/**
	 * Der Konstruktor des SepsisIntroScreenController
	 * 
	 * @author Markus Strobel
	 * @since 29.11.2012
	 * 
	 * @param game Die Instanz der SimpleApplication
	 * @param stateManager Der stateManager für eventuelle Zustandskontrolle
	 * @param nifty Die Nifty Instanz für die GUI
	 */
	public SepsisIntroScreenController(Game game, AppStateManager stateManager, Nifty nifty)
	{
		this.nifty = nifty;		
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		
	}

	@Override
	public void onStartScreen() {
		
	}

	public void goToStartScreen()
	{
		skipIntro = true;
	}
	
	
	/**
	 * Diese Methode updated Elemente des IntroScreens.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 19.12.2012<br>
	 */
	@Override
	public void update(float tpf)
	{
		introTimer += tpf;
		
		if(introTimer > 20f || skipIntro)
		{
			nifty.gotoScreen("startScreen");
		}
		
	}	
}
