using OpenTK;
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
    /// <author>Birthe Anne Wiegand, Markus Strobel</author>
    public class XYZ
    {
        public float X { get; set; }
        public float Y { get; set; }
        public float Z { get; set; }

        public XYZ() : this(0, 0, 0) { }

        public XYZ(Vector3 XYZ) : this(XYZ.X, XYZ.Y, XYZ.Z) { }
        public XYZ(float X, float Y, float Z)
        {
            this.X = X;
            this.Y = Y;
            this.Z = Z;
        }

        ~XYZ()
        {
        }


        /// <summary>
        /// this checks if an XYZ value is non-negative
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>true if > 0, false otherwise</returns>
        public static bool checkInputValidity(string input)
        {
            try
            {
                float parsedInput = float.Parse(input, System.Globalization.CultureInfo.InvariantCulture);

                // check if XYZ input is non-negative
                if (parsedInput < 0f)
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
        /// transformation CIE XYZ -> linear (s)RGB (but without gamma correction)
        /// the matrix is statically saved, not calculated every time, to minimize calculation errors
        /// as this becomes the color also used for HSL/HSV calculation and the gamut visualisation
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>RGB value</returns>
        public RGB asRGB()
        {
            RGB temp = new RGB();

            Vector3 Vector_XYZ = new Vector3(this.X, this.Y, this.Z);
            Vector3 Vector_temp = ColorHelper.Multiply_Mat3_Vec3(ColorHelper.Matrix_XYZ_to_linRGB, Vector_XYZ);
            temp.R = Math.Min(1f, Vector_temp[0]);
            temp.G = Math.Min(1f, Vector_temp[1]);
            temp.B = Math.Min(1f, Vector_temp[2]);

            return temp;
        }


        /// <summary>
        /// transformation CIE XYZ -> custom RGB with given white point and primary colors
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>custom RGB value</returns>
        public customRGB as_customRGB()
        {
            customRGB temp = new customRGB();
            ColorHelper.TranformationMatrices_Update();

            Vector3 Vector_XYZ = new Vector3(this.X, this.Y, this.Z);
            Vector3 Vector_temp = ColorHelper.Multiply_Mat3_Vec3(ColorHelper.XYZtoRGBTransformation, Vector_XYZ);
            temp.R = Math.Max(0f, Math.Min(1f, Vector_temp[0]));
            temp.G = Math.Max(0f, Math.Min(1f, Vector_temp[1]));
            temp.B = Math.Max(0f, Math.Min(1f, Vector_temp[2]));

            return temp;
        }


        /// <summary>
        /// transformatin CIE XYZ -> Yxy
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>Yxy value</returns>
        public Yxy asYxy()
        {
            Yxy temp = new Yxy();

            // transformation CIE XYZ -> Yxy (luminance Y = 1
            temp.Y = 1f;
            temp.x = this.X / (this.X + this.Y + this.Z);
            temp.y = this.Y / (this.X + this.Y + this.Z);

            return temp;
        }



        // if no white point is given, asLUV uses the default one
        public LUV asLUV()
        {
            return this.asLUV(ColorHelper.WP_default);
        }


        /// <summary>
        /// transformatin CIE XYZ -> CIE L*a*b*
        /// source: http://en.wikipedia.org/wiki/CIELUV
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>L*u*v* value</returns>
        public LUV asLUV(XYZ WP)
        {
            LUV temp = new LUV();

            float XXn = this.X / WP.X; // X / Xn
            float YYn = this.Y / WP.Y; // Y / Yn
            float ZZn = this.Z / WP.Z; // Z / Zn

            if (YYn <= 0.008856452)
            {
                temp.L = 903.2962963f * YYn;
            }
            else
            {
                temp.L = 116f * (float)Math.Pow(YYn, (1f / 3f)) - 16;
            }
            temp.U = 13f * temp.L * (ColorHelper.function_XYZ_to_LUV(this)[0] - ColorHelper.function_XYZ_to_LUV(WP)[0]);
            temp.V = 13f * temp.L * (ColorHelper.function_XYZ_to_LUV(this)[1] - ColorHelper.function_XYZ_to_LUV(WP)[1]);

            // to catch the otherwise occuring exception for (input.X + 15f * input.Y + 3f * input.Z) = 0
            // (ColorHelper.function_XYZ_to_LUV divides by zero if (input.X + 15f * input.Y + 3f * input.Z) = 0)
            // u* and v* are given the value 0 just for interface clarity as there is no information given about them.
            // (so 0 is as good as any other value, but does not imply anything wrong)
            if (float.IsNaN(temp.U)) temp.U = 0f;
            if (float.IsNaN(temp.V)) temp.V = 0f;

            return temp;
        }


        // if no white point is given, asLAB uses the default one
        public LAB asLAB()
        {
            return this.asLAB(ColorHelper.WP_default);
        }


        /// <summary>
        /// transformatin CIE XYZ -> CIE L*u*v*
        /// source: http://www.farbmetrik-gall.de/cielab/index.html, http://en.wikipedia.org/wiki/Lab_color_space
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>L*a*b* value</returns>
        public LAB asLAB(XYZ WP)
        {
            LAB temp = new LAB();

            float XXn = this.X / WP.X; // X / Xn
            float YYn = this.Y / WP.Y; // Y / Yn
            float ZZn = this.Z / WP.Z; // Z / Zn

            temp.L = 116f * function_XYZ_to_LAB(YYn) - 16f;
            temp.A = 500f * (function_XYZ_to_LAB(XXn) - function_XYZ_to_LAB(YYn));
            temp.B = 200f * (function_XYZ_to_LAB(YYn) - function_XYZ_to_LAB(ZZn));

            return temp;
        }

        // calculation needed for the CIE XYZ -> CIE L*a*b* transformation
        private float function_XYZ_to_LAB(float input)
        {

            float temp = new float();
            if (input > 0.00885645f)
            {
                temp = (float)Math.Pow(input, (1f / 3f));
            }
            else
            {
                temp = 7.787f * input + 0.137931f;
            }

            return temp;
        }
    }
}
