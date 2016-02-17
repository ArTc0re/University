package sepsis.game;


/**
 * Repräsentiert die Startwerte einer Einheit auf dem Spielfeld.
 * 
 * @author Markus Strobel
 * @since 17.11.12
 */
public class UnitSetup {
	
	/**
	 * ID der Einheit
	 */
	public int id;
	
	/** 
	 * Typ der Einheit
	 */
	public String type = "";
	
	/**
	 * Besitzer der Einheit
	 */
	public String ownerID;
	
	/**
	 * X-Koordinate auf dem Spielfeld
	 */
	public int positionX;
	
	/**
	 * Y-Koordinate auf dem Spielfeld
	 */
	public int positionY;
	
	/**
	 *  Wenn die Einheit einen HitPoint-Wert hat, dann wird dieser Wert auf true gesetzt
	 */
	public boolean hasCustomHitPointValue = false;;

	/**
	 * Die aktuellen Trefferpunkte der Einheit
	 */
	public int currentHitPoints;
	
	/**
	 *  Wenn die Einheit einen MovementPoint-Wert hat, dann wird dieser Wert auf true gesetzt
	 */
	public boolean hasCustomMovementPointValue = false;;

	/**
	 * Die aktuellen Movement-Punkte der Einheit
	 */
	public int currentMovementPoints;

	/**
	 * Adenin-Cargo
	 */
	public int cargo_1;
	/**
	 * Thymin-Cargo
	 */
	public int cargo_2;
	/**
	 *  Guanin-Cargo
	 */
	public int cargo_3;
	/**
	 * Cytosin-Cargo
	 */
	public int cargo_4;
	/**
	 * Sauerstoff-Cargo
	 */
	public int cargo_5;
}
