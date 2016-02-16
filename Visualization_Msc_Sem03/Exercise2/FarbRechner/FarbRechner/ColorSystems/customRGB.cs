using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OpenTK;

namespace FarbRechner.FarbSysteme
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Birthe Anne Wiegand</author>
    public class customRGB
    {
        public float R { get; set; }
        public float G { get; set; }
        public float B { get; set; }

        public customRGB() : this(0, 0, 0) { }
        public customRGB(int R, int G, int B) : this((float)R, (float)G, (float)B) { }
        public customRGB(float Red, float Green, float Blue)
        {
            this.R = Red;
            this.G = Green;
            this.B = Blue;
        }

        ~customRGB()
        {
        }



        /// <summary>
        /// transformatin (custom)RGB -> CIE XYZ with our set primary colors and white point
        /// </summary>
        /// <author>Birthe Anne Wiegand</author>
        /// <returns>XYZ value</returns>
        public XYZ asXYZ()
        {
            XYZ temp = new XYZ();
            XYZ temp2 = new XYZ();
            XYZ temp3 = new XYZ();
            ColorHelper.TranformationMatrices_Update();

            Vector3 Vector_XYZ = new Vector3(this.R, this.G, this.B);
            Vector3 Vector_temp = ColorHelper.Multiply_Mat3_Vec3(ColorHelper.RGBtoXYZTransformation, Vector_XYZ);
            temp.X = Vector_temp[0];
            temp.Y = Vector_temp[1];
            temp.Z = Vector_temp[2];

            return temp;
        }
    }
}
