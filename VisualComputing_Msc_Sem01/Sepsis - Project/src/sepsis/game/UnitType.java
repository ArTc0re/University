package sepsis.game;


/**
 * Repräsentiert einen möglichen Einheitentyp des Spiels.
 * 
 * @author Markus Strobel
 * @since 17.11.12
 */
public class UnitType {
		
	/**
	 * Der Einheitentyp
	 */
	public String type = "";
	
	/**
	 * Maximale Anzahl an Bewegungspunkten
	 */
	public int movement;
	
	/**
	 * Maximale Anzahl an Trefferpunkten
	 */
	public int maxHitPoints;
	
	/**
	 * Maximale Feuerkraft
	 */
	public int maxFirePower;
	
	/**
	 * Maximale Ladekapazität
	 */
	public int maxCargo;

}
