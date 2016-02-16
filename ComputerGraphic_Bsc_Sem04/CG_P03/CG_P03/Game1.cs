Using System;
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

namespace CG_P03
{
	/// <summary>
    /// Author: Markus Strobel
	/// E-Mail: mksstrobel@googlemail
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        #region Variablendeklaration
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        float aspectRatio;
        XNAState state;
        Vector3 Position, LookAt, Rotation;

        // Für die Beleuchtungsrechnung benötigte Variablen
        Vector3 Normale;
        Vector3 Light_ambient, Light_diffuse, Light_specular;
        Vector3 Material_ambient, Material_diffuse, Material_specular;
        Vector3 Lichtquelle, Camera, Licht_direction, Camera_direction, Reflexion;
        Vector3 Color_ambient, Color_diffuse, Color_specular, Color_iluminated;
        float shininess;
        float angle_diffuse, angle_specular, angle_CN;
        int subdivwidth, subdivheight, renderstate, drawvectors;
        Rechteck.Rechteck rechteck;

        // "Dummyobjekte"
        Rechteck.Rechteck cameradummy, lichtdummy;

        // Vektorendarstellung
        Vektor Normalenvektor, Kameravektor, Lichtvektor, Reflexionsvektor;
        int selected_vertex = 0;

        // HUDText
        HUDText hudtext;
        string text;

        #endregion

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";

        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {

            aspectRatio = (float)graphics.GraphicsDevice.Viewport.Width / (float)graphics.GraphicsDevice.Viewport.Height;
            Position = new Vector3(0, 0, 40);
            LookAt = new Vector3(0, 0, 0);
            Rotation = new Vector3(0, 0, 0);
            

            state = new XNAState(Keyboard.GetState(), Mouse.GetState());

            // Initialisierung der für die Beleuchtungsrechnung benötigten Vektoren
            // Farben am Beispiel orientiert
            Normale = new Vector3(0, 0, 1);
            Light_ambient = new Vector3(33/255f, 33/255f, 33/255f);
            Light_diffuse = new Vector3(1f, 1f, 1f);
            Light_specular = new Vector3(1f, 1f, 1f);

            Material_ambient = new Vector3(1f, 1f, 1f);
            Material_specular = new Vector3(1f, 1f, 1f);
            Material_diffuse = new Vector3(20/255f, 110/255f, 171/255f);

            Color_iluminated = new Vector3(1f, 1f, 1f);

            Lichtquelle = new Vector3(0, 0, 4);
            Camera = new Vector3(0, 0, 20);
            shininess = 1;

            subdivwidth = 20;
            subdivheight = 20;
            renderstate = 1;
            drawvectors = 0;

            hudtext = new HUDText(this, "SpriteFont1");
            hudtext.Color = Color.White;
            Components.Add(hudtext);



            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);
            rechteck = new Rechteck.Rechteck(GraphicsDevice, subdivwidth, subdivheight, 15, 15);


            // Dummy Objekte werden quasi erstellt.
            lichtdummy = new Rechteck.Rechteck(GraphicsDevice, 1, 1, 4, 4);
            Lichtdummy();
            cameradummy = new Rechteck.Rechteck(GraphicsDevice, 1, 1, 4, 4);
            Cameradummy();
            cameradummy.Buffer[0].Color = new Color(1.0f, 0.0f, 0.0f);
            cameradummy.Buffer[1].Color = new Color(0.0f, 1.0f, 0.0f);
            cameradummy.Buffer[2].Color = new Color(0.0f, 0.0f, 1.0f);
            cameradummy.Buffer[3].Color = new Color(0.8f, 0.4f, 0.1f);




            Beleuchtungsrechnung();
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }


        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            state.Update(Keyboard.GetState(), Mouse.GetState());
            if ((GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed) ||
                (state.Keys.IsDown(Keys.Escape)))
                this.Exit();

            #region Kamerasteuerung

            // Bewegung der Szenenkamera in X und Y Richtung
            if (state.Keys.IsDown(Keys.A))
                LookAt.X -= 0.2f;
            if (state.Keys.IsDown(Keys.D))
                LookAt.X += 0.2f;
            if (state.Keys.IsDown(Keys.W))
                LookAt.Y += 0.2f;
            if (state.Keys.IsDown(Keys.S))
                LookAt.Y -= 0.2f;

            // Zoom nun mittels Mausrad in Z Richtung zu bewerkstelligen
            LookAt.Z += (state.Mouse.DeltaScrollWheel) * 0.015f;

            //delta auslesen, skalieren (Übernommen aus dem Beispiel)
            Rotation.X = (state.Mouse.DeltaY * 0.004f + Rotation.X) % 360;
            Rotation.Y = (state.Mouse.DeltaX * 0.004f + Rotation.Y) % 360;
            #endregion

            #region Rechteckunterteilung
            if (state.Keys.IsDown(Keys.U) || state.Keys.IsDown(Keys.J) || state.Keys.IsDown(Keys.I) || state.Keys.IsDown(Keys.K))
            {
                rechteck.SubdivisionS = subdivwidth;
                rechteck.SubdivisionT = subdivheight;

                // Materialfarbe neu setzen
                int v = rechteck.Buffer.Length - 1;
                for (int i = 0; i <= v; i++)
                {
                    rechteck.Buffer[i].Color = new Color(Light_diffuse);
                }
            }

            #endregion

            #region  Beleuchtungsrechnung
            if (state.Keys.IsDown(Keys.C))
            {
                Beleuchtungsrechnung();
            }


            #endregion

            #region Parametereinstellungen

            #region Lichtquellenposition
            // Bewegung der Lichtquelle und des Lichtquellendummy's
            if (state.Keys.IsDown(Keys.Up))
            {
                Lichtquelle.Y += 0.2f;
                Lichtdummy();
            }
            if (state.Keys.IsDown(Keys.Down))
            {
                Lichtquelle.Y -= 0.2f;
                Lichtdummy();
            }
            if (state.Keys.IsDown(Keys.Left))
            {
                Lichtquelle.X -= 0.2f;
                Lichtdummy();
            }
            if (state.Keys.IsDown(Keys.Right))
            {
                Lichtquelle.X += 0.2f;
                Lichtdummy();
            }
            if (state.Keys.IsDown(Keys.NumPad9))
            {
                if (Lichtquelle.Z <= 50.0f)
                {
                    Lichtquelle.Z += 0.2f;
                    Lichtdummy();
                }
            }
            if (state.Keys.IsDown(Keys.NumPad3))
            {
                if (Lichtquelle.Z >= 0.2f)
                {
                    Lichtquelle.Z -= 0.2f;
                    Lichtdummy();
                }
            }

            #endregion

            #region Beleuchtungskameraposition
            // Bewegung der Beleuchtungskamera
            if (state.Keys.IsDown(Keys.NumPad8))
            {
                Camera.Y += 0.2f;
                Cameradummy();
            }
            if (state.Keys.IsDown(Keys.NumPad2))
            {
                Camera.Y -= 0.2f;
                Cameradummy();
            }
            if (state.Keys.IsDown(Keys.NumPad4))
            {
                Camera.X -= 0.2f;
                Cameradummy();
            }
            if (state.Keys.IsDown(Keys.NumPad6))
            {
                Camera.X += 0.2f;
                Cameradummy();
            }
            if (state.Keys.IsDown(Keys.NumPad7))
            {
                Camera.Z += 0.2f;
                Cameradummy();
            }
            if (state.Keys.IsDown(Keys.NumPad1))
            {
                Camera.Z -= 0.2f;
                Cameradummy();
            }




            #endregion

            #region Lichtfarbe
            // Lichtfarbe ändern
            // Rot
            if (state.Keys.IsDown(Keys.F1))
            {
                if (Light_diffuse.X * 255 < 255)
                {
                    Light_diffuse.X += 1 / 255f;
                    Light_specular.X += 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }
            if (state.Keys.IsDown(Keys.F2))
            {
                if (Light_diffuse.X >= 0)
                {
                    Light_diffuse.X -= 1 / 255f;
                    Light_specular.X -= 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }
            // Grün
            if (state.Keys.IsDown(Keys.F3))
            {
                if (Light_diffuse.Y * 255 < 255)
                {
                    Light_diffuse.Y += 1 / 255f;
                    Light_specular.Y += 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }
            if (state.Keys.IsDown(Keys.F4))
            {
                if (Light_diffuse.Y >= 0)
                {
                    Light_diffuse.Y -= 1 / 255f;
                    Light_specular.Y -= 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }
            // Blau
            if (state.Keys.IsDown(Keys.F5))
            {
                if (Light_diffuse.Z * 255 < 255)
                {
                    Light_diffuse.Z += 1 / 255f;
                    Light_specular.Z += 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }
            if (state.Keys.IsDown(Keys.F6))
            {
                if (Light_diffuse.Z >= 0)
                {
                    Light_diffuse.Z -= 1 / 255f;
                    Light_specular.Z -= 1 / 255f;
                    lichtdummy.Buffer[0].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[1].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[2].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                    lichtdummy.Buffer[3].Color = new Color(Light_diffuse.X, Light_diffuse.Y, Light_diffuse.Z);
                }
            }

            #endregion

            #region Materialfarbe
            // Materialfarbe ändern
            // Rot
            if (state.Keys.IsDown(Keys.D1))
            {
                if (Material_diffuse.X * 255 < 255)
                {
                    Material_diffuse.X += 1 / 255f;
                    Material_specular.X += 1 / 255f;
                }
            }
            if (state.Keys.IsDown(Keys.D2))
            {
                if (Material_diffuse.X >= 0)
                {
                    Material_diffuse.X -= 1 / 255f;
                    Material_specular.X -= 1 / 255f;
                }
            }
            // Grün
            if (state.Keys.IsDown(Keys.D3))
            {
                if (Material_diffuse.Y * 255 < 255)
                {
                    Material_diffuse.Y += 1 / 255f;
                    Material_specular.Y += 1 / 255f;
                }
            }
            if (state.Keys.IsDown(Keys.D4))
            {
                if (Material_diffuse.Y >= 0)
                {
                    Material_diffuse.Y -= 1 / 255f;
                    Material_specular.Y -= 1 / 255f;
                }
            }
            // Blau
            if (state.Keys.IsDown(Keys.D5))
            {
                if (Material_diffuse.Z * 255 < 255)
                {
                    Material_diffuse.Z += 1 / 255f;
                    Material_specular.Z += 1 / 255f;
                }
            }
            if (state.Keys.IsDown(Keys.D6))
            {
                if (Material_diffuse.Z >= 0)
                {
                    Material_diffuse.Z -= 1 / 255f;
                    Material_specular.Z -= 1 / 255f;
                }
            }
            #endregion

            #region Rechteckunterteilung
            // Unterteilung der Rechtecke Breite
            if (state.Keys.IsDown(Keys.U))
            {
                if (subdivwidth < 100)
                {
                    subdivwidth += 1;
                }
            }
            if (state.Keys.IsDown(Keys.J))
            {
                if (subdivwidth > 1)
                {
                    subdivwidth -= 1;
                }
            }

            // Unterteilung der Rechtecke Hoehe
            if (state.Keys.IsDown(Keys.I))
            {
                if (subdivheight < 100)
                {
                    subdivheight += 1;
                }
            }
            if (state.Keys.IsDown(Keys.K))
            {
                if (subdivheight > 1)
                {
                    subdivheight -= 1;
                }
            }
            #endregion

            #region FillMode wechseln
            // Renderstate wechseln
            if (state.Keys.KeyDown(Keys.X))
            {
                if (renderstate == 1)
                {
                    renderstate = 0;
                }
                else
                {
                    renderstate = 1;
                }
            }
            #endregion

            // Alle Parameter zurücksetzen zurücksetzen
            if (state.Keys.KeyDown(Keys.NumPad0))
            {
                Lichtquelle = new Vector3(0, 0, 4);
                Camera = new Vector3(0, 0, 20);
                shininess = 1;
                Light_specular = new Vector3(1f, 1f, 1f);
                Light_diffuse = new Vector3(1f, 1f, 1f);
                Material_diffuse = new Vector3(20 / 255f, 110 / 255f, 171 / 255f);
                selected_vertex = 0;
                Lichtdummy();
                Cameradummy();     
            }
            // Shininess bzw Glanz Wert anpassen
            if (state.Keys.IsDown(Keys.H))
            {
                if (shininess >= 2)
                    shininess -= 1;
            }
            if (state.Keys.IsDown(Keys.Z))
            {
                if (shininess <= 100)
                    shininess += 1;
            }

            #endregion

            #region Vektorendarstellung

            Vektorendarstellung();

            if (state.Keys.KeyDown(Keys.M))
            {
                if (drawvectors == 1)
                {
                    drawvectors = 0;
                }
                else
                {
                    drawvectors = 1;
                }
            }

            if (state.Keys.IsDown(Keys.PageUp))
            {
                if (selected_vertex < rechteck.Buffer.Length - 1)
                {
                    selected_vertex += 1;
                }
            }
            if (state.Keys.IsDown(Keys.PageDown))
            {
                if (selected_vertex >= 1)
                {
                    selected_vertex -= 1;
                }
            }

            #endregion

            #region HUDText

            text = "Grundlagen der Computergraphik - Programmieraufgabe 3 - Beleuchtungsrechung\nAuthor: Markus Strobel\n";
            text += "\nLichtquelle: X = " + Lichtquelle.X.ToString() + " Y = " + Lichtquelle.Y.ToString() + " Z = " + Lichtquelle.Z.ToString();
            text += "\nKamera: X = " + Camera.X.ToString() + " Y = " + Camera.Y.ToString() + " Z = " + Camera.Z.ToString();
            // Habe hier nur eine Farbe zum anpassen zur Verfuegung gestellt, bzw Diffus und Spiegelnd ist gleich,
            // da unklar war ob mehr gewollt ist... Ambient bleibt auch unveraendert.
            text += "\nLichtfarbe: R = " + ((int)(Light_diffuse.X * 255)).ToString() + " G = " + ((int)(Light_diffuse.Y * 255)).ToString() + " B = " + ((int)(Light_diffuse.Z * 255)).ToString();
            text += "\nMaterialfarbe: R = " + ((int)(Material_diffuse.X * 255)).ToString() + " G = " + ((int)(Material_diffuse.Y * 255)).ToString() + " B = " + ((int)(Material_diffuse.Z * 255)).ToString();
            text += "\nGlanzwert = " + shininess.ToString();
            text += "\n\n### Steuerung ###";
            text += "\n\nTaste C zum Berechnen";
            text += "\n\nSzenenkamera: \nW|A|S|D Zoom: Mausrad";
            text += "\nLichtquelle: \nUP|DOWN|LEFT|RIGHT|Num9|Num3";
            text += "\nBeleuchtungskamera: \nNum: X: 8|2  Y: 4|6  Z: 7|1";
            text += "\n\nLichtfarbe variieren ";
            text += "\nR: 1|2 G: 3|4 B: 5|6";
            text += "\nMaterialfarbe variieren ";
            text += "\nR: F1|F2 G: F3|F4 B: F5|F6";
            text += "\n\nGlanz: Z|H";
            text += "\nRechteckunterteilung: \nX:  U|J    Y:  I|K \n(neu berechnen!)";
            text += "\nX * Y  =  " + subdivwidth.ToString() + " * " + subdivheight.ToString();
            text += "\nWireFrame/Solid:  X";
            text += "\nVektoren an/aus:  M";
            text += "\nVertices durchschalten: \nPageUp|PageDown";
            text += "\nReset:\n";
            text += "Num 0";
            text += "\n\n### Das bunte Rechteck ist die Beleuchtungskamera ###";
            text += "\n### Das andere Rechteck ist die Lichtquelle ###";

            hudtext.Text = text;
            #endregion

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(new Color(0, 0, 0));
            if (renderstate == 1)
            {
                graphics.GraphicsDevice.RenderState.FillMode = FillMode.Solid;
            }
            else
            {
                graphics.GraphicsDevice.RenderState.FillMode = FillMode.WireFrame;
            }

            BasicEffect fx = new BasicEffect(GraphicsDevice, null);
            fx.VertexColorEnabled = true;


            fx.World = Matrix.CreateRotationX(Rotation.X) * Matrix.CreateRotationY(Rotation.Y);
            fx.View = Matrix.CreateLookAt(Position + LookAt, LookAt, Vector3.Up);
            fx.Projection = Matrix.CreatePerspectiveFieldOfView(MathHelper.ToRadians(45.0f), aspectRatio, 1.0f, 10000.0f);

            fx.Begin();
            foreach (EffectPass pass in fx.CurrentTechnique.Passes)
            {
                pass.Begin();
                rechteck.Draw();

                if (drawvectors == 1)
                {
                    Normalenvektor.Draw();
                    Lichtvektor.Draw();
                    Kameravektor.Draw();
                    //Reflexionsvektor.Draw();
                }

                lichtdummy.Draw();
                cameradummy.Draw();
                graphics.GraphicsDevice.RenderState.FillMode = FillMode.Solid;
                hudtext.Draw(gameTime);

                pass.End();
            }
            fx.End();

            base.Draw(gameTime);
        }

        /// <summary>
        /// Hier wird die Position des Lichtquellendummys bestimmt.
        /// </summary>
        private void Lichtdummy()
        {
            lichtdummy.Buffer[0].Position = new Vector3(Lichtquelle.X - 0.5f, Lichtquelle.Y - 0.5f, Lichtquelle.Z);
            lichtdummy.Buffer[1].Position = new Vector3(Lichtquelle.X + 0.5f, Lichtquelle.Y - 0.5f, Lichtquelle.Z);
            lichtdummy.Buffer[2].Position = new Vector3(Lichtquelle.X - 0.5f, Lichtquelle.Y + 0.5f, Lichtquelle.Z);
            lichtdummy.Buffer[3].Position = new Vector3(Lichtquelle.X + 0.5f, Lichtquelle.Y + 0.5f, Lichtquelle.Z);
        }

        /// <summary>
        /// Hier wird die Position des Cameradummys bestimmt.
        /// </summary>
        private void Cameradummy()
        {
            cameradummy.Buffer[0].Position = new Vector3(Camera.X - 0.5f, Camera.Y - 0.5f, Camera.Z);
            cameradummy.Buffer[1].Position = new Vector3(Camera.X + 0.5f, Camera.Y - 0.5f, Camera.Z);
            cameradummy.Buffer[2].Position = new Vector3(Camera.X - 0.5f, Camera.Y + 0.5f, Camera.Z);
            cameradummy.Buffer[3].Position = new Vector3(Camera.X + 0.5f, Camera.Y + 0.5f, Camera.Z);
        }

        /// <summary>
        /// Hier wird die Beleuchtungsrechnung für alle Vertices durchgeführt.
        /// </summary>
        private void Beleuchtungsrechnung()
        {
            int vertices = rechteck.Buffer.Length - 1;

            // Ambienter Farbanteil
            Color_ambient = Light_ambient * Material_ambient;

            for (int i = 0; i <= vertices; i++)
            {
                Licht_direction = rechteck.Buffer[i].Position - Lichtquelle;
                Camera_direction = rechteck.Buffer[i].Position - Camera;

                // Diffuser Farbanteil
                float Skalarprodukt_NL = (Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z);
                float Betrag_NL = (float)Math.Sqrt((Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z));
                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;

                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = (Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z);
                float Betrag_CN = (float)Math.Sqrt((Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z));

                angle_CN = Skalarprodukt_CN / Betrag_CN;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;
                Color_specular = Light_specular * Material_specular * ((float)Math.Pow(angle_specular, shininess));

                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;
                rechteck.Buffer[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z); ;

            }
        }

        private void Vektorendarstellung()
        {
            // Vektoren für die Darstellung der einzelnen "Vektoren"(Licht, Kamera, Normale und Reflexions werden hier erstellt.
            // Normalenvektor
            Normalenvektor = new Vektor(GraphicsDevice, rechteck.Buffer[selected_vertex].Position, new Vector3(rechteck.Buffer[selected_vertex].Position.X, rechteck.Buffer[selected_vertex].Position.Y, rechteck.Buffer[selected_vertex].Position.Z + 2));

            // Lichtvektor
            Vector3 Licht_direction = (Lichtquelle - rechteck.Buffer[selected_vertex].Position);
            float Lichtlaenge = (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z);
            Vector3 Laengenanpassung = Licht_direction * 2 / Lichtlaenge;
            Lichtvektor = new Vektor(GraphicsDevice, rechteck.Buffer[selected_vertex].Position, new Vector3(rechteck.Buffer[selected_vertex].Position.X, rechteck.Buffer[selected_vertex].Position.Y, rechteck.Buffer[selected_vertex].Position.Z) + Laengenanpassung);
            
            // Kameravektor
            Vector3 Kamera_direction = (Camera - rechteck.Buffer[selected_vertex].Position);
            float Kameralaenge = (float)Math.Sqrt(Kamera_direction.X * Kamera_direction.X + Kamera_direction.Y * Kamera_direction.Y + Kamera_direction.Z * Kamera_direction.Z);
            Vector3 Laengenanpassung2 = Kamera_direction * 2 / Kameralaenge;
            Kameravektor = new Vektor(GraphicsDevice, rechteck.Buffer[selected_vertex].Position, rechteck.Buffer[selected_vertex].Position + Laengenanpassung2);
            
            // Reflexionsvektor __ funktioniert leider nicht so wie er soll, find den Fehler nicht -.-
            Reflexion = ((2 * Normale * angle_diffuse) - Lichtquelle );
            Vector3 Reflexions_direction = (Reflexion - rechteck.Buffer[selected_vertex].Position);
            float Reflexionslaenge = (float)Math.Sqrt(Reflexions_direction.X * Reflexions_direction.X + Reflexions_direction.Y * Reflexions_direction.Y + Reflexions_direction.Z * Reflexions_direction.Z);
            Vector3 Laengenanpassung3 = Reflexions_direction * 2 / Reflexionslaenge;
            Reflexionsvektor = new Vektor(GraphicsDevice, rechteck.Buffer[selected_vertex].Position, new Vector3(rechteck.Buffer[selected_vertex].Position.X, rechteck.Buffer[selected_vertex].Position.Y, rechteck.Buffer[selected_vertex].Position.Z) + Laengenanpassung3);
        }
    }
}
