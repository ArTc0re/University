using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using FarbRechner;

namespace FarbRechner.FarbSysteme
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand</author>
    public class sRGB
    {
        public float R { get; set; }
        public float G { get; set; }
        public float B { get; set; }

        public sRGB() : this(0, 0, 0) { }
        public sRGB(int R, int G, int B) : this((float)R, (float)G, (float)B) { }
        public sRGB(float Red, float Green, float Blue)
        {
            this.R = Red;
            this.G = Green;
            this.B = Blue;
        }



        ~sRGB()
        {

        }


        /// <summary>
        /// this is the inverse function to applying the sRGB gamma correction to a single color value in RGB
        /// (it is the same for R, G and B)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the un-gamma-corrected color value</returns>
        public static float Reverse_Gamma_Correction(float Color) // reverses the gamma correction that has been applied to Color
        {
            float temp = new float();

            if (Color <= 0.04045f)
            {
                temp = Color / 12.92f;
            }
            else
            {
                temp = (float)Math.Pow(((Color + 0.055f) / 1.055f), 2.4f);
            }

            return temp;
        }


        /// <summary>
        /// this returns an sRGB gamma corrected RGB value to its non-corrected self
        /// (of course, if used on a non-corrected value, it will still give an output)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the RGB value without gamma correction</returns>
        public RGB asRGB()
        {
            RGB temp = new RGB();

            // this inverts the gamma correction, as given on http://en.wikipedia.org/wiki/SRGB
            temp.R = Reverse_Gamma_Correction(this.R);
            temp.G = Reverse_Gamma_Correction(this.G);
            temp.B = Reverse_Gamma_Correction(this.B);

            return temp;
        }


    }
}
