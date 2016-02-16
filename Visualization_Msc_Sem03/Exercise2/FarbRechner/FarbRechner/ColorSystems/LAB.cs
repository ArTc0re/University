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
    public class LAB
    {
        public float L { get; set; }
        public float A { get; set; }
        public float B { get; set; }

        public LAB() : this(0, 0, 0) { }
        public LAB(Vector3 LAB) : this(LAB.X, LAB.Y, LAB.Z) { }
        public LAB(float L, float A, float B)
        {
            this.L = L;
            this.A = A;
            this.B = B;
        }


        ~LAB()
        {
        }


        // if no white point is given, asXYZ uses the default one
        public XYZ asXYZ()
        {
            return this.asXYZ(ColorHelper.WP_default);
        }


        /// <summary>
        /// transformatin CIE L*a*b* -> CIE XYZ
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>XYZ value</returns>
        public XYZ asXYZ(XYZ WP)
        {
            XYZ temp = new XYZ();

            temp.Y = WP.Y * function_LAB_to_XYZ((this.L + 16f) / 116f);
            temp.X = WP.X * function_LAB_to_XYZ((this.L + 16f) / 116f + this.A / 500);
            temp.Z = WP.Z * function_LAB_to_XYZ((this.L + 16f) / 116f - this.B / 200);

            return temp;
        }

        // this is used in the L*a*b* value calculation
        private float function_LAB_to_XYZ(float input)
        {
            float temp = new float();
            if (input > (0.20689655f))
            {
                temp = (float)Math.Pow(input, 3f);
            }
            else
            {
                temp = 0.128418549f * (input - 0.13793103f);
            }

            return temp;
        }

    }
}
