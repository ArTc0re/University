using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using FarbRechner.FarbSysteme;
using FarbRechner;
using System.Globalization;

using System.IO;

namespace FarbRechner
{
    /// <summary>
    /// 
    /// </summary>
    /// <author>Markus Strobel, Justine Smyzek, Birthe Anne Wiegand</author>
    public partial class OpenGLForm : Form, IDisposable
    {

        #region Member Variables

        bool glLoaded;
        bool showColorCircle;

        VBO xyzColorSpaceVBO;
        VBO hslColorSpaceRectVBO;
        VBO hslColorSpaceCircleVBO;
        VBO selectedColorCircleVBO;
        VBO YxyPolygonVBO;
        VBO rgbPreviewLines;
        VBO hslPreviewLines;
        VBO hslPreviewCircle;
        List<VBO> ellipsesVBOs;

        VBO selectedHueCircleVBO;
        VBO selectedSLCircleVBO;

        int xyzShaderProgramId;
        int transformationShaderProgramId;
        int hslHShaderProgramId;
        int hslSLShaderProgramId;

        ColorHelper colorHelper;

        Matrix4 ModelMatrix;
        Matrix4 ScaleMatrix;
        Matrix4 TranslationMatrix;
        int MatrixLocation;
        int HueLocation;
        //int XYZtoRGBMatrixLocation;

        private Container container;
        private ToolTip tooltip;

        // XYZ Space
        private XYZ hoveredXYZColor;
        private XYZ selectedXYZColor;

        // Yxy
        private Yxy selectedYxyColor;

        // HSL Space
        private HSL selectedHSLBaseSL;
        private HSL selectedHSLColor;

        // preview/selected Colors
        private RGB hoveredRGBColor;
        private RGB selectedRGBColor;
        
        private RGB RGBvalue; // linear rgb value only used for internal calculations (no gamma correction)
        private customRGB customRGBvalue;
        private sRGB sRGBvalue;
        private HSL HSLvalue;
        private HSV HSVvalue;
        private XYZ XYZvalue;
        private LUV LUVvalue;
        private LAB LABvalue;

        
        #endregion Member Variables



        #region Ctors & Dtors

        /// <summary>
        ///  OpenGLForm constructor
        /// </summary>
        /// <author>Markus Strobel</author>
        public OpenGLForm()
        {
            InitializeComponent();

            colorHelper = new ColorHelper();

            container = new System.ComponentModel.Container();
            tooltip = new System.Windows.Forms.ToolTip(container);

            selectedRGBColor = new RGB();
            selectedHSLBaseSL = new HSL(1f, 1f, 0.5f);
            selectedHSLColor = new HSL(1f, 1f, 0.5f);

            // VBO inits
            ellipsesVBOs = new List<VBO>();

            customRGB.Checked = true;

            // WP, RR, GG, BB set to default (lables etc)
            labelWPXValue.Text = Math.Round((double)ColorHelper.WP_default.X, 3).ToString();
            labelWPYValue.Text = Math.Round((double)ColorHelper.WP_default.Y, 3).ToString();
            labelWPZValue.Text = Math.Round((double)ColorHelper.WP_default.Z, 3).ToString();
            labelRRXValue.Text = Math.Round((double)ColorHelper.RR_default.X, 3).ToString();
            labelRRYValue.Text = Math.Round((double)ColorHelper.RR_default.Y, 3).ToString();
            labelRRZValue.Text = Math.Round((double)ColorHelper.RR_default.Z, 3).ToString();
            labelGGXValue.Text = Math.Round((double)ColorHelper.GG_default.X, 3).ToString();
            labelGGYValue.Text = Math.Round((double)ColorHelper.GG_default.Y, 3).ToString();
            labelGGZValue.Text = Math.Round((double)ColorHelper.GG_default.Z, 3).ToString();
            labelBBXValue.Text = Math.Round((double)ColorHelper.BB_default.X, 3).ToString();
            labelBBYValue.Text = Math.Round((double)ColorHelper.BB_default.Y, 3).ToString();
            labelBBZValue.Text = Math.Round((double)ColorHelper.BB_default.Z, 3).ToString();
            

            // initializes internal color values
            ResetColorValues();

            //set RGB Slider lables to values (between 0 and 1)
            labelTabRGB_R.Text = "R: " + trackBarR.Value;
            labelTabRGB_G.Text = "G: " + trackBarG.Value;
            labelTabRGB_B.Text = "B: " + trackBarB.Value;

            //set HSL Slider lables to values 
            labelTabHSL_H.Text = "H: " + (trackBarH.Value) + "°";
            labelTabHSL_S.Text = "S: " + trackBarS.Value + "%";
            labelTabHSL_L.Text = "L: " + trackBarL.Value + "%";


        }

        /// <summary>
        ///  OpenGLForm destructor
        /// </summary>
        /// <author>Markus Strobel</author>
        ~OpenGLForm() 
        {
        }

        #endregion Ctors & Dtors



        /// <summary>
        ///  OpenGL init load method
        /// </summary>
        /// <author>Markus Strobel</author>
        private void OpenGL_Load(object sender, EventArgs e)
        {
            glLoaded = true;
            SetupViewport();

            // the rect for color visualization in the background
            createXYZViewVBO();
            createYxyPolygonVBO();
            createHSLViewVBO();
            //createSelectedColorCircleVBO(0.3f, 0.3f, 0.007f, 36);

            GL.ClearColor(0f, 0f, 0f, 1.0f);
            ModelMatrix = new Matrix4(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

            ScaleMatrix = Matrix4.CreateScale(2.0f);
            TranslationMatrix = Matrix4.CreateTranslation(-1f, -1f, 0f);
            //ScaleMatrix = Matrix4.CreateScale(1.0f);
            //TranslationMatrix = Matrix4.CreateTranslation(0f, 0f, 0f);

            ModelMatrix = Matrix4.Mult(ModelMatrix, ScaleMatrix);
            ModelMatrix = Matrix4.Mult(ModelMatrix, TranslationMatrix);

            xyzShaderProgramId = CreateShaderprogram(@"Shaders\Transformation.vertex", @"Shaders\XYZ.fragment"); // xyz color space
            hslHShaderProgramId = CreateShaderprogram(@"Shaders\Transformation.vertex", @"Shaders\HSL_H.fragment"); // HSL H Circle
            hslSLShaderProgramId = CreateShaderprogram(@"Shaders\Transformation.vertex", @"Shaders\HSL_SL.fragment"); // HSL SL Rect
            transformationShaderProgramId = CreateShaderprogram(@"Shaders\Transformation.vertex", ""); // just transformation
        }

        /// <summary>
        ///  Resize method, more or less obsolete at this moment, but maybe for further developement :P
        /// </summary>
        /// <author>Markus Strobel</author>
        private void OpenGL_Resize(object sender, EventArgs e)
        {
            if (!glLoaded)
                return;
        }

        /// <summary>
        /// The main update/paint loop for the openGL window
        /// </summary>
        /// <author>Markus Strobel</author>
        private void OpenGL_Update(object sender, PaintEventArgs e)
        {
            if (!glLoaded)
                return;

            GL.EnableVertexAttribArray(0);
            GL.Clear(ClearBufferMask.ColorBufferBit);

            if (radioButtonXYZView.Checked) // draw XYZ space
            {
                // XYZ space triangle and Yxy Polygon
                DrawXYZSpace();
                DrawVBO(YxyPolygonVBO, PrimitiveType.Polygon);

                // show the selected color
                if (showColorCircle)
                    DrawVBO(selectedColorCircleVBO, PrimitiveType.Polygon);

                // Trackbar Preview Lines for RGB
                if (checkBoxRGB.Checked && rgbPreviewLines != null)
                {
                    DrawVBO(rgbPreviewLines, PrimitiveType.Lines);
                }

                // Trackbar Priview Lines & Selected Color Circle for HSL                
                if (checkBoxHSL.Checked && hslPreviewLines != null)
                {
                    DrawVBO(hslPreviewLines, PrimitiveType.Lines);
                    DrawVBO(hslPreviewCircle, PrimitiveType.Polygon);
                }

                // JND Ellipses
                foreach (VBO vbo in ellipsesVBOs)
                {
                    DrawVBO(vbo, PrimitiveType.Polygon);
                }
            }
            else if (radioButtonHSLView.Checked) // draw HSL space
            {
                // rect & circle
                DrawHSLSpace();

                // show selected color within the center rect
                if (selectedSLCircleVBO != null)
                    DrawVBO(selectedSLCircleVBO, PrimitiveType.Polygon);
                if (selectedHueCircleVBO != null)
                    DrawVBO(selectedHueCircleVBO, PrimitiveType.Polygon);

            }

            GL.DisableVertexAttribArray(0);
            glControl1.SwapBuffers();
        }
        


        #region OpenGL Methods
        
        

        #region VBO Drawing Related Methods


        /// <summary>
        ///  The Draw Method for the XYZ Space
        /// </summary>
        /// <author>Markus Strobel</author>
        private void DrawXYZSpace()
        {
            GL.UseProgram(xyzShaderProgramId);
            // the matrix for the model transformation
            MatrixLocation = GL.GetUniformLocation(xyzShaderProgramId, "modelMatrix");
            GL.UniformMatrix4(MatrixLocation, false, ref ModelMatrix);
            
            //XYZtoRGBMatrixLocation = GL.GetUniformLocation(xyzShaderProgramId, "XYZtoRGBMatrix");
            //GL.UniformMatrix3(XYZtoRGBMatrixLocation, false, ref colorHelper.XYZtoRGBTransformation);


            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Fill);
            GL.EnableVertexAttribArray(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, xyzColorSpaceVBO.VertexBufferID);
            GL.VertexAttribPointer(0, 3, VertexAttribPointerType.Float, false, 0, 0);
            GL.DrawArrays(PrimitiveType.TriangleStrip, 0, 4);

            GL.UseProgram(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);
        }

        /// <summary>
        ///  The Draw Method for the HSL Space
        /// </summary>
        /// <author>Markus Strobel</author>
        private void DrawHSLSpace()
        {
            // Draw H Circle            
            GL.UseProgram(hslHShaderProgramId);
            // the matrix for the model transformation
            MatrixLocation = GL.GetUniformLocation(hslHShaderProgramId, "modelMatrix");
            GL.UniformMatrix4(MatrixLocation, false, ref ModelMatrix);

            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Fill);
            GL.EnableVertexAttribArray(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, hslColorSpaceCircleVBO.VertexBufferID);
            GL.VertexAttribPointer(0, 3, VertexAttribPointerType.Float, false, 0, 0);
            GL.DrawArrays(PrimitiveType.TriangleStrip, 0, hslColorSpaceCircleVBO.NumElements);

            GL.UseProgram(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);

            
            // Draw SL Rect
            GL.UseProgram(hslSLShaderProgramId);
            // the matrix for the model transformation
            MatrixLocation = GL.GetUniformLocation(hslSLShaderProgramId, "modelMatrix");
            GL.UniformMatrix4(MatrixLocation, false, ref ModelMatrix);
            HueLocation = GL.GetUniformLocation(hslSLShaderProgramId, "Hue");
            GL.Uniform1(HueLocation, (float)(selectedHSLColor.H));

            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Fill);
            GL.EnableVertexAttribArray(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, hslColorSpaceRectVBO.VertexBufferID);
            GL.VertexAttribPointer(0, 3, VertexAttribPointerType.Float, false, 0, 0);
            GL.DrawArrays(PrimitiveType.TriangleStrip, 0, hslColorSpaceRectVBO.NumElements);

            GL.UseProgram(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);



        }

        /// <summary>
        /// A draw method for all other VBOs
        /// </summary>
        /// <param name="VertexBufferObject">the VBO to draw</param>
        /// <param name="type">the PrimitiveType to draw the VBO</param>
        /// <author>Markus Strobel</author>
        private void DrawVBO(VBO VertexBufferObject, PrimitiveType type)
        {
            GL.UseProgram(transformationShaderProgramId);
            MatrixLocation = GL.GetUniformLocation(transformationShaderProgramId, "modelMatrix");
            GL.UniformMatrix4(MatrixLocation, false, ref ModelMatrix);

            GL.PolygonMode(MaterialFace.FrontAndBack, PolygonMode.Line);
            GL.EnableVertexAttribArray(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, VertexBufferObject.VertexBufferID);
            GL.VertexAttribPointer(0, 3, VertexAttribPointerType.Float, false, 0, 0);

            GL.DrawArrays(type, 0, VertexBufferObject.NumElements);

            GL.UseProgram(0);
            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);
        }



        #endregion VBO Drawing Related Methods

        

        #region VBO Creation Related Methods


        /// <summary>
        /// This method takes vertices and indices and creates/binds buffer and stores the respective BufferIds in a VBO and returns it
        /// </summary>
        /// <param name="vertices">the vertices</param>
        /// <param name="indices">the indices</param>
        /// <returns>a VBO with the IDs to the respective Buffers</returns>
        /// <author>Markus Strobel</author>
        private VBO LoadVBO(Vector3[] vertices, int[] indices)
        {
            VBO VertexBufferObject = new VBO();

            if (vertices != null)
            {
                VertexBufferObject.VertexBufferID = GL.GenBuffer();
                GL.BindBuffer(BufferTarget.ArrayBuffer, VertexBufferObject.VertexBufferID);
                GL.BufferData(BufferTarget.ArrayBuffer, (IntPtr)(vertices.Length * Vector3.SizeInBytes), vertices, BufferUsageHint.DynamicDraw);
                GL.BindBuffer(BufferTarget.ArrayBuffer, 0);
            }

            if (indices != null)
            {
                VertexBufferObject.IndicesBufferID = GL.GenBuffer();
                GL.BindBuffer(BufferTarget.ElementArrayBuffer, VertexBufferObject.IndicesBufferID);
                GL.BufferData(BufferTarget.ElementArrayBuffer, (IntPtr)(indices.Length * sizeof(int)), indices, BufferUsageHint.StaticDraw);
                GL.BindBuffer(BufferTarget.ElementArrayBuffer, 0);

                VertexBufferObject.NumElements = indices.Length;
            }

            return VertexBufferObject;
        }

        /// <summary>
        /// This method creates the circle for the selected Color in XYZ Space
        /// </summary>
        /// <param name="x">the current x coordinate or x value from Yxy</param>
        /// <param name="y">the current y coordinate or y value from Yxy</param>
        /// <param name="radius">the radius of the circle</param>
        /// <param name="vertices">the amount of vertices, which the circle shell have</param>
        /// <author>Markus Strobel</author>
        private void createSelectedColorCircleVBO(float x, float y, float radius, int vertices)
        {
            selectedColorCircleVBO = creatCircleVBO(x, y, radius, vertices);
        }

        /// <summary>
        /// This method creates the circle for the selected Hue value in HSL Space
        /// </summary>
        /// <param name="x">the current x coordinate or x value from Yxy</param>
        /// <param name="y">the current y coordinate or y value from Yxy</param>
        /// <param name="radius">the radius of the circle</param>
        /// <param name="vertices">the amount of vertices, which the circle shell have</param>
        /// <author>Markus Strobel</author>
        private void createSelectedHueCircle(float x, float y, float radius, int vertices)
        {
            selectedHueCircleVBO = creatCircleVBO(x, y, radius, vertices);
        }

        /// <summary>
        /// This method creates the circle for the selected S & L value in HSL Space
        /// </summary>
        /// <param name="x">the current x coordinate or x value from Yxy</param>
        /// <param name="y">the current y coordinate or y value from Yxy</param>
        /// <param name="radius">the radius of the circle</param>
        /// <param name="vertices">the amount of vertices, which the circle shell have</param>
        /// <author>Markus Strobel</author>
        private void createSelectedSLCircle(float x, float y, float radius, int vertices)
        {
            selectedSLCircleVBO = creatCircleVBO(x, y, radius, vertices);
        }

        /// <summary>
        /// this method generally generates a circle primitive/VBO based on the given values
        /// </summary>
        /// <param name="x">the current x coordinate (center x)</param>
        /// <param name="y">the current y coordinate (center y)</param>
        /// <param name="radius">the radius of the circle</param>
        /// <param name="vertices">the amount of vertices, which the circle shell have</param>
        /// <author>Markus Strobel</author>
        private VBO creatCircleVBO(float x, float y, float radius, int vertices)
        {
            float degreeStep = 360f / vertices;

            Vector3[] verticesList = new Vector3[vertices];
            int[] indices = new int[vertices];

            for (int i = 0; i < vertices; i++)
            {
                float xOffSet = radius * (float)Math.Cos((double)(i * degreeStep) * Math.PI / 180d);
                float yOffSet = radius * (float)Math.Sin((double)(i * degreeStep) * Math.PI / 180d);
                verticesList[i] = new Vector3(x + xOffSet, y + yOffSet, 0);
                indices[i] = i;
            }

            return LoadVBO(verticesList, indices);
        }

        /// <summary>
        /// this method creates the VBO for the XYZ Space
        /// </summary>
        /// <author>Markus Strobel</author>
        private void createXYZViewVBO()
        {
            Vector3[] vertices = new Vector3[4];
            vertices[0] = new Vector3(0.0f, 0.0f, 0f);
            vertices[1] = new Vector3(1.0f, 0.0f, 0f);
            vertices[2] = new Vector3(0.0f, 1.0f, 0f);
            vertices[3] = new Vector3(1.0f, 1.0f, 0f);

            int[] indices = { 0, 1, 2, 3 };

            xyzColorSpaceVBO = LoadVBO(vertices, indices);
        }

        /// <summary>
        /// this method creates the Circle and Rect VBO for the HSL Space
        /// </summary>
        /// <author>Markus Strobel</author>
        private void createHSLViewVBO()
        {
            // Hue Circle
            float x = 0.5f;
            float y = 0.5f;
            float outerRadius = 0.49f;
            float innerRadius = 0.36f;
            int vertices = 360;
            float degreeStep = 360f / vertices;

            Vector3[] innerCircle = new Vector3[vertices];
            Vector3[] outerCircle = new Vector3[vertices];

            for (int i = 0; i < vertices; i++)
            {
                float outerXOffSet = outerRadius * (float)Math.Cos((double)(i * degreeStep) * Math.PI / 180d);
                float outerYOffSet = outerRadius * (float)Math.Sin((double)(i * degreeStep) * Math.PI / 180d);
                float innerXOffSet = innerRadius * (float)Math.Cos((double)(i * degreeStep) * Math.PI / 180d);
                float innerYOffSet = innerRadius * (float)Math.Sin((double)(i * degreeStep) * Math.PI / 180d);

                outerCircle[i] = new Vector3(x + outerXOffSet, y + outerYOffSet, 0);
                innerCircle[i] = new Vector3(x + innerXOffSet, y + innerYOffSet, 0);
            }

            Vector3[] circleVertices = new Vector3[2 * vertices + 2];
            int[] circleIndices = new int[2 * vertices + 2];

            int j = 0; // helper index for
            for (int i = 0; i < vertices; i++)
            {
                circleVertices[j] = outerCircle[i];
                circleVertices[j + 1] = innerCircle[i];
                j += 2;
            }

            // connect last with first vertex
            circleVertices[2 * vertices] = outerCircle[0];
            circleVertices[2 * vertices + 1] = innerCircle[0];

            hslColorSpaceCircleVBO = LoadVBO(circleVertices, circleIndices);


            // S & L Rect
            Vector3[] rectVertices = new Vector3[4];
            rectVertices[0] = new Vector3(0.25f, 0.25f, 0.1f);
            rectVertices[1] = new Vector3(0.75f, 0.25f, 0.1f);
            rectVertices[2] = new Vector3(0.25f, 0.75f, 0.1f);
            rectVertices[3] = new Vector3(0.75f, 0.75f, 0.1f);
            int[] rectIndices = { 0, 1, 2, 3 };
            hslColorSpaceRectVBO = LoadVBO(rectVertices, rectIndices);
        }

        /// <summary>
        /// this method creates the RGBPreviewLines for the XYZ Space
        /// </summary>
        /// <author>Markus Strobel</author>
        private void createRGBPreviewLines(RGB selectedColor)
        {
            RGB rgb = new RGB();
            Yxy yxy;

            GL.Color3(0.5f, 0.5f, 0.5f);
            rgb.R = 1;
            rgb.G = selectedColor.G;
            rgb.B = selectedColor.B;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_1GB = new Vector3(yxy.x, yxy.y, 0f);
            rgb.R = 0;
            rgb.G = selectedColor.G;
            rgb.B = selectedColor.B;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_0GB = new Vector3(yxy.x, yxy.y, 0f);
            rgb.R = selectedColor.R;
            rgb.G = 1;
            rgb.B = selectedColor.B;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_R1B = new Vector3(yxy.x, yxy.y, 0f);
            rgb.R = selectedColor.R;
            rgb.G = 0;
            rgb.B = selectedColor.B;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_R0B = new Vector3(yxy.x, yxy.y, 0f);
            rgb.R = selectedColor.R;
            rgb.G = selectedColor.G;
            rgb.B = 1;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_RG1 = new Vector3(yxy.x, yxy.y, 0f);
            rgb.R = selectedColor.R;
            rgb.G = selectedColor.G;
            rgb.B = 0;
            yxy = rgb.asXYZ().asYxy();
            Vector3 RGB_RG0 = new Vector3(yxy.x, yxy.y, 0f);

            Vector3[] vertices = { RGB_1GB, RGB_0GB, RGB_R1B, RGB_R0B, RGB_RG1, RGB_RG0 };

            int[] indices = new int[vertices.Length];
            for (int i = 0; i < vertices.Length; i++)
            {
                indices[i] = i;
            }
            rgbPreviewLines = LoadVBO(vertices, indices);
        }

        /// <summary>
        /// creating the PreviewLines for HSL Saturation
        /// </summary>
        /// <param name="selectedColor">the selected Color</param>
        /// <author>Justine Smyzek</author>
        private void createHSLPreviewLines(HSL selectedColor)
        {
            HSL hsl = new HSL();
            Yxy yxy;

            GL.Color3(0.5f, 0.5f, 0.5f);
            hsl.H = selectedColor.H;
            hsl.S = 0;
            hsl.L = selectedColor.L;
            yxy = hsl.asRGB().asXYZ().asYxy();
            Vector3 HSL_H0L = new Vector3(yxy.x, yxy.y, 0f);
            hsl.H = selectedColor.H;
            hsl.S = 1;
            hsl.L = selectedColor.L;
            yxy = hsl.asRGB().asXYZ().asYxy();
            Vector3 HSL_H1L = new Vector3(yxy.x, yxy.y, 0f);

            Vector3[] vertices = { HSL_H0L, HSL_H1L };

            int[] indices = new int[vertices.Length];
            for (int i = 0; i < vertices.Length; i++)
            {
                indices[i] = i;
            }
            hslPreviewLines = LoadVBO(vertices, indices);
        }

        /// <summary>
        /// create HDL Polygon for Hue circulation
        /// </summary>
        /// <param name="selectedColor">the selected Color</param>
        /// <author>Justine Smyzek</author>
        private void createHSLPreviewCircle(HSL selectedColor)
        {
            HSL hsl = new HSL();
            Yxy yxy;
            int j = 0;
            Vector3[] vertices = new Vector3[36];

            GL.Color3(0.5f, 0.5f, 0.5f);
            for (int i = 0; i < 360; i += 10)
            {
                hsl.H = i;
                hsl.S = selectedColor.S;
                hsl.L = selectedColor.L;
                yxy = hsl.asRGB().asXYZ().asYxy();
                Vector3 hsli = new Vector3(yxy.x, yxy.y, 0f);
                vertices[j] = hsli;
                j += 1;

            }


            int[] indices = new int[vertices.Length];
            for (int i = 0; i < vertices.Length; i++)
            {
                indices[i] = i;
            }
            hslPreviewCircle = LoadVBO(vertices, indices);

        }

        /// <summary>
        /// creates the VBO for the YxyPolygonVBO
        /// </summary>
        /// <author>Justine Smyzek</author>
        private void createYxyPolygonVBO()
        {
            //Quelle: http://www.amazon.com/exec/obidos/ASIN/0471399183/efgscomputlab#reader_0471399183

            VBO vbo = new VBO();
            Vector3[] vertices = new Vector3[64];
            GL.Color3(0.5f, 0.5f, 0.5f);
            vertices[0] = new Vector3(0.1741f, 0.0050f, 0f);
            vertices[1] = new Vector3(0.1740f, 0.0050f, 0f);
            vertices[2] = new Vector3(0.1738f, 0.0049f, 0f);
            vertices[3] = new Vector3(0.1736f, 0.0049f, 0f);

            vertices[4] = new Vector3(0.1733f, 0.0048f, 0f);
            vertices[5] = new Vector3(0.1730f, 0.0048f, 0f);
            vertices[6] = new Vector3(0.1726f, 0.0048f, 0f);
            vertices[7] = new Vector3(0.1721f, 0.0048f, 0f);
            vertices[8] = new Vector3(0.1714f, 0.0051f, 0f);

            vertices[9] = new Vector3(0.1703f, 0.0058f, 0f);
            vertices[10] = new Vector3(0.1689f, 0.0069f, 0f);
            vertices[11] = new Vector3(0.1669f, 0.0086f, 0f);
            vertices[12] = new Vector3(0.1644f, 0.0109f, 0f);
            vertices[13] = new Vector3(0.1611f, 0.0138f, 0f);

            vertices[14] = new Vector3(0.1566f, 0.0177f, 0f);
            vertices[15] = new Vector3(0.1510f, 0.0227f, 0f);
            vertices[16] = new Vector3(0.1440f, 0.0297f, 0f);
            vertices[17] = new Vector3(0.1355f, 0.0399f, 0f);
            vertices[18] = new Vector3(0.1241f, 0.0578f, 0f);

            vertices[19] = new Vector3(0.1096f, 0.0868f, 0f);
            vertices[20] = new Vector3(0.0913f, 0.1327f, 0f);
            vertices[21] = new Vector3(0.0687f, 0.2007f, 0f);
            vertices[22] = new Vector3(0.0454f, 0.2950f, 0f);
            vertices[23] = new Vector3(0.0235f, 0.4127f, 0f);

            vertices[24] = new Vector3(0.0082f, 0.5384f, 0f);
            vertices[25] = new Vector3(0.0039f, 0.6548f, 0f);
            vertices[26] = new Vector3(0.0139f, 0.7502f, 0f);
            vertices[27] = new Vector3(0.0389f, 0.8120f, 0f);
            vertices[28] = new Vector3(0.0743f, 0.8338f, 0f);

            vertices[29] = new Vector3(0.1142f, 0.8262f, 0f);
            vertices[30] = new Vector3(0.1547f, 0.8059f, 0f);
            vertices[31] = new Vector3(0.1929f, 0.7816f, 0f);
            vertices[32] = new Vector3(0.2296f, 0.7543f, 0f);
            vertices[33] = new Vector3(0.2658f, 0.7243f, 0f);

            vertices[34] = new Vector3(0.3016f, 0.6923f, 0f);
            vertices[35] = new Vector3(0.3373f, 0.6589f, 0f);
            vertices[36] = new Vector3(0.3731f, 0.6245f, 0f);
            vertices[37] = new Vector3(0.4087f, 0.5896f, 0f);
            vertices[38] = new Vector3(0.4441f, 0.5547f, 0f);

            vertices[39] = new Vector3(0.4788f, 0.5202f, 0f);

            vertices[40] = new Vector3(0.5125f, 0.4866f, 0f);
            vertices[41] = new Vector3(0.5448f, 0.4544f, 0f);
            vertices[42] = new Vector3(0.5752f, 0.4242f, 0f);
            vertices[43] = new Vector3(0.6029f, 0.3965f, 0f);

            vertices[44] = new Vector3(0.6270f, 0.3725f, 0f);
            vertices[45] = new Vector3(0.6482f, 0.3514f, 0f);
            vertices[46] = new Vector3(0.6658f, 0.3340f, 0f);
            vertices[47] = new Vector3(0.6801f, 0.3197f, 0f);
            vertices[48] = new Vector3(0.6915f, 0.3083f, 0f);

            vertices[49] = new Vector3(0.7006f, 0.2993f, 0f);
            vertices[50] = new Vector3(0.7079f, 0.2920f, 0f);
            vertices[51] = new Vector3(0.7140f, 0.2859f, 0f);
            vertices[52] = new Vector3(0.7190f, 0.2809f, 0f);
            vertices[53] = new Vector3(0.7230f, 0.2770f, 0f);

            vertices[54] = new Vector3(0.7260f, 0.2740f, 0f);
            vertices[55] = new Vector3(0.7283f, 0.2717f, 0f);
            vertices[56] = new Vector3(0.7300f, 0.2700f, 0f);
            vertices[57] = new Vector3(0.7311f, 0.2689f, 0f);
            vertices[58] = new Vector3(0.7320f, 0.2680f, 0f);

            vertices[59] = new Vector3(0.7327f, 0.2673f, 0f);
            vertices[60] = new Vector3(0.7334f, 0.2666f, 0f);
            vertices[61] = new Vector3(0.7340f, 0.2660f, 0f);
            vertices[62] = new Vector3(0.7344f, 0.2656f, 0f);
            vertices[63] = new Vector3(0.7346f, 0.2654f, 0f);

            int[] indices = new int[vertices.Length];
            for (int i = 0; i < vertices.Length; i++)
            {
                indices[i] = i;
            }
            YxyPolygonVBO = LoadVBO(vertices, indices);
        }

        /// <summary>
        /// This method creates a VBO for the JND Ellipses for the selected Color with the values for McAdams Ellipses (0.055f)
        /// 
        /// The method first points to several directions with aequidistance angle and creates a line with points
        /// the points will taken to compute jnd until threshold is reached and breaks to take the next line
        /// </summary>
        /// <param name="color">the selected Color</param>
        /// <returns>returns a VBO with the Ellipse value</returns>
        /// <author>Markus Strobel, Justine Smyzek</author>
        private VBO createJNDEllipse(Yxy color)
        {
            int lines = 36;
            double degreeStep = 360d / lines;

            List<Vector2>[] linesArray = new List<Vector2>[lines];

            // generate lines to check
            for (int i = 0; i < lines; i++)
            {
                List<Vector2> linePoints = new List<Vector2>();
                
                double xOffSet = 0d;
                double yOffSet = 0d;

                // generate all vertices for each line
                for (double radius = 0.0001d; radius < 0.1d; radius += 0.0001d)
                {
                    xOffSet = radius * Math.Cos((i * degreeStep) * Math.PI / 180d);
                    yOffSet = radius * Math.Sin((i * degreeStep) * Math.PI / 180d);
                    linePoints.Add(new Vector2((float) (color.x + xOffSet), (float) (color.y + yOffSet)));
                }
                linesArray[i] = linePoints;
            }

            float jnd = 0.0055f; // value of McAdams Ellipsen

            List<Vector3> vertices = new List<Vector3>();
            List<int> indices = new List<int>();
            int ind = 0;

            // compute JND
            foreach(List<Vector2> line in linesArray)
            {
                foreach(Vector2 point in line)
                {
                    float result = 0f;
                    Yxy comp = new Yxy(point.X, point.Y);
                    result = ColorHelper.Chromatic_Difference(color.asXYZ(), comp.asXYZ());
                                        
                    if (result > jnd)
                    {
                        vertices.Add(new Vector3(point.X, point.Y, 0f));
                        indices.Add(ind++);
                        break;
                    }
                }
            }

            return LoadVBO(vertices.ToArray(), indices.ToArray());
        }

        /// <summary>
        /// trigger the creation and add of a new ellipse to the ellipse list
        /// </summary>
        /// <author>Justine Smyzek</author>
        private void AddEllipseToVBOList()
        {
            if (selectedYxyColor != null && selectedXYZColor != null && selectedRGBColor != null)
            {
                ellipsesVBOs.Add(createJNDEllipse(selectedYxyColor));
                buttonClearEllipses.Enabled = true;
                glControl1.Invalidate();
            }
        }

        /// <summary>
        /// this method is a quick n dirty modified HSLtoRGB for HSL Space computation
        /// </summary>
        /// <param name="hsl">the given hsl color</param>
        /// <returns>the resulting rgb color</returns>
        /// <author>Markus Strobel</author>
        public RGB HSLtoRGB(HSL hsl)
        {
            // quick n dirty modified version for -180° to 180°

            RGB rgb = new RGB();

            float hue = hsl.H;

            while (hue < 0)
            {
                hue += 360;
            }

            float C = (1f - Math.Abs(2f * hsl.L - 1f)) * hsl.S;
            float X = C * (1f - Math.Abs(((hue / 60f) % 2f) - 1f));
            float m = hsl.L - C / 2f;

            if (0f <= hsl.H && hsl.H < 60f)
            {
                rgb.R = C;
                rgb.G = X;
                rgb.B = 0;
            }
            else if (60f <= hsl.H && hsl.H < 120f)
            {
                rgb.R = X;
                rgb.G = C;
                rgb.B = 0;
            }
            else if (120f <= hsl.H && hsl.H <= 180f)
            {
                rgb.R = 0;
                rgb.G = C;
                rgb.B = X;
            }
            else if (-180f <= hsl.H && hsl.H < -120f)
            {
                rgb.R = 0;
                rgb.G = X;
                rgb.B = C;
            }
            else if (-120f <= hsl.H && hsl.H < -60f)
            {
                rgb.R = X;
                rgb.G = 0;
                rgb.B = C;
            }
            else if (-60f <= hsl.H && hsl.H < 0f)
            {
                rgb.R = C;
                rgb.G = 0;
                rgb.B = X;
            }
            rgb.R += m;
            rgb.G += m;
            rgb.B += m;

            return rgb;
        }

        

        #endregion VBO Related Methods
        


        #region OpenGL Init & Dispose


        /// <summary>
        /// Setting up our viewport
        /// </summary>
        /// <author>Markus Strobel</author>
        private void SetupViewport()
        {
            int width = glControl1.Width;
            int height = glControl1.Height;
            GL.MatrixMode(MatrixMode.Modelview);
            //GL.LoadIdentity();

            //GL.Ortho(0.0d, 1.0d, 0.0d, 1.0d, -1.0d, 1.0d);
            GL.Viewport(0, 0, width, height); // damit geben wir an wo links unten beginnt und wie weit es geht, in abhängigkeit der glControl Size, die -1.0 bis 1.0 werden darauf gematched
        }

        /// <summary>
        /// This method loads a shader from shaderfile to the program of the provided programId
        /// </summary>
        /// <param name="programId">the programId of the shaderprogram, where the shader should be attached to</param>
        /// <param name="fileName">the shaderfile</param>
        /// <param name="shaderType">the ShaderType</param>
        /// <author>Markus Strobel</author>
        private void LoadShader(int programId, String fileName, ShaderType shaderType)
        {
            String shaderCode = String.Empty;
            using (StreamReader reader = new StreamReader(fileName))
            {
                shaderCode = reader.ReadToEnd();
            }

            int address = GL.CreateShader(shaderType);
            GL.ShaderSource(address, shaderCode);
            GL.CompileShader(address);
            GL.AttachShader(programId, address);
            Console.WriteLine(GL.GetShaderInfoLog(address));
        }

        /// <summary>
        /// this methods creates a shader method
        /// </summary>
        /// <returns>returns the programId of a the shader program</returns>
        /// <author>Markus Strobel</author>
        private int CreateShaderprogram(String vertexFileName, String fragmentFileName)
        {
            int programId = GL.CreateProgram();

            if (!String.IsNullOrEmpty(vertexFileName))
                LoadShader(programId, vertexFileName, ShaderType.VertexShader);
            if (!String.IsNullOrEmpty(fragmentFileName))
                LoadShader(programId, fragmentFileName, ShaderType.FragmentShader);

            // Link Program
            GL.LinkProgram(programId);

            return programId;
        }

        /// <summary>
        /// This method deletes a given VBO Object
        /// </summary>
        /// <param name="vbo">the vbo to delete</param>
        /// <author>Markus Strobel</author>
        private void DeleteVBO(VBO vbo)
        {
            if (vbo != null)
            {
                if (vbo.VertexBufferID != -1)
                    GL.DeleteBuffer(vbo.VertexBufferID);
                if (vbo.IndicesBufferID != -1)
                    GL.DeleteBuffer(vbo.IndicesBufferID);
            }
        }

        /// <summary>
        /// This method disposes opengl related stuff
        /// </summary>
        /// <author>Markus Strobel</author>
        private void DisposeOpenGL()
        {
            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);

            DeleteVBO(xyzColorSpaceVBO);
            DeleteVBO(hslColorSpaceRectVBO);
            DeleteVBO(hslColorSpaceCircleVBO);
            DeleteVBO(selectedColorCircleVBO);
            DeleteVBO(YxyPolygonVBO);
            DeleteVBO(rgbPreviewLines);
            DeleteVBO(hslPreviewLines);
            DeleteVBO(hslPreviewCircle);
            DeleteVBO(selectedHueCircleVBO);
            DeleteVBO(selectedSLCircleVBO);

            foreach (VBO vbo in ellipsesVBOs)
            {
                DeleteVBO(vbo);
            }

            this.glControl1.Dispose();
            this.Dispose(true);
        }
        


        #endregion OpenGL Init & Dispose



        #endregion OpenGL Methods



        #region Mouse Related Methods

        /// <summary>
        /// this method performs all tasks done with a left mouse click in XYZ space
        /// </summary>
        /// <param name="x">the current x coordinate</param>
        /// <param name="y">the current y coordinate</param>
        /// <param name="showCircle">if true the selected Color circle will be shown</param>
        /// <author>Markus Strobel</author>
        private void LeftClickXYZSpace(float x, float y, bool showCircle)
        {
            // set trackbar values
            if (!(hoveredRGBColor.R < 0 || hoveredRGBColor.G < 0 || hoveredRGBColor.B < 0))
            {
                HSL hsl = hoveredRGBColor.asHSL();

                // normalize
                float maxV = Math.Max(hoveredRGBColor.R, Math.Max(hoveredRGBColor.G, hoveredRGBColor.B));
                hoveredRGBColor.R /= maxV;
                hoveredRGBColor.G /= maxV;
                hoveredRGBColor.B /= maxV;



                // set RGB tab values for trackbars
                labelTabRGB_R.Text = "R: " + hoveredRGBColor.R;
                labelTabRGB_G.Text = "G: " + hoveredRGBColor.G;
                labelTabRGB_B.Text = "B: " + hoveredRGBColor.B;
                trackBarR.Value = (int)(hoveredRGBColor.R * 255);
                trackBarG.Value = (int)(hoveredRGBColor.G * 255);
                trackBarB.Value = (int)(hoveredRGBColor.B * 255);

                //set HSL values for trackbars
                labelTabHSL_H.Text = "H: " + (hsl.H) + "°";
                labelTabHSL_S.Text = "S: " + (hsl.S * 100) + "%";
                labelTabHSL_L.Text = "L: " + (hsl.L * 100) + "%";
                trackBarH.Value = (int)hsl.H;
                trackBarS.Value = (int)(hsl.S * 100);
                trackBarL.Value = (int)(hsl.L * 100);

                selectedRGBColor = hoveredRGBColor;
                selectedXYZColor = hoveredXYZColor;
                selectedYxyColor = new Yxy(x, y);

                if (checkBoxRGB.Checked)
                {
                    showColorCircle = false;
                    createRGBPreviewLines(selectedRGBColor);
                }
                else if (checkBoxHSL.Checked)
                {
                    showColorCircle = false;
                    createHSLPreviewLines(selectedRGBColor.asHSL());
                    createHSLPreviewCircle(selectedRGBColor.asHSL());
                }
                else
                {
                    showColorCircle = showCircle;
                    createSelectedColorCircleVBO(x, y, 0.01f, 36); // computes color circle position
                }
            }
            else // (!(hoveredRGBColor.R < 0 || hoveredRGBColor.G < 0 || hoveredRGBColor.B < 0))
            {
                trackBarR.Value = 0;
                trackBarG.Value = 0;
                trackBarB.Value = 0;
                labelTabRGB_R.Text = "R: 0";
                labelTabRGB_G.Text = "G: 0";
                labelTabRGB_B.Text = "B: 0";
                trackBarH.Value = 1;
                trackBarS.Value = 100;
                trackBarL.Value = 50;
                labelTabHSL_H.Text = "H: 1°";
                labelTabHSL_S.Text = "S: 100%";
                labelTabHSL_L.Text = "L: 50%";

                selectedRGBColor = null;
                selectedXYZColor = null;
                showColorCircle = false;
            }

            // take current preview Color as selected Color 
            // we dont need to compute it twice
            // set selectedColor Panel, label
            panelColorSelected.BackColor = panelColorPreview.BackColor;
            labelColorSelectedHex.Text = labelColorPreviewHex.Text;
            labelColSelectedXH.Text = labelColPreviewXH.Text;
            labelColSelectedYS.Text = labelColPreviewYS.Text;
        }
        
        /// <summary>
        /// this method performs all tasks done with a left mouse click in HSL space
        /// </summary>
        /// <param name="x">the current x coordinate</param>
        /// <param name="y">the current y coordinate</param>
        /// <param name="showCircle">if true the selected Color circle will be shown</param>
        /// <author>Markus Strobel</author>
        private void LeftCLickHSLSpace(float x, float y)
        {
            if (x < 0.75f && x > 0.25f && y < 0.75f && y > 0.25f)
            {
                createSelectedSLCircle(x, y, 0.01f, 72);

                // compute S & L
                float xtemp = (x - 0.25f) * 2;
                float ytemp = (y - 0.25f) * 2;
                float S = 0f;
                float L = 0f;
                S = xtemp * 0.5f + ytemp * 0.5f;
                L = (1 - xtemp) * 0.5f + ytemp * 0.5f;
                selectedHSLColor.S = S;
                selectedHSLColor.L = L;

                String H = String.Empty;
                if (selectedHSLColor.H < 0)
                {
                    H = "H:" + Math.Round(selectedHSLColor.H + 360, 0).ToString() + "°";
                }
                else
                {
                    H = "H:" + Math.Round(selectedHSLColor.H, 0).ToString() + "°";
                }
                labelColSelectedXH.Text = H;
                labelColSelectedYS.Text = "S:" + Math.Round(selectedHSLColor.S, 2).ToString();
                labelColSelectedL.Text = "L:" + Math.Round(selectedHSLColor.L, 2).ToString();

                RGB hovered_SLselected = HSLtoRGB(selectedHSLColor);
                panelColorSelected.BackColor = Color.FromArgb((int)(hovered_SLselected.R * 255), (int)(hovered_SLselected.G * 255), (int)(hovered_SLselected.B * 255));
                labelColorSelectedHex.Text = "#" + ((int)(hovered_SLselected.R * 255)).ToString("X") + ((int)(hovered_SLselected.G * 255)).ToString("X") + ((int)(hovered_SLselected.B * 255)).ToString("X");
            }
        }


        /// <summary>
        /// mouse clicking will be handled here
        /// </summary>
        /// <author>Markus Strobel</author>
        private void glControl_MouseClick(object sender, MouseEventArgs e)
        {
            GLControl p = sender as GLControl;

            int screenX = e.X;
            int screenY = p.Height - e.Y;

            float x = (float)screenX / (float)p.Width;
            float y = (float)screenY / (float)p.Height;

            if (e.Button.Equals(MouseButtons.Left)) // LEFT CLICK
            {
                if (radioButtonXYZView.Checked)
                {
                    // LEFT CLICK in XYZ Space
                    LeftClickXYZSpace(x, y, true);
                }
                else if (radioButtonHSLView.Checked)
                {
                    // LEFT CLICK in HSL Space
                    LeftCLickHSLSpace(x, y);
                }
                glControl1.Invalidate();
            }
            else if (e.Button.Equals(MouseButtons.Right)) // RIGHT CLICK
            {
                if(radioButtonXYZView.Checked)
                {
                    LeftClickXYZSpace(x, y, false); // we just provide
                    AddEllipseToVBOList();
                }
                else if (radioButtonHSLView.Checked)
                {
                    float deltaX = x - 0.5f;
                    float deltaY = y - 0.5f;

                    float angle = (float)(Math.Atan2((double)deltaY, (double)deltaX) * 180d / Math.PI);

                    selectedHSLColor.H = angle;
                    try
                    {
                        RGB hovered_SLselected = HSLtoRGB(selectedHSLColor);
                        RGB hovered_SLstandard = HSLtoRGB(new HSL(angle, 1f, 0.5f));

                        panelColorPreview.BackColor = Color.FromArgb((int)(hovered_SLstandard.R * 255), (int)(hovered_SLstandard.G * 255), (int)(hovered_SLstandard.B * 255));
                        labelColorPreviewHex.Text = "#" + ((int)(hovered_SLstandard.R * 255)).ToString("X") + ((int)(hovered_SLstandard.G * 255)).ToString("X") + ((int)(hovered_SLstandard.B * 255)).ToString("X");

                        panelColorSelected.BackColor = Color.FromArgb((int)(hovered_SLselected.R * 255), (int)(hovered_SLselected.G * 255), (int)(hovered_SLselected.B * 255));
                        labelColorSelectedHex.Text = "#" + ((int)(hovered_SLselected.R * 255)).ToString("X") + ((int)(hovered_SLselected.G * 255)).ToString("X") + ((int)(hovered_SLselected.B * 255)).ToString("X");

                        String H = String.Empty;
                        if (selectedHSLColor.H < 0)
                        {
                            H = "H:" + Math.Round(selectedHSLColor.H + 360, 0).ToString() + "°";
                        }
                        else
                        {
                            H = "H:" + Math.Round(selectedHSLColor.H, 0).ToString() + "°";
                        }

                        // Set selected Hue with S = 1.0f and L = 0.5f
                        labelColPreviewXH.Text = H;
                        labelColPreviewYS.Text = "S:1";
                        labelColPreviewL.Text = "L:0.5";

                        // Set selected Hue with selected S and L
                        labelColSelectedXH.Text = H;
                        labelColSelectedYS.Text = "S:" + Math.Round(selectedHSLColor.S, 2).ToString();
                        labelColSelectedL.Text = "L:" + Math.Round(selectedHSLColor.L, 2).ToString();

                        // create circle for selected Hue
                        float xOffSet = 0.425f * (float)Math.Cos((double)(angle) * Math.PI / 180d);
                        float yOffSet = 0.425f * (float)Math.Sin((double)(angle) * Math.PI / 180d);

                        createSelectedHueCircle(0.5f + xOffSet, 0.5f + yOffSet, 0.015f, 36);
                        glControl1.Invalidate();
                    }
                    catch
                    {
                        labelColorPreviewHex.Text = "#000";
                        panelColorPreview.BackColor = Color.Black;
                    }
                }
            }
        }

        /// <summary>
        /// mouse hover ofer the OpenGL Window will be handled here
        /// </summary>
        /// <author>Markus Strobel</author>
        private void glControl_MouseHover(object sender, System.EventArgs e)
        {
            // Get current mouse position within the glControl
            Point mousePos = glControl1.PointToClient(Cursor.Position);
            // get x,y relative to an origin in the bottom left
            float x = mousePos.X;
            float y = glControl1.Height - mousePos.Y;

            // normalize to 0..1
            x /= glControl1.Width;
            y /= glControl1.Height;

            if (radioButtonXYZView.Checked)
            {
                Yxy Yxy = new Yxy(x, y);
                hoveredXYZColor = Yxy.asXYZ();
                hoveredRGBColor = hoveredXYZColor.asRGB(); //FarbRechner.CIEXYZ_to_RGB(FarbRechner.CIEYxy_to_CIEXYZ(Yxy));

                x = (float)Math.Round((double)x, 2);
                y = (float)Math.Round((double)y, 2);

                // filter negative rgb values
                if (!(hoveredRGBColor.R < 0 || hoveredRGBColor.G < 0 || hoveredRGBColor.B < 0))
                {
                    try
                    {
                        // normalize
                        float maxV = Math.Max(hoveredRGBColor.R, Math.Max(hoveredRGBColor.G, hoveredRGBColor.B));
                        hoveredRGBColor.R /= maxV;
                        hoveredRGBColor.G /= maxV;
                        hoveredRGBColor.B /= maxV;

                        // gamma correction 
                        hoveredRGBColor.R = ColorHelper.Reverse_Gamma_Correction(hoveredRGBColor.R);
                        hoveredRGBColor.G = ColorHelper.Reverse_Gamma_Correction(hoveredRGBColor.G);
                        hoveredRGBColor.B = ColorHelper.Reverse_Gamma_Correction(hoveredRGBColor.B);

                        panelColorPreview.BackColor = Color.FromArgb((int)(hoveredRGBColor.R * 255), (int)(hoveredRGBColor.G * 255), (int)(hoveredRGBColor.B * 255));
                        labelColorPreviewHex.Text = "#" + ((int)(hoveredRGBColor.R * 255)).ToString("X") + ((int)(hoveredRGBColor.G * 255)).ToString("X") + ((int)(hoveredRGBColor.B * 255)).ToString("X");
                    }
                    catch
                    {
                        labelColorPreviewHex.Text = "#000";
                        panelColorPreview.BackColor = Color.Black;
                    }
                }
                else
                {
                    labelColorPreviewHex.Text = "#000";
                    panelColorPreview.BackColor = Color.Black;
                }
                labelColPreviewXH.Text = "x: " + x.ToString();
                labelColPreviewYS.Text = "y: " + y.ToString();
            }
        }


        #endregion Mouse Related Events



        #region Tab FarbRechner


        // This is necessary so that the textboxes always have the appropriate color
        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void Input_ReadOnlyChanged(object sender, EventArgs e)
        {
            if ((sender as TextBox).ReadOnly)
            {
                (sender as TextBox).BackColor = Color.FromKnownColor(KnownColor.Control);
            }
            else
            {
                (sender as TextBox).BackColor = Color.FromKnownColor(KnownColor.White);
                this.Input_TextChanged(sender, e);
            }
        }



        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void Input_TextChanged(object sender, EventArgs e)
        {

            RadioButton checkedButton = this.tabFarbRechner.Controls.OfType<RadioButton>().FirstOrDefault(radioButton => radioButton.Checked);
            TextBox senderBox = sender as TextBox;

            // Check if the textbox that has been changed is currently active
            if (senderBox.Name.Contains(checkedButton.Name))
            {
                // check which input type and act accordingly
                switch (checkedButton.Name)
                {
                    // uses RGB's rules, so RGB's checkValidity is used
                    case "customRGB":
                        if (!FarbSysteme.RGB.checkInputValidity(senderBox.Text))
                        {
                            // Show a tooltip if the input is invalid, color the box red and disable the convert button.
                            tooltip.Show("That is not a valid RGB input value.\n(Note: The input has to be between 0 and 1.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                            senderBox.BackColor = Color.LightCoral;
                        }
                        else
                        {
                            // If the tooltip is being displayed but the input is valid, hide it, color the box white and reactivate the convert button.
                            if (tooltip.Active)
                            {
                                tooltip.Hide(senderBox);
                            }
                            senderBox.BackColor = Color.White;
                        }
                        // Check if all 3 fields have valid input and update the convert button
                        if (customRGB_R.BackColor == Color.White && customRGB_G.BackColor == Color.White && customRGB_B.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    // uses RGB's rules, so RGB's checkValidity is used
                    case "sRGB":
                        if (!FarbSysteme.RGB.checkInputValidity(senderBox.Text))
                        {
                            senderBox.BackColor = Color.LightCoral;
                            tooltip.Show("That is not a valid RGB input value.\n(Note: The input has to be between 0 and 1.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                        }
                        else
                        {
                            if (tooltip.Active)
                            {
                                tooltip.Hide(senderBox);
                            }
#if DEBUG
                            System.Diagnostics.Debug.WriteLine("Valid RGB input detected!");
#endif
                            senderBox.BackColor = Color.White;
                        }
                        // Check if all 3 fields have valid input and update the convert button
                        if (sRGB_R.BackColor == Color.White && sRGB_G.BackColor == Color.White && sRGB_B.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    case "HSL":
                        // H has different rules than S and L, so the use different methods
                        if (senderBox.Name == "HSL_H")
                        {
                            if (!FarbSysteme.HSL.checkInputValidity_H(senderBox.Text))
                            {
                                tooltip.Show("That is not a valid H input value.\n(Note: The input has to be between 0 and 360.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                                senderBox.BackColor = Color.LightCoral;
                            }
                            else
                            {
                                if (tooltip.Active)
                                {
                                    tooltip.Hide(senderBox);
                                }
                                senderBox.BackColor = Color.White;
                            }
                        }
                        else
                        {
                            if (!FarbSysteme.HSL.checkInputValidity_SL(senderBox.Text))
                            {
                                tooltip.Show("That is not a valid S/L input value.\n(Note: The input has to be between 0 and 1.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                                senderBox.BackColor = Color.LightCoral;
                            }
                            else
                            {
                                if (tooltip.Active)
                                {
                                    tooltip.Hide(senderBox);
                                }
                                senderBox.BackColor = Color.White;
                            }
                        }
                        // Check if all 3 fields have valid input and update the convert button
                        if (HSL_H.BackColor == Color.White && HSL_S.BackColor == Color.White && HSL_L.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    // uses HSL's rules
                    case "HSV":
                        if (senderBox.Name == "HSV_H")
                        {
                            if (!FarbSysteme.HSL.checkInputValidity_H(senderBox.Text))
                            {
                                tooltip.Show("That is not a valid H input value.\n(Note: The input has to be between 0 and 360.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                                senderBox.BackColor = Color.LightCoral;
                            }
                            else
                            {
                                if (tooltip.Active)
                                {
                                    tooltip.Hide(senderBox);
                                }
                                senderBox.BackColor = Color.White;
                            }
                        }
                        else
                        {
                            if (!FarbSysteme.HSL.checkInputValidity_SL(senderBox.Text))
                            {
                                tooltip.Show("That is not a valid S/V input value.\n(Note: The input has to be between 0 and 1.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                                senderBox.BackColor = Color.LightCoral;
                            }
                            else
                            {
                                if (tooltip.Active)
                                {
                                    tooltip.Hide(senderBox);
                                }
                                senderBox.BackColor = Color.White;
                            }
                        }
                        // Check if all 3 fields have valid input and update the convert button
                        if (HSV_H.BackColor == Color.White && HSV_S.BackColor == Color.White && HSV_V.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    case "XYZ":
                        if (!FarbSysteme.XYZ.checkInputValidity(senderBox.Text))
                        {
                            // Show a tooltip if the input is invalid, color the box red and disable the convert button.
                            tooltip.Show("That is not a valid XYZ input value.\n(Note: The input has to be non-negative.)", senderBox, senderBox.Width, senderBox.Height, 5000);
                            senderBox.BackColor = Color.LightCoral;
                        }
                        else
                        {
                            // If the tooltip is being displayed but the input is valid, hide it, color the box white and reactivate the convert button.
                            if (tooltip.Active)
                            {
                                tooltip.Hide(senderBox);
                            }
                            senderBox.BackColor = Color.White;
                        }
                        // Check if all 3 fields have valid input and update the convert button
                        if (XYZ_X.BackColor == Color.White && XYZ_Y.BackColor == Color.White && XYZ_Z.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    case "LUV":
                        /*if (!FarbSysteme.LUV.checkInputValidity(senderBox.Text))
                        {
                            // Show a tooltip if the input is invalid, color the box red and disable the convert button.
                            tooltip.Show("That is not a valid L*u*v* input value.", senderBox, senderBox.Width, senderBox.Height, 5000);
                            senderBox.BackColor = Color.LightCoral;
                        }
                        else
                        {*/
                            // If the tooltip is being displayed but the input is valid, hide it, color the box white and reactivate the convert button.
                            if (tooltip.Active)
                            {
                                tooltip.Hide(senderBox);
                            }
                            senderBox.BackColor = Color.White;
                        //}
                        // Check if all 3 fields have valid input and update the convert button
                        if (LUV_L.BackColor == Color.White && LUV_U.BackColor == Color.White && LUV_V.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                    case "LAB":
                        /*if (!FarbSysteme.LAB.checkInputValidity(senderBox.Text))
                        {
                            // Show a tooltip if the input is invalid, color the box red and disable the convert button.
                            tooltip.Show("That is not a valid L*a*b* input value.", senderBox, senderBox.Width, senderBox.Height, 5000);
                            senderBox.BackColor = Color.LightCoral;
                        }
                        else
                        {*/
                            // If the tooltip is being displayed but the input is valid, hide it, color the box white and reactivate the convert button.
                            if (tooltip.Active)
                            {
                                tooltip.Hide(senderBox);
                            }
                            senderBox.BackColor = Color.White;
                        //}
                        // Check if all 3 fields have valid input and update the convert button
                        if (LAB_L.BackColor == Color.White && LAB_A.BackColor == Color.White && LAB_B.BackColor == Color.White)
                        {
                            KonvertierenButton.Enabled = true;
                        }
                        else
                        {
                            KonvertierenButton.Enabled = false;
                        }
                        break;
                }
            }
        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void Input_KeyPress(object sender, System.Windows.Forms.KeyPressEventArgs e)
        {
            TextBox senderBox = sender as TextBox;

            // ensures that only numbers can be put in the text boxes.
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar) && e.KeyChar != '.' && e.KeyChar != '-')
            {
#if DEBUG
                System.Diagnostics.Debug.WriteLine("something else than a number has been typed.");
#endif
                e.Handled = true;
            }

            // ensures that only 1 comma can be input
            if ((e.KeyChar == '.') && (senderBox.Text.IndexOf('.') > -1))
            {
                e.Handled = true;
            }
            if ((e.KeyChar == ',') && !((senderBox.Text.IndexOf('.') > -1)))
            {
                senderBox.Text += ".";
                senderBox.SelectionStart = senderBox.Text.Length;
                senderBox.SelectionLength = 0;
                e.Handled = true;
            }

            // This ensures that only 1 '-' can be input
            if ((e.KeyChar == '-') && (senderBox.Text.IndexOf('-') > -1))
            {
#if DEBUG
                System.Diagnostics.Debug.WriteLine("invalid - tried");
#endif

                e.Handled = true;
            }

        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void KonvertierenButton_Click(object sender, EventArgs e)
        {
            RadioButton checkedButton = this.tabFarbRechner.Controls.OfType<RadioButton>().FirstOrDefault(radioButton => radioButton.Checked);

            switch (checkedButton.Name)
            {
                case "customRGB":
                    // parse input to internal customRGBvalue
                    customRGBvalue.R = float.Parse(customRGB_R.Text, CultureInfo.InvariantCulture);
                    customRGBvalue.G = float.Parse(customRGB_G.Text, CultureInfo.InvariantCulture);
                    customRGBvalue.B = float.Parse(customRGB_B.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    XYZvalue = customRGBvalue.asXYZ();
                    RGBvalue = XYZvalue.asRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSLvalue = RGBvalue.asHSL();
                    HSVvalue = RGBvalue.asHSV();
                    LUVvalue = XYZvalue.asLUV();
                    LABvalue = XYZvalue.asLAB();
                    break;
                case "sRGB":
                    // parse input to internal customRGBvalue
                    sRGBvalue.R = float.Parse(sRGB_R.Text, CultureInfo.InvariantCulture);
                    sRGBvalue.G = float.Parse(sRGB_G.Text, CultureInfo.InvariantCulture);
                    sRGBvalue.B = float.Parse(sRGB_B.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    RGBvalue = sRGBvalue.asRGB();
                    XYZvalue = RGBvalue.asXYZ();
                    customRGBvalue = XYZvalue.as_customRGB();
                    HSLvalue = RGBvalue.asHSL();
                    HSVvalue = RGBvalue.asHSV();
                    LUVvalue = XYZvalue.asLUV();
                    LABvalue = XYZvalue.asLAB();
                    break;
                case "HSL":
                    // parse input to internal HSLvalue
                    HSLvalue.H = float.Parse(HSL_H.Text, CultureInfo.InvariantCulture);
                    HSLvalue.S = float.Parse(HSL_S.Text, CultureInfo.InvariantCulture);
                    HSLvalue.L = float.Parse(HSL_L.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    RGBvalue = HSLvalue.asRGB();
                    XYZvalue = RGBvalue.asXYZ();
                    customRGBvalue = XYZvalue.as_customRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSVvalue = RGBvalue.asHSV();
                    LUVvalue = XYZvalue.asLUV();
                    LABvalue = XYZvalue.asLAB();
                    break;
                case "HSV":
                    // parse input to internal HSVvalue
                    HSVvalue.H = float.Parse(HSV_H.Text, CultureInfo.InvariantCulture);
                    HSVvalue.S = float.Parse(HSV_S.Text, CultureInfo.InvariantCulture);
                    HSVvalue.V = float.Parse(HSV_V.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    RGBvalue = HSVvalue.asRGB();
                    XYZvalue = RGBvalue.asXYZ();
                    customRGBvalue = XYZvalue.as_customRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSLvalue = RGBvalue.asHSL();
                    LUVvalue = XYZvalue.asLUV();
                    LABvalue = XYZvalue.asLAB();
                    break;
                case "XYZ":
                    // parse input to internal HSVvalue
                    XYZvalue.X = float.Parse(XYZ_X.Text, CultureInfo.InvariantCulture);
                    XYZvalue.Y = float.Parse(XYZ_Y.Text, CultureInfo.InvariantCulture);
                    XYZvalue.Z = float.Parse(XYZ_Z.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    RGBvalue = XYZvalue.asRGB();
                    customRGBvalue = XYZvalue.as_customRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSLvalue = RGBvalue.asHSL();
                    HSVvalue = RGBvalue.asHSV();
                    LUVvalue = XYZvalue.asLUV(ColorHelper.WP_used);
                    LABvalue = XYZvalue.asLAB(ColorHelper.WP_used);
                    break;
                case "LUV":
                    // parse input to internal LUVvalue
                    LUVvalue.L = float.Parse(LUV_L.Text, CultureInfo.InvariantCulture);
                    LUVvalue.U = float.Parse(LUV_U.Text, CultureInfo.InvariantCulture);
                    LUVvalue.V = float.Parse(LUV_V.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    XYZvalue = LUVvalue.asXYZ(ColorHelper.WP_used);
                    RGBvalue = XYZvalue.asRGB();
                    customRGBvalue = XYZvalue.as_customRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSLvalue = RGBvalue.asHSL();
                    HSVvalue = RGBvalue.asHSV();
                    LABvalue = XYZvalue.asLAB(ColorHelper.WP_used);
                    break;
                case "LAB":
                    // parse input to internal LABvalue
                    LABvalue.L = float.Parse(LAB_L.Text, CultureInfo.InvariantCulture);
                    LABvalue.A = float.Parse(LAB_A.Text, CultureInfo.InvariantCulture);
                    LABvalue.B = float.Parse(LAB_B.Text, CultureInfo.InvariantCulture);

                    // update other internal values
                    XYZvalue = LABvalue.asXYZ(ColorHelper.WP_used);
                    RGBvalue = XYZvalue.asRGB();
                    customRGBvalue = XYZvalue.as_customRGB();
                    sRGBvalue = RGBvalue.as_sRGB();
                    HSLvalue = RGBvalue.asHSL();
                    HSVvalue = RGBvalue.asHSV();
                    LUVvalue = XYZvalue.asLUV(ColorHelper.WP_used);
                    break;
            }

            UpdateBoxes();
        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void UpdateBoxes()
        {
            // customRGB update
            customRGB_R.Text = ((float)Math.Round(customRGBvalue.R, 3)).ToString();
            customRGB_G.Text = ((float)Math.Round(customRGBvalue.G, 3)).ToString();
            customRGB_B.Text = ((float)Math.Round(customRGBvalue.B, 3)).ToString();

            // sRGB update
            sRGB_R.Text = ((float)Math.Round(sRGBvalue.R, 3)).ToString();
            sRGB_G.Text = ((float)Math.Round(sRGBvalue.G, 3)).ToString();
            sRGB_B.Text = ((float)Math.Round(sRGBvalue.B, 3)).ToString();

            // HSL update
            HSL_H.Text = ((float)Math.Round(HSLvalue.H, 0)).ToString();
            HSL_S.Text = ((float)Math.Round(HSLvalue.S, 3)).ToString();
            HSL_L.Text = ((float)Math.Round(HSLvalue.L, 3)).ToString();

            // HSV update
            HSV_H.Text = ((float)Math.Round(HSVvalue.H, 0)).ToString();
            HSV_S.Text = ((float)Math.Round(HSVvalue.S, 3)).ToString();
            HSV_V.Text = ((float)Math.Round(HSVvalue.V, 3)).ToString();

            // CIE XYZ update
            XYZ_X.Text = ((float)Math.Round(XYZvalue.X, 3)).ToString();
            XYZ_Y.Text = ((float)Math.Round(XYZvalue.Y, 3)).ToString();
            XYZ_Z.Text = ((float)Math.Round(XYZvalue.Z, 3)).ToString();

            // LUV update
            LUV_L.Text = ((float)Math.Round(LUVvalue.L, 3)).ToString();
            LUV_U.Text = ((float)Math.Round(LUVvalue.U, 3)).ToString();
            LUV_V.Text = ((float)Math.Round(LUVvalue.V, 3)).ToString();

            // LAB update
            LAB_L.Text = ((float)Math.Round(LABvalue.L, 3)).ToString();
            LAB_A.Text = ((float)Math.Round(LABvalue.A, 3)).ToString();
            LAB_B.Text = ((float)Math.Round(LABvalue.B, 3)).ToString();
        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void TextBox_OnReturn(object sender, KeyEventArgs e)
        {
            if (e.KeyData == Keys.Enter)
            {
                KonvertierenButton_Click(sender, e);
                e.SuppressKeyPress = true;
            }
        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void ResetButton_Click(object sender, EventArgs e)
        {
            ResetColorValues();
        }

        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void ResetColorValues()
        {
            // set color values to 0
            RGBvalue = new RGB();
            customRGBvalue = new customRGB();
            sRGBvalue = new sRGB();
            HSLvalue = new HSL();
            HSVvalue = new HSV();
            XYZvalue = new XYZ();
            LUVvalue = new LUV();
            LABvalue = new LAB();

            UpdateBoxes();
        }

        #endregion Tab FarbRechner


        
        #region Button Events


        /// <summary>
        /// Button to draw/find an Ellipse for selected Color 
        /// </summary>
        /// <author>Justine Smyzek, Markus Strobel</author>
        private void ButtonAddEllipse_Click(object sender, EventArgs e)
        {
            AddEllipseToVBOList();
        }

        /// <summary>
        /// Clears the ellipsesList
        /// </summary>
        /// <author>Justine Smyzek, Markus Strobel</author>
        private void ButtonClearEllipses_Click(object sender, EventArgs e)
        {
            foreach (VBO vbo in ellipsesVBOs)
            {
                DeleteVBO(vbo);
            }
            glControl1.Invalidate();
            ellipsesVBOs.Clear();
            buttonClearEllipses.Enabled = false;
        }

        /// <summary>
        /// Sets the selected color as used White Point
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonWPSet_Click(object sender, EventArgs e)
        {
            if (selectedRGBColor != null)
            {
                labelWPXValue.Text = Math.Round((double)selectedXYZColor.X, 3).ToString();
                labelWPYValue.Text = Math.Round((double)selectedXYZColor.Y, 3).ToString();
                labelWPZValue.Text = Math.Round((double)selectedXYZColor.Z, 3).ToString();
                ColorHelper.WP_used = selectedXYZColor;
            }
        }

        /// <summary>
        /// sets the RR_used value to selected Color
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonRRSet_Click(object sender, EventArgs e)
        {
            if (selectedRGBColor != null)
            {
                labelRRXValue.Text = Math.Round((double)selectedXYZColor.X, 3).ToString();
                labelRRYValue.Text = Math.Round((double)selectedXYZColor.Y, 3).ToString();
                labelRRZValue.Text = Math.Round((double)selectedXYZColor.Z, 3).ToString();
                ColorHelper.RR_used = selectedXYZColor;
            }
        }

        /// <summary>
        /// sets the GG_used value to selected Color
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonGGSet_Click(object sender, EventArgs e)
        {
            if (selectedRGBColor != null)
            {
                labelGGXValue.Text = Math.Round((double)selectedXYZColor.X, 3).ToString();
                labelGGYValue.Text = Math.Round((double)selectedXYZColor.Y, 3).ToString();
                labelGGZValue.Text = Math.Round((double)selectedXYZColor.Z, 3).ToString();
                ColorHelper.GG_used = selectedXYZColor;
            }
        }

        /// <summary>
        /// sets the BB_used value to selected Color
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonBBSet_Click(object sender, EventArgs e)
        {
            if (selectedRGBColor != null)
            {
                labelBBXValue.Text = Math.Round((double)selectedXYZColor.X, 3).ToString();
                labelBBYValue.Text = Math.Round((double)selectedXYZColor.Y, 3).ToString();
                labelBBZValue.Text = Math.Round((double)selectedXYZColor.Z, 3).ToString();
                ColorHelper.BB_used = selectedXYZColor;
            }
        }

        /// <summary>
        /// resets the WP_used value to WP_default
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonWPReset_Click(object sender, EventArgs e)
        {
            // reset WhitePoint to default
            labelWPXValue.Text = Math.Round((double)ColorHelper.WP_default.X, 3).ToString();
            labelWPYValue.Text = Math.Round((double)ColorHelper.WP_default.Y, 3).ToString();
            labelWPZValue.Text = Math.Round((double)ColorHelper.WP_default.Z, 3).ToString();

            ColorHelper.WP_used = ColorHelper.WP_default;
        }
        
        /// <summary>
        /// resets the RR_used value to RR_default
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonRRReset_Click(object sender, EventArgs e)
        {
            // reset Reddest Red to default
            //FarbRechner.RR_used = FarbRechner.RR_default;
            labelRRXValue.Text = Math.Round((double)ColorHelper.RR_default.X, 3).ToString();
            labelRRYValue.Text = Math.Round((double)ColorHelper.RR_default.Y, 3).ToString();
            labelRRZValue.Text = Math.Round((double)ColorHelper.RR_default.Z, 3).ToString();

            ColorHelper.RR_used = ColorHelper.RR_default;
        }

        /// <summary>
        /// resets the GG_used value to GG_default
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonGGReset_Click(object sender, EventArgs e)
        {
            // reset Greenest Green to default
            //FarbRechner.GG_used = FarbRechner.GG_default;
            labelGGXValue.Text = Math.Round((double)ColorHelper.GG_default.X, 3).ToString();
            labelGGYValue.Text = Math.Round((double)ColorHelper.GG_default.Y, 3).ToString();
            labelGGZValue.Text = Math.Round((double)ColorHelper.GG_default.Z, 3).ToString();

            ColorHelper.GG_used = ColorHelper.GG_default;

        }
        
        /// <summary>
        /// resets the BB_used value to BB_default
        /// </summary>
        /// <author>Markus Strobel</author>
        private void buttonBBReset_Click(object sender, EventArgs e)
        {
            // reset Blueest Blue to default
            labelBBXValue.Text = Math.Round((double)ColorHelper.BB_default.X, 3).ToString();
            labelBBYValue.Text = Math.Round((double)ColorHelper.BB_default.Y, 3).ToString();
            labelBBZValue.Text = Math.Round((double)ColorHelper.BB_default.Z, 3).ToString();

            ColorHelper.BB_used = ColorHelper.BB_default;
        }
        


        #endregion Button Events



        #region TrackBar Events


        /// <summary>
        /// Slidebar Event for RGB - allows to change the current selected point 
        /// </summary>
        /// <author> Markus Strobel, Justine Smyzek</author>
        private void trackBarRGB_Scroll(object sender, EventArgs e)
        {
            if (radioButtonXYZView.Checked)
            {
                //calculate values
                RGB rgb_slide = new RGB(trackBarR.Value / 255f, trackBarG.Value / 255f, trackBarB.Value / 255f);
                
                // here is a small computation error, TODO - fix to get and show the correct color from trackbarRGB
                
                HSL hsl_slide = rgb_slide.asHSL();

                //change labels of color
                labelTabRGB_R.Text = "R: " + rgb_slide.R;
                labelTabRGB_G.Text = "G: " + rgb_slide.G;
                labelTabRGB_B.Text = "B: " + rgb_slide.B;

                //change also HSL scroll 
                trackBarH.Value = (int)hsl_slide.H;
                trackBarS.Value = (int)(hsl_slide.S * 100);
                trackBarL.Value = (int)(hsl_slide.L * 100);
                labelTabHSL_H.Text = "H: " + (trackBarH.Value) + "°";
                labelTabHSL_S.Text = "S: " + trackBarS.Value + "%";
                labelTabHSL_L.Text = "L: " + trackBarL.Value + "%";

                //set new selected Color
                panelColorSelected.BackColor = Color.FromArgb((int)(rgb_slide.R * 255), (int)(rgb_slide.G * 255), (int)(rgb_slide.B * 255));
                labelColorSelectedHex.Text = "#" + ((int)(rgb_slide.R * 255)).ToString("X") + ((int)(rgb_slide.G * 255)).ToString("X") + ((int)(rgb_slide.B * 255)).ToString("X");

                //calculate yxy room
                Yxy yxy = rgb_slide.asXYZ().asYxy();

                //control if Line prieview is wished - draw
                if (checkBoxRGB.Checked)
                {
                    createRGBPreviewLines(rgb_slide);
                    selectedRGBColor = rgb_slide;
                    showColorCircle = false;
                    checkBoxHSL.Enabled = false;
                }
                else
                {
                    showColorCircle = true;
                    checkBoxHSL.Enabled = true;
                }

                createSelectedColorCircleVBO(yxy.x, yxy.y, 0.01f, 36); // computes color circle position
            }
            glControl1.Invalidate();
        }

        /// <summary>
        ///  Slidebar Event for HSL - allows to change the current selected point 
        /// </summary>
        /// <author> Justine Smyzek</author>
        private void trackBarHSL_Scroll(object sender, EventArgs e)
        {
            TrackBar tb = sender as TrackBar;

            //calculate values
            HSL hsl_slide = new HSL(trackBarH.Value, trackBarS.Value / 100f, trackBarL.Value / 100f);
            RGB rgb_slide = hsl_slide.asRGB();

            // here is a small computation error (like in trackbarRGB), TODO - fix to get and show the correct color from trackbarHSL

            // gamma correction 
            //rgb_slide.R = ColorHelper.Apply_Gamma_Correction(rgb_slide.R);
            //rgb_slide.G = ColorHelper.Apply_Gamma_Correction(rgb_slide.G);
            //rgb_slide.B = ColorHelper.Apply_Gamma_Correction(rgb_slide.B);


            if (radioButtonXYZView.Checked)
            {
                //set new selected Color
                panelColorSelected.BackColor = Color.FromArgb((int)(rgb_slide.R * 255), (int)(rgb_slide.G * 255), (int)(rgb_slide.B * 255));
                labelColorSelectedHex.Text = "#" + ((int)(rgb_slide.R * 255)).ToString("X") + ((int)(rgb_slide.G * 255)).ToString("X") + ((int)(rgb_slide.B * 255)).ToString("X");

                //calculate yxy room
                Yxy yxy = rgb_slide.asXYZ().asYxy();

                //control if Line prieview is wished - draw
                if (checkBoxHSL.Checked)
                {
                    createHSLPreviewLines(hsl_slide);
                    createHSLPreviewCircle(hsl_slide);
                    showColorCircle = false;
                    trackBarS.Enabled = false;
                    checkBoxRGB.Enabled = false;
                    trackBarS.Value = 100;
                }
                else
                {
                    showColorCircle = true;
                    trackBarS.Enabled = true;
                    checkBoxRGB.Enabled = true;
                }
                //change labels of color
                labelTabHSL_H.Text = "H: " + trackBarH.Value + "°";
                labelTabHSL_S.Text = "S: " + trackBarS.Value + "%";
                labelTabHSL_L.Text = "L: " + trackBarL.Value + "%";

                createSelectedColorCircleVBO(yxy.x, yxy.y, 0.01f, 36); // computes color circle position
            }

            //change also RBG scroll 
            trackBarR.Value = (int)(rgb_slide.R * 255);
            trackBarG.Value = (int)(rgb_slide.G * 255);
            trackBarB.Value = (int)(rgb_slide.B * 255);
            labelTabRGB_R.Text = "R: " + rgb_slide.R;
            labelTabRGB_G.Text = "G: " + rgb_slide.G;
            labelTabRGB_B.Text = "B: " + rgb_slide.B;

            glControl1.Invalidate();
        }


        #endregion TrackBar Events

        

        #region RadioButton Events


        /// <summary>
        /// </summary>
        /// <author> Birthe Anne Wiegand</author>
        private void radioButton_FarbRechner_CheckedChanged(object sender, EventArgs e)
        {
            RadioButton senderButton = (RadioButton)sender;
            bool buttonIsChecked = senderButton.Checked;

            switch (senderButton.Name)
            {
                case "customRGB":
                    customRGB_R.ReadOnly = !senderButton.Checked;
                    customRGB_G.ReadOnly = !senderButton.Checked;
                    customRGB_B.ReadOnly = !senderButton.Checked;
                    break;
                case "sRGB":
                    sRGB_R.ReadOnly = !senderButton.Checked;
                    sRGB_G.ReadOnly = !senderButton.Checked;
                    sRGB_B.ReadOnly = !senderButton.Checked;
                    break;
                case "HSL":
                    HSL_H.ReadOnly = !senderButton.Checked;
                    HSL_S.ReadOnly = !senderButton.Checked;
                    HSL_L.ReadOnly = !senderButton.Checked;
                    break;
                case "HSV":
                    HSV_H.ReadOnly = !senderButton.Checked;
                    HSV_S.ReadOnly = !senderButton.Checked;
                    HSV_V.ReadOnly = !senderButton.Checked;
                    break;
                case "XYZ":
                    XYZ_X.ReadOnly = !senderButton.Checked;
                    XYZ_Y.ReadOnly = !senderButton.Checked;
                    XYZ_Z.ReadOnly = !senderButton.Checked;
                    break;
                case "LUV":
                    LUV_L.ReadOnly = !senderButton.Checked;
                    LUV_U.ReadOnly = !senderButton.Checked;
                    LUV_V.ReadOnly = !senderButton.Checked;
                    break;
                case "LAB":
                    LAB_L.ReadOnly = !senderButton.Checked;
                    LAB_A.ReadOnly = !senderButton.Checked;
                    LAB_B.ReadOnly = !senderButton.Checked;
                    break;
            }
        }

        /// <summary>
        /// Color Space View - check radio Button and notifiy OpenGL 
        /// </summary>
        /// <author> Markus Strobel</author>
        private void radioButton_ColorSpaceView_CheckedChanged(object sender, EventArgs e)
        {
            if (radioButtonHSLView.Checked)
            {
                checkBoxHSL.Visible = false;
                checkBoxRGB.Visible = false;
                ToggleXYZGUIELements(false);
                showColorCircle = false;
                labelColorPreviewHex.Text = "#000";
                panelColorPreview.BackColor = Color.Black;
                this.labelGuide.Text = "Left Click:      select S&L-Value \nRight Click:    select H-Value";
            }
            else
            {
                checkBoxHSL.Visible = true;
                checkBoxRGB.Visible = true;
                ToggleXYZGUIELements(true);
                showColorCircle = false;
                labelColorPreviewHex.Text = "#000";
                panelColorPreview.BackColor = Color.Black;
                this.labelGuide.Text = "Left Click:      select Color \nRight Click:    add JND Ellipse";
            }
            // if radio button is switched, then OpenGL needs to recognize it
            glControl1.Invalidate();
        }
        
        /// <summary>
        /// this method is called when we switch between XYZ and HSL Colorspace
        /// </summary>
        /// <param name="enable">true or false to enables or disables some gui elements</param>
        /// <author>Markus Strobel</author>
        private void ToggleXYZGUIELements(bool enable)
        {
            buttonWPSet.Enabled = enable;
            buttonRRSet.Enabled = enable;
            buttonGGSet.Enabled = enable;
            buttonBBSet.Enabled = enable;
            buttonWPReset.Enabled = enable;
            buttonRRReset.Enabled = enable;
            buttonGGReset.Enabled = enable;
            buttonBBReset.Enabled = enable;
            labelWP.Enabled = enable;
            labelWPXValue.Enabled = enable;
            labelWPYValue.Enabled = enable;
            labelWPZValue.Enabled = enable;
            labelRR.Enabled = enable;
            labelRRXValue.Enabled = enable;
            labelRRYValue.Enabled = enable;
            labelRRZValue.Enabled = enable;
            labelGG.Enabled = enable;
            labelGGXValue.Enabled = enable;
            labelGGYValue.Enabled = enable;
            labelGGZValue.Enabled = enable;
            labelBB.Enabled = enable;
            labelBBXValue.Enabled = enable;
            labelBBYValue.Enabled = enable;
            labelBBZValue.Enabled = enable;
            labelX.Enabled = enable;
            labelY.Enabled = enable;
            labelZ.Enabled = enable;
            tabControlColorSlider.Enabled = enable;
            labelColSelectedL.Visible = !enable;
            labelColPreviewL.Visible = !enable;
            buttonClearEllipses.Visible = enable;
            buttonAddEllipse.Visible = enable;

            if (!enable) // true -> XYZ, false -> HSL
            {
                labelColSelectedXH.Text = "H: ";
                labelColSelectedYS.Text = "S: ";
                labelColSelectedL.Text = "L: ";
                labelColPreviewXH.Text = "H: ";
                labelColPreviewYS.Text = "S: ";
                labelColPreviewL.Text = "L: ";
            }

        }



        #endregion RadioButton Events
        


    }
}
