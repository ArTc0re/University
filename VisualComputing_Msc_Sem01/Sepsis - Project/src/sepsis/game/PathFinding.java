package sepsis.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Diese Klasse wendet den A*-Algorithmus mit untersch�tzender Heuristik an, um den k�rzesten Weg zum Zielfeld zu bestimmen.<br><br>
 * 
 * Programmcode basiert auf folgender Quelle: http://www.ki.informatik.uni-frankfurt.de/lehre/WS2012/KI/folien/04-suche-2-4sw.pdf<br><br>
 * 
 * @author Markus Strobel
 * @since 19.11.2012<br><br>
 * 
 * 20.11.2012 (Markus Strobel) Beachtung der nicht-begehbaren Knoten wurde hinzugef�gt<br><br>
 * 
 * 09.12.2012 (Markus Strobel) getDistance(Point startPoint, Point endPoint) hinzugef�gt.<br><br>
 * 
 * 14.12.2012 (Markus Strobel) getShortestPathFromPointToPoint(...) hinzugef�gt (auf basis der bisherigen getShortestPath Methode).<br><br>
 *
 */
public class PathFinding {

	/**
	 * Die Liste der Wegpunkte des k�rzesten Weges
	 */
	private ArrayList<Point> shortestPath;
	/**
	 * Die OpenMenge des A*-Algorithmus
	 */
	private ArrayList<WayPoint> openList;
	/**
	 * Hier werden die tats�chlich besuchten WayPoints gespeichert.
	 */
	private LinkedHashSet<WayPoint> closedSet;
	/**
	 * Hier werden die schon besuchten oder nicht besuchbaren Punkte als Points gespeichert, damit keine doppelten Werte auftreten k�nnen
	 */
	private HashSet<Point> closedPointsSet;

	/**
	 * Konstruktor der PathFinding-Klasse<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	public PathFinding()
	{
		shortestPath = new ArrayList<Point>();		
		openList = new ArrayList<WayPoint>();
		closedSet = new LinkedHashSet<WayPoint>();
		closedPointsSet = new HashSet<Point>();
	}

	/**
	 * Diese Methode gibt den k�rzesten Weg von einer Einheit bis zu ihrem Zielknoten aus
	 * 
	 * @param map Die Karte des Spiels
	 * @param mapHeight Die Kartenh�he
	 * @param mapWidth Die Kartenbreite
	 * @param units Die Liste der Einheiten
	 * @param selectedUnitIndex Der Index der ausgew�hlten Einheit
	 * @param x_destination Die X-Koordinate des Zielknotens
	 * @param z_destination Die Z-Koordinate des Zielknotens
	 * @param disableFriendlyUnitCollision Wenn dieser Wert auf true ist, dann k�nnen Einheiten durch verb�ndete Einheiten durchlaufen
	 * @return Eine ArrayList<Point> welche vom Start bis zum Zielknoten den k�rzesten Weg beschreibt<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 * 
	 */
	public ArrayList<Point> getShortestPath(int map[][], int mapHeight, int mapWidth, ArrayList<Unit> units, int selectedUnitID, int x_destination, int z_destination, boolean disableFriendlyUnitCollision)
	{
				
		try 
		{		

		// Die ArrayListen / Mengen resetten
		shortestPath = new ArrayList<Point>();
		openList = new ArrayList<WayPoint>(); 
		closedSet = new LinkedHashSet<WayPoint>();
		closedPointsSet = new LinkedHashSet<Point>();	
		Unit selectedUnit = null;
		
		// Die ausgew�hlte Einheit
		for(int i = 0; i < units.size(); i++)
		{
			if(units.get(i).ID == selectedUnitID)
			{
				selectedUnit = units.get(i);
			}				
		}		
		
		// Knoten, welche von Einheiten besetzt sind, werden der closedPointsSet Menge hinzugef�gt
		for(int i = 0; i < units.size(); i++)
		{
			// f�gt nur Knoten mit feindlichen Einheiten darauf der closedPointsSet hinzu
			if(disableFriendlyUnitCollision)
			{
				if(units.get(i).ownerID != selectedUnit.ownerID)
				{
					if(units.get(i).x != x_destination || units.get(i).y != z_destination)
					{
						closedPointsSet.add(new Point(units.get(i).x, units.get(i).y));
					}
				}
			}
			// f�gt alle Knoten mit Einheiten darauf der closedPointsSet hinzu
			else
			{
				if(units.get(i).x != x_destination || units.get(i).y != z_destination)
				{
					closedPointsSet.add(new Point(units.get(i).x, units.get(i).y));
				}
			}			
		}

		// Der Startknoten der ausgew�hlten Einheit
		WayPoint startPoint = new WayPoint(selectedUnit.x , selectedUnit.y, 0, x_destination, z_destination);
		WayPoint cP = startPoint; // currentPoint
		WayPoint temporaryPoint = null;
		boolean noPossiblePath = false;
		boolean initOpenList = false;		

		// Solange der k�rzeste Weg nicht gefunden wurde ist dieser Wert false
		boolean shortestWay = false;
		
		// Der Hauptteil des Algorithmus
		while(!shortestWay)
		{
			
			// An dieser Stelle wird gepr�ft ob das Ziel erreicht wurde
			if(cP.heuristicValue == 0)
			{
				shortestWay = true;
				continue;
			}
			if(openList.size() == 0 && initOpenList)
			{
				shortestWay = true;
				noPossiblePath = true;
				continue;
			}

			// Erstellen der Open-Menge
			// gerade Reihe
			if(cP.y % 2 == 0)
			{								
				WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, x_destination, z_destination, cP); // rechts
				WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, x_destination, z_destination, cP); // links
				WayPoint neighbor3 = new WayPoint(cP.x, 	cP.y + 1, cP.costValue + 1, x_destination, z_destination, cP); // oben rechts
				WayPoint neighbor4 = new WayPoint(cP.x + 1, cP.y - 1, cP.costValue + 1, x_destination, z_destination, cP); // unten links				
				WayPoint neighbor5 = new WayPoint(cP.x + 1, cP.y + 1, cP.costValue + 1, x_destination, z_destination, cP); // oben links
				WayPoint neighbor6 = new WayPoint(cP.x, 	cP.y - 1, cP.costValue + 1, x_destination, z_destination, cP); // unten rechts
				
				// G�ltigkeits-Pr�fung f�r neighbor1
				WayPoint temp = getValidNeighbor(map, neighbor1, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}				

				// G�ltigkeits-Pr�fung f�r neighbor2
				temp = getValidNeighbor(map, neighbor2, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}					

				// G�ltigkeits-Pr�fung f�r neighbor3
				temp = getValidNeighbor(map, neighbor3, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor4
				temp = getValidNeighbor(map, neighbor4, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor5
				temp = getValidNeighbor(map, neighbor5, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor6
				temp = getValidNeighbor(map, neighbor6, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}				
			}
			// ungerade Reihe
			else
			{				
				WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, x_destination, z_destination, cP); // rechts
				WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, x_destination, z_destination, cP); // links
				WayPoint neighbor3 = new WayPoint(cP.x - 1, cP.y + 1, cP.costValue + 1, x_destination, z_destination, cP); // oben rechts
				WayPoint neighbor4 = new WayPoint(cP.x	  , cP.y - 1, cP.costValue + 1, x_destination, z_destination, cP); // unten links				
				WayPoint neighbor5 = new WayPoint(cP.x	  , cP.y + 1, cP.costValue + 1, x_destination, z_destination, cP); // oben links
				WayPoint neighbor6 = new WayPoint(cP.x - 1, cP.y - 1, cP.costValue + 1, x_destination, z_destination, cP); // unten rechts

				// G�ltigkeits-Pr�fung f�r neighbor1
				WayPoint temp = getValidNeighbor(map, neighbor1, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor2
				temp = getValidNeighbor(map, neighbor2, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor3
				temp = getValidNeighbor(map, neighbor3, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor4
				temp = getValidNeighbor(map, neighbor4, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor5
				temp = getValidNeighbor(map, neighbor5, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor6
				temp = getValidNeighbor(map, neighbor6, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}
				

			}			
			// Removing the currentPoint (cP) from the openList
			int cp_index = -1;
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).x == cP.x && openList.get(i).y == cP.y)
				{
					cp_index = i;
				}
			}
			if(cp_index != -1)
				openList.remove(cp_index);

			// initieren des min Wertes f�r die Kosten + Heuristic Pr�fung
			// Herausfinden welcher Knoten als n�chstes ausgew�hlt wird
			int minCost = mapWidth * mapHeight; // skalierbar f�r gr��ere Kartengr��en und so wird garantiert ein niedrigerer minCost-Wert vorhanden sein
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).totalCostValue < minCost)
				{
					minCost = openList.get(i).totalCostValue;
					temporaryPoint = openList.get(i);
				}
			}

			// Der aktuelle Knoten wird als Point der closedSet hinzugef�gt und der neue aktuelle Knoten wird gesetzt
			closedPointsSet.add(new Point(cP.x, cP.y));

			closedSet.add(cP);

			// Setzen des neuen aktuellen Knotens
			cP = temporaryPoint;

		}

		// Hier wird der k�rzeste Weg zu den Elternknoten zur�ckgegangen und gespeichert
		ArrayList<Point> reversePointList = new ArrayList<Point>();
		boolean finished = false;
		while(!finished)
		{						
			if(cP != null)
			{
				reversePointList.add(new Point(cP.x, cP.y));
			}
			else
			{
				finished = true;
				continue;
			}
			cP = cP.getParentWayPoint();
		}
		
		// Hier wird die ArrayList umgedreht und anschlie�end ausgegeben.
		for(int i = reversePointList.size() - 1; i >= 0; i--)
		{
			shortestPath.add(reversePointList.get(i));
		}		
		
		if(noPossiblePath)
		{
			shortestPath = null;
		}
		
		// Den Punkt entfernen auf dem man steht.
		shortestPath.remove(0);

		return shortestPath;
		} 
		catch (Exception e) {
			Game.logger.debug("Game.PathFinding.getShortestPath: try/catch exception message: " + e.getMessage());
			if(e.getLocalizedMessage() != null)
			{
				Game.logger.debug("Game.PathFinding.getShortestPath: try/catch localized exception message: " + e.getLocalizedMessage());
			}
			return null;
		}
	}

	/**
	 * Diese Methode pr�ft auf die G�ltigkeit eines Feldes, ob sie schon in der closedPointsSet ist und ob sie schon in der openList ist,
	 * wenn g�ltig und in keiner der beiden Listen bzw Mengen, dann wird der WayPoint returned, andernfalls erfolgt return null;
	 * @param wp Der zu �berpr�fende WayPoint
	 * @param mapWidth Die Kartenh�he
	 * @param mapHeight Die Kartenbreite
	 * @return Der WayPoint sofern er g�ltig ist, andernfalls null<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	private WayPoint getValidNeighbor(int[][] map, WayPoint wp, int mapWidth, int mapHeight)
	{
		boolean copy = false;
		
		// Wenn der Punkt in der closedPointsSet ist, dann wird null returned
		if(closedPointsSet.contains(new Point(wp.x, wp.y)))
		{			
			return null;
		}
		
		// Pr�fung ob das Feld g�ltig ist f�r wp
		if ((wp.x >= 0 && wp.x < mapWidth && wp.y >= 0 && wp.y < mapHeight))
		{
			// Wenn das Feld g�ltig ist, wird hier gepr�ft, ob das Feld schon in der openList ist oder nicht, wenn ja wird copy = true gesetzt
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).x == wp.x && openList.get(i).y == wp.y)
				{
					copy = true; // Wenn das Feld schon vorhanden ist
				}								
			}
			if(!copy) // Wenn das Feld nicht in der openList vorhanden ist, dann wird es jetzt hinzugef�gt
			{
				if(map[wp.x][wp.y] == 1) // begehbares Feld
				{
					return wp;
				}
				else // nicht begehbares Feld
				{
					return null;
				}
			}
		}
		return null;
	}
	

	/**
	 * Diese Methode berechnet den Abstand zwischen 2 Punkten f�r den Fog of War.<br>
	 * 
	 * @param startPoint Der Startpunkt von dem aus die Distanz zum Endpunkt ermittelt werden soll.
	 * @param endPoint Der Endpunkt zu dem die Distanz vom Startpunkt ermittelt werden soll.
	 * @return Die Distanz zwischen diesen beiden Punkten.
	 * 
	 * @author Markus Strobel<br>
	 * @since 09.12.2012<br>
	 */
	public Integer getDistanceForFOW(Point startPoint, Point endPoint)
	{			
		// Y-Wert <-> Zur selben Zeile kommen und die Distanz dazu berechnen 
		int yDistance = Math.abs(endPoint.y - startPoint.y);

		// Wird f�r das anpassen der Zeile ben�tigt.
		// addiert f�r je 2 yDistance Einheiten einen xOffSet hinzu.
		int xROffSet = 0;
		int xLOffSet = 0;
		
		int distance = yDistance;

		
		boolean right = false;
		boolean left = false;
		
		
		// Y-DISTANZ IST GERADE
		if(yDistance % 2 == 0)
		{
			// ENDPUNKT OBERHALT STARTPUNKT			 ODER IN GLEICHER ZEILE
			if(endPoint.y >= startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				//		 OOOOOOO	######O		####O##		innerhalb der Kegelfl�che ist alles in Distanz Y zu erreichen, dar�ber hinaus muss X addiert werden.
				//		 #OOOOO#	#####O#		#####O#
				//		 ##OOO##	####O##		####O##
				//		 ###O###	###O###		###O###		etc

				if(startPoint.y % 2 == 0)
				{
					if(endPoint.y == startPoint.y){
						xROffSet--;
					}
					xROffSet++;
				}
				else
				{
					if(endPoint.y == startPoint.y){
						xLOffSet--;
					}
					xLOffSet++;
				}

				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
					left = true;
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
					right = true;
				}
			}
			
			// ENDPUNKT UNTERHALB STARTPUNKT
			else if(endPoint.y < startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 0)
				{
					if(endPoint.y == startPoint.y){
						xROffSet--;
					}
					xROffSet++;
				}
				else
				{
					if(endPoint.y == startPoint.y){
						xLOffSet--;
					}
					xLOffSet++;
				}
				
				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
					left = true;
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
					right = true;
				}	
			}			

		}
		// Y-DISTANZ IST UNGERADE
		else
		{
			// ENDPUNKT OBERHALT STARTPUNKT			 ODER IN GLEICHER ZEILE
			if(endPoint.y >= startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 1)
				{
					xROffSet++;
				}
				else
				{
					xLOffSet++;
				}
				
				// Ob der Punkt weiter links davon liegt
				if(endPoint.x > startPoint.x + xLOffSet ) 
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
					left = true;
				}
				
				// Ob der Punkt weiter rechts davon liegt
				if(endPoint.x < startPoint.x - xROffSet) 
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
					right = true;
				}
			}
			
			// ENDPUNKT UNTERHALB STARTPUNKT
			else if(endPoint.y < startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 1)
				{
					xROffSet++;
				}
				else
				{
					xLOffSet++;
				}
				
				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
					left = true;
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
					right = true;
				}	
			}		
		}
		
		// Nachkorrekturen
		// Startpunkt in gerader Reihe
		if(startPoint.y % 2 == 0)
		{
			// Endpunkt in gerader Reihe
			if(endPoint.y % 2 == 0)
			{
				if(right)
				{
					// in gleicher reihe soll diese Korrektur nicht durchgef�hrt werden
					if(startPoint.y != endPoint.y)
					{
						distance += 1;
					}
				}
			}
		}
		// Startpunkt in ungerader Reihe
		else
		{
			// Endpunkt in gerader Reihe
			if(endPoint.y % 2 == 1)
			{
				if(left)
				{
					// in gleicher reihe soll diese Korrektur nicht durchgef�hrt werden
					if(startPoint.y != endPoint.y)
					{
						distance += 1;
					}
				}
			}
		}
		
		

		return distance;
	}
	

	/**
	 * QUICK & DIRTY =)
	 * 
	 * Diese Methode berechnet den Abstand zwischen 2 Punkten.<br>
	 * 
	 * @param startPoint Der Startpunkt von dem aus die Distanz zum Endpunkt ermittelt werden soll.
	 * @param endPoint Der Endpunkt zu dem die Distanz vom Startpunkt ermittelt werden soll.
	 * @return Die Distanz zwischen diesen beiden Punkten.
	 * 
	 * @author Markus Strobel<br>
	 * @since 09.12.2012<br>
	 */
	public Integer getDistance(Point startPoint, Point endPoint)
	{			
		// Y-Wert <-> Zur selben Zeile kommen und die Distanz dazu berechnen 
		int yDistance = Math.abs(endPoint.y - startPoint.y);

		// Wird f�r das anpassen der Zeile ben�tigt.
		// addiert f�r je 2 yDistance Einheiten einen xOffSet hinzu.
		int xROffSet = 0;
		int xLOffSet = 0;
		
		int distance = yDistance;	
		
		// Y-DISTANZ IST GERADE
		if(yDistance % 2 == 0)
		{
			// ENDPUNKT OBERHALT STARTPUNKT			 ODER IN GLEICHER ZEILE
			if(endPoint.y >= startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				//		 OOOOOOO	######O		####O##		innerhalb der Kegelfl�che ist alles in Distanz Y zu erreichen, dar�ber hinaus muss X addiert werden.
				//		 #OOOOO#	#####O#		#####O#
				//		 ##OOO##	####O##		####O##
				//		 ###O###	###O###		###O###		etc

				if(startPoint.y % 2 == 0)
				{
					if(endPoint.y == startPoint.y){
						xROffSet--;
					}
					xROffSet++;
				}
				else
				{
					if(endPoint.y == startPoint.y){
						xLOffSet--;
					}
					xLOffSet++;
				}

				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
				}
			}
			
			// ENDPUNKT UNTERHALB STARTPUNKT
			else if(endPoint.y < startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 0)
				{
					if(endPoint.y == startPoint.y){
						xROffSet--;
					}
					xROffSet++;
				}
				else
				{
					if(endPoint.y == startPoint.y){
						xLOffSet--;
					}
					xLOffSet++;
				}
				
				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
				}	
			}			

		}
		// Y-DISTANZ IST UNGERADE
		else
		{
			// ENDPUNKT OBERHALT STARTPUNKT			 ODER IN GLEICHER ZEILE
			if(endPoint.y >= startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 1)
				{
					xROffSet++;
				}
				else
				{
					xLOffSet++;
				}
				
				// Ob der Punkt weiter links davon liegt
				if(endPoint.x > startPoint.x + xLOffSet ) 
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
				}
				
				// Ob der Punkt weiter rechts davon liegt
				if(endPoint.x < startPoint.x - xROffSet) 
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
				}
			}
			
			// ENDPUNKT UNTERHALB STARTPUNKT
			else if(endPoint.y < startPoint.y)
			{
				xLOffSet = xROffSet = yDistance / 2;

				if(startPoint.y % 2 == 1)
				{
					xROffSet++;
				}
				else
				{
					xLOffSet++;
				}
				
				if(endPoint.x > startPoint.x + xLOffSet ) // Ob der Punkt weiter links davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x + xLOffSet));
				}

				if(endPoint.x < startPoint.x - xROffSet) // Ob der Punkt weiter rechts davon liegt
				{
					distance += Math.abs(endPoint.x - (startPoint.x - xROffSet));
				}	
			}		
		}

		return distance;
	}
	
	
	/**
	 * Diese Methode gibt den k�rzesten Weg vom Startpunkt unitPoint bist zu den Zielkoordinaten des Zielknoten (x_destination, z_destination)
	 * 
	 * @param map Die Karte des Spiels
	 * @param mapHeight Die Kartenh�he
	 * @param mapWidth Die Kartenbreite
	 * @param units Die Liste der Einheiten
	 * @param unitPoint Der Startpunkt von dem aus der shortestPath berechnet wird
	 * @param x_destination Die X-Koordinate des Zielknotens
	 * @param z_destination Die Z-Koordinate des Zielknotens
	 * @param disableFriendlyUnitCollision Wenn dieser Wert auf true ist, dann k�nnen Einheiten durch verb�ndete Einheiten durchlaufen
	 * @return Eine ArrayList<Point> welche vom Start bis zum Zielknoten den k�rzesten Weg beschreibt<br><br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 14.12.2012<br>
	 * 
	 */
	public ArrayList<Point> getShortestPathFromPointToPoint(int map[][], int mapHeight, int mapWidth, ArrayList<Unit> units, Point unitPoint, Point targetPoint, boolean disableFriendlyUnitCollision)
	{
				
		try {			

		// Die ArrayListen / Mengen resetten
		shortestPath = new ArrayList<Point>();
		openList = new ArrayList<WayPoint>(); 
		closedSet = new LinkedHashSet<WayPoint>();
		closedPointsSet = new LinkedHashSet<Point>();	
		Unit selectedUnit = null;
		
		// Die ausgew�hlte Einheit
		for(int i = 0; i < units.size(); i++)
		{
			if(units.get(i).x == unitPoint.x && units.get(i).y == unitPoint.y)
			{
				selectedUnit = units.get(i);
			}				
		}	
		
		// Wenn es eine ausgew�hlte Einheit gibt, dann werden andere Einheiten bei der Wegfindung ber�cksichtigt
		if(selectedUnit != null)
		{
			// Knoten, welche von Einheiten besetzt sind, werden der closedPointsSet Menge hinzugef�gt
			for(int i = 0; i < units.size(); i++)
			{
				// f�gt nur Knoten mit feindlichen Einheiten darauf der closedPointsSet hinzu
				if(disableFriendlyUnitCollision)
				{
					if(units.get(i).ownerID != selectedUnit.ownerID)
					{
						if(units.get(i).x != targetPoint.x || units.get(i).y != targetPoint.y)
						{
							closedPointsSet.add(new Point(units.get(i).x, units.get(i).y));
						}
					}
				}
				// f�gt alle Knoten mit Einheiten darauf der closedPointsSet hinzu
				else
				{
					if(units.get(i).x != targetPoint.x || units.get(i).y != targetPoint.y)
					{
						closedPointsSet.add(new Point(units.get(i).x, units.get(i).y));
					}
				}			
			}
		}

		// Der Startknoten der ausgew�hlten Einheit
		WayPoint startPoint = new WayPoint(unitPoint.x , unitPoint.y, 0, targetPoint.x, targetPoint.y);
		WayPoint cP = startPoint; // currentPoint
		WayPoint temporaryPoint = null;
		boolean noPossiblePath = false;
		boolean initOpenList = false;		

		// Solange der k�rzeste Weg nicht gefunden wurde ist dieser Wert false
		boolean shortestWay = false;
		
		// Der Hauptteil des Algorithmus
		while(!shortestWay)
		{
			
			// An dieser Stelle wird gepr�ft ob das Ziel erreicht wurde
			if(cP.heuristicValue == 0)
			{
				shortestWay = true;
				continue;
			}
			if(openList.size() == 0 && initOpenList)
			{
				shortestWay = true;
				noPossiblePath = true;
				continue;
			}
			
			
			// Erstellen der Open-Menge
			// gerade Reihe
			if(cP.y % 2 == 0)
			{								
				WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // rechts
				WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // links
				WayPoint neighbor3 = new WayPoint(cP.x, 	cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben rechts
				WayPoint neighbor4 = new WayPoint(cP.x + 1, cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten links				
				WayPoint neighbor5 = new WayPoint(cP.x + 1, cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben links
				WayPoint neighbor6 = new WayPoint(cP.x, 	cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten rechts

				// G�ltigkeits-Pr�fung f�r neighbor1
				WayPoint temp = getValidNeighbor(map, neighbor1, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}				

				// G�ltigkeits-Pr�fung f�r neighbor2
				temp = getValidNeighbor(map, neighbor2, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}					

				// G�ltigkeits-Pr�fung f�r neighbor3
				temp = getValidNeighbor(map, neighbor3, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor4
				temp = getValidNeighbor(map, neighbor4, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor5
				temp = getValidNeighbor(map, neighbor5, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor6
				temp = getValidNeighbor(map, neighbor6, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}				
			}
			// ungerade Reihe
			else
			{				
				WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // rechts
				WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // links
				WayPoint neighbor3 = new WayPoint(cP.x - 1, cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben rechts
				WayPoint neighbor4 = new WayPoint(cP.x	  , cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten links				
				WayPoint neighbor5 = new WayPoint(cP.x	  , cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben links
				WayPoint neighbor6 = new WayPoint(cP.x - 1, cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten rechts

				// G�ltigkeits-Pr�fung f�r neighbor1
				WayPoint temp = getValidNeighbor(map, neighbor1, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor2
				temp = getValidNeighbor(map, neighbor2, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor3
				temp = getValidNeighbor(map, neighbor3, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor4
				temp = getValidNeighbor(map, neighbor4, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor5
				temp = getValidNeighbor(map, neighbor5, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}

				// G�ltigkeits-Pr�fung f�r neighbor6
				temp = getValidNeighbor(map, neighbor6, mapWidth, mapHeight);
				if(temp != null)
				{
					openList.add(temp);
					initOpenList = true;
				}
				

			}			
			// Removing the currentPoint (cP) from the openList
			int cp_index = -1;
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).x == cP.x && openList.get(i).y == cP.y)
				{
					cp_index = i;
				}
			}
			if(cp_index != -1)
				openList.remove(cp_index);

			// initieren des min Wertes f�r die Kosten + Heuristic Pr�fung
			// Herausfinden welcher Knoten als n�chstes ausgew�hlt wird
			int minCost = mapWidth * mapHeight; // skalierbar f�r gr��ere Kartengr��en und so wird garantiert ein niedrigerer minCost-Wert vorhanden sein
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).totalCostValue < minCost)
				{
					minCost = openList.get(i).totalCostValue;
					temporaryPoint = openList.get(i);
				}
			}

			// Der aktuelle Knoten wird als Point der closedSet hinzugef�gt und der neue aktuelle Knoten wird gesetzt
			closedPointsSet.add(new Point(cP.x, cP.y));

			closedSet.add(cP);

			// Setzen des neuen aktuellen Knotens
			cP = temporaryPoint;

		}

		// Hier wird der k�rzeste Weg zu den Elternknoten zur�ckgegangen und gespeichert
		ArrayList<Point> reversePointList = new ArrayList<Point>();
		boolean finished = false;
		while(!finished)
		{						
			if(cP != null)
			{
				reversePointList.add(new Point(cP.x, cP.y));
			}
			else
			{
				finished = true;
				continue;
			}
			cP = cP.getParentWayPoint();
		}
		
		// Hier wird die ArrayList umgedreht und anschlie�end ausgegeben.
		for(int i = reversePointList.size() - 1; i >= 0; i--)
		{
			shortestPath.add(reversePointList.get(i));
		}		
		
		if(noPossiblePath)
		{
			shortestPath = null;
		}
		
		// Den Punkt entfernen auf dem man steht.
		shortestPath.remove(0);

		return shortestPath;
		} 
		catch (Exception e) {
			Game.logger.debug("Game.PathFinding.getShortestPath: try/catch exception message: " + e.getMessage());
			if(e.getLocalizedMessage() != null)
			{
				Game.logger.debug("Game.PathFinding.getShortestPath: try/catch localized exception message: " + e.getLocalizedMessage());
			}
			return null;
		}
	}
	
	

	/**
	 * Diese Methode gibt den k�rzesten Weg vom Startpunkt unitPoint bist zu den Zielkoordinaten des Zielknoten (x_destination, z_destination)
	 * 
	 * @param map Die Karte des Spiels
	 * @param mapHeight Die Kartenh�he
	 * @param mapWidth Die Kartenbreite
	 * @param units Die Liste der Einheiten
	 * @param unitPoint Der Startpunkt von dem aus der shortestPath berechnet wird
	 * @param x_destination Die X-Koordinate des Zielknotens
	 * @param z_destination Die Z-Koordinate des Zielknotens
	 * @param disableFriendlyUnitCollision Wenn dieser Wert auf true ist, dann k�nnen Einheiten durch verb�ndete Einheiten durchlaufen
	 * @return Eine ArrayList<Point> welche vom Start bis zum Zielknoten den k�rzesten Weg beschreibt<br><br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 14.12.2012<br>
	 * 
	 */
	public ArrayList<Point> getDirectLineFromPointToPoint(int map[][], int mapHeight, int mapWidth, Point unitPoint, Point targetPoint)
	{
				
		try {			

			// Die ArrayListen / Mengen resetten
			shortestPath = new ArrayList<Point>();
			openList = new ArrayList<WayPoint>(); 
			closedSet = new LinkedHashSet<WayPoint>();
			closedPointsSet = new LinkedHashSet<Point>();	

			// Der Startknoten der ausgew�hlten Einheit
			WayPoint startPoint = new WayPoint(unitPoint.x , unitPoint.y, 0, targetPoint.x, targetPoint.y);
			WayPoint cP = startPoint; // currentPoint
			WayPoint temporaryPoint = null;
			boolean noPossiblePath = false;
			boolean initOpenList = false;		

			// Solange der k�rzeste Weg nicht gefunden wurde ist dieser Wert false
			boolean shortestWay = false;

			// Der Hauptteil des Algorithmus
			while(!shortestWay)
			{

				// An dieser Stelle wird gepr�ft ob das Ziel erreicht wurde
				if(cP.heuristicValue == 0)
				{
					shortestWay = true;
					continue;
				}
				if(openList.size() == 0 && initOpenList)
				{
					shortestWay = true;
					noPossiblePath = true;
					continue;
				}


				// Erstellen der Open-Menge
				// gerade Reihe
				if(cP.y % 2 == 0)
				{								
					WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // rechts
					WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // links
					WayPoint neighbor3 = new WayPoint(cP.x, 	cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben rechts
					WayPoint neighbor4 = new WayPoint(cP.x + 1, cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten links				
					WayPoint neighbor5 = new WayPoint(cP.x + 1, cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben links
					WayPoint neighbor6 = new WayPoint(cP.x, 	cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten rechts

					// G�ltigkeits-Pr�fung f�r neighbor1
					WayPoint temp = getAllNeighbors(map, neighbor1, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}				

					// G�ltigkeits-Pr�fung f�r neighbor2
					temp = getAllNeighbors(map, neighbor2, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}					

					// G�ltigkeits-Pr�fung f�r neighbor3
					temp = getAllNeighbors(map, neighbor3, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor4
					temp = getAllNeighbors(map, neighbor4, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor5
					temp = getAllNeighbors(map, neighbor5, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor6
					temp = getAllNeighbors(map, neighbor6, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}				
				}
				// ungerade Reihe
				else
				{				
					WayPoint neighbor1 = new WayPoint(cP.x - 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // rechts
					WayPoint neighbor2 = new WayPoint(cP.x + 1, cP.y	, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // links
					WayPoint neighbor3 = new WayPoint(cP.x - 1, cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben rechts
					WayPoint neighbor4 = new WayPoint(cP.x	  , cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten links				
					WayPoint neighbor5 = new WayPoint(cP.x	  , cP.y + 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // oben links
					WayPoint neighbor6 = new WayPoint(cP.x - 1, cP.y - 1, cP.costValue + 1, targetPoint.x, targetPoint.y, cP); // unten rechts

					// G�ltigkeits-Pr�fung f�r neighbor1
					WayPoint temp = getAllNeighbors(map, neighbor1, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor2
					temp = getAllNeighbors(map, neighbor2, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor3
					temp = getAllNeighbors(map, neighbor3, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor4
					temp = getAllNeighbors(map, neighbor4, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor5
					temp = getAllNeighbors(map, neighbor5, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}

					// G�ltigkeits-Pr�fung f�r neighbor6
					temp = getAllNeighbors(map, neighbor6, mapWidth, mapHeight);
					if(temp != null)
					{
						openList.add(temp);
						initOpenList = true;
					}


				}			
				// Removing the currentPoint (cP) from the openList
				int cp_index = -1;
				for(int i = 0; i < openList.size(); i++)
				{
					if(openList.get(i).x == cP.x && openList.get(i).y == cP.y)
					{
						cp_index = i;
					}
				}
				if(cp_index != -1)
					openList.remove(cp_index);

				ArrayList<WayPoint> tempPointList = new ArrayList<WayPoint>();
				// initieren des min Wertes f�r die Kosten + Heuristic Pr�fung
				// Herausfinden totalCostValue der geringste ist
				int minCost = mapWidth * mapHeight; // skalierbar f�r gr��ere Kartengr��en und so wird garantiert ein niedrigerer minCost-Wert vorhanden sein
				for(int i = 0; i < openList.size(); i++)
				{
					if(openList.get(i).totalCostValue < minCost)
					{
						minCost = openList.get(i).totalCostValue;
					}
				}
				
				// Nimmt die BEIDEN WayPoints die den totalCostValue == minCost haben.
				for(int i = 0; i < openList.size(); i++)
				{
					if(openList.get(i).totalCostValue == minCost)
					{
						tempPointList.add(openList.get(i));
					}
				}
				
				
				// TODO ab hier MUSS entschieden werden anhand irgend einer noch zu erstellenden funktion welcher der beiden Punkte genommen wird
				// sodass eine exakte linie vom Start bis zum Zielknoten entsteht.
				// Der ausgew�hlte Punkt wird nachdem cP der closedSet hinzugef�gt wurde entsprechend zum neuen cP
				// temporaryPoint = TODO
				

				// Der aktuelle Knoten wird als Point der closedSet hinzugef�gt und der neue aktuelle Knoten wird gesetzt
				closedPointsSet.add(new Point(cP.x, cP.y));

				closedSet.add(cP);

				// Setzen des neuen aktuellen Knotens
				cP = temporaryPoint;

			}

			// Hier wird der k�rzeste Weg zu den Elternknoten zur�ckgegangen und gespeichert
			ArrayList<Point> reversePointList = new ArrayList<Point>();
			boolean finished = false;
			while(!finished)
			{						
				if(cP != null)
				{
					reversePointList.add(new Point(cP.x, cP.y));
				}
				else
				{
					finished = true;
					continue;
				}
				cP = cP.getParentWayPoint();
			}

			// Hier wird die ArrayList umgedreht und anschlie�end ausgegeben.
			for(int i = reversePointList.size() - 1; i >= 0; i--)
			{
				shortestPath.add(reversePointList.get(i));
			}		

			if(noPossiblePath)
			{
				shortestPath = null;
			}

			// Den Punkt entfernen auf dem man steht.
			shortestPath.remove(0);

			return shortestPath;
		} 
		catch (Exception e) {
			Game.logger.debug("Game.PathFinding.getShortestPath: try/catch exception message: " + e.getMessage());
			if(e.getLocalizedMessage() != null)
			{
				Game.logger.debug("Game.PathFinding.getShortestPath: try/catch localized exception message: " + e.getLocalizedMessage());
			}
			return null;
		}
	}

	/**
	 * Diese Methode pr�ft auf die G�ltigkeit eines Feldes, ob sie schon in der closedPointsSet ist und ob sie schon in der openList ist,
	 * wenn g�ltig und in keiner der beiden Listen bzw Mengen, dann wird der WayPoint returned, andernfalls erfolgt return null;
	 * @param wp Der zu �berpr�fende WayPoint
	 * @param mapWidth Die Kartenh�he
	 * @param mapHeight Die Kartenbreite
	 * @return Der WayPoint sofern er g�ltig ist, andernfalls null<br><br>
	 * 
	 * @author Markus Strobel
	 * @since 19.11.2012<br>
	 */
	private WayPoint getAllNeighbors(int[][] map, WayPoint wp, int mapWidth, int mapHeight)
	{
		boolean copy = false;

		// Wenn der Punkt in der closedPointsSet ist, dann wird null returned
		if(closedPointsSet.contains(new Point(wp.x, wp.y)))
		{			
			return null;
		}

		// Pr�fung ob das Feld g�ltig ist f�r wp
		if ((wp.x >= 0 && wp.x <= mapWidth && wp.y >= 0 && wp.y <= mapHeight))
		{
			// Wenn das Feld g�ltig ist, wird hier gepr�ft, ob das Feld schon in der openList ist oder nicht, wenn ja wird copy = true gesetzt
			for(int i = 0; i < openList.size(); i++)
			{
				if(openList.get(i).x == wp.x && openList.get(i).y == wp.y)
				{
					copy = true; // Wenn das Feld schon vorhanden ist
				}								
			}
			if(!copy) // Wenn das Feld nicht in der openList vorhanden ist, dann wird es jetzt hinzugef�gt
			{
				return wp;
			}
		}
		return null;
	}

	
	
	
}
