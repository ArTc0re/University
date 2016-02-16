package bigdata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author MarkusStrobel
 * @email mksstrobel@googlemail.com
 */
public class DisplayClass {
		 
	private int VertexArrayObjectId = -1;
	private int BoundingBoxesVertexArrayObjectId = -1;
	private int SplatVertexArrayObjectId = -1;
	
	
	private List<VBO> VBOsList = new ArrayList<VBO>();
	private VBO DataVBO;
	private VBO BoundingBoxesVBO;
	private VBO SplatsVBO;
		
	private int PointCloudShaderProgramId;
	private int BoundingBoxShaderProgramId;
	
	private int height = 768;
	private int width = 1024;
	private GUICanvas gui;
	
	private Vector3f[] data;
	private Octree dataOctree;
	
	// Splats
	int octreeMaxDepth;
	int octreeVisualizationDepth;
	ArrayList<Octree> LeafsMaxDepth;
	private float splatScale;;
	boolean circleSplats = true;
	
	
	// model matrices stuff
	int modelMatrixLocation = -1;
	Vector3f modelPos;
	Vector3f modelAngle;
	Vector3f modelScale;	
	Matrix4f modelMatrix;		
	Vector3f scaleShrink, scaleGrow;
	float rotationDelta;
	float positionDelta;
	FloatBuffer matrixBuffer;
	
	
	// render selection
	private enum RenderVisualizationMode { None, PointCloud, Splats };	
	private RenderVisualizationMode RenderMode = RenderVisualizationMode.PointCloud;
	
	// Octree Visualisierung	
	boolean wireFrameOctree = false;
	boolean showDistributionColors = true;
	private enum OctreeVisualizationMode { None, Detailed, OctreeState };
	private OctreeVisualizationMode OctreeMode = OctreeVisualizationMode.None;

	/**
	 * generate start Display
	 */
	public void start(Octree octree) {
		
		try {
			//high & width of display & Title
			this.gui = new GUICanvas("Big Data Visualisation", this.width, this.height);
			Display.setDisplayMode(new DisplayMode(this.width, this.height));
			Display.setParent(gui.getCanvas());
			Display.setTitle("3D Plot mit OpenGL");
			Display.create();

			GL11.glViewport(0, 0, this.width, this.height);

		} catch(LWJGLException e){

			e.printStackTrace();
			System.exit(0);
		}
	
		dataOctree = octree;
		// all vertices
		this.data =	dataOctree.getLeafsData();
	
		LeafsMaxDepth = octree.getLeafs();
		splatScale = 1.75f;
		octreeMaxDepth = octree.max_depth;
		octreeVisualizationDepth = octree.max_depth - 2;
		
		//creat VBOs		
		CreatePointCloudVBO();					
		CreateOctreeBoundingBoxVBO2(octree.getDepthRelatedLeafs(octreeVisualizationDepth), octreeVisualizationDepth);
		gui.setCurrentMode(0, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
		
		
		CreateCircleSplats(LeafsMaxDepth, octreeMaxDepth, splatScale);
		
		initShaderStuff();

		while (!Display.isCloseRequested()) {

			//loop
			this.loop();

			//Frames set to 60
			Display.sync(60);
			Display.update();
		}
		this.disposeOpenGL();			
	}
			
	private void initShaderStuff()
	{				
		PointCloudShaderProgramId = this.initPointCloudShaders();
		BoundingBoxShaderProgramId = this.initBoundingBoxShaders();
		
	    GL20.glBindAttribLocation(BoundingBoxShaderProgramId, 0, "in_position");
	    GL20.glBindAttribLocation(BoundingBoxShaderProgramId, 1, "in_color");
		
		modelPos = new Vector3f(0, -0.125f, 0);
		modelAngle = new Vector3f(15, 15, 0);
		modelScale = new Vector3f(5, 5, 5);	
		
		scaleGrow = new Vector3f(0.1f, 0.1f, 0.1f);
		scaleShrink = new Vector3f(-0.1f, -0.1f, -0.1f);		
		positionDelta = 0.025f;
		rotationDelta = 10f;		
	
		// init Matrix buffer for Model Matrix
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	private void CreatePointCloudVBO() {
		
		int VerticesBufferId = -1;
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(data.length * 3);
		
		for (Vector3f vtx : data)
			verticesBuffer.put(new float[] {vtx.x, vtx.y, vtx.z});
		verticesBuffer.flip();
		
		// Vertex Array Object
		VertexArrayObjectId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VertexArrayObjectId);
		
		// vertices
		VerticesBufferId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VerticesBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, true, 0, 0);
				
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);		 
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);		

		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			
		DataVBO = new VBO(VerticesBufferId, -1, this.data.length, -1);
		VBOsList.add(DataVBO);
	}		
	
	private float GetDistance(float min, float max)
	{		
		if(min < 0f && max < 0f)
		{
			return (Math.abs(min) - Math.abs(max)); 
		}
		else if(min < 0f && max >= 0f)
		{
			return (max + Math.abs(min));
		}
		else
		{
			return (max - min);
		}
	}
	
	private void CreateOctreeBoundingBoxVBO(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, int treeDepth)
	{	
		if(treeDepth < 1)
			treeDepth = 1;
		else if(treeDepth > 6)
		{
			treeDepth = 5;
			System.out.println("BoundingBoxes depth is limited to 5 due to performance issues");
		}
		
		// compute distances between min and max		
		float xDistance = GetDistance(xMin, xMax);
		float yDistance = GetDistance(yMin, yMax);
		float zDistance = GetDistance(zMin, zMax);
		
		int div = (int)Math.pow(2, treeDepth);
		
		// compute deltas for each subvoxel
		float xDelta = xDistance / div; // example: depth = 1, xDistance = 1 -> 1 / (1+1) = 0.5 xDelta (width) per subvoxel
		float yDelta = yDistance / div; 
		float zDelta = zDistance / div; 
		
		
		int range = (int)Math.pow(2, treeDepth) + 1; // 
		int verticesCount = (int) Math.pow(range, 3);		
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verticesCount * 3);
				
		float xCurrent = xMin;
		float yCurrent = yMin;
		float zCurrent = zMin;

		// example -> depth 1 -> 8 ^ (treeDepth) Subvoxel, -> (2 ^ (treeDepth) + 1) ^ 3 Vertices
		// TODO -> Vertices können im späteren Verlauf direkt vom Octree bezogen werden.
		for (int z = 0; z < range; z++) // depth
		{
			for(int y = 0; y < range; y++) // height
			{
				for(int x = 0; x < range; x++) // width
				{
					verticesBuffer.put(new float[]{ xCurrent, yCurrent, zCurrent });
					xCurrent += xDelta;
				}
				yCurrent += yDelta;
				xCurrent = xMin;
			}			
			zCurrent += zDelta;
			yCurrent = yMin;
		}
		
		
		// ######################
		// indices
		// ######################
		int indices = range * (range - 1) * (range -1) * 21;		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices);
		int i;
		
		int rangeQuad = range * range;
		for(int z = 0; z < range; z++)
		{
			i = z * rangeQuad;			
			for(int y = 0; y < range; y++)
			{
				for(int x = 0; x < range; x++)
				{
					int val = i + (y * range) + x;	
										
					if(y < range - 1 && x < range - 1)
					{
						// XY - Planes
						indicesBuffer.put(new int[] { val, val + 1, val + range} ); // 1. XY Triangle
						indicesBuffer.put(new int[] { val + 1, val + range, val + range + 1} ); // 2. XY Triangle		
						
						if(z < range - 1 && y < range -1 && x < range - 1)
						{
							// XZ - Planes (incomplete)
							indicesBuffer.put(new int[] { val , val + 1, val + rangeQuad} ); // 3. XZ Triangle
							indicesBuffer.put(new int[] { val + 1 , val + rangeQuad, val + rangeQuad + 1} ); // 4. XZ Triangle
							
							// YZ - Planes (incomplete)
							indicesBuffer.put(new int[] { val , val + range, val + rangeQuad} ); // 5. YZ Triangle
							indicesBuffer.put(new int[] { val + range , val + rangeQuad, val + rangeQuad + range} ); // 6. YZ Triangle
						}
					}
					else // finish XZ, YZ Planes
					{
						if(y == range - 1 && x < range - 1 && z < range - 1)
						{
							//val = i + (y * range) + x;	
							// finish XZ - Planes
							indicesBuffer.put(new int[] { val , val + 1, val + rangeQuad} ); // 3. XZ Triangle
							indicesBuffer.put(new int[] { val + 1 , val + rangeQuad, val + rangeQuad + 1} ); // 4. XZ Triangle							
						}
						else if(x == range - 1  && y < range - 1 && z < range - 1)
						{
							// finish YZ - Planes
							indicesBuffer.put(new int[] { val , val + range, val + rangeQuad} ); // 5. YZ Triangle
							indicesBuffer.put(new int[] { val + range , val + rangeQuad, val + rangeQuad + range} ); // 6. YZ Triangle
						}
					}
										
					
				}
			}
		}
		

		
		
		
		
		
		
		verticesBuffer.flip();
		indicesBuffer.flip();
		
		BoundingBoxesVertexArrayObjectId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(BoundingBoxesVertexArrayObjectId);
		
		// Create a new VBO for the indices and select it (bind)
		int VertexBufferObjectId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VertexBufferObjectId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in the attributes list at index 1
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, true, 0, 0);
		
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
        
        // Create a new VBO for the indices and select it (bind)
        int indicesBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
        BoundingBoxesVBO = new VBO(VertexBufferObjectId, indicesBufferId, verticesBuffer.capacity(), indicesBuffer.capacity());
        VBOsList.add(BoundingBoxesVBO);
	}
	
	private void CreateOctreeBoundingBoxVBO2(ArrayList<Octree> octrees, int selectedDepth)
	{	
		float xOff;
		float yOff;
		float zOff;
		int currentDepth;
		
		Vector3f cent;
		
		int verticesCount = octrees.size() * 8;
		int indicesCount = octrees.size() * 6 * 6;
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(verticesCount * 3);
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount);
		
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(verticesCount * 3);
		
		int maxNum = 0;
		for(Octree oct : octrees)
		{
			int val = oct.inside_data.length;			
			if(val > maxNum)
				maxNum = val;
		}
		
		int i = 0;
		for(Octree oct : octrees)
		{
			cent = oct.center;			
			xOff = oct.offsetX;
			yOff = oct.offsetY;
			zOff = oct.offsetZ;
			
			// if OctreeVisualizationMode.Detailed is selected, we compute bigger subvoxels to correct size
			if(OctreeMode.equals(OctreeVisualizationMode.Detailed))
			{
				currentDepth = oct.current_depth;
								
				while(currentDepth < selectedDepth)
				{
					Vector3f rep = oct.getRepresentative();
					
					if(rep != null) // if we know the oddball point, then we can get the correct center of lower level
					{
						xOff /= 2f;
						yOff /= 2f;
						zOff /= 2f;
						
						if(cent.x > rep.x) // X						
							cent.x -= xOff;
						else						
							cent.x += xOff;

						if(cent.y > rep.y) // Y						
							cent.y -= yOff;
						else						
							cent.y += yOff;
						
						if(cent.z > rep.z) // Z						
							cent.z -= zOff;
						else						
							cent.z += zOff;						
					}
					else
					{
						xOff /= 2f;
						yOff /= 2f;
						zOff /= 2f;
					}
					currentDepth++;
				}				
			}

			// vertices
			float[] vertices = {
					cent.x - xOff, cent.y - yOff, cent.z + zOff, // 1
					cent.x + xOff, cent.y - yOff, cent.z + zOff, // 2
					cent.x - xOff, cent.y + yOff, cent.z + zOff, // 3
					cent.x + xOff, cent.y + yOff, cent.z + zOff, // 4
					
					cent.x - xOff, cent.y - yOff, cent.z - zOff, // 5
					cent.x + xOff, cent.y - yOff, cent.z - zOff, // 6
					cent.x - xOff, cent.y + yOff, cent.z - zOff, // 7
					cent.x + xOff, cent.y + yOff, cent.z - zOff, // 8
			};
			
			verticesBuffer.put(vertices);	
			
			// indices
			int[] indices = {
					i, i + 1, i + 2,
					i + 1, i + 2, i + 3,	// front rect
					
					i + 4, i + 5, i + 6,
					i + 5, i + 6, i + 7,	// back rect
					
					i, i + 1, i + 4,
					i + 1, i + 4, i + 5,	// bottom rect
					
					i + 2, i + 3, i + 6,
					i + 3, i + 6, i + 7,	// top rect
					
					i, i + 4, i + 2,
					i + 4, i + 2, i + 6,	// left rect
					
					i + 1, i + 5, i + 3,
					i + 5, i + 3, i + 7,	// right rect
			};
			
			indicesBuffer.put(indices);			
			
			float R = 0f;
			float G = 1f;
			float B = 1f;
			
			if(showDistributionColors)
			{
				// colors
				float PointRatio = (float)oct.inside_data.length / (float)maxNum;					
				R = 1f - PointRatio;
				G = PointRatio;
				B = 0f;
			}
			
			float[] colors = {
					R, G, B,
					R, G, B,
					R, G, B,
					R, G, B,
					
					R, G, B,
					R, G, B,
					R, G, B,
					R, G, B,
			};			
			colorBuffer.put(colors);	
			
		i +=8;	
		}

		verticesBuffer.flip();
		indicesBuffer.flip();
		colorBuffer.flip();

		BoundingBoxesVertexArrayObjectId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(BoundingBoxesVertexArrayObjectId);
		
        
        // VERTICES        
		// Create a new VBO for the indices and select it (bind)
		int VertexBufferObjectId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VertexBufferObjectId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);		
		// Put the VBO in the attributes list at index 1
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, true, 0, 0);		
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	    GL20.glEnableVertexAttribArray(0);
		
		
		// COLORS
		int ColorVertexBufferObjectId = GL15.glGenBuffers();		
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ColorVertexBufferObjectId); // Bind our second Vertex Buffer Object  	      
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW); // Set the size and data of our VBO and set it to STATIC_DRAW  	      
	    GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0); // Set up our vertex attributes pointer  	      
	    GL20.glEnableVertexAttribArray(1); // Enable the second vertex attribute array  
		
		
		
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
        
        
        // INDICES        
        // Create a new VBO for the indices and select it (bind)
        int indicesBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
        BoundingBoxesVBO = new VBO(VertexBufferObjectId, indicesBufferId, verticesBuffer.capacity(), indicesBuffer.capacity());
        VBOsList.add(BoundingBoxesVBO);

	}
	
	private void CreateRectSplats(ArrayList<Octree> leafs, int maxDepth, float scale)
	{
		int splatVertices = 4;
		float xOffSet = 0.05f;
		float yOffSet = 0.05f;
		float zOffSet = 0.05f;

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(leafs.size() * 3 * splatVertices);
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(leafs.size() * (splatVertices + 2));
		
		int i = 0;
		for(Octree octree : leafs)
		{
			Vector3f vec = octree.getRepresentative();
			
			xOffSet = octree.offsetX / splatScale;
			yOffSet = octree.offsetY / splatScale;
			zOffSet = octree.offsetZ / splatScale; // In my opinion they are too big otherwise
			
			int depth = octree.current_depth;
			
			while(depth != maxDepth) // splat size depends on xyzOffSet, but wrong level means wrong splat size
			{
				xOffSet /= 2f;
				yOffSet /= 2f;
				zOffSet /= 2f;
				depth++;
			}		
			
			float[] rect = { 
					vec.x - xOffSet, vec.y - yOffSet, vec.z,
					vec.x + xOffSet, vec.y - yOffSet, vec.z,
					vec.x - xOffSet, vec.y + yOffSet, vec.z,
					vec.x + xOffSet, vec.y + yOffSet, vec.z,
			};		
			verticesBuffer.put(rect);
			
			int[] indices = {
					i, i + 1, i + 2,
					i + 1, i + 2, i + 3
			};						
			indicesBuffer.put(indices);		
			
			i+=4;
		}				
		
		verticesBuffer.flip();
		indicesBuffer.flip();
		
		if(SplatVertexArrayObjectId == -1)
			SplatVertexArrayObjectId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(SplatVertexArrayObjectId);
		
		// Create a new VBO for the indices and select it (bind)
		int VertexBufferObjectId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VertexBufferObjectId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in the attributes list at index 1
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, true, 0, 0);
		
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
        
        // Create a new VBO for the indices and select it (bind)
        int indicesBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
        SplatsVBO = new VBO(VertexBufferObjectId, indicesBufferId, verticesBuffer.capacity(), indicesBuffer.capacity());
        VBOsList.add(SplatsVBO);	
	}	
	
	private void CreateCircleSplats(ArrayList<Octree> leafs, int maxDepth, float scale)
	{
		int splatVertices = 12;
		
		float degreeStep = 360f / splatVertices;
		float xOffSet = 0.05f;
		float yOffSet = 0.05f;
		float zOffSet = 0.05f;

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(leafs.size() * 3 * (splatVertices + 1));
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(leafs.size() * (splatVertices + 1) * 3);
		
		int i = 0;
		for(Octree octree : leafs)
		{
			Vector3f vec = octree.getRepresentative();
			xOffSet = octree.offsetX / scale;
			yOffSet = octree.offsetY / scale;
			zOffSet = octree.offsetZ / scale;
			
			int depth = octree.current_depth; // root = max_depth, leaf = 0... wtf ... anders rum ist es logischer, aber gut..
			
			while(depth != maxDepth) // splat size depends on xyzOffSet, but wrong level means wrong splat size
			{
				xOffSet /= 2f;
				yOffSet /= 2f;
				zOffSet /= 2f;
				depth++;
			}		
			
			verticesBuffer.put(new float[] { vec.x, vec.y, vec.z });
			
			for(int rad = 0; rad < splatVertices; rad++)
			{
                float xCircleOffSet = xOffSet * (float)Math.cos((rad * degreeStep) * Math.PI / 180d);
                float yCircleOffSet = yOffSet * (float)Math.sin((rad * degreeStep) * Math.PI / 180d);				

				verticesBuffer.put(new float[] { vec.x + xCircleOffSet, vec.y + yCircleOffSet, vec.z });
			}
						
			
			int[] indices = {
					i, i + 1, i + 2,
					i, i + 2, i + 3,
					i, i + 3, i + 4,
					i, i + 4, i + 5,
					i, i + 5, i + 6,
					i, i + 6, i + 7,
					i, i + 7, i + 8,
					i, i + 8, i + 9,
					i, i + 9, i + 10,
					i, i + 10, i + 11,
					i, i + 11, i + 12,
					i, i + 12, i + 1,
			};						
			indicesBuffer.put(indices);		
			
			i+=13;
		}				
		
		verticesBuffer.flip();
		indicesBuffer.flip();
		
		if(SplatVertexArrayObjectId == -1)
			SplatVertexArrayObjectId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(SplatVertexArrayObjectId);
		
		// Create a new VBO for the indices and select it (bind)
		int VertexBufferObjectId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VertexBufferObjectId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in the attributes list at index 1
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, true, 0, 0);
		
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
        
        // Create a new VBO for the indices and select it (bind)
        int indicesBufferId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
        SplatsVBO = new VBO(VertexBufferObjectId, indicesBufferId, verticesBuffer.capacity(), indicesBuffer.capacity());
        VBOsList.add(SplatsVBO);	
	}	
	
	public void loop()
	{
		checkKeyboard();
		checkMouse();
		
		// #################################
		// ProjectionMatrix
		// #################################
		modelMatrix = new Matrix4f();				
		// scale object
		Matrix4f.scale(modelScale, modelMatrix, modelMatrix);		
		// translate object
		Matrix4f.translate(modelPos, modelMatrix, modelMatrix);		
		// rotate object
		Matrix4f.rotate(DegreeToRadian(modelAngle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.rotate(DegreeToRadian(modelAngle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate(DegreeToRadian(modelAngle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		modelMatrix.store(matrixBuffer);
		matrixBuffer.flip();
				
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		
		if(RenderMode.equals(RenderVisualizationMode.PointCloud))
		{		
			// #################################
			// PointCloud
			// #################################
			GL20.glUseProgram(PointCloudShaderProgramId);
			modelMatrixLocation = GL20.glGetUniformLocation(PointCloudShaderProgramId, "modelMatrix");	
			GL20.glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);
					
			// Bind to the VAO that has all the information about the vertices
			GL30.glBindVertexArray(VertexArrayObjectId);
			GL20.glEnableVertexAttribArray(0);
			
			// Bind to the index VBO that has all the information about the order of the vertices
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, DataVBO.VertexBufferID);				
					
			// Draw the VBO
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);	
			GL11.glDrawArrays(GL11.GL_POINTS, 0, DataVBO.NumElements);		
	
			// Put everything back to default (deselect)
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			// Put everything back to default (deselect)
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
					
			GL20.glUseProgram(0);
		}		
		else if(RenderMode.equals(RenderVisualizationMode.Splats))
		{
			// #################################
			// Splats
			// #################################
			GL20.glUseProgram(PointCloudShaderProgramId);
			modelMatrixLocation = GL20.glGetUniformLocation(PointCloudShaderProgramId, "modelMatrix");	
			GL20.glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);
			
			// Bind to the VAO that has all the information about the vertices
			GL30.glBindVertexArray(SplatVertexArrayObjectId);
		    GL20.glEnableVertexAttribArray(0);
	    

		    // Bind to the index VBO that has all the information about the order of the vertices
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, SplatsVBO.IndicesBufferID);
			  

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);	
			// Draw the vertices
			GL11.glDrawElements(GL11.GL_TRIANGLES, SplatsVBO.NumIndices, GL11.GL_UNSIGNED_INT, 0);

		    // Put everything back to default (deselect)
		    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		    GL20.glDisableVertexAttribArray(0);
		    GL30.glBindVertexArray(0);
		    
			GL20.glUseProgram(0);	
		}
		
		
		
		
		
		
		
		
		if(OctreeMode.equals(OctreeVisualizationMode.Detailed) || OctreeMode.equals(OctreeVisualizationMode.OctreeState))
		{
			// #################################
			// BoundingBox
			// #################################
			GL20.glUseProgram(BoundingBoxShaderProgramId);
			modelMatrixLocation = GL20.glGetUniformLocation(BoundingBoxShaderProgramId, "modelMatrix");	
			GL20.glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);
					
			GL11.glDisable(GL11.GL_DEPTH_TEST);
	
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
			//GL11.glDepthMask(false);		
			
			// Bind to the VAO that has all the information about the vertices
			GL30.glBindVertexArray(BoundingBoxesVertexArrayObjectId);
		    GL20.glEnableVertexAttribArray(0);
		    
		    // Bind to the index VBO that has all the information about the order of the vertices
		    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, BoundingBoxesVBO.IndicesBufferID);
		         
		    int mode;
		    if(wireFrameOctree)
		    	mode = GL11.GL_LINE;
		    else
		    	mode = GL11.GL_FILL;
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, mode);	
		    // Draw the vertices
		    GL11.glDrawElements(GL11.GL_TRIANGLES, BoundingBoxesVBO.NumIndices, GL11.GL_UNSIGNED_INT, 0);
		         
		    // Put everything back to default (deselect)
		    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		    GL20.glDisableVertexAttribArray(0);
		    GL30.glBindVertexArray(0);
		    
			GL20.glUseProgram(0);	
		}
	}
		
	public void checkKeyboard()
	{
		// all keys trigger if released
		while(Keyboard.next())
		{		
			if(!Keyboard.getEventKeyState())
				continue;
			
			switch(Keyboard.getEventKey())
			{			
				// Control for Model translation, rotation and scaling
				// Translate
				case Keyboard.KEY_W:
					modelPos.y += positionDelta; // translate up (y-axis) +y
					System.out.println("ModelPosition: (" + modelPos.x + ", " + modelPos.y + ", " + modelPos.z + ")");
					break;				
				case Keyboard.KEY_S:
					modelPos.y -= positionDelta; // translate down (y-axis) -y
					System.out.println("ModelPosition: (" + modelPos.x + ", " + modelPos.y + ", " + modelPos.z + ")");
					break;
				case Keyboard.KEY_A:	
					modelPos.x -= positionDelta; // translate left (x-axis) -x
					System.out.println("ModelPosition: (" + modelPos.x + ", " + modelPos.y + ", " + modelPos.z + ")");
					break;				
				case Keyboard.KEY_D: 
					modelPos.x += positionDelta; // translate right (x-axis) +x
					System.out.println("ModelPosition: (" + modelPos.x + ", " + modelPos.y + ", " + modelPos.z + ")");
					break;
				// Rotate
				case Keyboard.KEY_Q:	
					modelAngle.z += rotationDelta; // rotate the object to the left		
					System.out.println("ModelRotation from 0°: " + modelAngle.z);
					break;				
				case Keyboard.KEY_E: 
					modelAngle.z -= rotationDelta; // rotate the object to the right
					System.out.println("ModelRotation from 0°: " + modelAngle.z);
					break;									
				case Keyboard.KEY_SPACE:
					modelPos = new Vector3f(0, -0.125f, 0);
					modelAngle = new Vector3f(15, 15, 0);
					modelScale = new Vector3f(5, 5, 5);	
					System.out.println("RESET VALUES");
					break;
				case Keyboard.KEY_I:				
					if(!OctreeMode.equals(OctreeVisualizationMode.None))
					{
						if(octreeVisualizationDepth < octreeMaxDepth)
						{
							octreeVisualizationDepth++;						
							CreateOctreeBoundingBoxVBO2(dataOctree.getDepthTree(octreeVisualizationDepth), octreeVisualizationDepth);
							
							if(OctreeMode.equals(OctreeVisualizationMode.Detailed))
								gui.setCurrentMode(1, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
							else
								gui.setCurrentMode(2, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
						}
					}
					break;
				case Keyboard.KEY_K:
					if(!OctreeMode.equals(OctreeVisualizationMode.None))
					{
						if(octreeVisualizationDepth >= 1)
						{
							octreeVisualizationDepth--;						
							CreateOctreeBoundingBoxVBO2(dataOctree.getDepthTree(octreeVisualizationDepth),octreeVisualizationDepth);

							if(OctreeMode.equals(OctreeVisualizationMode.Detailed))
								gui.setCurrentMode(1, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
							else
								gui.setCurrentMode(2, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
						}
					}
					break;
				case Keyboard.KEY_O: // Toggle Octree Visualization on/off
				{
					if(OctreeMode.equals(OctreeVisualizationMode.None))
					{
						OctreeMode = OctreeVisualizationMode.Detailed;
						CreateOctreeBoundingBoxVBO2(dataOctree.getDepthTree(octreeVisualizationDepth),octreeVisualizationDepth);
						gui.setCurrentMode(1, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
					}
					else if(OctreeMode.equals(OctreeVisualizationMode.Detailed))
					{
						OctreeMode = OctreeVisualizationMode.OctreeState;
						CreateOctreeBoundingBoxVBO2(dataOctree.getDepthTree(octreeVisualizationDepth),octreeVisualizationDepth);
						gui.setCurrentMode(2, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
					}
					else if(OctreeMode.equals(OctreeVisualizationMode.OctreeState))
					{
						OctreeMode = OctreeVisualizationMode.None;
						gui.setCurrentMode(0, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
					}					
					break;
				}
				case Keyboard.KEY_L: // Switch Octree Visualization between Fill and Line
				{
					wireFrameOctree = !wireFrameOctree;
					break;
				}
				case Keyboard.KEY_Z: //  Switch between Rect and Circle Splats
				{
					circleSplats = !circleSplats;					
					updateSplatVBOs();
					break;
				}
				case Keyboard.KEY_T: // switch display mode between PointCloud and Splats
				{					
					if(RenderMode.equals(RenderVisualizationMode.None))
					{
						RenderMode = RenderVisualizationMode.PointCloud;
					}
					else if(RenderMode.equals(RenderVisualizationMode.PointCloud))
					{
						RenderMode = RenderVisualizationMode.Splats;
					}
					else if(RenderMode.equals(RenderVisualizationMode.Splats))
					{
						RenderMode = RenderVisualizationMode.None;
					}		
					break;
				}
				case Keyboard.KEY_U: // Increase Splat Scale
				{
					if(splatScale > 0.25f)
						splatScale -= 0.1f;	 // Increase due to divide bye the scale
					updateSplatVBOs();
//					System.out.println("splatScale: " + splatScale);		
					break;
				}
				case Keyboard.KEY_J: // Decrease Splat Scale
				{
					if(splatScale < 10)
						splatScale += 0.1f; // Decrease due to divide bye the scale
				
					updateSplatVBOs();
//					System.out.println("splatScale: " + splatScale);					
					break;
				}
				case Keyboard.KEY_TAB: // Show/Hide GUI
				{
					this.gui.toggleGUI();
				}
				case Keyboard.KEY_P:
				{
					showDistributionColors = !showDistributionColors;
					CreateOctreeBoundingBoxVBO2(dataOctree.getDepthTree(octreeVisualizationDepth), octreeVisualizationDepth);
					if(OctreeMode.equals(OctreeVisualizationMode.Detailed))
						gui.setCurrentMode(1, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
					else if(OctreeMode.equals(OctreeVisualizationMode.OctreeState))
						gui.setCurrentMode(2, octreeVisualizationDepth, octreeMaxDepth, showDistributionColors);
					break;
				}
			}			
		}
	}
	
	private void updateSplatVBOs()
	{		
		if(circleSplats)
		{
			CreateCircleSplats(LeafsMaxDepth, octreeMaxDepth, splatScale);
		}
		else
		{
			CreateRectSplats(LeafsMaxDepth, octreeMaxDepth, splatScale);
		}
	}
	
	public void checkMouse()
	{
		while(Mouse.next())
		{
			if(Mouse.isButtonDown(0))
			{
				modelAngle.x += Mouse.getDY() % 360;
				modelAngle.y += Mouse.getDX() % 360;
			}			
			if(Mouse.getEventDWheel() > 0)
			{
				Vector3f.add(modelScale, scaleGrow, modelScale); // grow the object by +0.1f	
//				System.out.println("Size: (" + modelScale.x + ", " + modelScale.y + ", " + modelScale.z + ")");
			}
			if(Mouse.getEventDWheel() < 0)
			{
				Vector3f.add(modelScale, scaleShrink, modelScale); // shrink the object by -0.1f
				if(modelScale.x < 0)
					modelScale.x = 0;
				if(modelScale.y < 0)
					modelScale.y = 0;
				if(modelScale.z < 0)
					modelScale.z = 0;					
//				System.out.println("Size: (" + modelScale.x + ", " + modelScale.y + ", " + modelScale.z + ")");
			}			
		}	
	}
	
	@SuppressWarnings("deprecation")
	public int initPointCloudShaders()
	{
		int programId, vertexShader, fragmentShader = 0;		
		programId = GL20.glCreateProgram();		
		
		// vertex shader
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);				
		String vertexCode = loadShaderFile("shader/PointCloud.vertex");
		GL20.glShaderSource(vertexShader, vertexCode);
		GL20.glCompileShader(vertexShader);		
		GL20.glAttachShader(programId, vertexShader);
				
	
		// fragment shader
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		String fragmentCode = loadShaderFile("shader/PointCloud.fragment");
		GL20.glShaderSource(fragmentShader, fragmentCode);
		GL20.glCompileShader(fragmentShader);
		GL20.glAttachShader(programId, fragmentShader);
		
		GL20.glLinkProgram(programId);
		
		// validate linking
		if (GL20.glGetProgram(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) 
		{
			throw new RuntimeException("could not link shader. Reason: " + GL20.glGetProgramInfoLog(programId, 1000));
		}
		
		GL20.glValidateProgram(programId);		
		
		if (GL20.glGetProgram(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE)
		{
			throw new RuntimeException("could not validate shader. Reason: " + GL20.glGetProgramInfoLog(programId, 1000));            
		}		
		return programId;
	}
	
	@SuppressWarnings("deprecation")
	public int initBoundingBoxShaders()
	{
		int programId, vertexShader, fragmentShader = 0;		
		programId = GL20.glCreateProgram();		
		
		// vertex shader
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);				
		String vertexCode = loadShaderFile("shader/BoundingBox.vertex");
		GL20.glShaderSource(vertexShader, vertexCode);
		GL20.glCompileShader(vertexShader);		
		GL20.glAttachShader(programId, vertexShader);
				
	
		// fragment shader
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		String fragmentCode = loadShaderFile("shader/BoundingBox.fragment");
		GL20.glShaderSource(fragmentShader, fragmentCode);
		GL20.glCompileShader(fragmentShader);
		GL20.glAttachShader(programId, fragmentShader);
		
		GL20.glLinkProgram(programId);
		
		// validate linking
		if (GL20.glGetProgram(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) 
		{
			throw new RuntimeException("could not link shader. Reason: " + GL20.glGetProgramInfoLog(programId, 1000));
		}
		
		GL20.glValidateProgram(programId);		
		
		if (GL20.glGetProgram(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE)
		{
			throw new RuntimeException("could not validate shader. Reason: " + GL20.glGetProgramInfoLog(programId, 1000));            
		}		
		return programId;
	}
		
	public float DegreeToRadian(float degree)
	{
		return degree * (float)(Math.PI / 180d);
	}
	
	private String loadShaderFile(String filename)
	{
		StringBuilder sb = new StringBuilder();
		try 
		{	
			String line = "";
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while( (line = reader.readLine()) !=null )
			{
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}
		return sb.toString();
	}
	
	private void disposeOpenGL() {
		// Disable the VBO index from the VAO attributes list
				GL20.glDisableVertexAttribArray(0);
				
				for(VBO vbo : VBOsList)
				{
					// Delete the vertex VBO
					GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
					GL15.glDeleteBuffers(vbo.VertexBufferID);

					// Delete the index VBO
					GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
					GL15.glDeleteBuffers(vbo.IndicesBufferID);
				}
				
				// Delete the VAO
				GL30.glBindVertexArray(0);
				GL30.glDeleteVertexArrays(VertexArrayObjectId);

				Display.destroy();
		
	}
}
