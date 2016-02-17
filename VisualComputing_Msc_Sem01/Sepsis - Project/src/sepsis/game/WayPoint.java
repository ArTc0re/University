package sepsis.game;

/**
 * 
 * Diese Klasse stellt einen Knoten auf dem Spielfeldgraph dar und wird für den A*-Algorithmus benötigt.<br><br>
 * 
 * @author Markus Strobel
 * @since 19.11.2012<br>
 *
 */
public class WayPoint {

	/** 
	 * Der x-Wert des Knotens
	 */
	public int x;
	/** 
	 * Der y-Wert des Knotens
	 */
	public int y;
	/** 
	 * Die Wegkosten dex Knotens
	 */
	public int costValue;
	/**
	 * Der Heuristik-Wert des Knotens
	 */
	public int heuristicValue;
	/**
	 * Dieser Wert entspricht den Gesamtkosten costValue + heuristicValue
	 */
	public int totalCostValue;
	
	/** 
	 * Dies ist der Vorgänger des aktuellen Knotens, wenn es sich um den Startknoten handelt wird dieser Wert auf null gesetzt.
	 */
	private WayPoint parentWayPoint;
	
	/**
	 * Der Konstruktor des Startknotens
	 * @param x_pos Der x-Wert des Knotens
	 * @param y_pos Der y-Wert des Knotens
	 * @param cost Die Wegkosten des Knotens
	 * @param x_destination Der x-Wert des Zielknotens
	 * @param y_destination Der y-Wert des Zielknotens<br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	public WayPoint(int x_pos, int y_pos, int cost, int x_destination, int y_destination)
	{
		x = x_pos;
		y = y_pos;
		costValue = cost;
		heuristicValue = getHeuristicValue(x, y, x_destination, y_destination);	
		totalCostValue = costValue + heuristicValue;
		parentWayPoint = null;
	}

	/**
	 * Der Konstruktor des Nachfolgerknotens
	 * @param x_pos Der x-Wert des Knotens
	 * @param y_pos Der y-Wert des Knotens
	 * @param cost Die Wegkosten des Knotens
	 * @param x_destination Der x-Wert des Zielknotens
	 * @param y_destination Der y-Wert des Zielknotens<br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	public WayPoint(int x_pos, int y_pos, int cost, int x_destination, int y_destination, WayPoint parent)
	{
		x = x_pos;
		y = y_pos;
		costValue = cost;
		heuristicValue = getHeuristicValue(x, y, x_destination, y_destination);	
		totalCostValue = costValue + heuristicValue;
		parentWayPoint = parent;
	}
	
	/**
	 * Diese Methode gibt den Vorgängerknoten aus
	 * @return Der Elternknoten bzw. Vorgängerknoten<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	public WayPoint getParentWayPoint()
	{	
		return parentWayPoint;
	}
	
	/** 
	 * Diese Methode gibt die unterschätzende Heuristic für den A*-Algorithmus aus
	 * @param x Der x-Wert des Knotens
	 * @param y Der y-Wert des Knotens
	 * @param x_destination Der x-Wert des Zielknotens
	 * @param y_destination Der y-Wert des Zielknotens
	 * @return Der heuristicValue des Knotens<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
 	private int getHeuristicValue(int x, int y, int x_destination, int y_destination)
	{
		return (Math.abs((x - x_destination)) + Math.abs((y - y_destination)));
	}
	
}
