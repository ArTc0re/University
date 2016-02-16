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
    public class LUV
    {
        public float L { get; set; }
        public float U { get; set; }
        public float V { get; set; }

        public LUV() : this(0, 0, 0) { }
        public LUV(Vector3 LUV) : this(LUV.X, LUV.Y, LUV.Z) { }
        public LUV(float L, float U, float V)
        {
            this.L = L;
            this.U = U;
            this.V = V;
        }

        ~LUV()
        {
        }


        // if no white point is given, asXYZ uses the default one
        public XYZ asXYZ()
        {
            return this.asXYZ(ColorHelper.WP_default);
        }

        /// <summary>
        /// transformatin CIE L*u*v* -> CIE XYZ
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>XYZ value</returns>
        public XYZ asXYZ(XYZ WP)
        {
            XYZ temp = new XYZ();
            float temp_u = new float();
            float temp_v = new float();

            temp_u = this.U / (13f * this.L) + ColorHelper.function_XYZ_to_LUV(WP)[0];
            temp_v = this.V / (13f * this.L) + ColorHelper.function_XYZ_to_LUV(WP)[1];

            if (this.L <= 8)
            {
                temp.Y = WP.Y * this.L * 0.001107056f;
            }
            else
            {
                temp.Y = WP.Y * (float)Math.Pow(((this.L + 16f) / 116f), 3f);
            }
            temp.X = temp.Y * 9f * temp_u / (4f * temp_v);
            temp.Z = temp.Y * (12f - 3f * temp_u - 20f * temp_v) / (4f * temp_v);

            // to catch the otherwise occuring exception for L = 0 (black -> no color information)
            // X and Z are given the value 0 just for interface clarity as there is no information given about them.
            // (so 0 is as good as any other value, but does not imply anything wrong)
            if (float.IsNaN(temp.X)) temp.X = 0f;
            if (float.IsNaN(temp.Z)) temp.Z = 0f;

            return temp;
        }
    }
}
