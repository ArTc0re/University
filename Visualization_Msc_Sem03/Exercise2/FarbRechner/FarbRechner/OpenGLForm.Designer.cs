namespace FarbRechner
{
    partial class OpenGLForm
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            System.Windows.Forms.Label linRGB_R_label;
            System.Windows.Forms.Label linRGB_G_label;
            System.Windows.Forms.Label linRGB_B_label;
            this.glControl1 = new OpenTK.GLControl();
            this.panelColorSelected = new System.Windows.Forms.Panel();
            this.labelWP = new System.Windows.Forms.Label();
            this.labelWPXValue = new System.Windows.Forms.Label();
            this.labelRR = new System.Windows.Forms.Label();
            this.labelGG = new System.Windows.Forms.Label();
            this.labelBB = new System.Windows.Forms.Label();
            this.buttonWPSet = new System.Windows.Forms.Button();
            this.buttonWPReset = new System.Windows.Forms.Button();
            this.radioButtonXYZView = new System.Windows.Forms.RadioButton();
            this.radioButtonHSLView = new System.Windows.Forms.RadioButton();
            this.tabControlColorSlider = new System.Windows.Forms.TabControl();
            this.tabRGB = new System.Windows.Forms.TabPage();
            this.checkBoxRGB = new System.Windows.Forms.CheckBox();
            this.labelTabRGB_B = new System.Windows.Forms.Label();
            this.trackBarB = new System.Windows.Forms.TrackBar();
            this.labelTabRGB_G = new System.Windows.Forms.Label();
            this.trackBarG = new System.Windows.Forms.TrackBar();
            this.labelTabRGB_R = new System.Windows.Forms.Label();
            this.trackBarR = new System.Windows.Forms.TrackBar();
            this.tabHSV = new System.Windows.Forms.TabPage();
            this.checkBoxHSL = new System.Windows.Forms.CheckBox();
            this.labelTabHSL_L = new System.Windows.Forms.Label();
            this.trackBarL = new System.Windows.Forms.TrackBar();
            this.labelTabHSL_S = new System.Windows.Forms.Label();
            this.trackBarS = new System.Windows.Forms.TrackBar();
            this.labelTabHSL_H = new System.Windows.Forms.Label();
            this.trackBarH = new System.Windows.Forms.TrackBar();
            this.buttonRRReset = new System.Windows.Forms.Button();
            this.buttonRRSet = new System.Windows.Forms.Button();
            this.buttonGGReset = new System.Windows.Forms.Button();
            this.buttonGGSet = new System.Windows.Forms.Button();
            this.buttonBBReset = new System.Windows.Forms.Button();
            this.buttonBBSet = new System.Windows.Forms.Button();
            this.panelColorPreview = new System.Windows.Forms.Panel();
            this.labelColPreviewXH = new System.Windows.Forms.Label();
            this.labelColPreviewYS = new System.Windows.Forms.Label();
            this.labelColSelectedYS = new System.Windows.Forms.Label();
            this.labelColSelectedXH = new System.Windows.Forms.Label();
            this.labelColorPreviewHex = new System.Windows.Forms.Label();
            this.labelColorSelectedHex = new System.Windows.Forms.Label();
            this.labelWPYValue = new System.Windows.Forms.Label();
            this.labelWPZValue = new System.Windows.Forms.Label();
            this.labelRRZValue = new System.Windows.Forms.Label();
            this.labelRRYValue = new System.Windows.Forms.Label();
            this.labelRRXValue = new System.Windows.Forms.Label();
            this.labelGGZValue = new System.Windows.Forms.Label();
            this.labelGGYValue = new System.Windows.Forms.Label();
            this.labelGGXValue = new System.Windows.Forms.Label();
            this.labelBBZValue = new System.Windows.Forms.Label();
            this.labelBBYValue = new System.Windows.Forms.Label();
            this.labelBBXValue = new System.Windows.Forms.Label();
            this.labelX = new System.Windows.Forms.Label();
            this.labelY = new System.Windows.Forms.Label();
            this.labelZ = new System.Windows.Forms.Label();
            this.buttonAddEllipse = new System.Windows.Forms.Button();
            this.tabFarbRechner = new System.Windows.Forms.TabPage();
            this.label19 = new System.Windows.Forms.Label();
            this.label20 = new System.Windows.Forms.Label();
            this.label21 = new System.Windows.Forms.Label();
            this.LAB_B = new System.Windows.Forms.TextBox();
            this.LAB_A = new System.Windows.Forms.TextBox();
            this.LAB_L = new System.Windows.Forms.TextBox();
            this.LUV_V = new System.Windows.Forms.TextBox();
            this.LUV_U = new System.Windows.Forms.TextBox();
            this.LUV_L = new System.Windows.Forms.TextBox();
            this.XYZ_Z = new System.Windows.Forms.TextBox();
            this.XYZ_Y = new System.Windows.Forms.TextBox();
            this.XYZ_X = new System.Windows.Forms.TextBox();
            this.HSV_V = new System.Windows.Forms.TextBox();
            this.HSV_S = new System.Windows.Forms.TextBox();
            this.HSV_H = new System.Windows.Forms.TextBox();
            this.HSL_L = new System.Windows.Forms.TextBox();
            this.HSL_S = new System.Windows.Forms.TextBox();
            this.HSL_H = new System.Windows.Forms.TextBox();
            this.sRGB_B = new System.Windows.Forms.TextBox();
            this.sRGB_G = new System.Windows.Forms.TextBox();
            this.sRGB_R = new System.Windows.Forms.TextBox();
            this.customRGB_B = new System.Windows.Forms.TextBox();
            this.customRGB_G = new System.Windows.Forms.TextBox();
            this.customRGB_R = new System.Windows.Forms.TextBox();
            this.label16 = new System.Windows.Forms.Label();
            this.label17 = new System.Windows.Forms.Label();
            this.label18 = new System.Windows.Forms.Label();
            this.label13 = new System.Windows.Forms.Label();
            this.label14 = new System.Windows.Forms.Label();
            this.label15 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.InfoLabel = new System.Windows.Forms.Label();
            this.ResetButton = new System.Windows.Forms.Button();
            this.KonvertierenButton = new System.Windows.Forms.Button();
            this.XYZ = new System.Windows.Forms.RadioButton();
            this.LAB = new System.Windows.Forms.RadioButton();
            this.LUV = new System.Windows.Forms.RadioButton();
            this.HSL = new System.Windows.Forms.RadioButton();
            this.radioButtonsRGB = new System.Windows.Forms.RadioButton();
            this.HSV = new System.Windows.Forms.RadioButton();
            this.customRGB = new System.Windows.Forms.RadioButton();
            this.tabControlFarbRechner = new System.Windows.Forms.TabControl();
            this.labelColPreviewL = new System.Windows.Forms.Label();
            this.labelColSelectedL = new System.Windows.Forms.Label();
            this.buttonClearEllipses = new System.Windows.Forms.Button();
            this.labelGuide = new System.Windows.Forms.Label();
            linRGB_R_label = new System.Windows.Forms.Label();
            linRGB_G_label = new System.Windows.Forms.Label();
            linRGB_B_label = new System.Windows.Forms.Label();
            this.tabControlColorSlider.SuspendLayout();
            this.tabRGB.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarB)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarG)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarR)).BeginInit();
            this.tabHSV.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarL)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarS)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarH)).BeginInit();
            this.tabFarbRechner.SuspendLayout();
            this.tabControlFarbRechner.SuspendLayout();
            this.SuspendLayout();
            // 
            // linRGB_R_label
            // 
            linRGB_R_label.AutoSize = true;
            linRGB_R_label.Location = new System.Drawing.Point(143, 34);
            linRGB_R_label.Name = "linRGB_R_label";
            linRGB_R_label.Size = new System.Drawing.Size(18, 13);
            linRGB_R_label.TabIndex = 46;
            linRGB_R_label.Text = "R:";
            // 
            // linRGB_G_label
            // 
            linRGB_G_label.AutoSize = true;
            linRGB_G_label.Location = new System.Drawing.Point(243, 34);
            linRGB_G_label.Name = "linRGB_G_label";
            linRGB_G_label.Size = new System.Drawing.Size(18, 13);
            linRGB_G_label.TabIndex = 47;
            linRGB_G_label.Text = "G:";
            // 
            // linRGB_B_label
            // 
            linRGB_B_label.AutoSize = true;
            linRGB_B_label.Location = new System.Drawing.Point(343, 34);
            linRGB_B_label.Name = "linRGB_B_label";
            linRGB_B_label.Size = new System.Drawing.Size(17, 13);
            linRGB_B_label.TabIndex = 48;
            linRGB_B_label.Text = "B:";
            // 
            // glControl1
            // 
            this.glControl1.BackColor = System.Drawing.Color.Black;
            this.glControl1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
            this.glControl1.Location = new System.Drawing.Point(14, 12);
            this.glControl1.Margin = new System.Windows.Forms.Padding(4);
            this.glControl1.Name = "glControl1";
            this.glControl1.Size = new System.Drawing.Size(500, 500);
            this.glControl1.TabIndex = 0;
            this.glControl1.VSync = false;
            this.glControl1.Load += new System.EventHandler(this.OpenGL_Load);
            this.glControl1.Paint += new System.Windows.Forms.PaintEventHandler(this.OpenGL_Update);
            this.glControl1.MouseClick += new System.Windows.Forms.MouseEventHandler(this.glControl_MouseClick);
            this.glControl1.MouseMove += new System.Windows.Forms.MouseEventHandler(this.glControl_MouseHover);
            this.glControl1.Resize += new System.EventHandler(this.OpenGL_Resize);
            // 
            // panelColorSelected
            // 
            this.panelColorSelected.BackColor = System.Drawing.Color.Black;
            this.panelColorSelected.Location = new System.Drawing.Point(525, 53);
            this.panelColorSelected.Name = "panelColorSelected";
            this.panelColorSelected.Size = new System.Drawing.Size(30, 30);
            this.panelColorSelected.TabIndex = 33;
            // 
            // labelWP
            // 
            this.labelWP.AutoSize = true;
            this.labelWP.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelWP.Location = new System.Drawing.Point(521, 112);
            this.labelWP.Name = "labelWP";
            this.labelWP.Size = new System.Drawing.Size(34, 20);
            this.labelWP.TabIndex = 37;
            this.labelWP.Text = "WP";
            // 
            // labelWPXValue
            // 
            this.labelWPXValue.AutoSize = true;
            this.labelWPXValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelWPXValue.Location = new System.Drawing.Point(558, 116);
            this.labelWPXValue.Name = "labelWPXValue";
            this.labelWPXValue.Size = new System.Drawing.Size(14, 13);
            this.labelWPXValue.TabIndex = 35;
            this.labelWPXValue.Text = "X";
            // 
            // labelRR
            // 
            this.labelRR.AutoSize = true;
            this.labelRR.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelRR.Location = new System.Drawing.Point(521, 140);
            this.labelRR.Name = "labelRR";
            this.labelRR.Size = new System.Drawing.Size(33, 20);
            this.labelRR.TabIndex = 40;
            this.labelRR.Text = "RR";
            // 
            // labelGG
            // 
            this.labelGG.AutoSize = true;
            this.labelGG.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelGG.Location = new System.Drawing.Point(521, 168);
            this.labelGG.Name = "labelGG";
            this.labelGG.Size = new System.Drawing.Size(35, 20);
            this.labelGG.TabIndex = 43;
            this.labelGG.Text = "GG";
            // 
            // labelBB
            // 
            this.labelBB.AutoSize = true;
            this.labelBB.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelBB.Location = new System.Drawing.Point(521, 196);
            this.labelBB.Name = "labelBB";
            this.labelBB.Size = new System.Drawing.Size(31, 20);
            this.labelBB.TabIndex = 46;
            this.labelBB.Text = "BB";
            // 
            // buttonWPSet
            // 
            this.buttonWPSet.Location = new System.Drawing.Point(694, 112);
            this.buttonWPSet.Name = "buttonWPSet";
            this.buttonWPSet.Size = new System.Drawing.Size(37, 22);
            this.buttonWPSet.TabIndex = 47;
            this.buttonWPSet.Text = "Set";
            this.buttonWPSet.UseVisualStyleBackColor = true;
            this.buttonWPSet.Click += new System.EventHandler(this.buttonWPSet_Click);
            // 
            // buttonWPReset
            // 
            this.buttonWPReset.Location = new System.Drawing.Point(733, 112);
            this.buttonWPReset.Name = "buttonWPReset";
            this.buttonWPReset.Size = new System.Drawing.Size(43, 22);
            this.buttonWPReset.TabIndex = 48;
            this.buttonWPReset.Text = "Reset";
            this.buttonWPReset.UseVisualStyleBackColor = true;
            this.buttonWPReset.Click += new System.EventHandler(this.buttonWPReset_Click);
            // 
            // radioButtonXYZView
            // 
            this.radioButtonXYZView.AutoSize = true;
            this.radioButtonXYZView.Checked = true;
            this.radioButtonXYZView.Location = new System.Drawing.Point(14, 521);
            this.radioButtonXYZView.Name = "radioButtonXYZView";
            this.radioButtonXYZView.Size = new System.Drawing.Size(46, 17);
            this.radioButtonXYZView.TabIndex = 49;
            this.radioButtonXYZView.TabStop = true;
            this.radioButtonXYZView.Text = "XYZ";
            this.radioButtonXYZView.UseVisualStyleBackColor = true;
            this.radioButtonXYZView.CheckedChanged += new System.EventHandler(this.radioButton_ColorSpaceView_CheckedChanged);
            // 
            // radioButtonHSLView
            // 
            this.radioButtonHSLView.AutoSize = true;
            this.radioButtonHSLView.Location = new System.Drawing.Point(68, 522);
            this.radioButtonHSLView.Name = "radioButtonHSLView";
            this.radioButtonHSLView.Size = new System.Drawing.Size(46, 17);
            this.radioButtonHSLView.TabIndex = 50;
            this.radioButtonHSLView.TabStop = true;
            this.radioButtonHSLView.Text = "HSL";
            this.radioButtonHSLView.UseVisualStyleBackColor = true;
            this.radioButtonHSLView.CheckedChanged += new System.EventHandler(this.radioButton_ColorSpaceView_CheckedChanged);
            // 
            // tabControlColorSlider
            // 
            this.tabControlColorSlider.Controls.Add(this.tabRGB);
            this.tabControlColorSlider.Controls.Add(this.tabHSV);
            this.tabControlColorSlider.Location = new System.Drawing.Point(525, 221);
            this.tabControlColorSlider.Name = "tabControlColorSlider";
            this.tabControlColorSlider.SelectedIndex = 0;
            this.tabControlColorSlider.Size = new System.Drawing.Size(248, 322);
            this.tabControlColorSlider.TabIndex = 51;
            // 
            // tabRGB
            // 
            this.tabRGB.BackColor = System.Drawing.Color.White;
            this.tabRGB.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.tabRGB.Controls.Add(this.checkBoxRGB);
            this.tabRGB.Controls.Add(this.labelTabRGB_B);
            this.tabRGB.Controls.Add(this.trackBarB);
            this.tabRGB.Controls.Add(this.labelTabRGB_G);
            this.tabRGB.Controls.Add(this.trackBarG);
            this.tabRGB.Controls.Add(this.labelTabRGB_R);
            this.tabRGB.Controls.Add(this.trackBarR);
            this.tabRGB.Location = new System.Drawing.Point(4, 22);
            this.tabRGB.Name = "tabRGB";
            this.tabRGB.Padding = new System.Windows.Forms.Padding(3);
            this.tabRGB.Size = new System.Drawing.Size(240, 296);
            this.tabRGB.TabIndex = 0;
            this.tabRGB.Text = "RGB";
            // 
            // checkBoxRGB
            // 
            this.checkBoxRGB.AutoSize = true;
            this.checkBoxRGB.Location = new System.Drawing.Point(16, 258);
            this.checkBoxRGB.Name = "checkBoxRGB";
            this.checkBoxRGB.Size = new System.Drawing.Size(139, 17);
            this.checkBoxRGB.TabIndex = 63;
            this.checkBoxRGB.Text = "Show in OpenGL Frame";
            this.checkBoxRGB.UseVisualStyleBackColor = true;
            this.checkBoxRGB.CheckedChanged += new System.EventHandler(this.trackBarRGB_Scroll);
            // 
            // labelTabRGB_B
            // 
            this.labelTabRGB_B.AutoSize = true;
            this.labelTabRGB_B.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabRGB_B.Location = new System.Drawing.Point(6, 187);
            this.labelTabRGB_B.Name = "labelTabRGB_B";
            this.labelTabRGB_B.Size = new System.Drawing.Size(20, 20);
            this.labelTabRGB_B.TabIndex = 62;
            this.labelTabRGB_B.Text = "B";
            // 
            // trackBarB
            // 
            this.trackBarB.Location = new System.Drawing.Point(6, 210);
            this.trackBarB.Maximum = 255;
            this.trackBarB.Name = "trackBarB";
            this.trackBarB.Size = new System.Drawing.Size(228, 45);
            this.trackBarB.TabIndex = 61;
            this.trackBarB.Scroll += new System.EventHandler(this.trackBarRGB_Scroll);
            // 
            // labelTabRGB_G
            // 
            this.labelTabRGB_G.AutoSize = true;
            this.labelTabRGB_G.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabRGB_G.Location = new System.Drawing.Point(6, 97);
            this.labelTabRGB_G.Name = "labelTabRGB_G";
            this.labelTabRGB_G.Size = new System.Drawing.Size(22, 20);
            this.labelTabRGB_G.TabIndex = 60;
            this.labelTabRGB_G.Text = "G";
            // 
            // trackBarG
            // 
            this.trackBarG.Location = new System.Drawing.Point(6, 120);
            this.trackBarG.Maximum = 255;
            this.trackBarG.Name = "trackBarG";
            this.trackBarG.Size = new System.Drawing.Size(228, 45);
            this.trackBarG.TabIndex = 59;
            this.trackBarG.Scroll += new System.EventHandler(this.trackBarRGB_Scroll);
            // 
            // labelTabRGB_R
            // 
            this.labelTabRGB_R.AutoSize = true;
            this.labelTabRGB_R.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabRGB_R.Location = new System.Drawing.Point(6, 7);
            this.labelTabRGB_R.Name = "labelTabRGB_R";
            this.labelTabRGB_R.Size = new System.Drawing.Size(21, 20);
            this.labelTabRGB_R.TabIndex = 58;
            this.labelTabRGB_R.Text = "R";
            // 
            // trackBarR
            // 
            this.trackBarR.Location = new System.Drawing.Point(6, 30);
            this.trackBarR.Maximum = 255;
            this.trackBarR.Name = "trackBarR";
            this.trackBarR.Size = new System.Drawing.Size(228, 45);
            this.trackBarR.TabIndex = 0;
            this.trackBarR.Scroll += new System.EventHandler(this.trackBarRGB_Scroll);
            // 
            // tabHSV
            // 
            this.tabHSV.BackColor = System.Drawing.Color.White;
            this.tabHSV.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.tabHSV.Controls.Add(this.checkBoxHSL);
            this.tabHSV.Controls.Add(this.labelTabHSL_L);
            this.tabHSV.Controls.Add(this.trackBarL);
            this.tabHSV.Controls.Add(this.labelTabHSL_S);
            this.tabHSV.Controls.Add(this.trackBarS);
            this.tabHSV.Controls.Add(this.labelTabHSL_H);
            this.tabHSV.Controls.Add(this.trackBarH);
            this.tabHSV.Location = new System.Drawing.Point(4, 22);
            this.tabHSV.Name = "tabHSV";
            this.tabHSV.Padding = new System.Windows.Forms.Padding(3);
            this.tabHSV.Size = new System.Drawing.Size(240, 296);
            this.tabHSV.TabIndex = 1;
            this.tabHSV.Text = "HSL";
            // 
            // checkBoxHSL
            // 
            this.checkBoxHSL.AutoSize = true;
            this.checkBoxHSL.Location = new System.Drawing.Point(16, 258);
            this.checkBoxHSL.Name = "checkBoxHSL";
            this.checkBoxHSL.Size = new System.Drawing.Size(139, 17);
            this.checkBoxHSL.TabIndex = 69;
            this.checkBoxHSL.Text = "Show in OpenGL Frame";
            this.checkBoxHSL.UseVisualStyleBackColor = true;
            this.checkBoxHSL.CheckedChanged += new System.EventHandler(this.trackBarHSL_Scroll);
            // 
            // labelTabHSL_L
            // 
            this.labelTabHSL_L.AutoSize = true;
            this.labelTabHSL_L.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabHSL_L.Location = new System.Drawing.Point(6, 187);
            this.labelTabHSL_L.Name = "labelTabHSL_L";
            this.labelTabHSL_L.Size = new System.Drawing.Size(18, 20);
            this.labelTabHSL_L.TabIndex = 68;
            this.labelTabHSL_L.Text = "L";
            // 
            // trackBarL
            // 
            this.trackBarL.Location = new System.Drawing.Point(6, 210);
            this.trackBarL.Maximum = 100;
            this.trackBarL.Name = "trackBarL";
            this.trackBarL.Size = new System.Drawing.Size(228, 45);
            this.trackBarL.TabIndex = 67;
            this.trackBarL.Value = 50;
            this.trackBarL.Scroll += new System.EventHandler(this.trackBarHSL_Scroll);
            // 
            // labelTabHSL_S
            // 
            this.labelTabHSL_S.AutoSize = true;
            this.labelTabHSL_S.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabHSL_S.Location = new System.Drawing.Point(6, 97);
            this.labelTabHSL_S.Name = "labelTabHSL_S";
            this.labelTabHSL_S.Size = new System.Drawing.Size(20, 20);
            this.labelTabHSL_S.TabIndex = 66;
            this.labelTabHSL_S.Text = "S";
            // 
            // trackBarS
            // 
            this.trackBarS.Location = new System.Drawing.Point(6, 120);
            this.trackBarS.Maximum = 100;
            this.trackBarS.Name = "trackBarS";
            this.trackBarS.Size = new System.Drawing.Size(228, 45);
            this.trackBarS.TabIndex = 65;
            this.trackBarS.Value = 100;
            this.trackBarS.Scroll += new System.EventHandler(this.trackBarHSL_Scroll);
            // 
            // labelTabHSL_H
            // 
            this.labelTabHSL_H.AutoSize = true;
            this.labelTabHSL_H.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelTabHSL_H.Location = new System.Drawing.Point(6, 7);
            this.labelTabHSL_H.Name = "labelTabHSL_H";
            this.labelTabHSL_H.Size = new System.Drawing.Size(21, 20);
            this.labelTabHSL_H.TabIndex = 64;
            this.labelTabHSL_H.Text = "H";
            // 
            // trackBarH
            // 
            this.trackBarH.Location = new System.Drawing.Point(6, 30);
            this.trackBarH.Maximum = 359;
            this.trackBarH.Name = "trackBarH";
            this.trackBarH.Size = new System.Drawing.Size(228, 45);
            this.trackBarH.TabIndex = 63;
            this.trackBarH.Value = 1;
            this.trackBarH.Scroll += new System.EventHandler(this.trackBarHSL_Scroll);
            // 
            // buttonRRReset
            // 
            this.buttonRRReset.Location = new System.Drawing.Point(733, 140);
            this.buttonRRReset.Name = "buttonRRReset";
            this.buttonRRReset.Size = new System.Drawing.Size(43, 22);
            this.buttonRRReset.TabIndex = 53;
            this.buttonRRReset.Text = "Reset";
            this.buttonRRReset.UseVisualStyleBackColor = true;
            this.buttonRRReset.Click += new System.EventHandler(this.buttonRRReset_Click);
            // 
            // buttonRRSet
            // 
            this.buttonRRSet.Location = new System.Drawing.Point(694, 140);
            this.buttonRRSet.Name = "buttonRRSet";
            this.buttonRRSet.Size = new System.Drawing.Size(37, 22);
            this.buttonRRSet.TabIndex = 52;
            this.buttonRRSet.Text = "Set";
            this.buttonRRSet.UseVisualStyleBackColor = true;
            this.buttonRRSet.Click += new System.EventHandler(this.buttonRRSet_Click);
            // 
            // buttonGGReset
            // 
            this.buttonGGReset.Location = new System.Drawing.Point(733, 168);
            this.buttonGGReset.Name = "buttonGGReset";
            this.buttonGGReset.Size = new System.Drawing.Size(43, 22);
            this.buttonGGReset.TabIndex = 55;
            this.buttonGGReset.Text = "Reset";
            this.buttonGGReset.UseVisualStyleBackColor = true;
            this.buttonGGReset.Click += new System.EventHandler(this.buttonGGReset_Click);
            // 
            // buttonGGSet
            // 
            this.buttonGGSet.Location = new System.Drawing.Point(694, 168);
            this.buttonGGSet.Name = "buttonGGSet";
            this.buttonGGSet.Size = new System.Drawing.Size(37, 22);
            this.buttonGGSet.TabIndex = 54;
            this.buttonGGSet.Text = "Set";
            this.buttonGGSet.UseVisualStyleBackColor = true;
            this.buttonGGSet.Click += new System.EventHandler(this.buttonGGSet_Click);
            // 
            // buttonBBReset
            // 
            this.buttonBBReset.Location = new System.Drawing.Point(733, 196);
            this.buttonBBReset.Name = "buttonBBReset";
            this.buttonBBReset.Size = new System.Drawing.Size(43, 22);
            this.buttonBBReset.TabIndex = 57;
            this.buttonBBReset.Text = "Reset";
            this.buttonBBReset.UseVisualStyleBackColor = true;
            this.buttonBBReset.Click += new System.EventHandler(this.buttonBBReset_Click);
            // 
            // buttonBBSet
            // 
            this.buttonBBSet.Location = new System.Drawing.Point(694, 196);
            this.buttonBBSet.Name = "buttonBBSet";
            this.buttonBBSet.Size = new System.Drawing.Size(37, 22);
            this.buttonBBSet.TabIndex = 56;
            this.buttonBBSet.Text = "Set";
            this.buttonBBSet.UseVisualStyleBackColor = true;
            this.buttonBBSet.Click += new System.EventHandler(this.buttonBBSet_Click);
            // 
            // panelColorPreview
            // 
            this.panelColorPreview.BackColor = System.Drawing.Color.Black;
            this.panelColorPreview.Location = new System.Drawing.Point(525, 12);
            this.panelColorPreview.Name = "panelColorPreview";
            this.panelColorPreview.Size = new System.Drawing.Size(30, 30);
            this.panelColorPreview.TabIndex = 59;
            // 
            // labelColPreviewXH
            // 
            this.labelColPreviewXH.AutoSize = true;
            this.labelColPreviewXH.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColPreviewXH.Location = new System.Drawing.Point(561, 10);
            this.labelColPreviewXH.Name = "labelColPreviewXH";
            this.labelColPreviewXH.Size = new System.Drawing.Size(27, 16);
            this.labelColPreviewXH.TabIndex = 58;
            this.labelColPreviewXH.Text = "x: 0";
            // 
            // labelColPreviewYS
            // 
            this.labelColPreviewYS.AutoSize = true;
            this.labelColPreviewYS.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColPreviewYS.Location = new System.Drawing.Point(605, 10);
            this.labelColPreviewYS.Name = "labelColPreviewYS";
            this.labelColPreviewYS.Size = new System.Drawing.Size(28, 16);
            this.labelColPreviewYS.TabIndex = 60;
            this.labelColPreviewYS.Text = "y: 0";
            // 
            // labelColSelectedYS
            // 
            this.labelColSelectedYS.AutoSize = true;
            this.labelColSelectedYS.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColSelectedYS.Location = new System.Drawing.Point(606, 51);
            this.labelColSelectedYS.Name = "labelColSelectedYS";
            this.labelColSelectedYS.Size = new System.Drawing.Size(28, 16);
            this.labelColSelectedYS.TabIndex = 62;
            this.labelColSelectedYS.Text = "y: 0";
            // 
            // labelColSelectedXH
            // 
            this.labelColSelectedXH.AutoSize = true;
            this.labelColSelectedXH.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColSelectedXH.Location = new System.Drawing.Point(562, 51);
            this.labelColSelectedXH.Name = "labelColSelectedXH";
            this.labelColSelectedXH.Size = new System.Drawing.Size(27, 16);
            this.labelColSelectedXH.TabIndex = 61;
            this.labelColSelectedXH.Text = "x: 0";
            // 
            // labelColorPreviewHex
            // 
            this.labelColorPreviewHex.AutoSize = true;
            this.labelColorPreviewHex.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColorPreviewHex.Location = new System.Drawing.Point(561, 27);
            this.labelColorPreviewHex.Name = "labelColorPreviewHex";
            this.labelColorPreviewHex.Size = new System.Drawing.Size(36, 16);
            this.labelColorPreviewHex.TabIndex = 63;
            this.labelColorPreviewHex.Text = "#000";
            // 
            // labelColorSelectedHex
            // 
            this.labelColorSelectedHex.AutoSize = true;
            this.labelColorSelectedHex.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColorSelectedHex.Location = new System.Drawing.Point(562, 68);
            this.labelColorSelectedHex.Name = "labelColorSelectedHex";
            this.labelColorSelectedHex.Size = new System.Drawing.Size(36, 16);
            this.labelColorSelectedHex.TabIndex = 64;
            this.labelColorSelectedHex.Text = "#000";
            // 
            // labelWPYValue
            // 
            this.labelWPYValue.AutoSize = true;
            this.labelWPYValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelWPYValue.Location = new System.Drawing.Point(603, 116);
            this.labelWPYValue.Name = "labelWPYValue";
            this.labelWPYValue.Size = new System.Drawing.Size(14, 13);
            this.labelWPYValue.TabIndex = 65;
            this.labelWPYValue.Text = "Y";
            // 
            // labelWPZValue
            // 
            this.labelWPZValue.AutoSize = true;
            this.labelWPZValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelWPZValue.Location = new System.Drawing.Point(648, 116);
            this.labelWPZValue.Name = "labelWPZValue";
            this.labelWPZValue.Size = new System.Drawing.Size(14, 13);
            this.labelWPZValue.TabIndex = 66;
            this.labelWPZValue.Text = "Z";
            // 
            // labelRRZValue
            // 
            this.labelRRZValue.AutoSize = true;
            this.labelRRZValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelRRZValue.Location = new System.Drawing.Point(648, 144);
            this.labelRRZValue.Name = "labelRRZValue";
            this.labelRRZValue.Size = new System.Drawing.Size(14, 13);
            this.labelRRZValue.TabIndex = 69;
            this.labelRRZValue.Text = "Z";
            // 
            // labelRRYValue
            // 
            this.labelRRYValue.AutoSize = true;
            this.labelRRYValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelRRYValue.Location = new System.Drawing.Point(603, 144);
            this.labelRRYValue.Name = "labelRRYValue";
            this.labelRRYValue.Size = new System.Drawing.Size(14, 13);
            this.labelRRYValue.TabIndex = 68;
            this.labelRRYValue.Text = "Y";
            // 
            // labelRRXValue
            // 
            this.labelRRXValue.AutoSize = true;
            this.labelRRXValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelRRXValue.Location = new System.Drawing.Point(558, 144);
            this.labelRRXValue.Name = "labelRRXValue";
            this.labelRRXValue.Size = new System.Drawing.Size(14, 13);
            this.labelRRXValue.TabIndex = 67;
            this.labelRRXValue.Text = "X";
            // 
            // labelGGZValue
            // 
            this.labelGGZValue.AutoSize = true;
            this.labelGGZValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelGGZValue.Location = new System.Drawing.Point(648, 173);
            this.labelGGZValue.Name = "labelGGZValue";
            this.labelGGZValue.Size = new System.Drawing.Size(14, 13);
            this.labelGGZValue.TabIndex = 72;
            this.labelGGZValue.Text = "Z";
            // 
            // labelGGYValue
            // 
            this.labelGGYValue.AutoSize = true;
            this.labelGGYValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelGGYValue.Location = new System.Drawing.Point(603, 173);
            this.labelGGYValue.Name = "labelGGYValue";
            this.labelGGYValue.Size = new System.Drawing.Size(14, 13);
            this.labelGGYValue.TabIndex = 71;
            this.labelGGYValue.Text = "Y";
            // 
            // labelGGXValue
            // 
            this.labelGGXValue.AutoSize = true;
            this.labelGGXValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelGGXValue.Location = new System.Drawing.Point(558, 173);
            this.labelGGXValue.Name = "labelGGXValue";
            this.labelGGXValue.Size = new System.Drawing.Size(14, 13);
            this.labelGGXValue.TabIndex = 70;
            this.labelGGXValue.Text = "X";
            // 
            // labelBBZValue
            // 
            this.labelBBZValue.AutoSize = true;
            this.labelBBZValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelBBZValue.Location = new System.Drawing.Point(648, 201);
            this.labelBBZValue.Name = "labelBBZValue";
            this.labelBBZValue.Size = new System.Drawing.Size(14, 13);
            this.labelBBZValue.TabIndex = 75;
            this.labelBBZValue.Text = "Z";
            // 
            // labelBBYValue
            // 
            this.labelBBYValue.AutoSize = true;
            this.labelBBYValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelBBYValue.Location = new System.Drawing.Point(603, 201);
            this.labelBBYValue.Name = "labelBBYValue";
            this.labelBBYValue.Size = new System.Drawing.Size(14, 13);
            this.labelBBYValue.TabIndex = 74;
            this.labelBBYValue.Text = "Y";
            // 
            // labelBBXValue
            // 
            this.labelBBXValue.AutoSize = true;
            this.labelBBXValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelBBXValue.Location = new System.Drawing.Point(558, 201);
            this.labelBBXValue.Name = "labelBBXValue";
            this.labelBBXValue.Size = new System.Drawing.Size(14, 13);
            this.labelBBXValue.TabIndex = 73;
            this.labelBBXValue.Text = "X";
            // 
            // labelX
            // 
            this.labelX.AutoSize = true;
            this.labelX.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelX.Location = new System.Drawing.Point(558, 100);
            this.labelX.Name = "labelX";
            this.labelX.Size = new System.Drawing.Size(14, 13);
            this.labelX.TabIndex = 76;
            this.labelX.Text = "X";
            // 
            // labelY
            // 
            this.labelY.AutoSize = true;
            this.labelY.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelY.Location = new System.Drawing.Point(603, 100);
            this.labelY.Name = "labelY";
            this.labelY.Size = new System.Drawing.Size(14, 13);
            this.labelY.TabIndex = 77;
            this.labelY.Text = "Y";
            // 
            // labelZ
            // 
            this.labelZ.AutoSize = true;
            this.labelZ.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelZ.Location = new System.Drawing.Point(648, 100);
            this.labelZ.Name = "labelZ";
            this.labelZ.Size = new System.Drawing.Size(14, 13);
            this.labelZ.TabIndex = 78;
            this.labelZ.Text = "Z";
            // 
            // buttonAddEllipse
            // 
            this.buttonAddEllipse.Location = new System.Drawing.Point(328, 517);
            this.buttonAddEllipse.Name = "buttonAddEllipse";
            this.buttonAddEllipse.Size = new System.Drawing.Size(90, 25);
            this.buttonAddEllipse.TabIndex = 79;
            this.buttonAddEllipse.Text = "Add Ellipse";
            this.buttonAddEllipse.UseVisualStyleBackColor = true;
            this.buttonAddEllipse.Click += new System.EventHandler(this.ButtonAddEllipse_Click);
            // 
            // tabFarbRechner
            // 
            this.tabFarbRechner.Controls.Add(this.label19);
            this.tabFarbRechner.Controls.Add(this.label20);
            this.tabFarbRechner.Controls.Add(this.label21);
            this.tabFarbRechner.Controls.Add(this.LAB_B);
            this.tabFarbRechner.Controls.Add(this.LAB_A);
            this.tabFarbRechner.Controls.Add(this.LAB_L);
            this.tabFarbRechner.Controls.Add(this.LUV_V);
            this.tabFarbRechner.Controls.Add(this.LUV_U);
            this.tabFarbRechner.Controls.Add(this.LUV_L);
            this.tabFarbRechner.Controls.Add(this.XYZ_Z);
            this.tabFarbRechner.Controls.Add(this.XYZ_Y);
            this.tabFarbRechner.Controls.Add(this.XYZ_X);
            this.tabFarbRechner.Controls.Add(this.HSV_V);
            this.tabFarbRechner.Controls.Add(this.HSV_S);
            this.tabFarbRechner.Controls.Add(this.HSV_H);
            this.tabFarbRechner.Controls.Add(this.HSL_L);
            this.tabFarbRechner.Controls.Add(this.HSL_S);
            this.tabFarbRechner.Controls.Add(this.HSL_H);
            this.tabFarbRechner.Controls.Add(this.sRGB_B);
            this.tabFarbRechner.Controls.Add(this.sRGB_G);
            this.tabFarbRechner.Controls.Add(this.sRGB_R);
            this.tabFarbRechner.Controls.Add(this.customRGB_B);
            this.tabFarbRechner.Controls.Add(this.customRGB_G);
            this.tabFarbRechner.Controls.Add(this.customRGB_R);
            this.tabFarbRechner.Controls.Add(this.label16);
            this.tabFarbRechner.Controls.Add(this.label17);
            this.tabFarbRechner.Controls.Add(this.label18);
            this.tabFarbRechner.Controls.Add(this.label13);
            this.tabFarbRechner.Controls.Add(this.label14);
            this.tabFarbRechner.Controls.Add(this.label15);
            this.tabFarbRechner.Controls.Add(this.label7);
            this.tabFarbRechner.Controls.Add(this.label8);
            this.tabFarbRechner.Controls.Add(this.label9);
            this.tabFarbRechner.Controls.Add(this.label4);
            this.tabFarbRechner.Controls.Add(this.label5);
            this.tabFarbRechner.Controls.Add(this.label6);
            this.tabFarbRechner.Controls.Add(this.label1);
            this.tabFarbRechner.Controls.Add(this.label2);
            this.tabFarbRechner.Controls.Add(this.label3);
            this.tabFarbRechner.Controls.Add(this.InfoLabel);
            this.tabFarbRechner.Controls.Add(this.ResetButton);
            this.tabFarbRechner.Controls.Add(this.KonvertierenButton);
            this.tabFarbRechner.Controls.Add(linRGB_B_label);
            this.tabFarbRechner.Controls.Add(linRGB_G_label);
            this.tabFarbRechner.Controls.Add(linRGB_R_label);
            this.tabFarbRechner.Controls.Add(this.XYZ);
            this.tabFarbRechner.Controls.Add(this.LAB);
            this.tabFarbRechner.Controls.Add(this.LUV);
            this.tabFarbRechner.Controls.Add(this.HSL);
            this.tabFarbRechner.Controls.Add(this.radioButtonsRGB);
            this.tabFarbRechner.Controls.Add(this.HSV);
            this.tabFarbRechner.Controls.Add(this.customRGB);
            this.tabFarbRechner.Location = new System.Drawing.Point(4, 22);
            this.tabFarbRechner.Name = "tabFarbRechner";
            this.tabFarbRechner.Padding = new System.Windows.Forms.Padding(3);
            this.tabFarbRechner.Size = new System.Drawing.Size(445, 505);
            this.tabFarbRechner.TabIndex = 0;
            this.tabFarbRechner.Text = "Farbrechner";
            this.tabFarbRechner.UseVisualStyleBackColor = true;
            // 
            // label19
            // 
            this.label19.AutoSize = true;
            this.label19.Location = new System.Drawing.Point(343, 394);
            this.label19.Name = "label19";
            this.label19.Size = new System.Drawing.Size(16, 13);
            this.label19.TabIndex = 88;
            this.label19.Text = "b:";
            // 
            // label20
            // 
            this.label20.AutoSize = true;
            this.label20.Location = new System.Drawing.Point(243, 394);
            this.label20.Name = "label20";
            this.label20.Size = new System.Drawing.Size(16, 13);
            this.label20.TabIndex = 87;
            this.label20.Text = "a:";
            // 
            // label21
            // 
            this.label21.AutoSize = true;
            this.label21.Location = new System.Drawing.Point(143, 394);
            this.label21.Name = "label21";
            this.label21.Size = new System.Drawing.Size(16, 13);
            this.label21.TabIndex = 86;
            this.label21.Text = "L:";
            // 
            // LAB_B
            // 
            this.LAB_B.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LAB_B.Location = new System.Drawing.Point(363, 385);
            this.LAB_B.MaxLength = 5;
            this.LAB_B.Name = "LAB_B";
            this.LAB_B.ReadOnly = true;
            this.LAB_B.Size = new System.Drawing.Size(59, 26);
            this.LAB_B.TabIndex = 85;
            this.LAB_B.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LAB_B.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LAB_B.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LAB_B.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LAB_B.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // LAB_A
            // 
            this.LAB_A.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LAB_A.Location = new System.Drawing.Point(263, 385);
            this.LAB_A.MaxLength = 5;
            this.LAB_A.Name = "LAB_A";
            this.LAB_A.ReadOnly = true;
            this.LAB_A.Size = new System.Drawing.Size(59, 26);
            this.LAB_A.TabIndex = 84;
            this.LAB_A.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LAB_A.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LAB_A.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LAB_A.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LAB_A.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // LAB_L
            // 
            this.LAB_L.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LAB_L.Location = new System.Drawing.Point(163, 385);
            this.LAB_L.MaxLength = 5;
            this.LAB_L.Name = "LAB_L";
            this.LAB_L.ReadOnly = true;
            this.LAB_L.Size = new System.Drawing.Size(59, 26);
            this.LAB_L.TabIndex = 83;
            this.LAB_L.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LAB_L.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LAB_L.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LAB_L.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LAB_L.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // LUV_V
            // 
            this.LUV_V.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LUV_V.Location = new System.Drawing.Point(363, 325);
            this.LUV_V.MaxLength = 5;
            this.LUV_V.Name = "LUV_V";
            this.LUV_V.ReadOnly = true;
            this.LUV_V.Size = new System.Drawing.Size(59, 26);
            this.LUV_V.TabIndex = 79;
            this.LUV_V.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LUV_V.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LUV_V.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LUV_V.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LUV_V.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // LUV_U
            // 
            this.LUV_U.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LUV_U.Location = new System.Drawing.Point(263, 325);
            this.LUV_U.MaxLength = 5;
            this.LUV_U.Name = "LUV_U";
            this.LUV_U.ReadOnly = true;
            this.LUV_U.Size = new System.Drawing.Size(59, 26);
            this.LUV_U.TabIndex = 78;
            this.LUV_U.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LUV_U.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LUV_U.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LUV_U.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LUV_U.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // LUV_L
            // 
            this.LUV_L.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.LUV_L.Location = new System.Drawing.Point(163, 325);
            this.LUV_L.MaxLength = 5;
            this.LUV_L.Name = "LUV_L";
            this.LUV_L.ReadOnly = true;
            this.LUV_L.Size = new System.Drawing.Size(59, 26);
            this.LUV_L.TabIndex = 77;
            this.LUV_L.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.LUV_L.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.LUV_L.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.LUV_L.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.LUV_L.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // XYZ_Z
            // 
            this.XYZ_Z.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.XYZ_Z.Location = new System.Drawing.Point(363, 265);
            this.XYZ_Z.MaxLength = 5;
            this.XYZ_Z.Name = "XYZ_Z";
            this.XYZ_Z.ReadOnly = true;
            this.XYZ_Z.Size = new System.Drawing.Size(59, 26);
            this.XYZ_Z.TabIndex = 73;
            this.XYZ_Z.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.XYZ_Z.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.XYZ_Z.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.XYZ_Z.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.XYZ_Z.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // XYZ_Y
            // 
            this.XYZ_Y.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.XYZ_Y.Location = new System.Drawing.Point(263, 265);
            this.XYZ_Y.MaxLength = 5;
            this.XYZ_Y.Name = "XYZ_Y";
            this.XYZ_Y.ReadOnly = true;
            this.XYZ_Y.Size = new System.Drawing.Size(59, 26);
            this.XYZ_Y.TabIndex = 72;
            this.XYZ_Y.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.XYZ_Y.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.XYZ_Y.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.XYZ_Y.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.XYZ_Y.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // XYZ_X
            // 
            this.XYZ_X.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.XYZ_X.Location = new System.Drawing.Point(163, 265);
            this.XYZ_X.MaxLength = 5;
            this.XYZ_X.Name = "XYZ_X";
            this.XYZ_X.ReadOnly = true;
            this.XYZ_X.Size = new System.Drawing.Size(59, 26);
            this.XYZ_X.TabIndex = 71;
            this.XYZ_X.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.XYZ_X.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.XYZ_X.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.XYZ_X.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.XYZ_X.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSV_V
            // 
            this.HSV_V.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSV_V.Location = new System.Drawing.Point(363, 205);
            this.HSV_V.MaxLength = 5;
            this.HSV_V.Name = "HSV_V";
            this.HSV_V.ReadOnly = true;
            this.HSV_V.Size = new System.Drawing.Size(59, 26);
            this.HSV_V.TabIndex = 67;
            this.HSV_V.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSV_V.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSV_V.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSV_V.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSV_V.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSV_S
            // 
            this.HSV_S.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSV_S.Location = new System.Drawing.Point(263, 205);
            this.HSV_S.MaxLength = 5;
            this.HSV_S.Name = "HSV_S";
            this.HSV_S.ReadOnly = true;
            this.HSV_S.Size = new System.Drawing.Size(59, 26);
            this.HSV_S.TabIndex = 66;
            this.HSV_S.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSV_S.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSV_S.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSV_S.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSV_S.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSV_H
            // 
            this.HSV_H.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSV_H.Location = new System.Drawing.Point(163, 205);
            this.HSV_H.MaxLength = 5;
            this.HSV_H.Name = "HSV_H";
            this.HSV_H.ReadOnly = true;
            this.HSV_H.Size = new System.Drawing.Size(59, 26);
            this.HSV_H.TabIndex = 65;
            this.HSV_H.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSV_H.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSV_H.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSV_H.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSV_H.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSL_L
            // 
            this.HSL_L.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSL_L.Location = new System.Drawing.Point(363, 145);
            this.HSL_L.MaxLength = 5;
            this.HSL_L.Name = "HSL_L";
            this.HSL_L.ReadOnly = true;
            this.HSL_L.Size = new System.Drawing.Size(59, 26);
            this.HSL_L.TabIndex = 61;
            this.HSL_L.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSL_L.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSL_L.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSL_L.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSL_L.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSL_S
            // 
            this.HSL_S.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSL_S.Location = new System.Drawing.Point(263, 145);
            this.HSL_S.MaxLength = 5;
            this.HSL_S.Name = "HSL_S";
            this.HSL_S.ReadOnly = true;
            this.HSL_S.Size = new System.Drawing.Size(59, 26);
            this.HSL_S.TabIndex = 60;
            this.HSL_S.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSL_S.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSL_S.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSL_S.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSL_S.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // HSL_H
            // 
            this.HSL_H.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.HSL_H.Location = new System.Drawing.Point(163, 145);
            this.HSL_H.MaxLength = 5;
            this.HSL_H.Name = "HSL_H";
            this.HSL_H.ReadOnly = true;
            this.HSL_H.Size = new System.Drawing.Size(59, 26);
            this.HSL_H.TabIndex = 59;
            this.HSL_H.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.HSL_H.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.HSL_H.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.HSL_H.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.HSL_H.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // sRGB_B
            // 
            this.sRGB_B.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.sRGB_B.Location = new System.Drawing.Point(363, 85);
            this.sRGB_B.MaxLength = 5;
            this.sRGB_B.Name = "sRGB_B";
            this.sRGB_B.ReadOnly = true;
            this.sRGB_B.Size = new System.Drawing.Size(59, 26);
            this.sRGB_B.TabIndex = 55;
            this.sRGB_B.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.sRGB_B.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.sRGB_B.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.sRGB_B.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.sRGB_B.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // sRGB_G
            // 
            this.sRGB_G.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.sRGB_G.Location = new System.Drawing.Point(263, 85);
            this.sRGB_G.MaxLength = 5;
            this.sRGB_G.Name = "sRGB_G";
            this.sRGB_G.ReadOnly = true;
            this.sRGB_G.Size = new System.Drawing.Size(59, 26);
            this.sRGB_G.TabIndex = 54;
            this.sRGB_G.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.sRGB_G.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.sRGB_G.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.sRGB_G.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.sRGB_G.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // sRGB_R
            // 
            this.sRGB_R.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.sRGB_R.Location = new System.Drawing.Point(163, 85);
            this.sRGB_R.MaxLength = 5;
            this.sRGB_R.Name = "sRGB_R";
            this.sRGB_R.ReadOnly = true;
            this.sRGB_R.Size = new System.Drawing.Size(59, 26);
            this.sRGB_R.TabIndex = 53;
            this.sRGB_R.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.sRGB_R.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.sRGB_R.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.sRGB_R.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.sRGB_R.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // customRGB_B
            // 
            this.customRGB_B.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.customRGB_B.Location = new System.Drawing.Point(363, 25);
            this.customRGB_B.MaxLength = 5;
            this.customRGB_B.Name = "customRGB_B";
            this.customRGB_B.ReadOnly = true;
            this.customRGB_B.Size = new System.Drawing.Size(59, 26);
            this.customRGB_B.TabIndex = 32;
            this.customRGB_B.Text = "0";
            this.customRGB_B.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.customRGB_B.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.customRGB_B.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.customRGB_B.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.customRGB_B.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // customRGB_G
            // 
            this.customRGB_G.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.customRGB_G.Location = new System.Drawing.Point(263, 25);
            this.customRGB_G.MaxLength = 5;
            this.customRGB_G.Name = "customRGB_G";
            this.customRGB_G.ReadOnly = true;
            this.customRGB_G.Size = new System.Drawing.Size(59, 26);
            this.customRGB_G.TabIndex = 31;
            this.customRGB_G.Text = "0";
            this.customRGB_G.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.customRGB_G.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.customRGB_G.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.customRGB_G.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.customRGB_G.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // customRGB_R
            // 
            this.customRGB_R.Font = new System.Drawing.Font("Microsoft Sans Serif", 12.25F);
            this.customRGB_R.Location = new System.Drawing.Point(163, 25);
            this.customRGB_R.MaxLength = 5;
            this.customRGB_R.Name = "customRGB_R";
            this.customRGB_R.ReadOnly = true;
            this.customRGB_R.Size = new System.Drawing.Size(59, 26);
            this.customRGB_R.TabIndex = 30;
            this.customRGB_R.Text = "0";
            this.customRGB_R.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            this.customRGB_R.ReadOnlyChanged += new System.EventHandler(this.Input_ReadOnlyChanged);
            this.customRGB_R.TextChanged += new System.EventHandler(this.Input_TextChanged);
            this.customRGB_R.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBox_OnReturn);
            this.customRGB_R.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.Input_KeyPress);
            // 
            // label16
            // 
            this.label16.AutoSize = true;
            this.label16.Location = new System.Drawing.Point(343, 334);
            this.label16.Name = "label16";
            this.label16.Size = new System.Drawing.Size(16, 13);
            this.label16.TabIndex = 82;
            this.label16.Text = "v:";
            // 
            // label17
            // 
            this.label17.AutoSize = true;
            this.label17.Location = new System.Drawing.Point(243, 334);
            this.label17.Name = "label17";
            this.label17.Size = new System.Drawing.Size(16, 13);
            this.label17.TabIndex = 81;
            this.label17.Text = "u:";
            // 
            // label18
            // 
            this.label18.AutoSize = true;
            this.label18.Location = new System.Drawing.Point(143, 334);
            this.label18.Name = "label18";
            this.label18.Size = new System.Drawing.Size(16, 13);
            this.label18.TabIndex = 80;
            this.label18.Text = "L:";
            // 
            // label13
            // 
            this.label13.AutoSize = true;
            this.label13.Location = new System.Drawing.Point(343, 274);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(17, 13);
            this.label13.TabIndex = 76;
            this.label13.Text = "Z:";
            // 
            // label14
            // 
            this.label14.AutoSize = true;
            this.label14.Location = new System.Drawing.Point(243, 274);
            this.label14.Name = "label14";
            this.label14.Size = new System.Drawing.Size(17, 13);
            this.label14.TabIndex = 75;
            this.label14.Text = "Y:";
            // 
            // label15
            // 
            this.label15.AutoSize = true;
            this.label15.Location = new System.Drawing.Point(143, 274);
            this.label15.Name = "label15";
            this.label15.Size = new System.Drawing.Size(17, 13);
            this.label15.TabIndex = 74;
            this.label15.Text = "X:";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(343, 214);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(17, 13);
            this.label7.TabIndex = 70;
            this.label7.Text = "V:";
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(243, 214);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(17, 13);
            this.label8.TabIndex = 69;
            this.label8.Text = "S:";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(143, 214);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(18, 13);
            this.label9.TabIndex = 68;
            this.label9.Text = "H:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(343, 154);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(16, 13);
            this.label4.TabIndex = 64;
            this.label4.Text = "L:";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(243, 154);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(17, 13);
            this.label5.TabIndex = 63;
            this.label5.Text = "S:";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(143, 154);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(18, 13);
            this.label6.TabIndex = 62;
            this.label6.Text = "H:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(343, 94);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(17, 13);
            this.label1.TabIndex = 58;
            this.label1.Text = "B:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(243, 94);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(18, 13);
            this.label2.TabIndex = 57;
            this.label2.Text = "G:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(143, 94);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(18, 13);
            this.label3.TabIndex = 56;
            this.label3.Text = "R:";
            // 
            // InfoLabel
            // 
            this.InfoLabel.AutoSize = true;
            this.InfoLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.25F);
            this.InfoLabel.Location = new System.Drawing.Point(111, -24);
            this.InfoLabel.Name = "InfoLabel";
            this.InfoLabel.Size = new System.Drawing.Size(316, 17);
            this.InfoLabel.TabIndex = 52;
            this.InfoLabel.Text = "R, G, B, X, Y, Z, S, L = [0, 1]           H = [0°, 360°]";
            // 
            // ResetButton
            // 
            this.ResetButton.Location = new System.Drawing.Point(246, 432);
            this.ResetButton.Name = "ResetButton";
            this.ResetButton.Size = new System.Drawing.Size(105, 46);
            this.ResetButton.TabIndex = 13;
            this.ResetButton.Text = "Reset";
            this.ResetButton.UseVisualStyleBackColor = true;
            this.ResetButton.Click += new System.EventHandler(this.ResetButton_Click);
            // 
            // KonvertierenButton
            // 
            this.KonvertierenButton.Location = new System.Drawing.Point(103, 432);
            this.KonvertierenButton.Name = "KonvertierenButton";
            this.KonvertierenButton.Size = new System.Drawing.Size(105, 46);
            this.KonvertierenButton.TabIndex = 12;
            this.KonvertierenButton.Text = "Convert";
            this.KonvertierenButton.UseVisualStyleBackColor = true;
            this.KonvertierenButton.Click += new System.EventHandler(this.KonvertierenButton_Click);
            // 
            // XYZ
            // 
            this.XYZ.AutoSize = true;
            this.XYZ.Location = new System.Drawing.Point(32, 272);
            this.XYZ.Name = "XYZ";
            this.XYZ.Size = new System.Drawing.Size(66, 17);
            this.XYZ.TabIndex = 42;
            this.XYZ.TabStop = true;
            this.XYZ.Text = "CIE XYZ";
            this.XYZ.UseVisualStyleBackColor = true;
            this.XYZ.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // LAB
            // 
            this.LAB.AutoSize = true;
            this.LAB.Location = new System.Drawing.Point(32, 392);
            this.LAB.Name = "LAB";
            this.LAB.Size = new System.Drawing.Size(75, 17);
            this.LAB.TabIndex = 41;
            this.LAB.TabStop = true;
            this.LAB.Text = "CIE L*a*b*";
            this.LAB.UseVisualStyleBackColor = true;
            this.LAB.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // LUV
            // 
            this.LUV.AutoSize = true;
            this.LUV.Location = new System.Drawing.Point(32, 332);
            this.LUV.Name = "LUV";
            this.LUV.Size = new System.Drawing.Size(75, 17);
            this.LUV.TabIndex = 40;
            this.LUV.TabStop = true;
            this.LUV.Text = "CIE L*u*v*";
            this.LUV.UseVisualStyleBackColor = true;
            this.LUV.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // HSL
            // 
            this.HSL.AutoSize = true;
            this.HSL.Location = new System.Drawing.Point(32, 152);
            this.HSL.Name = "HSL";
            this.HSL.Size = new System.Drawing.Size(46, 17);
            this.HSL.TabIndex = 39;
            this.HSL.TabStop = true;
            this.HSL.Text = "HSL";
            this.HSL.UseVisualStyleBackColor = true;
            this.HSL.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // radioButtonsRGB
            // 
            this.radioButtonsRGB.AutoSize = true;
            this.radioButtonsRGB.Location = new System.Drawing.Point(32, 92);
            this.radioButtonsRGB.Name = "radioButtonsRGB";
            this.radioButtonsRGB.Size = new System.Drawing.Size(53, 17);
            this.radioButtonsRGB.TabIndex = 38;
            this.radioButtonsRGB.TabStop = true;
            this.radioButtonsRGB.Text = "sRGB";
            this.radioButtonsRGB.UseVisualStyleBackColor = true;
            this.radioButtonsRGB.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // HSV
            // 
            this.HSV.AutoSize = true;
            this.HSV.Location = new System.Drawing.Point(32, 212);
            this.HSV.Name = "HSV";
            this.HSV.Size = new System.Drawing.Size(47, 17);
            this.HSV.TabIndex = 37;
            this.HSV.TabStop = true;
            this.HSV.Text = "HSV";
            this.HSV.UseVisualStyleBackColor = true;
            this.HSV.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // customRGB
            // 
            this.customRGB.AutoSize = true;
            this.customRGB.Location = new System.Drawing.Point(32, 32);
            this.customRGB.Name = "customRGB";
            this.customRGB.Size = new System.Drawing.Size(85, 17);
            this.customRGB.TabIndex = 33;
            this.customRGB.TabStop = true;
            this.customRGB.Text = "custom RGB";
            this.customRGB.UseVisualStyleBackColor = true;
            this.customRGB.CheckedChanged += new System.EventHandler(this.radioButton_FarbRechner_CheckedChanged);
            // 
            // tabControlFarbRechner
            // 
            this.tabControlFarbRechner.Controls.Add(this.tabFarbRechner);
            this.tabControlFarbRechner.Location = new System.Drawing.Point(779, 12);
            this.tabControlFarbRechner.Name = "tabControlFarbRechner";
            this.tabControlFarbRechner.SelectedIndex = 0;
            this.tabControlFarbRechner.Size = new System.Drawing.Size(453, 531);
            this.tabControlFarbRechner.TabIndex = 31;
            // 
            // labelColPreviewL
            // 
            this.labelColPreviewL.AutoSize = true;
            this.labelColPreviewL.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColPreviewL.Location = new System.Drawing.Point(648, 10);
            this.labelColPreviewL.Name = "labelColPreviewL";
            this.labelColPreviewL.Size = new System.Drawing.Size(28, 16);
            this.labelColPreviewL.TabIndex = 82;
            this.labelColPreviewL.Text = "L: 0";
            this.labelColPreviewL.Visible = false;
            // 
            // labelColSelectedL
            // 
            this.labelColSelectedL.AutoSize = true;
            this.labelColSelectedL.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelColSelectedL.Location = new System.Drawing.Point(648, 51);
            this.labelColSelectedL.Name = "labelColSelectedL";
            this.labelColSelectedL.Size = new System.Drawing.Size(28, 16);
            this.labelColSelectedL.TabIndex = 85;
            this.labelColSelectedL.Text = "L: 0";
            this.labelColSelectedL.Visible = false;
            // 
            // buttonClearEllipses
            // 
            this.buttonClearEllipses.Enabled = false;
            this.buttonClearEllipses.Location = new System.Drawing.Point(424, 517);
            this.buttonClearEllipses.Name = "buttonClearEllipses";
            this.buttonClearEllipses.Size = new System.Drawing.Size(90, 25);
            this.buttonClearEllipses.TabIndex = 86;
            this.buttonClearEllipses.Text = "Clear Ellipses";
            this.buttonClearEllipses.UseVisualStyleBackColor = true;
            this.buttonClearEllipses.Click += new System.EventHandler(this.ButtonClearEllipses_Click);
            // 
            // labelGuide
            // 
            this.labelGuide.AutoSize = true;
            this.labelGuide.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelGuide.Location = new System.Drawing.Point(175, 517);
            this.labelGuide.Name = "labelGuide";
            this.labelGuide.Size = new System.Drawing.Size(148, 26);
            this.labelGuide.TabIndex = 87;
            this.labelGuide.Text = "Left Click:      select Color \nRight Click:    add JND Ellipse";
            // 
            // OpenGLForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.Azure;
            this.ClientSize = new System.Drawing.Size(1241, 551);
            this.Controls.Add(this.labelGuide);
            this.Controls.Add(this.buttonClearEllipses);
            this.Controls.Add(this.labelColSelectedL);
            this.Controls.Add(this.labelColPreviewL);
            this.Controls.Add(this.buttonAddEllipse);
            this.Controls.Add(this.labelZ);
            this.Controls.Add(this.labelY);
            this.Controls.Add(this.labelX);
            this.Controls.Add(this.labelBBZValue);
            this.Controls.Add(this.labelBBYValue);
            this.Controls.Add(this.labelBBXValue);
            this.Controls.Add(this.labelGGZValue);
            this.Controls.Add(this.labelGGYValue);
            this.Controls.Add(this.labelGGXValue);
            this.Controls.Add(this.labelRRZValue);
            this.Controls.Add(this.labelRRYValue);
            this.Controls.Add(this.labelRRXValue);
            this.Controls.Add(this.labelWPZValue);
            this.Controls.Add(this.labelWPYValue);
            this.Controls.Add(this.labelColorSelectedHex);
            this.Controls.Add(this.labelColorPreviewHex);
            this.Controls.Add(this.labelColSelectedYS);
            this.Controls.Add(this.labelColSelectedXH);
            this.Controls.Add(this.labelColPreviewYS);
            this.Controls.Add(this.panelColorPreview);
            this.Controls.Add(this.labelColPreviewXH);
            this.Controls.Add(this.buttonBBReset);
            this.Controls.Add(this.buttonBBSet);
            this.Controls.Add(this.buttonGGReset);
            this.Controls.Add(this.buttonGGSet);
            this.Controls.Add(this.buttonRRReset);
            this.Controls.Add(this.buttonRRSet);
            this.Controls.Add(this.tabControlColorSlider);
            this.Controls.Add(this.radioButtonHSLView);
            this.Controls.Add(this.radioButtonXYZView);
            this.Controls.Add(this.buttonWPReset);
            this.Controls.Add(this.buttonWPSet);
            this.Controls.Add(this.labelBB);
            this.Controls.Add(this.labelGG);
            this.Controls.Add(this.labelRR);
            this.Controls.Add(this.labelWP);
            this.Controls.Add(this.labelWPXValue);
            this.Controls.Add(this.panelColorSelected);
            this.Controls.Add(this.tabControlFarbRechner);
            this.Controls.Add(this.glControl1);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F);
            this.ForeColor = System.Drawing.SystemColors.ControlText;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.Name = "OpenGLForm";
            this.Text = "OpenGL - FarbRechner";
            this.tabControlColorSlider.ResumeLayout(false);
            this.tabRGB.ResumeLayout(false);
            this.tabRGB.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarB)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarG)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarR)).EndInit();
            this.tabHSV.ResumeLayout(false);
            this.tabHSV.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarL)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarS)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarH)).EndInit();
            this.tabFarbRechner.ResumeLayout(false);
            this.tabFarbRechner.PerformLayout();
            this.tabControlFarbRechner.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        void OpenGLForm_FormClosed(object sender, System.Windows.Forms.FormClosedEventArgs e)
        {
            this.DisposeOpenGL();
        }

        #endregion

        private OpenTK.GLControl glControl1;
        private System.Windows.Forms.Panel panelColorSelected;
        private System.Windows.Forms.Label labelWP;
        private System.Windows.Forms.Label labelWPXValue;
        private System.Windows.Forms.Label labelRR;
        private System.Windows.Forms.Label labelGG;
        private System.Windows.Forms.Label labelBB;
        private System.Windows.Forms.Button buttonWPSet;
        private System.Windows.Forms.Button buttonWPReset;
        private System.Windows.Forms.RadioButton radioButtonXYZView;
        private System.Windows.Forms.RadioButton radioButtonHSLView;
        private System.Windows.Forms.TabControl tabControlColorSlider;
        private System.Windows.Forms.TabPage tabRGB;
        private System.Windows.Forms.TabPage tabHSV;
        private System.Windows.Forms.Button buttonRRReset;
        private System.Windows.Forms.Button buttonRRSet;
        private System.Windows.Forms.Button buttonGGReset;
        private System.Windows.Forms.Button buttonGGSet;
        private System.Windows.Forms.Button buttonBBReset;
        private System.Windows.Forms.Button buttonBBSet;
        private System.Windows.Forms.Label labelTabRGB_G;
        private System.Windows.Forms.TrackBar trackBarG;
        private System.Windows.Forms.Label labelTabRGB_R;
        private System.Windows.Forms.Label labelTabRGB_B;
        private System.Windows.Forms.TrackBar trackBarB;
        private System.Windows.Forms.Label labelTabHSL_L;
        private System.Windows.Forms.TrackBar trackBarL;
        private System.Windows.Forms.Label labelTabHSL_S;
        private System.Windows.Forms.TrackBar trackBarS;
        private System.Windows.Forms.Label labelTabHSL_H;
        private System.Windows.Forms.TrackBar trackBarH;
        private System.Windows.Forms.Panel panelColorPreview;
        private System.Windows.Forms.Label labelColPreviewXH;
        private System.Windows.Forms.CheckBox checkBoxRGB;
        private System.Windows.Forms.CheckBox checkBoxHSL;
        private System.Windows.Forms.Label labelColPreviewYS;
        private System.Windows.Forms.Label labelColSelectedYS;
        private System.Windows.Forms.Label labelColSelectedXH;
        private System.Windows.Forms.Label labelColorPreviewHex;
        private System.Windows.Forms.Label labelColorSelectedHex;
        private System.Windows.Forms.Label labelWPYValue;
        private System.Windows.Forms.Label labelWPZValue;
        private System.Windows.Forms.Label labelRRZValue;
        private System.Windows.Forms.Label labelRRYValue;
        private System.Windows.Forms.Label labelRRXValue;
        private System.Windows.Forms.Label labelGGZValue;
        private System.Windows.Forms.Label labelGGYValue;
        private System.Windows.Forms.Label labelGGXValue;
        private System.Windows.Forms.Label labelBBZValue;
        private System.Windows.Forms.Label labelBBYValue;
        private System.Windows.Forms.Label labelBBXValue;
        private System.Windows.Forms.Label labelX;
        private System.Windows.Forms.Label labelY;
        private System.Windows.Forms.Label labelZ;
        private System.Windows.Forms.TrackBar trackBarR;
        private System.Windows.Forms.Button buttonAddEllipse;
        private System.Windows.Forms.TabPage tabFarbRechner;
        private System.Windows.Forms.Label label19;
        private System.Windows.Forms.Label label20;
        private System.Windows.Forms.Label label21;
        private System.Windows.Forms.TextBox LAB_B;
        private System.Windows.Forms.TextBox LAB_A;
        private System.Windows.Forms.TextBox LAB_L;
        private System.Windows.Forms.TextBox LUV_V;
        private System.Windows.Forms.TextBox LUV_U;
        private System.Windows.Forms.TextBox LUV_L;
        private System.Windows.Forms.TextBox XYZ_Z;
        private System.Windows.Forms.TextBox XYZ_Y;
        private System.Windows.Forms.TextBox XYZ_X;
        private System.Windows.Forms.TextBox HSV_V;
        private System.Windows.Forms.TextBox HSV_S;
        private System.Windows.Forms.TextBox HSV_H;
        private System.Windows.Forms.TextBox HSL_L;
        private System.Windows.Forms.TextBox HSL_S;
        private System.Windows.Forms.TextBox HSL_H;
        private System.Windows.Forms.TextBox sRGB_B;
        private System.Windows.Forms.TextBox sRGB_G;
        private System.Windows.Forms.TextBox sRGB_R;
        private System.Windows.Forms.TextBox customRGB_B;
        private System.Windows.Forms.TextBox customRGB_G;
        private System.Windows.Forms.TextBox customRGB_R;
        private System.Windows.Forms.Label label16;
        private System.Windows.Forms.Label label17;
        private System.Windows.Forms.Label label18;
        private System.Windows.Forms.Label label13;
        private System.Windows.Forms.Label label14;
        private System.Windows.Forms.Label label15;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label InfoLabel;
        private System.Windows.Forms.Button ResetButton;
        private System.Windows.Forms.Button KonvertierenButton;
        private System.Windows.Forms.RadioButton XYZ;
        private System.Windows.Forms.RadioButton LAB;
        private System.Windows.Forms.RadioButton LUV;
        private System.Windows.Forms.RadioButton HSL;
        private System.Windows.Forms.RadioButton radioButtonsRGB;
        private System.Windows.Forms.RadioButton HSV;
        private System.Windows.Forms.RadioButton customRGB;
        private System.Windows.Forms.TabControl tabControlFarbRechner;
        private System.Windows.Forms.Label labelColPreviewL;
        private System.Windows.Forms.Label labelColSelectedL;
        private System.Windows.Forms.Button buttonClearEllipses;
        private System.Windows.Forms.Label labelGuide;

    }
}

