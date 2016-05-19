package ihm;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar pbar;
	
	public static final int MY_MINIMUM = 0;
	
	public static final int MY_MAXIMUM = 100;
	
	public ProgressBarFrame() {
		
		super ("Création du pdf");
		
		JPanel progressBarPanel = new JPanel();
		
	    // initialize Progress Bar
		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
		pbar.setStringPainted(true);
		
		// add to JPanel
		progressBarPanel.add(pbar);
		
		this.setContentPane(progressBarPanel);
		this.pack();
		this.setLocationRelativeTo(null); //position de la fenetre
		this.setAutoRequestFocus(true);
		this.setVisible(true);
		this.toFront();
	}
	
	public void updateBar(int newValue) {
		pbar.setValue(newValue);
	}
	
	public int getProgress() {
		return  pbar.getValue();
	}
}