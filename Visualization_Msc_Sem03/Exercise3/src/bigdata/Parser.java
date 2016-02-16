package bigdata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 *  
 * @author Justine Smyzek
 *
 */
public class Parser {
	
	private BufferedReader reader;
	private Vector3f coord;
	private ArrayList<PointCoord> points = new ArrayList<PointCoord>();
	private ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
	private Vector3f[] vectors;
	private int count = 0;
	
	
	
	/**
	 * Parse file and create Coordinates
	 * @param file
	 */
	public void parse(FileInputStream file){
		
		try{
			this.reader = new BufferedReader(new InputStreamReader(file));
			String strLine; //variable for next line
			
			//get next line
			while((strLine = reader.readLine())!= null)
			{
				count += 1; // count quantity of points in total (inclusive duplicate)
				String[] splitStr = strLine.split("\\s+"); //split line into 3 coordinates				
				//System.out.println("0: " + splitStr[0] + " 1: " + splitStr[1] + " 2: " + splitStr[2]);
				
				String x = splitStr[0];
				String y = splitStr[1];
				String z = splitStr[2];
				
				//---- Bufferobject für openGL -----
				this.coord = new Vector3f(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
				
				if(Math.abs(coord.x) > 1 || Math.abs(coord.y) > 1 || Math.abs(coord.z) > 1)
				{
					// normalize coords
					float total = 500f;
					coord.x /= total;
					coord.y /= total;
					coord.z /= total;
				}
							
//				if(!coords.contains(coord)){
					this.coords.add(coord);
//					
//					//---- Point Class & ArrayList ----- 		
//					//create new Point
//					PointCoord newPoint = new PointCoord(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
//					//save Point
//					this.points.add(newPoint);
//				}
//				else
//				{
//					//don't add duplicates, but count it number
//					int index = 0;
//					for(int i = 0; i < this.coords.size(); i++){
//						if(coords.get(i).equals(coord)){
//							index = i;
//						}
//					}
//					PointCoord currentPoint = this.points.get(index);
//					currentPoint.incAmount();
//					//System.out.println("increased");
//				}
				//System.out.println(coords.size());
				 
			}
		 
		}catch(Exception e){
		   System.out.println(e);
		}
		
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<PointCoord> getList(){
		return points;
	}
	
	/**
	 * generate Vector3f[] for openGL
	 */
	public Vector3f[] getVector(){
		this.vectors = new Vector3f[this.coords.size()];
		for(int i = 0; i < this.vectors.length; i++){
			this.vectors[i] = this.coords.get(i);
		}
		//System.out.println(this.vectors.length);
		return vectors;
	}
	
}
