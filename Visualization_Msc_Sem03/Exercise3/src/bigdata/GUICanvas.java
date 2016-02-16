package bigdata;


import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * This class was created thanks to Max Help in Moodle Forum
 * @author Justine Smyzek
 */
public class GUICanvas extends JFrame {

	
	private Canvas canvas;
	private int width;
	private int height;
	private JPanel controls;
	private JPanel infos;
	private JPanel status;	
	
	// Size of boxes
	private int infoBOX_X = 150;
	private int infoBOX_Y = 30;
	private int controlBOX_X = 230;
	private int controlBOX_Y = 510;
	
	//Info Strings
	private ArrayList<String> infoStrings = new ArrayList<String>();
	
	public GUICanvas(String title, int width, int height){
		this.setTitle(title);
		this.width = width;
		this.height = height;
		
		//insert Text
		this.infoStrings.add("Control KeyBindings");
		this.infoStrings.add("KEY [W] Translate UP");
		this.infoStrings.add("KEY [S] Translate DOWN");
		this.infoStrings.add("KEY [A] Translate LEFT");
		this.infoStrings.add("KEY [D] Translate RIGHT");
		this.infoStrings.add("KEY [Q] Rotate LEFT");
		this.infoStrings.add("KEY [E] Rotate RIGHT");
		this.infoStrings.add("KEY [SPACE] Reset Position");
		this.infoStrings.add("MOUSE [LEFT] Rotate");
		this.infoStrings.add("MOUSE [WHEEL] ZOOM");
		this.infoStrings.add("                       ");
		
		this.infoStrings.add("Octree Visualization");
		this.infoStrings.add("KEY [O] Toggle OctreeModes");
		this.infoStrings.add("KEY [P] Distribution ON/OFF");
		this.infoStrings.add("KEY [L] Show as LINE/FILL");
		this.infoStrings.add("KEY [I] BOX Increase depth");
		this.infoStrings.add("KEY [K] BOX Decrease depth");	
		this.infoStrings.add("                       ");
		
		this.infoStrings.add("PointCloud & Splats");
		this.infoStrings.add("KEY [T] Points/Splats/None");
		this.infoStrings.add("KEY [Z] Circle/Rectangle Splats");
		this.infoStrings.add("KEY [U] Increase Splat Size");
		this.infoStrings.add("KEY [J] Decrease Splat Size");
		
		
		initGUI();
		
	}
	
	/**
	 * Initzial GUI
	 */
	private void initGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		FlowLayout flayout = new FlowLayout();
		flayout.setAlignment(FlowLayout.LEFT);
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		setLayout(flayout);
		
		
		//JPAnel layout & Text
		controls = new JPanel(flayout);
		
		for (int i = 0; i < this.infoStrings.size(); i++) {
			String current = this.infoStrings.get(i);
			JLabel tmplabel = new JLabel(current);
			Font tmpfont = new Font("Consolas", Font.PLAIN, 12);
			tmplabel.setForeground(Color.orange);
			
			//titles
			if (i == 0 || i == 11 || i == 18) {
				tmpfont = new Font("Consolas", Font.BOLD, 16);
				tmplabel.setForeground(Color.green);
			}
			
			tmplabel.setFont(tmpfont);
			controls.add(tmplabel);
		}
		
		
		infos = new JPanel(layout);
		JLabel infolabel = new JLabel("[TAB] Show/Hide GUI");
		Font infofont = new Font("Consolas", Font.BOLD, 12);
		infolabel.setForeground(Color.GRAY);
		infolabel.setFont(infofont);
		infos.add(infolabel, gbc);
		
		status = new JPanel(layout);
		JLabel statuslable = new JLabel("Mode: Standard   Depth: 0/0 ");
		Font statusfont = new Font("Consolas", Font.BOLD, 12);
		statuslable.setForeground(Color.GRAY);
		statuslable.setFont(statusfont);
		status.add(statuslable);

		// Set some background		
		controls.setBackground(Color.darkGray);
		infos.setBackground(Color.BLACK);
		status.setBackground(Color.BLACK);
		
		// initially not visible
		status.setVisible(false);
		
		infos.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, false));
		controls.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1, false));
		status.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, false));
		
		
		// Set some tooltip text
		infos.setBounds(0 + 2, 0, this.infoBOX_X, this.infoBOX_Y);
		controls.setBounds(0 + 2, this.infoBOX_Y + 2 + 2, this.controlBOX_X, this.controlBOX_Y);		
		status.setBounds(this.height - 50, 0, 300, 25);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(this.width, this.height));
		
		canvas = new Canvas();
		canvas.setSize(this.width, this.height);
		
		
		layeredPane.add(canvas);
		

		layeredPane.add(controls);
		layeredPane.add(infos);
		layeredPane.add(status);
		
		layeredPane.moveToFront(controls);
		layeredPane.moveToFront(infos);
		layeredPane.moveToFront(status);
		
		layeredPane.setVisible(true);
		add(layeredPane);

		
		setSize(this.width, this.height);
		
		setVisible(true);

		// make the content pane disappear
		this.getContentPane().setBackground(Color.BLACK);
		
		// make sure only very little extra space is used
		pack();
		
	}

	/**
	 * 
	 * @return
	 */
	public Canvas getCanvas() {
		return this.canvas;
	}
	
	
	/**
	 * setCurrentMode
	 */
	public void setCurrentMode(int mode, int currentDepth, int maxDepth, boolean colored){
		Component[] c = status.getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JLabel) {

				if(mode == 0){
					status.setVisible(false);
					//						((JLabel) c[i]).getText().contains("Mode:");
					//						((JLabel) c[i]).setText("Mode: Standard   Depth:" + currentDepth + "/" + maxDepth);
				}else if(mode == 1){
					((JLabel) c[i]).getText().contains("Mode ");
					((JLabel) c[i]).setText("Mode: Box Tree   Depth:" + currentDepth + "/" + maxDepth + "  Color: " + colored);
					status.setVisible(true);
				}else if(mode == 2){
					((JLabel) c[i]).getText().contains("Mode ");
					((JLabel) c[i]).setText("Mode: Box Octree   Depth:" + currentDepth + "/" + maxDepth + " Color: " + colored);
					status.setVisible(true);
				}
			}
		}
	}

	
	/**
	 * Show/Hide Gui Keybinds
	 */
	public void toggleGUI() {
		if (this.controls.isVisible()) {
			this.controls.setVisible(false);

		} else {
			this.controls.setVisible(true);
		}
	}
	
}
