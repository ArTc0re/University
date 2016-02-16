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
    public class Yxy
    {
        public float Y { get; set; }
        public float x { get; set; }
        public float y { get; set; }

        public Yxy() : this(0f, 0f) { }
        public Yxy(float x, float y) : this(1.0f, x, y) { }
        public Yxy(Vector2 xy) : this(1.0f, xy.X, xy.Y) { }
        public Yxy(Vector3 Yxy) : this(Yxy.X, Yxy.Y, Yxy.Z) { }
        public Yxy(float Y, float x, float y)
        {
            this.Y = Y;
            this.x = x;
            this.y = y;
        }

        ~Yxy() { }
        public XYZ asXYZ()
        {
            XYZ temp = new XYZ();

            // transformation Yxy -> CIE XYZ
            float normalizeFactor = this.Y / this.y;
            float Z = (1 - this.x - this.y) * normalizeFactor;

            temp.X = this.x * normalizeFactor;
            temp.Y = this.Y;
            temp.Z = Z;

            return temp;
        }
    }
}
