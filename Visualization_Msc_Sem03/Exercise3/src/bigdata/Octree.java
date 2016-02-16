package bigdata;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * Octree  
 * @author Justine Smyzek
 */
public class Octree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6932462403515165834L;
	public Vector3f[] inside_data = null; 	//points inside this Octree part
	public int current_depth;   	// max depth left (also shows currently depth max_overall - current)
	public int max_depth;
	public Vector3f center;			//middle point
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	
	
	
	//public Octree[] children_nodes;
	public ArrayList<Octree> children_nodes;
	
	//representive if leaf - optional
	public Vector3f representive;
	
	public boolean leaf = false;
	//private ArrayList<PointCoord> points; //leafs have datatype with frequency of points
	
	
	//normal
	public Vector3f normal;
	
	
	
	/**
	 * 
	 */
	public Octree()
	{
		this(null, -1f, -1f, -1f, null, -1, -1);
	}
	
	/**
	 * Constructor for Octree
	 */
	public Octree(Vector3f[] data, float offsetX, float offsetY, float offsetZ, Vector3f center, int max_depth, int current_depth){
		
		this.inside_data = data; //data that was given from previous depth 
		this.center = center;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.max_depth = max_depth;
		this.current_depth = current_depth;
		
		createOctrees();
		
	}
	
	/**
	 * create Octrees
	 */
	public void createOctrees(){
		
		
		if (this.current_depth < this.max_depth && this.inside_data.length > 1){  //stop if depth is reached, then this is last leaf
		
			//System.out.println("create dept: " + this.current_depth);
			this.children_nodes = new ArrayList<Octree>();
			//new list of data (reduction into new/smaller nodes)
			ArrayList<Vector3f> child1 = new ArrayList<Vector3f>(); //[0][0][0] l,u,v
			ArrayList<Vector3f> child2 = new ArrayList<Vector3f>(); //[0][0][1] l,u,h
			ArrayList<Vector3f> child3 = new ArrayList<Vector3f>(); //[0][1][0] l,o,v
			ArrayList<Vector3f> child4 = new ArrayList<Vector3f>(); //[0][1][1] l,o,h
			ArrayList<Vector3f> child5 = new ArrayList<Vector3f>(); //[1][0][0] r,u,v
			ArrayList<Vector3f> child6 = new ArrayList<Vector3f>(); //[1][0][1] r,u,h
			ArrayList<Vector3f> child7 = new ArrayList<Vector3f>(); //[1][1][0] r,o,v
			ArrayList<Vector3f> child8 = new ArrayList<Vector3f>(); //[1][1][1] r,o,h
			
			
			//data sorting into new trees & creating new lvl if not empty
			for(int i = 0; i < this.inside_data.length; i++){
				//System.out.println("schleife " + this.current_depth);
				if(this.inside_data[i].x <= this.center.x && this.inside_data[i].y <= this.center.y && this.inside_data[i].z >= this.center.z){
					
					child1.add(this.inside_data[i]);
					
				}else
				if(this.inside_data[i].x <= this.center.x && this.inside_data[i].y <= this.center.y && this.inside_data[i].z < this.center.z){
					
					child2.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x <= this.center.x && this.inside_data[i].y > this.center.y && this.inside_data[i].z >= this.center.z){
					
					child3.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x <= this.center.x && this.inside_data[i].y > this.center.y && this.inside_data[i].z < this.center.z){
					
					child4.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x > this.center.x && this.inside_data[i].y <= this.center.y && this.inside_data[i].z >= this.center.z){
					
					child5.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x > this.center.x && this.inside_data[i].y <= this.center.y && this.inside_data[i].z < this.center.z){
					
					child6.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x > this.center.x && this.inside_data[i].y > this.center.y && this.inside_data[i].z >= this.center.z){
					
					child7.add(this.inside_data[i]);
					
				} else
				if(this.inside_data[i].x > this.center.x && this.inside_data[i].y > this.center.y && this.inside_data[i].z < this.center.z)	
				{
					child8.add(this.inside_data[i]);
					
				}
					
			}
			
			
			//init new nodes with new center & offsets 
			Octree ocChild1 = new Octree(this.getArray(child1), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x - (offsetX/2), center.y - (offsetY/2), center.z + (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild1.createOctrees();
			this.children_nodes.add(ocChild1);
			Octree ocChild2 = new Octree(this.getArray(child2), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x - (offsetX/2), center.y - (offsetY/2), center.z - (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild2.createOctrees();
			this.children_nodes.add(ocChild2);
			Octree ocChild3 = new Octree(this.getArray(child3), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x - (offsetX/2), center.y + (offsetY/2), center.z + (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild3.createOctrees();
			this.children_nodes.add(ocChild3);
			Octree ocChild4 = new Octree(this.getArray(child4), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x - (offsetX/2), center.y + (offsetY/2), center.z - (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild4.createOctrees();
			this.children_nodes.add(ocChild4);
			Octree ocChild5 = new Octree(this.getArray(child5), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x + (offsetX/2), center.y - (offsetY/2), center.z + (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild5.createOctrees();
			this.children_nodes.add(ocChild5);
			Octree ocChild6 = new Octree(this.getArray(child6), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x + (offsetX/2), center.y - (offsetY/2), center.z - (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild6.createOctrees();
			this.children_nodes.add(ocChild6);
			Octree ocChild7 = new Octree(this.getArray(child7), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x + (offsetX/2), center.y + (offsetY/2), center.z + (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild7.createOctrees();
			this.children_nodes.add(ocChild7);
			Octree ocChild8 = new Octree(this.getArray(child8), offsetX/2, offsetY/2, offsetZ/2, new Vector3f(center.x + (offsetX/2), center.y + (offsetY/2), center.z - (offsetZ/2)), this.max_depth, this.current_depth+1);
			ocChild8.createOctrees();
			this.children_nodes.add(ocChild8);
				
			
			//---------- optional representative 
			if(this.getData().length != 0){	
				float dis_min = 100f;
				int index = 0;
				
				for(int i = 0; i < this.inside_data.length; i++){
					float distance = (float) Math.sqrt(Math.pow(this.center.x - this.inside_data[i].x, 2) + Math.pow(this.center.y - this.inside_data[i].y, 2) + Math.pow(this.center.z - this.inside_data[i].z, 2));
					if(distance < dis_min){
						index = i;
					}
				} 
				this.representive = this.inside_data[index];
				this.creatNormalVector();
			}
			
			
			
		
		}else{
			this.children_nodes = null;
			this.leaf = true;
			
			//---------- optional representative 
			if(this.getData().length != 0){	
				float dis_min = 100f;
				int index = 0;
				
				for(int i = 0; i < this.inside_data.length; i++){
					float distance = (float) Math.sqrt(Math.pow(this.center.x - this.inside_data[i].x, 2) + Math.pow(this.center.y - this.inside_data[i].y, 2) + Math.pow(this.center.z - this.inside_data[i].z, 2));
					if(distance < dis_min){
						index = i;
					}
				} 
				this.representive = this.inside_data[index];
				this.creatNormalVector();
			}
			
			//--------------------------- optional - if leaf
			//frequency of occurrence & datatype   
			//Parser better because doubles can make additional leafs
/*			ArrayList<Vector3f> noDouble = new ArrayList<Vector3f>();
			for(int i = 0; i < this.inside_data.size(); i++){
				if(!noDouble.contains(this.inside_data.get(i))){
					this.points.add(new PointCoord(this.inside_data.get(i)));
				}
				else{
					int index = 0;
					for(int j = 0; j < noDouble.size(); j++){
						if(noDouble.get(i).equals(this.inside_data.get(i))){
							index = i;
						}
					}
					PointCoord currentPoint = this.points.get(index);
					currentPoint.incAmount();
				}
				
			}
			
			//Do we need vector List with doubles? no
			this.inside_data = noDouble;*/		
		}
		
		
		
	}
	
	
	/**
	 * Creates the Normal Vector per Octree box
	 * Randomized with max 9 samples (or less if data < 9 > 3)
	 */
	public void creatNormalVector(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		//----------- generate random list for neighbors -----------
		if(this.inside_data.length > 3){ //need minimum 3 for a triangle
			int samples = 0;
			if(this.inside_data.length < 10){
				samples = this.inside_data.length -1; //excluding representive itself	
			}else{
				samples = 9;
			}
				for(int n = 0; n < samples; n++){			
					if(this.representive != this.inside_data[n]){ 
						indexes.add(n);
					}
				}		
		
			// ----------- calculate normal from triangles------------
			Vector3f u = new Vector3f();
			Vector3f v = new Vector3f();
			ArrayList<Vector3f> triangleNormal = new ArrayList<Vector3f>();
			
			for(int j = 0; j < indexes.size()-1; j++){
				u = new Vector3f (this.inside_data[indexes.get(j)].x - this.representive.x,
						this.inside_data[indexes.get(j)].y - this.representive.x,
						this.inside_data[indexes.get(j)].z - this.representive.x);
				
				v = new Vector3f (this.inside_data[indexes.get(j+1)].x - this.representive.x,
						this.inside_data[indexes.get(j+1)].y - this.representive.x,
						this.inside_data[indexes.get(j+1)].z - this.representive.x);
				
				triangleNormal.add(new Vector3f ((u.y * v.z) - (u.z * v.y),
						(u.z * v.x) - (u.x * v.z),
						(u.x * v.y) - (u.y * v.x) ));		
			}
			float x = 0;
			float y = 0;
			float z = 0;
			
			for(int k = 0; k < triangleNormal.size(); k++){
				x += triangleNormal.get(k).x;
				y += triangleNormal.get(k).y;
				z += triangleNormal.get(k).z;
			}
			
			this.normal = new Vector3f();
			this.normal.x = x / (float)triangleNormal.size();
			this.normal.y = y / (float)triangleNormal.size();
			this.normal.z = z / (float)triangleNormal.size();
		
		}else{
			this.normal = null;
		}
	}
	
	
	/**
	 * transform into Vector3f[]
	 * @return
	 */
	public Vector3f[] getArray(ArrayList<Vector3f> vec){
		Vector3f[] intoArray = new Vector3f[vec.size()];
		
		for(int i = 0; i < intoArray.length; i++){
			intoArray[i] = vec.get(i);
		}
		
		return intoArray;
	}
	
	
	/**
	 * Get Data
	 * @return
	 */
	public  Vector3f[] getData(){
		return this.inside_data;
	}
	
	/**
	 * Get Representative
	 * @return
	 */
	public Vector3f getRepresentative(){
		return this.representive;
	}
	
	/**
	 * get children
	 * @return
	 */
	public ArrayList<Octree> getChildren(){
		return this.children_nodes;
	}
		
	/**
	 * Get all leafs in tree (without empty leafs)
	 * @return
	 */
	public ArrayList<Octree> getLeafs(){
		ArrayList<Octree> leafs = new ArrayList<Octree>();
		
		if(this.leaf == false){		
			for(int i = 0; i < this.children_nodes.size(); i++){
				leafs.addAll(this.children_nodes.get(i).getLeafs());
			}
		}
		else if(this.getData().length != 0){
			leafs.add(this);
		}
		return leafs;
	}
	
	/**
	 * Get all nodes of level in tree (without empty)
	 * @return
	 */
	public ArrayList<Octree> getDepthTree(int depth){
		ArrayList<Octree> depthTree = new ArrayList<Octree>();
		
		if(this.current_depth != depth && this.children_nodes != null){
			for(int i = 0; i < this.children_nodes.size(); i++){
				depthTree.addAll(this.children_nodes.get(i).getDepthTree(depth));
			}			
		}else if(this.getData().length != 0){
			depthTree.add(this);
		}
		return depthTree;
	}
	
	/**
	 * Return Leafs related to the current chosen level depth
	 * @param depth
	 * @return
	 */
	public ArrayList<Octree> getDepthRelatedLeafs(int depth){
		ArrayList<Octree> depthRelatedLeafs = new ArrayList<Octree>();
		ArrayList<Octree> leafs = new ArrayList<Octree>();
		
		depthRelatedLeafs = this.getDepthTree(depth); //add nodes with depth level
		
		leafs = this.getLeafs(); //add higher leafs aswell
		for(int j = 0; j > leafs.size(); j++){
			if(leafs.get(j).current_depth < depth){
				depthRelatedLeafs.add(leafs.get(j));
			}
		}

		return depthRelatedLeafs;
		
	}
	
	
	/**
	 * Get Data from Leafs
	 * @return
	 */
	public Vector3f[] getLeafsData(){
		ArrayList<Octree> leafs = getLeafs();
		ArrayList<Vector3f> dataTemp = new ArrayList<Vector3f>();
		Vector3f[] leafData;
		
		for(int i = 0; i < leafs.size(); i++){
			for(int j = 0; j < leafs.get(i).inside_data.length; j++ ){
				dataTemp.add(leafs.get(i).inside_data[j]);
			}		
		}
		leafData = this.getArray(dataTemp);
		return leafData;
	}
	
	
	/**
	 * Get Representatives from Leafs
	 * @return
	 */
	public Vector3f[] getLeafsRepresentatives(){
		ArrayList<Octree> leafs = getLeafs();
		Vector3f[] leafData = new Vector3f[leafs.size()];
		
		for(int i = 0; i < leafs.size(); i++){
			leafData[i] = leafs.get(i).getRepresentative();
		}
		
		return leafData;
	}
	
	
	
	public static void SaveOctreeToBinaryFile(String fileName, Octree octree)
	{
		try {
//        	long start = System.currentTimeMillis();   
	    	FileOutputStream fileStream = new FileOutputStream(fileName);
	    	ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

	    	objectStream.writeObject(octree);
	    	objectStream.flush();
	    	objectStream.close();			
	    	
//    		float time = (float) ((System.currentTimeMillis() - start) / 1000f);  
//            System.out.println("BinaryOctreeFile saved in: " + time + " seconds");  
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	

	public static Octree LoadOctreeFromBinaryFile(String fileName)
	{
		Octree data = null;
        try {       	
//        	long start = System.currentTimeMillis();        	
        	
        	InputStream is = new FileInputStream(fileName);        
        	BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            ObjectInputStream fs = new ObjectInputStream(new BufferedInputStream(bufferedInputStream));
            data = (Octree)fs.readObject();
            fs.close();                  
//    		float time = (float) ((System.currentTimeMillis() - start) / 1000f);    		
//            System.out.println("BinaryOctreeFile loaded in: " + time + " seconds");  
            
        }
        catch (Exception e) {
			System.out.println(e.getMessage());
        }
        return data;
	}		
}
