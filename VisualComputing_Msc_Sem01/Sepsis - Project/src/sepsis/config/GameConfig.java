package sepsis.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;	

import sepsis.game.Game;

/**
 * Diese Klasse liest den Map-Name und den hexMapCSV MapConfig.xml definiert sind.
 * 
 * @author Markus Strobel
 * @since 22.11.2012
 *
 */
public class GameConfig {	
	
	/**
	 * Der Name der Karte
	 */
	private String mapName;	
	/**
	 * Der Pfad der hexMapCSV
	 */
	private String hexMapCSV;
	
	/**
	 * Der Konstruktor der GameConfig, mit dem instanzieren wird direkt geparst, sodass alle benötigten Informationen direkt vorhanden sind<br>
	 * 
	 * @author Markus Strobel
	 * @since 22.11.12 <br>
	 */
	public GameConfig()
	{
		
	}
	
	/**
	 * Diese Methode liest die GameConfig.xml, um den mapname und den hexmapcsv value zu erhalten.
	 * 
	 * @param xmlfile Der Pfad der XML-Datei<br>
	 * 
	 * @author Markus Strobel
	 * @since 22.11.12 <br>
	 */
	public void parseFromXML(String xmlfile) {

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
					case "name":
						xmlEvent = reader.nextEvent();
						mapName = xmlEvent.asCharacters().getData();
						Game.logger.debug("GameConfig.parseFromXML: name Startelement gefunden. Neuer UnitType instanziert.");
						break;
					case "hexmapcsv":
						xmlEvent = reader.nextEvent();
						hexMapCSV = xmlEvent.asCharacters().getData();
						Game.logger.debug("GameConfig.parseFromXML: hexmapcsv Startelement gefunden. Wert zugewiesen.");
						break;
					}
				}			
			}
		} 
		catch (FileNotFoundException e) 
		{
			Game.logger.error("GameConfig.parseFromXML: " + e.getMessage());
		} 
		catch (XMLStreamException e) 
		{
			Game.logger.error("GameConfig.parseFromXML: " + e.getMessage());
		}		
	}

	/**
	 * This Method returns the mapName value
	 * @return a string with the mapName value
	 * 
	 * @author Markus Strobel
	 * @since 22.11.2012
	 */
	public String getMapName()
	{
		return mapName;
	}
	
	/**
	 * This Method returns the hexmapcsv value
	 * @return a string with the hexmapcsv value
	 * 
	 * @author Markus Strobel
	 * @since 22.11.2012
	 */
	public String getHexMapCSV()
	{
		return hexMapCSV;
	}
}