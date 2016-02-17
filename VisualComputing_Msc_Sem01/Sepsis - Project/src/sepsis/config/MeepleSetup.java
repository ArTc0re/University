package sepsis.config;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;	
import sepsis.game.Game;
import sepsis.game.Unit;
import sepsis.game.UnitSetup;
import sepsis.game.UnitType;
import sepsis.network.ComObject_tradestations;
import sepsis.network.ComObject_unitinfo;
import sepsis.network.IComObject.STATUS;
import sepsis.network.Network;

/**
 * Diese Klasse erstellt die Liste der Einheiten, welche auf dem Spielfeld vorhanden sind.
 * 
 * @author Markus Strobel
 * @since 16.11.2012
 *
 */
public class MeepleSetup {
	
	/**
	 * Die Liste der Einheiten, die sich bei Spielstart im Spiel befinden.
	 */
	private ArrayList<UnitSetup> meepleSetups;
	/**
	 * Eine Einheit die sich bei Spielstart im Spiel befindet
	 */
	private UnitSetup unitSetup;
	
	/**
	 * Diese Klasse erstellt eine List der Einheiten auf dem Spielfeld.<br>
	 * 
	 * @author Markus Strobel
	 * @since 16.11.2012<br>
	 */
	public MeepleSetup()
	{
		meepleSetups = new ArrayList<UnitSetup>();	
	}
	

	/**
	 * Diese Methode liest die MeepleSetup.xml ein und sammelt die Daten in einer ArrayList mit Units<br>
	 * 
	 * @param xmlFile Der Pfad der XML-Datei<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.12 <br>
	 */
	public void parseFromXML(String xmlFile) {
		meepleSetups.clear();
		try 
		{			
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream inputStream = new FileInputStream(xmlFile);	
			XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);
			
			while(reader.hasNext())
			{
				XMLEvent xmlEvent = reader.nextEvent();
				
				if(xmlEvent.isStartElement())
				{					
					switch(xmlEvent.asStartElement().getName().getLocalPart())
					{
					case "meeple":
						unitSetup = new UnitSetup();
						Game.logger.debug("MeepleSetup.parseFromXML: meeple Startelement gefunden. Neue UnitSetup instanziert.");
						break;
					case "id":
						xmlEvent = reader.nextEvent();
						unitSetup.id = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: id Startelement gefunden. Wert zugewiesen.");
						break;
					case "type":
						xmlEvent = reader.nextEvent();
						unitSetup.type = xmlEvent.asCharacters().getData();
						Game.logger.debug("MeepleSetup.parseFromXML: type Startelement gefunden. Wert zugewiesen.");
						break;
					case "ownerindex":
						xmlEvent = reader.nextEvent();
						unitSetup.ownerID = xmlEvent.asCharacters().getData();
						Game.logger.debug("MeepleSetup.parseFromXML: ownerIndex Startelement gefunden. Wert zugewiesen.");
						break;
					case "positionx":
						xmlEvent = reader.nextEvent();
						unitSetup.positionX = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: positionX Startelement gefunden. Wert zugewiesen.");
						break;
					case "positiony":
						xmlEvent = reader.nextEvent();
						unitSetup.positionY = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: positionY Startelement gefunden. Wert zugewiesen.");
						break;
					case "cargo1":
						xmlEvent = reader.nextEvent();
						unitSetup.cargo_1 = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: cargo_1 Startelement gefunden. Wert zugewiesen.");
						break;	
					case "cargo2":
						xmlEvent = reader.nextEvent();
						unitSetup.cargo_2 = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: cargo_2 Startelement gefunden. Wert zugewiesen.");
						break;	
					case "cargo3":
						xmlEvent = reader.nextEvent();
						unitSetup.cargo_3 = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: cargo_3 Startelement gefunden. Wert zugewiesen.");
						break;	
					case "cargo4":
						xmlEvent = reader.nextEvent();
						unitSetup.cargo_4 = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: cargo_4 Startelement gefunden. Wert zugewiesen.");
						break;	
					case "cargo5":
						xmlEvent = reader.nextEvent();
						unitSetup.cargo_5 = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleSetup.parseFromXML: cargo_5 Startelement gefunden. Wert zugewiesen.");
						break;	
					case "hitpoints":
						xmlEvent = reader.nextEvent();
						unitSetup.currentHitPoints = Integer.parseInt(xmlEvent.asCharacters().getData());
						unitSetup.hasCustomHitPointValue = true;
						Game.logger.debug("MeepleSetup.parseFromXML: currentHitPoints Startelement gefunden. Wert zugewiesen.");
						Game.logger.debug("MeepleSetup.parseFromXML: hasCustomHitPointValue auf true gesetzt.");
						break;	
					}
				}
				if(xmlEvent.isEndElement())
				{
					if(xmlEvent.asEndElement().getName().getLocalPart().equals("meeple"))
					{
						meepleSetups.add(unitSetup);
						Game.logger.debug("MeepleSetup.parseFromXML: meeple Endelement gefunden. unitSetup der ArrayList<UnitSetup> hinzugefügt.");
					}						
				}				
			}
		} 
		catch (FileNotFoundException e) 
		{
			Game.logger.error("MeepleSetup.parseFromXML: " + e.getMessage());
		} 
		catch (XMLStreamException e) 
		{
			Game.logger.error("MeepleSetup.parseFromXML: " + e.getMessage());
		}
		
	}
	
	/**
	 * Diese Methode gibt die ArrayList<UnitSetup> zurück, welche die Startkonfiguration der Einheiten auf dem Spielfeld enthält<br>
	 * 
	 * @return Die ArrayList der Einheiten auf dem Spielfeld<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 */
	public ArrayList<UnitSetup> getUnitSetups()
	{
		return meepleSetups;
	}
	

	/**
	 * Diese Methode verwendet die Daten aus der Netzwerkkommunikation, um eine UnitSetup zu erstellen.<br>
	 * 
	 * @param unitinfo das ComObject_unitinfo Objekt, welches die unitinfo einer Einheiten ID repräsentiert.<br>
	 * @param unitMap das ComObject_unitMap Objekt, welches die unitMap Daten beinhaltet und somit die Positionen jeder Einheiten ID.<br>
	 * @return Gibt das UnitSetup der aktuellen unitinfo aus.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.12.2012<br>
	 */
	public ArrayList<UnitSetup> parseFromNetwork(ArrayList<ComObject_unitinfo> unitinfos, ArrayList<Integer> unitIDsList, ArrayList<Point> unitPointsList)
	{		
		meepleSetups = new ArrayList<UnitSetup>();

		for(int i = 0; i < unitinfos.size(); i++)
		{
			UnitSetup meepleSetup = new UnitSetup();
							
			meepleSetup.id = unitinfos.get(i).getUnitID();
			meepleSetup.type = unitinfos.get(i).getUtype();

			if(Game.playerName.equals(unitinfos.get(i).getOwnerID())) // Für den Spieler
			{
				meepleSetup.ownerID = unitinfos.get(i).getOwnerID();
				meepleSetup.currentHitPoints = unitinfos.get(i).getHitpoints();
				meepleSetup.currentMovementPoints = unitinfos.get(i).getMovement();		
				meepleSetup.cargo_1 = unitinfos.get(i).getCargo().get(0);
				meepleSetup.cargo_2 = unitinfos.get(i).getCargo().get(1);	
				meepleSetup.cargo_3 = unitinfos.get(i).getCargo().get(2);
				meepleSetup.cargo_4 = unitinfos.get(i).getCargo().get(3);
				meepleSetup.cargo_5 = unitinfos.get(i).getCargo().get(4);		
			}
			else // Für den Gegner (da unbekannt)
			{
				meepleSetup.ownerID = unitinfos.get(i).getOwnerID();
				meepleSetup.currentHitPoints = 0;
				meepleSetup.currentMovementPoints = 0;
				meepleSetup.cargo_1 = 0;
				meepleSetup.cargo_2 = 0;
				meepleSetup.cargo_3 = 0;	
				meepleSetup.cargo_4 = 0;
				meepleSetup.cargo_5 = 0;		
			}			
			Game.logger.debug("MeepleSetup.parseFromNetwork: OWNER:" + meepleSetup.ownerID + " ID: " + meepleSetup.id);

			int ID_index = unitIDsList.indexOf(meepleSetup.id);
			meepleSetup.positionX = unitPointsList.get(ID_index).x;
			meepleSetup.positionY = unitPointsList.get(ID_index).y;			

			meepleSetups.add(meepleSetup);
		}
		return meepleSetups;
	}	
	
	
	/**
	 * Diese Methode erstellt die Einheiten anhand der Daten der unitTypes- und unitSetups-Listen<br>
	 * 
	 * @param unitTypes Die unitTypes-Liste, welche die möglichen Einheiten beinhaltet.<br>
	 * @param unitSetups Die unitSetups-Liste, welche die Einheiten auf der Karte beinhaltet.<br>
	 * @return Die fertige Einheitenliste der sich im Spiel befindlichen Einheiten (zum Startzeitpunkt).<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.12.2012<br>
	 */
	public ArrayList<Unit> initUnits(ArrayList<UnitType> unitTypes, ArrayList<UnitSetup> unitSetups)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
			
		// tradestation information
		Network network = new Network();
		ArrayList<ArrayList<Integer>> availableGoods = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> friendlyPlayerIndex = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> tradeEquivalences = new ArrayList<ArrayList<Integer>>();
		ArrayList<String> stationNames = new ArrayList<String>();
		ArrayList<Integer> stationIDs = new ArrayList<Integer>();
		try 
		{
			ComObject_tradestations station = (ComObject_tradestations)network.establishCommunication(new ComObject_tradestations(Game.clientID, Game.clientKey, Game.mapID));
	
			if(station.getStatus().equals(STATUS.OK))
			{
				availableGoods = station.getAvailablegoods();
				friendlyPlayerIndex = station.getFriendlyplayerindexes();
				tradeEquivalences  = station.getTradeequivalences();	
				stationNames = station.getStationID();
				stationIDs = station.getUnitID();

			}
			else
			{
				Game.logger.error("MeepleSetup.initUnits().station: " + station.getErrorInfo());
			}
		} 
		catch (Exception e) 
		{
			Game.logger.error("MeepleSetup.initUnits(): catch: " + e.getMessage());
		}
		

		//Hier wird für jede UnitSetup der Typ aus der UnitTypesList rausgesucht, um anhand dessen die Unit zu erstellen
		for(int i = 0; i < unitSetups.size(); i++)
		{
			UnitType unitType = new UnitType();
			
			for(int j = 0; j < unitTypes.size(); j++)
			{
				//Den passenden Type finden
				if(unitSetups.get(i).type.equals(unitTypes.get(j).type))
				{
					unitType = unitTypes.get(j);
				}
			}
			

			if(unitSetups.get(i).currentHitPoints == 0 && unitSetups.get(i).currentMovementPoints == 0 && unitSetups.get(i).ownerID.equals(""))
			{ // TRADESTATIONS KONSTRUKTOR
				
				int currentIndex = 0;
				
				for(int j = 0; j < stationIDs.size(); j++)
				{
					int unitID = unitSetups.get(i).id;
					int stationID = stationIDs.get(j);
					
					if(unitID == stationID)
					{
						currentIndex = j;
					}
				}
				
				Unit unit = new Unit(stationNames.get(currentIndex), unitSetups.get(i).id, unitSetups.get(i).positionX, unitSetups.get(i).positionY, friendlyPlayerIndex.get(currentIndex), availableGoods.get(currentIndex), tradeEquivalences.get(currentIndex));
				units.add(unit);

			}
			else
			{ // EINHEITEN KONSTRUKTOR
				
				//Die Einheit wird erstellt und der unitList hinzugefügt
				Unit unit = new Unit(unitSetups.get(i), unitType);
				units.add(unit);
			}

		}		

		return units;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}




