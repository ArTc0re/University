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
    /// <summary>
    /// Author: Markus Strobel
    /// Matrikelnummer: 3545258
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        #region Variablendeklaration
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        Vector3 Position, LookAt, Rotation;
        float aspectRatio;
        XNAState state;
        int Punktestand_Spieler1, Punktestand_Spieler2, puckcamera;
        float startmode;

        // Variablen für das 3D-Spiel
        int Spielfeldbreite;
        int Spielfeldlaenge;
        Spielfeld field;
        Puck puck;
        Schlaeger Spieler1, Spieler2;

        // Texturen --- wenn das mal klappen würde.. naja -.-
        Texture2D fieldTexture;

        // HUDText
        HUDText hudtext;
        string text;

        // Für die Beleuchtungsrechnung benötigte Variablen
        Vector3 Normale;
        Vector3 Light_ambient, Light_diffuse, Light_specular;
        Vector3 Material_ambient, Material_diffuse, Material_specular;
        Vector3 Lichtquelle, Camera, Licht_direction, Camera_direction;
        Vector3 Color_ambient, Color_diffuse, Color_specular, Color_iluminated;
        float shininess;
        float angle_diffuse, angle_specular, angle_CN;

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
            Position = new Vector3(0, -46, 46);
            LookAt = new Vector3(0, 5, 0);
            Rotation = new Vector3(0, 0, 0);

            state = new XNAState(Keyboard.GetState(), Mouse.GetState());

            Spielfeldbreite = 30;
            Spielfeldlaenge = 60;

            Punktestand_Spieler1 = 0;
            Punktestand_Spieler2 = 0;

            // hudtext
            hudtext = new HUDText(this, "SpriteFont1");
            hudtext.Color = Color.White;
            Components.Add(hudtext);



            // Initialisierung der für die Beleuchtungsrechnung benötigten Vektoren
            Light_ambient = new Vector3(0f, 0f, 0f);
            Light_diffuse = new Vector3(1f, 1f, 1f);
            Light_specular = new Vector3(1f, 1f, 1f);

            Material_ambient = new Vector3(1f, 1f, 1f);
            Material_specular = new Vector3(1f, 1f, 1f);


            Lichtquelle = new Vector3(0, 0, 20);
            Camera = new Vector3(0, 0, 25);
            shininess = 1;


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

            field = new Spielfeld(GraphicsDevice, Spielfeldbreite, Spielfeldlaenge, new Vector3(0, 0, 0), fieldTexture);


            puck = new Puck(GraphicsDevice, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
            Spieler1 = new Schlaeger(GraphicsDevice, new Vector3(-22, 0, 0), new Color(1f, 0f, 0f));
            Spieler2 = new Schlaeger(GraphicsDevice, new Vector3(22, 0, 0), new Color(0f, 0f, 1f));
          
            fieldTexture = Content.Load<Texture2D>("textures\\snow");



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
            // Tastatur und Maus Statusabfrage
            state.Update(Keyboard.GetState(), Mouse.GetState());
            startmode = 1.0f * (float)gameTime.TotalRealTime.Milliseconds % 4;

            float koeffizient = 2f / (((float)gameTime.TotalRealTime.Milliseconds % 5)+1);
            float koeffizient2 = 2f / ((((float)gameTime.TotalRealTime.Milliseconds + 132) % 5) + 1);

            #region Programmsteuerung

            #region Menüsteuerungen bzw Optionen

            //Programm beenden
            if (state.Keys.IsDown(Keys.Escape))
                this.Exit();
            //Programm resetten
            if (state.Keys.IsDown(Keys.F1))
                Restart();
            //Programm/Runde starten
            if (puck.Speed == new Vector3(0, 0, 0))
            {
                if (state.Keys.IsDown(Keys.Space))
                {
                    if (startmode == 0)
                        puck.Speed = new Vector3(koeffizient * 15,koeffizient2 * 15, 0);
                    if (startmode == 1)
                        puck.Speed = new Vector3(-koeffizient * 15, koeffizient2 * 15, 0);
                    if (startmode == 2)
                        puck.Speed = new Vector3(koeffizient * 15, -koeffizient2 * 15, 0);
                    if (startmode == 3)
                        puck.Speed = new Vector3(-koeffizient * 15, -koeffizient2 * 15, 0);
                }
            }

            // Kamerasteuerung
            if (state.Keys.IsDown(Keys.F2))
                puckcamera = 1;
            if (state.Keys.IsDown(Keys.F3))
                puckcamera = 0;

            if (puckcamera == 1)
            {
                LookAt = puck.Position;
            }
            else
            {
                LookAt = new Vector3(0, 0, 0);
            }

            #endregion

            #region Steuerung Spieler 1

            if (Spieler1.CurrentPosition.X + 2 < -Spielfeldlaenge / 8f)
            {
                if (state.Keys.IsDown(Keys.D))
                    Spieler1.CurrentPosition += new Vector3(0.5f, 0, 0);
            }
            if (Spieler1.CurrentPosition.X - 2 > -Spielfeldlaenge / 2f)
            {
                if (state.Keys.IsDown(Keys.A))
                    Spieler1.CurrentPosition += new Vector3(-0.5f, 0, 0);
            }

            if (Spieler1.CurrentPosition.Y + 2 < Spielfeldbreite / 2f)
            {
                if (state.Keys.IsDown(Keys.W))
                    Spieler1.CurrentPosition += new Vector3(0, 0.5f, 0);
            }

            if (Spieler1.CurrentPosition.Y - 2 > -Spielfeldbreite / 2f)
            {
                if (state.Keys.IsDown(Keys.S))
                    Spieler1.CurrentPosition += new Vector3(0, -0.5f, 0);
            }

            #endregion

            #region Steuerung Spieler 2

            if (Spieler2.CurrentPosition.X + 2 < Spielfeldlaenge / 2f)
            {
                if (state.Keys.IsDown(Keys.Right))
                    Spieler2.CurrentPosition += new Vector3(0.5f, 0, 0);
            }
            if (Spieler2.CurrentPosition.X - 2 > Spielfeldlaenge / 8f)
            {
                if (state.Keys.IsDown(Keys.Left))
                    Spieler2.CurrentPosition += new Vector3(-0.5f, 0, 0);
            }

            if (Spieler2.CurrentPosition.Y + 2 < Spielfeldbreite / 2f)
            {
                if (state.Keys.IsDown(Keys.Up))
                    Spieler2.CurrentPosition += new Vector3(0, 0.5f, 0);
            }

            if (Spieler2.CurrentPosition.Y - 2 > -Spielfeldbreite / 2f)
            {
                if (state.Keys.IsDown(Keys.Down))
                    Spieler2.CurrentPosition += new Vector3(0, -0.5f, 0);
            }

            #endregion

            #region Kamerasteuerung
            // Begrenzung der Kameraposition
            if ((Rotation.X < 0.78) && (Rotation.X > -0.4))
            {
                Rotation.X = (state.Mouse.DeltaY * 0.004f + Rotation.X) % 360;
            }
            Rotation.X = MathHelper.Clamp(Rotation.X, (float)-0.39999, (float)0.779999);

            //if ((Rotation.X < 1.78) && (Rotation.X > -1.4))
            //{
            //    Rotation.X = (state.Mouse.DeltaY * 0.004f + Rotation.X) % 360;
            //}
            //Rotation.X = MathHelper.Clamp(Rotation.X, (float)-1.39999, (float)1.779999);



            #endregion

            #endregion

            #region Beleuchtungsrechnung
            Beleuchtungsrechnung_Spielfeld();
            Beleuchtungsrechnung_Spieler(Spieler1);
            Beleuchtungsrechnung_Spieler(Spieler2);
            Beleuchtungsrechnung_Puck();
            #endregion

            #region HUDText
            text = "Grundlagen der Computergraphik - Programmieraufgabe 4 - 3D-Spiel - Airhockey \nAuthor: Markus Strobel\n";
            text += "\n### Steuerung ###";
            text += "\nSpieler 1: W|A|S|D                                                         Runde starten:";
            text += "\nSpieler 2: Up|Left|Down|Right                                              Space";
            text += "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nPunktestand: " + Punktestand_Spieler1.ToString() + " : " + Punktestand_Spieler2.ToString();
            text += "                        Puckkamera: F2             Standardkamera: F3      ";
            text += "      Spiel neustarten: F1         Spiel beenden: ESC";


            hudtext.Text = text;
            
            #endregion

            #region Spiellogik / Spielphysik

            if (puck.Position.Y > (Spielfeldbreite / 2f) - 0.9f)
                puck.Position = new Vector3(puck.Position.X, puck.Position.Y - 2, 0);
            if (puck.Position.Y < (-Spielfeldbreite / 2f) + 0.9f)
                puck.Position = new Vector3(puck.Position.X, puck.Position.Y + 2, 0);

            
            #region Spieler 1

            // Abstand zwischen Puck und Spieler 1 berechnen
            float X1m2_quadrat = (puck.Position.X - Spieler1.CurrentPosition.X) * (puck.Position.X - Spieler1.CurrentPosition.X);
            float Y1m2_quadrat = (puck.Position.Y - Spieler1.CurrentPosition.Y) * (puck.Position.Y - Spieler1.CurrentPosition.Y);
            float Z1m2_quadrat = (puck.Position.Z - Spieler1.CurrentPosition.Z) * (puck.Position.Z - Spieler1.CurrentPosition.Z);
            float Abstand_puck_spieler1 = (float)Math.Sqrt(X1m2_quadrat + Y1m2_quadrat + Z1m2_quadrat);
            
            // Länge des Geschwindigkeitsvektors berechnen, damit die Geschwindigkeit beim Richtungswechsel erhalten bleibt
            float puck_speed_length = (float)Math.Sqrt(puck.Speed.X * puck.Speed.X + puck.Speed.Y * puck.Speed.Y + puck.Speed.Z * puck.Speed.Z);


            // Hier wird ermittelt von welcher Richtung der Spieler auf den Puck trifft um so dem Puck die richtige Richtung zu geben.
            Vector3 Spieler1_richtung = (puck.Position - Spieler1.CurrentPosition);
            float laenge_Spieler1_richtung = (float)Math.Sqrt(Spieler1_richtung.X * Spieler1_richtung.X + Spieler1_richtung.Y * Spieler1_richtung.Y + Spieler1_richtung.Z * Spieler1_richtung.Z);
            Vector3 Spieler1_richtung_normalized = Spieler1_richtung / laenge_Spieler1_richtung;

            // Spieler1 <> Puck Bounce Check. Richtung und Geschwindigkeit wird an den Puck übergeben, sowie eine kleine 5% Erhöhrung der Geschwindigkeit.
            if (Abstand_puck_spieler1 < 3)
            {
                puck.Speed = Spieler1_richtung_normalized * (puck_speed_length * 1.02f);
            }
            #endregion

            #region Spieler 2

            // Abstand zwischen Puck und Spieler 1 berechnen
            X1m2_quadrat = (puck.Position.X - Spieler2.CurrentPosition.X) * (puck.Position.X - Spieler2.CurrentPosition.X);
            Y1m2_quadrat = (puck.Position.Y - Spieler2.CurrentPosition.Y) * (puck.Position.Y - Spieler2.CurrentPosition.Y);
            Z1m2_quadrat = (puck.Position.Z - Spieler2.CurrentPosition.Z) * (puck.Position.Z - Spieler2.CurrentPosition.Z);
            float Abstand_puck_spieler2 = (float)Math.Sqrt(X1m2_quadrat + Y1m2_quadrat + Z1m2_quadrat);

            // Länge des Geschwindigkeitsvektors berechnen, damit die Geschwindigkeit beim Richtungswechsel erhalten bleibt
            puck_speed_length = (float)Math.Sqrt(puck.Speed.X * puck.Speed.X + puck.Speed.Y * puck.Speed.Y + puck.Speed.Z * puck.Speed.Z);


            // Hier wird ermittelt von welcher Richtung der Spieler auf den Puck trifft um so dem Puck die richtige Richtung zu geben.
            Vector3 Spieler2_richtung = (puck.Position - Spieler2.CurrentPosition);
            float laenge_Spieler2_richtung = (float)Math.Sqrt(Spieler2_richtung.X * Spieler2_richtung.X + Spieler2_richtung.Y * Spieler2_richtung.Y + Spieler2_richtung.Z * Spieler2_richtung.Z);
            Vector3 Spieler2_richtung_normalized = Spieler2_richtung / laenge_Spieler2_richtung;

            // Spieler1 <> Puck Bounce Check. Richtung und Geschwindigkeit wird an den Puck übergeben, sowie eine kleine 5% Erhöhrung der Geschwindigkeit.
            if (Abstand_puck_spieler2 < 3)
            {
                puck.Speed = Spieler2_richtung_normalized * (puck_speed_length * 1.02f);
            }
            #endregion

            #region Puck
            puck.Position += puck.Speed * (float)gameTime.ElapsedGameTime.TotalSeconds;

            // Check for Bounce in Richtung X
            if ((puck.Position.X + 1 > Spielfeldlaenge / 2) || (puck.Position.X - 1 < -Spielfeldlaenge / 2))
            {
                puck.Speed = new Vector3(puck.Speed.X * -1, puck.Speed.Y, 0);
            }

            // Check for Bounce in Richtung Y
            if ((puck.Position.Y + 1 > Spielfeldbreite / 2) || (puck.Position.Y - 1 < -Spielfeldbreite / 2))
            {
                puck.Speed = new Vector3(puck.Speed.X, puck.Speed.Y * -1, 0);
            }

            #endregion

            #region Punktestand

            if (puck.Position.X - 1 <= -(Spielfeldlaenge / 2f))
            {
                Punktestand_Spieler2 += 1;
                puck.Speed = new Vector3(0, 0, 0);
                puck.Position = new Vector3(0, 0, 0);
            }

            if (puck.Position.X + 1 >= Spielfeldlaenge / 2f)
            {
                Punktestand_Spieler1 += 1;
                puck.Speed = new Vector3(0, 0, 0);
                puck.Position = new Vector3(0, 0, 0);
            }
            #endregion

            #endregion

            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);
            GraphicsDevice.RenderState.CullMode = CullMode.None; 
            graphics.GraphicsDevice.RenderState.FillMode = FillMode.Solid;
            graphics.GraphicsDevice.RenderState.PointSize = 5;
            BasicEffect fx = new BasicEffect(GraphicsDevice, null);
            fx.VertexColorEnabled = true;
            fx.TextureEnabled = true;

            fx.World = Matrix.CreateRotationX(Rotation.X) * Matrix.CreateRotationY(Rotation.Y);
            fx.View = Matrix.CreateLookAt(Position + LookAt, LookAt, Vector3.Up);
            fx.Projection = Matrix.CreatePerspectiveFieldOfView(MathHelper.ToRadians(45.0f), aspectRatio, 1.0f, 10000.0f);


            fx.Begin();
            foreach (EffectPass pass in fx.CurrentTechnique.Passes)
            {
                pass.Begin();

                field.Draw();

                puck.Draw();
                Spieler1.Draw();
                Spieler2.Draw();


                graphics.GraphicsDevice.RenderState.FillMode = FillMode.Solid;
                hudtext.Draw(gameTime);
                pass.End();
            }
            fx.End();

            base.Draw(gameTime);
        }

        public void Restart()
        {
            puck.Speed = new Vector3(0, 0, 0);
            puck.Position = new Vector3(0, 0, 0);
            Spieler1.CurrentPosition = new Vector3(-22, 0, 0);
            Spieler2.CurrentPosition = new Vector3(22, 0, 0);
            Punktestand_Spieler1 = 0;
            Punktestand_Spieler2 = 0;
        }

        private void Beleuchtungsrechnung_Spielfeld()
        {
            Material_diffuse = new Vector3(0f, 0.5f, 0.7f);
            Normale = new Vector3(0, 0, 1);
            int vertices = field.Buffer.Length - 1;

            // Ambienter Farbanteil
            Color_ambient = Light_ambient * Material_ambient;

            for (int i = 0; i <= vertices; i++)
            {
                Licht_direction = field.Buffer[i].Position - Lichtquelle;
                Camera_direction = field.Buffer[i].Position - Camera;

                // Diffuser Farbanteil
                float Skalarprodukt_NL = (Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z);
                float Betrag_NL = ((float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z));
                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);
                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;

                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = (Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z);
                float Betrag_CN = ((float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z));

                angle_CN = Skalarprodukt_CN / Betrag_CN;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;

                Color_specular = Light_specular * Material_specular * ((float)Math.Pow(angle_specular, shininess));

                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;

                field.Buffer[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z);

            }
        }

        private void Beleuchtungsrechnung_Spieler(Schlaeger spieler)
        {

            if (spieler == Spieler1)
                Material_diffuse = new Vector3(1f, 0f, 0f);
            if (spieler == Spieler2)
                Material_diffuse = new Vector3(0f, 0f, 1f);

            int vertices = spieler.Buffer2.Length - 1;
            int vertices2 = spieler.Buffer.Length - 1;

            // Ambienter Farbanteil
            Color_ambient = Light_ambient * Material_ambient;

            #region Zylinder"deckel"

            for (int i = 0; i <= vertices; i++)
            {
                
                Licht_direction = spieler.Buffer2[i].Position - Lichtquelle;
                Camera_direction = spieler.Buffer2[i].Position - Camera;
                Normale = new Vector3(0, 0, 1);



                // Diffuser Farbanteil
                float Skalarprodukt_NL = Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z;
                float Betrag_NL = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z);

                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);
                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;



                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z;
                float Betrag_CN = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z);

                angle_CN = Skalarprodukt_NL / Betrag_NL;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;

                Color_specular = Light_specular * Material_specular * angle_specular;



                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;
                spieler.Buffer2[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z);
            }


            #endregion

            #region Zylindermantel

            for (int i = 0; i <= vertices2; i++)
            {

                Licht_direction = spieler.Buffer[i].Position - Lichtquelle;
                Camera_direction = spieler.Buffer[i].Position - Camera;

                float NormaleX = spieler.Buffer[i].Position.X - Spieler1.CurrentPosition.X;
                float NormaleY = spieler.Buffer[i].Position.Y - Spieler1.CurrentPosition.Y;

                float laenge_normale = (float)Math.Sqrt(NormaleX * NormaleX + NormaleY * NormaleY);
                Normale = new Vector3(NormaleX, NormaleY, 0);


                // Diffuser Farbanteil
                float Skalarprodukt_NL = Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z;
                float Betrag_NL = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z);

                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);
                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;



                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z;
                float Betrag_CN = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z);

                angle_CN = Skalarprodukt_NL / Betrag_NL;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;

                Color_specular = Light_specular * Material_specular * angle_specular;



                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;
                spieler.Buffer[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z);
            }


            #endregion



















        }

        private void Beleuchtungsrechnung_Puck()
        {
            Material_diffuse = new Vector3(1f, 1f, 1f);

            int vertices = puck.Buffer2.Length - 1;
            int vertices2 = puck.Buffer.Length - 1;

            // Ambienter Farbanteil
            Color_ambient = Light_ambient * Material_ambient;

            #region Zylinder"deckel"

            for (int i = 0; i <= vertices; i++)
            {

                Licht_direction = puck.Buffer2[i].Position - Lichtquelle;
                Camera_direction = puck.Buffer2[i].Position - Camera;
                Normale = new Vector3(0, 0, 1);



                // Diffuser Farbanteil
                float Skalarprodukt_NL = Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z;
                float Betrag_NL = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z);

                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);
                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;


                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z;
                float Betrag_CN = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z);

                angle_CN = Skalarprodukt_NL / Betrag_NL;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;

                Color_specular = Light_specular * Material_specular * angle_specular;


                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;
                puck.Buffer2[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z);
            }


            #endregion

            #region Zylindermantel

            for (int i = 0; i <= vertices2; i++)
            {

                Licht_direction = puck.Buffer[i].Position - Lichtquelle;
                Camera_direction = puck.Buffer[i].Position - Camera;

                float NormaleX = puck.Buffer[i].Position.X - Spieler1.CurrentPosition.X;
                float NormaleY = puck.Buffer[i].Position.Y - Spieler1.CurrentPosition.Y;

                float laenge_normale = (float)Math.Sqrt(NormaleX * NormaleX + NormaleY * NormaleY);
                Normale = new Vector3(NormaleX, NormaleY, 0);


                // Diffuser Farbanteil
                float Skalarprodukt_NL = Normale.X * Licht_direction.X + Normale.Y * Licht_direction.Y + Normale.Z * Licht_direction.Z;
                float Betrag_NL = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Licht_direction.X * Licht_direction.X + Licht_direction.Y * Licht_direction.Y + Licht_direction.Z * Licht_direction.Z);

                angle_diffuse = Skalarprodukt_NL / Betrag_NL;

                if (angle_diffuse < 0)
                {
                    angle_diffuse = angle_diffuse * (-1);
                }

                Color_diffuse = Light_diffuse * Material_diffuse * angle_diffuse;



                // Spiegelnder Farbanteil
                float Skalarprodukt_CN = Normale.X * Camera_direction.X + Normale.Y * Camera_direction.Y + Normale.Z * Camera_direction.Z;
                float Betrag_CN = (float)Math.Sqrt(Normale.X * Normale.X + Normale.Y * Normale.Y + Normale.Z * Normale.Z) * (float)Math.Sqrt(Camera_direction.X * Camera_direction.X + Camera_direction.Y * Camera_direction.Y + Camera_direction.Z * Camera_direction.Z);

                angle_CN = Skalarprodukt_NL / Betrag_NL;

                if (angle_CN < 0)
                {
                    angle_CN = angle_CN * (-1);
                }
                angle_specular = angle_diffuse - angle_CN;

                Color_specular = Light_specular * Material_specular * angle_specular;



                // Gesamtfarbe
                Color_iluminated = Color_ambient + Color_diffuse + Color_specular;
                puck.Buffer[i].Color = new Color(Color_iluminated.X, Color_iluminated.Y, Color_iluminated.Z);
            }


            #endregion

        }


    }
}
 