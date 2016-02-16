using OpenTK;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace FarbRechner.FarbSysteme
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand, Markus Strobel</author>
    public class HSV
    {
        public float H { get; set; }
        public float S { get; set; }
        public float V { get; set; }

        public HSV() : this(0, 0, 0) { }
        public HSV(Vector3 HSV) : this(HSV.X, HSV.Y, HSV.Z) { }
        /// <summary>
        /// The HSV Colorsystem
        /// </summary>
        /// <param name="H">0...360</param>
        /// <param name="S">0...1</param>
        /// <param name="V">0...1</param>
        public HSV(float H, float S, float V)
        {
            this.H = H;
            this.S = S;
            this.V = V;
        }

        ~HSV()
        {
        }


        /// <summary>
        /// transformation HSV -> RGB
        /// source: http://en.wikipedia.org/wiki/HSL_and_HSV (consistent with anything found on the web)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the RGB value</returns>
        public RGB asRGB()
        {
            RGB temp = new RGB();

            float f = this.H / 60f - (float)Math.Floor(this.H / 60f);
            float p = this.V * (1f - this.S);
            float q = this.V * (1f - this.S * f);
            float t = this.V * (1f - this.S * (1 - f));


            if (0f <= this.H && this.H < 60f)
            {
                temp.R = this.V;
                temp.G = t;
                temp.B = p;
            }
            else if (60f <= this.H && this.H < 120f)
            {
                temp.R = q;
                temp.G = this.V;
                temp.B = p;
            }
            else if (120f <= this.H && this.H < 180f)
            {
                temp.R = p;
                temp.G = this.V;
                temp.B = t;
            }
            else if (180f <= this.H && this.H < 240f)
            {
                temp.R = p;
                temp.G = q;
                temp.B = this.V;
            }
            else if (240f <= this.H && this.H < 300f)
            {
                temp.R = t;
                temp.G = p;
                temp.B = this.V;
            }
            else if (300f <= this.H && this.H < 360f)
            {
                temp.R = this.V;
                temp.G = p;
                temp.B = q;
            }
            else
            {
                System.Diagnostics.Debug.Write("HSV to RGB out of range");
            }


            return temp;
        }







    }
}
