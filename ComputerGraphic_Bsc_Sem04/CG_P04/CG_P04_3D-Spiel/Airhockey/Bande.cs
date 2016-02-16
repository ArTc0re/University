using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Airhockey
{
    class Bande
    {
        private int laenge, breite;

        public Texture2D sprite;
        public Vector3 Startposition;

        public int Spielfeldbreite { get { return breite; } set { breite = value; createGeometry(); } }
        public int Spielfeldlaenge { get { return laenge; } set { laenge = value; createGeometry(); } }
        public Vector3 BandeColor;

        private GraphicsDevice GD;

        public VertexPositionColorTexture[] BufferOBEN;
        public VertexPositionColorTexture[] BufferUNTEN;
        public VertexPositionColorTexture[] BufferRECHTS;
        public VertexPositionColorTexture[] BufferLINKS;


        public Bande(GraphicsDevice GraphicsDevice, int Spielfeldbreite, int Spielfeldlaenge, Vector3 iStartposition, Texture2D loadedtexture, Vector3 Farbe)
        {
            laenge = Spielfeldlaenge;
            breite = Spielfeldbreite;
            Startposition = iStartposition;
            this.GD = GraphicsDevice;
            sprite = loadedtexture;
            BandeColor = Farbe;

            createGeometry();

        }

        public void Draw()
        {
            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, BufferOBEN, 0, 2);

            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, BufferUNTEN, 0, 2);

            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, BufferRECHTS, 0, 2);

            GD.VertexDeclaration = new VertexDeclaration(GD, VertexPositionColorTexture.VertexElements);
            GD.DrawUserPrimitives<VertexPositionColorTexture>(PrimitiveType.TriangleStrip, BufferLINKS, 0, 2);

        }


        public void createGeometry()
        {
            BufferUNTEN = new VertexPositionColorTexture[4];

            BufferUNTEN[0].Position = new Vector3(-laenge / 2, -breite / 2, 0);
            BufferUNTEN[0].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferUNTEN[0].TextureCoordinate = new Vector2(0.0f, 0.0f);
            BufferUNTEN[1].Position = new Vector3(laenge / 2, -breite / 2, 0);
            BufferUNTEN[1].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferUNTEN[1].TextureCoordinate = new Vector2(1.0f, 0.0f);
            BufferUNTEN[2].Position = new Vector3(-laenge / 2, -breite / 2, 1);
            BufferUNTEN[2].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferUNTEN[2].TextureCoordinate = new Vector2(0.0f, 1.0f);
            BufferUNTEN[3].Position = new Vector3(laenge / 2, -breite / 2, 1);
            BufferUNTEN[3].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferUNTEN[3].TextureCoordinate = new Vector2(1.0f, 1.0f);


            BufferOBEN = new VertexPositionColorTexture[4];

            BufferOBEN[0].Position = new Vector3(-laenge / 2, breite / 2, 0);
            BufferOBEN[0].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferOBEN[0].TextureCoordinate = new Vector2(0.0f, 0.0f);
            BufferOBEN[1].Position = new Vector3(laenge / 2, breite / 2, 0);
            BufferOBEN[1].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferOBEN[1].TextureCoordinate = new Vector2(1.0f, 0.0f);
            BufferOBEN[2].Position = new Vector3(-laenge / 2, breite / 2, 1);
            BufferOBEN[2].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferOBEN[2].TextureCoordinate = new Vector2(0.0f, 1.0f);
            BufferOBEN[3].Position = new Vector3(laenge / 2, breite / 2, 1);
            BufferOBEN[3].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferOBEN[3].TextureCoordinate = new Vector2(1.0f, 1.0f);


            BufferLINKS = new VertexPositionColorTexture[4];

            BufferLINKS[0].Position = new Vector3(-laenge / 2, breite / 2, 0);
            BufferLINKS[0].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferLINKS[0].TextureCoordinate = new Vector2(0.0f, 0.0f);
            BufferLINKS[1].Position = new Vector3(-laenge / 2, -breite / 2, 0);
            BufferLINKS[1].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferLINKS[1].TextureCoordinate = new Vector2(1.0f, 0.0f);
            BufferLINKS[2].Position = new Vector3(-laenge / 2, breite / 2, 1);
            BufferLINKS[2].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferLINKS[2].TextureCoordinate = new Vector2(0.0f, 1.0f);
            BufferLINKS[3].Position = new Vector3(-laenge / 2, -breite / 2, 1);
            BufferLINKS[3].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferLINKS[3].TextureCoordinate = new Vector2(1.0f, 1.0f);


            BufferRECHTS = new VertexPositionColorTexture[4];

            BufferRECHTS[0].Position = new Vector3(laenge / 2, breite / 2, 0);
            BufferRECHTS[0].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferRECHTS[0].TextureCoordinate = new Vector2(0.0f, 0.0f);
            BufferRECHTS[1].Position = new Vector3(laenge / 2, -breite / 2, 0);
            BufferRECHTS[1].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferRECHTS[1].TextureCoordinate = new Vector2(1.0f, 0.0f);
            BufferRECHTS[2].Position = new Vector3(laenge / 2, breite / 2, 1);
            BufferRECHTS[2].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferRECHTS[2].TextureCoordinate = new Vector2(0.0f, 1.0f);
            BufferRECHTS[3].Position = new Vector3(laenge / 2, -breite / 2, 1);
            BufferRECHTS[3].Color = new Color(BandeColor.X, BandeColor.Y, BandeColor.Z);
            BufferRECHTS[3].TextureCoordinate = new Vector2(1.0f, 1.0f);

        }
    }
}


