import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

/**
 * Fingerübung_VCP2012_Markus_Strobel
 * @author Markus Strobel
 *  
 * sources:
 * 
 * the methods initFloor, createKevaPlank, createBall, initTerrain_Skybox, initHUD are 
 * generally based on the code of the tutorials with some minor or major changes
 * but also other methods are created with the tutorial experiences
 * 
 * the heightmap, alphamap, the snowpeak texture and all ui graphics are selfmade with GIMP
 * 
 * the textures grass_dark.jpg, WoodFine0018_L.jpg, BrickRound0044_3_L.jpg are for free and from www.CGTextures.com
 * 
 */
public class Fingerübung_VCP2012_Markus_Strobel extends SimpleApplication {
	public static void main(String args[]) {
		AppSettings settings = new AppSettings(true);
		settings.setFullscreen(false);
		settings.setVSync(false);
		settings.setBitsPerPixel(24); // 24 bits per pixel -> true color
		settings.setFrameRate(40); // 40fps
		settings.setSamples(0); 
		settings.setTitle("Markus Strobel - VCP Fingerübung 2012/13 - \"First-Person-Tank-Clone\"");		
		settings.setResolution(1280, 720);
		settings.setFullscreen(false);		
//		settings.setResolution(1920, 1080);
//		settings.setFullscreen(true);

		Fingerübung_VCP2012_Markus_Strobel app = new Fingerübung_VCP2012_Markus_Strobel();
		app.setSettings(settings);
		app.setDisplayStatView(false);
		app.setDisplayFps(false);		
		app.setShowSettings(false); // disable the settings menu 		
		app.start();
	}

	/** 3D Model / Animation */
	private Spatial player1_model;
	private Spatial player2_model;
	private AnimControl player1AnimControl;
	private AnimControl player2AnimControl;	
	private AnimChannel player1AnimChannel;
	private AnimChannel player2AnimChannel;	    

	/** Prepare the Physics Application State (jBullet) */
	private BulletAppState bulletAppState;

	/** KEVA PLANKS settings */	
	private final float plankFactor = 0.4f; // scaling
	private final float plankLength = 4.5f * plankFactor;
	private final float plankWidth  = 0.25f * plankFactor;
	private final float plankHeight = 0.75f * plankFactor;

	/** Misc Settings */
	private float zero_level = -7f;
	private Boolean game_over = false;
	private int winning_condition = 5;
	private Vector3f player1TowerPosition = new Vector3f(5f, zero_level + 1f, 5f);
	private Vector3f player2TowerPosition = new Vector3f(-5f, zero_level + 1f, -5f);
	private Boolean performanceMode = false; // if true the keva planks wont be reattached to the garbageNode for visualisation
	private float stepHeight = 0.05f; // stepheight

	/** BALL settings */
	private float ballMass = 5f;
	private float ballRadius = 0.4f;
	private Boolean ball_mode = true;
	private long ballShotTime;
	private Boolean ball_flying = false;
	private float balanceFactor = 1.5f;

	/** Materials */
	private Material mat_planks;
	private Material mat_ball;
	private Material mat_terrain;
	private Material mat_floor1;
	private Material mat_floor2;

	/** terrain */
	private TerrainQuad terrain;

	/** nodes */
	private Node player1_planks_node;
	private Node player2_planks_node;
	private Node garbage_planks_node;
	private Node balls_node;

	/** hud texts */
	private BitmapText player1_planks_text;
	private BitmapText player2_planks_text;
	private BitmapText ball_mode_text;
	private BitmapText WINNING_text;
	private Picture player1ui;
	private Picture player2ui;

	/** player control */
	private Boolean left = false, right = false, up = false, down = false;
	private Boolean player1_turn = true;
	private Vector3f moveDirection = new Vector3f();
	private CharacterControl player1;
	private CharacterControl player2;
	private Boolean playerViewDirectionSetAtEnemyTower = false;	
	private Vector3f player1Position = new Vector3f(0f, zero_level + 4.3f, 28f);
	private Vector3f player2Position = new Vector3f(0f, zero_level + 4.3f, -28f);

	private Vector3f player1ViewDirection = new Vector3f(0f, 0f, -1f);
	private Vector3f player2ViewDirection = new Vector3f(0f, 0f, 1f);

	/**
	 * This method initializes the game
	 */
	@Override
	public void simpleInitApp() {
		/** Set up Physics Game */
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//		bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		/* Nodes */
		player1_planks_node = new Node();    
		player2_planks_node = new Node();
		garbage_planks_node = new Node();
		balls_node = new Node();				
		rootNode.attachChild(player1_planks_node);
		rootNode.attachChild(player2_planks_node);
		rootNode.attachChild(garbage_planks_node);
		rootNode.attachChild(balls_node);		

		// Inits		
		initMaterials();
		initKeys();
		initHUD();
		initTerrain_Skybox();
		initPlayers(player1Position, player2Position, stepHeight);
		initLight();
		initModelsAndAnimation();

		// Shadow
		PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
		pssmRenderer.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal()); 
		viewPort.addProcessor(pssmRenderer);
		rootNode.setShadowMode(ShadowMode.Off);	

		/* Init Player 1 and Player 2 floors */
		// Player 1
		initFloor(new Vector3f(0f, zero_level + 3f, 28f), 8f, 0.1f, 5f, mat_floor2, true);
		// Player 2
		initFloor(new Vector3f(0f, zero_level + 3f, -28f), 8f, 0.1f, 5f, mat_floor2, true);



		/* Init Towers */
		// Player 1
		initKevaPlankSimpleTower(player1TowerPosition, player1_planks_node, 12, 1, mat_planks, false, 3f);		
		createTeaPot(player1Position.add(-5f, -2f, 0f));
		// Player 1
		initKevaPlankSimpleTower(player2TowerPosition, player2_planks_node, 12, 1, mat_planks, false, 3f);
		createTeaPot(player2Position.add(5f, -2f, 0f));

		/* Init Castles */
		// Player 1		
		//		initCastle_v1(new Vector3f(7f, zero_level + 1f, 2f), player1_planks_node, 8, 4, mat_planks, mat_planks, 3f, 2f);
		// Player 2
		//		initCastle_v1(new Vector3f(-7f, zero_level + 1f, -2f), player2_planks_node, 8, 4, mat_planks, mat_planks, 3f, 2f);

		// init cam position
		cam.setLocation(player1_model.getLocalTranslation());
		cam.lookAtDirection(player1ViewDirection, Vector3f.UNIT_Y);

	}

	/**
	 * The game loop
	 * @param tpf ticks per frame
	 */
	@Override
	public void simpleUpdate(float tpf) {

		// Check for planks of player 1
		for(int i = 0; i < player1_planks_node.getChildren().size(); i++) {
			if(player1_planks_node.getChild(i).getLocalTranslation().y < zero_level - 1f) {
				// removes plank from the player1_planks_node if it has fall of the platform and add him (for visibility reason) to the garbage_bricks_node
				if(performanceMode)
				{
					player1_planks_node.detachChildAt(i);
				}
				else
				{
					garbage_planks_node.attachChild(player1_planks_node.detachChildAt(i));  
				}  		  
				i--;
			}
		}

		// Check for planks of player 2
		for(int i = 0; i < player2_planks_node.getChildren().size(); i++) {
			if(player2_planks_node.getChild(i).getLocalTranslation().y < zero_level - 1f) {
				// removes plank from the player2_planks_node if it has fall of the platform and add him (for visibility reason) to the garbage_bricks_node
				if(performanceMode)
				{
					player2_planks_node.detachChildAt(i);
				}
				else
				{
					garbage_planks_node.attachChild(player2_planks_node.detachChildAt(i));  
				}  	
				i--;
			}
		}
		player1_planks_text.setText("Player 1: " + String.valueOf(player1_planks_node.getChildren().size()));
		player2_planks_text.setText("Player 2: " + String.valueOf(player2_planks_node.getChildren().size()));


		int balls_counter = balls_node.getChildren().size();

		if((balls_node.getChildren().size() > 0 && balls_node.getChild(balls_counter - 1).getLocalTranslation().y > zero_level) && System.currentTimeMillis() - ballShotTime < 2000)
		{
			ball_flying = true;
			cam.setLocation(balls_node.getChild(balls_counter - 1).getLocalTranslation().add(cam.getDirection().negate().mult(3f)));
		}		
		else
		{			
			// Set Player View Direction 
			if(!playerViewDirectionSetAtEnemyTower)
			{
				if(player1_turn)
				{
					cam.setLocation(player1_model.getLocalTranslation());
					cam.lookAtDirection(player1ViewDirection, Vector3f.UNIT_Y);
				}
				else
				{
					cam.setLocation(player2_model.getLocalTranslation());
					cam.lookAtDirection(player2ViewDirection, Vector3f.UNIT_Y);
				}
				playerViewDirectionSetAtEnemyTower = true;
			}

			ball_flying = false;

			// player movement
			if(player1_turn)
			{		
				// Player 1 Ui visible
				guiNode.detachChild(player2ui);
				guiNode.attachChild(player1ui);
				player1_planks_text.setColor(ColorRGBA.Green);
				player2_planks_text.setColor(ColorRGBA.White);
				
				// Player1 Movement
				Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
				Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
				moveDirection.set(0, 0, 0);
				if (left)  { moveDirection.addLocal(camLeft); }
				if (right) { moveDirection.addLocal(camLeft.negate()); }
				if (up)    { moveDirection.addLocal(camDir); }
				if (down)  { moveDirection.addLocal(camDir.negate()); }
				moveDirection.y = 0f; // no "flying"
				player1.setWalkDirection(moveDirection.mult(0.3f));
				player1_model.setLocalTranslation(player1.getPhysicsLocation());
				Matrix3f rotation = cam.getRotation().toRotationMatrix();
				rotation.set(0, 1, 0f);
				rotation.set(1, 0, 0f);
				rotation.set(1, 2, 0f);
				rotation.set(2, 1, 0f);
				player1_model.setLocalRotation(rotation);
				cam.setLocation(player1.getPhysicsLocation().add(cam.getDirection().negate().mult(5f)).add(0f, 1.5f, 0f));
			}
			else
			{					
				// Player 2 Ui visible
				guiNode.detachChild(player1ui);
				guiNode.attachChild(player2ui);
				player2_planks_text.setColor(ColorRGBA.Green);
				player1_planks_text.setColor(ColorRGBA.White);
				
				// Player2 Movement
				Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
				Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
				moveDirection.set(0, 0, 0);
				if (left)  { moveDirection.addLocal(camLeft); }
				if (right) { moveDirection.addLocal(camLeft.negate()); }
				if (up)    { moveDirection.addLocal(camDir); }
				if (down)  { moveDirection.addLocal(camDir.negate()); }
				moveDirection.y = 0f;
				player2.setWalkDirection(moveDirection.mult(0.3f));
				player2_model.setLocalTranslation(player2.getPhysicsLocation());
				Matrix3f rotation = cam.getRotation().toRotationMatrix();
				rotation.set(0, 1, 0f);
				rotation.set(1, 0, 0f);
				rotation.set(1, 2, 0f);
				rotation.set(2, 1, 0f);
				player2_model.setLocalRotation(rotation);
				cam.setLocation(player2.getPhysicsLocation().add(cam.getDirection().negate().mult(5f)).add(0f, 1.5f, 0f));
			}
		}

		// Winning Conditions
		// case 1: planks below minimum 
		if(player1_planks_node.getChildren().size() < winning_condition)
		{
			WINNING_text.setText("player 1 has less then " + winning_condition + " planks on the platform\n\nPLAYER 2 WINS!");
			game_over = true;
		}
		if(player2_planks_node.getChildren().size() < winning_condition)
		{
			WINNING_text.setText("player 2 has less then " + winning_condition + " planks on the platform\n\nPLAYER 1 WINS!");
			game_over = true;
		}
		// case 2: enemy falling off the platform
		if(player1.getPhysicsLocation().y < zero_level - 6f)
		{
			WINNING_text.setText("player 1 fell of the platform! \n\nPLAYER 2 WINS!");
			game_over = true;
		}
		if(player2.getPhysicsLocation().y < zero_level - 6f)
		{
			WINNING_text.setText("player 2 fell of the platform! \n\nPLAYER 1 WINS!");
			game_over = true;
		}

		// catch the case if the player is falling off the platform
		if(player1.getPhysicsLocation().y < zero_level - 3f)
		{
			player1.setPhysicsLocation(player1Position);
		}
		if(player2.getPhysicsLocation().y < zero_level - 3f)
		{
			player2.setPhysicsLocation(player2Position);
		}

		// stop animation if not moving
		if(!up && !down && !left && !right)
		{
			stopAnimation();
		}		
	}

	/**
	 * The actionListener to get keyboard and mouse inputs
	 */
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("shoot") && !keyPressed && !ball_flying && !game_over) {	
				ballShotTime = System.currentTimeMillis();
				if(player1_turn)
				{
					player1ViewDirection = cam.getDirection();
					createBall(ballMass, player1_model);
				}
				else
				{
					player2ViewDirection = cam.getDirection();
					createBall(ballMass, player2_model);	
				}				

				player1_turn = !player1_turn;
				playerViewDirectionSetAtEnemyTower = false;
			}
			if (name.equals("Left") && !game_over)
			{
				left = keyPressed;
				startAnimation();
			}
			if (name.equals("Right") && !game_over)
			{
				right = keyPressed;
				startAnimation();
			}
			if (name.equals("Up") && !game_over)
			{
				up = keyPressed;
				startAnimation();
			}
			if (name.equals("Down") && !game_over)
			{
				down = keyPressed;
				startAnimation();
			}
			if (name.equals("mode") && !keyPressed && !game_over) {
				ball_mode = !ball_mode;
				if(ball_mode) {
					ball_mode_text.setText("Slow Ball");
					ballRadius = 0.7f;
					ballMass = 5f * balanceFactor;
				}
				else {
					ball_mode_text.setText("Fast Ball");
					ballRadius = 0.3f;
					ballMass = 2f * balanceFactor;
				}				
			}

		}
	};

	/**
	 * This method starts the animation of the player models (based on the tutorial HelloAnimation)
	 */
	private void startAnimation()
	{
		if(player1_turn)
		{		      
			if (!player1AnimChannel.getAnimationName().equals("Walk")) 
			{
				player1AnimChannel.setAnim("Walk", 0.50f);
				player1AnimChannel.setLoopMode(LoopMode.Loop);
			}
		}
		else
		{
			if (!player2AnimChannel.getAnimationName().equals("Walk")) 
			{
				player2AnimChannel.setAnim("Walk", 0.50f);
				player2AnimChannel.setLoopMode(LoopMode.Loop);
			}					
		}
	}

	/**
	 * This method stops the animation of the player models (based on the tutorial HelloAnimation)
	 */
	private void stopAnimation()
	{
		// disable animation
		if(!up || !down || !left || !right)
		{
			if(player1_turn)
			{
				if (player1AnimChannel.getAnimationName().equals("Walk")) 
				{
					player1AnimChannel.setAnim("stand", 0.50f);
					player1AnimChannel.setLoopMode(LoopMode.DontLoop);
					player1AnimChannel.setSpeed(1f);
				}
			}
			else
			{
				if (player2AnimChannel.getAnimationName().equals("Walk")) 
				{
					player2AnimChannel.setAnim("stand", 0.50f);
					player2AnimChannel.setLoopMode(LoopMode.DontLoop);
					player2AnimChannel.setSpeed(1f);
				}
			}
		}
	}

	/**
	 * This method initialize all used materials
	 */
	private void initMaterials() {
		/* KEVA PLANKS */
		mat_planks = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key = new TextureKey("images/textures/WoodFine0018_L.jpg");
		key.isFlipY();
		key.setGenerateMips(true);
		Texture tex = assetManager.loadTexture(key);
		mat_planks.setTexture("ColorMap", tex);

		/* stone_mat1 */
		mat_ball = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
		key2.setGenerateMips(true);
		Texture tex2 = assetManager.loadTexture(key2);
		mat_ball.setTexture("ColorMap", tex2);

		/* floor1 */
		mat_floor1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		mat_floor1.setTexture("ColorMap", tex3);

		//		/* floor2 */
		mat_floor2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key4 = new TextureKey("images/textures/BrickRound0044_3_L.jpg");
		key4.setGenerateMips(true);
		Texture tex4 = assetManager.loadTexture(key4);
		tex4.setWrap(WrapMode.Repeat);
		mat_floor2.setTexture("ColorMap", tex4);

		/* terrain */
		mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
	}

	/**
	 * This method is the floor/platform of the players (based on the tutorial HelloPhysics)
	 * @param position the centre position of the players platform
	 * @param length the half length of the platform
	 * @param height the half height of the platform
	 * @param width the half width of the platform
	 * @param material the material of the platform
	 * @param border if the platform has a border to avoid falling of the platform
	 */
	private void initFloor(Vector3f position, float length, float height, float width, Material material, Boolean border) {

		/** Initialize the floor geometry */
		Box floor = new Box(Vector3f.ZERO, length, height, width);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Geometry floor_geo = new Geometry("Floor", floor);
		floor_geo.setMaterial(material);
		floor_geo.setLocalTranslation(position.x, position.y - height - plankHeight, position.z);
		floor_geo.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(floor_geo);

		/* Make the floor physical with mass 0.0f! */
		RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
		floor_geo.addControl(floor_phy);
		bulletAppState.getPhysicsSpace().add(floor_phy);

		// to avoid falling off the platform
		if(border)
		{
			// X
			floor_border(position.add(0f, -.1f, width), length, 0.25f, 0.1f, mat_planks);
			floor_border(position.add(0f, -.1f, -width), length, 0.25f, 0.1f, mat_planks);

			// Y
			floor_border(position.add(length, -.1f, 0f), 0.1f, 0.25f, width, mat_planks);
			floor_border(position.add(-length, -.1f, 0f), 0.1f, 0.25f, width, mat_planks);
		}
	}

	/**
	 * This method creates a box for the floor border
	 * @param position the position of the box
	 * @param length the length of the box
	 * @param width the width of the box
	 */
	private void floor_border(Vector3f position, float length, float height, float width, Material material)
	{		
		// X + width
		Box box = new Box(Vector3f.ZERO, length, height, width);

		Geometry floor_border = new Geometry("floor_border", box);

		floor_border.setMaterial(material);
		rootNode.attachChild(floor_border);

		floor_border.setLocalTranslation(position);

		RigidBodyControl border_physic = new RigidBodyControl(0f);
		floor_border.addControl(border_physic);

		bulletAppState.getPhysicsSpace().add(border_physic);
	}

	/**
	 * This method initialize the Keva Planks Tower and creates the neccessary floor
	 * @param startposition the position of the towers centre
	 * @param playerNode the node where all keva planks will be added to (this is useful to count them)
	 * @param height_max the stages of the tower
	 * @param length_max how many keva planks will be set in row -> causes the tower dimension to scale with it: 1 = 1x1, 2 = 2x2, 3 = 3x3, etc
	 * @param material the material texture of the keva planks
	 * @param roof if their should be a roof or not (not yet implemented)
	 * @param planksMass the mass of each keva plank
	 */
	public void initKevaPlankSimpleTower(Vector3f startposition, Node playerNode, int height_max, int length_max, Material material, Boolean roof, float planksMass) {

		//		// initiate the towers platform position (works and is scalable)		
		Vector3f floorPosition = new Vector3f(startposition.x + plankLength * (length_max - 1), startposition.y, startposition.z  + plankLength * (length_max - 1));		
		initFloor(floorPosition, length_max * plankLength, 0.1f , length_max * plankLength, mat_floor1, false);

		Vector3f position;
		float distance;

		int width_max = length_max * 4 - (length_max - 1);
		distance = (plankLength - 4f * plankWidth) / 3; 

		// individuelle Positionierung
		Vector3f startpoint_0 = new Vector3f(0f, 0f, - plankLength / 2f - 8 * plankWidth);
		startpoint_0 = startpoint_0.add(startposition);
		Vector3f startpoint_1 = new Vector3f(-plankLength + length_max * plankWidth, 0f, plankLength - length_max * plankWidth - plankLength / 2f - 8 * plankWidth);
		startpoint_1 = startpoint_1.add(startposition);

		for(int height = 0; height < height_max; height++) { // Höhe des Keva Planks Turm					Y	  
			for(int width = 0; width < width_max; width++) { // Länge des Keva Planks Turm 			  	Z
				for(int length = 0; length < length_max; length++) { // Breite des Keva Planks Turm 		X

					String name = "kevaPlank_" + String.valueOf(height) + "_" + String.valueOf(width) + "_" + String.valueOf(length);

					if(height % 2 == 0) { // Gerade Höhe
						position = new Vector3f(2 * length * plankLength, height * plankHeight * 2f, 2 * width * (distance + plankWidth) );
						position = position.add(startpoint_0);					  
						createKevaPlank(name, position, 0f, 0f, 0f, planksMass, plankLength, plankHeight, plankWidth, material, playerNode);
					}
					else { // Ungerade Höhe
						position = new Vector3f(2 * width * (distance + plankWidth), height * plankHeight * 2f, 2 * length * plankLength + plankWidth * (length_max - 1));
						position = position.add(startpoint_1);				
						createKevaPlank(name, position, 0f, 90f, 0f, planksMass, plankLength, plankHeight, plankWidth, material, playerNode);
					}
				}
			}
		}




		int topPlanks_max = (int) (plankLength * length_max / plankHeight) ; // Damit die exakte Anzahl der Planks für die zwischenebene ermittelt wird, bei 4.5 / 0.75 = 6

		// TO-DO stabile Decke zwischen Turm und Dach bauen
		for(int topPlanks = 0; topPlanks < topPlanks_max; topPlanks++) { // Länge des Keva Planks Turm 			  	Z
			for(int length = 0; length < length_max; length++) { // Breite des Keva Planks Turm 		X

				String name = "kevaPlank_top_" + String.valueOf(topPlanks) + "_" + String.valueOf(length);

				if(height_max % 2 == 0) { // Gerade Höhe
					position = new Vector3f(2 * plankLength * length, height_max * plankHeight * 2f - 2 * plankWidth, 2 * topPlanks * plankHeight + 2 * plankWidth);
					position = position.add(startpoint_0);					  
					createKevaPlank(name, position, 90f, 0f, 0f, planksMass, plankLength, plankHeight, plankWidth, material, playerNode);
				}
				else { // Ungerade Höhe
					position = new Vector3f(2 * topPlanks * plankHeight + 2 * plankWidth, height_max * plankHeight * 2f - 2 * plankWidth, 2 * plankLength * length);
					position = position.add(startpoint_1);				
					createKevaPlank(name, position, 90f, 90f, 0f, planksMass, plankLength, plankHeight, plankWidth, material, playerNode);
				}
			}
		}

	}

	/**
	 * This method initialize the HUD
	 */
	private void initHUD() {

		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

//		// Crosshair
//		Picture crosshair = new Picture("crosshair");
//		crosshair.setImage(assetManager, "images/textures/ui/crosshair_v1.png", true);
//		crosshair.setWidth(30f);
//		crosshair.setHeight(30f);
//		crosshair.setLocalTranslation(settings.getWidth() / 2f - 15, settings.getHeight() / 2f - 15, 0f);
//		guiNode.attachChild(crosshair);
		
		// Player 1 UI
		player1ui = new Picture("player1ui");
		player1ui.setImage(assetManager, "images/textures/ui/player1ui.png", true);
		player1ui.setWidth(250f);
		player1ui.setHeight(150f);
		player1ui.setLocalTranslation(10f, 10f, -1f);
		guiNode.attachChild(player1ui);
		
		// Player 2 UI
		player2ui = new Picture("player2ui");
		player2ui.setImage(assetManager, "images/textures/ui/player2ui.png", true);
		player2ui.setWidth(250f);
		player2ui.setHeight(150f);
		player2ui.setLocalTranslation(10f, 10f, -1f);
		guiNode.attachChild(player2ui);


		// Score Player 1
		player1_planks_text = new BitmapText(guiFont, false);
		player1_planks_text.setText("Player 1: " + String.valueOf(player1_planks_node.getChildren().size()));
		player1_planks_text.setLocalTranslation(100f, 130f , 0f);
		player1_planks_text.setSize(25f);
		guiNode.attachChild(player1_planks_text);

		// Score Player 2
		player2_planks_text = new BitmapText(guiFont, false);
		player2_planks_text.setText("Player 2: " + String.valueOf(player2_planks_node.getChildren().size()));
		player2_planks_text.setLocalTranslation(100f, 85f , 0f);
		player2_planks_text.setSize(25f);
		guiNode.attachChild(player2_planks_text);

		// Ball Mode
		ball_mode_text = new BitmapText(guiFont, false);
		ball_mode_text.setText("Slow Ball");
		ball_mode_text.setLocalTranslation(65f, 35f , 0f);
		ball_mode_text.setSize(15f);
		guiNode.attachChild(ball_mode_text);

		// WINNING Text
		WINNING_text = new BitmapText(guiFont, false);
		WINNING_text.setSize(50f);
		WINNING_text.setBox(new Rectangle(-250f, 300f, 500f, 200f));
		WINNING_text.setAlignment(Align.Center);
		WINNING_text.setVerticalAlignment(VAlign.Center);
		WINNING_text.setText("");
		WINNING_text.setLocalTranslation(settings.getWidth() / 2 , settings.getHeight() / 2, 0f);
		guiNode.attachChild(WINNING_text);

	}

	/**
	 * This method initialize the terrain and the skybox
	 */
	private void initTerrain_Skybox() {

		// skybox
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));

		// this part is based on the terrain tutorial
		// alpha map for the heightmap
		mat_terrain.setTexture("Alpha", assetManager.loadTexture(
				"images/textures/terrain/heightmap_3_alpha.jpg"));

		// grass texture Tex1
		Texture grass = assetManager.loadTexture(
				"images/textures/grass_dark_002_L.jpg");
		grass.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);

		// rock texture Tex2
		Texture rock = assetManager.loadTexture(
				"Textures/Terrain/splat/dirt.jpg");
		rock.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex2", rock);
		mat_terrain.setFloat("Tex2Scale", 32f);

		// snowpeak texture Tex3
		Texture peak = assetManager.loadTexture(
				"images/textures/snowpeak.jpg");
		peak.setWrap(WrapMode.Repeat);
		mat_terrain.setTexture("Tex3", peak);
		mat_terrain.setFloat("Tex3Scale", 128f);

		AbstractHeightMap heightmap = null;
		Texture heightMapImage = assetManager.loadTexture(
				"images/textures/terrain/heightmap_3.jpg");
		heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
		heightmap.load();

		int patchSize = 65;
		terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

		terrain.setMaterial(mat_terrain);
		terrain.setLocalTranslation(0, -20, 0);
		terrain.setLocalScale(1.8f, 0.8f, 1.8f);
		terrain.scale(0.2f);
		terrain.setShadowMode(ShadowMode.CastAndReceive);
		rootNode.attachChild(terrain);

		// terrain physic
		RigidBodyControl map_physic = new RigidBodyControl(0f);
		terrain.addControl(map_physic);
		bulletAppState.getPhysicsSpace().add(map_physic);

		TerrainLodControl control = new TerrainLodControl(terrain, getCamera());

		terrain.addControl(control);
	}

	/**
	 * This method introduced some ambient light for the 3d models
	 * This code is based on the jmonkey tutorial HelloAssets
	 */
	private void initLight()
	{
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);
	}

	/**
	 * This method initiates the players
	 * @param player1_position the position of player1
	 * @param player2_position the position of player2
	 */
	private void initPlayers(Vector3f player1_position, Vector3f player2_position, float stepHeight)
	{
		// player 1
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.5f, 2f);
		player1 = new CharacterControl(capsuleShape, stepHeight);
		player1.setJumpSpeed(0);
		player1.setFallSpeed(30);
		player1.setGravity(30);
		player1.setPhysicsLocation(player1_position); 	    

		// player 2
		CapsuleCollisionShape capsuleShape2 = new CapsuleCollisionShape(0.5f, 2f);
		player2 = new CharacterControl(capsuleShape2, stepHeight);
		player2.setJumpSpeed(0);
		player2.setFallSpeed(30);
		player2.setGravity(30);
		player2.setPhysicsLocation(player2_position);

		bulletAppState.getPhysicsSpace().add(player1);
		bulletAppState.getPhysicsSpace().add(player2);
	}

	/**
	 * This method initialize the key input settings (based on the tutorial code HelloInput)
	 */
	private void initKeys()
	{	
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("mode", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addListener(actionListener, new String[]{"Left", "Right", "Up", "Down", "shoot", "mode"});  		
	}

	/**
	 * This method initialize the player models and the animations (based on the tutorial code HelloAnimation)
	 */
	private void initModelsAndAnimation()
	{		
		player1_model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");	
		player2_model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");			
		player1_model.setLocalScale(0.25f);
		player2_model.setLocalScale(0.25f);		
		player1_model.setLocalTranslation(player1Position);
		player2_model.setLocalTranslation(player2Position);
		rootNode.attachChild(player1_model);
		rootNode.attachChild(player2_model);
		player1AnimControl = player1_model.getControl(AnimControl.class);
		player2AnimControl = player2_model.getControl(AnimControl.class);		
		player1AnimChannel = player1AnimControl.createChannel();
		player2AnimChannel = player2AnimControl.createChannel();
		player1AnimChannel.setAnim("stand");
		player2AnimChannel.setAnim("stand");        
	}

	/**
	 * This method creates a tea pot at the given position (based on the tutorial code HelloAnimation)
	 * @param position the position of the tea pot
	 */
	private void createTeaPot(Vector3f position)
	{
		Spatial teapot_player = assetManager.loadModel("Models/Teapot/Teapot.obj");
		RigidBodyControl teapot_physics = new RigidBodyControl(0.5f);
		Material teapot_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		teapot_player.setMaterial(teapot_mat);     
		teapot_player.setLocalTranslation(position.add(new Vector3f(0f, 0.5f, 0f)));
		rootNode.attachChild(teapot_player);
		teapot_player.setShadowMode(ShadowMode.CastAndReceive);
		teapot_player.addControl(teapot_physics);
		bulletAppState.getPhysicsSpace().add(teapot_player);
	}

	/**
	 * This method builds a castle around his position.
	 * @param position the position where the castle will build around
	 * @param node the node which the planks will be attached to
	 * @param towerHeight how many plank rows will stacked at the corners
	 * @param midHeight how many plank rows will stacked at the mid "walls"
	 * @param towerMat the material of the towers in the corner
	 * @param midMat the material of the walls in the mid
	 * @param towerPlanksMass the mass of the tower planks
	 * @param planksMass the mass of the wall planks
	 */
	@SuppressWarnings("unused")
	private void initCastle_v1(Vector3f position, Node node, int towerHeight, int midHeight, Material towerMat, Material midMat, float towerPlanksMass, float planksMass) {

		float x = position.x;
		float y = position.y;
		float z = position.z;

		// Keva Planks Simple Tower build by modules

		initKevaPlankSimpleTower(new Vector3f(x + (-10f + 4 * plankWidth / plankFactor) * plankFactor, y + 0 * plankFactor, z + (10f - 4 * plankWidth / plankFactor)* plankFactor), node, towerHeight, 1, towerMat, true, towerPlanksMass);		
		initKevaPlankSimpleTower(new Vector3f(x + 0 * plankFactor, y + 0 * plankFactor, z + (10f - 4 * plankWidth / plankFactor) * plankFactor), node, midHeight, 1, midMat, false, planksMass);		
		initKevaPlankSimpleTower(new Vector3f(x + (10f - 4 * plankWidth / plankFactor) * plankFactor, y + 0 * plankFactor, z + (10f - 4 * plankWidth / plankFactor)* plankFactor), node, towerHeight, 1, towerMat, true, towerPlanksMass);	
		//		
		initKevaPlankSimpleTower(new Vector3f(x + (-10f + 4 * plankWidth / plankFactor) * plankFactor, y + 0* plankFactor, z + 0* plankFactor), node, midHeight, 1, midMat, false, planksMass);	
		// castle centre
		//		initKevaPlankSimpleTower(new Vector3f(x + 0 * plankFactor, y + 0 * plankFactor, z + 0 * plankFactor), node, 2, 1, mat_planks, false);		
		initKevaPlankSimpleTower(new Vector3f(x + (10f - 4 * plankWidth / plankFactor) * plankFactor, y + 0 * plankFactor, z + 0 * plankFactor), node, midHeight, 1, midMat, false, planksMass);		
		//		
		initKevaPlankSimpleTower(new Vector3f(x + (-10f + 4 * plankWidth / plankFactor) * plankFactor, y + 0 * plankFactor, z + (-10f + 4 * plankWidth / plankFactor)* plankFactor), node, towerHeight, 1, towerMat, true, towerPlanksMass);		
		initKevaPlankSimpleTower(new Vector3f(x + 0 * plankFactor, y + 0 * plankFactor, z + (-10f + 4 * plankWidth / plankFactor) * plankFactor), node, midHeight, 1, midMat, false, planksMass);		
		initKevaPlankSimpleTower(new Vector3f(x + (10f - 4 * plankWidth / plankFactor) * plankFactor, y + 0 * plankFactor, z + (-10f + 4 * plankWidth / plankFactor)* plankFactor), node, towerHeight, 1, towerMat, true, towerPlanksMass);	

	}

	/**
	 * This method create a keva plank
	 * @param name the name of the keva plank
	 * @param position the position of the keva planks' centre
	 * @param rotate_xAngle the rotation around the x axis
	 * @param rotate_yAngle the rotation around the y axis
	 * @param rotate_zAngle the rotation around the z axis
	 * @param mass the mass of the keva plank
	 * @param length the length of the keva plank
	 * @param width the width of the keva plank
	 * @param height the height of the keva plank
	 * @param material the material of the keva plank
	 * @param node the node which the keva planks will be attached to
	 */
	public void createKevaPlank(String name, Vector3f position, float rotate_xAngle, float rotate_yAngle, float rotate_zAngle, float mass, float length, float height, float width, Material material, Node node) {

		// TO DO Friction settings variieren, damit das Dach eventuell nicht einstürzt

		// based on the tutorial code "hello physics"
		Box box = new Box(Vector3f.ZERO, length, height, width);
		box.scaleTextureCoordinates(new Vector2f(1f, .5f));

		Geometry brick_geo = new Geometry(name, box);
		brick_geo.setMaterial(material);
		brick_geo.setShadowMode(ShadowMode.CastAndReceive);
		node.attachChild(brick_geo);

		brick_geo.setLocalTranslation(position);
		brick_geo.rotate(rotate_xAngle * FastMath.DEG_TO_RAD, rotate_yAngle * FastMath.DEG_TO_RAD, rotate_zAngle * FastMath.DEG_TO_RAD);

		RigidBodyControl plank_physic = new RigidBodyControl(mass);
		brick_geo.addControl(plank_physic);
		bulletAppState.getPhysicsSpace().add(plank_physic);
	}

	/**
	 * This method creates a ball and is based on the tutorial HelloPhysics
	 * @param mass the mass of the ball
	 * @param model the model is required for the spawn position of the ball, because the ball needs some distance to the model
	 */
	public void createBall(float mass, Spatial model) {

		Sphere sphere = new Sphere(32, 32, ballRadius, true, false);
		sphere.setTextureMode(TextureMode.Projected);		

		// Create a ball geometry and attach to scene graph
		Geometry ball_geo = new Geometry("cannon ball", sphere);
		ball_geo.setMaterial(mat_ball);
		balls_node.attachChild(ball_geo);

		// Position the cannon ball
		ball_geo.setLocalTranslation(model.getLocalTranslation().add(cam.getDirection().mult(2f)));
		// Shadow
		ball_geo.setShadowMode(ShadowMode.CastAndReceive);

		RigidBodyControl ball_phy = new RigidBodyControl(mass);
		ball_geo.addControl(ball_phy);
		bulletAppState.getPhysicsSpace().add(ball_phy);
		int speed = (int) (balanceFactor * 90f / mass);

		// speed
		ball_phy.setLinearVelocity(cam.getDirection().mult(speed));
	}
}