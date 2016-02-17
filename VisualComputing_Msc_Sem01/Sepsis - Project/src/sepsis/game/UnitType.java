package sepsis.game;


/**
 * Repr�sentiert einen m�glichen Einheitentyp des Spiels.
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
	 * Maximale Ladekapazit�t
	 */
	public int maxCargo;

}
