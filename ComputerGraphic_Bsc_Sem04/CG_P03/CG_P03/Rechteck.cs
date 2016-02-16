/**
 *  Author: Sebastian Schaefer, schaefer@gdv.cs.uni-frankfurt.de
 **/
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;

namespace Rechteck
{
    public class Rechteck
    {
        private int iSubDivS;
        private int iSubDivT;

        public int SubdivisionS { get { return iSubDivS; } set { iSubDivS = value; createGeometry(); } }
        public int SubdivisionT { get { return iSubDivT; } set { iSubDivT = value; createGeometry(); } }

        private float fLengthS;
        private float fLengthT;

        public float LengthS { get { return fLengthS; } set { fLengthS = value; createGeometry(); } }
        public float LengthT { get { return fLengthT; } set { fLengthT = value; createGeometry(); } }

        private int iVertexCount;

        private GraphicsDevice GD;

        public VertexPositionColor[] Buffer;
        public int[] iIndices;
        
        public Rechteck(GraphicsDevice GraphicsDevice, int SubdivisionS, int SubdivisionT, float LengthS, float LengthT)
        {
            iSubDivS = SubdivisionS;
            iSubDivT = SubdivisionT;

            fLengthS = LengthS;
            fLengthT = LengthT;

            this.GD = GraphicsDevice;

            createGeometry();
        }

        public Rechteck(GraphicsDevice GraphicsDevice, int SubdivisionS, int SubdivisionT)
            :this(GraphicsDevice, SubdivisionS, SubdivisionT, 2, 2)
        {

        }

        public Rechteck(GraphicsDevice GraphicsDevice)
            :this(GraphicsDevice, 2, 2, 2, 2)
        {
        }

        public void Draw()
        {
            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColor.VertexElements);
            GD.DrawUserIndexedPrimitives<VertexPositionColor>(
                PrimitiveType.TriangleStrip,
                Buffer,
                0,
                iVertexCount,
                iIndices,
                0,
                iIndices.Length - 2
            );

        }

        private void createGeometry()
        {
            iVertexCount = (iSubDivS + 1) * (iSubDivT + 1);
            Buffer = new VertexPositionColor[iVertexCount];

            // Code angepasst -> X,Y ist viel schöner als X,Z o_O Sorry finde ich jedenfalls :P

            Vector3 posDelta = new Vector3(fLengthS / iSubDivS, fLengthT / iSubDivT, 0);
            Vector3 posBase = new Vector3(-fLengthS / 2f, -fLengthT / 2f, 0);
            Vector3 posCurrent = posBase;

            int idx = 0;
            for (int zz = 0; zz <= iSubDivT; zz++)
            {
                posCurrent.X = posBase.X;
                for (int xx = 0; xx <= iSubDivS; xx++)
                {
                    Buffer[idx++] = new VertexPositionColor(posCurrent, Color.White);
                    posCurrent.X += posDelta.X;
                }
                posCurrent.Y += posDelta.Y;
            }

            iIndices = new int[(4 + 2 * iSubDivS) * iSubDivT];
            idx = 0;
            for (int zz = 0; zz < iSubDivT; zz++)
            {
                if (zz % 2 == 0)
                {
                    for (int xx = 0; xx <= iSubDivS; xx++)
                    {
                        iIndices[idx] = (short)(zz * (iSubDivS + 1) + xx);
                        idx++;

                        iIndices[idx] = (short)((zz + 1) * (iSubDivS + 1) + xx);
                        idx++;
                    }
                    iIndices[idx] = iIndices[idx - 1];
                    idx++;
                    iIndices[idx] = iIndices[idx - 1];
                    idx++;
                }
                else
                {
                    for (int xx = iSubDivS; xx >= 0; xx--)
                    {
                        iIndices[idx] = (short)((zz + 1) * (iSubDivS + 1) + xx);
                        idx++;

                        iIndices[idx] = (short)(zz * (iSubDivS + 1) + xx);
                        idx++;
                    }
                    iIndices[idx] = iIndices[idx - 2];
                    idx++;
                    iIndices[idx] = iIndices[idx - 2];
                    idx++;
                }
            }
        }
    }
}
