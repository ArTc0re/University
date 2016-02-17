package sepsis.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.jme3.post.filters.GammaCorrectionFilter;
import com.jme3.system.AppSettings;
import sepsis.game.Game;
import sepsis.network.Network;
/**
 * Diese Hilfs-Klasse dient dem Laden/Speichern der AppSettings für Sepsis - Dawn of the DNA.<br>
 * 
 * @author Markus Strobel<br>
 * @since 08.01.2013<br>
 *
 */
public class SepsisAppSettingsContainer {

	public String resolution;
	public String colordepth;
	public String multisample;
	public float gamma;
	public float brightness;
	public boolean fullscreen;
	public boolean vsync; 
	public String style;
	public String language;
	public boolean savesettings;
	public float mastervolume;
	public float soundvolume;
	public float musicvolume;
	public String ip;
	public String port;
	
	/**
	 * Der Konstruktor der SepsisAppSettingsContainer-Klasse.<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 08.01.2013<br>
	 */
	public SepsisAppSettingsContainer()
	{
		resolution = "";
		colordepth = "";
		multisample = "";
		gamma = 1.0f;
		brightness = 0.0f;
		fullscreen = false;
		vsync = false; 
		style = "";
		language = "";
		savesettings = true;
		mastervolume = 0.5f;
		soundvolume = 0.5f;
		musicvolume = 0.5f;
		ip = "127.0.0.1";
		port = "1504";	
	}

	
	/**
	 * Diese Methode speichert die sepsisAppSettings.xml für die AppSettings des Spiels<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 08.01.2013<br>
	 *
	 * Quelle: http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	 */
	public void saveSepsisAppSettings()
	{
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			/*
			 * ### root element ###
			 */
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("sepsisAppSettings");
			doc.appendChild(rootElement);

			
		    /*
		     * ### graphics element ###
		     */
			Element graphicElement = doc.createElement("graphic");
			rootElement.appendChild(graphicElement);
			
			// resolution element
			Element resolutionElement = doc.createElement("resolution");
			resolutionElement.appendChild(doc.createTextNode(resolution));
			graphicElement.appendChild(resolutionElement);
			
			// colordepth element
			Element colordepthElement = doc.createElement("colordepth");
			colordepthElement.appendChild(doc.createTextNode(colordepth));
			graphicElement.appendChild(colordepthElement);
			
			// multisample element
			Element multisampleElement = doc.createElement("multisample");
			multisampleElement.appendChild(doc.createTextNode(multisample));
			graphicElement.appendChild(multisampleElement);
			
			// gamma element
			Element gammaElement = doc.createElement("gamma");
			gammaElement.appendChild(doc.createTextNode(String.valueOf(gamma)));
			graphicElement.appendChild(gammaElement);
			
			// brightness element
			Element brightnessElement = doc.createElement("brightness");
			brightnessElement.appendChild(doc.createTextNode(String.valueOf(brightness)));
			graphicElement.appendChild(brightnessElement);
			
			// fullscreen element
			Element fullscreenElement = doc.createElement("fullscreen");
			fullscreenElement.appendChild(doc.createTextNode(String.valueOf(fullscreen)));
			graphicElement.appendChild(fullscreenElement);
			
			// vsync element
			Element vsyncElement = doc.createElement("vsync");
			vsyncElement.appendChild(doc.createTextNode(String.valueOf(vsync)));
			graphicElement.appendChild(vsyncElement);
			
			
			
			
			/*
			 * ### misc element ###
			 */
			Element miscElement = doc.createElement("misc");
			rootElement.appendChild(miscElement);
			
			// style element
			Element styleElement = doc.createElement("style");
			styleElement.appendChild(doc.createTextNode(style));
			miscElement.appendChild(styleElement);
			
			// language element
			Element languageElement = doc.createElement("language");
			languageElement.appendChild(doc.createTextNode(language));
			miscElement.appendChild(languageElement);
			
			// savesettings element
			Element savesettingsElement = doc.createElement("savesettings");
			savesettingsElement.appendChild(doc.createTextNode(String.valueOf(savesettings)));
			miscElement.appendChild(savesettingsElement);
			
			
			
			
			/*
			 * ### audio element ###
			 */
			Element audioElement = doc.createElement("audio");
			rootElement.appendChild(audioElement);
			
			// mastervolume element
			Element mastervolumeElement = doc.createElement("mastervolume");
			mastervolumeElement.appendChild(doc.createTextNode(String.valueOf(mastervolume)));
			miscElement.appendChild(mastervolumeElement);
			
			// soundvolume element
			Element soundvolumeElement = doc.createElement("soundvolume");
			soundvolumeElement.appendChild(doc.createTextNode(String.valueOf(soundvolume)));
			miscElement.appendChild(soundvolumeElement);
			
			// musicvolume element
			Element musicvolumeElement = doc.createElement("musicvolume");
			musicvolumeElement.appendChild(doc.createTextNode(String.valueOf(musicvolume)));
			miscElement.appendChild(musicvolumeElement);
			
			
			
			
			/*
			 * ### network element ###
			 */
			Element networkElement = doc.createElement("network");
			rootElement.appendChild(networkElement);
			
			// ip element
			Element ipElement = doc.createElement("ip");
			ipElement.appendChild(doc.createTextNode(ip));
			miscElement.appendChild(ipElement);
			
			// port element
			Element portElement = doc.createElement("port");
			portElement.appendChild(doc.createTextNode(port));
			miscElement.appendChild(portElement);
			

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/config/sepsisAppSettings.xml"));
			transformer.transform(source, result);

		} 
		catch (Exception e) 
		{
			Game.logger.info("SepsisAppSettingsHandler.saveSepsisAppSettings: " + e.getMessage());
		} 
	}
	
	/**
	 * Diese Methode lädt die sepsisAppSettings.xml für die AppSettings des Spiels<br>
	 * 
	 * @author Markus Strobel<br>
	 * @since 08.01.2013<br>
	 *
	 */
	public AppSettings loadSepsisAppSettings()
	{
		AppSettings settings = new AppSettings(true);
		try 
		{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//			InputStream inputStream = Sepsis.class.getClassLoader().getResourceAsStream("config/sepsisAppSettings.xml");	
			InputStream inputStream = new FileInputStream("data/config/sepsisAppSettings.xml");
			XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);

			while(reader.hasNext())
			{
				XMLEvent xmlEvent = reader.nextEvent();

				if(xmlEvent.isStartElement())
				{					
					switch(xmlEvent.asStartElement().getName().getLocalPart())
					{
					case "resolution":
						xmlEvent = reader.nextEvent();
						resolution = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: resolution Startelement gefunden.");
						break;
					case "colordepth":
						xmlEvent = reader.nextEvent();
						colordepth = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: colordepth Startelement gefunden.");
						break;
					case "multisample":
						xmlEvent = reader.nextEvent();
						multisample = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: multisample Startelement gefunden.");
						break;
					case "gamma":
						xmlEvent = reader.nextEvent();
						gamma = Float.parseFloat(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: gamma Startelement gefunden.");
						break;
					case "brightness":
						xmlEvent = reader.nextEvent();
						brightness = Float.parseFloat(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: brightness Startelement gefunden.");
						break;
					case "fullscreen":
						xmlEvent = reader.nextEvent();
						fullscreen = Boolean.parseBoolean(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: fullscreen Startelement gefunden.");
						break;
					case "vsync":
						xmlEvent = reader.nextEvent();
						vsync = Boolean.parseBoolean(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: vsync Startelement gefunden.");
						break;
					case "style":
						xmlEvent = reader.nextEvent();
						style = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: style Startelement gefunden.");
						break;
					case "language":
						xmlEvent = reader.nextEvent();
						language = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: language Startelement gefunden.");
						break;
					case "savesettings":
						xmlEvent = reader.nextEvent();
						savesettings = Boolean.parseBoolean(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: savesettings Startelement gefunden.");
						break;
					case "mastervolume":
						xmlEvent = reader.nextEvent();
						mastervolume = Float.parseFloat(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: mastervolume Startelement gefunden.");
						break;
					case "soundvolume":
						xmlEvent = reader.nextEvent();
						soundvolume = Float.parseFloat(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: soundvolume Startelement gefunden.");
						break;
					case "musicvolume":
						xmlEvent = reader.nextEvent();
						musicvolume = Float.parseFloat(xmlEvent.asCharacters().getData());
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: musicvolume Startelement gefunden.");
						break;
					case "ip":
						xmlEvent = reader.nextEvent();
						ip = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: ip Startelement gefunden.");
						break;
					case "port":
						xmlEvent = reader.nextEvent();
						port = xmlEvent.asCharacters().getData();
						Game.logger.debug("SepsisAppSettingsContainer.parseFromXML: port Startelement gefunden.");
						break;
					}
				}			
			}
			
			String resolutionList[] = resolution.split("x");
			settings.setResolution(Integer.parseInt(resolutionList[0]), Integer.parseInt(resolutionList[1]));
			settings.setBitsPerPixel(Integer.parseInt(colordepth));
			settings.setSamples(Integer.parseInt(multisample));
			
			GammaCorrectionFilter gammaCorrection = new GammaCorrectionFilter();
			gammaCorrection.setGamma(gamma);
			
			// TODO set BRIGHTNESS
			
			
			settings.setFullscreen(fullscreen);
			settings.setVSync(vsync);
			
			Network.IP = ip;
			Network.PORT = Integer.parseInt(port);
			

			settings.setTitle("Sepsis - Dawn of the DNA");
			return settings;
		} 
		catch (Exception e) 
		{
			Game.logger.error("SepsisAppSettingsContainer.loadSepsisAppSettings(): catch: " + e.getMessage());
			Game.logger.info("SepsisAppSettingsContainer.loadSepsisAppSettings(): catch: defaults loaded");
			// default werte laden und setzen
			settings = new AppSettings(true);
			
			settings.setResolution(1024, 768);
			settings.setTitle("Sepsis - Dawn of the DNA");
			return settings;
		}
	}
}
