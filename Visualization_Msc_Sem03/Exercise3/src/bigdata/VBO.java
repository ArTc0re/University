package bigdata;

/**
 * Vertex Buffer Object Class
 * @author Markus Strobel
 *
 */
public class VBO {

    public int VertexBufferID;
    public int IndicesBufferID;
    public int NumElements;
    public int NumIndices;
    
    public VBO()
    {
    	this(-1, -1, -1, -1);
    }
    
    public VBO(int verticesBufferId, int indicesBufferId, int numElements, int numIndices)
    {
    	VertexBufferID = verticesBufferId;
    	IndicesBufferID = indicesBufferId;
    	NumElements = numElements;
    	NumIndices = numIndices;
    }
}
