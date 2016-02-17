package sepsis;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import sepsis.game.Game;
import sepsis.gui.SepsisAppSettingsContainer;
import sepsis.network.Network;

import com.jme3.system.AppSettings;



public class Sepsis {

	/**
	 * Dieses Objekt ist für das laden und speichern der gameSettings notwendig.
	 */
	private static SepsisAppSettingsContainer sepsisAppSettings;
	
	/**
	 * Dies ist der Einstiegspunkt in das Programm "Sepsis - Dawn of the DNA",
	 * das im Rahmen der Veranstaltung "Visual Computing Praktikum 2012/13" entstand. Startet das Spiel über Instanzierung der Game-Klasse im game-Package.<br><br>
	 * 
	 * Startet das Spiel, ohne das JME3-Optionsmenü anzuzeigen.<br>
	 * 
	 * 16.11.12 (Peter Dörr): Settings bei Programmstart festgelegt, JME3-Optionsmenü deaktiviert.
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12
	 * 
	 * @param args Dieser Parameter wird nicht ausgelesen und hat keinerlei Bedeutung.
	 */
	public static void main(String[] args) {
		DOMConfigurator.configureAndWatch("data/config/log4j.xml", 60*1000);
		
//		Test: WinConditions parsen (sinnfrei, aber gefordert) =)
//		MapConfig test = new MapConfig();
//		test.parseFromXML("data/config/POV_WinCondi.map.config.xml");
		
		Game game = new Game();
		game.showSettings(false);
		
		// SepsisAppSettings initieren und laden
		sepsisAppSettings = new SepsisAppSettingsContainer();

		AppSettings settings = new AppSettings(true);
		settings = sepsisAppSettings.loadSepsisAppSettings();
		
		Network.IP = sepsisAppSettings.ip;
		Network.PORT = Integer.parseInt(sepsisAppSettings.port);	
				
		game.setSettings(settings);
		game.sepsisAppSettings = sepsisAppSettings;
		game.setDisplayFps(false);
		game.setDisplayStatView(false);
		game.setPauseOnLostFocus(false);

		//JME Logger etwas weniger mitteilungsfreudig konfigurieren
		Logger.getLogger("").setLevel(Level.SEVERE);

		//Spiel starten
		game.start();
		
	}

}
