using OpenTK;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;

namespace FarbRechner.FarbSysteme
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand, Markus Strobel</author>
    public class HSL
    {
        public float H { get; set; }
        public float S { get; set; }
        public float L { get; set; }

        public HSL() : this(0, 0, 0) { }
        public HSL(Vector3 HSL) : this(HSL.X, HSL.Y, HSL.Z) { }
        /// <summary>
        /// The HSL Colorsystem
        /// </summary>
        /// <param name="H">0...360</param>
        /// <param name="S">0...1</param>
        /// <param name="L">0...1</param>
        public HSL(float H, float S, float L)
        {
            this.H = H;
            this.S = S;
            this.L = L;
        }

        ~HSL()
        {
        }


        /// <summary>
        /// this checks if the H value (as in HSL) is a correct input, meaning if it is between 0 and 360.
        /// also used for HSV, as they have the same scope.
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>true if legit input, false otherwise</returns>
        public static bool checkInputValidity_H(string input)
        {
            try
            {
                float parsedInput = float.Parse(input);

                if (parsedInput > 360f || parsedInput < 0f)
                {
                    return false;
                }
            }
            catch
            {
#if DEBUG
                System.Diagnostics.Debug.WriteLine("Input parsing failed!");
#endif
                return false;
            }
            return true;
        }


        /// <summary>
        /// this checks if the S or L value (as in HSL) is a correct input, meaning if it is between 0 and 1.
        /// also used for HSV, as they have the same scope.
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>true if legit input, false otherwise</returns>
        public static bool checkInputValidity_SL(string input)
        {
            try
            {
                float parsedInput = float.Parse(input, CultureInfo.InvariantCulture);

                if (parsedInput > 1f || parsedInput < 0f)
                {
                    return false;
                }
            }
            catch
            {
#if DEBUG
                System.Diagnostics.Debug.WriteLine("Input parsing failed!");
#endif
                return false;
            }
            return true;
        }


        /// <summary>
        /// transformation HSL -> RGB
        /// source: http://www.rapidtables.com/convert/color/hsl-to-rgb.htm
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the RGB value</returns>
        public RGB asRGB()
        {
            RGB temp = new RGB();

            float C = (1f - Math.Abs(2f * this.L - 1f)) * this.S;
            float X = C * (1f - Math.Abs((this.H / 60f) % 2f - 1f));
            float m = this.L - C / 2f;

            if (0f <= this.H && this.H < 60f)
            {
                temp.R = C;
                temp.G = X;
                temp.B = 0;
            }
            else if (60f <= this.H && this.H < 120f)
            {
                temp.R = X;
                temp.G = C;
                temp.B = 0;
            }
            else if (120f <= this.H && this.H < 180f)
            {
                temp.R = 0;
                temp.G = C;
                temp.B = X;
            }
            else if (180f <= this.H && this.H < 240f)
            {
                temp.R = 0;
                temp.G = X;
                temp.B = C;
            }
            else if (240f <= this.H && this.H < 300f)
            {
                temp.R = X;
                temp.G = 0;
                temp.B = C;
            }
            else if (300f <= this.H && this.H < 360f)
            {
                temp.R = C;
                temp.G = 0;
                temp.B = X;
            }
#if DEBUG
            else
            {
                System.Diagnostics.Debug.Write("HSL to RGB out of range");
            }
#endif
            temp.R += m;
            temp.G += m;
            temp.B += m;

            return temp;
        }




    }
}
