using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FarbRechner
{
    /// <summary>
    /// Container class for our VertexBufferObjects, for better handling and to possibly avoid ambiguitations
    /// </summary>
    /// <author>Markus Strobel</author>
    public class VBO
    {
        public int VertexBufferID { get; set;}
        public int IndicesBufferID { get; set; }
        public int NumElements { get; set; } 

        public VBO()
        {
            VertexBufferID = -1;
            IndicesBufferID = -1;
            NumElements = -1;
        }
    }
}
