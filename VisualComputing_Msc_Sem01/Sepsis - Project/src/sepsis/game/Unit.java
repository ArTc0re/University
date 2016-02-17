package sepsis.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;


/**
 * Repräsentiert eine Einheit auf dem Spielfeld.
 * 
 * @author Peter Dörr
 * @since 16.11.12 <br>
 * 
 * 17.11.12 (Peter Dörr): enum TYPE und loadModel-Methode auf Markus' Anraten überarbeitet.<br>
 * 
 * 17.11.12 (Markus Strobel): Konstruktor Unit(UnitType unitType, UnitSetup unitSetup) hinzugefügt<br>
 * 
 * 28.11.12 (Markus Strobel): Variable boolean isActive hinzugefügt.<br>
 * 
 * 28.11.12 (Markus Strobel): wayPointsList hinzugefügt.<br>
 * 
 * 05.12.12 (Peter Dörr): Kleinere Veränderungen.<br>
 */
public class Unit {
	
	/**
	 * Enumeration an möglichen Typen der Einheit.
	 */
	public enum TYPE {Fighter_1, Fighter_2, Fighter_3, Transporter_1, Transporter_2, Tradestation}
	/**
	 * Enumeration von möglichen rundenübergreifenden Befehlen.
	 */
	public enum ORDERS {NONE, FOLLOW, INTERCEPT}
	
	
	/**
	 * Gibt an, ob die Einheit in dieser Runde bereits ihren Zug abgeschlossen hat.
	 */
	public boolean doneThisRound = false;
	/**
	 * Typ der Einheit.
	 */
	public TYPE type;
	/**
	 * ID der Einheit, die die Einheit in der Karte eindeutig identifiziert.
	 */
	public int ID;
	/**
	 * Besitzer der Einheit.
	 */
	public String ownerID;
	/**
	 * X-Koordinate der Einheit.
	 */
	public int x;
	/**
	 * Y-Koordinate der Einheit.
	 */
	public int y;	
	/**
	 * Aktuelle Lebenspunkte der Einheit.
	 */
	public int hitpoints_curr;
	/**
	 * Maximale Lebenspunkte der Einheit.
	 */
	public int hitpoints_max;
	/**
	 * Gibt an, ob die Einheit tot ist.
	 */
	public boolean isDead;
	/**
	 * Aktuelle Bewegungspunkte der Einheit.
	 */
	public int movement_curr;
	/**
	 * Maximale Bewegungspunkte der Einheit.
	 */
	public int movement_max;
	/**
	 * Angriffskraft der Einheit.
	 */
	public int attackPower;
	/**
	 * Maximale Tragekapazität der Einheit.
	 */
	public int cargo_max;	
	/**
	 * Aktuelle Frachtmenge von Adenin.
	 */
	public int cargo_1;
	/**
	 * Aktuelle Frachtmenge von Thymin.
	 */
	public int cargo_2;
	/**
	 * Aktuelle Frachtmenge von Guanin.
	 */
	public int cargo_3;
	/**
	 * Aktuelle Frachtmenge von Cytosin.
	 */
	public int cargo_4;
	/**
	 * Aktuelle Frachtmenge an Sauerstoff.
	 */
	public int cargo_5;
	/**
	 * Sichtweite der Einheit.
	 */
	public int visibilityRange;
	/**
	 * Model der Einheit, das auf dem Spielfeld dargestellt wird.
	 */
	public Node model;
	/**
	 * Gibt an, ob der Healthbar angezeigt werden soll.
	 */
	public boolean showHealtBar = true;
	/**
	 * Speichert, wie weit der Lebensbalken rotert ist. Da der Lebensbalken in regelmäßigen Abständen aus einem neuen
	 * Mash erstellt wird, ist das speichern der momentanen Rotation wichtig.
	 */
	public float healthBarRotation = 0.0f;
	/**
	 * Wenn die Einheit aktiv ist, wird dieser Wert auf true gesetzt
	 */
	public boolean isActive = false;
	/**
	 * Diese List speichert den Wegpunkte dieser Einheit, falls sie welche bekommt.
	 */
	public ArrayList<Point> wayPointsList = new ArrayList<Point>();
	/**
	 * Rundenübergreifender Befehl, den die Einheit ausführen soll.
	 */
	public ORDERS order = ORDERS.NONE;
	/**
	 * UnitID der Einheit, die bei einem Intercept- oder Follow-Befehl verfolgt werden soll.
	 */
	public int orderUnitID = -1;
	/**
	 * Buffer, der die Bewegung einer per Follow-Funktion verfolgten Einheit aufzeichnet.
	 */
	public ArrayList<Point> followBuffer = new ArrayList<Point>();
	/**
	 * TRADESTATION ONLY: Der Name der Handelsstation.
	 */
	public String stationID = "";
	/**
	 * TRADESTATION ONLY: Gibt an, mit welchem Spieler die Station handelt.
	 */
	public ArrayList<Integer> tradestation_friendlyPlayerIndexes = new ArrayList<Integer>();
	/**
	 * TRADESTATION ONLY: Gibt an, welche Güter wie oft verfügbar sind.
	 */
	public ArrayList<Integer> tradestation_availableGoods = new ArrayList<Integer>();
	/**
	 * TRADESTATION ONLY: Gibt an, welche Güter in welchen Ratios getauscht werden können.
	 */
	public ArrayList<Integer> tradestation_tradeEquivalences = new ArrayList<Integer>();
	/**
	 * Speichert die letzte Bewegung aus der UnitInfo.
	 */
	public ArrayList<Point> lastMovement = null;
	
	
	
	/**
	 * Konstruktor für Einheiten ohne Anfangsfracht.
	 * 
	 * @param type Typ der Einheit.
	 * @param ID ID der Einheit, über den die Einheit eindeutig identifiziert werden kann.
	 * @param ownerID Der Besitzer der Einheit.
	 * @param x X-Koordinate der Einheit.
	 * @param y Y-Koordinate der Einheit.
	 * @param hitpoints_curr Momentane Lebenspunkte der Einheit.
	 * @param hitpoints_max Maximale Lebenspunkte der Einheit.
	 * @param movement_curr Momentane Bewegungspunkte der Einheit.
	 * @param movement_max Maximale Bewegungspunkte der Einheit.
	 * @param attackPower Angriffskraft der Einheit.
	 * @param cargo_max Maximale Fracht, die die Einheit mit sich führen kann.
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12 <br>
	 * 
	 * 05.12.12 (Peter Dörr): Überarbeitung.<br>
	 */
	public Unit(TYPE type, int ID, String ownerID, int x, int y, int hitpoints_curr, int hitpoints_max, int movement_curr, int movement_max, int attackPower, int cargo_max){
		Game.logger.debug("Unit.Unit: Erstelle Einheit...");
		
		//Parameter initialisieren
		this.doneThisRound = false;
		this.type = type;
		this.ID = ID;
		this.ownerID = ownerID;
		this.x = x;
		this.y = y;
		this.hitpoints_curr = hitpoints_curr;
		this.hitpoints_max = hitpoints_max;
		this.isDead = false;
		this.movement_curr = movement_curr;
		this.attackPower = attackPower;
		this.cargo_max = cargo_max;
		this.cargo_1 = 0;
		this.cargo_2 = 0;
		this.cargo_3 = 0;
		this.cargo_4 = 0;
		this.cargo_5 = 0;
		this.visibilityRange = movement_max;
		this.isActive = false;
		
		//Model laden
		this.model = loadModel(type, ownerID);
		this.loadHealthbar(hitpoints_curr, hitpoints_max);
		
		Game.logger.debug("Unit.Unit: Einheit erstellt. ID:" + ID + " X:" + x + " Y:" + y);
	}
	
	
	
	/**
	 * Konstruktor für Einheiten mit Anfangsfracht.
	 * 
	 * @param type Typ der Einheit.
	 * @param ID ID der Einheit, über den die Einheit eindeutig identifiziert werden kann.
	 * @param owner Der Besitzer der Einheit.
	 * @param x X-Koordinate der Einheit.
	 * @param y Y-Koordinate der Einheit.
	 * @param hitpoints_curr Momentane Lebenspunkte der Einheit.
	 * @param hitpoints_max Maximale Lebenspunkte der Einheit.
	 * @param movement_curr Momentane Bewegungspunkte der Einheit.
	 * @param movement_max Maximale Bewegungspunkte der Einheit.
	 * @param attackPower Angriffskraft der Einheit.
	 * @param cargo_max Maximale Fracht, die die Einheit mit sich führen kann.
	 * @param cargo_1 Aktuelle Frachtmenge von Adenin.
	 * @param cargo_2 Aktuelle Frachtmenge von Thymin.
	 * @param cargo_3 Aktuelle Frachtmenge von Guanin.
	 * @param cargo_4 Aktuelle Frachtmenge von Cytosin.
	 * @param cargo_5 Aktuelle Frachtmenge von Sauerstoff.
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12<br>
	 * 
	 * 05.12.12 (Peter Dörr): Überarbeitung.<br>
	 */
	public Unit(TYPE type, int ID, String ownerID, int x, int y, int hitpoints_curr, int hitpoints_max, int movement_curr, int movement_max, int attackPower, int cargo_max, int cargo_1, int cargo_2, int cargo_3, int cargo_4, int cargo_5){
		Game.logger.debug("Unit.Unit: Erstelle Einheit...");
		
		//Parameter initialisieren
		this.doneThisRound = false;
		this.type = type;
		this.ID = ID;
		this.ownerID = ownerID;
		this.x = x;
		this.y = y;
		this.hitpoints_curr = hitpoints_curr;
		this.hitpoints_max = hitpoints_max;
		this.isDead = false;
		this.movement_curr = movement_curr;
		this.attackPower = attackPower;
		this.cargo_max = cargo_max;
		this.cargo_1 = cargo_1;
		this.cargo_2 = cargo_2;
		this.cargo_3 = cargo_3;
		this.cargo_4 = cargo_4;
		this.cargo_5 = cargo_5;
		this.visibilityRange = movement_max;
		this.isActive = false;
		
		//Model laden
		this.model = loadModel(type, ownerID);
		this.loadHealthbar(hitpoints_curr, hitpoints_max);
		
		Game.logger.debug("Unit.Unit: Einheit erstellt. ID:" + ID + " X:" + x + " Y:" + y);
	}
	
	

	/**
	 * Konstruktor für Einheiten anhand der UnitSetup und UnitConfig<br>
	 * 
	 * @author Markus Strobel
	 * @since 17.11.2012<br>
	 * 
	 * @param unitSetup Das Einheitensetup der auf dem Feld bestehenden Einheit
	 * @param unitType Die Einheitentypen, welche in der MeepleConfig festgelegt sind
	 */
	public Unit(UnitSetup unitSetup, UnitType unitType)
	{
		Game.logger.debug("Unit.Unit: Erstelle Einheit anhand der UnitType- und UnitSetup-Objekte...");

		//Parameter initialisieren
		this.doneThisRound = false;
		
		 //Setzt den Typ der Einheit
		switch(unitType.type)
		{
		case "fightersmall":
			this.type = TYPE.Fighter_1;
			break;
		case "fightermedium":
			this.type = TYPE.Fighter_2;
			break;
		case "fighterheavy":
			this.type = TYPE.Fighter_3;
			break;
		case "cargosmall":
			this.type = TYPE.Transporter_1;
			break;
		case "cargoheavy":
			this.type = TYPE.Transporter_2;
			break;
		}
		
		Game.logger.debug("Unit.Unit.type: Type der Einheit ist " + this.type.toString());
		
		this.ID = unitSetup.id;
		this.ownerID = unitSetup.ownerID;
		this.x = unitSetup.positionX;
		this.y = unitSetup.positionY;
		
		/*
		 * Die aktuellen Trefferpunkte werden auf den Wert der UnitSetup gesetzt, falls der Wert hasCustonHitPointsValue auf true ist
		 * andernfalls wird der aktuelle Trefferpunktwert auf die maximalen Trefferpunkte der unitType gesetzt
		 */
		if(unitSetup.hasCustomHitPointValue)
		{
			this.hitpoints_curr = unitSetup.currentHitPoints;
		}
		else
		{
			this.hitpoints_curr = unitType.maxHitPoints;
		}
		
		/*
		 * Die aktuellen Movement Punkte
		 */
		if(unitSetup.hasCustomMovementPointValue)
		{
			this.movement_curr = unitSetup.currentMovementPoints;
		}
		else
		{
			this.movement_curr = unitType.movement;
		}
		
		this.hitpoints_max = unitType.maxHitPoints;
		this.isDead = false;
		this.movement_max = unitType.movement;
		this.attackPower = unitType.maxFirePower;
		this.cargo_max = unitType.maxCargo;

		this.cargo_1 = unitSetup.cargo_1;
		this.cargo_2 = unitSetup.cargo_2;
		this.cargo_3 = unitSetup.cargo_3;
		this.cargo_4 = unitSetup.cargo_4;
		this.cargo_5 = unitSetup.cargo_5;
		this.visibilityRange = movement_max;
		this.isActive = false;
		
		//Model laden
		this.model = loadModel(type, ownerID);
		this.loadHealthbar(hitpoints_curr, hitpoints_max);
		
		Game.logger.debug("Unit.Unit: Einheit erstellt. ID:" + ID + " X:" + x + " Y:" + y);
		
	}
	
	
	
	/**
	 * Konstruktor für Handelsstationen.
	 * 
	 * @param stationID Name der Handelsstation.
	 * @param ID ID der Einheit, über den die Einheit eindeutig identifiziert werden kann.
	 * @param x X-Koordinate der Einheit.
	 * @param y Y-Koordinate der Einheit.
	 * @param friendlyPlayerIndexes Indices der Spieler, die mit dieser Station handeln können.
	 * @param availableGoods Gibt an, welche Güter wie oft verfügbar sind.
	 * @param tradeEquivalences Gibt an, welche Güter in welchen Ratios getauscht werden können.
	 * 
	 * @author Peter Dörr
	 * @since 13.01.13
	 */
	public Unit(String stationID, int ID, int x, int y, ArrayList<Integer> friendlyPlayerIndexes, ArrayList<Integer> availableGoods, ArrayList<Integer> tradeEquivalences){
		Game.logger.debug("Unit.Unit: Erstelle Tradestation...");
		
		//Parameter initialisieren
		this.doneThisRound = true;
		this.stationID = stationID;
		this.type = TYPE.Tradestation;
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.tradestation_friendlyPlayerIndexes = friendlyPlayerIndexes;
		this.tradestation_availableGoods = availableGoods;
		this.tradestation_tradeEquivalences = tradeEquivalences;
		this.hitpoints_curr = 100000;
		this.hitpoints_max = 100000;
		this.isDead = false;
		this.ownerID = null;
		
		//Model laden
		this.model = loadModel(type, ownerID);
		this.loadHealthbar(hitpoints_curr, hitpoints_max);
		
		Game.logger.debug("Unit.Unit: Tradestation erstellt. ID:" + ID + " X:" + x + " Y:" + y);
	}
	
	
	
	/**
	 * Läd das Model und die StatusBox zum entsprechenden Einheitentyp.
	 * 
	 * @param type Typ der Einheit.
	 * @param ownerID Besitzer der Einheit, 0 für eigene, 1 für feindliche Einheiten.
	 * @return Das Model der Einheit und die StatusBox in einem Node.
	 * 
	 * @author Peter Dörr
	 * @since 16.11.12 <br>
	 * 
	 * 17.11.12 (Markus Strobel) Code-Optimierung -> switch/case statt der vielen if klauseln.<br>
	 * 
	 * 17.11.12 (Peter Dörr): enum TYPE und loadModel-Methode auf Markus' Anraten überarbeitet.<br>
	 * 
	 * 14.12.12 (Peter Dörr): Model von Spatial auf Node geändert und StatusBox hinzugefügt.<br>
	 * 
	 * 06.01.13 (Peter Dörr): StatusBox von Würfel auf Torus geändert.
	 */
	private Node loadModel(TYPE type, String ownerID){

		Node node = new Node();
		Spatial model = null;
		Random random = new Random();
		
		switch(type)
		{
		case Fighter_1:
			if(ownerID.equals(Game.playerID)){
				//AntiVir_Fighter_1 laden
				Game.logger.debug("Unit.loadModel: Lade Model: AntiVir_Fighter_1.obj");
				model = Game.AM.loadModel("models/AntiVir_Fighter_1.obj");
				Material mat_default = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
		        mat_default.setTexture("ColorMap", Game.AM.loadTexture("models/Granulozyten_Textur1.jpg"));
		        model.setMaterial(mat_default);
		        model.scale(1.75f);
		        model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
		        break;
			} else {
				//Germs_Fighter_1 laden
				Game.logger.debug("Unit.loadModel: Lade Model: Germ_Fighter_1.obj");
				model = Game.AM.loadModel("models/Germ_Fighter_1.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			}
		case Fighter_2:
			if(ownerID.equals(Game.playerID)){
				//AntiVir_Fighter_2 laden
				Game.logger.debug("Unit.loadModel: Lade Model: AntiVir_Fighter_2.obj");
				model = Game.AM.loadModel("models/AntiVir_Fighter_2.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			} else {
				//Germs_Fighter_2 laden
				Game.logger.debug("Unit.loadModel: Lade Model: Germ_Fighter_2.obj");
				model = Game.AM.loadModel("models/Germ_Fighter_2.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			}			
		case Fighter_3:
			if(ownerID.equals(Game.playerID)){
				//AntiVir_Fighter_3 laden
				Game.logger.debug("Unit.loadModel: Lade Model: AntiVir_Fighter_3.obj");
				model = Game.AM.loadModel("models/AntiVir_Fighter_3.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			} else {
				//Germs_Fighter_3 laden
				Game.logger.debug("Unit.loadModel: Lade Model: Germ_Fighter_3.obj");
				model = Game.AM.loadModel("models/Germ_Fighter_3.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			}
		case Transporter_1:
			if(ownerID.equals(Game.playerID)){
				//AntiVir_Transporter_1 laden
				Game.logger.debug("Unit.loadModel: Lade Model: AntiVir_Transporter_1.obj");
				model = Game.AM.loadModel("models/AntiVir_Transporter_1.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			} else {
				//Germs_Transporter_1 laden
				Game.logger.debug("Unit.loadModel: Lade Model: Germ_Transporter_1.obj");
				model = Game.AM.loadModel("models/Germ_Transporter_1.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			}
		case Transporter_2:
			if(ownerID.equals(Game.playerID)){
				//AntiVir_Transporter_2 laden
				Game.logger.debug("Unit.loadModel: Lade Model: AntiVir_Transporter_2.obj");
				model = Game.AM.loadModel("models/AntiVir_Transporter_2.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			} else {
				//Germs_Transporter_2 laden
				Game.logger.debug("Unit.loadModel: Lade Model: Germ_Transporter_2.obj");
				model = Game.AM.loadModel("models/Germ_Transporter_2.obj");
				model.scale(1.75f);
				model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
				break;
			}
		case Tradestation:
			//Handelsstation laden
			Game.logger.debug("Unit.loadModel: Lade Model: Tradestation.obj");
			model = Game.AM.loadModel("models/Tradestation.obj");
			model.scale(1.0f);
			model.rotate(random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI, random.nextFloat() * (float)Math.PI);
			break;
		}
		//Schatten aktivieren
		model.setShadowMode(ShadowMode.Receive);
		
		//StatusBox hinzufügen
		//Bestimmt die Größe der StatusBox und Höhe über dem Model der Einheit
		int torus_fragmentResolution = 20;
		int torus_circleResolution = 10;
		double radius_outer = 1.5d;
		double radius_inner = 0.25d;
		double offset = 5.0d;
		
		//Initialisiere Vertices, Texturkoordinaten
		Mesh mesh;
		Vector3f [] vertices = new Vector3f[(torus_fragmentResolution + 1) * (torus_circleResolution + 1)];
		Vector2f[] texCoord = new Vector2f[(torus_fragmentResolution + 1) * (torus_circleResolution + 1)];

		//Farbe der StatusBox-Fläche mit Transparenz
		ColorRGBA color = ColorRGBA.Black;
		
		//Mesh erstellen
		mesh = new Mesh();
		mesh.setMode(Mode.Triangles);
		
		//Indizes festlegen
		int indexes[] = new int[torus_fragmentResolution * torus_circleResolution * 6];
		for(int i = 0; i < torus_fragmentResolution; i++){
			for(int j = 0; j < torus_circleResolution; j++){
				indexes[(i * torus_circleResolution + j) * 6    ] = i * torus_circleResolution + j;
				indexes[(i * torus_circleResolution + j) * 6 + 1] = i * torus_circleResolution + j + 1;
				indexes[(i * torus_circleResolution + j) * 6 + 2] = i * torus_circleResolution + j + torus_circleResolution;
				
				indexes[(i * torus_circleResolution + j) * 6 + 3] = i * torus_circleResolution + j + torus_circleResolution;
				indexes[(i * torus_circleResolution + j) * 6 + 4] = i * torus_circleResolution + j + 1;
				indexes[(i * torus_circleResolution + j) * 6 + 5] = i * torus_circleResolution + j + torus_circleResolution + 1;
			}
		}
		
		
		//Texturkoordinaten für potentielle zukünftige Verwendung
		for(int i = 0; i < texCoord.length; i++){
			texCoord[i] = new Vector2f(0.0f, 0.0f);
		}
		
		//Vertices erstellen
		for(double i = 0; i <= torus_fragmentResolution; i++){
			for(double j = 0; j <= torus_circleResolution; j++){
				Vector3f point = new Vector3f();
				
				point.x = (float)((radius_outer + radius_inner * Math.cos(j * Math.PI * 2.0d / (double)torus_circleResolution)) * Math.cos(i * Math.PI * 2.0d / (double)torus_fragmentResolution));
				point.y = (float)(radius_inner * Math.sin(j * Math.PI * 2.0d / (double)torus_circleResolution));
				point.z = (float)((radius_outer + radius_inner * Math.cos(j * Math.PI * 2.0d / (double)torus_circleResolution)) * Math.sin(i * Math.PI * 2.0d / (double)torus_fragmentResolution));
				
				vertices[(int)i * torus_circleResolution + (int)j] = point;
			}
		}
		
		//Buffer setzen und Mesh erstellen
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexes));
		mesh.updateBound();
		
		//StatusBox hinzufügen
		Geometry statusBox = new Geometry("StatusBox", mesh);
		Material mat = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		statusBox.setMaterial(mat);
		statusBox.setShadowMode(ShadowMode.Cast);
		statusBox.move(0.0f, (float)offset, 0.0f);
		
		//Model und StatusBox in den Node einfügen
		node.attachChild(model);
		node.attachChild(statusBox);
		
		return node;
	}
	
	
	
	/**
	 * Erzeugt den Lebensbalken als Torus über der Einheit. Muss aufgerufen werden, wenn sich die Lebenspunkte der Einheit
	 * verändert haben. In diesem Fall wird das aktuelle Model entfernt und durch ein neues ersetzt.
	 * Je weniger Lebenspunkte die Einheit (im Vergleich zu ihren maximalen Lebenspunkten) besitzt, desto kleiner wird der Torus
	 * und desto mehr verändert sich seine Farbe von grün über gelb zu rot.
	 * 
	 * @param hitpoints_curr Momentanen Lebenspunkte der Einheit.
	 * @param hitpoints_max Maximalen Lebenspunkte der Einheit.
	 * 
	 * @author Peter Dörr
	 * @since 06.01.13
	 */
	public void loadHealthbar(int hitpoints_curr, int hitpoints_max){		
		//Alte Healthbar entfernen, falls nötig
		if(model.getQuantity() > 2){
			model.detachChildAt(2);
		}
		
		//Abbrechen, falls Healthbar nicht gezeigt werden soll
		if(!showHealtBar){
			return;
		}
		
		//Abbrechen bei Tradestations
		if(type == TYPE.Tradestation){
			return;
		}
		
		//Healthbar hinzufügen
		//Bestimmt die Größe der Healthbar und Höhe über dem Model der Einheit
		int torus_fragmentResolution = 20;
		int torus_circleResolution = 10;
		double radius_outer = 1.0d;
		double radius_inner = 0.25d;
		double offset = 5.0d;

		//Initialisiere Vertices, Texturkoordinaten
		Mesh mesh;
		Vector3f [] vertices = new Vector3f[(torus_fragmentResolution + 1) * (torus_circleResolution + 1)];
		Vector2f[] texCoord = new Vector2f[(torus_fragmentResolution + 1) * (torus_circleResolution + 1)];

		//Farbe der Healthbar-Fläche mit Transparenz
		ColorRGBA color = new ColorRGBA(1.0f - (float)hitpoints_curr/(float)hitpoints_max, (float)hitpoints_curr/(float)hitpoints_max, 0.0f, 1.0f);

		//Mesh erstellen
		mesh = new Mesh();
		mesh.setMode(Mode.Triangles);

		//Indizes festlegen
		int indexes[] = new int[torus_fragmentResolution * torus_circleResolution * 6];
		for(int i = 0; i < torus_fragmentResolution; i++){
			for(int j = 0; j < torus_circleResolution; j++){
				indexes[(i * torus_circleResolution + j) * 6    ] = i * torus_circleResolution + j;
				indexes[(i * torus_circleResolution + j) * 6 + 1] = i * torus_circleResolution + j + 1;
				indexes[(i * torus_circleResolution + j) * 6 + 2] = i * torus_circleResolution + j + torus_circleResolution;

				indexes[(i * torus_circleResolution + j) * 6 + 3] = i * torus_circleResolution + j + torus_circleResolution;
				indexes[(i * torus_circleResolution + j) * 6 + 4] = i * torus_circleResolution + j + 1;
				indexes[(i * torus_circleResolution + j) * 6 + 5] = i * torus_circleResolution + j + torus_circleResolution + 1;
			}
		}


		//Texturkoordinaten für potentielle zukünftige Verwendung
		for(int i = 0; i < texCoord.length; i++){
			texCoord[i] = new Vector2f(0.0f, 0.0f);
		}

		//Vertices erstellen
		float percentage = (float)hitpoints_curr/(float)hitpoints_max;
		for(double i = 0; i <= torus_fragmentResolution; i++){
			for(double j = 0; j <= torus_circleResolution; j++){
				Vector3f point = new Vector3f();

				point.x = (float)((radius_outer + radius_inner * Math.cos(j * Math.PI * 2.0d / (double)torus_circleResolution)) * Math.cos(percentage * i * Math.PI * 2.0d / (double)torus_fragmentResolution));
				point.y = (float)(radius_inner * Math.sin(j * Math.PI * 2.0d / (double)torus_circleResolution));
				point.z = (float)((radius_outer + radius_inner * Math.cos(j * Math.PI * 2.0d / (double)torus_circleResolution)) * Math.sin(percentage * i * Math.PI * 2.0d / (double)torus_fragmentResolution));

				vertices[(int)i * torus_circleResolution + (int)j] = point;
			}
		}

		//Buffer setzen und Mesh erstellen
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexes));
		mesh.updateBound();

		//Healthbar hinzufügen
		Geometry healthbar = new Geometry("HealthBar", mesh);
		Material mat = new Material(Game.AM, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		healthbar.setMaterial(mat);
		healthbar.setShadowMode(ShadowMode.Cast);
		healthbar.move(0.0f, (float)offset, 0.0f);
		healthbar.rotate(0.0f, healthBarRotation, 0.0f);
		
		//Healthbar in den Node einfügen
		model.attachChild(healthbar);
	}
}
