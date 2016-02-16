using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using Microsoft.Xna.Framework.Net;
using Microsoft.Xna.Framework.Storage;
using XTL.Models;
using XTL.Display;
using XTL.Input;

namespace Airhockey
{
    class Puck
    {
        #region Variablendeklaration

        Vector3 Geschwindigkeit, Startposition;

        public Vector3 Position { get { return Startposition; } set { Startposition = value; createGeometry(); } }
        public Vector3 Speed { get { return Geschwindigkeit; } set { Geschwindigkeit = value; createGeometry(); } }

        public GraphicsDevice GD;

        public VertexPositionColorTexture[] Buffer;
        public VertexPositionColorTexture[] Buffer2;

        #endregion

        public Puck(GraphicsDevice GraphicsDevice, Vector3 startposition, Vector3 Speed)
        {
            Startposition = Position;
            Geschwindigkeit = Speed;


            this.GD = GraphicsDevice;

            createGeometry();
        }
        
        public void createGeometry()
        {
            Buffer = new VertexPositionColorTexture[36];
            for (int xx = 0; xx < 36; xx += 2)
            {
                Buffer[xx].Position =
                    Position + new Vector3((float)Math.Cos(MathHelper.ToRadians(xx * 20f)),
                        (float)Math.Sin(MathHelper.ToRadians(xx * 20f)),
                            0.1f);
                Buffer[xx].Color = Color.Black;
                //Buffer[xx].TextureCoordinate =
                //    new Vector2(1.0f,
                //        1.0f);

                Buffer[xx + 1].Position =
                    Position + new Vector3((float)Math.Cos(MathHelper.ToRadians(xx * 20f)),
                        (float)Math.Sin(MathHelper.ToRadians(xx * 20f)),
                        0.6f);
                Buffer[xx + 1].Color = Color.Black;
                //Buffer[xx + 1].TextureCoordinate =
                //    new Vector2(0.0f,
                //        0.0f);
            }
            Buffer2 = new VertexPositionColorTexture[20];
            Buffer2[0].Position = Position + new Vector3(0, 0, 0.6f);
            Buffer2[19].Position = Position + new Vector3((float)Math.Cos(MathHelper.ToRadians(0 * 20f)),
                        (float)Math.Sin(MathHelper.ToRadians(0 * 20f)),
                        0.6f);
            for (int yy = 0; yy < 18; yy++)
            {
                Buffer2[yy + 1].Position =
                    Position + new Vector3((float)Math.Cos(MathHelper.ToRadians(yy * 20f)),
                        (float)Math.Sin(MathHelper.ToRadians(yy * 20f)),
                        0.6f);
                Buffer2[yy + 1].Color = Color.Black;
                //Buffer2[yy + 1].TextureCoordinate =
                //    new Vector2(1.0f,
                //        1.0f);
            }

            
        }

        public void Draw()
        {
            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, Buffer, 0, Buffer.Length - 2);

            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleFan, Buffer2, 0, Buffer2.Length - 2);



        }

    }
}
