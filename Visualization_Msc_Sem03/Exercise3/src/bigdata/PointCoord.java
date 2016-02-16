package bigdata;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * @author Justine Smyzek
 *
 */
public class PointCoord {

		//save as Vector
		private Vector3f vec = new Vector3f(); 
		//save more information
		int amount = 1;
		
		/**
		 * 
		 * @param x
		 * @param y
		 * @param z
		 */
		public PointCoord(float x, float y, float z) {
			vec.x = x;
			vec.y = y;
			vec.z = z;
		}

		public PointCoord(Vector3f vector3f) {
			vec.x = vector3f.x;
			vec.y = vector3f.y;
			vec.z = vector3f.z;
		}

		/**
		 * 
		 * @param x
		 */
		public void setX(float x){
			vec.x = x;
		}
		
		/**
		 * 
		 * @param y
		 */
		public void setY(float y){
			vec.x = y;
		}
		
		/**
		 * 
		 * @param z
		 */
		public void setZ(float z){
			vec.x = z;
		}
		
		/**
		 * 
		 * @return
		 */
		public float getX(){
			return vec.x;
		}
		
		/**
		 * 
		 * @return
		 */
		public float getY(){
			return vec.y;
		}
		
		/**
		 * 
		 * @return
		 */
		public float getZ(){
			return vec.z;
		}
		
		/**
		 * 
		 * @return
		 */
		public Vector3f getVec(){
			return vec;
		}
		
		
		/**
		 * 
		 */
		public void incAmount(){
			this.amount += 1;
		}
		
		/**
		 * 
		 * @return
		 */
		public int getAmount(){
			return this.amount;
		}
}
