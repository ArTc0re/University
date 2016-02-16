using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Airhockey
{	
	/// <summary>
    /// Author: Markus Strobel
	/// E-Mail: mksstrobel@googlemail
    /// </summary>
    class Spielfeld
    {

        public int[] iIndices;
        private int iVertexCount;


        private int laenge, breite;

        public Texture2D sprite;
        public Vector3 Startposition;
        public Vector3 Normale;

        public int Spielfeldbreite { get { return breite; } set { breite = value; createGeometry(); } }
        public int Spielfeldlaenge { get { return laenge; } set { laenge = value; createGeometry(); } }

        private GraphicsDevice GD;

        public VertexPositionColorTexture[] Buffer;


        public Spielfeld(GraphicsDevice GraphicsDevice, int Spielfeldbreite, int Spielfeldlaenge, Vector3 iStartposition, Texture2D loadedtexture)
        {
            laenge = Spielfeldlaenge;
            breite = Spielfeldbreite;
            Startposition = iStartposition;
            this.GD = GraphicsDevice;
            sprite = loadedtexture;
            Normale = new Vector3(1, 0, 0);  
            createGeometry();
        }

 
            //GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            //GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, Buffer, 0, 2);

        public void Draw()
        {
            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColor.VertexElements);
            GD.DrawUserIndexedPrimitives<VertexPositionColorTexture>(
                PrimitiveType.TriangleStrip,
                Buffer,
                0,
                iVertexCount,
                iIndices,
                0,
                iIndices.Length - 2
            );

        }


        public void createGeometry()
        {
            //Buffer = new VertexPositionColorTexture[4];

            //Buffer[0].Position = new Vector3(-laenge / 2, -breite / 2, 0);
            //Buffer[0].Color = Color.White;
            //Buffer[0].TextureCoordinate = new Vector2(0.0f, 0.0f);
            //Buffer[1].Position = new Vector3(laenge / 2, -breite / 2, 0);
            //Buffer[1].Color = Color.White;
            //Buffer[1].TextureCoordinate = new Vector2(1.0f, 0.0f);
            //Buffer[2].Position = new Vector3(-laenge / 2, breite / 2, 0);
            //Buffer[2].Color = Color.White;
            //Buffer[2].TextureCoordinate = new Vector2(0.0f, 1.0f);
            //Buffer[3].Position = new Vector3(laenge / 2, breite / 2, 0);
            //Buffer[3].Color = Color.White;
            //Buffer[3].TextureCoordinate = new Vector2(1.0f, 1.0f);


            iVertexCount = (10 + 1) * (10 + 1);
            Buffer = new VertexPositionColorTexture[iVertexCount];

            // Code angepasst -> X,Y ist viel schöner als X,Z o_O Sorry finde ich jedenfalls :P

            Vector3 posDelta = new Vector3(Spielfeldlaenge / 10, Spielfeldbreite / 10, 0);
            Vector3 posBase = new Vector3(-Spielfeldlaenge / 2f, -Spielfeldbreite / 2f, 0);
            Vector3 posCurrent = posBase;

            int idx = 0;
            for (int zz = 0; zz <= 10; zz++)
            {
                posCurrent.X = posBase.X;
                for (int xx = 0; xx <= 10; xx++)
                {
                    Buffer[idx++] = new VertexPositionColorTexture(posCurrent, Color.White, new Vector2(1f, 1f));
                    posCurrent.X += posDelta.X;
                }
                posCurrent.Y += posDelta.Y;
            }

            iIndices = new int[(4 + 2 * 10) * 10];
            idx = 0;
            for (int zz = 0; zz < 10; zz++)
            {
                if (zz % 2 == 0)
                {
                    for (int xx = 0; xx <= 10; xx++)
                    {
                        iIndices[idx] = (short)(zz * (10 + 1) + xx);
                        idx++;

                        iIndices[idx] = (short)((zz + 1) * (10 + 1) + xx);
                        idx++;
                    }
                    iIndices[idx] = iIndices[idx - 1];
                    idx++;
                    iIndices[idx] = iIndices[idx - 1];
                    idx++;
                }
                else
                {
                    for (int xx = 10; xx >= 0; xx--)
                    {
                        iIndices[idx] = (short)((zz + 1) * (10 + 1) + xx);
                        idx++;

                        iIndices[idx] = (short)(zz * (10 + 1) + xx);
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


