using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OpenTK;
using FarbRechner;
using FarbRechner.FarbSysteme;

namespace FarbRechner
{
    public static class Utility
    {
        #region public methods

        public static Vector3 Multiply_Mat3_Vec3(Matrix3 Matrix_In, Vector3 Vector_In) // this is here because i didnt find a built-in one after 3mins of google-ing, which made writing one myself faster
        {
            Vector3 Vector_Out = new Vector3();

            Vector_Out[0] = Matrix_In.M11 * Vector_In[0] + Matrix_In.M12 * Vector_In[1] + Matrix_In.M13 * Vector_In[2];
            Vector_Out[1] = Matrix_In.M21 * Vector_In[0] + Matrix_In.M22 * Vector_In[1] + Matrix_In.M23 * Vector_In[2];
            Vector_Out[2] = Matrix_In.M31 * Vector_In[0] + Matrix_In.M32 * Vector_In[1] + Matrix_In.M33 * Vector_In[2];

            return Vector_Out;
        }

        public static Matrix3 Matrix_XYZ_to_linRGB = new Matrix3(3.2406f, -1.5372f, -0.4986f, -0.9689f, 1.8758f, 0.0415f, 0.0557f, -0.2040f, 1.0570f);

        public static Matrix3 Matrix_linRGB_to_XYZ = new Matrix3(0.4124f, 0.3576f, 0.1805f, 0.2126f, 0.7152f, 0.0722f, 0.0193f, 0.1192f, 0.9505f);

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

        //public static Vector3 RR_default = new Vector3(0.64f, 0.33f, 0.03f);
        //public static Vector3 GG_default = new Vector3(0.30f, 0.60f, 0.10f);
        //public static Vector3 BB_default = new Vector3(0.15f, 0.06f, 0.79f);
        //public static Vector3 WP_default = new Vector3(0.3127f, 0.3290f, 0.3583f);


        public static XYZ RR_default = new XYZ(0.64f, 0.33f, 0.03f);
        public static XYZ GG_default = new XYZ(0.30f, 0.60f, 0.10f);
        public static XYZ BB_default = new XYZ(0.15f, 0.06f, 0.79f);
        //public static XYZ WP_default = new XYZ(0.3127f, 0.3290f, 0.3583f);
        public static XYZ WP_default = new XYZ(0.95045596f, 1f, 1.0890577f);



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


        #endregion public methods

    }

}
