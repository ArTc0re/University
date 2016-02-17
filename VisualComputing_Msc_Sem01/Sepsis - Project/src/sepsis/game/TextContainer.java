package sepsis.game;

/**
 * Innerhalb dieser Klasse werden einige Texte gespeichert, die in verschiedenen Stellen innerhalb des Spiels verwendet werden.
 * 
 * @author Peter Doerr
 * @since 19.11.12
 */
public class TextContainer {
	
	private String endOfLine = System.getProperty("line.separator"); 
	
	private String story =	"Dein Koerper ist in Gefahr!" + endOfLine + endOfLine +

							"Hunderte von Bakterien und Viren wandern durch deine Adern," + endOfLine +
							"erpicht auf die Zerstoerung von Zellen und Gewebe." + endOfLine +
							"Es gibt nur noch eine Chance, die Eindringline aufzuhalten:" + endOfLine +
							"dein Immunsystem!" + endOfLine + endOfLine +
							"Uebernimm die Kontrolle über fuenf verschiedene Antikoerper," + endOfLine +
							"bekaempfe Vien und Bakterien und rette dich selbst!";
	
	private String credits ="Lead-Designer: Markus Strobel"  + endOfLine +
							"Stakeholder-Manager: Markus Strobel"  + endOfLine +
							"Project-Manager: Peter Doerr" + endOfLine + endOfLine + endOfLine +
							"Quellen:" + endOfLine + endOfLine + endOfLine +
							"Audio:" + endOfLine +
							"Alle Sounds stammen von www.freesounds.org!"  + endOfLine + endOfLine +
							"Alle Musikstuecke stammen von www.freemusicarchive.org und sind dort" + endOfLine +
							"fuer nicht kommerzielle Projekte gratis verfuegbar." + endOfLine + endOfLine + endOfLine +
							"Texturen:" + endOfLine +
							"Alle Texturen stammen von www.turbosquid.com" + endOfLine +
							"und sind dort gratis erhaeltlich!" + endOfLine + endOfLine +
							"Alle GUI Grafiken wurden selbst erstellt!" + endOfLine + endOfLine +
							"Alle 3D-Modelle wurden ebenfalls selbst erstellt!";
	
	
	
	private String controls =	"W, A, S, D = Kamera nach vorne, links, hinten, rechts" + endOfLine +
								"Mittlere Maustaste (gedrueckt) = Kamera frei bewegen" + endOfLine +
								"F2 = Lebensbalken ein- und ausblenden" + endOfLine +
								"F4 = 'Fog of War'-Modus wechseln" + endOfLine +
								"F5 = Autofokus ein- und ausschalten" + endOfLine +
								"F6 = Gewinnbedingungen ein- und ausblenden" + endOfLine +
								"F7 = 'Minimap'-Modus wechseln" + endOfLine +
								"F10/ESCAPE = Menue" + endOfLine +
								"Return = Runde beenden" + endOfLine +
								"Linksklick = Einheit auswaehlen" + endOfLine +
								"Rechtsklick = Einheit bewegen / Aktionsmenue" + endOfLine +
								"SHIFT + Linksklick = Wegpunkt setzen" + endOfLine +
								"E = Letzten Wegpunkt entfernen" + endOfLine +
								"Q = Alle Wegpunkt entfernen" + endOfLine +
								"Mausrad = Zoom";

	public String getStory() {
		return story;
	}

	public String getCredits() {
		return credits;
	}

	public String getControls() {
		return controls;
	}
	
	
}
