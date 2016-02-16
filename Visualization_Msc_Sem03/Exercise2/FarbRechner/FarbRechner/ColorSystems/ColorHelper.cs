using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OpenTK;
using FarbRechner;
using FarbRechner.FarbSysteme;

namespace FarbRechner
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand, Markus Strobel</author>
    public class ColorHelper
    {
        #region member variables

        // D65 sRGB Values
        public static XYZ RR_default = new XYZ(0.64f, 0.33f, 0.03f);
        public static XYZ GG_default = new XYZ(0.30f, 0.60f, 0.10f);
        public static XYZ BB_default = new XYZ(0.15f, 0.06f, 0.79f);
        public static XYZ WP_default = new XYZ(0.95045596f, 1f, 1.0890577f);

        public static XYZ RR_used = new XYZ(0.64f, 0.33f, 0.03f);
        public static XYZ GG_used = new XYZ(0.30f, 0.60f, 0.10f);
        public static XYZ BB_used = new XYZ(0.15f, 0.06f, 0.79f);
        public static XYZ WP_used = new XYZ(0.95045596f, 1f, 1.0890577f);

        public static Matrix3 XYZtoRGBTransformation = new Matrix3(0, 0, 0, 0, 0, 0, 0, 0, 0);
        public static Matrix3 RGBtoXYZTransformation = new Matrix3(0, 0, 0, 0, 0, 0, 0, 0, 0);
              
        public static Matrix3 Matrix_XYZ_to_linRGB = new Matrix3(3.2406f, -1.5372f, -0.4986f, -0.9689f, 1.8758f, 0.0415f, 0.0557f, -0.2040f, 1.0570f);
        public static Matrix3 Matrix_linRGB_to_XYZ = new Matrix3(0.4124f, 0.3576f, 0.1805f, 0.2126f, 0.7152f, 0.0722f, 0.0193f, 0.1192f, 0.9505f);

        #endregion member variables


        #region Ctors & Dtors

        public ColorHelper()
        {
            setInitValues();
        }

        ~ColorHelper() { }

        #endregion Ctors & Dtors



        #region private methods


        /// <summary>
        /// (re-)sets (to) init values
        /// </summary>
        /// <author>Markus Strobel</author>
        private void setInitValues()
        {
            WP_default = WP_used = new XYZ(0.95045596f, 1f, 1.0890577f);
            RR_default = RR_used = new XYZ(0.64f, 0.33f, 0.03f);
            GG_default = GG_used = new XYZ(0.30f, 0.60f, 0.10f);
            BB_default = BB_used = new XYZ(0.15f, 0.06f, 0.79f);

            TranformationMatrices_Update();
        }


        /// <summary>
        /// This method is required to compute the transformation matrix to convert RGB to XYZ in dependency of the used white point and RR, GG, BB points
        /// </summary>
        /// <author>Markus Strobel</author>
        /// <returns>returns the 3x3 Matrix for Compute RGB to XYZ</returns>
        private static Matrix3 GetRGBtoXYZTransformationMatrix()
        {
            // Quelle: http://www.ryanjuckett.com/programming/rgb-color-space-conversion/

            Vector3 rr = new Vector3(RR_used.X, RR_used.Y, RR_used.Z);
            Vector3 gg = new Vector3(GG_used.X, GG_used.Y, GG_used.Z);
            Vector3 bb = new Vector3(BB_used.X, BB_used.Y, BB_used.Z);

            // Rx Gx Bx
            // Ry Gy By
            // Rz Gz Bz
            Matrix3 M = new Matrix3(rr, gg, bb);
            M.Transpose(); // M

            Matrix3 MInv = M.Inverted(); // M^-1

            //Vector3 WP_XYZ_used = new Vector3(WP_used.X / WP_used.Y, 1, WP_used.Z / WP_used.Y); // WP_xyz to WP_XYZ
            Vector3 WP_XYZ_used = new Vector3(WP_used.X, WP_used.Y, WP_used.Z);

            WP_XYZ_used.X = Reverse_Gamma_Correction(WP_XYZ_used.X);
            WP_XYZ_used.Y = Reverse_Gamma_Correction(WP_XYZ_used.Y);
            WP_XYZ_used.Z = Reverse_Gamma_Correction(WP_XYZ_used.Z);

            Vector3 temp = new Vector3();
            temp.X = MInv.M11 * WP_XYZ_used.X + MInv.M12 * WP_XYZ_used.Y + MInv.M13 * WP_XYZ_used.Z;
            temp.Y = MInv.M21 * WP_XYZ_used.X + MInv.M22 * WP_XYZ_used.Y + MInv.M23 * WP_XYZ_used.Z;
            temp.Z = MInv.M31 * WP_XYZ_used.X + MInv.M32 * WP_XYZ_used.Y + MInv.M33 * WP_XYZ_used.Z;

            M = Matrix3.Mult(M, new Matrix3(temp.X, 0, 0, 0, temp.Y, 0, 0, 0, temp.Z));

            return M;
        }


        #endregion private methods

        

        #region public methods


        public static void TranformationMatrices_Update()
        {
            RGBtoXYZTransformation = GetRGBtoXYZTransformationMatrix();
            XYZtoRGBTransformation = RGBtoXYZTransformation.Inverted();
        }


        #endregion public methods


        
        #region public static methods

        /// <summary>
        /// multiplies a 3x3 matrix by a 3 vector (in this order). source: maths.
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>Matrix_In * Vector_In</returns>
        public static Vector3 Multiply_Mat3_Vec3(Matrix3 Matrix_In, Vector3 Vector_In) // this is here because i didnt find a built-in one after 3mins of google-ing, which made writing one myself faster
        {
            Vector3 Vector_Out = new Vector3();

            Vector_Out[0] = Matrix_In.M11 * Vector_In[0] + Matrix_In.M12 * Vector_In[1] + Matrix_In.M13 * Vector_In[2];
            Vector_Out[1] = Matrix_In.M21 * Vector_In[0] + Matrix_In.M22 * Vector_In[1] + Matrix_In.M23 * Vector_In[2];
            Vector_Out[2] = Matrix_In.M31 * Vector_In[0] + Matrix_In.M32 * Vector_In[1] + Matrix_In.M33 * Vector_In[2];

            return Vector_Out;
        }

        public static float Apply_Gamma_Correction(float Color) // applies the gamma correction to a color value Color
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
        /// this calclates the u' and v' values for a given XYZ value
        /// outsourced because it is used by more than one color class (mostly for L*u*v*, hence the name)
        /// source: http://en.wikipedia.org/wiki/CIELUV (checked with various google results, this was the most consistent one)
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>a vector with u' in its first argument ([0]) and v' in the second ([1]), both floats</returns>
        public static Vector2 function_XYZ_to_LUV(XYZ input)
        {
            // calculates the u' and v' values for a given XYZ value
            // (needed for the transformation CIE XYZ -> CIE L*u*v* and back)
            // [0] gives the u' value and [1] the v' value
            Vector2 temp = new Vector2();
            temp[0] = (4f * input.X) / (input.X + 15f * input.Y + 3f * input.Z);
            temp[1] = (9f * input.Y) / (input.X + 15f * input.Y + 3f * input.Z);
            return temp;
        }


        public static float Chromatic_Difference(XYZ input1, XYZ input2)
        {
            // returns the chromatic difference of two colors given in the XYZ format
            // using the euclidean distance on the u' and v' values
            // (e.g. needed for the just noticeable difference (JND) calculation)
            // source: http://files.cie.co.at/738_CIE_TN_001-2014.pdf

            float temp = new float();
            Vector2 input1_vector = new Vector2();
            Vector2 input2_vector = new Vector2();

            input1_vector = function_XYZ_to_LUV(input1);
            input2_vector = function_XYZ_to_LUV(input2);

            temp = (float)Math.Sqrt((float)Math.Pow((input2_vector[0] - input1_vector[0]), 2f) + (float)Math.Pow((input2_vector[1] - input1_vector[1]), 2f));

            return temp;
        }

        #endregion public static methods

        

    }
}
