package sepsis.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;
import sepsis.network.ComObject_mappreview;

/**
 * Diese Klasse kann dazu verwendet werden, eine Minimap zu erzeugen.
 * 
 * @author Peter Dörr
 * @since 18.11.12
 */
public class Minimap {
	
	/**
	 * Gibt an, welche Informationen die Minimap anzeigen soll.<br>
	 * TERRAIN: Zeigt nur das Terrain an.<br>
	 * UNITS: Zeigt nur Einheiten an.<br>
	 * TERRAIN_UNITS: Zeigt Terrain und Einheiten.<br>
	 * TERRAIN_UNITS_MOVEMENTRANGE: Zeigt Terrain, Einheiten und die Bewegungsreichweite aller Einheiten.
	 */
	public enum MINIMAPMODE {TERRAIN, UNITS, TERRAIN_UNITS, TERRAIN_UNITS_MOVEMENTRANGE}
	/**
	 * Gibt an, ob in die Minimap der Fog of War einfließen soll.
	 */
	public static boolean showFOW = true;
	/**
	 * Gibt an, welche Informationen die Minimap nach dem nächsten Zeichnen anzeigen soll.
	 */
	public static MINIMAPMODE mode = MINIMAPMODE.TERRAIN_UNITS;
	/**
	 * Gibt die Auflösung des mit draw() erzeugten Minimap-Bildes an.
	 */
	private int resolution = 1024;
	
	
	
	/**
	 * Zeichnet eine neue Minimap und speichert diese als minimap.png ab. Die in der Minimap dargestellten Informationen können über setMinimapMode(mode) und setFOG(showFOG) angepasst werden.
	 * 
	 * @param saveAsPNG Gibt an, ob das Bild auch als PNG gespeichert werden soll.
	 * @return Die Minmap als BufferedImage.
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public BufferedImage draw(boolean saveAsPNG) throws Exception{
		
		//Farben
		Color nothing = new Color(0, 0, 0);
		Color terrain_walkable = new Color(200, 200, 200);
		Color terrain_unwalkable = new Color(50, 50, 50);
		Color units_friendly = new Color(0, 200, 0);
		Color units_enemy = new Color(200, 0, 0);
//		Color units_tradestation = new Color(0, 0, 200);
		Color units_tradestation = new Color(255, 125, 255);
		@SuppressWarnings("unused")
		Color units_all = new Color(200, 200, 0);
		Color fog = new Color(100, 100, 100);
		
		//Parameter
		int resolution_x = 4096 * Game.playingField.width / 25;
		int resolution_y = (3072 + 512) * Game.playingField.height / 25;
		int hexagonRadius = 94;
		
		//2D Grafik mit ausreichender Auflösung und schwarzem Hintergrund erzeugen
		BufferedImage bufferedImage = new BufferedImage(resolution_x, resolution_y, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.BLACK);
		graphics2D.fillRect(0, 0, resolution_x, resolution_y);
		
		//Hexfelder einzeichnen
		for(int y = 0; y < Game.playingField.height; y++){
			for(int x = 0; x < Game.playingField.width; x++){
				
				//Farbe festlegen
				switch(mode){
				
				case TERRAIN:
					//Unbegehbare Felder
					if(Game.playingField.map[x][y] == 0){
						graphics2D.setColor(terrain_unwalkable);
					}
					//Begehbare Felder
					if(Game.playingField.map[x][y] == 1){
						graphics2D.setColor(terrain_walkable);
					}
					//Fog of War
					if(showFOW && Game.playingField.visibility[x][y] == false){
						graphics2D.setColor(fog);
					}
					break;
					
				case UNITS:
					//Einheiten
					graphics2D.setColor(nothing);
					for(int i = 0; i < Game.playingField.units.size(); i++){
						if(Game.playingField.units.get(i).x == x && Game.playingField.units.get(i).y == y){
							if(Game.playingField.units.get(i).ownerID != null){
								if(Game.playingField.units.get(i).ownerID.equals(Game.playerID)){
									graphics2D.setColor(units_friendly);
								} else {
									graphics2D.setColor(units_enemy);
								}
							} else {
								graphics2D.setColor(units_tradestation);
							}
						}
					}
					//Fog of War
					if(showFOW && Game.playingField.visibility[x][y] == false){
						graphics2D.setColor(fog);
					}
					break;
					
				case TERRAIN_UNITS:
					//Unbegehbare Felder
					if(Game.playingField.map[x][y] == 0){
						graphics2D.setColor(terrain_unwalkable);
					}
					//Begehbare Felder
					if(Game.playingField.map[x][y] == 1){
						graphics2D.setColor(terrain_walkable);
					}
					//Einheiten
					for(int i = 0; i < Game.playingField.units.size(); i++){
						if(Game.playingField.units.get(i).x == x && Game.playingField.units.get(i).y == y){
							if(Game.playingField.units.get(i).ownerID != null){
								if(Game.playingField.units.get(i).ownerID.equals(Game.playerID)){
									graphics2D.setColor(units_friendly);
								} else {
									graphics2D.setColor(units_enemy);
								}
							} else {
								graphics2D.setColor(units_tradestation);
							}
						}
					}
					//Fog of War
					if(showFOW && Game.playingField.visibility[x][y] == false){
						graphics2D.setColor(fog);
					}
					break;
					
				case TERRAIN_UNITS_MOVEMENTRANGE:
					
					//TODO
					
					break;
				}
				
				//Hexagon erstellen
				double center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (Game.playingField.width + 0.5d);
				double center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (Game.playingField.width + 0.5d);
				double center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * Game.playingField.height + 1)) - (resolution_y / (3 * Game.playingField.height + 1)));

				Polygon hexagon;
				if(y % 2 == 1){
					hexagon = createHexagonWithPointyOrientation(center_x, center_y, hexagonRadius);
				} else {
					hexagon = createHexagonWithPointyOrientation(center_x_offsetted, center_y, hexagonRadius);
				}

				//Hexagon einzeichnen
				graphics2D.fill(hexagon);
			}
		}
		
		//Resize
		RenderedImage renderedImage = commonResize(bufferedImage, resolution, resolution, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		if(saveAsPNG){
			//Alte Dateien löschen
			File file = new File("data/images/minimap.png");
			if(file.exists()){
				file.delete();
			}
			
			//Speichern
			File file2 = new File("data/images/minimap.png");
			ImageIO.write(renderedImage, "png", file2);
		}
		
		return (BufferedImage)renderedImage;
	}
	
	
	
	/**
	 * Erzeugt aus einem ComObject_mappreview Objekt eine Kartenvorschau.
	 * 
	 * @param mappreview Das ComObject_mappreview Objekt mit Karten- und Einheiteninformationen.
	 * @return Die Kartenvorschau als BufferedImage.
	 * 
	 * @author Peter Dörr
	 * @since 16.01.13
	 */
	public BufferedImage drawMapPreview(ComObject_mappreview mappreview){
		//Farben
		Color terrain_walkable = new Color(200, 200, 200);
		Color terrain_unwalkable = new Color(50, 50, 50);
		Color units_all = new Color(150, 150, 0);
		
		//Parameter
		int height = mappreview.getTerrainmappreview().get(0).size();
		int width = mappreview.getTerrainmappreview().size();
		int resolution_x = 4096 * width / 25;
		int resolution_y = (3072 + 512) * height / 25;
		int hexagonRadius = 94;

		//2D Grafik mit ausreichender Auflösung und schwarzem Hintergrund erzeugen
		BufferedImage bufferedImage = new BufferedImage(resolution_x, resolution_y, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.BLACK);
		graphics2D.fillRect(0, 0, resolution_x, resolution_y);
		
		int [][] map = new int[width][height];
		int [][] units = new int[width][height];
		
		for(int spalte = 0; spalte < mappreview.getTerrainmappreview().get(0).size(); spalte++)
		{	
			for(int zeile = 0; zeile < mappreview.getTerrainmappreview().size(); zeile++)
			{
				map[zeile][spalte] = mappreview.getTerrainmappreview().get(zeile).get(spalte);
				units[zeile][spalte] = mappreview.getUnitmappreview().get(zeile).get(spalte);
			}
		}

		//Hexfelder einzeichnen
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){

				//Farbe festlegen
				//Unbegehbare Felder
				if(map[x][y] == 0){
					graphics2D.setColor(terrain_unwalkable);
				}
				//Begehbare Felder
				if(map[x][y] == 1){
					graphics2D.setColor(terrain_walkable);
				}
				//Einheiten
				if(units[x][y] > 0){
					graphics2D.setColor(units_all);
				}

				//Hexagon erstellen
				double center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (width + 0.5d);
				double center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (width + 0.5d);
				double center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * height + 1)) - (resolution_y / (3 * height + 1)));

				Polygon hexagon;
				if(y % 2 == 1){
					hexagon = createHexagonWithPointyOrientation(center_x, center_y, hexagonRadius);
				} else {
					hexagon = createHexagonWithPointyOrientation(center_x_offsetted, center_y, hexagonRadius);
				}

				//Hexagon einzeichnen
				graphics2D.fill(hexagon);
			}
		}

		//Resize
		RenderedImage renderedImage = commonResize(bufferedImage, resolution * width / 25, resolution * height / 25, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		return (BufferedImage)renderedImage;
	}
	
	
	
	/**
	 * Setzt die Auflösung auf den gegebenen Wert, minimum 320, maximum 2048.
	 * 
	 * @param resolution Die Auflösung des Minimap-Bildes, minimum 320, maximum 2048.
	 * 
	 * @author Peter Dörr
	 * @since 18.12.12
	 */
	public void setResolution(int resolution){
		if(resolution < 320){
			this.resolution = 320;
			return;
		}
		if(resolution > 2048){
			this.resolution = 2048;
			return;
		}
		
		this.resolution = resolution;
	}
	
	
	
	/**
	 * Erzeugt ein Hexagon mit Pointy Orientation, dessen Mittelpunkt bei den Koordinaten center_x und center_y liegt und
	 * dessen Radius radius entspricht.<br>
	 * DIESE METHODE WURDE AUS PlayingField.java ÜBERNOMMEN!
	 * 
	 * @param center_x X-Koordinate des Mittelpunktes des Hexagons.
	 * @param center_y Y-Koordinate des Mittelpunktes des Hexagons.
	 * @param radius Radius des Hexagons, gemessen vom Mittelpunkt zu den 6 Eckpunkten.
	 * @return Das Hexagon als Polygon-Objekt.
	 * 
	 * @author Peter Dörr
	 * @since 18.12.12
	 */
	private Polygon createHexagonWithPointyOrientation(double center_x, double center_y, double radius){
		Polygon hexagon = new Polygon();
		for(int i = 0; i < 6; i++){
			hexagon.addPoint((int)(center_x + (radius * Math.sin(Math.PI * (double)i / 3.0d))), (int)(center_y + (radius * Math.cos(Math.PI * (double)i / 3.0d))));
		}
		return hexagon;
	}
	
	
	
	/**
	 * Skaliert ein BufferedImage auf eine neue Größe.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java<br>
	 * DIESE METHODE WURDE AUS PlayingField.java ÜBERNOMMEN!
	 * 
	 * @param source Das neu zu skalierende Bild.
	 * @param width Neue Breite des Bildes.
	 * @param height Neue Höhe des Bildes.
	 * @param hint Hint zum Rendern des neuen Bildes.
	 * @return Das neu skalierte Bild.
	 * 
	 * @author Peter Dörr
	 * @since 18.12.12
	 */
	private BufferedImage commonResize(BufferedImage source, int width, int height, Object hint) {
        BufferedImage img = new BufferedImage(width, height, source.getType());
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g.drawImage(source, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return img;
    }
}
