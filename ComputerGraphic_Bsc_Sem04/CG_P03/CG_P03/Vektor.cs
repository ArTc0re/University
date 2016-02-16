using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace CG_P03
{
	/// <summary>
    /// Author: Markus Strobel
	/// E-Mail: mksstrobel@googlemail
    /// </summary>
    class Vektor
    {
        Vector3 start, end;
        public Vector3 startpunkt { get { return start; } set { start = value; createGeometry(); } }
        public Vector3 endpunkt{ get { return end; } set { end = value; createGeometry(); } }

        private GraphicsDevice GD;
        public VertexPositionColor[] Buffer;

        public Vektor(GraphicsDevice GraphicsDevice, Vector3 startpunkt, Vector3 endpunkt)
        {
            start = startpunkt;
            end = endpunkt;
            this.GD = GraphicsDevice;
            createGeometry();
        }

        public void Draw()
        {
            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColor.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColor>(
                PrimitiveType.LineStrip,
                Buffer,
                0,
                1
            );
        }

        public void createGeometry()
        {
            Buffer = new VertexPositionColor[2];
            Buffer[0] = new VertexPositionColor(start, Color.White);
            Buffer[1] = new VertexPositionColor(end, Color.White);
        }
    }
}
