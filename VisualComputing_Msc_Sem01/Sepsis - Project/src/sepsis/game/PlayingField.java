package sepsis.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Sphere;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.util.BufferUtils;



/**
 * Diese Klasse beherbergt das Spielfeld, sowie die Einheiten, die sich in der Karte befinden. Hier wird zudem über die
 * initTerrain() Methode das Terrain erstellt.<br><br>
 * Programmcode basiert auf folgenden Quellen: <br>
 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_terrain <br>
 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_material<br>
 * http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java<br>
 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:custom_meshes<br><br>
 * 
 * @author Peter Dörr
 * @since 17.11.12 <br>
 * 
 * 17.11.12 (Markus Strobel) added InitConfigParser()<br>
 * 
 * 18.11.12 (Markus Strobel) moved InitConfigParser() to Game.java<br>
 * 
 * 20.11.12 (Peter Dörr) initHexagonBases und initHexagonBorders hinzugefügt.<br>
 * 
 * 21.11.12 (Peter Dörr) initHexagonCenters hinzugefügt.
 */
public class PlayingField {
	
	/**
	 * Die Karteninformationen über begehbare und nicht begehbare Felder. Die X-Achse ist in Dimension 1, die 
	 * Y-Achse in Dimension 2 gespeichert (map[x][y]).<br>
	 * ACHTUNG: Der Ursprung der fertigen Hexfeldkarte liegt rechts-unten, d.h. map[0][0] ist das Hexfeld,
	 * welches später auf dem Spielfeld rechts-unten zu sehen sein wird.
	 */
	public int map[][];
	/**
	 * Beschreibt, welche Felder des Spielfeldes für diesen Client sichtbar sind und welche nicht.
	 */
	public boolean visibility[][];
	/**
	 * Alle Einheiten der Karte, die noch im Spiel sind.
	 */
	public ArrayList<Unit> units;
	/**
	 * Breite der Karte.
	 */
	public int width;
	/**
	 * Höhe der Karte.
	 */
	public int height;
	/**
	 * Name der Karte.
	 */
	public String name;
	
	
	
	/**
	 * Default-Konstruktor.
	 * 
	 * @author Peter Dörr
	 * @since 07.12.12
	 */
	public PlayingField(){
		
	}
	
	
	
	/**
	 * Konstruktor der PlayingField-Klasse. Initialisiert die Karte, Einheiten und Sichtradien, erstellt aber noch kein Terrain!
	 * Implementiert für eine mögliche zukünftige Verwendung.
	 * 
	 * @param map Die Karteninformationen über begehbare und nicht begehbare Felder.
	 * @param units Alle Einheiten der Karte, die noch im Spiel sind.
	 * @param width Breite der Karte.
	 * @param height Höhe der Karte.
	 * @param name Name der Karte.
	 * 
	 * @author Peter Dörr
	 * @since 07.12.12
	 */
	public PlayingField(int[][] map, ArrayList<Unit> units, int width, int height, String name){
		this.map = map;
		this.units = units;
		this.width = width;
		this.height = height;
		this.name = name;
		
		this.visibility = new boolean[width][height];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				visibility[x][y] = true;
			}
		}
	}
	
	
	
	/**
	 * Erstellt das Terrain aus einer Alpha- und Height-Map. Diese werden dynamisch anhand der Karteninformationen erstellt. <br>
	 * Erst aufrufen, nachdem mittels initConfigParser die Karteninformationen durch die Config-Parser ausgelesen, bzw. vom Server heruntergeladen wurden.<br><br>
	 * Programmcode basiert auf folgenden Quellen: <br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_terrain <br><br>
	 * 
	 * @return Den TerrainQuad, der an einen Node angehängt werden muss.
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12
	 */
	public TerrainQuad initTerrain(){
		Game.logger.debug("PlayingField.initTerrain: Terrain wird erstellt...");
		
		//Terrain-Material initialisieren
		Material terrainMaterial = new Material(Game.AM, "Common/MatDefs/Terrain/Terrain.j3md");
		
		//Alpha-Map laden
		AWTLoader awtLoader = new AWTLoader();
		Texture alphaMapTexture = new Texture2D(awtLoader.load(createAlphaMap(), true));
		terrainMaterial.setTexture("Alpha", alphaMapTexture);
		Game.logger.debug("PlayingField.initTerrain: alphamap.png geladen.");
		
		//Height-Map laden
		Texture heightMapTexture = new Texture2D(awtLoader.load(createHeightMap(), true));
		AbstractHeightMap heightMap = new ImageBasedHeightMap(heightMapTexture.getImage());
		heightMap.load();
		Game.logger.debug("PlayingField.initTerrain: heightmap.png geladen.");
		
		//Texturen laden
		Texture tex_1 = Game.AM.loadTexture("images/terrainTexture_1.jpg");
		Texture tex_2 = Game.AM.loadTexture("images/terrainTexture_2.jpg");
		Texture tex_3 = Game.AM.loadTexture("images/terrainTexture_3.jpg");
		tex_1.setWrap(WrapMode.Repeat);
		tex_2.setWrap(WrapMode.Repeat);
		tex_3.setWrap(WrapMode.Repeat);
	    terrainMaterial.setTexture("Tex1", tex_1);
	    terrainMaterial.setTexture("Tex2", tex_2);
	    terrainMaterial.setTexture("Tex3", tex_3);
	    terrainMaterial.setFloat("Tex1Scale", 8.0f);
	    terrainMaterial.setFloat("Tex2Scale", 20.0f);
	    terrainMaterial.setFloat("Tex3Scale", 32.0f);
	    Game.logger.debug("PlayingField.initTerrain: Texturen geladen.");
	    
	    //Terrain erstellen
	    int patchSize = 65;
	    int totalSize = 1025;
	    TerrainQuad terrain = new TerrainQuad("Terrain", patchSize, totalSize, heightMap.getHeightMap());
	    terrain.setMaterial(terrainMaterial);
	    terrain.setLocalScale(new Vector3f(width / 128.0f, 0.1f, height / 128.0f));
	    Game.logger.debug("PlayingField.initTerrain: Terrain erstellt.");
	    
	    return terrain;
	}
	
	
	
	/**
	 * Erzeugt Hexagon-Polygone, die über dem Terrain schweben. Diese werden dynamisch anhand der Karteninformationen erstellt. <br>
	 * Erst aufrufen, nachdem mittels initConfigParser die Karteninformationen durch die Config-Parser ausgelesen, bzw. vom Server heruntergeladen wurden.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_material<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:custom_meshes
	 * 
	 * @return Eine ArrayList mit Geometry-Objekten, die jeweils ein Hexagon darstellen. Jedes dieser Objekte muss an einen Node angehängt werden.
	 * 
	 * @author Peter Dörr
	 * @since 18.11.12
	 */
	public ArrayList<Geometry> initHexagonBases(){
		Game.logger.debug("PlayingField.initHexagonBases: Erstelle Hexfelderbasen...");
		
		//Initialisiere Vertices, Texturkoordinaten und Output-ArrayList
		Mesh mesh;
		Vector3f [] vertices = new Vector3f[13];
		Vector2f[] texCoord = new Vector2f[13];
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		
		//Farbe der Polygon-Fläche mit Transparenz
		ColorRGBA color = new ColorRGBA(0.0f, 1.0f, 0.0f, 0.5f);
		
		//Auflösungsparameter zum Abgleichen der Polygon-Größen mit dem Terrain
		double resolution_x = 1024 * width / 128.0d;
		double resolution_y = 1024 * height / 128.0d;
		
		//Bestimmt die Größe der Hexfelder
		double radius = 4.0d;
		
		//Hebt die Hexfelder über das Terrain
		double offset_y = 2.0d;
		
		//Mittelpunkte der Hexfelder
		double center_x;
		double center_x_offsetted;
		double center_y;
		
		//Texturkoordinaten setzen (werden nicht gebraucht, für evtl. verwendete Texturen verwendbar)
		texCoord[0] = new Vector2f(0,0);
		texCoord[1] = new Vector2f(0,0);
		texCoord[2] = new Vector2f(0,0);
		texCoord[3] = new Vector2f(0,0);
		texCoord[4] = new Vector2f(0,0);
		texCoord[5] = new Vector2f(0,0);
		texCoord[6] = new Vector2f(0,0);
		texCoord[7] = new Vector2f(0,0);
		texCoord[8] = new Vector2f(0,0);
		texCoord[9] = new Vector2f(0,0);
		texCoord[10] = new Vector2f(0,0);
		texCoord[11] = new Vector2f(0,0);
		texCoord[12] = new Vector2f(0,0);
		
		//Indizes festlegen
		int indexes[] = {12,0,1, 12,1,2, 12,2,3, 12,3,4, 12,4,5, 12,5,0,
						 0,6,7, 0,7,1, 1,7,8, 1,8,2, 2,8,9, 2,9,3, 3,9,10, 3,10,4, 4,10,11, 4,11,5, 5,11,6, 5,6,1};
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				//Kein Hexagon für nicht begehbare Felder erstellen
				if(map[x][y] == 0){
					continue;
				}
				
				//Mesh erstellen
				mesh = new Mesh();
				mesh.setMode(Mode.Triangles);
				
				//X-Koordinate des Hexagon-Mittelpunktes berechnen
				center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (width + 0.5d);
				center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (width + 0.5d);
				
				//Y-Koordinate des Hexagon-Mittelpunktes berechnen
				center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * height + 1)) - (resolution_y / (3 * height + 1)));
				
				//Mittelpunkt festlegen
				vertices[12] = new Vector3f(0.0f, 0.0f, 0.0f);
				if(y % 2 == 1){
					vertices[12].x = (float)center_x;
					vertices[12].y = (float)offset_y;
					vertices[12].z = (float)center_y;
				} else {
					vertices[12].x = (float)center_x_offsetted;
					vertices[12].y = (float)offset_y;
					vertices[12].z = (float)center_y;
				}
				
				//Vertices des Hexagons berechnen
				for(int i = 0; i < 6; i++){
					if(y % 2 == 1){
						vertices[i] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i].x = (float)(center_x + (radius * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i].y = (float)offset_y;
						vertices[i].z = (float)(center_y + (radius * Math.cos(Math.PI * (double)i / 3.0d)));
					} else {
						vertices[i] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i].x = (float)(center_x_offsetted + (radius * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i].y = (float)offset_y;
						vertices[i].z = (float)(center_y + (radius * Math.cos(Math.PI * (double)i / 3.0d)));
					}
				}
				for(int i = 6; i < 12; i++){
					if(y % 2 == 1){
						vertices[i] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i].x = (float)(center_x + (radius * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i].y = (float)offset_y - 1.0f;
						vertices[i].z = (float)(center_y + (radius * Math.cos(Math.PI * (double)i / 3.0d)));
					} else {
						vertices[i] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i].x = (float)(center_x_offsetted + (radius * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i].y = (float)offset_y - 1.0f;
						vertices[i].z = (float)(center_y + (radius * Math.cos(Math.PI * (double)i / 3.0d)));
					}
				}
				
				//Buffer setzen und Mesh erstellen
				mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
				mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
				mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexes));
				mesh.updateBound();
				
				//Geometry-Objekt erstellen, Transparenz einrichten, versetzen und in die Output-ArrayList einfügen
				Geometry geo = new Geometry("HexagonBase_" + String.valueOf(x) + "_" + String.valueOf(y), mesh);
				Material mat = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.setColor("Color", color);
				geo.setMaterial(mat);
				geo.setQueueBucket(Bucket.Transparent);
				geo.setLocalTranslation((float)resolution_x / -2.0f, 0.0f, (float)resolution_y / -2.0f);
				geometries.add(geo);
			}
		}
		
		Game.logger.debug("PlayingField.initHexagonBases: Hexfelderbasen erstellt.");
		
		return geometries;
	}
	
	
	
	/**
	 * Erzeugt die Mittelpunkte der Hexagon-Polygone, die über dem Terrain schweben. Diese werden dynamisch anhand der Karteninformationen erstellt. <br>
	 * Erst aufrufen, nachdem mittels initConfigParser die Karteninformationen durch die Config-Parser ausgelesen, bzw. vom Server heruntergeladen wurden.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_material<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:custom_meshes
	 * 
	 * @return Eine ArrayList mit Geometry-Objekten, die jeweils ein Hexagon darstellen. Jedes dieser Objekte muss an einen Node angehängt werden.
	 * 
	 * @author Peter Dörr
	 * @since 21.11.12
	 */
	public ArrayList<Geometry> initHexagonCenters(){
		Game.logger.debug("PlayingField.initHexagonCenters: Erstelle Hexfeldermittelpunkte...");
		
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		
		//Farbe der Polygon-Punkte mit Transparenz
		ColorRGBA color = new ColorRGBA(0.0f, 0.0f, 0.5f, 0.75f);
		
		//Auflösungsparameter zum Abgleichen der Polygon-Größen mit dem Terrain
		double resolution_x = 1024 * width / 128.0d;
		double resolution_y = 1024 * height / 128.0d;
		
		//Bestimmt die Größe der Punkte
		double radius = 0.25d;
		
		//Hebt die Hexfelder über das Terrain
		double offset_y = 2.05d;
		
		//Mittelpunkte der Hexfelder
		double center_x;
		double center_x_offsetted;
		double center_y;
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				//Kein Hexagon für nicht begehbare Felder erstellen
				if(map[x][y] == 0){
					continue;
				}
				
				//X-Koordinate des Hexagon-Mittelpunktes berechnen
				center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (width + 0.5d);
				center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (width + 0.5d);
				
				//Y-Koordinate des Hexagon-Mittelpunktes berechnen
				center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * height + 1)) - (resolution_y / (3 * height + 1)));
				
				//Mittelpunkt festlegen
				Vector3f point = new Vector3f(0.0f, 0.0f, 0.0f);
				if(y % 2 == 1){
					point.x = (float)center_x;
					point.y = (float)offset_y;
					point.z = (float)center_y;
				} else {
					point.x = (float)center_x_offsetted;
					point.y = (float)offset_y;
					point.z = (float)center_y;
				}
				
				//Geometry-Objekt erstellen, Transparenz einrichten, versetzen und in die Output-ArrayList einfügen
				Sphere rock = new Sphere(5, 5, (float)radius);
			    Geometry geo = new Geometry("HexagonCenter_" + String.valueOf(x) + "_" + String.valueOf(y), rock);
				Material mat = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.setColor("Color", color);
				geo.setMaterial(mat);
				geo.setQueueBucket(Bucket.Transparent);
				geo.setLocalTranslation(point);
				geo.move((float)resolution_x / -2.0f, 0.0f, (float)resolution_y / -2.0f);
				geometries.add(geo);
			}
		}
		
		Game.logger.debug("PlayingField.initHexagonCenters: Hexfeldermittelpunkte erstellt.");
		
		return geometries;
	}
	
	
	
	/**Erzeugt die Grenzen der Hexagon-Polygone, die über dem Terrain schweben. Diese werden dynamisch anhand der Karteninformationen erstellt. <br>
	 * Erst aufrufen, nachdem mittels initConfigParser die Karteninformationen durch die Config-Parser ausgelesen, bzw. vom Server heruntergeladen wurden.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_material<br>
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:custom_meshes
	 * 
	 * @return Eine ArrayList mit Geometry-Objekten, die jeweils eine Hexagon-Grenze darstellen. Jedes dieser Objekte muss an einen Node angehängt werden.
	 * 
	 * @author Peter Dörr
	 * @since 19.11.12
	 */
	public ArrayList<Geometry> initHexagonBorders(){
		Game.logger.debug("PlayingField.initHexagonBorders: Erstelle Hexfeldergrenzen...");
		
		//Initialisiere Vertices, Texturkoordinaten und Output-ArrayList
		Mesh mesh;
		Vector3f [] vertices = new Vector3f[24];
		Vector2f[] texCoord = new Vector2f[24];
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		
		//Farbe der Polygon-Fläche mit Transparenz
		ColorRGBA color = new ColorRGBA(0.0f, 0.0f, 0.5f, 0.75f);
		
		//Auflösungsparameter zum Abgleichen der Polygon-Größen mit dem Terrain
		double resolution_x = 1024 * width / 128.0d;
		double resolution_y = 1024 * height / 128.0d;
		
		//Bestimmt die Größe der Hexfelder
		double radius_inner = 3.5d;
		double radius_outer = 4.1d;
		
		//Hebt die Hexfelder über das Terrain
		double offset_y = 2.1d;
		
		//Mittelpunkte der Hexfelder
		double center_x;
		double center_x_offsetted;
		double center_y;
		
		//Texturkoordinaten setzen (werden nicht gebraucht, für evtl. verwendete Texturen verwendbar)
		texCoord[0] = new Vector2f(0,0);
		texCoord[1] = new Vector2f(0,0);
		texCoord[2] = new Vector2f(0,0);
		texCoord[3] = new Vector2f(0,0);
		texCoord[4] = new Vector2f(0,0);
		texCoord[5] = new Vector2f(0,0);
		texCoord[6] = new Vector2f(0,0);
		texCoord[7] = new Vector2f(0,0);
		texCoord[8] = new Vector2f(0,0);
		texCoord[9] = new Vector2f(0,0);
		texCoord[10] = new Vector2f(0,0);
		texCoord[11] = new Vector2f(0,0);
		texCoord[12] = new Vector2f(0,0);
		texCoord[13] = new Vector2f(0,0);
		texCoord[14] = new Vector2f(0,0);
		texCoord[15] = new Vector2f(0,0);
		texCoord[16] = new Vector2f(0,0);
		texCoord[17] = new Vector2f(0,0);
		texCoord[18] = new Vector2f(0,0);
		texCoord[19] = new Vector2f(0,0);
		texCoord[20] = new Vector2f(0,0);
		texCoord[21] = new Vector2f(0,0);
		texCoord[22] = new Vector2f(0,0);
		texCoord[23] = new Vector2f(0,0);
		
		//Indizes festlegen
		int indexes[] = {1,0,2, 2,3,1, 3,2,4, 4,5,3, 5,4,6, 6,7,5, 7,6,8, 8,9,7, 9,8,10, 10,11,9, 11,10,0, 0,1,11,
						 13,12,14, 14,15,13, 15,14,16, 16,17,15, 17,16,18, 18,19,17, 19,18,20, 20,21,19, 21,20,22, 22,23,21, 23,22,12, 12,13,23,
						 0,12,14, 0,14,2, 2,14,16, 2,16,4, 4,16,18, 4,18,6, 6,18,20, 6,20,8, 8,20,22, 8,22,10, 10,22,12, 10,12,0};
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				//Kein Hexagon für nicht begehbare Felder erstellen
				if(map[x][y] == 0){
					continue;
				}
				
				//Mesh erstellen
				mesh = new Mesh();
				mesh.setMode(Mode.Triangles);
				
				//X-Koordinate des Hexagon-Mittelpunktes berechnen
				center_x = resolution_x - ((x+1) * resolution_x - resolution_x / 2.0d) / (width + 0.5d);
				center_x_offsetted = resolution_x - ((x+1) * resolution_x) / (width + 0.5d);
				
				//Y-Koordinate des Hexagon-Mittelpunktes berechnen
				center_y = resolution_y - (3 * (y+1) * (resolution_y / (3 * height + 1)) - (resolution_y / (3 * height + 1)));
				
				//Vertices des Hexagons berechnen
				for(int i = 0; i < 6; i++){
					if(y % 2 == 1){
						vertices[i*2] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2].x = (float)(center_x + (radius_outer * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2].y = (float)offset_y;
						vertices[i*2].z = (float)(center_y + (radius_outer * Math.cos(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+1].x = (float)(center_x + (radius_inner * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1].y = (float)offset_y;
						vertices[i*2+1].z = (float)(center_y + (radius_inner * Math.cos(Math.PI * (double)i / 3.0d)));
					} else {
						vertices[i*2] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2].x = (float)(center_x_offsetted + (radius_outer * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2].y = (float)offset_y;
						vertices[i*2].z = (float)(center_y + (radius_outer * Math.cos(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+1].x = (float)(center_x_offsetted + (radius_inner * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1].y = (float)offset_y;
						vertices[i*2+1].z = (float)(center_y + (radius_inner * Math.cos(Math.PI * (double)i / 3.0d)));
					}
				}
				for(int i = 0; i < 6; i++){
					if(y % 2 == 1){
						vertices[i*2+12] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+12].x = (float)(center_x + (radius_outer * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+12].y = (float)offset_y - 1.2f;
						vertices[i*2+12].z = (float)(center_y + (radius_outer * Math.cos(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1+12] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+1+12].x = (float)(center_x + (radius_inner * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1+12].y = (float)offset_y - 1.2f;
						vertices[i*2+1+12].z = (float)(center_y + (radius_inner * Math.cos(Math.PI * (double)i / 3.0d)));
					} else {
						vertices[i*2+12] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+12].x = (float)(center_x_offsetted + (radius_outer * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+12].y = (float)offset_y - 1.2f;
						vertices[i*2+12].z = (float)(center_y + (radius_outer * Math.cos(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1+12] = new Vector3f(0.0f, 0.0f, 0.0f);
						vertices[i*2+1+12].x = (float)(center_x_offsetted + (radius_inner * Math.sin(Math.PI * (double)i / 3.0d)));
						vertices[i*2+1+12].y = (float)offset_y - 1.2f;
						vertices[i*2+1+12].z = (float)(center_y + (radius_inner * Math.cos(Math.PI * (double)i / 3.0d)));
					}
				}
				
				//Buffer setzen und Mesh erstellen
				mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
				mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
				mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexes));
				mesh.updateBound();
				
				//Geometry-Objekt erstellen, Transparenz einrichten, versetzen und in die Output-ArrayList einfügen
				Geometry geo = new Geometry("HexagonBorder_" + String.valueOf(x) + "_" + String.valueOf(y), mesh);
				Material mat = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
				mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				mat.setColor("Color", color);
				geo.setMaterial(mat);
				geo.setQueueBucket(Bucket.Transparent);
				geo.setLocalTranslation((float)resolution_x / -2.0f, 0.0f, (float)resolution_y / -2.0f);
				geo.setCullHint(CullHint.Dynamic);
				geometries.add(geo);
			}
		}
		
		Game.logger.debug("PlayingField.initHexagonBorders: Hexfeldergrenzen erstellt.");
		
		return geometries;
	}
	
	
	
	/**
	 * Erzeugt die heightmap.png anhand der Karteninformationen.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java
	 * 
	 * @author Peter Dörr
	 * @since 17.11.12 <br>
	 * 
	 * 17.11.2012 (Peter Dörr): Bugfixing und auf auf beliebige Kartengrößen angepasst.
	 */
	private BufferedImage createHeightMap(){
		Game.logger.debug("PlayingField.createHeightMap: Erstelle heightmap.png");
		
		//Alte Dateien löschen
		File file = new File("data/images/heightmap_tmp.png");
        if(file.exists()){
            file.delete();
        }
        file = new File("data/images/heightmap.png");
        if(file.exists()){
            file.delete();
        }
		
		//Parameter
		Color color_high = new Color(16, 16, 16);
		Color color_low = new Color(8, 8, 8);
		int alphamapResolution_x = 4096 * width / 25;
		int alphamapResolution_y = (3072 + 512) * height / 25;
		int hexagonRadius = 94;
		
		//2D Grafik mit ausreichender Auflösung und schwarzem Hintergrund erzeugen
		BufferedImage bufferedImage = new BufferedImage(alphamapResolution_x, alphamapResolution_y, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.BLACK);
		graphics2D.fillRect(0, 0, alphamapResolution_x, alphamapResolution_y);
		
		//Hexfelder einzeichnen
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				//Farbe festlegen
				if(map[x][y] == 0){
					graphics2D.setColor(color_high);
				} else {
					graphics2D.setColor(color_low);
				}
				
				//Hexagon erstellen
				double center_x = alphamapResolution_x - ((x+1) * alphamapResolution_x - alphamapResolution_x / 2.0d) / (width + 0.5d);
				double center_x_offsetted = alphamapResolution_x - ((x+1) * alphamapResolution_x) / (width + 0.5d);
//				double center_y = alphamapResolution_y - (3 * (y+1) * (alphamapResolution_y / (3 * height + 1)) - (alphamapResolution_y / (3 * height + 1)));
				double center_y = alphamapResolution_y - alphamapResolution_y * ((3.0d * (y + 1.0d) * (1.0d / (3.0d * height + 1.0d)) - (1.0d / (3.0d * height + 1.0d))));
				
//				if(height != width && height % 2 != 0){
//					center_y += (alphamapResolution_y / (3 * height + 1)) / 2;
//				}
				
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
		
		//Korrigiert das Bild, indem es auf eine Größe umgerechnet wird, bei der Breite = Höhe ist
		try {
			BufferedImage img2 = commonResize(bufferedImage, 1024, 1024, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			
			Game.logger.debug("PlayingField.createAlphaMap: Erstellung der heightmap.png erfolgreich.");
			
			return img2;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
	/**
	 * Erzeugt die alphamap.png anhand der Karteninformationen.<br><br>
	 * Programmcode basiert auf folgenden Quellen:<br>
	 * http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java
	 * 
	 * @author Peter Dörr
	 * @since 17.11.12
	 */
	private BufferedImage createAlphaMap(){
		Game.logger.debug("PlayingField.createAlphaMap: Erstelle alphamap.png");
		
		//Alte Dateien löschen
		File file = new File("data/images/alphamap_tmp.png");
		if(file.exists()){
			file.delete();
		}
		file = new File("data/images/alphamap.png");
		if(file.exists()){
		    file.delete();
		}
		
		//Parameter
		Color color_openField = Color.BLUE;
		Color color_unreachableField = Color.GREEN;
		int alphamapResolution_x = 4096 * width / 25;
		int alphamapResolution_y = (3072 + 512) * height / 25;
		int hexagonRadius = 94;
		
		//2D Grafik mit ausreichender Auflösung und rotem Hintergrund erzeugen
		BufferedImage bufferedImage = new BufferedImage(alphamapResolution_x, alphamapResolution_y, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.RED);
		graphics2D.fillRect(0, 0, alphamapResolution_x, alphamapResolution_y);
		
		//Hexfelder einzeichnen
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				//Farbe festlegen
				if(map[x][y] == 0){
					graphics2D.setColor(color_unreachableField);
				} else {
					graphics2D.setColor(color_openField);
				}
				
				//Hexagon erstellen
				double center_x = alphamapResolution_x - ((x+1) * alphamapResolution_x - alphamapResolution_x / 2.0d) / (width + 0.5d);
				double center_x_offsetted = alphamapResolution_x - ((x+1) * alphamapResolution_x) / (width + 0.5d);
//				double center_y = alphamapResolution_y - (3 * (y+1) * (alphamapResolution_y / (3 * height + 1)) - (alphamapResolution_y / (3 * height + 1)));
				double center_y = alphamapResolution_y - alphamapResolution_y * ((3.0d * (y + 1.0d) * (1.0d / (3.0d * height + 1.0d)) - (1.0d / (3.0d * height + 1.0d))));
				
//				if(height != width && height % 2 != 0){
//					center_y -= (alphamapResolution_y / (3 * height + 1));
//				}
				
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
		
//		//Alte Dateien löschen
//		File file2 = new File("data/images/alpha.png");
//		if(file2.exists()){
//			file2.delete();
//		}
//		
//		//Speichern
//		File file3 = new File("data/images/alpha.png");
//		try {
//			ImageIO.write(bufferedImage, "png", file3);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		//Korrigiert das Bild, indem es auf eine Größe umgerechnet wird, bei der Breite = Höhe ist
		try {
			BufferedImage img = commonResize(bufferedImage, 1024, 1024, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			
			Game.logger.debug("PlayingField.createAlphaMap: Erstellung der alphamap.png erfolgreich.");
			
			return img;
		} catch (Exception e) {
			Game.logger.error("PlayingField.createAlphaMap: Fehler beim Kopieren von alphamap.png: " + e.toString());
			return null;
		}
	}
	
	
	
	/**
	 * Erzeugt ein Hexagon mit Pointy Orientation, dessen Mittelpunkt bei den Koordinaten center_x und center_y liegt und
	 * dessen Radius radius entspricht.
	 * 
	 * @param center_x X-Koordinate des Mittelpunktes des Hexagons.
	 * @param center_y Y-Koordinate des Mittelpunktes des Hexagons.
	 * @param radius Radius des Hexagons, gemessen vom Mittelpunkt zu den 6 Eckpunkten.
	 * @return Das Hexagon als Polygon-Objekt.
	 * 
	 * @author Peter Dörr
	 * @since 17.11.12
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
	 * http://stackoverflow.com/questions/4756268/how-to-resize-the-buffered-image-n-graphics-2d-in-java
	 * 
	 * @param source Das neu zu skalierende Bild.
	 * @param width Neue Breite des Bildes.
	 * @param height Neue Höhe des Bildes.
	 * @param hint Hint zum Rendern des neuen Bildes.
	 * @return Das neu skalierte Bild.
	 * 
	 * @author Peter Dörr
	 * @since 17.11.12
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
