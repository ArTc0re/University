package sepsis.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import sepsis.game.Game;

/**
 * Diese Klasse stellt den MapConfigParser dar und dient gleichzeitig auch als Container, um die Daten weiter zu reichen.<br>
 * 
 * @author Markus Strobel
 * @since 17.11.2012<br>
 *
 *10.01.2013 (Markus Strobel) WinConditions werden nun auch geparsed.<br>
 */
public class MapConfig{

	/**
	 * Das 2-Dimensionale int-Array, welches die Karte repräsentiert
	 */
	private int map[][];
	/**
	 * Die Breite der Karte
	 */
	private int width;
	/**
	 * Die Höhe der Karte
	 */
	private int height;
	/**
	 * Der Name der Karte
	 */
	private String mapName;
	
	/**
	 * Diese Liste speichert alle <goodplacement> Win Conditions
	 * Jedes <goodplacement> in der XML entspricht einem int[] Array
	 * Die Indices dieses int[] Arrays entsprechen:
	 * 0 : good
	 * 1 : amount
	 * 2 : playerindex
	 * 3 : fieldx
	 * 4 : fieldy
	 */
	public ArrayList<int[]> winConditionsGoodPlacementList;
	
	/**
	 * Dieser Wert entspricht dem <neededconditions> Wert
	 */
	public int GoodPlacementNeededConditions;
	
	/**
	 * Dieser Wert entspricht dem <extinction> Wert
	 */
	public Boolean winConditionExtinction = false;
	
	
	/**
	 * Der Konstruktor des MapConfig Objekts, welches die Kartneninformationen beinhaltet<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 */
	public MapConfig()
	{
		winConditionsGoodPlacementList = new ArrayList<int[]>();
	}
	
	/**
	 * Diese Methode parst die XML-Datei, welche im Pfad xmlfile angegeben ist.<br>
	 * 
	 * @param xmlfile Der Pfad der XML-Datei<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 * 
	 */
	public int[][] parseFromCSV(String csvfile, String mapname) {
		
		mapName = mapname;

		BufferedReader reader;
		String input;
		
		ArrayList<String[]> tempList = new ArrayList<String[]>();
		try {			
			reader = new BufferedReader(new FileReader(new File(csvfile)));
			Game.logger.debug("MapConfig.parseCSV: started to read " + csvfile);
			while((input = reader.readLine()) != null) 
			{
				String[] line = input.split(",");
				tempList.add(line);			    
			}
			
			height = tempList.size();
			width = tempList.get(0).length;
			
			map = new int[width][height];
			
			for(int i = 0; i < height; i++)
			{
				for(int j = 0; j < width; j++)
				{					
					map[j][i] = Integer.parseInt(tempList.get(i)[j]);
				}
			}
			
			reader.close();
		} 
		catch (NumberFormatException e) 
		{
			Game.logger.error("MapConfig.parseCSV: " + e.getMessage() );
		} 
		catch (IOException e) 
		{
			Game.logger.error("MapConfig.parseCSV: " + e.getMessage() );
		}
		
		// Karte im Logger ausgeben (Ursprung liegt links-oben)
		for(int y = 0; y < 25; y++){
			String tmp = "";
			for(int x = 0; x < 25; x++){
				tmp = tmp + String.valueOf(map[width - x - 1][height - y - 1]);
			}
			Game.logger.info(tmp);
		}

		return map;
	}
	
	/**
	 * Diese Methode liest die MapConfig.xml, um den mapname, hexmapcsv-value und die Win-Conditions zu erhalten.<br>
	 * 
	 * @param xmlfile Der Pfad der XML-Datei<br>
	 * 
	 * @author Markus Strobel
	 * @since 10.01.13 <br>
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
						Game.logger.debug("MapConfig.parseFromXML: name Startelement gefunden.");
						break;
					case "winconditions":
						
						while(reader.hasNext())
						{
							xmlEvent = reader.nextEvent();
							
							if(xmlEvent.isStartElement())
							{					
								switch(xmlEvent.asStartElement().getName().getLocalPart())
								{
								case "extinction":
									xmlEvent = reader.nextEvent();
									winConditionExtinction = Boolean.parseBoolean(xmlEvent.asCharacters().getData());
									Game.logger.debug("MapConfig.parseFromXML: extinction Startelement gefunden.");
									break;
								case "neededconditions":
									xmlEvent = reader.nextEvent();
									GoodPlacementNeededConditions = Integer.parseInt(xmlEvent.asCharacters().getData());
									Game.logger.debug("MapConfig.parseFromXML: neededconditions Startelement gefunden.");
									break;
								case "goodplacement":
									Game.logger.debug("MapConfig.parseFromXML: goodplacement Startelement gefunden.");
									int[] goodplacementarray = new int[5];
									
									boolean loop = reader.hasNext();
									while(loop)
									{
										xmlEvent = reader.nextEvent();
										
										if(xmlEvent.isEndElement())
										{
											if(xmlEvent.asEndElement().getName().getLocalPart().equals("goodplacement"))
											{
												winConditionsGoodPlacementList.add(goodplacementarray);
												loop = false;
												break;
											}
										}								
										
										if(xmlEvent.isStartElement())
										{		
										
											switch(xmlEvent.asStartElement().getName().getLocalPart())
											{
											case "good":
												xmlEvent = reader.nextEvent();
												goodplacementarray[0] = Integer.parseInt(xmlEvent.asCharacters().getData());
												Game.logger.debug("MapConfig.parseFromXML: good Startelement gefunden.");
												break;
											case "amount":
												xmlEvent = reader.nextEvent();
												goodplacementarray[1] = Integer.parseInt(xmlEvent.asCharacters().getData());
												Game.logger.debug("MapConfig.parseFromXML: amount Startelement gefunden.");
												break;
											case "playerindex":
												xmlEvent = reader.nextEvent();
												goodplacementarray[2] = Integer.parseInt(xmlEvent.asCharacters().getData());
												Game.logger.debug("MapConfig.parseFromXML: playerindex Startelement gefunden.");
												break;
											case "fieldx":
												xmlEvent = reader.nextEvent();
												goodplacementarray[3] = Integer.parseInt(xmlEvent.asCharacters().getData());
												Game.logger.debug("MapConfig.parseFromXML: fieldx Startelement gefunden.");
												break;
											case "fieldy":
												xmlEvent = reader.nextEvent();
												goodplacementarray[4] = Integer.parseInt(xmlEvent.asCharacters().getData());
												Game.logger.debug("MapConfig.parseFromXML: fieldy Startelement gefunden.");
												break;
											}

										}
										
									}
									// dieser break gehört zum case "goodplacement"
									break;
								}
							}
						}
						// dieser break gehört zum case "winconditions"
						break;
					}
				}			
			}
			reader.close();
		} 
		catch (FileNotFoundException e) 
		{
			Game.logger.error("MapConfig.parseFromXML: " + e.getMessage());
		} 
		catch (XMLStreamException e) 
		{
			Game.logger.error("MapConfig.parseFromXML: " + e.getMessage());
		}	
	}
	
	
	/**
	 * Diese Methode gibt das 2-Dimensionale Array aus, welches die Map repräsentiert
	 * @return Gibt das Array der Map aus
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012
	 */
	public int[][] getMap()
	{
		return map;
	}	
	/**
	 * Diese Methode gibt die Breite der Karte aus<br>
	 * 
	 * @return Die Breite der Karte<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Diese Methode gibt die Höhe der Karte aus<br>
	 * 
	 * @return Die Höhe der Karte<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Diese Methode gibt den Namen der Karte aus<br>
	 * 
	 * @return Der Name der Karte<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 */
	public String getMapName()
	{
		return mapName;
	}
}