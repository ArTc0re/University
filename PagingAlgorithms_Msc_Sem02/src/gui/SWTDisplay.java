package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class SWTDisplay {

	private Shell shell;
	
	/**
	 * Initial Display
	 */
	public SWTDisplay(){		
		Display display = new Display();
		shell = new Shell(display);
		shell.setText("Paging Simulator");
		shell.setSize(700, 500);
		center(shell);
		
		//Initial Buttons
		initButton();
		 
		
		//shell.pack(); //auto scale
		shell.open();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}	
	
	/**
	 * Center the Display on Screen
	 * @param shell
	 */
	public void center(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
	
	/**
	 * create Buttons
	 */
	public void initButton(){
		
		//init Start Button
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Start Simulation");
		button.setBounds(50,50,100,30);
		
	    button.addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event) {
	        	//TODO
	    	}
	    });
				 
		
		
	}
	
	
}
