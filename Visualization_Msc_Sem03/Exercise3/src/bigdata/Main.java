package bigdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author MarkusStrobel
 * @email mksstrobel@googlemail.com
 */
public class Main {

	public static void main(String[] args) {

		/**
		 * How To Start für Daniel :)
		 * 
		 * 1. Im data-Ordner befinden sich die drei object0.txt - object3.txt Dateien (falls nicht bitte dort hinzufügen)
		 * 
		 * 2. Die gewünschte Tiefe unten bitte bei "depth" eintragen
		 * 
		 * 3. Die gewünschte Datei bitte bei "objectFileName" eintragen
		 * 	  Beispiel: 	int depth = 5;
		 *					String objectFileName = "object0";
		 *
		 * 4. Der Octree kann entweder geparsed und erstellt werden oder einfach aus einer bestehenden BinaryFile Representation des Octree geladen werden.
		 *    Dafür ist es natürlich zunächst erforderlich diese .octree File(s) erst einmal zu erstellen.
		 *    
		 *    
		 * 5. Falls die Datei nicht existiert, wird Sie entsprechend erst erstellt.
		 * 	  Sollte die Datei existieren, wird diese einfach geladen.
		 * 
		 */

		int depth = 6;								// the maximum depth for the Octree
		String objectFileName = "object0";			// the data filename (without .txt)

		
		
		
		
		
		/**
		 * To Create a BinaryFile representation of an Octree object use the lines below!	
		 * 
		 * This will load raw data, create Octree and save it to a BinaryFile representation
		 * -> this will speedup future loadings time of the same octree specification by about 400% (in most test cases on development computer)
		 */				
		
		if(!(new File("data/" + objectFileName + "_depth" + depth + ".octree").exists()))
			CreateOctreeBinaryFile("data/" + objectFileName + ".txt", "data/" + objectFileName + "_depth" + depth + ".octree", depth);



		
		/**
		 * Loading the Octree
		 * if LoadOctreeFromBinaryFile is true, it will try to load the Octree from a Binary File
		 * else it will parse the objectX.txt and generate and Octree of specified depth
		 */
		boolean LoadOctreeFromBinaryFile = true; // change this to switch between loading the octree from binaryFile representation of an Octree oject to loading the raw data and generating an new octree 
		
		Octree octree = LoadOctree(objectFileName, depth, LoadOctreeFromBinaryFile);


		

		
		/**
		 * Starting OpenGL etc..
		 */
		DisplayClass displayNew = new DisplayClass();
		displayNew.start(octree);








	}

	public static Octree LoadOctree(String objectFileName, int depth, boolean LoadFromBinaryFile)
	{
		// if we want to load the octree from binary file
		if(LoadFromBinaryFile)
		{
//			System.out.println("Try to load Octree from BinaryFile..."); 
			String fileName = "data/" + objectFileName + "_depth" + depth + ".octree";
			File file = new File(fileName);

			if(file.exists())
			{
//				System.out.println("BinaryFile found..."); 
				return Octree.LoadOctreeFromBinaryFile(fileName);
			}	
//			System.out.println("BinaryFile not found..."); 
		}


		// we need to parse data and create octree otherwise..

//		System.out.println("Parsing Data..."); 
		Vector3f[] data = ParseData("data/" + objectFileName + ".txt");

		float xMin = 1f;
		float yMin = 1f;
		float zMin = 1f;
		float xMax = -1f; 
		float yMax = -1f;
		float zMax = -1f;

		for (Vector3f vec : data)
		{
			if(vec.x > xMax)
				xMax = vec.x;
			if(vec.x < xMin)
				xMin = vec.x;			
			if(vec.y > yMax)
				yMax = vec.y;
			if(vec.y < yMin)
				yMin = vec.y;			
			if(vec.z > zMax)
				zMax = vec.z;
			if(vec.z < zMin)
				zMin = vec.z;			
		}

		float xDistance = GetDistance(xMin, xMax);
		float yDistance = GetDistance(yMin, yMax);
		float zDistance = GetDistance(zMin, zMax);		
		float xOffSet = (xDistance / 2f);
		float yOffSet = (yDistance / 2f);
		float zOffSet = (zDistance / 2f);		
		float xCenter = xMax - xOffSet;
		float yCenter = yMax - yOffSet;
		float zCenter = zMax - zOffSet;

//		System.out.println("Create Octree..."); 
				
		return new Octree(data, xOffSet, yOffSet, zOffSet, new Vector3f(xCenter, yCenter, zCenter), depth, 0);
	}

	public static Vector3f[] ParseData(String sourceFileName)
	{
		// --------------- Initial parser & parse ----------------------
		Parser parser = new Parser();		
//		long start = 0;
		try 
		{			
//			System.out.println("Start Parsing");
//			start = System.currentTimeMillis();
			FileInputStream	file = new FileInputStream(sourceFileName); //change input file here
			parser.parse(file);			
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		Vector3f[] data = parser.getVector();
//		float time = (float) ((System.currentTimeMillis() - start) / 1000f);
//		System.out.println("Finished Parsing in " + time + " seconds");
		return data;
	}

	public static Octree CreateOctreeBinaryFile(String sourceFileName, String targetFileName, int depth)
	{		
		// --------------- Initial parser & parse ----------------------
		Parser parser = new Parser();		
//		long start = 0;
		try 
		{			
//			System.out.println("Start Parsing");
//			start = System.currentTimeMillis();
			FileInputStream	file = new FileInputStream(sourceFileName); //change input file here
			parser.parse(file);			
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		Vector3f[] data = parser.getVector();
//		System.out.println("Finished Parsing");
		float xMin = 1f;
		float yMin = 1f;
		float zMin = 1f;
		float xMax = -1f; 
		float yMax = -1f;
		float zMax = -1f;

		for (Vector3f vec : data)
		{
			if(vec.x > xMax)
				xMax = vec.x;
			if(vec.x < xMin)
				xMin = vec.x;			
			if(vec.y > yMax)
				yMax = vec.y;
			if(vec.y < yMin)
				yMin = vec.y;			
			if(vec.z > zMax)
				zMax = vec.z;
			if(vec.z < zMin)
				zMin = vec.z;			
		}

		float xDistance = GetDistance(xMin, xMax);
		float yDistance = GetDistance(yMin, yMax);
		float zDistance = GetDistance(zMin, zMax);		
		float xOffSet = (xDistance / 2f);
		float yOffSet = (yDistance / 2f);
		float zOffSet = (zDistance / 2f);		
		float xCenter = xMax - xOffSet;
		float yCenter = yMax - yOffSet;
		float zCenter = zMax - zOffSet;

//		System.out.println("Create Octree"); 

		Octree octree = new Octree(data, xOffSet, yOffSet, zOffSet, new Vector3f(xCenter, yCenter, zCenter), depth, 0);

	
//		System.out.println("Start saving file"); 		
		Octree.SaveOctreeToBinaryFile(targetFileName, octree);
//		System.out.println("Saved file to \"" + targetFileName + "\""); 

//		float time = (float) ((System.currentTimeMillis() - start) / 1000f);
//		System.out.println("Parsing, Creating and Saving finished in: " + time + " seconds"); 	

		return octree;
	}

	public static float GetDistance(float min, float max)
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

}


