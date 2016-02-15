package bigdata;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Markus Strobel
 * @email mksstrobel@googlemail.com
 *
 * Octree implementation (not finished, skipped, due to distribution of tasks)
 * 
 */
public class Octree {
	
	int _depth = 1;
	float min = -1f;
	float max = 1f;
	
	ArrayList<ArrayList<Vector3f>> Octree;
	int[] LeafDensity;
		
	public Octree(int depth)
	{
		if(depth < 1)
			this._depth = 1;
		else if (depth > 8)
			this._depth = 8;
		else
			this._depth = depth;
		
		// #leafs = 8^depth
		// 1 = 8, 2 = 8 * 8, 3 = 8 * 8 * 8, etc
		int leafs = (int)Math.pow(8d, this._depth);

		// init leafs array
		Octree = new ArrayList<ArrayList<Vector3f>>();		
		for(int i = 0; i < leafs; i++)
		{
			Octree.add(new ArrayList<Vector3f>());
		}
		
		// the amount of vertices in each leaf will be stored here (same indices as leafs)
		LeafDensity = new int[Octree.size()];
		
//		for(int i = 0; i < Octree.size(); i++)
//		{
//			if(Octree.get(i) != null)
//			{
//				System.out.println("index: " + i);
//			}
//		}
	}
	
	public int GetLeafIndex(Vector3f vertex)
	{
		float xMin = min;
		float xMax = max;
		float yMin = min;
		float yMax = max;
		float zMin = min;
		float zMax = max;		
		float xPivot = getPivot(xMin, zMax);
		float yPivot = getPivot(yMin, yMax);
		float zPivot = getPivot(zMin, zMax);
		
		int[] binary = new int[_depth * 3];
		
		int binIndex = 0;
		// get leaf index/number
		for(int ind = 0; ind < _depth; ind++)
		{
			if(vertex.x >= xPivot) 					// 1
			{
				binary[binIndex++] = 1;
				xMin = xPivot;
				xPivot = getPivot(xMin, xMax);
				if(vertex.y >= yPivot) 					// 11
				{
					binary[binIndex++] = 1;
					yMin = yPivot;
					yPivot = getPivot(yMin, yMax);
					if(vertex.z >= zPivot)					// 111
					{
						binary[binIndex++] = 1;
						zMin = zPivot;
						zPivot = getPivot(zMin, zMax);
					}
					else									// 110
					{
						binary[binIndex++] = 0;
						zMax = zPivot;
						zPivot = getPivot(zMin, zMax);
					}		
				}
				else									// 10
				{
					binary[binIndex++] = 0;
					yMax = yPivot;
					yPivot = getPivot(yMin, yMax);
					if(vertex.z >= zPivot)					// 101
					{
						binary[binIndex++] = 1;
						zMin = zPivot;
						zPivot = getPivot(zMin, zMax);
					}
					else									// 100
					{
						binary[binIndex++] = 0;
						zMax = zPivot;
						zPivot = getPivot(zMin, zMax);
					}					
				}				
			}
			else									// 0
			{
				binary[binIndex++] = 0;
				xMax = xPivot;
				xPivot = getPivot(xMin, xMax);
				if(vertex.y >= yPivot) 					// 01
				{
					binary[binIndex++] = 1;
					yMin = yPivot;
					yPivot = getPivot(yMin, yMax);
					if(vertex.z >= zPivot)					// 011
					{
						binary[binIndex++] = 1;
					}
					else									// 010
					{
						binary[binIndex++] = 0;
					}		
				}
				else									// 00
				{
					binary[binIndex++] = 0;
					yMax = yPivot;
					yPivot = getPivot(yMin, yMax);
					if(vertex.z >= zPivot)					// 001
					{
						binary[binIndex++] = 1;
					}
					else									// 000
					{
						binary[binIndex++] = 0;
					}					
				}	
			}
		}

		int index = 0;
		for(int i = 0; i < binary.length; i++)
		{
			index += binary[i] * (int) Math.pow(2, i); 					
		}		
		
		return index;
	}
	
	public int GetLeafIndex2(Vector3f vertex)
	{
		float cMin = min;
		float cMax = max;
		float cPivot = getPivot(cMin, cMax);
				
		int[] xCoord = new int[_depth];
		int[] yCoord = new int[_depth];
		int[] zCoord = new int[_depth];
		
		// X
		for(int i = 0; i < _depth; i++)
		{
			if(vertex.x >= cPivot)
			{
				xCoord[i] = 1;
				cMin = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
			else
			{				
				xCoord[i] = 0;
				cMax = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
		}
		
		cMin = min;
		cMax = max;
		cPivot = getPivot(cMin, cMax);		
		// Y
		for(int i = 0; i < _depth; i++)
		{
			if(vertex.y >= cPivot)
			{
				yCoord[i] = 1;	
				cMin = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
			else
			{
				yCoord[i] = 0;
				cMax = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
		}
		
		cMin = min;
		cMax = max;
		cPivot = getPivot(cMin, cMax);		
		// Z
		for(int i = 0; i < _depth; i++)
		{
			if(vertex.z >= cPivot)
			{
				zCoord[i] = 1;	
				cMin = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
			else
			{
				zCoord[i] = 0;
				cMax = cPivot;
				cPivot = getPivot(cMin, cMax);
			}
		}
		
		int index = 0;
		int pow = 0;
		for(int i = xCoord.length - 1; i >= 0 ; i--)
		{
			index += (xCoord[i] * Math.pow(2, pow));
			pow++;
			index += (yCoord[i] * Math.pow(2, pow));
			pow++;
			index += (zCoord[i] * Math.pow(2, pow));
			pow++;
		}
		
		return index;
	}
	
	
	public void AddVector3f(Vector3f vertex)
	{				
		int index = GetLeafIndex(vertex);		
		Octree.get(index).add(vertex);
		LeafDensity[index]++;		
	}
	
	public void PrintDensity()
	{
		int i = 0;
		for(int count : LeafDensity)
		{			
			System.out.println("Leaf " + i + ": " + count);
			i++;
		}
	}


	
	
	private float getPivot(float min, float max)
	{
		
		// TODO
		if(max > min)
			return max - min;

	}
}
