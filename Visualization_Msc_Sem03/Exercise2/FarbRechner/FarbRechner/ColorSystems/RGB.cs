using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OpenTK;
using FarbRechner;
using System.Globalization;

namespace FarbRechner.FarbSysteme
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand, Markus Strobel</author>
    public class RGB // this is the linear rgb with D65 whitepoint and sRGB primary colors but without gamma correction. only used for internal calculations.
    {
        public float R { get; set; }
        public float G { get; set; }
        public float B { get; set; }

        public RGB() : this(0, 0, 0) { }
        public RGB(int R, int G, int B) : this((float)R, (float)G, (float)B) { }
        public RGB(float Red, float Green, float Blue)
        {
            this.R = Red;
            this.G = Green;
            this.B = Blue;
        }

        ~RGB()
        {

        }



        /// <summary>
        /// this checks if an RGB value is a correct input, meaning if R, G and B are between 0 and 1
        /// as we decided to not use the 0-255 scale.
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>true if legit input, false otherwise</returns>
        public static bool checkInputValidity(string input)
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
        /// transformatin RGB -> HSV
        /// source: http://en.wikipedia.org/wiki/HSL_and_HSV (consistent with anything found on the web)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the HSV value corresponding the the RGB value the method has been used on</returns>
        public HSV asHSV()
        {
            HSV temp = new HSV();

            this.R = (float)Math.Round(this.R, 3); // um zu vermeiden, dass sich rundungsfehler aufaddieren und zu großen fehlern führen
            this.G = (float)Math.Round(this.G, 3);
            this.B = (float)Math.Round(this.B, 3);

            this.R = Math.Min(1f, this.R);
            this.G = Math.Min(1f, this.G);
            this.B = Math.Min(1f, this.B);

            float max = Math.Max(this.R, Math.Max(this.G, this.B));
            float min = Math.Min(this.R, Math.Min(this.G, this.B));
            float C = max - min;

            if (max == min)
            {
                temp.H = 0f;
            }
            else if (max == this.R)
            {
                temp.H = (this.G - this.B) / C;
            }
            else if (max == this.G)
            {
                temp.H = 2f + (this.B - this.R) / C;
            }
            else if (max == this.B)
            {
                temp.H = 4f + (this.R - this.G) / C;
            }
            else
            {
#if DEBUG
                System.Diagnostics.Debug.Write("RGB to HSV out of range");
#endif
            }

            temp.H *= 60f;

            if (temp.H < 0f)
            {
                temp.H += 360f;
            }

            if (max == 0f)
            {
                temp.S = 0f;
            }
            else
            {
                temp.S = C / max;
            }
            temp.V = max;

            return temp;
        }




        /// <summary>
        /// transformatin RGB -> HSL
        /// source: http://en.wikipedia.org/wiki/HSL_and_HSV (consistent with anything found on the web)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the HSL value corresponding the the RGB value the method has been used on</returns>
        public HSL asHSL()
        {
            HSL temp = new HSL();

            this.R = (float)Math.Round(this.R, 2); // um zu vermeiden, dass sich rundungsfehler aufaddieren und zu großen fehlern führen
            this.G = (float)Math.Round(this.G, 2);
            this.B = (float)Math.Round(this.B, 2);

            this.R = Math.Min(1f, this.R);
            this.G = Math.Min(1f, this.G);
            this.B = Math.Min(1f, this.B);

            float max = Math.Max(this.R, Math.Max(this.G, this.B));
            float min = Math.Min(this.R, Math.Min(this.G, this.B));
            float C = max - min;

            if (max == min)
            {
                temp.H = 0f;
            }
            else if (max == this.R)
            {
                temp.H = (this.G - this.B) / C;
            }
            else if (max == this.G)
            {
                temp.H = 2f + (this.B - this.R) / C;
            }
            else if (max == this.B)
            {
                temp.H = 4f + (this.R - this.G) / C;
            }
            else
            {
#if DEBUG
                System.Diagnostics.Debug.Write("RGB to HSL out of range");
#endif
            }

            temp.H *= 60f;

            if (temp.H < 0f)
            {
                temp.H += 360f;
            }

            if (max == 0f || min == 1)
            {
                temp.S = 0f;
            }
            else
            {
                temp.S = C / (1 - Math.Abs(max + min - 1));
            }

            temp.L = (max + min) / 2;

            return temp;
        }


        /// <summary>
        /// transformatin linear RGB -> CIE XYZ with the d65 white point and the sRGB primary colors,
        /// with a separately saved matrix as this is our main RGB representation
        /// used for the visualization of the gamut and HSL/HSV calculation
        /// source: http://en.wikipedia.org/wiki/CIE_1931_color_space (consistent with anything found on the web and in the library)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>the XYZ value correcponding to the given RGB value</returns>
        public XYZ asXYZ()
        {
            XYZ temp = new XYZ();

            // transformation linear RGB -> CIE XYZ
            Vector3 Vector_linRGB = new Vector3(this.R, this.G, this.B);
            Vector3 Vector_temp = ColorHelper.Multiply_Mat3_Vec3(ColorHelper.Matrix_linRGB_to_XYZ, Vector_linRGB);
            temp.X = Vector_temp[0];
            temp.Y = Vector_temp[1];
            temp.Z = Vector_temp[2];

            return temp;
        }


        /// <summary>
        /// this applies the standard (sRGB) gamma correction to a single color value (R, G or B)
        /// source: http://en.wikipedia.org/wiki/SRGB
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>gamma corrected color value (as float)</returns>
        public static float Apply_Gamma_Correction(float Color)
        {
            float temp = new float();

            if (Color <= 0.0031308f)
            {
                temp = Color * 12.92f;
            }
            else
            {
                temp = 1.055f * (float)Math.Pow(Color, (1f / 2.4f)) - 0.055f;
            }

            return temp;
        }


        /// <summary>
        /// this applies the sRGB gamma correction to an RGB value
        /// source: http://en.wikipedia.org/wiki/SRGB
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>gamma corrected (s)RGB value</returns>
        public sRGB as_sRGB() // yes, the "_" is inconsistent. it still makes sense there, though.
        {
            sRGB temp = new sRGB();

            temp.R = Math.Max(0f, Math.Min(1f, Apply_Gamma_Correction(this.R)));
            temp.G = Math.Max(0f, Math.Min(1f, Apply_Gamma_Correction(this.G)));
            temp.B = Math.Max(0f, Math.Min(1f, Apply_Gamma_Correction(this.B)));

            return temp;
        }



        public string asHEX()
        {
            string temp = this.R.ToString("X") + this.G.ToString("X") + this.B.ToString("X");
            return temp;
        }

    }
}
