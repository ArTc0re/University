package sepsis.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;	

import sepsis.game.Game;
import sepsis.game.UnitType;
import sepsis.network.ComObject_unittype;


/**
 * Diese Klasse erstellt die Liste der Einheiten, welche in der MeepleConfig.xml definiert sind.<br>
 * 
 * @author Markus Strobel<br>
 * @since 17.11.2012<br>
 *
 * 01.12.2012 (Markus Strobel) parseFromNetwork(ComObject_unittype unittype) hinzugefügt.<br>
 */
public class MeepleConfig {
	
	/**
	 * Die Liste der möglichen Einheitentypen des Spiels
	 */
	private ArrayList<UnitType> meepleTypes;
	/**
	 * Ein möglicher Einheitentyp des Spiels
	 */
	private UnitType type;
	
	/**
	 * Der Konstruktor der MeepleConfig, mit dem instanzieren wird direkt geparst, sodass alle benötigten Informationen direkt vorhanden sind<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.12 <br>
	 */
	public MeepleConfig()
	{
		meepleTypes = new ArrayList<UnitType>();	
	}
	
	/**
	 * Diese Methode liest die MeepleConfig.xml ein und sammelt die Daten in einer ArrayList mit UnitTypes<br>
	 * 
	 * @param xmlfile Der Pfad der XML-Datei<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.12 <br>
	 */
	public void parseFromXML(String xmlfile) {
		meepleTypes.clear();
		try 
		{				
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream inputStream = new FileInputStream(xmlfile);	
			XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);

			while(reader.hasNext())
			{
				XMLEvent xmlEvent = reader.nextEvent();
				
				if(xmlEvent.isStartElement())
				{					
					switch(xmlEvent.asStartElement().getName().getLocalPart())
					{
					case "meeple-type":
						type = new UnitType();
						Game.logger.debug("MeepleConfig.parseFromXML: meeple-type Startelement gefunden. Neuer UnitType instanziert.");
						break;
					case "type":
						xmlEvent = reader.nextEvent();
						type.type = xmlEvent.asCharacters().getData();
						Game.logger.debug("MeepleConfig.parseFromXML: type Startelement gefunden. Wert zugewiesen.");
						break;
					case "movement":
						xmlEvent = reader.nextEvent();
						type.movement = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleConfig.parseFromXML: movement Startelement gefunden. Wert zugewiesen.");
						break;
					case "maxhitpoints":
						xmlEvent = reader.nextEvent();
						type.maxHitPoints = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleConfig.parseFromXML: maxHitPoints Startelement gefunden. Wert zugewiesen.");
						break;
					case "maxfirepower":
						xmlEvent = reader.nextEvent();
						type.maxFirePower = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleConfig.parseFromXML: maxFirePower Startelement gefunden. Wert zugewiesen.");
						break;
					case "maxcargo":
						xmlEvent = reader.nextEvent();
						type.maxCargo = Integer.parseInt(xmlEvent.asCharacters().getData());
						Game.logger.debug("MeepleConfig.parseFromXML: maxCargo Startelement gefunden. Wert zugewiesen.");
						break;	
					}
				}
				if(xmlEvent.isEndElement())
				{
					if(xmlEvent.asEndElement().getName().getLocalPart().equals("meeple-type"))
					{
						meepleTypes.add(type);
						Game.logger.debug("MeepleConfig.parseFromXML: meeple-type Endelement gefunden. Type der ArrayList<UnitType> hinzugefügt.");
					}						
				}				
			}
		} 
		catch (FileNotFoundException e) 
		{
			Game.logger.error("MeepleConfig.parseFromXML: " + e.getMessage());
		} 
		catch (XMLStreamException e) 
		{
			Game.logger.error("MeepleConfig.parseFromXML: " + e.getMessage());
		}
		
	}
	
	/**
	 * Diese Methode gibt die ArrayList<UnitType> zurück, welche die möglichen UnitTypes enthält
	 * @return Die ArrayList der möglichen UnitTypes
	 * @author Markus Strobel
	 * @since 17.11.12 <br>
	 */
	public ArrayList<UnitType> getUnitTypes()
	{
		return meepleTypes;
	}
	
	/**
	 * Diese Methode verwendet die Daten aus der Netzwerkkommunikation um die UnitTypesListe zu erstellen.<br>
	 * 
	 * @param unittype das ComObject_unittype Objekt, welches die unittype-Daten des Servers repräsentiert
	 * @return Die ArrayList<UnitType> mit allen Einheiten-Typen die es gibt.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 01.12.2012<br>
	 */
	public ArrayList<UnitType> parseFromNetwork(ComObject_unittype unittype)
	{		
		meepleTypes = new ArrayList<UnitType>();	
		
		// UnitTypes erstellen und unitTypeList füllen.
		for(int i = 0; i < unittype.getType().size(); i++)
		{
			UnitType type = new UnitType();
			type.maxCargo = unittype.getMaxcargo().get(i);
			type.maxFirePower = unittype.getMaxfirepower().get(i);
			type.maxHitPoints = unittype.getMaxhitpoints().get(i);							
			type.movement = unittype.getMaxmovement().get(i);
			type.type = unittype.getType().get(i);
			meepleTypes.add(type);							
		}	
		return meepleTypes;
	}
	
	
	
}






