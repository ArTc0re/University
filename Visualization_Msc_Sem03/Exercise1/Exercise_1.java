package plot;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Markus Strobel
 * @email mksstrobel@googlemail.com
 * @matrikelnr 3545258
 *
 * this class is based on the introduction from the lwjgl.org wiki documentation and tutorials
 * -> http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_DrawElements
 * 
 * projection-, view- and model-Matrix usings are based on the code from
 * -> https://solarianprogrammer.com/2013/05/22/opengl-101-matrices-projection-view-model/
 * -> http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_Projection,_View_and_Model_matrices
 * 
 * and further extended for exercise' tasks
 *
 */
public class Exercise_1 {
	
	int xResolution = 1024;
	int yResolution = 768;
	
	// indices count
	int rectVertexIndexCount = 0;	
	int guiVertexIndexCount = 0;
	
	// vertex array objects
	int vertexArrayObjectID = 0;
	
	// vertex buffer objects
	int rectVertexBufferObject = 0;	
	int rectOrderBufferObjectID = 1;
	
	int xElements = 100;
	int yElements = 100;
	
	// shader stuff
	int shaderProgramId = 0;
	
	// model matrices stuff
	Vector3f modelPos;
	Vector3f modelAngle;
	Vector3f modelScale;
	
	Matrix4f modelMatrix;	

	int modelMatrixLocation = 0;
	int rangeLocation = 0;
	
	Vector3f scaleShrink, scaleGrow;
	float rotationDelta;
	float positionDelta;
	
	float range = 70;
	
	FloatBuffer matrixBuffer;
	
	public static void main(String[] args) {
		
		Exercise_1 test = new Exercise_1();
		test.start();
	}
	
	public void initAll()
	{
		this.initOpenGL("Exercise 1 - Markus Strobel - Matr.Nr. 3545258", xResolution, yResolution);
		
		shaderProgramId = this.initShaders();
		
		modelPos = new Vector3f(0, 0, 0);
		modelAngle = new Vector3f(45, 10, 0);
		modelScale = new Vector3f(1, 1, 1);	
		
		scaleGrow = new Vector3f(0.1f, 0.1f, 0.1f);
		scaleShrink = new Vector3f(-0.1f, -0.1f, -0.1f);		
		positionDelta = 0.1f;
		rotationDelta = 10f;		
	
		// init Matrix buffer for Model Matrix
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	public void start()
	{		
		initAll();
			
		this.setupRect(xElements, yElements);
		
		while (!Display.isCloseRequested()) 
		{
			this.loop();				
			Display.update();
			Display.sync(60);
		}		

		this.disposeOpenGL();
	}
		
	public void loop()
	{
		this.checkInput();
		this.checkMouse();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);		
		GL20.glUseProgram(shaderProgramId);
		
		modelMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, "modelMatrix");		
		rangeLocation = GL20.glGetUniformLocation(shaderProgramId, "range");
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glEnable(GL11.GL_CULL_FACE_MODE);
		//GL11.glCullFace(GL11.GL_BACK);
		
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vertexArrayObjectID);
		GL20.glEnableVertexAttribArray(0);		 
		
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rectOrderBufferObjectID);				
		
		// line coverage problem fix
		GL11.glColorMask(false, false, false, false);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glPolygonOffset(1f, 0.1f); // so wirklich seh ich da keinen unterschied egal welche Werte ich eingebe :D
		GL11.glDrawElements(GL11.GL_TRIANGLES, rectVertexIndexCount, GL11.GL_UNSIGNED_INT, 0);	
		
		
		// Draw the Rect	
		GL11.glColorMask(true, true, true, true);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);	
		GL11.glDrawElements(GL11.GL_TRIANGLES, rectVertexIndexCount, GL11.GL_UNSIGNED_INT, 0);		
				
		
		
		
		
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);		


		// Control
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
		GL20.glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);
		
		int rangeLocation = GL20.glGetUniformLocation(shaderProgramId, "range");
		
		FloatBuffer rangeBuffer = BufferUtils.createFloatBuffer(1);
		rangeBuffer.put(range);
		rangeBuffer.flip();
		GL20.glUniform1(rangeLocation, rangeBuffer);
		
		GL20.glUseProgram(0);
	}

	public float DegreeToRadian(float degree)
	{
		return degree * (float)(Math.PI / 180d);
	}
	
	public void setupRect(int xSubRects, int ySubRects)
	{
		int vertexCount = (xSubRects + 1) * (ySubRects + 1);	
		Vector3f[][] verticesMap = new Vector3f[ySubRects + 1][xSubRects + 1];	
		
		// sub elements width/height
		float deltaX = 1f / xSubRects;
		float deltaY = 1f / ySubRects;
		
		// vertices Positions
		float xBasePosition = -0.5f;
		float xCurrentPosition = xBasePosition;
		float yCurrentPosition = -0.5f;
						
		// vertices
		for(int y = 0; y <= ySubRects; y++)
		{
			for(int x = 0; x <= xSubRects; x++)
			{				
				verticesMap[y][x] = new Vector3f(xCurrentPosition, yCurrentPosition, 0f);
				xCurrentPosition += deltaX;
			}
			yCurrentPosition += deltaY;
			xCurrentPosition = xBasePosition;						
		}	
		
		// indices
		rectVertexIndexCount = xSubRects * ySubRects * 2 * 3;
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(rectVertexIndexCount);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
		
		int idx = 0;
		for(int y = 0; y <= ySubRects; y++)
		{
			
			for(int x = 0; x <= xSubRects; x++)
			{
				idx = x + y * (xSubRects + 1);

				if(x < xSubRects && y < ySubRects)
				{					
					indicesBuffer.put(new int[] { idx, idx + 1, idx + xSubRects + 1 });
					indicesBuffer.put(new int[] { idx + 1, idx + xSubRects + 1, idx + xSubRects + 2 });
				}				
			}
		}					
		
		for(int y = 0; y <= ySubRects; y++)
		{
			for(int x = 0; x <= xSubRects; x++)
			{
				verticesBuffer.put(new float[] { verticesMap[y][x].x, verticesMap[y][x].y, verticesMap[y][x].z }); // row wise -> [y][x] 
			}
		}	
		
		verticesBuffer.flip();		
		indicesBuffer.flip();		 

		vertexArrayObjectID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayObjectID);
		 
		// create vertex buffer object for vertices
		rectVertexBufferObject = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, rectVertexBufferObject);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);		 
		
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);		 
		
		// create vertex buffer object for triangle strip indices order
		rectOrderBufferObjectID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rectOrderBufferObjectID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
			
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);		
	}
		
	public void initOpenGL(String windowTitle, int displayWidth, int displayHeight)
	{
		try 
		{
//			PixelFormat pixelFormat = new PixelFormat();
//			ContextAttribs contextAtrributes = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.setTitle(windowTitle);
			//Display.create(pixelFormat, contextAtrributes);
			Display.create();
			
			
			GL11.glViewport(0, 0, displayWidth, displayHeight);
		}		
		catch (LWJGLException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void disposeOpenGL()
	{
		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		
		// Delete the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(rectVertexBufferObject);
		
		GL15.glDeleteBuffers(rectOrderBufferObjectID);
		 
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vertexArrayObjectID);		
		
		Display.destroy();
	}	
	
	public void checkInput()
	{
		// all keys trigger if released
		while(Keyboard.next())
		{		
			if(!Keyboard.getEventKeyState())
				continue;
			
			switch(Keyboard.getEventKey())
			{			
				//Control for Rect Tesseleration
				case Keyboard.KEY_I: // + XSubElements
					xElements += 5;
					System.out.println("X: " + xElements + " Y: " + yElements);
					setupRect(xElements, yElements);
					break;				
				case Keyboard.KEY_K: // - XSubElements
					if(xElements >= 6)
						xElements -= 5;
					System.out.println("X: " + xElements + " Y: " + yElements);
					setupRect(xElements, yElements);
					break;					
				case Keyboard.KEY_O: // + YSubElements
					yElements += 5;
					System.out.println("X: " + xElements + " Y: " + yElements);						
					setupRect(xElements, yElements);

					break;					
				case Keyboard.KEY_L: // - YSubElements
					if(yElements >= 6)
						yElements -= 5;
					System.out.println("X: " + xElements + " Y: " + yElements);
					setupRect(xElements, yElements);
					break;
					
					
										
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
				// Scale
				case Keyboard.KEY_R:	
					Vector3f.add(modelScale, scaleGrow, modelScale); // grow the object by +0.1f	
					System.out.println("Size: (" + modelScale.x + ", " + modelScale.y + ", " + modelScale.z + ")");
					break;				
				case Keyboard.KEY_F: 
					Vector3f.add(modelScale, scaleShrink, modelScale); // shrink the object by -0.1f
					
					if(modelScale.x < 0)
						modelScale.x = 0;
					if(modelScale.y < 0)
						modelScale.y = 0;
					if(modelScale.z < 0)
						modelScale.z = 0;					
					System.out.println("Size: (" + modelScale.x + ", " + modelScale.y + ", " + modelScale.z + ")");
					break;				
				case Keyboard.KEY_SPACE:
					modelPos = new Vector3f(0, 0, 0);
					modelAngle = new Vector3f(45, 10, 0);
					modelScale = new Vector3f(1, 1, 1);
					System.out.println("RESET VALUES");
					break;
			}			
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
				range += 2.5f;
				System.out.println("Range: " + range);
			}
			if(Mouse.getEventDWheel() < 0)
			{
				if(range > 10)
					range -= 2.5f;
				System.out.println("Range: " + range);
			}			
		}	
	}
		
	public int initShaders()
	{
		int programId, vertexShader, fragmentShader = 0;		
		programId = GL20.glCreateProgram();		
		
		// vertex shader
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);				
		String vertexCode = loadShaderFile("shader/function2d.vertex");
		GL20.glShaderSource(vertexShader, vertexCode);
		GL20.glCompileShader(vertexShader);		
		GL20.glAttachShader(programId, vertexShader);
				
	
		// fragment shader
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		String fragmentCode = loadShaderFile("shader/function2d.fragment");
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
	
}


